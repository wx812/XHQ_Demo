package com.xhq.demo.activity;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xhq.demo.R;
import com.xhq.demo.widget.PieChart;

public class MainActivity extends AppCompatActivity{

    private Canvas mCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Resources res = getResources();
        setContentView(R.layout.activity_main);

        initView(res);

        initData();

        initListener();

    }


    private void initView(Resources res){

//        mCanvas.drawa

        final PieChart pie = (PieChart) this.findViewById(R.id.Pie);
        pie.addItem("Agamemnon", 2, res.getColor(R.color.seafoam));
        pie.addItem("Bocephus", 3.5f, res.getColor(R.color.chartreuse));
        pie.addItem("Calliope", 2.5f, res.getColor(R.color.emerald));
        pie.addItem("Daedalus", 3, res.getColor(R.color.bluegrass));
        pie.addItem("Euripides", 1, res.getColor(R.color.turquoise));
        pie.addItem("Ganymede", 3, res.getColor(R.color.slate));

        findViewById(R.id.Reset).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pie.setCurrentItem(0);
            }
        });

    }


    private void initData(){

    }


    private void initListener(){

    }
}
