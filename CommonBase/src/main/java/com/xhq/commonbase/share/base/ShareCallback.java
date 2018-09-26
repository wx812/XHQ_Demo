package com.xhq.commonbase.share.base;

/**
 * 分享成功回调
 * Created by ychen on 2018/2/7.
 */

public interface ShareCallback {
    int REQUEST_CODE = 2001;
    String TAG_PLATFORM_NAME = "platform_name";

    void onComplete(String platformName);

}
