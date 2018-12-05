package cn.liusiqian.webviewdemo.jsinterface;

import android.webkit.JavascriptInterface;

import java.lang.ref.WeakReference;

import cn.liusiqian.webviewdemo.utils.Utils;

/**
 * Created by liusiqian on 2018/11/29.
 */
public class JsInterfaces {

    private WeakReference<IBridgeInterface> iBridgeWR;

    public JsInterfaces(IBridgeInterface bridge) {
        iBridgeWR = new WeakReference<>(bridge);
    }

    @JavascriptInterface
    public void setToken(String ticket) {
        Thread thread = Thread.currentThread();
        Utils.log("thread:"+thread.getName());

        if (iBridgeWR == null || iBridgeWR.get() == null) {
            return;
        }

        iBridgeWR.get().onFetchTicket(ticket);
    }

    @JavascriptInterface
    protected void testProtected() {
        Utils.log("testProtected");
    }

    @JavascriptInterface
    private void testPrivate() {
        Utils.log("testPrivate");
    }

    public void testNoAnnotation(){

    }

}
