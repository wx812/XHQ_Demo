package com.xhq.demo.tools.uiTools;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Author: YuJunKui
 * Time:2018/3/28 17:27
 * Tips:
 */

public class RecyclerViewUtils{
    /**
     * 获取RecyclerView 线性布局下  当前显示的index
     *
     * @return
     */
    public static int getCurrentViewIndex(LinearLayoutManager mLineManager) {
        int firstVisibleItem = mLineManager.findFirstVisibleItemPosition();
        int lastVisibleItem = mLineManager.findLastVisibleItemPosition();
        int currentIndex = firstVisibleItem;
        int lastHeight = 0;
        for (int i = firstVisibleItem; i <= lastVisibleItem; i++) {
            View view = mLineManager.getChildAt(i - firstVisibleItem);
            if (null == view) {
                continue;
            }

            int[] location = new int[2];
            view.getLocationOnScreen(location);

            Rect localRect = new Rect();
            view.getLocalVisibleRect(localRect);

            int showHeight = localRect.bottom - localRect.top;
            if (showHeight > lastHeight) {
                currentIndex = i;
                lastHeight = showHeight;
            }
        }

        if (currentIndex < 0) {
            currentIndex = 0;
        }

        return currentIndex;
    }


    /**
     * 获取RecyclerView中的指定ViewHolder
     *
     * @param recyclerView
     * @param position
     * @return
     */
    public static <T extends RecyclerView.ViewHolder> T getViewHolder(RecyclerView recyclerView, int position) {

        return (T) recyclerView.findViewHolderForAdapterPosition(position);
    }


    /**
     * 滚动到指定position并且附带偏移量
     * @param recyclerView
     * @param position
     * @param offset  px 单位
     */
    public static void scrollToPositionWithOffset(RecyclerView recyclerView, int position, int offset) {

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(position, offset);
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            ((StaggeredGridLayoutManager) layoutManager).scrollToPositionWithOffset(position, offset);
        } else {
            recyclerView.scrollToPosition(position);
        }
    }

    public static void scrollToPositionWithOffset(RecyclerView recyclerView, int position) {

        scrollToPositionWithOffset(recyclerView,position,0);
    }
}
