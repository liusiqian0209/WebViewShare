package cn.liusiqian.webviewdemo.cache;

import android.util.LruCache;

/**
 * Created by liusiqian on 2018/12/17.
 */
public class GlobalCache {

    private LruCache<String, String> picCache;      //Key url, Value localPath

    private LruCache<String, Integer> scrollCache;    //key url, Value scrollY

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
        picCache = new LruCache<>(12);
        scrollCache = new LruCache<>(6);
    }

    public void putPic(String url, String absPath) {
        picCache.put(url, absPath);
    }

    public String getPic(String url) {
        return picCache.get(url);
    }

    public void putScrollPos(String url, int scroll) {
        scrollCache.put(url, scroll);
    }

    public int getScrollPos(String url) {
        if (scrollCache.get(url) != null) {
            return scrollCache.get(url);
        }
        return 0;
    }
}
