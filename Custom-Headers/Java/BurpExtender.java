package burp;

import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.net.URL;

public class BurpExtender implements IBurpExtender, IHttpListener {
    private static IBurpExtenderCallbacks callbacks;
	  private IExtensionHelpers helpers;
    private PrintWriter stdout;

    // Change URL(/s) in urlMatchList to match your scope. If empty, headers in headersToAdd are added to all request URL(/s). Excluding protocol also works e.g. portswigger-labs.net
    private String[] urlMatchList = {"https://portswigger-labs.net"};
    // Adjust key:value pairs in headersToAdd as appropriate
    private String[] headersToAdd = {"Accept-Encoding: gzip, deflateX"};
    // Toggle checkForDuplicates between false and true if you want to remove any duplicated headers
    private boolean checkForDuplicates = false;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks){
      // Initialize extension
      BurpExtender.callbacks = callbacks;
      stdout = new PrintWriter(callbacks.getStdout(), true);
      helpers = callbacks.getHelpers();
      callbacks.setExtensionName("Add-Headers-Java");
      callbacks.registerHttpListener(this);
    }

    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
      // Only edit requests
      if(messageIsRequest) {
        // Get details of request
        IRequestInfo rqInfo = helpers.analyzeRequest(messageInfo);
        URL url = rqInfo.getUrl();
        String urlString = url.getProtocol()+"://"+url.getHost()+url.getPath();
        // if the urlMatchList is empty, add headers to all requests
        if (urlMatchList.length == 0) {
          addHeaders(messageInfo, rqInfo);
        }
        else {
          // Iterate through URLs in urlMatchList
          for (String match : urlMatchList) {
            // If a URL contains a match to the urlMatchList, run the addHeaders function
            if (urlString.contains(match)) {
              addHeaders(messageInfo, rqInfo);
              // Once a match has been found, don't continue to check the rest of the list
              break;
            }
          }
        }
      }
    }

    private void addHeaders(IHttpRequestResponse messageInfo, IRequestInfo rqInfo) {
      // Get list of headers in request
      List<String> headers = new ArrayList<String>(rqInfo.getHeaders());
      // If checkForDuplicates is set to true, then check that a header has not been duplicated by the headersToAdd. If it has, remove original header
      if (checkForDuplicates) {
        for (String newHeader : headersToAdd) {
          // Split key:value pair up to just the key
          String[] key = newHeader.split(":");
          // Make an empty list to record which headers will need to be removed
          List<String> headersToRemove = new ArrayList<String>();
          for (String oldHeader : headers) {
            // If a header matches with the key, add it to the headersToRemove list
            if (oldHeader.startsWith(key[0])) {
              headersToRemove.add(oldHeader);
            }
          }
          // Remove all duplicated headers
          headers.removeAll(headersToRemove);
        }
      }
      // Iterate through the headersToAdd list, and add all new headers to the request
      for (String newHeader : headersToAdd) {
        headers.add(newHeader);
      }
      // Build new request
      String request = new String(messageInfo.getRequest());
      String messageBody = request.substring(rqInfo.getBodyOffset());
      byte[] updateMessage = helpers.buildHttpMessage(headers, messageBody.getBytes());
      // Update existing request with the new request
      messageInfo.setRequest(updateMessage);
    }
}
