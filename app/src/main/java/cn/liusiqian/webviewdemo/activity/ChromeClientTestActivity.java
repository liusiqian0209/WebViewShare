package cn.liusiqian.webviewdemo.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.liusiqian.webviewdemo.Consts;
import cn.liusiqian.webviewdemo.R;
import cn.liusiqian.webviewdemo.utils.Utils;
import cn.liusiqian.webviewdemo.web.IChromeClientBridge;
import cn.liusiqian.webviewdemo.web.MyWebChromeClient;
import cn.liusiqian.webviewdemo.web.OnGeoLocationReqCompleteListener;

/**
 * Created by liusiqian on 2018/12/4.
 */
public class ChromeClientTestActivity extends Activity implements IChromeClientBridge{
    private WebView webView;
    private OnGeoLocationReqCompleteListener listener;

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

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
                Utils.log("onReceivedSslError----Url:"+error.getUrl());
                Utils.log("onReceivedSslError----Cert:"+error.getCertificate().toString());
                handler.proceed();
            }
        });

        webView.loadUrl( Utils.getUrl(Consts.CHROME_CLIENT_PATH) );
    }

    @Override
    public void requestLocation(OnGeoLocationReqCompleteListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.listener = listener;
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 8001);
        } else {
            listener.onGeoLocationReqComplete(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 8001 && listener != null) {
            listener.onGeoLocationReqComplete(grantResults[0] == PackageManager.PERMISSION_GRANTED);
        }
    }
}
