package cn.liusiqian.webviewdemo.web;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import cn.liusiqian.webviewdemo.MyPromptDialog;
import cn.liusiqian.webviewdemo.utils.Utils;

/**
 * Created by liusiqian on 2018/12/4.
 */
public class MyWebChromeClient extends WebChromeClient {
    private WeakReference<IChromeClientBridge> iBridgeWR;

    public MyWebChromeClient(IChromeClientBridge iBridgeWR) {
        this.iBridgeWR = new WeakReference<>(iBridgeWR);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        Utils.log("onProgressChanged:" + newProgress);
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        new AlertDialog.Builder(view.getContext()).setMessage("Received title: " + title).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        Utils.log("onJsAlert:" + message);
        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
        result.confirm();
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        Utils.log("onJsConfirm:" + message);
        new AlertDialog.Builder(view.getContext()).setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        result.confirm();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                result.cancel();
            }
        }).create().show();
        return true;
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
        Utils.log("onJsPrompt:" + message);
        new MyPromptDialog(view.getContext()).setMessage(message).setDefalutMessage(defaultValue)
                .setOnDlgClickListener(new MyPromptDialog.OnDlgClickListener() {
                    @Override
                    public void onClickOk(String msg) {
                        result.confirm(msg);
                    }

                    @Override
                    public void onClickCancel(String msg) {
                        result.cancel();
                    }
                }).build().show();
        return true;
    }

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        Utils.log("onJsBeforeUnload:" + message);
        result.confirm();
        return true;
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
        Utils.log("onGeolocationPermissionsShowPrompt:" + origin);
        OnGeoLocationReqCompleteListener listener = new OnGeoLocationReqCompleteListener() {
            @Override
            public void onGeoLocationReqComplete(boolean succ) {
                Utils.log("onGeoLocationReqComplete -- " + succ);
                callback.invoke(origin, succ, false);
            }
        };
        if (iBridgeWR != null && iBridgeWR.get() != null) {
            iBridgeWR.get().requestLocation(listener);
        }
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        Utils.log("onGeolocationPermissionsHidePrompt");
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return super.onConsoleMessage(consoleMessage);
    }

}
