package com.xhq.demo.activity;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xhq.demo.R;
import com.xhq.demo.adapter.GalleryItem;
import com.xhq.demo.widget.CircleLayout.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义Gallery+ListView效果 主页面
 *
 * @author Administrator
 */
public class GalleryLVMainActivity extends BaseActivity{
    private ListView listView;
    private List<GalleryItem> galleryItemList = new ArrayList<>();
    private ArrayList<String> titleList = new ArrayList<>();


    /**
     * Called when the activity is first created.
     */
    @Override
    public void setView(){
        setContentView(R.layout.activity_gallery_lv_main);
    }


    @Override
    public void initView(){
        listView = findViewById(R.id.lv);
        for(int i = 0; i < 7; i++){
            titleList.add("List" + i);
        }
        initItems();
        setListView();
    }


    @Override
    public void setListener(){
    }


    private void initItems(){
        GalleryItem item;
        for(int i = 0; i < titleList.size(); i++){
            item = new GalleryItem(this);
            item.list = titleList;
            item.initAdapter(this, new int[]{R.id.tv2});
            galleryItemList.add(item);
        }
    }


    private void setListView(){
        SpecialAdapter adapter = new SpecialAdapter(this, galleryItemList, titleList, new int[]{R.id.tv1});
        listView.setAdapter(adapter);
    }


    public class SpecialAdapter extends BaseAdapter{
        private ArrayList<String> titleList;
        private List<GalleryItem> itemList;
        private int[] item;
        Context ctx;
        View retval;


        public SpecialAdapter(Context context, List<GalleryItem> itemList, ArrayList<String> dataObj, int[] item){
            super();
            this.ctx = context;
            this.titleList = dataObj;
            this.item = item;
            this.itemList = itemList;
        }


        @Override
        public int getCount(){
            return titleList.size();
        }


        @Override
        public Object getItem(int position){
            return itemList.get(position);
        }


        @Override
        public long getItemId(int position){
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            retval = LayoutInflater.from(ctx).inflate(R.layout.item_gallery_lv_all_video, null);
            for(int anItem : item){
                TextView title = retval.findViewById(anItem);
                title.setText(titleList.get(position));
            }
            GalleryItem items = this.itemList.get(position);
            GalleryItem gallery = retval.findViewById(R.id.item_gallery);
            // ,datas.get(position),new int[] {R.id.tv1, R.id.tv2, R.id.tv3}

            gallery.setAdapter(items.adapter);
            gallery.setSelection(1);
            return retval;
        }

    }

}