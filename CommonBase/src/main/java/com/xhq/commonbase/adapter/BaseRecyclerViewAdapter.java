package com.xhq.commonbase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Name: BaseRecyclerViewAdapter
 * User: fengliang
 * Date: 2015-11-24
 * Time: 13:32
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolderBase> {
    protected Context context;
    protected List<T> list;
    protected LayoutInflater layoutInflater;

    public BaseRecyclerViewAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setList(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void add(T t) {
        if (t != null) {
            list.add(t);
            notifyDataSetChanged();
        }
    }

    public void add(List<T> l) {
        if (list.size() > 0) {
            for (T t : l) {
                list.add(t);
            }
            notifyDataSetChanged();
        }
    }

    public void add(int position, List<T> l) {
        if (list.size() > 0) {
            for (T t : l) {
                list.add(t);
            }
            notifyItemRangeInserted(position, l.size());
        }
    }

    public void replace(List<T> l) {
        list.clear();
        if (l.size() > 0) {
            list.addAll(l);
            notifyDataSetChanged();
        }
    }

    public void remove(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAll() {
        list.clear();
        notifyDataSetChanged();
    }

    public void clear() {
        if (null != list && list.size() > 0) {
            list.clear();
        }
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolderBase viewHolder, int position) {
        bindView(viewHolder, position, list);
    }

    @Override
    public RecyclerViewHolderBase onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = createView(viewGroup, viewType);
        RecyclerViewHolderBase holder = createViewHolder(view);
        return holder;
    }


    public List<T> getDatas() {
        return list;
    }

    public abstract void bindView(RecyclerViewHolderBase viewHolder, int position, List<T> list);

    public abstract View createView(ViewGroup viewGroup, int viewType);

    public abstract RecyclerViewHolderBase createViewHolder(View view);

    Set<Integer> listeners;

    OnChildItemOnClickListener listener;

    public void setOnChildItemClickListener(OnChildItemOnClickListener listener) {
        this.listener = listener;
    }

    public interface OnChildItemOnClickListener {
        void OnChildItemOnClick(int position, View v,
                                List<Map<String, Object>> list);
    }

    public void addClickListenerFromId(int... ids) {
        if (listeners == null) {
            listeners = new HashSet<>();
        }
        for (int i = 0; i < ids.length; i++) {
            listeners.add(ids[i]);
        }
    }
}
