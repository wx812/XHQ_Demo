package com.xhq.demo.widget;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

/**
 * Created by lenovo on 2018/9/9.
 */

public class ScanTextWatcher implements TextWatcher{

    private onScanListener listener;

    private Handler handler = new Handler();

    private Editable editable;

    private Runnable delayRun = new Runnable() {
        @Override
        public void run() {
            listener.onScanFinish(editable.toString());
            editable.clear();
        }
    };

    public ScanTextWatcher(onScanListener listener) {
        this.listener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        handler.removeCallbacks(delayRun);
        this.editable = editable;
        if (!TextUtils.isEmpty(editable)) {
            handler.postDelayed(delayRun, 300);//0.3秒内没有输入才会执行
        }
    }

    public interface onScanListener {
        void onScanFinish(String result);
    }
}

