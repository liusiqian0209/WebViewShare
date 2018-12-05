package cn.liusiqian.webviewdemo.web;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

import cn.liusiqian.webviewdemo.jsinterface.JsInterfaces;

/**
 * Created by liusiqian on 2018/11/30.
 */
public class MyWebView extends WebView {
    public MyWebView(Context context) {
        super(context);
        init();
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //enable js
        getSettings().setJavaScriptEnabled(true);

        //支持中文
        getSettings().setDefaultTextEncodingName("GBK");

        //允许chrome调试
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }
}
