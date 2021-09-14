package burp;

import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.net.URL;

public class BurpExtender implements IBurpExtender, IHttpListener {
    public static IBurpExtenderCallbacks callbacks;
	  public IExtensionHelpers helpers;
    PrintWriter stdout;

     
    private String[] urlMatchList = {};                                                   // change urlMatchList to your in-scope URL/(s). If empty, headers are added to all requests.
    private String[] headersToAdd = {"Header1: value1", "Header2: value2"};               // adjust key:value pairs in headersToAdd as appropriate. 

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
          stdout.println(url.toString());
          if (urlMatchList.length == 0) {                    // if the urlMatchList is empty, add headers to all requests 
            addHeaders(messageInfo, rqInfo);
          }
          for (int i=0;i<urlMatchList.length; i++) {         // iterate through urlMatchList array in a loop 
            if (url.toString() == urlMatchList[i]) {         // check if the request URL is in the urlMatchList
              addHeaders(messageInfo, rqInfo);
              break;
            }
          }
        }
    }

    private void addHeaders(IHttpRequestResponse messageInfo, IRequestInfo rqInfo) {
      List<String> headers = new ArrayList<String>(rqInfo.getHeaders());
      for (int j=0; j<headersToAdd.length; j++) {
        headers.add(headersToAdd[j]);
      }
      String request = new String(messageInfo.getRequest());
      String messageBody = request.substring(rqInfo.getBodyOffset());
      byte[] updateMessage = helpers.buildHttpMessage(headers, messageBody.getBytes());
      messageInfo.setRequest(updateMessage);
    }
}    
