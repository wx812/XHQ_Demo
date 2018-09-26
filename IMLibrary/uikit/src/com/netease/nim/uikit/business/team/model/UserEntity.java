package com.netease.nim.uikit.business.team.model;

import common.commonsdk.http.HttpResultEntity;

/**
 * Author: YuJunKui
 * Time:2017/9/6 11:14
 * Tips: 根据userCode转user对象
 */

public class UserEntity extends HttpResultEntity{

    public ScSysUser scSysUser;

    public static class ScSysUser {
        public int id;
    }

}
