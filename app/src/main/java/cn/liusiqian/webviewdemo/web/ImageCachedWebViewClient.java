package cn.liusiqian.webviewdemo.web;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import java.util.Map;

import cn.liusiqian.webviewdemo.utils.Utils;

/**
 * Créé par liusiqian 18/12/15.
 */
public class ImageCachedWebViewClient extends BaseWebViewClient {

    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Uri uri = request.getUrl();
            Utils.log("uri = " + uri);
            Map<String,String> headers = request.getRequestHeaders();
            for (String key : headers.keySet()) {
                Utils.log("key = "+ key+"  value = "+headers.get(key));
            }
        }
        return null;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Uri uri = Uri.parse(url);

        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        //这里不用override
        Utils.log("onLoadResource   uri = " + url);
        super.onLoadResource(view, url);
    }
}
