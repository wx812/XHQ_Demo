package com.netease.nim.uikit.common.activity;

import com.netease.nim.uikit.R;

/**
 * Created by hzxuwen on 2016/6/16.
 */
public class ToolBarOptions {
    /**
     * toolbar的title资源id
     */
    public int titleId;
    /**
     * toolbar的title
     */
    public String titleString;
    /**
     * toolbar的logo资源id
     */
    public int logoId;
    /**
     * toolbar的返回按钮资源id
     */
    public int navigateId = R.drawable.nim_actionbar_dark_back_icon;
    /**
     * toolbar的返回按钮
     */
    public boolean isNeedNavigate = true;
}
