package com.nim.session;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.nim.session.params.FuncCode;
import com.smartcity.commonbase.util.Base64ForShareToChat;
import com.socks.library.KLog;

import org.json.JSONException;
import org.json.JSONObject;

import common.commonsdk.Constant;
import common.commonsdk.utils.JumpUtils;

/**
 * Administrator
 * 2017/9/30
 * 11:46
 */
public class FuncIntentHelper {
    private static final String TAG = FuncIntentHelper.class.getName();

    public static Intent getIntent(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        Intent jumpParamIntent = getIntent(Uri.parse(url));
        //如果解析成功跳转原生app，否则使用默认
        if (null != jumpParamIntent) {
            return jumpParamIntent;
        }
        return intent;
    }

    /**
     * 解析uri，如果解析失败，返回null
     *
     * @param data
     * @return
     */
    public static Intent getIntent(Uri data) {
        Intent intent = null;
        String jumpParams = data.getQueryParameter("jumpParams");
        if (TextUtils.isEmpty(jumpParams)) {
            return null;
        }
        byte[] decodeByte = Base64ForShareToChat.decode(jumpParams);
        if (null == decodeByte) {
            //base64解密失败直接返回
            return null;
        }
        String params = new String(decodeByte);
        String code = FuncParamHelper.getStrValueByKey(params, "jumpCode");
        Log.e(TAG, "code=" + code);
        if (TextUtils.isEmpty(code)) {
            //没有code，解析失败，直接返回
            return null;
        }
        switch (code) {
            //-------------智享头条---------------
            case FuncCode.BUSSINESS_TYPE_HOUR_ARTICLE_DETAIL_INFO:
            case FuncCode.BUSSINESS_TYPE_HOUR_VIDEO_DETAIL_INFO:
            case FuncCode.BUSSINESS_TYPE_HOUR_PHOTO_DETAIL_INFO:
            case FuncCode.BUSSINESS_TYPE_HOUR_QUESTION_INFO:
            case FuncCode.BUSSINESS_TYPE_HOUR_ANSWERDETAILS_INFO:
                Uri articleUri = Uri.parse("smartcity://news001/jumpParams?articleId=" + FuncParamHelper.getIntValueByKey(params, "articleId"));
                Uri atlasUri = Uri.parse("smartcity://news002/jumpParams?articleId=" + FuncParamHelper.getIntValueByKey(params, "articleId"));
                Uri videoUri = Uri.parse("smartcity://news003/jumpParams?articleId=" + FuncParamHelper.getIntValueByKey(params, "articleId"));
                Uri questionUri = Uri.parse("smartcity://news007/jumpParams?articleId=" + FuncParamHelper.getIntValueByKey(params, "articleId"));
                Uri answerDetailsUri = Uri.parse("smartcity://news008/jumpParams?articleId=" + FuncParamHelper.getIntValueByKey(params, "articleId"));
                int TypeCode = FuncParamHelper.getIntValueByKey(params, "typeCode");
                String typeCode = String.valueOf(TypeCode);
                switch (typeCode) {
                    case "1001":
                        intent = new Intent("tfhour_detail", articleUri);
                        return intent;
                    case "1002":
                        intent = new Intent("tfhour_atias", atlasUri);
                        return intent;
                    case "1003":
                        intent = new Intent("tfhour_video", videoUri);
                        return intent;
                    case "1005":
                        intent = new Intent("com.tfhour.activity.QuestionAndAnswerManagement.QuestionAndAnswerDetailsActivity", questionUri);
                        return intent;
                    case "1006":
                        intent = new Intent("com.tfhour.activity.AnswerDetailsPageManagement.AnswerDetailsPageActivity", answerDetailsUri);
                        return intent;

                }
            case FuncCode.BUSSINESS_TYPE_HOUR_SPECIAL_INFO:
                Uri specialUri = Uri.parse("smartcity://news004/jumpParams?scCmsSpecialId=" + FuncParamHelper.getIntValueByKey(params, "scCmsSpecialId") + "&cityCode=" + FuncParamHelper.getStrValueByKey(params, "cityCode"));
                intent = new Intent("tfhour_activity_SpecialcolumndetailspageActivity", specialUri);
                return intent;
            case FuncCode.BUSSINESS_TYPE_HOUR_MERCHANT_INFO:
                Uri MerchantUri = Uri.parse("smartcity://news005/jumpParams?merchantId=" + FuncParamHelper.getIntValueByKey(params, "merchantId") + "&shopStatus=" + FuncParamHelper.getIntValueByKey(params, "shopStatus"));
                intent = new Intent("tfhour_activity_MerchantHomePageActivity", MerchantUri);
                return intent;
            //-------------脸圈---------------
            case FuncCode.BUSSINESS_TYPE_FACE_PERSON_INFO:
                Uri faceUri = Uri.parse("smartcity://face/open_face_person_detail?otheruserid=" + FuncParamHelper.getIntValueByKey(params, "otherUserId"));
                intent = new Intent("face_person_detial", faceUri);
                return intent;
            //-------------全民炫---------------
            case FuncCode.BUSSINESS_TYPE_COOL_VIDEO_INFO:
                Uri u = Uri.parse("selfapp://izxcs/openwith_dazzle?dazzleId=" + FuncParamHelper.getIntValueByKey(params, "dazzleId") + "&isFromDis=" + false);
                intent = new Intent("CoolVideoDetailActivity", u);
                return intent;
            case FuncCode.BUSSINESS_TYPE_COOL_PERSON:
                intent = new Intent("com.video.activity.CoolPersonDetailActivity");
                intent.putExtra(Constant.USER_ID, FuncParamHelper.getIntValueByKey(params, "userid"));
                return intent;
            case FuncCode.BUSSINESS_TYPE_COOL_TOPIC:
                intent = new Intent("com.video.activity.DazzleTopicActivity");
                intent.putExtra("dazzle_topic", FuncParamHelper.getIntValueByKey(params, "labelid"));
                return intent;
            //-------------约吧---------------
            case FuncCode.BUSSINESS_TYPE_YB_SERVICE_INFO:
                try {
                    Uri serviceUri = Uri.parse("smartcity://ybservice/serviceDetail");
                    intent = new Intent("ServiceDetailActivity", serviceUri);
                    intent.putExtra("serviceId", FuncParamHelper.getIntValueByKey(params, "serviceId"));
                    intent.putExtra("skillUserId", FuncParamHelper.getIntValueByKey(params, "skillUserId"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return intent;
            //-------------社群---------------
            case FuncCode.BUSSINESS_TYPE_GROUP_INFO:
                Uri groupUri = Uri.parse("smartcity://organize/groupdetail?id=" + FuncParamHelper.getIntValueByKey(params, "id"));
                intent = new Intent("organize.groupdetial", groupUri);
                return intent;
            //-------------圈子动态---------------
            case FuncCode.BUSSINESS_TYPE_CIRCLE_DYNAMIC_INFO:
                Uri dynamicUri = Uri.parse("smartcity://playcircle/dynamicDetailPage?dynamicId=" + FuncParamHelper.getIntValueByKey(params, "dynamicId"));
                intent = new Intent("MyCircleDynamicDetailActivity", dynamicUri);
                return intent;
            //-------------圈子详情---------------
            case FuncCode.BUSSINESS_TYPE_CIRCLE_DETAIL_INFO:
                Uri circleUri = Uri.parse("smartCity://playCircle/circleDetailPage?circleId=" + FuncParamHelper.getIntValueByKey(params, "circleId"));
                intent = new Intent("NewCircleDetailActivity", circleUri);
                return intent;
            //-------------圈子活动详情---------------
            case FuncCode.BUSSINESS_TYPE_CIRCLE_ACTIVITY_INFO:
                Uri activiUri = Uri.parse("smartCity://playCircle/circleActivity?activityId=" + FuncParamHelper.getIntValueByKey(params, "activityId")
                        + "&activityDetailType=" + FuncParamHelper.getIntValueByKey(params, "activityDetailType")
                        + "&type=" + FuncParamHelper.getIntValueByKey(params, "type"));
                intent = new Intent("com.newcircle.activity.citycircle.NewActiveDetailActivity", activiUri);
                return intent;
            //-------------二维码扫描跳转圈子活动详情---------------
            case FuncCode.BUSSINESS_TYPE_CIRCLE_QRCODE_INFO:
                if (FuncParamHelper.getIntValueByKey(params, "jumpType") == 2) {//jumpType = 2 跳转圈子详情 其他的跳转h5
                    Uri QRcodeUri = Uri.parse("smartCity://playCircle/circleDetailPage?circleId=" + FuncParamHelper.getIntValueByKey(params, "circleId"));
                    intent = new Intent("NewCircleDetailActivity", QRcodeUri);
                    return intent;
                } else {
                    return null;
                }
            case FuncCode.BUSSINESS_TYPE_EARNMONEY_QB_DETIAL_INFO:
                //抢标赚
               /* Uri qbtaskdetail = Uri.parse("smartCity://playCircle/qbtaskdetail?taskId=" + FuncParamHelper.getIntValueByKey(params, "taskid"));
                intent = new Intent("com.money.activity.RobEarnDetailedActivity", qbtaskdetail);
                return intent;*/
                Uri qbtaskdetail = Uri.parse("smartCity://playCircle/qbtaskdetail?taskId=" + FuncParamHelper.getIntValueByKey(params, "taskId"));
                intent = new Intent("com.money.activity.RobEarnDetailedActivity", qbtaskdetail);
                return intent;
            case FuncCode.BUSSINESS_TYPE_EARNMONEY_ZW_DETIAL_INFO:
                //转文赚
                int reprintId2 = FuncParamHelper.getIntValueByKey(params, "reprintId");
                Uri qbreprintdetial = Uri.parse("smartcity://playcircle/qbreprintdetial?userid=" + FuncParamHelper.getIntValueByKey(params, "userid") + "&reprintId=" + reprintId2);
                intent = new Intent("com.money.activity.ChangeEarnDetailedActivity", qbreprintdetial);
                return intent;
            case FuncCode.BUSSINESS_TYPE_EARNMONEY_FX_DETIAL_INFO:
                //分享赚
                Uri qbsharemoneydetial = Uri.parse("smartcity://playcircle/qbsharemoneydetial?couponId=" + FuncParamHelper.getIntValueByKey(params, "couponId") + "&shareUserId=" + FuncParamHelper.getIntValueByKey(params, "shareUserId"));
                intent = new Intent("com.money.activity.ShareDetailedActivity", qbsharemoneydetial);
                return null;
            //-------------泡圈朋友圈详情---------------
            case FuncCode.BUSSINESS_TYPE_CHAT_FRIENDCIRCLE_DETIAL_INFO:
                Uri dynamicDetail = Uri.parse("smartcity://dynamic/open_dynamic_detail?dynamicId=" + FuncParamHelper.getStrValueByKey(params, "dynamicsId") + "&numberId=" + FuncParamHelper.getStrValueByKey(params, "numberId") + "&userCode=" + FuncParamHelper.getStrValueByKey(params, "userId"));
                intent = new Intent("DynamicDetailActivity", dynamicDetail);
                return intent;

            //群聊界面
            case FuncCode.BUSSINESS_TYPE_CHAT_GROUP_INFO:
                Uri teamChat = Uri.parse("smartcity://teamchat/open_teamcode?teamid=" + FuncParamHelper.getStrValueByKey(params, "teamid"));
                intent = new Intent("team_message_activity", teamChat);
                intent.putExtra("customization", SessionHelper.getTeamCustomization());

                /*intent = new Intent("team_message_activity");
                intent.putExtra("account",FuncParamHelper.getStrValueByKey(params, "teamid"));
                intent.putExtra("customization",SessionHelper.getTeamCustomization());*/
                return intent;
            case FuncCode.BUSSINESS_TYPE_CHAT_PERSONAL_INFO:
                Uri uri = Uri.parse("smartcity://face/open_face_person_detail?usercode=" + FuncParamHelper.getStrValueByKey(params, "usercode"));
                intent = new Intent("face_person_detial", uri);
                return intent;
            default:
                return intent;
        }

    }


    /**
     * 解析参数
     */
    public static boolean parseUrl(Context context, String shareUrl) {
        if (TextUtils.isEmpty(shareUrl)) {
            return false;
        }
        Uri uri = Uri.parse(shareUrl);
        return parseUrl(context, uri);
    }

    public static boolean parseUrl(Context context, Uri uri) {
        if (null == uri) {
            return false;
        }
        String jumpParams = uri.getQueryParameter("jumpParams");
        if (TextUtils.isEmpty(jumpParams)) {
            return false;
        }

        byte[] decodeRes = Base64ForShareToChat.decode(jumpParams);
        if (null == decodeRes) {
            return false;
        }

        String paramsJson = new String(decodeRes);
        if (TextUtils.isEmpty(paramsJson)) {
            return false;
        }

        String jumpCode = FuncParamHelper.getStrValueByKey(paramsJson, "jumpCode");
        KLog.e(TAG, paramsJson);
        if (TextUtils.equals(FuncCode.BUSSINESS_TYPE_SHOP_GOOD_INFO, jumpCode)) {
            startShopGoodDetail(context, paramsJson);
            return true;
        } else if (TextUtils.equals(FuncCode.BUSSINESS_TYPE_SHOP_DYNAMIC_INFO, jumpCode)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 跳转商品详情
     */
    private static void startShopGoodDetail(Context context, String jumpJson) {
        try {
            JSONObject jsonObject = new JSONObject(jumpJson);
            int shopId = jsonObject.getInt("shopId");
            int goodId = jsonObject.getInt("goodsId");
            JumpUtils.startShopGoodDetail(context, shopId, goodId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
