package com.xhq.demo.base.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Description --> add RecyclerView Bottom slip Monitor
 * <p>
 * Auth --> Created by ${XHQ} on 2017/8/21.
 */

public abstract class EndLessOnScrollListener extends RecyclerView.OnScrollListener{

    private LinearLayoutManager mLLManager;
    /**
     * loadedItemCount --> has been loaded out of the item count
     * <p>
     * firstVisibleItem --> first visible item on the screen
     * <p>
     * previousTotal --> last load count of item total
     */
    private int currentPage = 0, loadedItemCount, visibleItemCount, firstVisibleItem, previousTotal = 0;

    private boolean loading = true;


    public EndLessOnScrollListener(LinearLayoutManager manage){
        this.mLLManager = manage;
    }


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState){
        super.onScrollStateChanged(recyclerView, newState);
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy){
        super.onScrolled(recyclerView, dx, dy);
        visibleItemCount = recyclerView.getChildCount();
        loadedItemCount = mLLManager.getItemCount();
        firstVisibleItem = mLLManager.findFirstVisibleItemPosition();

        if(loading){

//            Log.d("xhq", "firstVisibleItem: " + firstVisibleItem);
//            Log.d("xhq", "totalPageCount:" + loadedItemCount);
//            Log.d("xhq", "visibleItemCount:" + visibleItemCount);

            if(loadedItemCount > previousTotal){
                loading = false;
                previousTotal = loadedItemCount;
            }
        }

        if(!loading && loadedItemCount - visibleItemCount <= firstVisibleItem){
            currentPage++;
            onLoadMore(currentPage);
            loading = true;
        }
    }


    public abstract void onLoadMore(int currentPage);
}
