package cn.liusiqian.webviewdemo.web;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.liusiqian.webviewdemo.utils.Utils;

/**
 * Créé par liusiqian 18/12/15.
 */
public class BaseWebViewClient extends WebViewClient {
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//      super.onReceivedSslError(view, handler, error);
        Utils.log("onReceivedSslError----Url:"+error.getUrl());
        Utils.log("onReceivedSslError----Cert:"+error.getCertificate().toString());
        handler.proceed();
    }
}
