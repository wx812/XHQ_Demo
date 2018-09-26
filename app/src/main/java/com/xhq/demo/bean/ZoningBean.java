package com.xhq.demo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description --> Zoning Entity
 * <p>
 * Auth --> Created by ${XHQ} on 2017/8/23.
 */

public class ZoningBean implements Parcelable, Serializable{

    private static final long serialVersionUID = -4837451821795363877L;

    // "area_name": "南独乐河镇",
    // "childOrgList": [
    //   {
    //     "area_name": "南独乐河村",
    //     "parent_id": "110117104",
    //     "area_code": "110117104200",
    //     "tree_level": "4",
    //     "area_id": "110117104200"
    //   },

    // province, Municipalities
    public String area_id;                              // 区划ID
    public String area_code;                            // 区划代码
    public String area_name;                            // 区划名称
    public String tree_level;                           // 区划等级
    public String parent_id;                            // 父级区划ID
    public List<ZoningBean2> childOrgList;              // 下级机构

    public ZoningBean(){}


    protected ZoningBean(Parcel in){
        this.area_id = in.readString();
        this.area_code = in.readString();
        this.area_name = in.readString();
        this.tree_level = in.readString();
        this.parent_id = in.readString();
        this.childOrgList = new ArrayList<>();
        in.readList(this.childOrgList, ZoningBean2.class.getClassLoader());
    }


    @Override
    public int describeContents(){ return 0; }


    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(this.area_id);
        dest.writeString(this.area_code);
        dest.writeString(this.area_name);
        dest.writeString(this.tree_level);
        dest.writeString(this.parent_id);
        dest.writeList(this.childOrgList);
    }


    public static final Creator<ZoningBean> CREATOR = new Creator<ZoningBean>(){
        @Override
        public ZoningBean createFromParcel(Parcel source){return new ZoningBean(source);}


        @Override
        public ZoningBean[] newArray(int size){return new ZoningBean[size];}
    };


    // city, Area
    public static class ZoningBean2 implements Parcelable, Serializable{

        private static final long serialVersionUID = 2022561691119034708L;
        public String area_id;
        public String area_code;
        public String area_name;
        public String tree_level;
        public String parent_id;
        public List<ZoningBean3> childOrgList;


        @Override
        public int describeContents(){ return 0; }


        @Override
        public void writeToParcel(Parcel dest, int flags){
            dest.writeString(this.area_id);
            dest.writeString(this.area_code);
            dest.writeString(this.area_name);
            dest.writeString(this.tree_level);
            dest.writeString(this.parent_id);
            dest.writeList(this.childOrgList);
        }


        public ZoningBean2(){}


        protected ZoningBean2(Parcel in){
            this.area_id = in.readString();
            this.area_code = in.readString();
            this.area_name = in.readString();
            this.tree_level = in.readString();
            this.parent_id = in.readString();
            this.childOrgList = new ArrayList<>();
            in.readList(this.childOrgList, ZoningBean3.class.getClassLoader());
        }


        public static final Creator<ZoningBean2> CREATOR = new Creator<ZoningBean2>(){
            @Override
            public ZoningBean2 createFromParcel(Parcel source){return new ZoningBean2(source);}


            @Override
            public ZoningBean2[] newArray(int size){return new ZoningBean2[size];}
        };
    }


    // county, street
    public static class ZoningBean3 implements Parcelable, Serializable{

        private static final long serialVersionUID = -1467640916945396134L;
        public String area_id;
        public String area_code;
        public String area_name;
        public String tree_level;
        public String parent_id;
        public List<ZoningBean4> childOrgList;


        @Override
        public int describeContents(){ return 0; }


        @Override
        public void writeToParcel(Parcel dest, int flags){
            dest.writeString(this.area_id);
            dest.writeString(this.area_code);
            dest.writeString(this.area_name);
            dest.writeString(this.tree_level);
            dest.writeString(this.parent_id);
            dest.writeList(this.childOrgList);
        }


        public ZoningBean3(){}


        protected ZoningBean3(Parcel in){
            this.area_id = in.readString();
            this.area_code = in.readString();
            this.area_name = in.readString();
            this.tree_level = in.readString();
            this.parent_id = in.readString();
            this.childOrgList = new ArrayList<>();
            in.readList(this.childOrgList, ZoningBean4.class.getClassLoader());
        }


        public static final Creator<ZoningBean3> CREATOR = new Creator<ZoningBean3>(){
            @Override
            public ZoningBean3 createFromParcel(Parcel source){return new ZoningBean3(source);}


            @Override
            public ZoningBean3[] newArray(int size){return new ZoningBean3[size];}
        };
    }


    // town, community
    public static class ZoningBean4 implements Parcelable, Serializable{

        private static final long serialVersionUID = 4249359006023036203L;
        public String area_id;
        public String area_code;
        public String area_name;
        public String tree_level;
        public String parent_id;


        @Override
        public int describeContents(){ return 0; }


        @Override
        public void writeToParcel(Parcel dest, int flags){
            dest.writeString(this.area_id);
            dest.writeString(this.area_code);
            dest.writeString(this.area_name);
            dest.writeString(this.tree_level);
            dest.writeString(this.parent_id);
        }


        public ZoningBean4(){}


        protected ZoningBean4(Parcel in){
            this.area_id = in.readString();
            this.area_code = in.readString();
            this.area_name = in.readString();
            this.tree_level = in.readString();
            this.parent_id = in.readString();
        }


        public static final Creator<ZoningBean4> CREATOR = new Creator<ZoningBean4>(){
            @Override
            public ZoningBean4 createFromParcel(Parcel source){return new ZoningBean4(source);}


            @Override
            public ZoningBean4[] newArray(int size){return new ZoningBean4[size];}
        };
    }

}
