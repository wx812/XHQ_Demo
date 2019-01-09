package com.xhq.demo.tools.uiTools;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;


/**
 * 用来关闭所有的act
 *
 * @author HuNan
 */
public class ActivityManagerTool extends Application{

    private final List<Activity> activities = new LinkedList<>();

    private static ActivityManagerTool manager;

    private boolean isExist = false;                     //act 存在标志

    private final List<Class<?>> bottomActivities = new LinkedList<>(); //底部导航类集合


    /**
     * 获得 activity管理对象
     */
    public static ActivityManagerTool getActivityManager(){
        if(null == manager){
            manager = new ActivityManagerTool();
        }
        return manager;
    }


    /**
     * 添加新的act
     */
    public boolean add(Activity act){

        if(act == null){
            Log.e("ActivityManagerTool_add", "act = null");
            return false;
        }

        int position = 0;
        //导航栏act进栈，删除非导航栏act
        if(isBottomActivity(act)){

            for(int i = 0; i < activities.size(); i++){

                if(!isBottomActivity(activities.get(i))){
                    popActivity(activities.get(i));
                    i--;
                }else if(activities.get(i).getClass().equals(act.getClass())){//获得重复act位置
                    isExist = true;
                    position = i;
                }
                //				
                //				//获得重复act位置
                //				if (i >= 0 && activities.get(i).getClass().equals(act.getClass())) {
                //					isExist = true;
                //					position = i;
                //				}

            }

        }

        if(!activities.add(act)){
            return false;
        }
        //删除重复act
        if(isExist){
            isExist = false;
            activities.remove(position);
        }

        return true;
    }


    /**
     * 关闭除参数act外的所有act
     */
    public void finish(Activity act){

        if(act == null){
            Log.e("ActivityManagerTool", "finish act = null");
            return;
        }

        for(Activity iterable : activities){
            if(act != iterable){
                iterable.finish();
            }
        }
    }


    /**
     * 关闭所有的act
     */
    public void exit(String tag){
        for(Activity act : activities){
            if(act != null){
                act.finish();
            }
        }
        System.out.println("退出系统");
//        Session.getSession().disconnect();
        System.exit(0);
    }


    /**
     * 删除指定act
     */
    private void popActivity(Activity act){

        if(act != null){
            act.finish();
            activities.remove(act);
        }

    }


    /**
     * 获得当前act
     */
    public Activity currentActivity(){
        if(activities.size() <= 0){
            Log.e("ActivityManagerTool", "currentActivity = null");
            return null;
        }

        return activities.get(activities.size() - 1);
    }


    /**
     * act是否为底部导航
     */
    private boolean isBottomActivity(Activity act){

        for(int i = 0; i < bottomActivities.size(); i++){
            if(act.getClass() == bottomActivities.get(i)){
                return true;
            }
        }

        return false;
    }


    /**
     * 如需返回IndexActivity则返回IndexActivity
     */
    public void backIndex(Context context, Class<?> indexActivity){

        if(activities.size() <= 0){
            Log.e("ActivityManagerTool", "backIndex activities.size() <= 0");
            return;
        }

        if(isBottomActivity(activities.get(activities.size() - 1))){
            Intent intent = new Intent();
            intent.setClass(context, indexActivity);
            context.startActivity(intent);
        }
    }


    /**
     * 删除已经finish的act
     */
    public void removeActivity(Activity act){

        if(act != null){
            activities.remove(act);
        }
    }


    /**
     * 初始化，存储底部导航类
     */
    public void setBottomActivities(Class<?> actClass){
        bottomActivities.add(actClass);
    }


    /**
     * 得到当前界面行为统计的界面Id
     *
     * @param act 当前界面实例
     * @return 当前界面行为统计id 如果在action_stat.xml的没有配置此界面，返回为 "";
     * @author zhai
     */
    public String getCurrentActivitytId(Context act){
        String actIdStr = "";
        if(null == act){
            return actIdStr;
        }
        int actResourceId = act.getResources().getIdentifier(act.getClass().getName(), "string",
                                                                  act.getPackageName());
        if(actResourceId > 0){
            actIdStr = act.getString(actResourceId);
        }
        return actIdStr;
    }


    /**
     * 根据传入的字符串从配置文件中获取ID
     *
     * @param act 当前界面实例
     * @param pageFlag 将根据这个标识从配置文件中获取ID
     * @return 当前界面行为统计id 如果在action_stat.xml的没有配置此界面，返回为 "";
     */
    public static String getCurrentActivitytId(Context act, String pageFlag){
        String actIdStr = "";
        if(null == act){
            return actIdStr;
        }
        int actResourceId = act.getResources().getIdentifier(pageFlag, "string",
                                                                  act.getPackageName());
        /* 如果没有找到返回0 */
        if(actResourceId > 0){
            actIdStr = act.getString(actResourceId);
        }
        return actIdStr;
    }


    /**
     * 删除非底部导航act
     */
    public void delNotBottomActivity(){
        for(int i = 0; i < activities.size(); i++){

            if(!isBottomActivity(activities.get(i))){
                popActivity(activities.get(i));
                if(i >= 1){
                    i--;
                }
            }
        }
    }


    /**
     * 判断当前act是否存在，用于tabbar切换
     */
    public Activity actExist(Class<?> c){
        if(c == null){
            return null;
        }
        for(int i = 0; i < activities.size(); i++){
            if(c.equals(activities.get(i).getClass())){
                return activities.get(i);
            }
        }
        return null;
    }
}
