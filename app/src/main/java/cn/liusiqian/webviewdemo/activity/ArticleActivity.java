package cn.liusiqian.webviewdemo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import cn.liusiqian.webviewdemo.Consts;
import cn.liusiqian.webviewdemo.R;
import cn.liusiqian.webviewdemo.utils.Utils;
import cn.liusiqian.webviewdemo.web.ImageCachedWebViewClient;

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
        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new ImageCachedWebViewClient());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 8002 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadWebContent();
        }
    }
}
