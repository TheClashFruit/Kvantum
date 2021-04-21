package xyz.theclashfruit.kvantum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                editTextUrl.setText(url);

                try {
                    webView.getCertificate();
                    imageViewLock.setColorFilter(0xFF4CAF50);
                } catch (Exception error) {
                    imageViewLock.setColorFilter(0xFFF44336);
                }

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                editTextUrl.setText(url);

                super.onPageFinished(view, url);
            }
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