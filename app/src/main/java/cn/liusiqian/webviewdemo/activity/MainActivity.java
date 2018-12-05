package cn.liusiqian.webviewdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.liusiqian.webviewdemo.Consts;
import cn.liusiqian.webviewdemo.R;
import cn.liusiqian.webviewdemo.jsinterface.IBridgeInterface;
import cn.liusiqian.webviewdemo.jsinterface.IBridgeScheme;
import cn.liusiqian.webviewdemo.jsinterface.JsInterfaces;
import cn.liusiqian.webviewdemo.utils.JsBuilder;
import cn.liusiqian.webviewdemo.utils.Utils;
import cn.liusiqian.webviewdemo.web.MyWebView;
import cn.liusiqian.webviewdemo.web.MyWebViewClient;

public class MainActivity extends AppCompatActivity implements IBridgeInterface, IBridgeScheme {

    private MyWebView webView;
    private TextView tvTicket;
    private EditText editCommit;
    private View viewLoadUrl, viewEvaljs, viewRefresh, viewGoto;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        initWebView();
    }

    private void initWidgets() {
        webView = findViewById(R.id.webview);
        tvTicket = findViewById(R.id.ticket);
        editCommit = findViewById(R.id.edit_commit);
        viewLoadUrl = findViewById(R.id.tv_commit_load);
        viewEvaljs = findViewById(R.id.tv_commit_eval);
        viewRefresh = findViewById(R.id.refresh);
        viewGoto = findViewById(R.id.goto_next);

        viewRefresh.setOnClickListener(ocl);
        viewLoadUrl.setOnClickListener(ocl);
        viewEvaljs.setOnClickListener(ocl);
        viewGoto.setOnClickListener(ocl);

        handler = new Handler(Looper.getMainLooper());
    }

    private View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.refresh:
                    webView.reload();
                    break;
                case R.id.tv_commit_load:
                    commitLoadUrl();
                    break;
                case R.id.tv_commit_eval:
                    commitEvaljs();
                    break;
                case R.id.goto_next:
                    startActivity(new Intent(MainActivity.this, ChromeClientTestActivity.class));
                    break;
            }
        }
    };

    private void commitEvaljs() {
        webView.evaluateJavascript("(function () {if(window.android && window.android.getTime){return window.android.getTime(960);}})();", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Utils.log("evaluateJs Result = "+value);
                Toast.makeText(MainActivity.this, value, Toast.LENGTH_LONG).show();
            }
        });
        Utils.log("after evaluate");
    }

    private void commitLoadUrl() {
        String jsString = new JsBuilder().setMethod("addText").addParam(editCommit.getText().toString(), String.class).build();
        Utils.log("jsString = " + jsString);
        webView.loadUrl(jsString);

    }

    private void initWebView() {
        //增加js接口
        webView.addJavascriptInterface(new JsInterfaces(this), "launcher");

        webView.loadUrl( Utils.getUrl(Consts.VERIFY_CODE_PATH) );

        webView.setWebViewClient(new MyWebViewClient(this));
    }



    @Override
    public void onFetchTicket(final String ticket) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                tvTicket.setText(ticket);
            }
        });
    }

    @Override
    public void clearToken(String defalut) {
        tvTicket.setText(defalut);
    }
}
