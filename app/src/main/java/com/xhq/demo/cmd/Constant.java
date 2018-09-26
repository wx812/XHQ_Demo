package com.xhq.demo.cmd;

public class Constant {
    public static final String MAX_NUMBER = "MaxNumber";
    public static final String IS_NEED_CAMERA = "IsNeedCamera";

    public static final int REQUEST_CODE_PICK_IMAGE = 0x100;
    public static final String RESULT_PICK_IMAGE = "ResultPickImage";
    public static final int REQUEST_CODE_TAKE_IMAGE = 0x101;

    public static final int REQUEST_CODE_BROWSER_IMAGE = 0x102;
    public static final String RESULT_BROWSER_IMAGE = "ResultBrowserImage";

    public static final int REQUEST_CODE_PICK_VIDEO = 0x200;
    public static final String RESULT_PICK_VIDEO = "ResultPickVideo";
    public static final int REQUEST_CODE_TAKE_VIDEO = 0x201;

    public static final int REQUEST_CODE_PICK_AUDIO = 0x300;
    public static final String RESULT_PICK_AUDIO = "ResultPickAudio";
    public static final int REQUEST_CODE_TAKE_AUDIO = 0x301;

    public static final int REQUEST_CODE_PICK_FILE = 0x400;
    public static final String RESULT_PICK_FILE = "ResultPickFILE";

    public static final int REQUEST_CODE_CROP_IMG = 0x103;

    public static final int REQUEST_CODE_SCAN = 1001;

    public static final int IdFront = 1;
    public static final int IdBack = 2;
    public static final int IdHold = 3;
    public static final int IdCivilCard = 4;

    public static final int SELF = 1;     //本人
    public static final int KIN = 2;      //亲属
    public static final int Friend = 3;      //朋友
    public static final int Worker = 4;  //工作人员

    public static final String CommonCard = "91304";     //91304普通卡
    public static final String PreferentialCard = "91302"; //91302优待卡
    public static final String PensionCard= "91303";      //91303养老助残卡
//    public static final int PensionCard= 2;      //91301 不制卡
    public static String TEST_IP = "123.56.13.121";
    //绑定状态
    public static final int NotBind = 0;        //未绑定
    public static final int Binding= 1;         //绑定中
    public static final int Binded= 3;          //已绑定
    public static final int AUTHING= 5;         //认证中
    public static final int AUTH= 9;            //已认证

    public static final String RecentMsgGroupNoticeKey = "RecentMsgGroupNoticeKey";  //群通知的主键id

    /**
     * 上传照片类型
     */
    public static final int Upload_Type_Head = 1;
    public static final int Upload_Type_Emoji = 2;
    public static final int Upload_Type_File = 3;
    public static final int Upload_Type_ZIP = 4;
    public static final int Upload_Type_ID_Front = 5;
    public static final int Upload_Type_ID_Back = 6;
    public static final int Upload_Type_Handled_ID = 7;
    public static final int Upload_Type_Handled_Civil_Card = 8;

    public static final int Type_Browse = 0;
    public static final int Type_Select = 1;

    public static final int Group_Chat = 1;   // 群聊
    public static final int Friend_Chat = 0;  //私聊
    public static final int Stranger_Chat = 2; //临时会话
    public static final int NewGroupLordSelect = 1;
    public static final int GroupManagerSelect = 2;
    public static final int GroupMemberBrowse = 0;

    public static final int Request_Chat_Group_Create = 1000;
    public static final int Request_ModifyInfo = 1001;
    public static final int Request_Group_Manager = 1002;
    public static final int Request_Group_Select = 1003;
    public static final int Request_Chat_Group_Introduce = 1004;
    public static final int Request_Emergency_Contact = 1005;
    public static final int Request_GroupNoticePublish = 1006;


    public static final int CancelM = 1000;
    public static final int  TransG = 1001;

    public static class Action {
        public static final String BroadCast_Action_DisconnectMsg = "BroadCast_Action_DisconnectMsg";       //连接失败
        public static final String BroadCast_Action_SocketTimeout = "BroadCast_Action_SocketTimeout";       //响应超时

        public static final String BroadCast_Action_NewGroupMsg = "BroadCast_Action_NewGroupMsg";           //群消息变更
        public static final String BroadCast_Action_ReceiveTextMsg = "BroadCast_Action_ReceiveTextMsg";

        public static final String BroadCast_Action_NewFriends_Notice = "BroadCast_Action_NewFriends_Notice";//新朋友 消息界面
        public static final String BroadCast_Action_NewMsg = "BroadCast_Action_NewMsg";                     //新消息更新
        public static final String BroadCast_Action_Friends_Online  = "BroadCast_Action_Friends_Online";//好友上线

        public static final String BroadCast_Action_FriendInfo_Changed  = "BroadCast_Action_FriendInfo_Changed";//好友更改资料
        public static final String BroadCast_Action_FriendGroup_Changed  = "BroadCast_Action_FriendGroup_Changed";//好友分组 删除的变更

        public static final String BroadCast_Action_DeleteOrRead_Msg  = "BroadCast_Action_DeleteOrRead_Msg";//已读或删除最近联系人

        public static final String BroadCast_Action_GroupMemberChanged  = "BroadCast_Action_GroupMemberChanged";//群成员变更

        public static final String BroadCast_Action_GroupLongClick = "BroadCast_Action_GroupLongClick";
    }

    public  static final String CONFIG = "user_config";

    public final static int GrantCardRequest = 1;//suMybmit返回值
    public final static String JPG = ".jpg";
	public final static int  WaitGrantCard = 38;
	public final static int  GrantCardSucceed = 50;
	public final static int  GrantCardFail = 45;
    public final static String YKT_PHOTO = "ykt_photo";

    public final static int MaxImgSize = 300 * 1024;
    public final static int CompressQuality = 60;

}
