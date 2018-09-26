package com.xhq.demo.constant.apiconfig;

/**
 * 作者:zhaoge
 * 时间:2017/8/3.
 * --------------------------------------------------------
 * 所有网络请求的地址
 * 192.168的接口为内网中转地址
 * 123.56的接口为外网云端地址
 * 地址以拼接的方式产生，基本格式为 “ip:端口” + “/run/具体的后缀”
 * --------------------------------------------------------
 * 注意！！
 * 所有向服务器传递的数据，是Map<"data",data>,Map<"sc",sc>的格式
 * “data”里面的数据是，本身需要请求的数据存放在Map中后，首先必须转换成Json格式的String类型，然后进行base64EncodeEx加密后的String类型的数据
 * “sc”里面的数据是，data的数据再进行md5加密后的数据
 * --------------------------------------------------------
 * 所有从服务器接受的Json数格式是
 * ｛
 * "isOk":boolean
 * "msg":""
 * "data"：｛｝
 * "sc":""
 * ｝
 * 首先Json解析完后,首先判断isOk是否为true，然后判断sc的值和data进行本地加密后的值是否一致后，对data的值进行base64EncodeEx解密，得到真实数据
 * 最后对真实的Json数据进行解析
 */
public class ApiUrl {
    /**
     * socket请求的ip地址
     */

//   public static final   String SOCKET_IP = "192.168.0.141";                             // 本地         超哥电脑，属于测试环境，局域网
//    public static final String SOCKET_IP = "123.56.13.121";                             // 云端         阿里云，属于测试环境哪里都可以连
    public static final String SOCKET_IP = "43.254.24.195";                            // 生产服务器
// 正式环境，你可以用这个，但是你没有权限，所以有些模块看不到，有些数据也不能随便改
//    public static final String SOCKET_IP = "43.254.24.39";                             // 银行卡激活测试地址

    public static int socketPort = 8098;                                        // 端口号
//    public static int socketPort = 10448;                                        // 端口号

    /**
     * Http请求前缀
     */
    public static final String URL_HOST = "https://" + SOCKET_IP + ":8443";


    /**
     * Http请求后缀
     */
    public static final String URL_HTTP = "/run/cust/common/httpService/service.do";    // 一般请求地址
    public static final String URL_UPIMAGE = "/run/attach/upload.do";                   // 上传图片请求地址
    public static final String URL_PUBLIC = "/run/cust/common/callFree/service.do";     // 对外公开服务器 （制卡流程及状态查询地址）
    public static final String URL_LOGIN = "/run/framework/login_ajax.do?";             // 登录服务器
    public static final String URL_DOWNLOAD = "/run/attach/showImg.do";                 // 下载图片请求地址
//    byte[] MSG_TAILER_CHAR = "\n".getBytes();                       // socket读写以换行符格式结尾

    /**
     * Http请求的接口
     */
    public static final String REGISTER = "2001";                                       // 新用户注册
    public static final String VERIFICATION_CODE = "2002";                              // 获取验证码
    public static final String CHANGE_PASSWORD = "2004";                                // 修改密码
    public static final String SET_PERSON_DETAILS = "2405";                             // 保存个人信息
    public static final String GET_PERSON_DETAILS = "2406";                             // 获取个人信息
    public static final String SUBMIT_REAL_NAME = "3001";                               // 提交实名认证信息
    public static final String IS_REAL_NAME = "3002";                                   // 检查是否实名认证
    public static final String HOME_INSIDE_ITEM_LIST = "3003";                          // 获主页和次页的图标列表
    public static final String ZONING = "400001";                                       // 区划
    public static final String SCAN_CHECK = "400002";                                   // 扫描验证
    public static final String LOGIN = "400003";                                        // 登陆
    public static final String CHECK_ALLCARD = "410002";                                // 查询用户是否有卡信息
    public static final String GET_ONECARD = "410004";                                  // 获取用户一卡通的信息
    public static final String SUBMIT_CIVIL_CARD = "410099";                            // 提交民政卡信息
    public static final String GET_CARD_STATISTICS = "411003";                          // 发卡统计
    public static final String GRANT_INQUIRE = "411004";                                // 发卡查询
    public static final String GET_END_COLLAR_CARD = "419999";                          // 问题卡, 结束领卡
    public static final String INQUIRE_CARD = "990003";                                 // 卡片查询
    public static final String CHECK_VERSION = "999999";                                // 检查更新
    public static final String GRANT_TREND = "411007";                                  // 发卡走势
    public static final String CHECK_ACTIVATE_CARD_INFO = "420001";                     // 根据身份证号查询此人所有相关银行卡信息
    public static final String CHECK_ACTIVATE_CARD_INFO2 = "420002";                    // 根据卡号获取卡的详细信息，并且获取到流水号
    public static final String ACTIVATE_UPLOAD_PHOTO = "420003";                        // 激活上传卡相关图片
    public static final String WORK_NUMBER_FOR_AUTH_CODE = "420004";                    // 根据柜员号发送验证码
    public static final String ACTIVATE_FINAL = "420005";                               // 提交最后一步
    public static final String SM2_KEY = "420007";                                      // 国密密钥下载
    public static final String GRANT_ACCOMPLISH_PERCENTAGE = "411008";                  // 发卡完成比例
    public static final String BODYTEST_QUERY = "430002";                               // 活体查询
    public static final String CHECK_WORK_NUMBER = "420006";                            // 确定是否有柜员号没有柜员号直接不让进
    public static final String CIVIL_ACTIVATE_INQUIRE = "420008";                       // 激活历史查询
    public static final String TRAFFIC_SUBSIDY = "411009";
    public static final String SUBMIT_SUBSIDY = "411010 ";                              //提交补贴申请
    public static final String UNDO_REPORT_LOSS_MAKEUP = "411011";                      // 注销挂失补办 卡片信息查询
    public static final String SUBMIT_UNDO_REPORT_LOSS_MAKEUP = "411012";               // 注销挂失补办 申请提交

    public static final String GET_IP = "999998";                                       //获取socket端口号

//===========================================================================================================================
//===============================================以下居家交易码==================================================================
//===========================================================================================================================

    public static final String VISIT_MANAGE_CHECK = "511000";                           //入门身份校验
    public static final String GET_PATROL_LIST = "511010";                              //档案管理：巡防员查询
    public static final String GET_VISIT_OBJECT_LIST = "511020";                        //档案管理：独居老人查询
    public static final String VISIT_PLAN_LIST = "511050";                              //巡访工作安排列表查询
    public static final String VISIT_CHECK = "511030";                                  //巡访记录查询
    public static final String VISIT_CHECK_DETAILS = "511031";                          //巡访记录查询
    public static final String VISIT_MISSION = "511040";                                //巡访任务列表查询
    public static final String VISIT_SUMMARY = "511042";                                //巡访登记总结列表查询
    public static final String VISIT_REGISTER = "511043";                               //巡访登记提交
    public static final String VISIT_OBJECT_DETAILS = "511025";                         //巡访对象详情获取
    public static final String VISIT_STAFF_DETAILS = "511015";                          //巡访员详情获取
    public static final String ADD_STAFF = "511012";                                    //添加巡视员
    public static final String ORG_DETAILS = "511001";                                  //获取组织详情
    public static final String SEARCH_REGISTER = "511011";                              //搜索注册用户(511011)
    public static final String UNATTACH_OBJECTS = "511027";                             //未绑定的用户
    public static final String BIND_VISIT_OBJECT = "511028";                            //绑定寻访对象和巡访员关系
    public static final String UNBIND_VISIT_RELATIONSHIP = "511029";                    //解绑寻访对象和巡访员关系
    public static final String ADD_CHILD_ORG = "511003";                                //添加子组织
    public static final String CHANGE_CHILD_ORG = "511004";                                //修改子组织

//  -----------------------------------------------------------------------------------------------------------------------
//  -----------------------------------------------------------------------------------------------------------------------
//  -----------------------------------------------------------------------------------------------------------------------

    public static final String Cancel_Chat_Group_Manager = "1116";          // 取消群管理员
    public static final String Get_Email_Auth_Code_Url = "2003";            // 发送邮箱验证
    public static final String Add_Group_Url = "2102";                      // 新增分组
    public static final String Shield_Msg_Url = "2201";                     // 屏蔽消息

    public static final String Create_New_Chat_Group_Url = "2302";          // 新建群组
    public static final String Modify_Chat_Group_Info_Url = "2303";         // 修改群组
    public static final String Get_Chat_Group_Notice_Url = "2306";          // 获取历史群公告
    public static final String Delete_Chat_Group_Notice_Url = "2307";       // 删除群公告
    public static final String Modify_Friend_Remarks = "2402";              // 修改好友信息（修改备注，分组）

    public static final String Verify_Auth_Code_Url = "2999";        // 验证手机验证码
    public static final String Submit_Real_Verify_Info_Url = "3001";   // 提交实名认证信息
    public static final String Cancel_Real_Verify_Request_Url = "30011";//取消实名认证请求
    public static final String Get_Real_Verify_Result_Url = "3002";   // 查询实名认证结果

    //	 String Get_Bind_Card_List_Url = "3004";   // 获取民政卡政策列表（暂缓，可以直接显示静态页面）
//	 String Get_Bind_Card_List_Url = "3005";   // 根据政策id查询政策详细信息（暂缓）
    public static final String Get_Bind_Card_List_Url = "3006";   // 获取所有绑定的卡
    public static final String Get_Card_Detail_Url = "3007";      // 获取卡详细信息
    public static final String Judge_Card_Detail_Info_Url = "3100";//绑卡片前判断是否有卡
    public static final String Get_Relation_List_Url = "31001";//获取关系列表
    public static final String Get_Reason_List_Url = "31002";//获取不制卡原因列表
    public static final String Get_Grant_Fail_Reason_List_Url = "31003";//获取领卡失败原因列表
    public static final String Get_Bind_Card_Info_Url = "30101";   // 获取绑卡提交的信息
    public static final String Submit_Card_Detail_Info_Url = "3008";      // 卡片信息提交（提交到居委会审核）
    public static final String Set_Card_Inquire_Password_Url = "3009";      // 设置民政卡查询密码
    public static final String Add_Bind_Card_Url = "3010";      // 绑定或添加卡片
    public static final String Authorization_Authenticate_Url = "3011";      // 授权认证（卡密码授权）
    public static final String Bind_Phone_Authorization_Authenticate_Url = "3012";      // 授权认证（绑定手机验证码授权）
    public static final String Delete_Card_Url = "3013";      // 删除卡片
    public static final String Get_Branch_Url = "3014";      // 根据区划获取网点列表
    public static final String Get_Reserve_List_Url = "3015";      // 根据网点id获取办事人员预约情况列表
    public static final String Submit_Reserve_Url = "3016";      // 提交预约
    public static final String Cancel_Reserve_Url = "3017";      // 取消预约
    public static final String Reserve_Handle_Url = "3018";      // 接受或拒绝预约
    public static final String Reserve_Start_Stop_Url = "3019";      // 开始或暂停预约
    public static final String Get_Reserve_Schedule_Url = "3020";      // 获取预约进度
    public static final String Inquire_Reserve_List_Url = "3021";      // 根据管理员id查询预约列表
    public static final String Card_Info_Authenticate_Url = "3022";      // 卡信息认证（银行接口卡状态校验）
    public static final String Submit_Activate_Url = "3023";      // 提交激活
    public static final String Get_Activate_Result_Url = "3024";      // 激活结果接收
    public static final String Get_Passbook_Id_List_Url = "3025";      // 人员存折号列表
    public static final String Submit_Bind_Passbook_Url = "3026";      // 提交绑折信息
    public static final String Verify_Account_Info_Url = "3027";      // 账户信息校验
    public static final String Inquire_Bind_Passbook_Info_Url = "3028";      // 查询卡折绑定信息
    public static final String Cancel_Bind_Passbook_Url = "3029";      // 取消卡折绑定
    public static final String Inquire_Subsidy_Grant_Record_Url = "3030";      // 查询补贴发放记录
    public static final String Inquire_Account_Subsidy_Overage_Url = "3031";      // 查询账户余额及补贴总余额
    public static final String Inquire_Subsidy_Consume_Record_Url = "3032";      // 查询补贴消费记录

    public static final String Jurisdiction_Of_All_Business_Card_Data_Check = "4001";      // 查询权限下所有制卡数据核对情况
    public static final String Check_Jurisdiction_Of_Different_Checks = "4002";        // 查询不同核对情况下去权限下所有人基础列表
    public static final String Submit_Make_Card_Info_Url = "4003";                                // 提交制卡信息
    public static final String Jurisdiction_Of_All_Business_Card_Data_Card = "4004";      // 查询权限下所有制卡数据领卡情况
    public static final String Situation_Of_The_Card_Access_Authority_List = "4005";      // 根据领卡情况查询权限下人基础列表
    public static final String According_To_The_Card_ID_Query_Personal_Card_Details = "4006";      // 根据卡id查询个人领卡详细信息
    public static final String Submit_Personal_Card_Details_Url = "4007";      // 个人领卡详细信息提交
}
