package com.tencent.liteav.ugsv.common.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.tencent.liteav.ugsv.R;
import com.tencent.liteav.ugsv.common.utils.DensityUtil;

/**
 * single click to record video
 * <p>
 * Created by ychen on 2018/3/29.
 * <p>
 * updt: xhq 2018/4/7
 */
public class ClickToRecordView extends View implements View.OnClickListener{

    private static final int mDefVW = 350;
    private static final int mDefVH = 350;

    private float mCenterX, mCenterY;                   // circle center x, y

    private float mStaticCircleRadius;
    private int mOutSideCircleRadius;
    private int mColor;
    private int mBoxColor;
    private int halfBoxWidth;

    private Paint mPaint;
    private Xfermode modeSrcOut;

    private ValueAnimator animator;
    private RectF rect;

    private float mInnerCircleRadius;

    private float mStartAnimValue;
    private float mEndAnimValue;
    private boolean isOnClick;


    public boolean isAnimatorRun(){
        return isOnClick;
    }

    public ClickToRecordView(Context context){
        this(context, null);
    }


    public ClickToRecordView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);

        init();

    }


    private void init(){
        initData();
        initObj();
    }


    private void initData(){

        mStaticCircleRadius = dpToPx(75) / 2;

        mColor = Color.parseColor("#C84646");   // C84646   ED879B
//        mBoxColor = Color.parseColor("#FE2C55");
        mBoxColor = ContextCompat.getColor(getContext(), R.color.bg_light_red);
        halfBoxWidth = dpToPx(15);

        mStartAnimValue = DensityUtil.dip2px(getContext(), 40);
        mEndAnimValue = DensityUtil.dip2px(getContext(), 50);
    }


    private void initObj(){
        setOnClickListener(this);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL);

        modeSrcOut = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

        rect = new RectF();

        animator = ValueAnimator.ofFloat(mStartAnimValue, mEndAnimValue);
        animator.setDuration(800);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);

        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(valueAnimator -> {
            mInnerCircleRadius = (float)valueAnimator.getAnimatedValue();
//            KLog.e("xhq - animator", "mInnerCircleRadius" + mInnerCircleRadius);
            invalidate();
        });
    }


    public void startAnimation(boolean isAutoStart){
        isOnClick = isAutoStart;
        animator.setFloatValues(mStartAnimValue, mEndAnimValue);
        animator.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredWidth = measureViewW(widthMeasureSpec, mDefVW);
        int measuredHeight = measureViewH(heightMeasureSpec, mDefVH);
        setMeasuredDimension(measuredWidth, measuredHeight);

        int min = Math.min(measuredWidth, measuredHeight);

        mOutSideCircleRadius = min / 2;

        //关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mInnerCircleRadius = mOutSideCircleRadius - 15;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);

        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;
    }


    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        if(isOnClick){
            int sc = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
            mPaint.setColor(mColor);

            canvas.drawBitmap(getBitmap(mOutSideCircleRadius), 0, 0, mPaint);             // outside circle
            mPaint.setXfermode(modeSrcOut);

            canvas.drawBitmap(getBitmap(mInnerCircleRadius), 0, 0, mPaint);   // inner circle
            mPaint.setXfermode(null);
            canvas.restoreToCount(sc);
            mPaint.setColor(mBoxColor);

            mPaint.setStrokeCap(Paint.Cap.ROUND);                                     // for the line, used with StrokeWidth attribute
            mPaint.setStrokeJoin(Paint.Join.ROUND);

            rect.set(mOutSideCircleRadius - halfBoxWidth, mOutSideCircleRadius - halfBoxWidth,
                     mOutSideCircleRadius + halfBoxWidth, mOutSideCircleRadius + halfBoxWidth);
            canvas.drawRoundRect(rect, dpToPx(5), dpToPx(5), mPaint);
        }else{

            mPaint.setColor(mColor);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(20);
            canvas.drawCircle(mCenterX, mCenterY, mStaticCircleRadius, mPaint); // static outside circle

            float strokeWidth = mPaint.getStrokeWidth();
            mPaint.reset();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mBoxColor);
            mPaint.setAntiAlias(true);
            canvas.drawCircle(mCenterX, mCenterY, mStaticCircleRadius - strokeWidth + 5, mPaint);   // static inner circle
        }

    }


    @Override
    protected void onDetachedFromWindow(){
        super.onDetachedFromWindow();
        if(animator != null){
            animator.removeAllUpdateListeners();
            animator.cancel();
            isOnClick = false;
            invalidate();
        }
    }


    @Override
    public void onClick(View v){
        isOnClick = !isOnClick;

        if(!isOnClick){
            animator.cancel();
            invalidate();
        }else {
            this.startAnimation(true);
        }

        if(null != mClickListener){
            mClickListener.onClick(v, isAnimatorRun());
        }
    }


    public Bitmap getBitmap(float radius){
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mColor);
        canvas.drawCircle(getWidth() / 2, getWidth() / 2, radius, paint);
        return bitmap;
    }


    private int measureViewW(int widthMeasureSpec, int defVW){
        int width = 0;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        switch(widthMode){
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                width = defVW;
                break;
        }
        return width;
    }


    private int measureViewH(int heightMeasureSpec, int defVH){
        int height = 0;
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        switch(heightMode){
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                height = defVH;
                break;
        }
        return height;
    }


    private int dpToPx(int dp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    private int spToPx(int sp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }


    private MyOnClickListener mClickListener;


    public void setMyOnclickListener(MyOnClickListener listener){
        this.mClickListener = listener;
    }


    public interface MyOnClickListener{
        void onClick(View view, boolean isAnimatorRun);
    }

}
