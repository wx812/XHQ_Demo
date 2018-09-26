package com.xhq.commonbase.share.base;

import android.app.Activity;

import com.common.utils.share.platform.SharePullBlack;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/8/21.
 *     Desc  : share the incoming parameters,
 *             if there are more parameters should be such inheritance
 *     Updt  : Description.
 * </pre>
 */
public class BaseShareArgs {
    public Activity activity;
    public String title;
    public String message;              // content
    public String targetUrl;            // the link of the shared
    public String iconUrl;              // the icon url
    public boolean isLocalImg = false;  // whether local icon


    public int targetUserId;            //举报或拉黑的userId 如果开启举报或拉黑，必传

    //举报
    public int reviewId;                //举报的key

    //拉黑
    public int blackListType = -1;//黑名单类型 0个人，1商户
    public String blackUserName;//要拉黑的人名字

    /**
     * 各模块拉黑接口
     * blackListAppName:{
     * shop    店铺
     * activity 活动
     * 24  智享头条
     * circle 圈子
     * xuan  炫
     * service  服务
     * game  游戏
     * earnmoney 转文赚
     * association 社群
     * circledynamic 圈子动态
     * }
     */
    public String blackListAppName;
    public SharePullBlack.PullBlackListener pullBlackListener;//拉黑回调

    /* @param reviewType   举报的类型
     *                     SCReviewTypeNews = 21,//24小时de文章(后台数据表：sc_cms_article)
            *                     SCReviewTypeYueBa = 31,//31：约吧de服务技能(后台数据表：sc_service_skill)
            *                     SCReviewTypeActDetail = 41,//41：活动de活动详情(后台数据表：sc_circle_activity_detail)
            *                     SCReviewTypeActShow = 42,//42：活动de活动晒单(后台数据表：sc_circle_activity_share)
            *                     SCReviewTypeAssociationDetail = 43, // 43:社群组织详情（后台数据表：sc_circle_detail_new）
            *                     SCReviewTypeShow = 51,//51：我要炫de炫(后台数据表：sc_circle_cool)
            *                     SCReviewTypeCircle = 61, //61：圈子de动态(后台数据表：sc_circle_video_album)
            *                     SCReviewTypeFaceCircle = 71, //71：脸圈图片(后台数据表：sc_game_picture)
            *                     SCReviewTypeShopDynamic = 81, //81：云店动态(后台数据表：sc_shop_dynamic_info)
            *                     SCReviewTypeEarnMoneyReprinted = 91, // 91：转文赚(后台数据表：sc_earnmoney_reprint_info)
            *                     SCReviewTypeEarnMoneyTask = 101, // 101：抢标赚(后台数据表：sc_earnmoney_task_info)
            *                     SCReviewTypeMsgpushDynamics = 111,// 111：抢标赚(后台数据表：sc_msgpush_dynamics)
            *                     SCReviewTypeActivity = 121 //趣相投 活动(后台数据表:sc_activity)
            */
    public int reviewType;

    public ShareCallback callback;

}
