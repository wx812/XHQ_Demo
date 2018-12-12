package com.xhq.demo.widget.CountDownTimer;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.xhq.demo.R;


/**
 * 倒计时
 * @author Lenovo
 */
public class CusCountDownTimer extends CountDownTimer {
    private TextView textView;
    private int resetResId;
    private int countResId;

    public CusCountDownTimer(TextView textView, int countResId, int resetResId, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
        this.countResId = countResId;
        this.resetResId = resetResId;
    }

    /**
     *  计时过程
     */
    @Override
    public void onTick(long l) {
        //防止计时过程中重复点击
        textView.setClickable(false);
        Context context = textView.getContext();
        if (context != null) {
            long remind = l / 1000;
            String content;
            if (remind <= 120) {
                content = String.valueOf(remind);
            } else {
                long minute = remind / 60;
                long second = remind % 60;
                content = context.getString(R.string.time_format, minute, second);
            }
            content = context.getString(countResId, content);
            textView.setText(content);
        }
    }

    /**
     * 计时完毕的方法
     */
    @Override
    public void onFinish() {
        //重新给Button设置文字
        textView.setText(resetResId);
        //设置可点击
        textView.setClickable(true);
    }
}