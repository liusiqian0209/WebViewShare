package cn.liusiqian.webviewdemo;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Créé par liusiqian 18/12/16.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //这里如果参考github上的 addInterceptor，在inspect之后会crash
                //see https://github.com/facebook/stetho/issues/346
                .addNetworkInterceptor(new StethoInterceptor())
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

    }
}
