package com.nim.redpacket;

/**
 * Administrator
 * 2018/1/24
 * 17:36
 */
public interface GrabRpCallBack {

    void updateRpResult(int rpStatus);//更新红包状态

    void grabRpResult(boolean isGetDone);//发送领取信息

}
