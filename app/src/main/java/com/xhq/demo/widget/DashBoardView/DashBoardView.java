package com.xhq.demo.widget.DashBoardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.xhq.demo.R;

import java.util.List;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/1/10.
 *     Desc  : dash boar.
 *     Updt  : 2018/1/19.
 * </pre>
 */
public class DashBoardView extends View{

    private int mArcRadius;                                 // radius of arc
    private int mStartAngle;                                // start angle
    private int mSweepAngle;                                // draw sweep angle
    private int mPieceTotalCount;                           // piece total count
    private int mMinScaleCountInOnePiece;                   // a minimum number of graduations
    private int mArcColor;                                  // color of arc
    private int mRibbonWidth;                               // width of ribbon
    private int mRibbonModeTyPe;                            // Ribbon display mode

    private int mScaleValueTextSize;                        // scale value text size
    private int mScaleValueTextColor;                       // scale value text color
    private int mMinScaleValue;                             // dash board min scale value
    private int mMaxScaleValue;                             // dash board max scale value
    private float mCurrentScaleValue;                       // current scale value

    private String mUnitText = "";                          // unit
    private int mUnitTextSize;                              // unit font size
    private int mUnitTextRadius;                            // Unit font radius relative to the center of the circle

    private int mPointerRadius;                             // radius of pointer
    private int mCenterCircleRadius;                        // radius of center circle

    private int mBgColor;                                   // dash board background

    // ==============================================================================================
    // ==============================================================================================

    private RibbonMode mRibbonMode = RibbonMode.NORMAL;     // Ribbon display default mode
    private int mPieceScaleRadius;                          // radius of piece scale
    private int mMinScaleRadius;                            // radius of min scale radius
    private int mScaleValueRadius;                          // radius of scale value

    private int mMinScaleTotalCount;                        // total count of min scale
    private float mPieceScaleBetweenAngle;                  // piece scale between angle
    private float mMinScaleBetweenAngle;                    // min scale between angle
    private String[] mGraduations;                          // scale value arrays

    private List<HighlightCR> mRibbonHLColor;               // Highlight a collection of color objects
    private int mDashBoardVW;                               // widget width
    private int mDashBoardVH;                               // widget height
    private float mCenterX;                                 // circle center x
    private float mCenterY;                                 // circle center y

    private Paint mArcAndScalePaint;                        // paint of arc scale
    private Paint mRibbonPaint;                             // paint of ribbon
    private Paint mScaleValuePaint;                         // paint of scale value
    private Paint mPointerPaint;                            // paint of pointer
    private Paint mCurrentScaleValuePaint;                  // paint of current scale value

    private RectF mArcRectF;                                // rectangle of arc
    private RectF mRibbonRectF;                             // rectangle of ribbon
    private Rect mScaleValueRect;                           // rectangle of scale value
    private Rect mUnitRect;                                 // rectangle of unit
    private Rect mCurrentScaleValueRect;                    // rectangle of current reading value

    private Path path;                                      // pointer path

    private float mCurrentScaleValueAngle;                  // current scale value angle
    private boolean mTextColorFlag = true;                  // If you do not set the text color alone then the text and arc the same color
    private boolean mAnimEnable;                            // animation is available
    private long duration = 500;                            // animation duration
    private MyHandler mHandler;


    public DashBoardView(Context context){
        this(context, null);
    }


    public DashBoardView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }


    public DashBoardView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);

        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DashBoardView, defStyleAttr, 0);

            // dash board arc
            mArcRadius = a.getDimensionPixelSize(R.styleable.DashBoardView_arcRadius, dpToPx(80));
            mStartAngle = a.getInteger(R.styleable.DashBoardView_startAngle, 180);
            mSweepAngle = a.getInteger(R.styleable.DashBoardView_sweepAngle, 180);
            mArcColor = a.getColor(R.styleable.DashBoardView_arcColor, Color.WHITE);
            mRibbonWidth = a.getDimensionPixelSize(R.styleable.DashBoardView_ribbonWidth, 0);
            mRibbonModeTyPe = a.getInt(R.styleable.DashBoardView_ribbonMode, 0);

            // scale line
            mPieceTotalCount = a.getInteger(R.styleable.DashBoardView_pieceTotalCount, 10);
            mMinScaleCountInOnePiece = a.getInteger(R.styleable.DashBoardView_minScaleCountInOnePiece, 5);
            // scale value
            mScaleValueTextSize = a.getDimensionPixelSize(R.styleable.DashBoardView_scaleValueTextSize,
                                                          spToPx(12));
            mScaleValueTextColor = a.getColor(R.styleable.DashBoardView_ScaleValueTextColor, mArcColor);
            // min max scale value
            mMinScaleValue = a.getInteger(R.styleable.DashBoardView_minScaleValue, 0);
            mMaxScaleValue = a.getInteger(R.styleable.DashBoardView_maxScaleValue, 100);
            mCurrentScaleValue = a.getFloat(R.styleable.DashBoardView_currentScaleValue, 0.0f);

            // pointer
            mPointerRadius = a.getDimensionPixelSize(R.styleable.DashBoardView_pointerRadius, mArcRadius / 3 * 2);
            mCenterCircleRadius = a.getDimensionPixelSize(R.styleable.DashBoardView_centerCircleRadius,
                                                          mArcRadius / 17);

            // unit
            mUnitText = a.getString(R.styleable.DashBoardView_unitText);
            if(mUnitText == null) mUnitText = "";
            mUnitTextSize = a.getDimensionPixelSize(R.styleable.DashBoardView_unitTextSize, spToPx(14));
            mUnitTextRadius = a.getDimensionPixelSize(R.styleable.DashBoardView_unitTextRadius, mArcRadius / 3);

            // dash board background
            mBgColor = a.getColor(R.styleable.DashBoardView_bgColor, 0);

            a.recycle();
        }

        initObjects();
        initData();
    }


    private void initObjects(){

        // canvas rectangle
        mScaleValueRect = new Rect();
        mUnitRect = new Rect();
        mCurrentScaleValueRect = new Rect();

        // dash boar arc and scale line paint
        mArcAndScalePaint = new Paint();
        mArcAndScalePaint.setAntiAlias(true);
        mArcAndScalePaint.setColor(mArcColor);
        mArcAndScalePaint.setStyle(Paint.Style.STROKE);
        mArcAndScalePaint.setStrokeCap(Paint.Cap.ROUND);

        // ribbon paint
        mRibbonPaint = new Paint();
        mRibbonPaint.setAntiAlias(true);
        mRibbonPaint.setStyle(Paint.Style.STROKE);
        mRibbonPaint.setStrokeWidth(mRibbonWidth);

        // scale value paint
        mScaleValuePaint = new Paint();
        mScaleValuePaint.setAntiAlias(true);
        mScaleValuePaint.setColor(mScaleValueTextColor);
        mScaleValuePaint.setStyle(Paint.Style.STROKE);

        // pointer paint
        mPointerPaint = new Paint();
        mPointerPaint.setAntiAlias(true);

        // current scale value paint
        mCurrentScaleValuePaint = new Paint();
        mCurrentScaleValuePaint.setAntiAlias(true);
        mCurrentScaleValuePaint.setColor(mScaleValueTextColor);
        mCurrentScaleValuePaint.setStyle(Paint.Style.STROKE);
        mCurrentScaleValuePaint.setTextAlign(Paint.Align.CENTER);
        mCurrentScaleValuePaint.setTextSize(Math.max(mUnitTextSize, mScaleValueTextSize));
        mCurrentScaleValuePaint.getTextBounds(trimFloat(mCurrentScaleValue), 0,
                                              trimFloat(mCurrentScaleValue).length(), mCurrentScaleValueRect);

        // paint path
        path = new Path();

        mHandler = new MyHandler();
    }


    private String[] getScaleNumbers(){
        String[] pieceValueStr = new String[mPieceTotalCount + 1];
        for(int i = 0; i <= mPieceTotalCount; i++){
            if(i == 0){
                pieceValueStr[i] = String.valueOf(mMinScaleValue);
            }else if(i == mPieceTotalCount){
                pieceValueStr[i] = String.valueOf(mMaxScaleValue);
            }else{
                pieceValueStr[i] = String.valueOf(((mMaxScaleValue - mMinScaleValue) / mPieceTotalCount) * i);
            }
        }
        return pieceValueStr;
    }


    private void initData(){
        if(mSweepAngle > 360) mSweepAngle = 360;
//            throw new IllegalArgumentException("sweepAngle", "sweepAngle must less than 360 degree");

        mMinScaleRadius = mArcRadius - dpToPx(8);
        mPieceScaleRadius = mMinScaleRadius - dpToPx(4);
        mScaleValueRadius = mPieceScaleRadius - dpToPx(3);

        mMinScaleTotalCount = mPieceTotalCount * mMinScaleCountInOnePiece;
        mPieceScaleBetweenAngle = mSweepAngle / (float)mPieceTotalCount;
        mMinScaleBetweenAngle = mPieceScaleBetweenAngle / (float)mMinScaleCountInOnePiece;

        mGraduations = getScaleNumbers();

        switch(mRibbonModeTyPe){
            case 0:
                mRibbonMode = RibbonMode.NORMAL;
                break;
            case 1:
                mRibbonMode = RibbonMode.INNER;
                break;
            case 2:
                mRibbonMode = RibbonMode.OUTER;
                break;
        }

        int totalRadius;
        if(mRibbonMode == RibbonMode.OUTER){
            totalRadius = mArcRadius + mRibbonWidth;
        }else{
            totalRadius = mArcRadius;
        }

        mCenterX = mCenterY = 0.0f;
        if(mStartAngle <= 180 && mStartAngle + mSweepAngle >= 180){
            mDashBoardVW = totalRadius * 2 + getPaddingLeft() + getPaddingRight() + dpToPx(2) * 2;
        }else{
            float[] point1 = getArcCoordinatePoint(totalRadius, mStartAngle);
            float[] point2 = getArcCoordinatePoint(totalRadius, mStartAngle + mSweepAngle);
            float max = Math.max(Math.abs(point1[0]), Math.abs(point2[0]));
            mDashBoardVW = (int)(max * 2 + getPaddingLeft() + getPaddingRight() + dpToPx(2) * 2);
        }
        if((mStartAngle <= 90 && mStartAngle + mSweepAngle >= 90) ||
                (mStartAngle <= 270 && mStartAngle + mSweepAngle >= 270)){
            mDashBoardVH = totalRadius * 2 + getPaddingTop() + getPaddingBottom() + dpToPx(2) * 2;
        }else{
            float[] point1 = getArcCoordinatePoint(totalRadius, mStartAngle);
            float[] point2 = getArcCoordinatePoint(totalRadius, mStartAngle + mSweepAngle);
            float max = Math.max(Math.abs(point1[1]), Math.abs(point2[1]));
            mDashBoardVH = (int)(max * 2 + getPaddingTop() + getPaddingBottom() + dpToPx(2) * 2);
        }

        mCenterX = mDashBoardVW / 2.0f;
        mCenterY = mDashBoardVH / 2.0f;

        mArcRectF = new RectF(mCenterX - mArcRadius, mCenterY - mArcRadius, mCenterX + mArcRadius,
                              mCenterY + mArcRadius);
        int r = 0;
        if(mRibbonWidth > 0){
            if(mRibbonMode == RibbonMode.OUTER){
                r = mArcRadius + dpToPx(1) + mRibbonWidth / 2;
            }else if(mRibbonMode == RibbonMode.INNER){
                r = mArcRadius + dpToPx(1) - mRibbonWidth / 2;
            }
            mRibbonRectF = new RectF(mCenterX - r, mCenterY - r, mCenterX + r, mCenterY + r);
        }

        mCurrentScaleValueAngle = getAngleFromResult(mCurrentScaleValue);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode == MeasureSpec.EXACTLY){
            mDashBoardVW = widthSize;
        }else{
            if(widthMode == MeasureSpec.AT_MOST)
                mDashBoardVW = Math.min(mDashBoardVW, widthSize);
        }
        if(heightMode == MeasureSpec.EXACTLY){
            mDashBoardVH = heightSize;
        }else{
            int totalRadius;
            if(mRibbonMode == RibbonMode.OUTER){
                totalRadius = mArcRadius + mRibbonWidth;
            }else{
                totalRadius = mArcRadius;
            }
            if(mStartAngle >= 180 && mStartAngle + mSweepAngle <= 360){
                mDashBoardVH = totalRadius + mCenterCircleRadius + dpToPx(2) + dpToPx(25) +
                        getPaddingTop() + getPaddingBottom() + mCurrentScaleValueRect.height();
            }else{
                float[] point1 = getArcCoordinatePoint(totalRadius, mStartAngle);
                float[] point2 = getArcCoordinatePoint(totalRadius, mStartAngle + mSweepAngle);
                float maxY = Math.max(Math.abs(point1[1]) - mCenterY, Math.abs(point2[1]) - mCenterY);
                float f = mCenterCircleRadius + dpToPx(2) + dpToPx(25) + mCurrentScaleValueRect.height();
                float max = Math.max(maxY, f);
                mDashBoardVH = (int)(max + totalRadius + getPaddingTop() + getPaddingBottom() + dpToPx(2) * 2);
            }
            if(widthMode == MeasureSpec.AT_MOST)
                mDashBoardVH = Math.min(mDashBoardVH, widthSize);
        }
        setMeasuredDimension(mDashBoardVW, mDashBoardVH);
    }


    @Override
    protected void onDraw(Canvas canvas){
        if(mBgColor != 0) canvas.drawColor(mBgColor);
        drawRibbon(canvas);
        drawArc(canvas);
        drawScaleLine(canvas);
        drawCircleAndCurrentScaleValue(canvas);
        drawPointer(canvas);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);
    }


    /**
     * draw ribbon
     */
    private void drawRibbon(Canvas canvas){
        if(mRibbonMode != RibbonMode.NORMAL && mRibbonHLColor != null){
            for(int i = 0; i < mRibbonHLColor.size(); i++){
                HighlightCR highlightCR = mRibbonHLColor.get(i);
                if(highlightCR.getColor() == 0 || highlightCR.getSweepAngle() == 0) continue;
                mRibbonPaint.setColor(highlightCR.getColor());

                if(highlightCR.getStartAngle() + highlightCR.getSweepAngle() <= mStartAngle + mSweepAngle){
                    canvas.drawArc(mRibbonRectF, (float)(highlightCR.getStartAngle() - 0.7),
                                   (float)(highlightCR.getSweepAngle() + 1.5), false, mRibbonPaint);
                }else{
                    canvas.drawArc(mRibbonRectF, (float)(highlightCR.getStartAngle() -0.7),
                                   (float)(mStartAngle + mSweepAngle - highlightCR.getStartAngle() + 1.5), false,
                                   mRibbonPaint);
                    break;
                }
            }
        }
    }


    /**
     * draw dash board arc
     */
    private void drawArc(Canvas canvas){
        mArcAndScalePaint.setStrokeWidth(dpToPx(2));
        if(mRibbonMode == RibbonMode.NORMAL){
            if(mRibbonHLColor != null){
                for(int i = 0; i < mRibbonHLColor.size(); i++){
                    HighlightCR highlightCR = mRibbonHLColor.get(i);
                    if(highlightCR.getColor() == 0 || highlightCR.getSweepAngle() == 0) continue;
                    mArcAndScalePaint.setColor(highlightCR.getColor());

                    if(highlightCR.getStartAngle() + highlightCR.getSweepAngle() <= mStartAngle + mSweepAngle){
                        canvas.drawArc(mArcRectF, highlightCR.getStartAngle(),
                                       highlightCR.getSweepAngle(), false, mArcAndScalePaint);
                    }else{
                        canvas.drawArc(mArcRectF, highlightCR.getStartAngle(),
                                       mStartAngle + mSweepAngle - highlightCR.getStartAngle(), false,
                                       mArcAndScalePaint);
                        break;
                    }
                }
            }else{
                mArcAndScalePaint.setColor(mArcColor);
                canvas.drawArc(mArcRectF, mStartAngle, mSweepAngle, false, mArcAndScalePaint);
            }
        }else if(mRibbonMode == RibbonMode.OUTER){
            mArcAndScalePaint.setColor(mArcColor);
            canvas.drawArc(mArcRectF, mStartAngle, mSweepAngle, false, mArcAndScalePaint);
        }
    }


    /**
     * draw scale line (piece and min scale line)
     */
    private void drawScaleLine(Canvas canvas){
        mArcAndScalePaint.setStrokeWidth(dpToPx(2));
        for(int i = 0; i <= mPieceTotalCount; i++){
            //draw piece line
            float angle = i * mPieceScaleBetweenAngle + mStartAngle;
            float[] point1 = getArcCoordinatePoint(mArcRadius, angle);
            float[] point2 = getArcCoordinatePoint(mPieceScaleRadius, angle);

            if(mRibbonMode == RibbonMode.NORMAL && mRibbonHLColor != null){
                for(int j = 0; j < mRibbonHLColor.size(); j++){
                    HighlightCR highlightCR = mRibbonHLColor.get(j);
                    if(highlightCR.getColor() == 0 || highlightCR.getSweepAngle() == 0) continue;
                    if(angle <= highlightCR.getStartAngle() + highlightCR.getSweepAngle()){
                        mArcAndScalePaint.setColor(highlightCR.getColor());
                        break;
                    }else{
                        mArcAndScalePaint.setColor(mArcColor);
                    }
                }
            }else{
                mArcAndScalePaint.setColor(mArcColor);
            }
            canvas.drawLine(point1[0], point1[1], point2[0], point2[1], mArcAndScalePaint);

            //draw scale value of one piece scale line
            mScaleValuePaint.setTextSize(mScaleValueTextSize);
            String number = mGraduations[i];
            mScaleValuePaint.getTextBounds(number, 0, number.length(), mScaleValueRect);
            if(angle % 360 > 135 && angle % 360 < 225){
                mScaleValuePaint.setTextAlign(Paint.Align.LEFT);
            }else if((angle % 360 >= 0 && angle % 360 < 45) || (angle % 360 > 315 && angle % 360 <= 360)){
                mScaleValuePaint.setTextAlign(Paint.Align.RIGHT);
            }else{
                mScaleValuePaint.setTextAlign(Paint.Align.CENTER);
            }
            float[] scaleValuePoint = getArcCoordinatePoint(mScaleValueRadius, angle);
            if(i == 0 || i == mPieceTotalCount){
                canvas.drawText(number, scaleValuePoint[0], scaleValuePoint[1] + (mScaleValueRect.height() / 2),
                                mScaleValuePaint);
            }else{
                canvas.drawText(number, scaleValuePoint[0], scaleValuePoint[1] + mScaleValueRect.height(),
                                mScaleValuePaint);
            }
        }

        //draw min scale line
        mArcAndScalePaint.setStrokeWidth(dpToPx(1));
        for(int i = 0; i < mMinScaleTotalCount; i++){
            if(i % mMinScaleCountInOnePiece != 0){
                float angle = i * mMinScaleBetweenAngle + mStartAngle;
                float[] point1 = getArcCoordinatePoint(mArcRadius, angle);
                float[] point2 = getArcCoordinatePoint(mMinScaleRadius, angle);

                if(mRibbonMode == RibbonMode.NORMAL && mRibbonHLColor != null){
                    for(int j = 0; j < mRibbonHLColor.size(); j++){
                        HighlightCR highlightCR = mRibbonHLColor.get(j);
                        if(highlightCR.getColor() == 0 || highlightCR.getSweepAngle() == 0) continue;
                        if(angle <= highlightCR.getStartAngle() + highlightCR.getSweepAngle()){
                            mArcAndScalePaint.setColor(highlightCR.getColor());
                            break;
                        }else{
                            mArcAndScalePaint.setColor(mArcColor);
                        }
                    }
                }else{
                    mArcAndScalePaint.setColor(mArcColor);
                }
//                mArcAndScalePaint.setStrokeWidth(dpToPx(1));
                canvas.drawLine(point1[0], point1[1], point2[0], point2[1], mArcAndScalePaint);
            }
        }
    }


    /**
     * draw center circle and unit and current scale value
     */
    private void drawCircleAndCurrentScaleValue(Canvas canvas){
        //draw unit
        mScaleValuePaint.setTextSize(mUnitTextSize);
        mScaleValuePaint.setTextAlign(Paint.Align.CENTER);
        mScaleValuePaint.getTextBounds(mUnitText, 0, mUnitText.length(), mUnitRect);
        canvas.drawText(mUnitText, mCenterX, mCenterY - mUnitTextRadius + mUnitRect.height(), mScaleValuePaint);

        //draw center circle
        mPointerPaint.setStyle(Paint.Style.FILL);
        mPointerPaint.setColor(Color.parseColor("#e4e9e9"));
        canvas.drawCircle(mCenterX, mCenterY - dpToPx(4), mCenterCircleRadius + dpToPx(1), mPointerPaint);

        // draw center circle
        mPointerPaint.setStyle(Paint.Style.STROKE);
        mPointerPaint.setStrokeWidth(dpToPx(2));
        mPointerPaint.setColor(mArcColor);
        canvas.drawCircle(mCenterX, mCenterY - dpToPx(4), mCenterCircleRadius + dpToPx(2), mPointerPaint);

        // draw current scale value
        mCurrentScaleValuePaint.setTextSize(48);
        canvas.drawText(trimFloat(mCurrentScaleValue), mCenterX,
                        mCenterY + mCenterCircleRadius - mUnitTextRadius + mUnitRect.height() + dpToPx(5),
                        mCurrentScaleValuePaint);
    }


    /**
     * draw pointer
     */
    private void drawPointer(Canvas canvas){
        mPointerPaint.setStyle(Paint.Style.FILL);
        mPointerPaint.setColor(mScaleValueTextColor);
        path.reset();
        float[] point1 = getArcCoordinatePoint(mCenterCircleRadius / 2, mCurrentScaleValueAngle + 90);
        path.moveTo(point1[0], point1[1] - dpToPx(4));
        float[] point2 = getArcCoordinatePoint(mCenterCircleRadius / 2, mCurrentScaleValueAngle - 90);
        path.lineTo(point2[0], point2[1] - dpToPx(4));
        float[] point3 = getArcCoordinatePoint(mPointerRadius, mCurrentScaleValueAngle);
        path.lineTo(point3[0], point3[1]);
        path.close();
        canvas.drawPath(path, mPointerPaint);
        // draw the circular arc effect at the bottom of the pointer
        canvas.drawCircle((point1[0] + point2[0]) / 2, (point1[1] + point2[1]) / 2 - dpToPx(4),
                          mCenterCircleRadius / 2, mPointerPaint);
    }


    /**
     * According to the coordinates of the center of
     * the circle radius fan angle calculate the xy coordinates of
     * the intersection of the fan-shaped final line and the arc
     */
    public float[] getArcCoordinatePoint(int radius, float cirAngle){
        float[] point = new float[2];

        // rad = 2π/360 * angle w-->rad
        double arcAngle = Math.toRadians(cirAngle);
        if(cirAngle < 90){
            point[0] = (float)(mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float)(mCenterY + Math.sin(arcAngle) * radius);
        }else if(cirAngle == 90){
            point[0] = mCenterX;
            point[1] = mCenterY + radius;
        }else if(cirAngle > 90 && cirAngle < 180){
            arcAngle = Math.PI * (180 - cirAngle) / 180.0;
            point[0] = (float)(mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float)(mCenterY + Math.sin(arcAngle) * radius);
        }else if(cirAngle == 180){
            point[0] = mCenterX - radius;
            point[1] = mCenterY;
        }else if(cirAngle > 180 && cirAngle < 270){
            arcAngle = Math.PI * (cirAngle - 180) / 180.0;
            point[0] = (float)(mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float)(mCenterY - Math.sin(arcAngle) * radius);
        }else if(cirAngle == 270){
            point[0] = mCenterX;
            point[1] = mCenterY - radius;
        }else{
            arcAngle = Math.PI * (360 - cirAngle) / 180.0;
            point[0] = (float)(mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float)(mCenterY - Math.sin(arcAngle) * radius);
        }

        return point;
    }


    /**
     * Get the angular position by current scale value
     */
    private float getAngleFromResult(float result){
        if(result > mMaxScaleValue)
            return mMaxScaleValue;
        return mSweepAngle * (result - mMinScaleValue) / (mMaxScaleValue - mMinScaleValue) + mStartAngle;
    }


    /**
     * float类型如果小数点后为零则显示整数否则保留
     */
    public static String trimFloat(float value){
        if(Math.round(value) - value == 0){
            return String.valueOf((long)value);
        }
        return String.valueOf(value);
    }


    // =======================================================================================================
    // =======================================================================================================


    public int getArcRadius(){
        return mArcRadius;
    }


    public void setArcRadius(int arcRadius){
        mArcRadius = dpToPx(arcRadius);
        initData();
        invalidate();
    }


    public int getStartAngle(){
        return mStartAngle;
    }


    public void setStartAngle(int startAngle){
        mStartAngle = startAngle;
        initData();
        invalidate();
    }


    public int getSweepAngle(){
        return mSweepAngle;
    }


    public void setSweepAngle(int sweepAngle){
        mSweepAngle = sweepAngle;
        initData();
        invalidate();
    }


    public int getPieceTotalCount(){
        return mPieceTotalCount;
    }


    public void setPieceTotalCount(int pieceTotalCount){
        mPieceTotalCount = pieceTotalCount;
        initData();
        invalidate();
    }


    public int getMinScaleCountInOnePiece(){
        return mMinScaleCountInOnePiece;
    }


    public void setMinScaleCountInOnePiece(int minScaleCountInOnePiece){
        mMinScaleCountInOnePiece = minScaleCountInOnePiece;
        initData();
        invalidate();
    }


    public int getArcColor(){
        return mArcColor;
    }


    public void setArcColor(int arcColor){
        mArcColor = arcColor;
        mArcAndScalePaint.setColor(arcColor);
        if(mTextColorFlag){
            mScaleValueTextColor = mArcColor;
            mScaleValuePaint.setColor(arcColor);
        }
        invalidate();
    }


    public int getScaleValueTextSize(){
        return mScaleValueTextSize;
    }


    public void setScaleValueTextSize(int scaleValueTextSize){
        mScaleValueTextSize = spToPx(scaleValueTextSize);
        initData();
        invalidate();
    }


    public int getTextColor(){
        return mScaleValueTextColor;
    }


    public void setTextColor(int textColor){
        mScaleValueTextColor = textColor;
        mTextColorFlag = false;
        mScaleValuePaint.setColor(textColor);
        invalidate();
    }


    public String getUnitText(){
        return mUnitText;
    }


    public void setUnitText(String unitText){
        mUnitText = unitText;
        invalidate();
    }


    public int getUnitTextSize(){
        return mUnitTextSize;
    }


    public void setUnitTextSize(int unitTextSize){
        mUnitTextSize = spToPx(unitTextSize);
        initData();
        invalidate();
    }


    public int getUnitRadius(){
        return mUnitTextRadius;
    }


    public void setUnitRadius(int unitRadius){
        mUnitTextRadius = dpToPx(unitRadius);
        initData();
        invalidate();
    }


    public int getPointerRadius(){
        return mPointerRadius;
    }


    public void setPointerRadius(int pointerRadius){
        mPointerRadius = dpToPx(pointerRadius);
        initData();
        invalidate();
    }


    public int getCircleCenterRadius(){
        return mCenterCircleRadius;
    }


    public void setCircleCenterRadius(int circleCenterRadius){
        mCenterCircleRadius = dpToPx(circleCenterRadius);
        initData();
        invalidate();
    }


    public int getMinScaleValue(){
        return mMinScaleValue;
    }


    public void setMinScaleValue(int minScaleValue){
        mMinScaleValue = minScaleValue;
        initData();
        invalidate();
    }


    public int getMaxScaleValue(){
        return mMaxScaleValue;
    }


    public void setMaxScaleValue(int maxScaleValue){
        mMaxScaleValue = maxScaleValue;
        initData();
        invalidate();
    }


    public float getCurrentScaleValue(){
        return mCurrentScaleValue;
    }


    public void setCurrentScaleValue(float currentScaleValue){
        mCurrentScaleValue = currentScaleValue;
        initData();
        if(!mAnimEnable)
            invalidate();
    }


    public void setCurrentScaleValue(float currentScaleValue, boolean animEnable, long duration){
        mHandler.preValue = mCurrentScaleValue;
        mAnimEnable = animEnable;
        initData();
        if(!mAnimEnable){
            invalidate();
        }else{
            this.duration = duration;
            mCurrentScaleValue = currentScaleValue;
            mHandler.endValue = currentScaleValue;
            mHandler.deltaValue = Math.abs(mHandler.endValue - mHandler.preValue);
            mHandler.sendEmptyMessage(0);
        }
    }


    public int getRibbonWidth(){
        return mRibbonWidth;
    }


    public void setRibbonWidth(int ribbonWidth){
        mRibbonWidth = dpToPx(ribbonWidth);
        initData();
        invalidate();
    }


    public RibbonMode getRibbonMode(){
        return mRibbonMode;
    }


    public void setRibbonMode(RibbonMode ribbonMode){
        this.mRibbonMode = ribbonMode;
        switch(ribbonMode){
            case NORMAL:
                mRibbonModeTyPe = 0;
                break;
            case INNER:
                mRibbonModeTyPe = 1;
                break;
            case OUTER:
                mRibbonModeTyPe = 2;
                break;
        }
        initData();
        invalidate();
    }


    public int getPieceScaleRadius(){
        return mPieceScaleRadius;
    }


    public void setPieceScaleRadius(int pieceScaleRadius){
        mPieceScaleRadius = dpToPx(pieceScaleRadius);
        initData();
        invalidate();
    }


    public int getMinScaleRadius(){
        return mMinScaleRadius;
    }


    public void setMinScaleRadius(int MinScaleRadius){
        mMinScaleRadius = dpToPx(MinScaleRadius);
        initData();
        invalidate();
    }


    public int getScaleValueRadius(){
        return mScaleValueRadius;
    }


    public void setScaleValueRadius(int scaleValueRadius){
        mScaleValueRadius = dpToPx(scaleValueRadius);
        initData();
        invalidate();
    }


    public void setRibbonHLColorAndRange(List<HighlightCR> ribbonHLColor){
        mRibbonHLColor = ribbonHLColor;
        mRibbonPaint.setStrokeWidth(mRibbonWidth);
        invalidate();
    }


    public enum RibbonMode{
        NORMAL,
        INNER,
        OUTER
    }


    public int getBgColor(){
        return mBgColor;
    }


    public void setBgColor(int mBgColor){
        this.mBgColor = mBgColor;
        invalidate();
    }


    public boolean isAnimEnable(){
        return mAnimEnable;
    }


    public void setAnimEnable(boolean animEnable){
        mAnimEnable = animEnable;
        if(mAnimEnable){
            mHandler.endValue = mCurrentScaleValue;
            mHandler.sendEmptyMessage(0);
        }
    }


    private int dpToPx(int dp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    private int spToPx(int sp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }


    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler{

        float preValue;
        float endValue;
        float deltaValue;

        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what == 0){
                if(preValue > endValue){
                    preValue -= 1;
                }else if(preValue < endValue){
                    preValue += 1;
                }
                if(Math.abs(preValue - endValue) > 1){
                    mCurrentScaleValue = preValue;
                    long t = (long)(duration / deltaValue);
                    sendEmptyMessageDelayed(0, t);
                }else{
                    mCurrentScaleValue = endValue;
                }
                mCurrentScaleValueAngle = getAngleFromResult(mCurrentScaleValue);
                invalidate();
            }
        }
    }
}
