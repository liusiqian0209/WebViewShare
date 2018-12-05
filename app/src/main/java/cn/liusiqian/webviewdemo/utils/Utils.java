package cn.liusiqian.webviewdemo.utils;

import android.util.Log;

import cn.liusiqian.webviewdemo.Consts;

/**
 * Created by liusiqian on 2018/11/29.
 */
public class Utils {
    public static String getUrl(String path) {
        return Consts.HOST + path;
    }

    public static void log(String msg) {
        Log.e(Consts.TAG, msg);
    }
}
