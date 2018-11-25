package com.xhq.demo.base.adapter;


import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/11/10.
 *     Desc  : Set the recyclerView item spacing
 *     Updt  : Description.
 * </pre>
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    public static final int LINEAR_LAYOUT = 0;
    public static final int GRID_LAYOUT = 1;
    public static final int STAGGERED_GRID_LAYOUT = 2;

    //限定为LINEAR_LAYOUT,GRID_LAYOUT,STAGGERED_GRID_LAYOUT
    @IntDef({LINEAR_LAYOUT, GRID_LAYOUT, STAGGERED_GRID_LAYOUT})
    //表示注解所存活的时间,在运行时,而不会存在. class 文件.
    @Retention(RetentionPolicy.SOURCE)
    public @interface LayoutManager {
        int type() default LINEAR_LAYOUT;
    }


    private int leftRight;
    private int topBottom;
    /**
     * 头布局个数
     */
    private int headItemCount;
    /**
     * 边距
     */
    private int space;
    /**
     * 是否包含边距
     */
    private boolean includeEdge;
    /**
     * 列数
     */
    private int spanCount;

    private @LayoutManager int layoutManager;


    /**
     * LinearLayoutManager or GridLayoutManager or StaggeredGridLayoutManager spacing
     */
    public SpaceItemDecoration(int space, @LayoutManager int layoutManager) {
        this(space, 0, true, layoutManager);
    }


    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     */
    public SpaceItemDecoration(int space,int headItemCount, @LayoutManager int layoutManager) {
        this(space, headItemCount, true, layoutManager);
    }

    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     */
    public SpaceItemDecoration(int space,boolean includeEdge, @LayoutManager int layoutManager) {
        this(space, 0, includeEdge, layoutManager);
    }


    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     * @param space
     * @param headItemCount
     * @param includeEdge
     * @param layoutManager
     */
    public SpaceItemDecoration(int space,int headItemCount,boolean includeEdge, @LayoutManager int layoutManager) {
        this.space = space;
        this.headItemCount = headItemCount;
        this.includeEdge = includeEdge;
        this.layoutManager = layoutManager;
    }


    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     * @param leftRight
     * @param topBottom
     * @param headItemCount
     * @param layoutManager
     */
    public SpaceItemDecoration(int leftRight, int topBottom, int headItemCount, @LayoutManager int layoutManager) {
        this.leftRight = leftRight;
        this.topBottom = topBottom;
        this.headItemCount = headItemCount;
        this.layoutManager = layoutManager;
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        switch (layoutManager) {
            case LINEAR_LAYOUT:
                setLinearLayoutSpaceItemDecoration(outRect,view,parent,state);
                break;
            case GRID_LAYOUT:
                GridLayoutManager glManager = (GridLayoutManager) parent.getLayoutManager();
                spanCount = glManager.getSpanCount();   //列数
                setGridLayoutSpaceItemDecoration(outRect,view,parent,state);
                break;
            case STAGGERED_GRID_LAYOUT:
                StaggeredGridLayoutManager sglManager = (StaggeredGridLayoutManager) parent.getLayoutManager();
                spanCount = sglManager.getSpanCount();  //列数
                setNGridLayoutSpaceItemDecoration(outRect,view,parent,state);
                break;
            default:
                break;
        }
    }

    /**
     * LinearLayoutManager spacing
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    private void setLinearLayoutSpaceItemDecoration(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }
    }

    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    private void setNGridLayoutSpaceItemDecoration(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
        int position = parent.getChildAdapterPosition(view) - headItemCount;
        if (headItemCount != 0 && position ==  - headItemCount){
            return;
        }
        int column = position % spanCount;
        if (includeEdge) {
            outRect.left = space - column * space / spanCount;
            outRect.right = (column + 1) * space / spanCount;
            if (position < spanCount) {
                outRect.top = space;
            }
            outRect.bottom = space;
        } else {
            outRect.left = column * space / spanCount;
            outRect.right = space - (column + 1) * space / spanCount;
            if (position >= spanCount) {
                outRect.top = space;
            }
        }

    }

    /**
     * GridLayoutManager设置间距（此方法最左边和最右边间距为设置的一半）
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    private void setGridLayoutSpaceItemDecoration(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        //判断总的数量是否可以整除
        int totalCount = layoutManager.getItemCount();
        int surplusCount = totalCount % layoutManager.getSpanCount();
        int childPosition = parent.getChildAdapterPosition(view);
        //竖直方向的
        if (layoutManager.getOrientation() == GridLayoutManager.VERTICAL) {
            if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
                //后面几项需要bottom
                outRect.bottom = topBottom;
            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
                outRect.bottom = topBottom;
            }
            //被整除的需要右边
            if ((childPosition + 1 - headItemCount) % layoutManager.getSpanCount() == 0) {
                //加了右边后最后一列的图就非宽度少一个右边距
                //outRect.right = leftRight;
            }
            outRect.top = topBottom;
            outRect.left = leftRight / 2;
            outRect.right = leftRight / 2;
        } else {
            if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
                //后面几项需要右边
                outRect.right = leftRight;
            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
                outRect.right = leftRight;
            }
            //被整除的需要下边
            if ((childPosition + 1) % layoutManager.getSpanCount() == 0) {
                outRect.bottom = topBottom;
            }
            outRect.top = topBottom;
            outRect.left = leftRight;
        }
    }
}