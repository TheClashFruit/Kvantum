package xyz.theclashfruit.kvantum;

import android.webkit.WebView;

public class KvengineUtil {
    public static void newEngine(WebView webView) {
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
    }

    public static Boolean sslCheck(WebView webView) {
        try {
            webView.getCertificate();
            return true;
        } catch (Exception error) {
            return false;
        }
    }

    public static String engineVersion() {
        return "pre_alpha-1.0-kve";
    }
}
