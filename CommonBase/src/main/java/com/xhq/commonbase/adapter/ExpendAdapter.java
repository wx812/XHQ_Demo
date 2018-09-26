package com.xhq.commonbase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.R;
import com.common.bean.ExpendInfo;
import com.common.bean.ExpendItemInfo;
import com.common.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Administrator
 * 2016/12/12
 * 14:25
 */
public class ExpendAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "ExpendAdapter ";
    private Context context;
    private LayoutInflater layoutInflater;
    private List<ExpendInfo> parentList = new ArrayList<>();
    private List<List<ExpendItemInfo>> childList = new ArrayList<>();

    public ExpendAdapter(Context context, List<ExpendInfo> parentList, List<List<ExpendItemInfo>> childList) {
        this.context = context;
        this.parentList = parentList;
        this.childList = childList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return null == parentList ? 0 : parentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return null == childList ? 0 : childList.get(
                groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater
                    .inflate(R.layout.expand_member_listview, null);
            holder = new ParentViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ParentViewHolder) convertView.getTag();
            convertView.getTag();
        }
        holder.iv_icon.setImageResource(parentList.get(groupPosition).getResId());
        holder.iv_num.setText("" + getChildrenCount(groupPosition));
        holder.iv_title.setText(parentList.get(groupPosition).getTitle());
        if (isExpanded) {
            holder.iv_jiant.setImageResource(R.drawable.iconfont_fanhui_down);
        } else {
            holder.iv_jiant.setImageResource(R.drawable.iconfont_fanhui);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.expand_member_childitem, null);
            holder = new ChildViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
            convertView.getTag();
        }
        ImageLoader.loadRoundView(context, childList.get(groupPosition).get(childPosition).showPicture, holder.head);
        holder.title.setText(childList.get(groupPosition).get(childPosition)
                .showName);
        int busiType = childList.get(groupPosition).get(childPosition).busiType;
        switch (busiType) {
            case 0:
                holder.type.setText("个人");
                holder.type.setBackgroundResource(R.drawable.huangsehuangse);
                break;
            case 1:
                holder.type.setText("店铺");
                holder.type.setBackgroundResource(R.drawable.fensefense);
                break;
            case 2:
                holder.type.setText("圈子");
                holder.type.setBackgroundResource(R.drawable.lanselanse);
                break;
            case 3:
                holder.type.setText("社群");
                holder.type.setBackgroundResource(R.drawable.lanselanse);
                break;
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class ParentViewHolder {
        ImageView iv_icon;
        TextView iv_title;
        TextView iv_num;
        ImageView iv_jiant;

        public ParentViewHolder(View view) {
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            iv_title = (TextView) view.findViewById(R.id.iv_title);
            iv_num = (TextView) view.findViewById(R.id.iv_num);
            iv_jiant = (ImageView) view.findViewById(R.id.iv_jiant);
        }
    }

    private static class ChildViewHolder {
        ImageView head;
        TextView title;
        TextView type;

        public ChildViewHolder(View view) {
            head = (ImageView) view
                    .findViewById(R.id.child_image);
            title = (TextView) view.findViewById(R.id.child_text);
            type = (TextView) view.findViewById(R.id.child_type);
        }
    }

}
