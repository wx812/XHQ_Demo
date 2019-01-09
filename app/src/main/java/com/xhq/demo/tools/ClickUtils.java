package com.xhq.demo.tools;

import android.os.SystemClock;
import android.view.View;

import com.xhq.demo.tools.uiTools.ToastUtils;


/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/6/11.
 *     Desc  : Description.
 *     Updt  : Description.
 * </pre>
 */
public class ClickUtils{

    private long lastClickTime; // time of last click
    private boolean clickLimit = true;  //switch of limit for click

    protected  void setClickLimit(boolean clickLimit){
        this.clickLimit = clickLimit;
    }

    /**
     * Determine whether rapid double-click;
     *
     * @return false, true
     */
    private boolean isFastDoubleClick() {

        // 从开机到现在的时间
//        long startTime = SystemClock.uptimeMillis();
        long startTime = System.currentTimeMillis();
        long timeDuration = startTime - lastClickTime;
        if (timeDuration>300) lastClickTime = startTime;
        if (!clickLimit) return timeDuration < 0;
        return timeDuration <= 300;
    }


        // 判断N次点击事件
//    long[] mClicks = new long[n];
//
//    view.setOnClickListener(new OnClickListener(){
//
//        @Override
//        public void onClick (View v){
//            System.arraycopy(mClicks, 1, mClicks, 0, mClicks.length - 1);
//            mClicks[mClicks.length - 1] = SystemClock.uptimeMillis();
//            if(mClicks[0] >= (SystemClock.uptimeMillis() - 500)){
//                doSomething();
//            }
//        }
//    }


//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            if (isFastDoubleClick()) {
//                return true;
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }



    private Long[] mThreeHits = new Long[3];
    public void threeClick(View view){

        //数组向左移位操作
        System.arraycopy(mThreeHits, 1, mThreeHits, 0, mThreeHits.length-1);
        mThreeHits[mThreeHits.length-1] = SystemClock.uptimeMillis();
        if (mThreeHits[0] >= (SystemClock.uptimeMillis()-500)) {
            ToastUtils.showToast("三击事件");
        }
    }

    private Long[] mDoubleHits = new Long[2];
    public boolean isDoubleClick(){
        System.arraycopy(mDoubleHits, 1, mDoubleHits, 0, mDoubleHits.length-1);
        mDoubleHits[mDoubleHits.length-1] = SystemClock.uptimeMillis();
        return mDoubleHits[0] >= (SystemClock.uptimeMillis() - 1000);
    }



}


