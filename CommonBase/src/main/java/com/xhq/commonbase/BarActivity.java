package com.xhq.commonbase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xhq.commonbase.widget.AutoLocateHorizontalView;
import com.xhq.commonbase.widget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/4/1.
 *     Desc  : Description.
 *     Updt  : Description.
 * </pre>
 */
public class BarActivity extends AppCompatActivity{
    private List<Integer> ages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);
        initData();
        AutoLocateHorizontalView horizontalView = (AutoLocateHorizontalView)findViewById(R.id.auto_locate_horizontal_view);
        BarAdapter adapter  = new BarAdapter(this,ages);
        horizontalView.setItemCount(10);
        horizontalView.setAdapter(adapter);
    }

    private void initData(){
        ages.add(100);
        ages.add(50);
        ages.add(40);
        ages.add(30);
        ages.add(60);
        ages.add(80);
        ages.add(90);
        ages.add(100);
        ages.add(11);
        ages.add(19);
        ages.add(23);
        ages.add(55);
        ages.add(55);
        ages.add(100);
        ages.add(100);
        ages.add(100);
        ages.add(66);
        ages.add(89);
        ages.add(22);
        ages.add(11);
        ages.add(10);
    }

}
