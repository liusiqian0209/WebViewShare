package cn.liusiqian.webviewdemo.cache;

import android.util.LruCache;

/**
 * Created by liusiqian on 2018/12/17.
 */
public class GlobalCache {

    private LruCache<String, String> lruCache;      //Key url, Value localPath

    private static GlobalCache sInstance;

    public static GlobalCache getInstance() {
        if (sInstance == null) {
            synchronized (GlobalCache.class) {
                if (sInstance == null) {
                    sInstance = new GlobalCache();
                }
            }
        }
        return sInstance;
    }

    private GlobalCache() {
        lruCache = new LruCache<>(12);
    }

    public void put(String url, String absPath) {
        lruCache.put(url, absPath);
    }

    public String get(String url) {
        return lruCache.get(url);
    }
}
