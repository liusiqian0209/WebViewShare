package cn.liusiqian.webviewdemo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import cn.liusiqian.webviewdemo.Consts;
import cn.liusiqian.webviewdemo.R;
import cn.liusiqian.webviewdemo.utils.Utils;
import cn.liusiqian.webviewdemo.web.IChromeClientBridge;
import cn.liusiqian.webviewdemo.web.MyWebChromeClient;

/**
 * Created by liusiqian on 2018/12/4.
 */
public class ChromeClientTestActivity extends Activity implements IChromeClientBridge{
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chrome_client);
        initWidgets();
    }

    private void initWidgets() {
        webView = findViewById(R.id.webview);

        //这句话不能缺
        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setGeolocationEnabled(true);

        webView.setWebChromeClient(new MyWebChromeClient(this));

        webView.loadUrl( Utils.getUrl(Consts.CHROME_CLIENT_PATH) );
    }

}
