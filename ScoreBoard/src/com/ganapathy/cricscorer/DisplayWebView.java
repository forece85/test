package com.ganapathy.cricscorer;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

public class DisplayWebView extends BaseActivity {
    private String _title = "";
    private String _url = "";

    public void onCreate(Bundle savedInstanceState) {
        try {
            if (VERSION.SDK_INT >= 21) {
                WebView.enableSlowWholeDocumentDraw();
            }
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_display_web_view);
            Bundle bundle = getIntent().getExtras();
            this._url = bundle.getString("url");
            this._title = bundle.getString("title");
            setTitle(this._title);
            initializeControls();
        } catch (Exception e) {
            finish();
        }
    }

    public boolean hideWindowTitle() {
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0252R.menu.activity_display_web_view, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case C0252R.id.menu_send:
                onSend(null);
                break;
            case C0252R.id.menu_close:
                onBackPressed();
                break;
        }
        return true;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        if (v.getId() == C0252R.id.buttonShare) {
            inflater.inflate(C0252R.menu.web_context_menu, menu);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case C0252R.id.miSendAsFile:
                shareAsFile();
                return true;
            case C0252R.id.miSendAsImage:
                shareAsImage();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    protected void initializeControls() {
        WebView webView = (WebView) findViewById(C0252R.id.webView1);
        webView.setWebViewClient(new WebViewClient());
        webView.setKeepScreenOn(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        webView.loadUrl(this._url);
        registerForContextMenu((ImageButton) findViewById(C0252R.id.buttonShare));
    }

    protected void onDestroy() {
        WebView webView = (WebView) findViewById(C0252R.id.webView1);
        if (!(webView == null || webView.getParent() == null)) {
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
        }
        super.onDestroy();
    }

    public void onBack(View view) {
        onBackPressed();
    }

    public void onShare(View view) {
        openContextMenu(view);
    }

    protected void shareAsFile() {
        Utility.showNotSupportedAlert(this);
    }

    protected void shareAsImage() {
        Utility.showNotSupportedAlert(this);
    }

    public void onSend(View view) {
        Utility.showNotSupportedAlert(this);
    }
}
