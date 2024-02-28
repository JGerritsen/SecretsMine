package testExtension;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

public class MyBurpExtension implements BurpExtension {

    @Override
    public void initialize(MontoyaApi api) {
        // Implement your extension logic here
        api.extension().setName("SecretsMine");
        api.logging().logToOutput("Extension loaded!");

        // Register proxy handlers
        api.proxy().registerResponseHandler(new MyProxyResponseHandler());
    }
}
