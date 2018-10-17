package com.xhq.demo.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.xhq.demo.R;


/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/3/28.
 *     Desc  : Video Record Button.
 *     Updt  : Description.
 * </pre>
 */
public class VideoRecordButton extends View{

    public int mBtnBgColor;                             // video record button background color
    public int mBtnTextColor;                           // the text color


    private int mViewDefW, mViewDefH;                   // widget default width height
    private float mRadius, mRingRadius, mRingWidth;
    private float mAnimatorValue;
    private float mCurrentValue;
    private float mDefStrokeWidth;                      // default Stroke width
    private String mBtnText;
    private float mTextSize;

    private Path mPath;
    private Paint mPaint, mTextPaint;


    private float mCenterX, mCenterY;                   // circle center x, y
    private boolean mTouchDown;
    private float mLastScreenX, mLastScreenY;           // the last time the screen coordinates
    private int mLeft = -1, mTop, mRight, mBottom;           // original position

    final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.5F, -0.5F);


    public VideoRecordButton(Context context){
        this(context, null);
    }


    public VideoRecordButton(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }


    public VideoRecordButton(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr){

        initData();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VideoRecordButton, defStyleAttr, 0);

        mBtnBgColor = ta.getColor(R.styleable.VideoRecordButton_btnBgColor, mBtnBgColor);
        mBtnTextColor = ta.getColor(R.styleable.VideoRecordButton_btnTextColor, mBtnTextColor);
        mBtnText = ta.getString(R.styleable.VideoRecordButton_btnText);
        if(mBtnText == null) mBtnText = "";
        mRingWidth = ta.getDimension(R.styleable.VideoRecordButton_ringWidth, mRingWidth);

        ta.recycle();

        initObjects();
    }


    private void initData(){

        mViewDefW = dpToPx(75);
        mViewDefH = dpToPx(75);

//        mBtnBgColor = ContextCompat.getColor(getContext(), R.color.bg_light_red);
        mBtnBgColor = Color.parseColor("#FE2C55");  // #FE2C55  #FF4081
        mBtnTextColor = ContextCompat.getColor(getContext(), android.R.color.white);
        mRingWidth = dpToPx(10);
        mDefStrokeWidth = mRingWidth / 2;
        mCurrentValue = mDefStrokeWidth;

        mTextSize = spToPx(16);

        valueAnimator.addUpdateListener(animation -> {
            mAnimatorValue = (float)valueAnimator.getAnimatedValue();
//            Log.d("xhq", "value:" + mAnimatorValue);
            invalidate();
        });

        valueAnimator.setDuration(1500);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
    }


    private Paint generatePaint(int color, Paint.Style style, float width){
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(style);
        paint.setStrokeWidth(width);
//        paint.setStrokeCap(Paint.Cap.ROUND);
        return paint;
    }


    private void initObjects(){

        mPath = new Path();
        mPaint = generatePaint(mBtnBgColor, Paint.Style.STROKE, mCurrentValue);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mBtnTextColor);
        mTextPaint.setTextSize(mTextSize);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = measureViewWH(widthMeasureSpec, mViewDefW);
        int height = measureViewWH(heightMeasureSpec, mViewDefH);

        mRadius = Math.min(width, height) / 2;
        mRingRadius = mRadius + mRingWidth;

        height = width = (int)(mRingRadius * 2 + mRingWidth + mDefStrokeWidth);

        mCenterX = width / 2;
        mCenterY = height / 2;

//        Log.e("xhq", "width:" + width + ",height:" + height);
        setMeasuredDimension(width, height);
    }


    /**
     * when customize view, call onMeasure(), measure the width and height of the view
     * @param whMeasureSpec widthMeasureSpec or heightMeasureSpec
     * @param defValue the default width or height of the view
     * @return Width or height that has been measured
     */
    public int measureViewWH(int whMeasureSpec, int defValue){
        int length = 0;
        int whSize = View.MeasureSpec.getSize(whMeasureSpec);
        int whMode = View.MeasureSpec.getMode(whMeasureSpec);

        switch(whMode){
            case View.MeasureSpec.EXACTLY:
                length = whSize;
                break;
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.UNSPECIFIED:
                length = defValue;
                break;
        }
        return length;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);
        if(mLeft < 0){
            mLeft = left;
            mTop = top;
            mRight = right;
            mBottom = bottom;

        }
    }


    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(mTouchDown){
            drawRing(canvas);
        }else{
            drawCircle(canvas);
        }
    }


    private void drawRing(Canvas canvas){
        mPath.reset();
        mPath.addCircle(mCenterX, mCenterY, mRingRadius, Path.Direction.CW);
        mPaint.setStyle(Paint.Style.STROKE);
        mCurrentValue = mCurrentValue + mAnimatorValue;
//        Log.d("xhq", "current:" + mCurrentValue);
        mPaint.setStrokeWidth(mCurrentValue);
        canvas.drawPath(mPath, mPaint);
    }


    private void drawCircle(Canvas canvas){
        mPath.reset();
        mPath.addCircle(mCenterX, mCenterY, mRadius, Path.Direction.CW);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mPath, mPaint);
        //draw text
        float y = mCenterY + mTextSize / 3;

        mTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(mBtnText, mCenterX, y, mTextPaint);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction()){

            case MotionEvent.ACTION_DOWN:
                mLastScreenX = event.getRawX();
                mLastScreenY = event.getRawY();
                mTouchDown = true;
                valueAnimator.start();
                postInvalidate();
                if(mOnHoldDownListener != null){
                    mOnHoldDownListener.onTouch(true);
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                int dx = (int)(event.getRawX() - mLastScreenX);
                int dy = (int)(event.getRawY() - mLastScreenY);
                int l = getLeft() + dx;
                int b = getBottom() + dy;
                int r = getRight() + dx;
                int t = getTop() + dy;

                layout(l, t, r, b);

                mLastScreenX = (int)event.getRawX();
                mLastScreenY = (int)event.getRawY();
                postInvalidate();
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mTouchDown = false;
                mCurrentValue = mDefStrokeWidth;
                valueAnimator.cancel();
                layout(mLeft, mTop, mRight, mBottom);
                postInvalidate();
                if(mOnHoldDownListener != null){
                    mOnHoldDownListener.onTouch(false);
                }
                return true;
            default:
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(valueAnimator != null) {
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.cancel();
            invalidate();
        }
    }

    private int dpToPx(int dp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    private int spToPx(int sp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }


    private OnHoldDownListener mOnHoldDownListener;


    public void setOnHoldDownListener(OnHoldDownListener listener){
        mOnHoldDownListener = listener;
    }


    public interface OnHoldDownListener{
        void onTouch(boolean onDown);
    }



}
