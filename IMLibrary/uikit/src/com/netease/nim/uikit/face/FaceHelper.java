package com.netease.nim.uikit.face;

import android.content.Context;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.team.model.TeamTypeEntity;
import com.netease.nim.uikit.face.faceapi.FaceGetTeamTypeCase;
import com.netease.nim.uikit.face.faceapi.FaceTeamOperationCase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import common.commonsdk.http.DefaultSubscriberNetCallBack;
import common.commonsdk.http.SubscribeManager;


/**
 * Author: YuJunKui
 * Time:2017/9/5 19:15
 * Tips: 脸圈跟云信相关
 */

public class FaceHelper {


    /**
     * @param context
     * @param tid
     */
    public static void updataTeamName(Context context, String tid, String detailName, String tag) {
        FaceTeamOperationCase faceCase = new FaceTeamOperationCase(context);
        faceCase.execute(faceCase.createUpdataTeamNameParams(tid, detailName), new SubscriberNetCallBack<String>());
        SubscribeManager.getInstance().add(tag, faceCase);
    }


    /**
     * 根据userCode启动脸圈个人详情页面
     *
     * @param context
     * @param userCode
     */
    public static void startFaceDetailByUserCode(final Context context, String userCode) {

        try {
            Class<?> threadClazz = Class.forName("com.smartcity.commonbussiness.router.FaceRouter");
            Object receiver = threadClazz.newInstance();
            Method method = threadClazz.getMethod("startPersonDetail", new Class[]{Context.class, String.class});
            method.invoke(receiver, new Object[]{context, userCode.toLowerCase()});
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    /**
     * 本地维护的tid 和 TeamTypeEntity
     */
    private static Map<String, Boolean> teamIsFaceMap = new HashMap<>();
    public static final int FACE_TEAM_TYPE = 7;

    /**
     * 查询群是否是脸圈群
     *
     * @param context
     * @param teamId
     * @param tag
     * @param callBack
     */
    public static void queryTeamIsFace(final Context context, final String teamId, String tag
            , final SubscriberNetCallBack<Boolean> callBack) {

        //查询本地
        final Boolean isFaceTeam = teamIsFaceMap.get(teamId);
        if (isFaceTeam != null) {
            //回调本地
            callBack.onSuccess(isFaceTeam);
        } else {

            queryTeamInfo(context, teamId, tag, new SubscriberNetCallBack<TeamTypeEntity>() {

                @Override
                protected void onFailure(Throwable throwable, String s, int httpResultEntity) {
                    callBack.onFailure(throwable, s, 0);
                }

               /* @Override
                protected void onLogin(String s) {
                    callBack.onLogin(s);
                }*/

                @Override
                public void onCompleted() {
                    callBack.onCompleted();
                }

                @Override
                protected void onSuccess(TeamTypeEntity entity) {

                    //云信自动创建的群，如果查不到信息服务器仍然返回code 1
                    if (entity == null) {
                        onFailure(null, context.getString(R.string.label_no_team_info), 0);
                    } else {

                        boolean isFaceTeam = entity.detailTypeId == FACE_TEAM_TYPE;
                        teamIsFaceMap.put(teamId, isFaceTeam);
                        callBack.onSuccess(isFaceTeam);
                    }
                }
            });
        }
    }

    /**
     * 查询群类型，基础方法 勿动
     *
     * @param context
     * @param teamId
     * @param tag
     * @param callBack 注意此回调肯能在code 1 的时候  entity位null
     */
    public static void queryTeamInfo(Context context, final String teamId, String tag
            , final SubscriberNetCallBack<TeamTypeEntity> callBack) {

        FaceGetTeamTypeCase faceCase = new FaceGetTeamTypeCase(context);
        faceCase.execute(faceCase.createParams(teamId), callBack);
        SubscribeManager.getInstance().add(tag, faceCase);
    }


    public static class SubscriberNetCallBack<T> extends DefaultSubscriberNetCallBack<T> {
        @Override
        protected void onFailure(Throwable throwable, String s, int httpResultEntity) {

        }

        @Override
        protected void onSuccess(T o) {

        }
    }

}
