package cn.liusiqian.webviewdemo.web;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.LruCache;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

    private LruCache<String, String> lruCache = new LruCache<>(4);

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
                    String absPath = GlobalCache.getInstance().get(uri.toString());
                    File file = null;
                    if (TextUtils.isEmpty(absPath)) {
                        //get from local
                        file = new File(Utils.getImageFolder(), uri.getLastPathSegment());
                        Utils.log("getImage Local");
                        if (!file.exists()) {
                            //get from remote
                            Response response = OkHttpUtils.get().url(uri.toString()).build().execute();
                            if (response != null && response.body() != null) {
                                file = parseImageFile(response, Utils.getImageFolder(), uri.getLastPathSegment());
                                Utils.log("getImage Remote");
                            }
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

                    GlobalCache.getInstance().put(uri.toString(), file.getAbsolutePath());

                    return new WebResourceResponse("image/" + type, "", inputStream);

                } catch (IOException e) {
                    Utils.log(e.getMessage());
                }
            }
        }
        return null;
    }


    private File parseImageFile(Response response, File directory, String fileName) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();

            long sum = 0;

            if (directory != null && !directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directory, fileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
//                final long finalSum = sum;
//                OkHttpUtils.getInstance().getDelivery().execute(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        inProgress(finalSum * 1.0f / total, total, id);
//                    }
//                });
            }
            fos.flush();

            return file;

        } finally {
            response.body().close();
            if (is != null) {
                is.close();
            }
            if (fos != null) {
                fos.close();
            }

        }
    }

}
