package cn.liusiqian.webviewdemo.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;

import cn.liusiqian.webviewdemo.Consts;
import cn.liusiqian.webviewdemo.R;
import cn.liusiqian.webviewdemo.cache.GlobalCache;
import cn.liusiqian.webviewdemo.utils.FileUtils;
import cn.liusiqian.webviewdemo.utils.Utils;
import cn.liusiqian.webviewdemo.web.ImageCachedWebViewClient;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.schedulers.IoScheduler;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Créé par liusiqian 18/12/15.
 */
public class ArticleActivity extends Activity{


    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chrome_client);
        initWidgets();
        processPermissions();
    }

    private void processPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                loadWebContent();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 8002);
            }
        }
    }

    private void loadWebContent() {
        webView.loadUrl(Utils.getHttpUrl(Consts.ARTICLE_PATH));
    }

    private void initWidgets() {

        View reqView = findViewById(R.id.txt_call);
        reqView.setVisibility(View.VISIBLE);
        reqView.setOnClickListener(ocl);
        findViewById(R.id.txt_next).setVisibility(View.GONE);

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebViewClient(new ImageCachedWebViewClient());

        webView.setOnLongClickListener(olcl);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 8002 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadWebContent();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String url = webView.getUrl();
        int scrollY = webView.getScrollY();
        GlobalCache.getInstance().putScrollPos(url, scrollY);
    }

    public Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OkHttpUtils.get().url(Utils.getHttpUrl(Consts.JSON_TEST_PATH))
                    .addParams("id","1125")
                    .addParams("name", "liusiqian")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Toast.makeText(ArticleActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (response != null) {
                                Toast.makeText(ArticleActivity.this, response, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    };

    private View.OnLongClickListener olcl = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            final WebView.HitTestResult result = webView.getHitTestResult();
            if (result.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                    result.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                new AlertDialog.Builder(ArticleActivity.this)
                        .setItems(new String[]{"保存图片", "分享图片"}, (dialog, which) -> {
                            final String picUrl = result.getExtra();//获取图片
                            if (!TextUtils.isEmpty(picUrl)) {
                                final Uri uri = Uri.parse(picUrl);
                                if (uri != null) {
                                    switch (which) {
                                        case 0:
                                            //保存图片到相册
                                            Observable.create(new ObservableOnSubscribe<Boolean>() {
                                                @Override
                                                public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {

                                                    Response response = OkHttpUtils.get().url(picUrl).build().execute();
                                                    if (response != null && response.body() != null) {
                                                        File file = FileUtils.parseImageFile(response, Utils.getDownloadFolder(), uri.getLastPathSegment());
                                                        if (file.exists()) {
                                                            emitter.onNext(true);
                                                            emitter.onComplete();
                                                        } else {
                                                            emitter.onError(new RuntimeException("response error!"));
                                                        }
                                                    } else {
                                                        emitter.onError(new RuntimeException("response error!"));
                                                    }
                                                }
                                            }).subscribeOn(new IoScheduler())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new Consumer<Boolean>() {
                                                        @Override
                                                        public void accept(Boolean value) throws Exception {
                                                            Toast.makeText(ArticleActivity.this, "已保存", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }, new Consumer<Throwable>() {
                                                        @Override
                                                        public void accept(Throwable throwable) throws Exception {
                                                            Toast.makeText(ArticleActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            break;
                                        case 1:
                                            // 分享图片
                                            Observable.create(new ObservableOnSubscribe<Bitmap>() {
                                                @Override
                                                public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {

                                                    Response response = OkHttpUtils.get().url(picUrl).build().execute();
                                                    if (response != null && response.body() != null) {
                                                        File file = FileUtils.parseImageFile(response, Utils.getDownloadFolder(), uri.getLastPathSegment());
                                                        if (file.exists()) {
                                                            emitter.onNext(BitmapFactory.decodeFile(file.getAbsolutePath()));
                                                            emitter.onComplete();
                                                        } else {
                                                            emitter.onError(new RuntimeException("response error!"));
                                                        }
                                                    } else {
                                                        emitter.onError(new RuntimeException("response error!"));
                                                    }
                                                }
                                            }).subscribeOn(new IoScheduler())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new Consumer<Bitmap>() {
                                                        @Override
                                                        public void accept(Bitmap bitmap) throws Exception {
                                                            try {
                                                                Intent intent = new Intent(Intent.ACTION_SEND);
                                                                intent.setType("image/*");
                                                                intent.putExtra(Intent.EXTRA_STREAM, getImageUri(ArticleActivity.this, bitmap));
                                                                startActivity(Intent.createChooser(intent, "分享图片"));
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(ArticleActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }, new Consumer<Throwable>() {
                                                        @Override
                                                        public void accept(Throwable throwable) throws Exception {
                                                            throwable.printStackTrace();
                                                            Toast.makeText(ArticleActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            break;
                                    }
                                }
                            }

                        })
                        .show();
                return true;
            }

            return false;
        }
    };

}
