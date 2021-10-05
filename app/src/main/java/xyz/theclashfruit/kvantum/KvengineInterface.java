package xyz.theclashfruit.kvantum;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class KvengineInterface {
    Context mainClass;

    KvengineInterface(Context appClass) {
        mainClass = appClass;
    }

    @JavascriptInterface
    public static String getVersion() {
        return "1.1";
    }

    @JavascriptInterface
    public void notifyBrowser() {
        Toast.makeText(mainClass, "You are using a website with Kvantum WAPP support!", Toast.LENGTH_SHORT).show();
    }

}
