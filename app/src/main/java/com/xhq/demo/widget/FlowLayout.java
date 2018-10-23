package com.xhq.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.xhq.demo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/09/30.
 *     Desc  : Description.
 *     Updt  : 2018/10/18.
 * </pre>
 */
public class FlowLayout extends ViewGroup{
    private static final String TAG = FlowLayout.class.getSimpleName();

    private Context mContext;
    private int usefulWidth; // the space of a line we can use(line's width minus the sum of left and right padding
    List<View> childList = new ArrayList<>();
    List<Integer> lineNumList = new ArrayList<>();


    private int mRowSpacing = 0; // the spacing between rows in flowlayout
    private int mColumnSpacing = 0;
    private int mMaxLine = 0;

    private SparseArray<Rect> childLTRBList = new SparseArray<>();
    private int limitCountInLine = Integer.MAX_VALUE;


    /**
     * FlowLayout not support checked
     */
    public static final int FLOW_TAG_CHECKED_NONE = 0;
    /**
     * FlowLayout support single-select
     */
    public static final int FLOW_TAG_CHECKED_SINGLE = 1;
    /**
     * FlowLayout support multi-select
     */
    public static final int FLOW_TAG_CHECKED_MULTI = 2;

    /**
     * Should be used by subclasses to listen to changes in the dataset
     */
    AdapterDataSetObserver mDataSetObserver;

    /**
     * The adapter containing the data to be displayed by this view
     */
    ListAdapter mAdapter;

    /**
     * the tag click event callback
     */
    OnTagClickListener mOnTagClickListener;

    /**
     * the tag select event callback
     */
    OnTagSelectListener mOnTagSelectListener;

    /**
     * 标签流式布局选中模式，默认是不支持选中的
     */
    private int mTagCheckMode = FLOW_TAG_CHECKED_NONE;

    /**
     * 存储选中的tag
     */
    private SparseBooleanArray mCheckedTagArray = new SparseBooleanArray();


    public FlowLayout(Context context){
        this(context, null);
    }


    public FlowLayout(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }


    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        mRowSpacing = ta.getDimensionPixelSize(R.styleable.FlowLayout_rowSpacing, 0);
        mColumnSpacing = ta.getDimensionPixelSize(R.styleable.FlowLayout_columnSpacing, 0);
//        mMaxLine = ta.getInteger(R.styleable.FlowLayout_maxLine, 0);
        ta.recycle();
    }


    /**
     * The first measurement calculation method
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        final int lineUsedWidth = getPaddingLeft() + getPaddingRight();

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int parentUsedWidth = getPaddingLeft() + getPaddingRight();
        int parentUsedHeight = getPaddingTop();
        int currentHeight = 0;      // auxiliary record the height of the current control

        final int childCount = this.getChildCount();
        for(int i = 0; i < childCount; i++){
            View child = this.getChildAt(i);
            if(child.getVisibility() == GONE) continue;

            LayoutParams childLp = child.getLayoutParams();
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams mlp = (MarginLayoutParams)childLp;
            final int childActualWidth = mlp.leftMargin + child.getMeasuredWidth() + mlp.rightMargin;
            final int childActualHeight = mlp.topMargin + child.getMeasuredHeight() + mlp.bottomMargin;

            if(parentUsedWidth + childActualWidth > widthSize){
                //approach the limit of width and move to next line
                parentUsedWidth = lineUsedWidth;
                parentUsedHeight += currentHeight + mRowSpacing;
            }

            parentUsedWidth += childActualWidth + mColumnSpacing;
            if(childActualHeight > currentHeight) currentHeight = childActualHeight;
        }

        widthSize = Math.max(widthSize, parentUsedWidth);
        int heightSize = parentUsedHeight + currentHeight + getPaddingBottom();
//        setMeasuredDimension(widthSize, heightMode == MeasureSpec.EXACTLY ? heightSize : heightSize);

        int resolveWidth = View.resolveSize(widthSize, widthMeasureSpec);
        int resolveHeight = View.resolveSize(heightSize, heightMeasureSpec);
        setMeasuredDimension(resolveWidth, resolveHeight);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b){
        final int widgetWidth = r - l;
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();

        usefulWidth = widgetWidth - paddingLeft - paddingRight;

        final int lineUsedWidth = getPaddingLeft() + getPaddingRight();

        int lineX = paddingLeft;
        int lineY = getPaddingTop();
        int parentUsedWidth = paddingLeft + paddingRight;
        int parentUsedHeight = 0;

        int childCountInLine = 0;

        lineNumList.clear();
        for(int i = 0; i < this.getChildCount(); i++){
            View child = this.getChildAt(i);
            if(child.getVisibility() == GONE) continue;

            int left, top, right, bottom;
            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();

            MarginLayoutParams mlp = (MarginLayoutParams)child.getLayoutParams();
            final int childActualWidth = mlp.leftMargin + childWidth + mlp.rightMargin;
            final int childActualHeight = mlp.topMargin + childHeight + mlp.bottomMargin;

            if(childCountInLine > 3 || parentUsedWidth + childActualWidth > widgetWidth){
                lineNumList.add(childCountInLine);
                lineX = paddingLeft;
                lineY += parentUsedHeight + mRowSpacing;
                parentUsedWidth = lineUsedWidth;
                parentUsedHeight = 0;
                childCountInLine = 0;
            }

            left = lineX + mlp.leftMargin;
            top = lineY + mlp.topMargin;
            right = left + childWidth;
            bottom = top + childHeight;
            child.layout(left, top, right, bottom);
            childCountInLine++;
            if(childActualHeight > parentUsedHeight) parentUsedHeight = childActualHeight;

            final int temChildWidth = childActualWidth + mColumnSpacing;
            parentUsedWidth += temChildWidth;
            lineX += temChildWidth;
        }
        // add the num of last line
        lineNumList.add(childCountInLine);
    }


//    /**
//     * calculate the height of the container while recording the top left and bottom right coordinates
//     */
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
//
//        final int parentUsedWidth = getPaddingLeft() + getPaddingRight();
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//
//        int tempStartX = getPaddingLeft();
//        int tempStartY = getPaddingTop();
//        int currLineWidth = getPaddingLeft();        // auxiliary record the width of the current control
//        int currLineHeight = getPaddingTop();            // auxiliary record the height of the current control
//
//        int childCount = this.getChildCount();
//        for(int i = 0; i < childCount; i++){
//            View child = this.getChildAt(i);
//            if(child.getVisibility() == GONE) continue;
//
//            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
////            measureChild(child, widthMeasureSpec, heightMeasureSpec);
//            MarginLayoutParams childMLP = (MarginLayoutParams)child.getLayoutParams();
//            int childMarginWidth = childMLP.leftMargin + childMLP.rightMargin;
//            int childMarginHeight = childMLP.topMargin + childMLP.bottomMargin;
//
//            int childWidth = child.getMeasuredWidth();
//            int childHeight = child.getMeasuredHeight();
//            int childActualWidth = childMarginWidth + childWidth;
//            int childActualHeight = childMarginHeight + childHeight;
//
//            if(i == 0){
//                currLineHeight += childActualHeight;
//            }else{
//                int tempLineWidth = mColumnSpacing + childActualWidth;
//                if(currLineWidth + tempLineWidth <= widthSize - parentUsedWidth){
//                    // layout's  l, t(because don't wrap, so 't' does not change)
//                    tempStartX = currLineWidth + mColumnSpacing;
//                    // higher efficiency
//                    if(childActualHeight > currLineHeight) currLineHeight = childActualHeight;
////                    currLineHeight = Math.max(currLineHeight, childActualHeight);
//                }else {
//                    //approach the limit of width and move to next line
//                    tempStartX = getPaddingLeft();
//                    tempStartY = currLineHeight + mRowSpacing;
//
//                    currLineHeight = tempStartY + childActualHeight;
//                }
//            }
//            currLineWidth = tempStartX + childActualWidth;
//            int left = tempStartX + childMLP.leftMargin;
//            int top = tempStartY + childMLP.topMargin;
//            childLTRBList.append(i, new Rect(left, top, left + childWidth, top + childHeight));
//        }
//
//        widthSize = Math.max(widthSize, currLineWidth);
//
////        setMeasuredDimension(widthSize,
////           heightMode == MeasureSpec.EXACTLY ? heightSize : parentUsedHeight + currLineHeight + paddingBottom);
//
//        int resolveWidth = View.resolveSize(widthSize, widthMeasureSpec);
//        int resolveHeight = View.resolveSize(currLineHeight + getPaddingBottom(), heightMeasureSpec);
//        setMeasuredDimension(resolveWidth, resolveHeight);
//    }
//
//
//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b){
//        for(int i = 0; i < getChildCount(); i++){
//            View child = getChildAt(i);
//            if (child.getVisibility() == View.GONE) continue;
//            Rect rect = childLTRBList.get(i);
//            child.layout(rect.left, rect.top, rect.right, rect.bottom);
//        }
//    }


    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p){
        return new MarginLayoutParams(p);
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs){
        return new MarginLayoutParams(getContext(), attrs);
    }


    @Override
    protected LayoutParams generateDefaultLayoutParams(){
        return new MarginLayoutParams(super.generateDefaultLayoutParams());
    }


    @Override
    protected boolean checkLayoutParams(LayoutParams p){
        return super.checkLayoutParams(p);
    }


    /**
     * resort child elements to use lines as few as possible
     */
    public void relayoutToCompress(){
        post(new Runnable(){
            @Override
            public void run(){
                compress();
            }
        });
    }


    /**
     * use both of relayout methods together
     */
    public void relayoutToCompressAndAlign(){
        post(new Runnable(){
            @Override
            public void run(){
                compress();
                align();
            }
        });
    }


    private void compress(){
        int childCount = this.getChildCount();
        //no need to sort if flowlayout has no child view
        if(0 == childCount) return;
        int count = 0;
        for(int i = 0; i < childCount; i++){
            View v = getChildAt(i);
            //BlankView is just to make childs look in alignment, we should ignore them when we relayout
            if(v instanceof BlankView) continue;
            count++;
        }
        View[] childArray = new View[count];
        int[] spaces = new int[count];
        int n = 0;
        for(int i = 0; i < childCount; i++){
            View v = getChildAt(i);
            //BlankView is just to make childs look in alignment, we should ignore them when we relayout
            if(v instanceof BlankView) continue;
            childArray[n] = v;
            LayoutParams childLp = v.getLayoutParams();
            int childWidth = v.getMeasuredWidth();
            if(childLp instanceof MarginLayoutParams){
                MarginLayoutParams mlp = (MarginLayoutParams)childLp;
                spaces[n] = mlp.leftMargin + childWidth + mlp.rightMargin;
            }else{
                spaces[n] = childWidth;
            }
            n++;
        }
        int[] compressSpaces = new int[count];
        for(int i = 0; i < count; i++){
            compressSpaces[i] = spaces[i] > usefulWidth ? usefulWidth : spaces[i];
        }
        sortToCompress(childArray, compressSpaces);
        this.removeAllViews();
        for(View v : childList){
            this.addView(v);
        }
        childList.clear();
    }


    private void sortToCompress(View[] childArray, int[] spaces){
        int childCount = childArray.length;
        int[][] table = new int[childCount + 1][usefulWidth + 1];
        for(int i = 0; i < childCount + 1; i++){
            for(int j = 0; j < usefulWidth; j++){
                table[i][j] = 0;
            }
        }
        boolean[] flag = new boolean[childCount];
        for(int i = 0; i < childCount; i++){
            flag[i] = false;
        }
        for(int i = 1; i <= childCount; i++){
            for(int j = spaces[i - 1]; j <= usefulWidth; j++){
                table[i][j] = (table[i - 1][j] > table[i - 1][j - spaces[i - 1]] + spaces[i - 1]) ? table[i -
                        1][j] : table[i - 1][j - spaces[i - 1]] + spaces[i - 1];
            }
        }
        int v = usefulWidth;
        for(int i = childCount; i > 0 && v >= spaces[i - 1]; i--){
            if(table[i][v] == table[i - 1][v - spaces[i - 1]] + spaces[i - 1]){
                flag[i - 1] = true;
                v = v - spaces[i - 1];
            }
        }
        int rest = childCount;
        View[] restArray;
        int[] restSpaces;
        for(int i = 0; i < flag.length; i++){
            if(flag[i]){
                childList.add(childArray[i]);
                rest--;
            }
        }

        if(0 == rest){
            return;
        }
        restArray = new View[rest];
        restSpaces = new int[rest];
        int index = 0;
        for(int i = 0; i < flag.length; i++){
            if(!flag[i]){
                restArray[index] = childArray[i];
                restSpaces[index] = spaces[i];
                index++;
            }
        }
        table = null;
        childArray = null;
        flag = null;
        sortToCompress(restArray, restSpaces);
    }


    /**
     * add some blank view to make child elements look in alignment
     */
    public void relayoutToAlign(){
        post(new Runnable(){
            @Override
            public void run(){
                align();
            }
        });
    }


    private void align(){
        int childCount = this.getChildCount();
        //no need to sort if flowlayout has no child view
        if(0 == childCount) return;
        int count = 0;
        for(int i = 0; i < childCount; i++){
            View v = getChildAt(i);
            if(v instanceof BlankView){
                //BlankView is just to make childs look in alignment, we should ignore them when we relayout
                continue;
            }
            count++;
        }
        View[] childArray = new View[count];
        int[] spaces = new int[count];
        int n = 0;
        for(int i = 0; i < childCount; i++){
            View v = getChildAt(i);
            //BlankView is just to make childs look in alignment, we should ignore them when we relayout
            if(v instanceof BlankView) continue;
            childArray[n] = v;
            LayoutParams childLp = v.getLayoutParams();
            int childWidth = v.getMeasuredWidth();
            if(childLp instanceof MarginLayoutParams){
                MarginLayoutParams mlp = (MarginLayoutParams)childLp;
                spaces[n] = mlp.leftMargin + childWidth + mlp.rightMargin;
            }else{
                spaces[n] = childWidth;
            }
            n++;
        }
        int lineTotal = 0;
        int start = 0;
        this.removeAllViews();
        for(int i = 0; i < count; i++){
            if(lineTotal + spaces[i] > usefulWidth){
                int blankWidth = usefulWidth - lineTotal;
                int end = i - 1;
                int blankCount = end - start;
                if(blankCount >= 0){
                    if(blankCount > 0){
                        int eachBlankWidth = blankWidth / blankCount;
                        MarginLayoutParams lp = new MarginLayoutParams(eachBlankWidth, 0);
                        for(int j = start; j < end; j++){
                            this.addView(childArray[j]);
                            BlankView blank = new BlankView(mContext);
                            this.addView(blank, lp);
                        }
                    }
                    this.addView(childArray[end]);
                    start = i;
                    i--;
                    lineTotal = 0;
                }else{
                    this.addView(childArray[i]);
                    start = i + 1;
                    lineTotal = 0;
                }
            }else{
                lineTotal += spaces[i];
            }
        }
        for(int i = start; i < count; i++){
            this.addView(childArray[i]);
        }
    }

    class BlankView extends View{
        public BlankView(Context context){
            super(context);
        }
    }


    /**
     * cut the flowlayout to the specified num of lines
     *
     * @param limitLineNum
     */
    public void limitLineNum(final int limitLineNum){
        post(new Runnable(){
            @Override
            public void run(){
                int lineNum = limitLineNum;
                int childNum = 0;
                if(lineNum > lineNumList.size()){
                    lineNum = lineNumList.size();
                }
                for(int i = 0; i <= lineNum; i++){
                    childNum += lineNumList.get(i);
                }
                List<View> viewList = new ArrayList<>();
                for(int i = 0; i < childNum; i++){
                    viewList.add(getChildAt(i));
                }
                removeAllViews();
                for(View v : viewList){
                    addView(v);
                }
            }
        });
    }


    public ListAdapter getAdapter() {
        return mAdapter;
    }

    class AdapterDataSetObserver extends DataSetObserver{
        @Override
        public void onChanged() {
            super.onChanged();
            reloadData();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    }


    private void reloadData() {
        removeAllViews();

        boolean isSelect = false;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            final int j = i;
            mCheckedTagArray.put(i, false);
            final View childView = mAdapter.getView(i, null, this);
            addView(childView, new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//            addView(childView, new MarginLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)));

            if (mAdapter instanceof OnInitSelectedPosition) {
                boolean isSelected = ((OnInitSelectedPosition) mAdapter).isSelectedPosition(i);
                if (mTagCheckMode == FLOW_TAG_CHECKED_SINGLE) {
                    //单选只有第一个起作用
                    if (isSelected && !isSelect) {
                        mCheckedTagArray.put(i, true);
                        childView.setSelected(true);
                        isSelect = true;
                    }
                } else if (mTagCheckMode == FLOW_TAG_CHECKED_MULTI) {
                    if (isSelected) {
                        mCheckedTagArray.put(i, true);
                        childView.setSelected(true);
                    }
                }
            }

            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTagCheckMode == FLOW_TAG_CHECKED_NONE) {
                        if (mOnTagClickListener != null) {
                            mOnTagClickListener.onItemClick(FlowLayout.this, childView, j);
                        }
                    } else if (mTagCheckMode == FLOW_TAG_CHECKED_SINGLE) {
                        //判断状态
                        if (mCheckedTagArray.get(j)) {
                            mCheckedTagArray.put(j, false);
                            childView.setSelected(false);
                            if (mOnTagSelectListener != null) {
                                mOnTagSelectListener.onItemSelect(FlowLayout.this, new ArrayList<Integer>());
                            }
                            return;
                        }

                        for (int k = 0; k < mAdapter.getCount(); k++) {
                            mCheckedTagArray.put(k, false);
                            getChildAt(k).setSelected(false);
                        }
                        mCheckedTagArray.put(j, true);
                        childView.setSelected(true);

                        if (mOnTagSelectListener != null) {
                            mOnTagSelectListener.onItemSelect(FlowLayout.this, Arrays.asList(j));
                        }
                    } else if (mTagCheckMode == FLOW_TAG_CHECKED_MULTI) {
                        if (mCheckedTagArray.get(j)) {
                            mCheckedTagArray.put(j, false);
                            childView.setSelected(false);
                        } else {
                            mCheckedTagArray.put(j, true);
                            childView.setSelected(true);
                        }
                        if (mOnTagSelectListener != null) {
                            List<Integer> list = new ArrayList<>();
                            for (int k = 0; k < mAdapter.getCount(); k++) {
                                if (mCheckedTagArray.get(k)) {
                                    list.add(k);
                                }
                            }
                            mOnTagSelectListener.onItemSelect(FlowLayout.this, list);
                        }
                    }
                }
            });
        }
    }


    /**
     * 像ListView、GridView一样使用FlowLayout
     */
    public void setAdapter(ListAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        removeAllViews();
        mAdapter = adapter;

        if (mAdapter != null) {
            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }
    }


    public int getLimitCountInLine(){
        return limitCountInLine;
    }


    public void setLimitCountInLine(int limitCountInLine){
        this.limitCountInLine = limitCountInLine;
    }


    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        this.mOnTagClickListener = onTagClickListener;
    }

    public void setOnTagSelectListener(OnTagSelectListener onTagSelectListener) {
        this.mOnTagSelectListener = onTagSelectListener;
    }


    public interface OnTagSelectListener {
        void onItemSelect(FlowLayout parent, List<Integer> selectedList);
    }

    public interface OnTagClickListener {
        void onItemClick(FlowLayout parent, View view, int position);
    }


    public interface OnInitSelectedPosition {
        boolean isSelectedPosition(int position);
    }

}
