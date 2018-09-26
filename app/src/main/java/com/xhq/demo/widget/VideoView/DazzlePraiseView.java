package com.xhq.demo.widget.VideoView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by cy on 2017/11/12.
 */

public class DazzlePraiseView extends RelativeLayout {

    public static final long COOLING_TIME = 300;

    private static final int REMOVE_HEART_VIEW = 1001;

//    private static final int NO_CLICK_DOUBLE = 1002;
//
//    /***
//     * 是否删除了检测双击时间的消息
//     */
//    private boolean isRemoveClickDoubleMessage = true;
//    private boolean isInterceptTouchEvent = true;

    private ArrayList<ImageView> mIvList = new ArrayList<>();

    private long mLastActionDown;

    private int mPicWidth;
    private int mPicHeight;
    private MyHandler mHandler;

    private DazzlePraiseActionListener mListener;

//    private MotionEvent motionEvent;

    public DazzlePraiseView(@NonNull Context context) {
        this(context, null);
    }

    public DazzlePraiseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
//        mPicWidth = ScreenUtils.getDimenPx(R.dimen.x123);
//        mPicHeight = getResources().getDimensionPixelSize(R.dimen.y144);
        mHandler = new MyHandler(this);

    }

    public static final String TAG = "DazzlePriaseView";

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

//        if (isInterceptTouchEvent) {
//
//            return true;
//        } else {
//            isInterceptTouchEvent = true;
//            return false;
//        }

//        KLog.i(TAG, "onTouchEvent" + "  ACTION_DOWN=" + (ev.getAction() == MotionEvent.ACTION_DOWN));
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                long nowTime = ev.getDownTime();
                if (nowTime - mLastActionDown < COOLING_TIME) {
                    if (mListener != null) {
                        mListener.onActionPraise();
                    }
                    playAnim(ev);
//                  删除判断是否有二次点击的post
//
//                  if (!isRemoveClickDoubleMessage) //消息还在
//                      mHandler.removeMessages(NO_CLICK_DOUBLE);
                }
//                else {
//                    //启动检测是否有二次点击的事件
//                    mHandler.sendEmptyMessageDelayed(NO_CLICK_DOUBLE, COOLING_TIME);
//                    isRemoveClickDoubleMessage = false;
//                }
                mLastActionDown = nowTime;
//                this.motionEvent = ev;
                break;
            case MotionEvent.ACTION_UP:
//                performClick();
            default:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        return super.onTouchEvent(event);
    }


    /**
     * 特殊情况下调用的
     * 比如快速在同一个位置触摸滑动viewpager  就会导致问题
     */
    public void resetClickTime(){
        mLastActionDown=0;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeMessages(REMOVE_HEART_VIEW);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private void playAnim(MotionEvent event) {
        ImageView pigImg = new ImageView(getContext());
//        pigImg.setImageResource(R.drawable.qmx_xiaoxi_zan);
        LayoutParams mLayoutParams;
        mLayoutParams = new LayoutParams(mPicWidth, mPicHeight);

//        KLog.i(TAG, "getRawX=" + event.getRawX() + "getX=" + event.getX());

        mLayoutParams.leftMargin = (int) event.getRawX();
        mLayoutParams.topMargin = (int) event.getRawY() - mPicHeight;

        mIvList.add(pigImg);
        addView(pigImg, mLayoutParams);

        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 2, 1, 2);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -mPicHeight);
        int rotation = 30 - new Random().nextInt(60);
        pigImg.setRotation(rotation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(translateAnimation);
        animationSet.setStartOffset(500);
        animationSet.setDuration(1000);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mIvList.size() > 0) {
                    mHandler.sendEmptyMessage(REMOVE_HEART_VIEW);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        pigImg.startAnimation(animationSet);
    }

    public void destroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void setListener(DazzlePraiseActionListener listener) {
        this.mListener = listener;
    }

    private class MyHandler extends Handler {
        private WeakReference<DazzlePraiseView> mDazzlePriaseView;

        public MyHandler(DazzlePraiseView dazzlePriaseView) {
            mDazzlePriaseView = new WeakReference<>(dazzlePriaseView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REMOVE_HEART_VIEW:
                    DazzlePraiseView priaseView = mDazzlePriaseView.get();
                    priaseView.removeView(priaseView.mIvList.get(0));
                    priaseView.mIvList.remove(0);
                    break;

//                case NO_CLICK_DOUBLE:
//                    //如果这里触发则表明 无双击事件  将会把事件发给子控件
//                    isRemoveClickDoubleMessage = true;
//                    isInterceptTouchEvent = false;
//
//                    long downTime = System.currentTimeMillis();
//                    dispatchTouchEvent(MotionEvent.obtain(downTime, downTime + 10, KeyEvent.ACTION_DOWN, motionEvent.getX(), motionEvent.getY(), 0));
//                    break;

                default:
                    break;
            }

        }
    }

    public interface DazzlePraiseActionListener {

        void onActionPraise();

    }


}
