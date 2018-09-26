package com.xhq.common.constant.apiconfig;

import java.util.Objects;

/**
 * Description
 * Created by ${XHQ} on 2017/8/10.
 */

public interface ApiEnum {

    class Consts {
        public final static String DONOT_CHECK_SC = "DONOT_CHECK_SC";//离线消息不校验sk

        public final static int GETOFFLISTNUM = 50;//获取离线消息条数

        public final static int CLIENT_TYPE_APP = 3;//客户端类型：app

        public final static int STATU_OK = 0;//状态常量：成功
        public final static int STATU_ERROR = 1;//状态常量：失败

        public final static int EVENT_TYPE_NET_STATU = 0;//网络状态切换
        public final static int EVENT_TYPE_SOCKET_STATU = 1;//socket状态切换
        public final static int EVENT_TYPE_LOGIN_STATU = 2;//登录状态切换
        public final static int EVENT_TYPE_NEW_MSG = 3;//新消息提醒，刷角标及消息列表
        public final static int EVENT_TYPE_NEW_FRIEND = 4;//新联系人提醒，刷联系人角标
        public final static int EVENT_TYPE_BASE_ANSWER = 5;//通用返回
        public final static int EVENT_TYPE_USER_STATU = 6;//用户状态切换
        public final static int EVENT_TYPE_USER_INFO = 7;//用户信息切换
        public final static int EVENT_TYPE_USER_CATALOG = 8;//用户分组切换
        public final static int EVENT_TYPE_DELETE_FRIEND = 9;//删除联系人，刷好友列表
        public final static int EVENT_TYPE_NEW_GROUPMSG = 10;//群消息，刷联系人角标
        public final static int EVENT_TYPE_NEW_GROUPMEMBERJOIN = 11;//新群员加入
        public final static int EVENT_TYPE_SEARCH = 12;
//        public final static int EVENT_TYPE_BEQUIT =10;//登陆被挤下线
    }


    //文件类型
    enum FileType {
        HEADPHOTO(1, "头像"),
        BROW(2, "表情"),
        FILE(3, "一般文件"),
        COMPRESSION(4, "压缩文件"),
        FRONTCARDPHT(5, "身份证正面照"),
        BACKCARDPHT(6, "身份证反面照"),
        HOLDCARDPHT(7, "手持身份证照片"),
        HOLDMZCARDPHT(8, "手持民政卡照片"),
        YKT_PHOTO(9, "一卡通头像"),
        YKT_PHOTO_HAND(10, "一卡通手工帐头像"),
        YKT_HAND_FILE(11, "一卡通手工帐导入文件"),
        FAMILYREGISTER(12, "户口本正面照"),
        AUTHFRONTCARDPHT(13, "代办人身份证正面照"),
        AUTHBACKCARDPHT(14, "代办人身份证反面照"),
        YKTFRONTCARDPHT(15, "一卡通卡片正面照"),
        YKTBACKCARDPHT(16, "一卡通卡片反面照"),
        YKTCEFRELA(17, "亲属关系证明"),
        OLDCARDFRONT(18, "养老助残卡正面照"),
        PARKCARDFRONT(19, "见义勇为公园卡正面照"),
        BUSCARDFRONT(20, "见义勇为乘车卡正面照"),
        MUSEUMCARDFRONT(21, "见义勇为博物馆卡正面照"),
        MUGSHOT(22, "卡主大头照"),
        HANDCARDPIC(23, "卡主手持证件(身份证或者户口本)和一卡通照");


        public int value;
        public String name;


        FileType(int value, String name) {
            this.value = value;
            this.name = name;
        }


        //        /**
//         * 构造decode sql
//         *
//         * @param field   sql中用于分支的原始字段
//         * @param asfield 结果字段名；
//         */
//        public static String getDecodeStr(String field, String asfield) {
//            StringBuilder sb = new StringBuilder(128);
//            sb.append("CASE\n");
//            for (FileType pbs : values()) {
//                sb.append("WHEN ")
//                        .append(field)
//                        .append("=")
//                        .append(pbs.value)
//                        .append(" THEN '")
//                        .append(pbs.name)
//                        .append("'\n");
//            }
//            sb.append("END ").append(asfield);
//            return sb.toString();
//        }
//
//
        //根据值获取对象
        public static FileType getByValue(int value) {
            for (FileType bs : FileType.values()) {
                if (bs.value == value) return bs;
            }
            return null;
        }

        public static String getNameByValue(int value) {
            for (FileType bs : FileType.values()) {
                if (bs.value == value) return bs.name;
            }
            return null;
        }
    }


    //卡片类型
    enum CardType {
        // (value, name)
        PREFERENTIAL_CARD("91302", "民政优待卡"),
        YLZC_CARD("91303", "养老助残卡"),
        REGULAR_CARD("91304", "民政普通卡");
        public String value;
        public String name;


        CardType(String value, String name) {
            this.value = value;
            this.name = name;
        }


        //根据值获取对象
        public static CardType getByValue(String value) {
            for (CardType bs : CardType.values()) {
                if (Objects.equals(bs.value, value)) return bs;
            }
            return null;
        }

        public static String getNameByValue(String value) {
            for (CardType bs : CardType.values()) {
                if (Objects.equals(bs.value, value)) return bs.name;
            }
            return null;
        }

    }

    // 卡片状态
    enum CardStatus {
        DEFAULT("0", "默认"),
        CANCEL_UNDO("2", "取消注销"),
        CANCEL_LOSS("4", "取消挂失"),
        CANCEL_MAKE("6", "取消补办"),
        ACCEPTED_MAKE("7", "已受理补办"),
        MAKE("65", "已申请补办"),
        LOSTING("70", "挂失中"),
        ALREADY_LOST("75", "已挂失"),
        UNDOING("80", "注销中"),
        ALREADY_UNDO("85", "已注销"),
        DB_DQS("31", "待签收"),    // value, name
        CARD_WAIT("35", "待发卡"), // 已签收
        GET_CARD_FAIL("45", "发卡失败"),
        ALREADY_CARD("50", "已发卡");

        public String value;
        public String name;


        CardStatus(String value, String name) {
            this.value = value;
            this.name = name;
        }


        public static CardStatus getByValue(String value) {
            for (CardStatus cardStatus : CardStatus.values()) {
                if (cardStatus.value.equals(value)) return cardStatus;
            }
            return null;
        }


        public static String getNameByValue(String value) {
            for (CardStatus cardStatus : CardStatus.values()) {
                if (cardStatus.value.equals(value)) return cardStatus.name;
            }
            return null;
        }

    }


    // 注销挂失补办 的业务状态
    enum UndoLossMakeStatu {

        DEFAULT("0", "默认"),    // value, name
        UNDO("80", "注销"),
        LOSS("70", "挂失"),
        MAKE("65", "申请补办"),
        CANCEL_UNDO("2", "取消注销"),
        CANCEL_LOSS("4", "取消挂失"),
        CANCEL_MAKE("6", "取消补办"),
        ACCEPTED_MAKE("7", "已受理补办");

        public String value;
        public String name;

        UndoLossMakeStatu(String value, String name) {
            this.value = value;
            this.name = name;
        }
    }

//  ------------------------------------------------------------------------------------------------------------------------
//  ------------------------------------------------------------------------------------------------------------------------
//  ------------------------------------------------------------------------------------------------------------------------

    //指令类型
    enum CmdType {
        UNKNOWN(0, "未知"),
        LOGIN(1001, "登录"),
        //        LOGRES(1002, "登录结果"),
        BESQU(1003, "登陆被挤下线"),//
        CHANGESTATU(1004, "切换登录状态"),
        LOGOUT(1005, "注销"),//
        SENDMSG(1101, "发送文本消息"),
        SENDYUYINMSG(1102, "发送语音消息"),
        SENDPICTUREMSG(1103, "发送图片消息"),
        SENDFILEMSG(1104, "发送文件消息"),
        UPDATESELFNOTIFYFRI(1105, "修改个人信息通知联系人"),

        SENDGRONOTICE(1107, "发送群公告"),


        ADDFRIEND(1109, "添加好友/群"),
        DELFRI(1110, "删除好友"),
        DELGROUPWORDER(1112, "解散群组"),
        NEWGROMEM(1113, "新群员通知"),
        TRANSGROHOLDER(1114, "转让群主"),
        SETGROUPADMIN(1115, "置为群管理员"),
        CANCELGROADMIN(1116, "取消群管理员"),
        DELGROMEMBER(1117, "删除群成员"),
        INVITEGROMEMBER(1118, "邀请群成员"),

        QUITGROUP(1119, "退群"),
        CONFIRADD(1120, "确认添加好友/群"),

        GETOFFLINEMSGLIST(1121, "获取离线消息列表"),
        AGRADMIN(1122, "同意管理员邀请进群"),
        SYNMSG(1123, "给其他设备同步消息"),
        LOGINAGAIN(1124, "请重新登录"),
        SETLOGINPWD(1125, "设置登录密码"),

        PING(303, "心跳"),
        REGISTER(2001, "注册"),
        RANDBYTEL(2002, "手机获取验证码"),
        RANDBYEMAIL(2003, "邮箱获取验证码"),
        GETPWD(2004, "找回密码"),
        GETCATAINFO(2101, "获取分组"),
        ADDCATALOG(2102, "新增分组"),
        CHANGECATALOG(2103, "修改分组"),
        DELCATALOG(2104, "删除分组"),
        GETCATAANDFRI(2105, "获取分组及分组下联系人基本信息"),
        GETFRIFROMCATID(2106, "根据分组ID获取联系人"),
        SHIELDMSG(2201, "屏蔽消息"),
        GETOFFLINEMSG(2202, "获取具体离线消息"),

        SEARCHGROUP(2301, "添加—搜索群"),
        BUILDNEWGROUP(2302, "新建群组"),
        CHANGEGROUP(2303, "修改群组"),
        GETGROMEMBASEINFO(2304, "获取群成员基本信息"),
        GETGROBASEINFO(2305, "获取用户所有群基本信息"),
        GETGRONOTICE(2306, "获取历史群公告"),
        DELGRONOTICE(2307, "删除群公告"),

        GETFRIDETAILBYID(2401, "ID获取联系人详细信息"),

        CHANGECONINFO(2402, "修改好友信息(修改备注，分组)"),

        UPDATEUSERSINFO(2405, "修改个人信息"),
        GETUSERSINFO(2406, "获取个人信息"),
        SEARCHFRIEND(2407, "添加-搜索联系人"),
        GETGROUPANDMEMBER(2410, "获取用户所有群基本信息及群成员基本信息"),
        GETGROUPINFOBYID(2411, "ID获取群基本信息"),
        AREACODELIST(2997, "获取区划列表"),
        CHECKYZM(2999, "验证验证码"),
        IDENTIFYCOMMIT(3001, "提交认证信息"),
        GETIDENTIFY(3002, "查询认证结果"),
        GETMENU(3003, "查询服务类别"),
        FindAllCard(3006, "获取用户绑定的所有卡"),
        FindCardById(3007, "根据卡id查询卡详细信息"),
        CARDINFOCOMMIT(3008, "填报民政卡信息"),
        SetCardPwd(3009, "设置民政卡查询密码"),
        SelCard(3100, "民政卡查询"),
        SetCard(3010, "绑定/添加卡片"),
        PwdCredit(3011, "卡密码授权"),
        PhoAccredit(3012, "绑定手机验证码授权"),
        DelCardById(3013, "删除卡片"),
        GetDotByArea(3014, "根据区划获取网点列表"),
        SelCardByAuthority(4001, "查询权限下所有制卡数据核对情况"),
        SelCardUserInfo(4002, "查看各种核对状况人员基本信息"),
        ChangeState(4003, "提交人员基本信息"),
        ReceiveCard(4004, "查询权限下所领卡卡数据核对情况"),
        SelReceiveUsers(4005, "查询权限下所领卡卡数据核对情况"),
        ReceiveUserInfo(4006, "根据卡id查询个人领卡详细信息"),
        SubReceiveUsers(4007, "个人领卡详细信息提交"),
        VerifyGrantStatus(410002, "校验待领卡信息"),
        InquireCardInfo(410004, "查询卡信息"),
        InquireInWarehouseCardInfo(411001, "查询入库卡信息"),
        SubmitInWarehouseCardInfo(411002, "提交签收入库"),
        ISSUECOUNT(411003, "发卡统计"),
        STOCKAMOUNT(411004, "库存查询"),
        SELCARDCOMMON(411005, "一卡通详细信息查询通用"),
        CHECKINLOSECARD(411006, "少卡登记"),
        ActivateVerifyCardInfo(420001, "激活查询卡信息"),
        ActivateVerifyCardStatus(420002, "激活查询卡状态"),
        ActivateUploadPhoto(420003, "激活上传所需照片"),
        ActivateVerifyWorkerNum(420004, "验证工号"),
        ActivateSubmitInfo(420005, "提交激活"),
        VerifyCardStatus(430003, "卡片查询"),
        SubmitGrantCardInfo(410099, "提交领卡信息"),
        InquireInfo(990003, "查询卡信息"),
        AriseProblem(419999, "遇到问题"),
        ANSWER(999, "应答消息");
        public int value;
        public String name;


        CmdType(int value, String name) {
            this.value = value;
            this.name = name;
        }


        /**
         * 构造decode sql
         *
         * @param field   sql中用于分支的原始字段
         * @param asfield 结果字段名；
         */
        public static String getDecodeStr(String field, String asfield) {
            StringBuilder sb = new StringBuilder(128);
            sb.append("CASE\n");
            for (CmdType pbs : values()) {
                sb.append("WHEN ")
                        .append(field)
                        .append("=")
                        .append(pbs.value)
                        .append(" THEN '")
                        .append(pbs.name)
                        .append("'\n");
            }
            sb.append("END ").append(asfield);
            return sb.toString();
        }


        //根据值获取对象
        public static CmdType getByValue(int value) {
            for (CmdType bs : CmdType.values()) {
                if (bs.value == value) return bs;
            }
            return null;
        }
    }

    //用户状态
    enum UserStatuEnum {
        OFFLINE(0, "离线"),
        ONLINE(1, "在线"),
        LEAVE(2, "离开"),
        BUSY(3, "忙碌"),
        DNDST(4, "请勿打扰"),
        HIDDEN(5, "隐身");
        public int value;
        public String name;


        UserStatuEnum(int value, String name) {
            this.value = value;
            this.name = name;
        }


        /**
         * 构造decode sql
         *
         * @param field   sql中用于分支的原始字段
         * @param asfield 结果字段名；
         */
        public static String getDecodeStr(String field, String asfield) {
            StringBuilder sb = new StringBuilder(128);
            sb.append("CASE\n");
            for (UserStatuEnum pbs : values()) {
                sb.append("WHEN ")
                        .append(field)
                        .append("=")
                        .append(pbs.value)
                        .append(" THEN '")
                        .append(pbs.name)
                        .append("'\n");
            }
            sb.append("END ").append(asfield);
            return sb.toString();
        }


        //根据值获取对象
        public static UserStatuEnum getByValue(int value) {
            for (UserStatuEnum bs : UserStatuEnum.values()) {
                if (bs.value == value) return bs;
            }
            return null;
        }


        public static boolean isOnline(int us) {
            return us != OFFLINE.value && us != HIDDEN.value;
        }


        public static int compare(int us1, int us2) {
            if (us1 == us2) return 0;
            if (us1 == ONLINE.value) return 1;
            if (us2 == ONLINE.value) return -1;

            if (us1 == OFFLINE.value) return -1;
            if (us2 == OFFLINE.value) return 1;

            if (us1 == HIDDEN.value) return -1;
            if (us2 == HIDDEN.value) return 1;

            if (us1 < us2) return -1;
            return 1;
        }
    }

    //错误码
    enum ErrorCodeEnum {
        SUCCESS(0, "交易成功"),
        YZMERROR(1, "验证码错误"),
        FWMERROR(4, "防伪码错误"),
        OTHER(999, "其他");

        public int value;
        public String name;


        ErrorCodeEnum(int value, String name) {
            this.value = value;
            this.name = name;
        }


        /**
         * 构造decode sql
         *
         * @param field   sql中用于分支的原始字段
         * @param asfield 结果字段名；
         */
        public static String getDecodeStr(String field, String asfield) {
            StringBuilder sb = new StringBuilder(128);
            sb.append("CASE\n");
            for (ErrorCodeEnum pbs : values()) {
                sb.append("WHEN ")
                        .append(field)
                        .append("=")
                        .append(pbs.value)
                        .append(" THEN '")
                        .append(pbs.name)
                        .append("'\n");
            }
            sb.append("END ").append(asfield);
            return sb.toString();
        }


        //根据值获取对象
        public static ErrorCodeEnum getByValue(int value) {
            for (ErrorCodeEnum bs : ErrorCodeEnum.values()) {
                if (bs.value == value) return bs;
            }
            return null;
        }
    }

    //好友验证
    enum RetEnum {
        NOANSWER(0, "未回复"),
        AGREE(1, "同意"),
        REFUSE(2, "拒绝");


        public int value;
        public String name;


        RetEnum(int value, String name) {
            this.value = value;
            this.name = name;
        }


        /**
         * 构造decode sql
         *
         * @param field   sql中用于分支的原始字段
         * @param asfield 结果字段名；
         */
        public static String getDecodeStr(String field, String asfield) {
            StringBuilder sb = new StringBuilder(128);
            sb.append("CASE\n");
            for (RetEnum pbs : values()) {
                sb.append("WHEN ")
                        .append(field)
                        .append("=")
                        .append(pbs.value)
                        .append(" THEN '")
                        .append(pbs.name)
                        .append("'\n");
            }
            sb.append("END ").append(asfield);
            return sb.toString();
        }


        //根据值获取对象
        public static RetEnum getByValue(int value) {
            for (RetEnum bs : RetEnum.values()) {
                if (bs.value == value) return bs;
            }
            return null;
        }
    }

    //屏蔽类型
    enum MsgTypeEnum {
        NORMAL(0, "接受消息并提醒"),
        //        NoWarn(2, "接受消息但不提醒"),
//        NoPicture(3, "仅不接受图片"),
//        NoYuYin(4, "仅不接受语音"),
//        NoText(5, "仅不接受文本消息"),
//        NoAll(6, "不接受任何消息");
        SCREEN(1, "屏蔽消息"),
        DONTDISTURB(2, "免打扰");


        public int value;
        public String name;


        MsgTypeEnum(int value, String name) {
            this.value = value;
            this.name = name;
        }


        /**
         * 构造decode sql
         *
         * @param field   sql中用于分支的原始字段
         * @param asfield 结果字段名；
         */
        public static String getDecodeStr(String field, String asfield) {
            StringBuilder sb = new StringBuilder(128);
            sb.append("CASE\n");
            for (MsgTypeEnum pbs : values()) {
                sb.append("WHEN ")
                        .append(field)
                        .append("=")
                        .append(pbs.value)
                        .append(" THEN '")
                        .append(pbs.name)
                        .append("'\n");
            }
            sb.append("END ").append(asfield);
            return sb.toString();
        }


        //根据值获取对象
        public static MsgTypeEnum getByValue(int value) {
            for (MsgTypeEnum bs : MsgTypeEnum.values()) {
                if (bs.value == value) return bs;
            }
            return null;
        }
    }

    //群成员类型
    enum GroupMemberType {
        HOLDER(1, "群主"),
        ADMIN(2, "管理员"),
        MEMBER(3, "群成员"),
        STRONGER(4, "其他");


        public int value;
        public String name;


        GroupMemberType(int value, String name) {
            this.value = value;
            this.name = name;
        }


        /**
         * 构造decode sql
         *
         * @param field   sql中用于分支的原始字段
         * @param asfield 结果字段名；
         */
        public static String getDecodeStr(String field, String asfield) {
            StringBuilder sb = new StringBuilder(128);
            sb.append("CASE\n");
            for (GroupMemberType pbs : values()) {
                sb.append("WHEN ")
                        .append(field)
                        .append("=")
                        .append(pbs.value)
                        .append(" THEN '")
                        .append(pbs.name)
                        .append("'\n");
            }
            sb.append("END ").append(asfield);
            return sb.toString();
        }


        //根据值获取对象
        public static GroupMemberType getByValue(int value) {
            for (GroupMemberType bs : GroupMemberType.values()) {
                if (bs.value == value) return bs;
            }
            return null;
        }
    }

    //我的群
    enum GroupType {
        BULID(1, "我创建的群"),
        ADMIN(2, "我管理的群"),
        JOIN(3, "我加入的群");

        public int value;
        public String name;


        GroupType(int value, String name) {
            this.value = value;
            this.name = name;
        }


        /**
         * 构造decode sql
         *
         * @param field   sql中用于分支的原始字段
         * @param asfield 结果字段名；
         */
        public static String getDecodeStr(String field, String asfield) {
            StringBuilder sb = new StringBuilder(128);
            sb.append("CASE\n");
            for (GroupType pbs : values()) {
                sb.append("WHEN ")
                        .append(field)
                        .append("=")
                        .append(pbs.value)
                        .append(" THEN '")
                        .append(pbs.name)
                        .append("'\n");
            }
            sb.append("END ").append(asfield);
            return sb.toString();
        }


        //根据值获取对象
        public static GroupType getByValue(int value) {
            for (GroupType bs : GroupType.values()) {
                if (bs.value == value) return bs;
            }
            return null;
        }
    }


    //屏蔽类型
    enum IsFriEnum {
        NO(0, "不是好友/群"),
        IS(1, "是好友/群");

        public int value;
        public String name;


        IsFriEnum(int value, String name) {
            this.value = value;
            this.name = name;
        }
    }


    //认证状态
    enum IdentifyStatuEnum {
        UNCOMMITTED(10, "未提交"),
        COMMITTED(20, "待审核"),
        PASS(30, "认证通过"),
        NOTPASS(40, "认证未通过");


        public int value;
        public String name;


        IdentifyStatuEnum(int value, String name) {
            this.value = value;
            this.name = name;
        }


        /**
         * 构造decode sql
         *
         * @param field   sql中用于分支的原始字段
         * @param asfield 结果字段名；
         */
        public static String getDecodeStr(String field, String asfield) {
            StringBuilder sb = new StringBuilder(128);
            sb.append("CASE\n");
            for (IdentifyStatuEnum pbs : values()) {
                sb.append("WHEN ")
                        .append(field)
                        .append("=")
                        .append(pbs.value)
                        .append(" THEN '")
                        .append(pbs.name)
                        .append("'\n");
            }
            sb.append("END ").append(asfield);
            return sb.toString();
        }


        //根据值获取对象
        public static IdentifyStatuEnum getByValue(int value) {
            for (IdentifyStatuEnum bs : IdentifyStatuEnum.values()) {
                if (bs.value == value) return bs;
            }
            return null;
        }


        //根据值获取名称
        public static String getNameByValue(int value) {
            for (IdentifyStatuEnum bs : IdentifyStatuEnum.values()) {
                if (bs.value == value) return bs.name;
            }
            return null;
        }
    }


    //发送标志
    enum SendSignEnum {
        SENDDING(0, "发送中"),
        SUCCESS(1, "发送成功"),
        FAILED(2, "发送失败");


        public int value;
        public String name;


        SendSignEnum(int value, String name) {
            this.value = value;
            this.name = name;
        }

    }

    //发送类型
    enum SendTypeEnum {
        SEND(0, "发送"),
        RECEIVE(1, "接收");


        public int value;
        public String name;


        SendTypeEnum(int value, String name) {
            this.value = value;
            this.name = name;
        }

    }

    //是否已读
    enum ReadTypeEnum {
        UNREAD(0, "未读"),
        READ(1, "已读");


        public int value;
        public String name;


        ReadTypeEnum(int value, String name) {
            this.value = value;
            this.name = name;
        }

    }

    //添加类型
    enum AddFriendTypeEnum {
        ADD(0, "主动添加"),
        BEADD(1, "被添加"),
        SELF_JOIN_GROUP(2, "自己主动加群"),
        INVITED_BY_ADMIN(3, "被管理员邀请加群"),
        INVITE_FRIEND(4, "主动邀请好友加群"),
        INVITED_BY_MEMBER(5, "被群成员邀请入群"),
        JOIN_GROUP(6, "其他人主动加群");


        public int value;
        public String name;


        AddFriendTypeEnum(int value, String name) {
            this.value = value;
            this.name = name;
        }
    }

    //客户端类型
    enum ClientTypeEnum {
        PC(1, "pc"),
        WEB(2, "web"),
        APP(3, "app");
        public int value;
        public String name;


        ClientTypeEnum(int value, String name) {
            this.value = value;
            this.name = name;
        }


        /**
         * 构造decode sql
         *
         * @param field   sql中用于分支的原始字段
         * @param asfield 结果字段名；
         */
        public static String getDecodeStr(String field, String asfield) {
            StringBuilder sb = new StringBuilder(128);
            sb.append("CASE\n");
            for (ClientTypeEnum pbs : values()) {
                sb.append("WHEN ")
                        .append(field)
                        .append("=")
                        .append(pbs.value)
                        .append(" THEN '")
                        .append(pbs.name)
                        .append("'\n");
            }
            sb.append("END ").append(asfield);
            return sb.toString();
        }


        //根据值获取对象
        public static String getNameByValue(int value) {
            for (ClientTypeEnum bs : ClientTypeEnum.values()) {
                if (bs.value == value) return bs.name;
            }
            return null;
        }
    }

    //客户端类型
    enum CatalogOpenTypeEnum {
        CLOSE(0, "关闭"),
        OPEN(1, "展开");
        public int value;
        public String name;


        CatalogOpenTypeEnum(int value, String name) {
            this.value = value;
            this.name = name;
        }
    }

    //
    enum TransType {
        GrantCard("1", "领卡"),
        Activate("2", "激活"),
        AccountBind("3", "账户绑定");
        public String value;
        public String name;


        TransType(String value, String name) {
            this.value = value;
            this.name = name;
        }
    }


    //	1上传卡主证件照正面 2 验证照片信息与制卡信息 3上传卡主证件照反面 4 查询制卡信息5 验证二维码信息 6 上传卡片照片 7 上传持卡本身照 99 领卡成功
    enum Pick_Status {
        UploadCardFront(1, "上传卡主证件照正面"),
        VerifyCardInfo(2, "验证照片信息与制卡信息"),
        UploadCardBack(3, "上传卡主证件照反面"),
        InquireInfo(4, "查询制卡信息"),
        VerifyCode(5, "验证二维码信息"),
        UploadCardPhoto(6, "上传卡片照片"),
        UploadHandholdPhoto(7, "上传持卡本身照"),
        GrantSuccess(99, "领卡成功");
        public int value;
        public String name;


        Pick_Status(int value, String name) {
            this.value = value;
            this.name = name;
        }
    }


    enum EC_PICK {
        EC_PICK_01("EC_PICK_01", "卡面信息错误"),
        EC_PICK_02("EC_PICK_02", "卡片损毁"),
        EC_PICK_03("EC_PICK_03", "照片错误"),
        EC_PICK_04("EC_PICK_04", "二维码信息错误"),
        EC_PICK_05("EC_PICK_05", "找不到卡"),
        EC_PICK_99("EC_PICK_99", "其他");
        public String value;
        public String name;


        EC_PICK(String value, String name) {
            this.value = value;
            this.name = name;
        }
    }

    enum EducationBackground {
        PRESCHOOL("22101", "学龄前"),
        PRIMARY_SCHOOL("22102", "小学"),
        JUNIOR_HIGH_SCHOOL("22103", "初中"),
        SENIOR_HIGH_SCHOOL("22104", "高中"),
        TECHNICAL_SCHOOL("22105", "技校"),
        PROFESSIONAL_HIGH_SCHOOL("22106", "职高"),
        TECHNICAL_SECONDARY_SCHOOL("22107", "中专"),
        JUNIOR_COLLEGE("22108", "大专"),
        UNDERGRADUATE("22109", "本科"),
        MASTER("22110", "硕士"),
        DOCTOR("22111", "博士"),
        POST_DOCTORAL("22112", "博士后");

        public String value;
        public String name;

        EducationBackground(String value, String name) {
            this.value = value;
            this.name = name;
        }
    }

    enum maritalStatus {
        UNMARRIED("22001", "未婚"), MARRIED("22002", "已婚"), DIVORCE("22003", "离婚"), WIDOWED("22004", "丧偶");

        maritalStatus(String value, String name) {
            this.value = value;
            this.name = name;
        }

        public String value;
        public String name;
    }

    enum politicsStatus {
        NON_PARTISAN("21901", "群众"),
        THE_COMMUNIST_PARTY("21902", "共党员"),
        PROBATIONARY_PARTY_MEMBERS("21903", "中共预备党员"),
        THE_COMMUNIST_YOUTH_LEAGUE("21904", "共青团员"),
        DEMOCRATIC_PARTIES("21905", "民主党派"),
        INDEPENDENT_DEMOCRAT("21906", "无党派民主人士"),
        COMORBIALIST("21907", "致共党党员"),
        DPP_MEMBERS("21908", "民进会员"),
        COMMITTEE_MEMBER("21909", "民建会员"),
        THE_PAD_LEAGUER("21910", "民盟盟员"),
        THE_MDC_MEMBER("21911", "民革会员"),
        LABORIST("21912", "农工党党员"),
        _93_MEMBERS_OF_SOCIETY("21913", "九三学社社员"),
        Close_union_member("21914", "合盟盟员");

        public String value;
        public String name;

        politicsStatus(String value, String name) {
            this.value = value;
            this.name = name;
        }
    }
}
