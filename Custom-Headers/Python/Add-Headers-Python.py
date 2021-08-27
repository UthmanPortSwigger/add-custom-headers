from burp import IBurpExtender
from burp import IHttpListener
from burp import IHttpRequestResponse
from burp import IRequestInfo

# implement BurpExtender Class
class BurpExtender(IBurpExtender, IHttpListener):

    # define registerExtenderCallbacks: From IBurpExtender Interface
    def registerExtenderCallbacks(self, callbacks):

        # keep a reference to our callbacks object
        self._callbacks = callbacks
        # obtain an extension helpers object
        self._helpers = callbacks.getHelpers()
        # set our extension name that will display in Extender > Output Tab
        self._callbacks.setExtensionName("Add-Headers-Python")
        # register an HTTP listener
        callbacks.registerHttpListener(self)

    # define processHttpMessage: From IHttpListener Interface
    def processHttpMessage(self, toolFlag, messageIsRequest, messageInfo):
        # only work on requests:
        if messageIsRequest:
            request = messageInfo.getRequest()                                          # get Request from IHttpRequestResponse instance
            requestStr = self._callbacks.getHelpers().bytesToString(request)
            requestParsed = self._helpers.analyzeRequest(request)
            body = requestStr[requestParsed.getBodyOffset():]
            headers = requestParsed.getHeaders()
            headers.add('Header1: value1')                                              # change header name and value here 
            httpRequest = self._callbacks.getHelpers().buildHttpMessage(headers, body)
            messageInfo.setRequest(httpRequest)
            return
