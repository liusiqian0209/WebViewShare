package cn.liusiqian.webviewdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.liusiqian.webviewdemo.R;

/**
 * Created by liusiqian on 2018/11/30.
 */
public class SchemeTargetActivity extends Activity {

    private Map<String, String> params;
    private TextView tvParams;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_target);
        initData(getIntent());
        initWidgets();
        loadData();
    }

    private void loadData() {
        StringBuilder sb = new StringBuilder();
        for (String k : params.keySet()) {
            String v = params.get(k);
            sb.append(k).append(" = ").append(v).append("\n");
        }
        tvParams.setText(sb.toString());
    }

    private void initWidgets() {
        tvParams = findViewById(R.id.params);
    }

    private void initData(Intent intent) {
        if (intent == null || intent.getData() == null) {
            return;
        }
        Uri uri = intent.getData();
        Set<String> set = uri.getQueryParameterNames();
        params = new HashMap<>();
        for (String s : set) {
            params.put(s, uri.getQueryParameter(s));
        }
    }
}
