package com.xhq.demo.base;

import android.app.Activity;

/**
 * 作者:zhaoge
 * 时间:2017/8/30.
 */

public interface IBaseView{
    void showToast(String s);

    void showProgress(String msg);

    void hideProgress();



    /**
     * 展示 loading 弹窗  默认点击消失
     *
     * @param msg 提示内容 -1 为默认提醒 loading
     */
    void showLoading(int msg);


    /**
     * 展示 loading 弹窗
     *
     * @param isCancel 设置弹窗属性
     * @param msg      提示内容  -1 为默认提醒 loading
     */
    void showLoading(int msg, boolean isCancel);

    /**
     * 关闭加载页面
     */

    void hideLoading();

    /**
     * token 过期  弹出登录
     */
    void startLogin();

    void showShortToast(String msg);

    void showShortToast(int resId);


    /**
     * 获取字符串资源id
     */
    String getStringRes(int resId);

    void finishActivity();

    //添加关注成功
    void onCocernRelaSuccess(String msg);

    //添加关注失败
    void onCocernRelaFail(String msg);

    //取消关注成功
    void onDelCocernRelaSuccess(String msg);

    //取消关注失败
    void onDelCocernRelaFail(String msg);

    //是否已关注
    void hasCocernRelaSuccess(boolean hasCocern);

    //是否已关注失败
    void hasCocernRelaFail(String msg);

    //举报成功
    void ReportSuccess(String msg);

    //举报失败
    void ReportFail(String msg);

    //拉黑成功
    void PullblackSuccess(String msg);

    //拉黑失败
    void Blackpullfailure(String msg);

    //上传音频进度
    void onUploadingVoice(int process);

    //上传音频成功
    void onUploadVoiceSuccess(String path);

    //上传音频失败
    void onUploadVoiceFail(String msg);

    //上传图片进度
    void onUploadingImg(int process);

    //上传图片成功
    void onUploadImgSuccess(String path);

    //上传图片失败
    void onUploadImgFail(String msg);

    //上传图片进度
    void onUploadingVideo(int process);

    //上传视频成功
    void onUploadVideoSuccess(String thumbnailPath, String path);

    //上传图片失败
    void onUploadVideoFail(String msg);

    //分享成功
    void shareSuccessMsg(String msg);

    //分享失败 错误
    void shareErrorMsg(String msg);

    //取消分享
    void shareCancelMsg(String msg);

    //获取账户余额成功
    void getAccountUserSuccess(double balance);

    //获取账户余额失败
    void getAccountUserFail(String msg);

    //社群中关注社群成功
    void addAttentionSuccess(String msg);

    //社群中关注社群失败
    void addAttentionFail(String msg);

    //取消关注成功
    void cancelAttentionSuccess(String msg);

    //取消关注失败
    void cancelAttentionFail(String msg);

    //评论成功
    void showCommentSuccessMsg(String msg);

    //评论失败
    void showCommentFailMsg(String msg);

    //点赞成功
    void showLikeSuccessMsg(String msg);

    //点赞失败
    void showLikeFailMsg(String msg);

    void onAddDazzlePraizeSuccess(int position, int isPraise);

    /**
     * 头条通用接口
     */
    //文章删除成功
    void showDeleteSuccessMsg(String msg, int position);

    //文章删除失败
    void showDeleteFailedMsg(String msg);

    //视频播放统计
    void videoPlaybackStatistics(String msg);

    //视频播放统计失败
    void videoPlayFailedMsg(String msg);

    //评论删除成功
    void showDeleteCommentMsg(String msg, int position);

    //评论删除失败
    void showCommentFailedMsg(String msg);

    //获取头条作者
//    void getAuthorSuccess(AuthordetailsEntity entity);

    void getAuthorFail(String msg);


    void PointOutTheFailureMsg(String msg);


    void getPayAttentionToFailureMsg(String msg);


    void getAbolishAttentionToFailureMsg(String msg);


    void getCollectionFailureMsg(String msg);


    void getZhuChengFailureMsg(String msg);


    void getCancelTheFortificationFailureMsg(String msg);
    /*--------------------------------------------------*/
//    //拉黑成功
//    void Pullblacksuccess(String msg);

    //朋友圈发布-分享成功
//    void onShare2CircleSuccess(ShareInfo2CircleEntity entity);

    void onShare2CircleFail(String msg);

    Activity getBaseActivity();

}
