from burp import IBurpExtender
from burp import IHttpListener
from burp import IHttpRequestResponse
from burp import IRequestInfo

# BurpExtender Class contaning all functions used to interact with Burp Suite Extender API
class BurpExtender(IBurpExtender, IHttpListener):

    # define registerExtenderCallbacks: From IBurpExtender Interface
    def registerExtenderCallbacks(self, callbacks):

        # keep a reference to our callbacks object (Burp Extensibility Feature)
        self._callbacks = callbacks
        # obtain an extension helpers object (Burp Extensibility Feature)
        # http://portswigger.net/burp/extender/api/burp/IExtensionHelpers.html
        self._helpers = callbacks.getHelpers()
        # set our extension name that will display in Extender Tab
        self._callbacks.setExtensionName("Add a custom header")
        # register ourselves as an HTTP listener
        callbacks.registerHttpListener(self)

    # define processHttpMessage: From IHttpListener Interface
    def processHttpMessage(self, toolFlag, messageIsRequest, messageInfo):
        # only work on requests:
        if messageIsRequest:
            request = messageInfo.getRequest()                                          #get Request from IHttpRequestResponse instance
            requestStr = self._callbacks.getHelpers().bytesToString(request)
            requestParsed = self._helpers.analyzeRequest(request)
            body = requestStr[requestParsed.getBodyOffset():]
            headers = requestParsed.getHeaders()
            headers.add('Header1: value1')                                              #change header name and value here 
            httpRequest = self._callbacks.getHelpers().buildHttpMessage(headers, body)
            messageInfo.setRequest(httpRequest)
            return