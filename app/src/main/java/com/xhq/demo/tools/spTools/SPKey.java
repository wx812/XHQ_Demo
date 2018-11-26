package com.xhq.demo.tools.spTools;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2016/8/10.
 *     Desc  : SharePreferences Constant Key.
 *     Updt  : Description
 * </pre>
 */
public interface SPKey {

    //------------------------------------------------------------------------------------------------------------
    //----------------------------------------------SP File Name--------------------------------------------------
    //------------------------------------------------------------------------------------------------------------
    /**
     * SharePreferences的名称key
     */
    String SP_FILE_NAME = "config";

    String SP_NAME = "sp_name";


    String USER_CONFIG = "user_config";                 // 原SettingLoader 存储的文件名

    /**
     * error log file
     */
    String ERROR_LOG = "error_log";

    //存放是否第一次登录
    String FIRST_USE = "first_use";
    //存放所有上传图片id
    String ALL_UPIMAGE_ID = "allUpImage_id";
    //存放是否是身份证，是否代办
    String REGISTRATION = "registration";
    //存放实名状态
    String REAL_NAME = "real_name";
    //存放巡访身份校验信息
    String OLDMANAGE_CHECK = "oldManage_check";

    String All_UPIMAGE_PATH = "all_upimage_path";

    //------------------------------------------------------------------------------------------------------------
    //---------------------------------------------Local SP Key--------------------------------------------------
    //------------------------------------------------------------------------------------------------------------
    /**
     * 用户信息 --> 原SettingLoader 用户字段
     */
    String USER_TOKEN = "user_token";               //
    String USER_ACCOUNT = "user_account";           //
    String PWD = "user_pwd";                        //
    String UID = "uid";                             // 服务器对应的请求参数opt_user_id
    String USER_CODE = "user_code";                 //
    String USER_STATE = "user_state";               //
    String USER_NAME = "user_name";                 //
    String USER_TEL = "user_tel";                   //
    String AUTO_LOGIN = "auto_login";               //

    /**
     * 是否第一次登录
     */
    String IS_FIRST = "isFirst";

    /**
     * 实名状态
     */
    String REAL_NAME_CHECK = "real_name_check";

    /**
     * 是否是身份证，是否代办
     */
    String PAPER_TYPE = "paperType";//是身份证还是户口本
    String IS_REPLACE = "isReplace"; //是否代办

    /**
     * 上传图片返回id的key
     */
    String IDCARD_FRONT_ID = "idCardFront_id"; //身份证正面
    String IDCARD_FRONT_ID_WITHOUT_CHECK = "idCardFront_id"; //身份证正面没有校验
    String IDCARD_BACK_ID = "idCardBack_id";   //身份证反面
    String IDCARD_BACK_ID_WITHOUT_CHECK = "idCardBack_id";   //身份证反面没有校验
    String RESIDENT_CARD_ID = "residentCard_id";//户口本
    String OLD_MAN_ID = "oldMan_id";//养老助残卡
    String BUS_ID = "bus_id";//乘车卡
    String PARK_ID = "park_id";//公园卡
    String VISIT_ID = "visit_id";//博物馆卡
    String ONE_CARD_ID = "oneCard_id";//一卡通卡
    String HAND_IDCARD_ID = "handIdCard_id";//手持身份证正面
    String MUGSHOT_ID = "imageMugShotId"; // 卡主大头照
    String HAND_CARD_ID = "imageHandCardPicId"; // 手持证件照
    String BEHALF_IDCARD_FRONT_ID = "imageBehalfIDCardFrontId"; // 代办人身份证正面照
    String BEHALF_IDCARD_BACK_ID = "imageBehalfIDCardBackId"; // 代办人身份证反面面照
    String BEHALF_RESIDENT_ACCOUNT_ID = "imageBehalfResidentAccountId"; // 代办人户口本照
    String BEHALF_RELATIONSHIP_PROOF_ID = "imageRelationShipProofId"; // 代办人亲属关系证明照
    String BODY_TEST_PIC_ID = "bodyTestPic_Id"; // 活体验证照片
    String BODY_TEST_VIDEO_ID = "bodyTestVideo_Id"; // 活体验证照片

    /**
     * 巡访身份校验信息
     */
    String ORG_ID = "org_id";//组织id
    String POSITION = "position";//职位
    String STAFF_ID = "staff_id";//职工id
    String PERSON_ID = "person_id";//老人id
    String ORG_NAME = "org_name";//组织名称
}
