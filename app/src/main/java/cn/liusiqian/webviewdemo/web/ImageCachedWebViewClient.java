package cn.liusiqian.webviewdemo.web;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.liusiqian.webviewdemo.cache.GlobalCache;
import cn.liusiqian.webviewdemo.utils.FileUtils;
import cn.liusiqian.webviewdemo.utils.Utils;
import okhttp3.Response;

/**
 * Créé par liusiqian 18/12/15.
 */
public class ImageCachedWebViewClient extends BaseWebViewClient {


    public ImageCachedWebViewClient() {
    }

    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Uri uri = request.getUrl();
            if (uri.getPath().contains("pics/")) {
                Utils.log("shouldInterceptRequest uri = " + uri);
                try {
                    //get from cache
                    String absPath = GlobalCache.getInstance().getPic(uri.toString());
                    File file = null;
                    if (TextUtils.isEmpty(absPath)) {
                        //get from local
                        file = new File(Utils.getImageFolder(), uri.getLastPathSegment());
                        if (!file.exists()) {
                            //get from remote
                            Response response = OkHttpUtils.get().url(uri.toString()).build().execute();
                            if (response != null && response.body() != null) {
                                file = FileUtils.parseImageFile(response, Utils.getImageFolder(), uri.getLastPathSegment());
                                Utils.log("getImage Remote");
                            }
                        } else {
                            Utils.log("getImage Local");
                        }
                    } else {
                        file = new File(absPath);
                        Utils.log("getImage Cache");
                    }

                    if (file == null || !file.exists()) {
                        return null;
                    }
                    InputStream inputStream;
                    String type;
                    try {
                        inputStream = new FileInputStream(file);
                        type = FileUtils.getFileType(file.getAbsolutePath());
                    } catch (Exception e) {
                        return null;
                    }
                    if (TextUtils.isEmpty(type)) {
                        return null;
                    }

                    GlobalCache.getInstance().putPic(uri.toString(), file.getAbsolutePath());

                    return new WebResourceResponse("image/" + type, "", inputStream);

                } catch (IOException e) {
                    Utils.log(e.getMessage());
                }
            }
        }
        return null;
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        int scrollY = GlobalCache.getInstance().getScrollPos(url);
        view.scrollTo(0, scrollY);
    }
}
