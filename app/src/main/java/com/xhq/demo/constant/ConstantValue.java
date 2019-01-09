package com.xhq.demo.constant;

/**
 * Enum比静态常量，至少需要多过于2倍以上的内存空间，应该在Android中避免使用枚举。
 *
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/8/14.
 *     Desc  : constant value of the interface doc.
 *     Updt  : Description.
 * </pre>
 */
public interface ConstantValue {
    long READ_SECONDS = 60;
    long WRITE_SECONDS = 60;
    long CONNECT_SECONDS = 60;


    String SUCCESS_CODE = "0";      // 服务器返回数据成功代号
    String FAIL_CODE = "-1";      // 服务器返回数据失败代号

    String CHARSET_UTF8 = "utf-8";


    /**
     * mugshot verify Callback interface of server
     */
    String MUGSHOT_VERIFY = "com.dfp.plugin.homeApp.common.AfterUploadFaceVerify";

    String PIC_PATH = "JJKJ_PIC_ATTACH"; // 需要传给服务器的图片路径
    String PIC_PATH_VISIT = "JJKJ_PIC_ATTACH/VISIT"; //巡访图片的上传路径

    String CRASH_LOG_PATH = "JJKJ_CRASH_LOG"; // app exception interface


    // 领卡遇到问题
    String EC_PICK_01 = "EC_PICK_01";                 //:卡面信息错误
    String EC_PICK_02 = "EC_PICK_01";                 //:卡片损毁
    String EC_PICK_03 = "EC_PICK_01";                 //:照片错误
    String EC_PICK_04 = "EC_PICK_01";                 //:二维码信息错误
    String EC_PICK_05 = "EC_PICK_01";                 //:找不到卡
    String EC_PICK_99 = "EC_PICK_01";                 //:其他


    // 卡片类型
    String PREFERENTIAL_CARD = "91302";   // 优待卡
    String YLZC_CARD = "91303";     // 养老助残卡
    String ORDINARY_CARD = "91304";   // 普通卡


    // 卡片状态
    String DB_DQS = "31";           //"31, "待签收"),
    String CARD_WAIT = "35";        //"35, "待发卡"), //已签收
    String GET_CARD_FAIL = "45";    //"45, "发卡失败"),
    String CARD = "50";             //"50, "已发卡")
    // 注销 挂失 补办
    String DEFAULT = "0";               // 默认
    String CANCEL_UNDO = "2";           // 取消注销
    String CANCEL_LOSS = "4";           // 取消挂失
    String CANCEL_MAKE = "6";           // 取消补办
    String ACCEPTED_MAKE = "7";         // 已受理补办
    String MAKE = "65";                 // 已申请补办
    String LOSTING = "70";              // 挂失中
    String ALREADY_LOSS = "75";         // 已挂失
    String UNDOING = "80";              // 注销中
    String ALREADY_UNDO = "85";         // 已注销

    // 卡片注销原因
    String DIED_1 = "1";                  // 已死亡
    String STOPPED_2 = "2";               // 已停发
    String ALREADY_PENSION_CARD_3 = "3";  // 已有养老卡
    String NOT_REPLACE_4 = "4";           // 不换卡
    String LOVELORN_5 = "5";              // 失联
    String NAME_ID_ERROR_7 = "7";         // 姓名或身份证号错误
    String OTHER_99 = "99";               // 其它


    // 卡片查询Intent传递的bean
    String INQUIRE_CARD = "inquire_card";

    // 一个人的卡片队列，包含卡的激活状态
    String ACTIVATE_INFO_BEANS = "activate_info_beans";
    String ACTIVATE_DATA = "activate_data";
    String ACTIVATE_INFO_BEAN2 = "activateInfoBean2";

    String APPLY_LIST_BEAN = "ApplyListBean";
    String CARD_BEAN = "CardBean";
    String BTBZ_MAP = "btbzMap";//补贴标准

    String STAFF_ID = "staff_id";
    String PERSON_ID = "person_id";
    String ORG_ID = "org_id";
    String CHECK_REGISTER_BEAN = "check_register_bean";
    String CHECK_WORK_NUMBER = "check_work_number";
    String ORG_NAME = "org_name";
    String ADD_STAFF_FOR_UPLOAD_BEAN = "add_staff_for_upload_bean";
    String PERSON_NAME = "person_name";
    String POSITION = "position";
    String PARENT_ORG_ID = "parent_org_id";
    String PARENT_ORG_NAME = "parent_org_name";
    String SIR_NAME = "sir_name";
    String SIR_ID = "sir_id";
    String HAVE_ADMIN = "have_admin";
}
