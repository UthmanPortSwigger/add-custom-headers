package burp;

import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

public class BurpExtender implements IBurpExtender, IHttpListener {
    //
    // implement IBurpExtender
    //

    private IExtensionHelpers helpers;
    PrintWriter stdout;

    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {
      stdout = new PrintWriter(callbacks.getStdout(), true);
      helpers = callbacks.getHelpers();
      callbacks.setExtensionName("Add-Headers-Java");
      callbacks.registerHttpListener(this);
    }

    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo)
    {
        if(messageIsRequest) {
            IRequestInfo rqInfo = helpers.analyzeRequest(messageInfo);
            List<String> headers = new ArrayList<String>(rqInfo.getHeaders());
            headers.add("Header1: value1");                                             /* change first header name and value here */ 
            headers.add("Header2: value2");                                             /* change second header name and value here */ 
            String request = new String(messageInfo.getRequest());
            String messageBody = request.substring(rqInfo.getBodyOffset());
            byte[] updateMessage = helpers.buildHttpMessage(headers, messageBody.getBytes());
            messageInfo.setRequest(updateMessage);

        }
    }
}    
