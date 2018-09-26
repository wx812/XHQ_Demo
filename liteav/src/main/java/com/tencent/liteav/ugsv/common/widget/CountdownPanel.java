package com.tencent.liteav.ugsv.common.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tencent.liteav.ugsv.R;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/4/9.
 *     Desc  : countdown panel.
 *     Updt  : Description.
 * </pre>
 */
public class CountdownPanel extends LinearLayout{

    private Context mContext;
    private Button btn_countdown_camera;

    private Object obj;

    public CountdownPanel(Context context){
        this(context, null);
    }


    public CountdownPanel(Context context, @Nullable AttributeSet attrs){
        this(context, attrs, 0);
    }


    public CountdownPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);

        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_countdown_pannel, this);

        init();
        initListener();
    }


    private void init(){

        btn_countdown_camera = findViewById(R.id.btn_countdown_camera);

    }

    private void initListener(){

        btn_countdown_camera.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                mListener.onClick(v, obj);
            }
        });
    }


    private MyOnclickListener mListener;

    public void setMyOnclickListener(MyOnclickListener listener){
        mListener = listener;
    }

    public interface MyOnclickListener{

        void onClick(View view, Object obj);
    }

}
