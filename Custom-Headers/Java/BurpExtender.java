package burp;

import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.net.URL;

public class BurpExtender implements IBurpExtender, IHttpListener {
    private static IBurpExtenderCallbacks callbacks;
	  private IExtensionHelpers helpers;
    private PrintWriter stdout;

    private String[] urlMatchList = {"https://portswigger-labs.net"};                                  /* change URL(/s) in urlMatchList to match your scope. If empty, headers in headersToAdd are added to all requests 
                                                                                                                URL(/s) without protocol also works e.g. portswigger-labs.net */
    private String[] headersToAdd = {"Header1: value1", "Header2: value2"};                            // adjust key:value pairs in headersToAdd as appropriate

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks){
      BurpExtender.callbacks = callbacks;
      stdout = new PrintWriter(callbacks.getStdout(), true);
      helpers = callbacks.getHelpers();
      callbacks.setExtensionName("Add-Headers-Java");
      callbacks.registerHttpListener(this);
    }
  
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
        if(messageIsRequest) {
          IRequestInfo rqInfo = helpers.analyzeRequest(messageInfo);
          URL url = rqInfo.getUrl();
          String urlString = url.getProtocol()+"://"+url.getHost()+url.getPath(); 
          if (urlMatchList.length == 0) {                                                               // if the urlMatchList is empty, add headers to all requests 
            addHeaders(messageInfo, rqInfo);
          }
          else {
            for (int i=0;i<urlMatchList.length; i++) {                                                  // iterate through the URLs in the urlMatchList array in a loop
              if (urlString.contains(urlMatchList[i])) {                                                // check if the request URL is in the urlMatchList
                addHeaders(messageInfo, rqInfo);                                                       
                break;
              }
            }
          }
        }
    }

    private void addHeaders(IHttpRequestResponse messageInfo, IRequestInfo rqInfo) {
      List<String> headers = new ArrayList<String>(rqInfo.getHeaders());                                // TO-DO: add some logic to detect if a header in headersToAdd already exists in the request. If it does, skip and add next header
      for (int j=0; j<headersToAdd.length; j++) {
        headers.add(headersToAdd[j]);
        }
      String request = new String(messageInfo.getRequest());
      String messageBody = request.substring(rqInfo.getBodyOffset());
      byte[] updateMessage = helpers.buildHttpMessage(headers, messageBody.getBytes());
      messageInfo.setRequest(updateMessage);
    }
} 
