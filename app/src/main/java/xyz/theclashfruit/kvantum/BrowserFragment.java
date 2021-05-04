package xyz.theclashfruit.kvantum;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;

import androidx.core.view.MenuCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

public class BrowserFragment extends Fragment {

    private static final String BROWSER_URL = "browserUrl";

    private String browserUrl;

    WebView webView;
    EditText editTextUrl;
    ImageView imageViewLock;
    ImageView imageViewTabs;
    ImageView imageViewMore;
    LinearLayout linearUrl;
    LinearLayout linearActionBar;

    public BrowserFragment() {
        // Required empty public constructor
    }

    public static BrowserFragment newInstance(String browserUrl) {
        BrowserFragment browserFragment = new BrowserFragment();

        Bundle args = new Bundle();
        args.putString(BROWSER_URL, browserUrl);
        browserFragment.setArguments(args);

        return browserFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            browserUrl = getArguments().getString(BROWSER_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewInflater =  inflater.inflate(R.layout.fragment_browser, container, false);

        getActivity().getWindow().setNavigationBarColor(0xFF212121);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        webView = viewInflater.findViewById(R.id.webview1);
        editTextUrl = viewInflater.findViewById(R.id.editTextUrl);
        imageViewLock = viewInflater.findViewById(R.id.imageViewLock);
        imageViewTabs = viewInflater.findViewById(R.id.imageViewTabs);
        imageViewMore = viewInflater.findViewById(R.id.imageViewMore);
        linearUrl = viewInflater.findViewById(R.id.linearUrl);
        linearActionBar = viewInflater.findViewById(R.id.linearActionBar);

        linearActionBar.setElevation(8f);
        linearUrl.setBackground(new GradientDrawable() {
            public GradientDrawable getIns(int a, int b) {
                this.setCornerRadius(a); this.setColor(b);
                return this;
            }
        }.getIns((int)8, 0xFF616161));

        webView.loadUrl(browserUrl);

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

            DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
        });

        editTextUrl.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                webView.loadUrl(editTextUrl.getText().toString());
                return true;
            }
            return false;
        });

        imageViewTabs.setOnClickListener(v -> {
            TabsFragment tabsFragment = TabsFragment.newInstance("a","b");
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.fragmentContainer, tabsFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        imageViewMore.setOnClickListener(v -> {
            PopupMenu optionsPopup = new PopupMenu(getActivity().getApplicationContext(), imageViewMore);

            optionsPopup.getMenuInflater().inflate(R.menu.options_menu, optionsPopup.getMenu());
            MenuCompat.setGroupDividerEnabled(optionsPopup.getMenu(), true);

            optionsPopup.setOnMenuItemClickListener(item -> {
                switch (item.getTitle().toString()) {
                    case "Bookmarks":

                        return true;
                    case "Settings":
                        SettingsFragment settingsFragment = SettingsFragment.newInstance("a","b");
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        fragmentTransaction.replace(R.id.fragmentContainer, settingsFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                        return true;
                    default: return false;
                }
            });

            optionsPopup.show();
        });

        return viewInflater;
    }
}