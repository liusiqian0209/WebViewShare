package cn.liusiqian.webviewdemo;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by liusiqian on 2018/12/5.
 */
public class MyPromptDialog extends Dialog {

    private TextView tvMessage;
    private EditText editResult;
    private View btnOk, btnCancel;

    private String message;
    private String defaultResult;
    private OnDlgClickListener listener;

    public MyPromptDialog(@NonNull Context context) {
        super(context, R.style.SimpleDialog);
        setContentView(R.layout.dlg_prompt);
    }

    public MyPromptDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public MyPromptDialog setDefalutMessage(String message) {
        defaultResult = message;
        return this;
    }

    public MyPromptDialog setOnDlgClickListener(OnDlgClickListener listener) {
        this.listener = listener;
        return this;
    }

    public MyPromptDialog build() {
        tvMessage = findViewById(R.id.txt_message_prompt);
        editResult = findViewById(R.id.edit_prompt);
        btnOk = findViewById(R.id.txt_ok_prompt);
        btnCancel = findViewById(R.id.txt_cancel_prompt);

        if (!TextUtils.isEmpty(message)) {
            tvMessage.setText(message);
        }

        if (!TextUtils.isEmpty(defaultResult)) {
            editResult.setText(defaultResult);
            editResult.setSelection(defaultResult.length());
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    String res = editResult.getText().toString();
                    listener.onClickOk(res);
                    dismiss();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    String res = editResult.getText().toString();
                    listener.onClickCancel(res);
                    dismiss();
                }
            }
        });

        return this;
    }

    @Override
    public void show() {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        super.show();
    }

    public interface OnDlgClickListener {
        void onClickOk(String msg);

        void onClickCancel(String msg);
    }
}
