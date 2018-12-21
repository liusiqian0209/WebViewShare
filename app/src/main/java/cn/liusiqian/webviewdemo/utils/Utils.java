package cn.liusiqian.webviewdemo.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import cn.liusiqian.webviewdemo.Consts;

/**
 * Created by liusiqian on 2018/11/29.
 */
public class Utils {

    public static String getHttpUrl(String path) {
        return Consts.HTTP_HOST + path;
    }

    public static String getUrl(String path) {
        return Consts.HOST + path;
    }

    public static void log(String msg) {
        Log.e(Consts.TAG, msg);
    }

    public static File getImageFolder() {
        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "webviewdemo");
        if (root.isDirectory() || root.mkdirs()) {
            File temp = new File(root, "image");
            if (temp.isDirectory() || temp.mkdirs()) {
                hideFromMediaScanner(temp);
            }
            return temp;
        }
        return null;
    }

    public static File getDownloadFolder() {
        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "webviewdemo");
        if (root.isDirectory() || root.mkdirs()) {
            File temp = new File(root, "download");
            if (temp.isDirectory() || temp.mkdirs()) {
                hideFromMediaScanner(temp);
            }
            return temp;
        }
        return null;
    }

    private static void hideFromMediaScanner(File root) {
        File file = new File(root, ".nomedia");
        if (!file.exists() || !file.isFile()) {
            try {
                file.createNewFile();
            } catch (IOException var3) {
                Utils.log(var3.getMessage());
            }
        }
    }
}
