package testExtension;

import burp.api.montoya.proxy.http.InterceptedResponse;
import burp.api.montoya.proxy.http.ProxyResponseHandler;
import burp.api.montoya.proxy.http.ProxyResponseReceivedAction;
import burp.api.montoya.proxy.http.ProxyResponseToBeSentAction;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import static burp.api.montoya.core.HighlightColor.RED;

class MyProxyResponseHandler implements ProxyResponseHandler {
    @Override
    public ProxyResponseReceivedAction handleResponseReceived(InterceptedResponse interceptedResponse) {
        // Define the regular expression pattern
        String regexPattern = "(?i)(?:key|api|token|secret|client|passwd|password|auth|access)(?:[0-9a-z\\\\-_\\\\t .]{0,20})(?:[\\\\s|']|[\\\\s|\\\"]){0,3}(?:=|>|:{1,3}=|\\\\|\\\\|:|<=|=>|:|\\\\?=)(?:'|\\\"|\\\\s|=|\\\\x60){0,5}([0-9a-z\\\\-_.=]{10,150})(?:['|\\\"|\\\\n"
                + //
                "|\\\\r|\\\\s|\\\\x60|;]|$)"; 
        Pattern pattern = Pattern.compile(regexPattern);

        // Create a matcher to perform matching operations
        Matcher matcher = pattern.matcher(interceptedResponse.bodyToString());

        // If the regex pattern is found in the response body, add a note and highlight
        // the response
        if (matcher.find()) {
            interceptedResponse.annotations().setNotes("Contains Secrets");
            return ProxyResponseReceivedAction.continueWith(interceptedResponse,
                    interceptedResponse.annotations().withHighlightColor(RED));
        }

        // If the regex pattern is not found, continue without highlighting the response
        return ProxyResponseReceivedAction.continueWith(interceptedResponse);
    }

    @Override
    public ProxyResponseToBeSentAction handleResponseToBeSent(InterceptedResponse interceptedResponse) {
        return ProxyResponseToBeSentAction.continueWith(interceptedResponse);
    }
}