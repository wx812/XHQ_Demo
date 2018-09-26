package com.tencent.liteav.ugsv;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.tencent.liteav.ugsv.common.widget.AutoDragHorizontalView;
import com.tencent.liteav.ugsv.common.widget.ClickToRecordView;
import com.tencent.liteav.ugsv.common.widget.VideoRecordButton;
import com.tencent.liteav.ugsv.videorecord.RVCameraTypeAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/3/27.
 *     Desc  : Description.
 *     Updt  : Description.
 * </pre>
 */
public class TestActivity extends AppCompatActivity{

    String[] typeStr = new String[]{"拍照", "长按拍摄", "单击拍摄"};
    List<String> typeList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_record_1);

        initView();
    }


    private void initView(){
        ClickToRecordView click_to_record_view = findViewById(R.id.click_to_record_view);
//        click_to_record_view.startAnimation();

        VideoRecordButton video_record_btn = findViewById(R.id.video_record_btn);
        video_record_btn.setOnHoldDownListener(on -> {
            if(on){
                Log.d("xhq", "holding the button");
            }else{
                Log.d("xhq", "release the button");
            }
        });


        AutoDragHorizontalView rv_auto_drag_horizontal = findViewById(R.id.rv_auto_drag_horizontal);


        typeList = new ArrayList<>();
        typeList.addAll(Arrays.asList(typeStr));

        RVCameraTypeAdapter typeAdapter = new RVCameraTypeAdapter(this, typeList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_auto_drag_horizontal.setLayoutManager(linearLayoutManager);
        rv_auto_drag_horizontal.setOnSelectedPositionChangedListener(pos -> Toast.makeText(TestActivity.this, "pos:" + pos, Toast.LENGTH_SHORT).show());
        rv_auto_drag_horizontal.setDefSelectPosition(5);
        rv_auto_drag_horizontal.setAdapter(typeAdapter);

    }
}
