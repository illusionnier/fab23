package com.fabryka.apfabryka;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public String MyTextUrl;
    public String android_id;
    public ImageButton btnPrint;
    public String myPrintUrl;
    public String url;
    private WebView webView;
    WebViewClient webViewClient = new WebViewClient() {
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            MainActivity.this.url = request.getUrl().toString();
            view.loadUrl(MainActivity.this.url);
            return true;
        }
    };

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        this.btnPrint = (ImageButton) findViewById(R.id.PrintBT);
        this.android_id = Settings.Secure.getString(getContentResolver(), "android_id");
        WebView webView2 = (WebView) findViewById(R.id.webView);
        this.webView = webView2;
        webView2.getSettings().setJavaScriptEnabled(true);
        CookieManager.getInstance().setAcceptCookie(true);
        this.webView.getSettings().setSupportMultipleWindows(true);
        this.webView.getSettings().setLoadWithOverviewMode(true);
        this.webView.getSettings().setAllowFileAccess(true);
        this.webView.getSettings().setBlockNetworkLoads(false);
        this.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.setWebViewClient(new WebViewClient());
        this.webView.setWebChromeClient(new WebChromeClient());
        if (InternetConnection.checkConnection(this, this.android_id)) {
            this.MyTextUrl = "https://pro-fabryka.ru/";
            PersistantStorage.init(this);
            String property = PersistantStorage.getProperty("MyTextUrl");
            this.MyTextUrl = property;
            if (!property.contains("http") || this.MyTextUrl.isEmpty()) {
                this.MyTextUrl = "https://pro-fabryka.ru/";
            }
            this.myPrintUrl = this.MyTextUrl + "aprint.html?att=1&asid=" + this.android_id;
            this.MyTextUrl += "?asid=" + this.android_id;
            Log.i("myData", "Адрес для печати " + this.myPrintUrl);
            if (!PrinterBT.CheckPrint(this, this.android_id)) {
                this.btnPrint.setVisibility(4);
            }
            this.webView.loadUrl(this.MyTextUrl);
            return;
        }
        startActivity(new Intent(this, NNInternet.class));
    }

    public void PrintBTMy(View view) {
        Log.i("myData", "Нажел кнопку печати с адресом: " + this.myPrintUrl);
        WebPingPrint.CheckWebPrint(this, this.myPrintUrl);
    }

    public void onBackPressed() {
        if (this.webView.canGoBack()) {
            this.webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
