package cn.liusiqian.webviewdemo.web;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;

import cn.liusiqian.webviewdemo.Consts;
import cn.liusiqian.webviewdemo.jsinterface.IBridgeScheme;
import cn.liusiqian.webviewdemo.utils.Utils;

/**
 * Created by liusiqian on 2018/11/30.
 */
public class MyWebViewClient extends WebViewClient {
    private WeakReference<IBridgeScheme> ibridgeWR;

    public MyWebViewClient(IBridgeScheme bridge) {
        ibridgeWR = new WeakReference<>(bridge);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
            final String decodedUrl = URLDecoder.decode(url, "UTF-8");
            if (!TextUtils.isEmpty(decodedUrl)) {
                Uri uri = Uri.parse(url);
                if (Consts.JS_BRIDGE_SCHEME.equals(uri.getScheme()) && uri.getHost() != null) {
                    return processActions(uri);
                }
            }

            return super.shouldOverrideUrlLoading(view, url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return super.shouldOverrideUrlLoading(view, url);
        }
    }


    private boolean processActions(Uri uri) {
        Utils.log("thread:" + Thread.currentThread().getName());


        switch (uri.getHost()) {
            case Consts.JS_BRIDGE_ACTION_CLEAR_TICKET:
                String resetStr = uri.getQueryParameter(Consts.JS_BRIDGE_PARAM_CLEAR_TICKET_DEF);
                if (resetStr == null) {
                    resetStr = "";
                }
                if (ibridgeWR == null || ibridgeWR.get() == null) {
                    return false;
                }
                ibridgeWR.get().clearToken(resetStr);
                return true;
        }

        return false;
    }
}
