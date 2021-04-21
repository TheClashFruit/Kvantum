package xyz.theclashfruit.kvantum;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = findViewById(R.id.webview1);
        EditText editTextUrl = findViewById(R.id.editTextUrl);
        ImageView imageViewLock = findViewById(R.id.imageViewLock);
        ImageView imageViewMore = findViewById(R.id.imageViewMore);
        LinearLayout linearUrl = findViewById(R.id.linearUrl);
        LinearLayout linearActionBar = findViewById(R.id.linearActionBar);

        linearActionBar.setElevation(8f);

        linearUrl.setBackground(new GradientDrawable() {
            public GradientDrawable getIns(int a, int b) {
                this.setCornerRadius(a); this.setColor(b);
                return this;
            }
        }.getIns((int)8, 0xFF616161));

        webView.loadUrl("https://start.duckduckgo.com");

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                editTextUrl.setText(url);

                if(KvengineUtil.sslCheck(view)) {
                    imageViewLock.setColorFilter(0xFF4CAF50);
                } else {
                    imageViewLock.setColorFilter(0xFFF44336);
                }

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                editTextUrl.setText(url);

                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                imageViewLock.setColorFilter(0xFFF44336);

                super.onReceivedSslError(view, handler, error);
            }
        });

        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            String cookies = CookieManager.getInstance().getCookie(url);
            request.addRequestHeader("cookie", cookies);
            request.addRequestHeader("User-Agent", userAgent);
            request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));

            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
        });


        editTextUrl.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                webView.loadUrl(editTextUrl.getText().toString());
                return true;
            }
            return false;
        });

        imageViewMore.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(MainActivity.this, imageViewMore);

            Menu menu = popup.getMenu();
            menu.add("Bookmarks");
            menu.add("History");
            menu.add("Downloads");
            menu.add("Desktop Site").setCheckable(true);
            menu.add("Settings");
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getTitle().toString()) {
                    case "Bookmarks":

                        return true;
                    case "Settings":
                        Intent i1 = new Intent();
                        i1.setClass(MainActivity.this, SettingsActivity.class);
                        startActivity(i1);

                        return true;
                    default: return false;
                }
            });

            popup.show();
        });
    }
}