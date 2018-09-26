package com.xhq.demo.constant.apiconfig;

/**
 * 作者:zhaoge
 * 时间:2017/8/3.
 */

public class ApiKey {

    /**
     * 公用网络请求传递的key
     */
    public interface CommonUrlKey {
        // 通用必须的 请求字段
        String sk = "sk";                           //登录令牌
        String _sk = "_sk";                         //登录令牌
        String mtype = "mtype";                     //消息类型（如登录：1001）
        String time = "time";                       //消息时间
        String ct = "ct";                           //客户端类型
        String id = "id";
        String data = "data";                       //传给服务器的加密后的data信息
        String sc = "sc";                           //传给服务器的md5加密后的data信息
        String nc = "1";
        String ownerId = "ownerId";                 //登录用户名的唯一ID
        String attachId = "attachId";
        String OPT_USER_ID = "opt_user_id";         // 用户ID（必填）
        String PICK_ID = "pick_id";                 // 领卡流水主键（必填）
    }

    /**
     * 登陆界面
     */
    public interface Login {
        String USER_NAME = "username";
        String PASSWORD = "password";
    }

    /**
     * 注册页面，以及发送验证码请求的key
     */
    public static class RegisterKey {

        //发送的电话号码
        public static final String tel = "tel";
        //发送的密码
        public static final String password = "up";
        //密码确认
        public static final String passwordConfirm = "np";
        //发送的验证码
        public static final String code = "code";
    }

    /**
     * home页面和民政一卡通页面请求的key
     */
    public static class HomeKey {

        //请求图片的大小，如果是“2”为X2大小的图，“3”为X3大小的图
        public static final String scale = "scale";
        //item传递的唯一mid
        public static final String home_id = "home_id";
        //item传递的name
        public static final String home_name = "home_name";
        //item传递的图片url
        public static final String home_pic = "home_pic";


    }

    /**
     * 上传图片请求的key
     */
    public static class UpImgKey {

        //图片路径
        public static final String path = "path";
        //标识这个图片为什么类型的图片
        public static final String type = "type";
        //服务器保存的地址
        public static final String afterDoClass = "afterDoClass";
        //上传的图片
        public static final String upload = "upload";
        //图片唯一id
        public static final String id = "id";
        //身份证号
        public static final String uid = "uid";
        //姓名
        public static final String name = "name";
        //出生日期
        public static final String bir = "bir";
        //民族
        public static final String nat = "nat";
        //身份证地址
        public static final String addr = "addr";
        //性别
        public static final String sex = "sex";
        //签署机关
        public static final String auth = "auth";
        //到期时间
        public static final String validT = "validT";
        //开始时间
        public static final String validF = "validF";
    }

    /**
     * 下载图片
     */
    public interface DownloadImg {
        String ATTACH_ID = "attachId";
    }

    /**
     * request params of the grant card inquire
     */
    public interface GrantInquire {
        String AREA_ID = "area_id";          // 区划ID
        String PAGESHOW = "pageshow";        // 当前页数（第几页）
        String CARD_TYPE = "card_type";      // 卡片类型
        String CARD_STATUS = "card_status";  // 卡状态
        String CONTENT = "content";          // 检索内容
    }

    /**
     * 卡片查询的key
     */
    public static class InquireCard {
        public static final String PERSON_UID = "person_uid";
    }

    /**
     * 个人信息查询的key
     */
    public interface Details {
        String TEL = "tel";
        String NAME = "name";
        String SEX = "sex";
        String AGE = "age";
        String BIRTHDAY = "birthday";
        String PL = "pl";
        String AC = "ac";
        String COMP = "comp";
        String COMPA = "compa";
        String PROF = "prof";
        String POST = "post";
        String HID = "hid";

    }

    /**
     * 检查二维码是否有效
     */
    public interface ScanCheck {
        String BJT_NO = "bjt_no";
        String BANK_ACCOUNT_CODE = "bank_account_code";

    }

    /**
     * MugShot verify
     */
    public interface MugShotVerify {
        String PERSON_UID = "person_uid";               // myself ID Card number
        String PERSON_NAME = "person_name";             // myself ID Card name
    }

    /**
     * 卡片激活
     */
    public interface CardActivate {
        String PERSON_UID = "person_uid";
        String PERSON_NAME = "person_name";
        String BANK_ACCOUNT_CODE = "bank_account_code";

        String ACTIVATE_ID = "activate_id";
        String FRONT_ID_CARD = "front_id_card";
        String BACK_ID_CARD = "back_id_card";
        String FRONT_MZ_CARD = "front_mz_card";
        String HANDHELD_MZ_BODY = "handheld_mz_body";
        String WORK_NUMBER = "work_number";
        String MOBILE = "mobile";

        String PASSWORD = "password";
        String KEY_VALUE = "keyValue";
        String TRADE_PWD = "trade_pwd";//交易密码
        String AREA_ID = "area_id";
        String MOBILE_NO = "mobile_no";
        String PERSON_MOBILE = "person_mobile";
        String UNIT_NO = "unit_no";
    }

}
