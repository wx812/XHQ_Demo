package com.xhq.demo.base.adapter;

import android.view.View;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/8/23.
 *     Desc  : Exposure click event interface
 *     Updt  : Description
 * </pre>
 */
public interface OnItemClickListener<T>{
    void onItemClick(View view, int position);
    void onItemLongClick(View view, int position);
}
