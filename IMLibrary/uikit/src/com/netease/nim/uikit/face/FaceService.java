package com.netease.nim.uikit.face;

import common.commonsdk.annotations.EasyHttp;

/**
 * Author: YuJunKui
 * Time:2018/3/16 16:32
 * Tips:
 */

public interface FaceService {


    //加入云信群（拉人进群）
    @EasyHttp(entityClass = String.class, isLogin = true, paramNames = "loginusercode,Tid,usercode,createUsercode,detailName")
    String JOIN_CIRCLE = "game/joinCircle";

    //退出云信群、踢人
    @EasyHttp(entityClass = String.class, isLogin = true, paramNames = "loginusercode,Tid,usercode,createUsercode")
    String EXIT_CIRCLE = "game/exitCircle";

    //解散云信群
    @EasyHttp(entityClass = String.class, isLogin = true, paramNames = "loginusercode,Tid,usercode")
    String DISSOLVE_CIRCLE = "game/dissolveCircle";
}
