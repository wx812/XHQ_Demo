package com.xhq.demo.cmd;

/**
 * Created by Akmm at 2017/4/17 22:20
 * 指令辅助类，一些公共方法，可能多处调用的
 */
public class ImCmdHelper {
    public interface IHttpSuccessHandler {
        void success();
    }

    /**
     * 登录成功之后要干的活。非自动登录
     * 1、初始化数据库
     * 2、初始化缓存
     * 3、请求分组及好友（异步）
     * 4、请求群组信息（异步）
     * 5、请求离线消息（异步）
     */
//    public static void doAfterLogin(Context context, LogresCmd cmd) {
//        try {
//            DbEngine.init(SettingLoader.getUserCode(context));
//            clearDataBase();//清空相关数据库
//        } catch (Exception e) {
//            LogUtil.e("初始化数据库失败!");
//            e.printStackTrace();
//        }
//        initBuffer();//先读一遍缓存，把未清库的数据读入缓存。以方便用缓存判断insert  or  update
//        getGroupAndMembers(context);
//        getCatalogFriends(context);
//
//        if (cmd.getNum() > 0) {//有离线消息，则请求离线消息
//            getOfflineMsg(context);
//        }
//        initBuffer();//初始化缓存
//        UserBaseDao userBaseDao = new UserBaseDao();
//
//        UserBaseEntity userBaseEntity = new UserBaseEntity();
//        userBaseEntity.setUserCode(cmd.getUserCode());
//        userBaseEntity.setUserId(cmd.getUserId());
//        userBaseEntity.setTel(cmd.getTel());
//        if (UtilPub.isNotEmpty(cmd.getName())) {
//            userBaseEntity.setNickName(UtilEncode.base64DecodeEx(cmd.getName()));
//        }
//        if (UtilPub.isNotEmpty(cmd.getPl())) {
//            userBaseEntity.setSignature(UtilEncode.base64DecodeEx(cmd.getPl()));
//        }
//        userBaseEntity.setPhotoId(cmd.getHid());
//        userBaseEntity.setUserType(cmd.getUt());
//
//        try {
////            UserBaseEntity user = userBaseDao.findOneByPK(userBaseEntity.getUserId());
//            UserBaseEntity user = UserBaseBuffer.getInstance().get(cmd.getUserId());
//            if (user == null) {
//                userBaseDao.insert(userBaseEntity);//个人信息存库
//            } else {
//                userBaseDao.update(userBaseEntity);
//            }
//            UserBaseBuffer.getInstance().add(userBaseEntity);
//        } catch (Exception e) {
//            LogUtil.e("我的信息插库失败!");
//        }
//        getOwnInfo();
//    }
//
//    //初始化缓存
//    private static void initBuffer() {
//        AttachBuffer.getInstance().init();
//        CatalogBuffer.getInstance().init();
//        GroupBuffer.getInstance().init();
//        GroupMemberBuffer.getInstance().init();
//        UserBaseBuffer.getInstance().init();
//        RecentContactsBuffer.getInstance().init();
//        AddFriendBuffer.getInstance().init();
//    }
//
//    /**
//     * http请求
//     *
//     * @param context  context
//     * @param cmdType  指令类别
//     * @param params   参数map，可以为空
//     * @param callBack 回调
//     */
//    public static void httpGet(final Context context, int cmdType, Map<String, String> params, final NetRequest.NetCallBack callBack) {
//        String sk = SPUtils.get(SPKey.USER_CONFIG, ApiKey.CommonUrlKey.sk, "");
//        if (UtilPub.isEmpty(sk)) {
//            return;
//        }
//        if (params == null) {
//            params = new HashMap<>();
//        }
//
//        params.put(ApiConfig.ParamsKey.SK, sk);
//        params.put(ApiConfig.ParamsKey.Mtype, String.valueOf(cmdType));
//        params.put(ApiConfig.ParamsKey.Time, Utils.getCurTime());
//        params.put(ApiConfig.ParamsKey.Ct, String.valueOf(ImEnum.Consts.CLIENT_TYPE_APP));
//
//        NetRequest.requestHttpsUrl(context, ApiConfig.getRequestUrl(ApiConfig.Http_Url), params, new NetRequest.NetCallBack() {
//            @Override
//            public void sucCallback(String str) {
//                if (UtilPub.isEmpty(str)) {//空就不管了吧
//                    return;
//                }
//                LogUtil.e("str ="+str);
//                Gson gson = new Gson();
//                HttpResult result = gson.fromJson(str, HttpResult.class);
//                if (!result.isOk()) {//错了，给个提示。这里需要完善
//                    try {
//                        callBack.failCallback(ExceptionConstant.EC_UNKNOWN, Utils.getErrorTips((Integer.parseInt(result.getMsg()))));
//                    } catch (NumberFormatException e) {
//                        callBack.failCallback(ExceptionConstant.EC_UNKNOWN, result.getMsg());
//                        e.printStackTrace();
//                    }
//                    return;
//                }
//                if (!result.getSc().equals(SnbUtils.getSecurityCode(result.getData()))) {
//                    try {
//                        callBack.failCallback(ExceptionConstant.EC_SECURITY_CODE, Utils.getErrorTips((Integer.parseInt(result.getMsg()))));
//                    } catch (NumberFormatException e) {
//                        callBack.failCallback(ExceptionConstant.EC_SECURITY_CODE, result.getMsg());
//                        e.printStackTrace();
//                    }
//                    return;
//                }
//                if ("".equals(result.getData())) {
//                    callBack.sucCallback("");
//                } else {
//                    String data = UtilEncode.base64DecodeEx(result.getData());
//                    callBack.sucCallback(data);
//                }
//            }
//
//            @Override
//            public void failCallback(int errorNo, String strMsg) {
//                callBack.failCallback(errorNo, strMsg);
//            }
//        });
//    }
//
//    /**
//     * http请求
//     *
//     * @param context  context
//     * @param cmdType  指令类别
//     * @param params   参数map，可以为空
//     * @param callBack 回调
//     */
//    public static void httpsGet(final Context context, int cmdType, Map<String, String> params, final NetRequest.NetCallBack callBack) {
//        String sk = SPUtils.get(SPKey.USER_CONFIG, ApiKey.CommonUrlKey.sk, "");
//        if (UtilPub.isEmpty(sk)) {
//            return;
//        }
//        if (params == null) {
//            params = new HashMap<>();
//        }
//
//        params.put(ApiConfig.ParamsKey.SK, sk);
//        params.put(ApiConfig.ParamsKey.Mtype, String.valueOf(cmdType));
//        params.put(ApiConfig.ParamsKey.Time, Utils.getCurTime());
//        params.put(ApiConfig.ParamsKey.Ct, String.valueOf(ImEnum.Consts.CLIENT_TYPE_APP));
//
//        NetRequest.requestHttpsUrl(context, ApiConfig.getRequestUrl(ApiConfig.Https_Url), params, new NetRequest.NetCallBack() {
//            @Override
//            public void sucCallback(String str) {
//                if (UtilPub.isEmpty(str)) {//空就不管了吧
//                    return;
//                }
//                LogUtil.e("str ="+str);
//                Gson gson = new Gson();
//                HttpResult result = gson.fromJson(str, HttpResult.class);
//                if (!result.isOk()) {//错了，给个提示。这里需要完善
//                    try {
//                        callBack.failCallback(ExceptionConstant.EC_UNKNOWN, Utils.getErrorTips((Integer.parseInt(result.getMsg()))));
//                    } catch (NumberFormatException e) {
//                        callBack.failCallback(ExceptionConstant.EC_UNKNOWN, result.getMsg());
//                        e.printStackTrace();
//                    }
//                    return;
//                }
//                if (!result.getSc().equals(SnbUtils.getSecurityCode(result.getData()))) {
//                    try {
//                        callBack.failCallback(ExceptionConstant.EC_SECURITY_CODE, Utils.getErrorTips((Integer.parseInt(result.getMsg()))));
//                    } catch (NumberFormatException e) {
//                        callBack.failCallback(ExceptionConstant.EC_SECURITY_CODE, result.getMsg());
//                        e.printStackTrace();
//                    }
//                    return;
//                }
//                if ("".equals(result.getData())) {
//                    callBack.sucCallback("");
//                } else {
//                    String data = UtilEncode.base64DecodeEx(result.getData());
//                    callBack.sucCallback(data);
//                }
//            }
//
//            @Override
//            public void failCallback(int errorNo, String strMsg) {
//                callBack.failCallback(errorNo, strMsg);
//            }
//        });
//    }
//
//
//    /**
//     * http请求
//     *
//     * @param context  context
//     * @param cmdType  指令类别
//     * @param params   参数map，可以为空
//     * @param callBack 回调
//     */
//    public static void httpGetNoDialog(final Context context, int cmdType, Map<String, String> params, final NetRequest.NetCallBack callBack) {
//        String sk = SPUtils.get(SPKey.USER_CONFIG, ApiKey.CommonUrlKey.sk, "");
//        if (UtilPub.isEmpty(sk)) {
//            return;
//        }
//        if (params == null) {
//            params = new HashMap<>();
//        }
//
//        params.put(ApiConfig.ParamsKey.SK, sk);
//        params.put(ApiConfig.ParamsKey.Mtype, String.valueOf(cmdType));
//        params.put(ApiConfig.ParamsKey.Time, Utils.getCurTime());
//        params.put(ApiConfig.ParamsKey.Ct, String.valueOf(ImEnum.Consts.CLIENT_TYPE_APP));
//
//        NetRequest.requestHttpsUrlNoDialog(context, ApiConfig.getRequestUrl(ApiConfig.Http_Url), params, new NetRequest.NetCallBack() {
//            @Override
//            public void sucCallback(String str) {
//                if (UtilPub.isEmpty(str)) {//空就不管了吧
//                    return;
//                }
//                LogUtil.e("str ="+str);
//                Gson gson = new Gson();
//                HttpResult result = gson.fromJson(str, HttpResult.class);
//                if (!result.isOk()) {
//                    try {
//                        callBack.failCallback(ExceptionConstant.EC_UNKNOWN, Utils.getErrorTips((Integer.parseInt(result.getMsg()))));
//                    } catch (NumberFormatException e) {
//                        callBack.failCallback(ExceptionConstant.EC_UNKNOWN, result.getMsg());
//                        e.printStackTrace();
//                    }
//                    return;
//                }
//                if (!result.getSc().equals(SnbUtils.getSecurityCode(result.getData()))) {
//                    try {
//                        callBack.failCallback(ExceptionConstant.EC_SECURITY_CODE, Utils.getErrorTips((Integer.parseInt(result.getMsg()))));
//                    } catch (NumberFormatException e) {
//                        callBack.failCallback(ExceptionConstant.EC_SECURITY_CODE, result.getMsg());
//                        e.printStackTrace();
//                    }
//                    return;
//                }
//                if ("".equals(result.getData())) {
//                    callBack.sucCallback("");
//                } else {
//                    String data = UtilEncode.base64DecodeEx(result.getData());
//                    callBack.sucCallback(data);
//                }
//            }
//
//            @Override
//            public void failCallback(int errorNo, String strMsg) {
//                callBack.failCallback(errorNo, strMsg);
//            }
//        });
//    }
//
//    //获取分组及好友
//    public static void getCatalogFriends(final Context context) {
//
//        httpGetNoDialog(context, ImEnum.CmdType.GETCATAANDFRI.value, null, new NetRequest.NetCallBack() {
//            @Override
//            public void sucCallback(String str) {
//                if (UtilPub.isEmpty(str)) return;
//                try {
//                    //转jsonArray
//                    JSONArray jsonArray = new JSONArray(str);
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.optJSONObject(i);
//                        saveCatalog(jsonObject);//存分组
//                        String fd = UtilEncode.base64DecodeEx(jsonObject.getString("fridata"));
//                        JSONArray userBase = new JSONArray(fd);
//                        saveUserBase(userBase);//存联系人
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                //通知界面刷新
//            }
//
//            @Override
//            public void failCallback(int errorNo, String strMsg) {
//                ToastUtils.showToastLong(context, strMsg);
//            }
//        });
//    }
//
//    //好友。分组。群。群成员。这几个表都是会重新获取的，先清空本地数据库
//    //好友不清，因为好友和服务器上不同。本地是清空cid，服务器上市直接删除。
//    private static void clearDataBase() throws Exception {
//        DbEngine.doTransaction(new AbsDbWorker() {
//            @Override
//            public void work() throws Exception {
//                CatalogDao catalogDao = new CatalogDao();
//                UserBaseDao userBaseDao = new UserBaseDao();
//                GroupMemberDao groupMemberDao = new GroupMemberDao();
//                userBaseDao.deleteAll("where cid <> '' and cid is not null");
//                catalogDao.deleteAll();
//                groupMemberDao.deleteAll();
//            }
//        });
//    }
//
//    //存分组
//    private static void saveCatalog(JSONObject jsonObject) {
//        CatalogDao catalogDao = new CatalogDao();
//        String cid = null;
//        try {
//            //将数据放到对应的分组entity中
//            cid = jsonObject.getString("cid");
//            CatalogEntity catalogEntity = CatalogBuffer.getInstance().get(cid);
//            if(catalogEntity == null){
//                catalogEntity = new CatalogEntity();
//            }
//            catalogEntity.setCatalogId(cid);
//            catalogEntity.setCatalogName(jsonObject.getString("cname"));
//            catalogEntity.setCatalogNum(jsonObject.getInt("cnum"));
//            catalogEntity.setCatalogOnLineNum(jsonObject.getInt("onum"));
//            CatalogBuffer catalogBuffer = CatalogBuffer.getInstance();
//            //存数据和缓存
//            catalogDao.insertOrUpdate(catalogEntity,catalogBuffer);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    //存联系人
//    private static void saveUserBase(JSONArray jsonArray) {
//        UserBaseDao userBaseDao = new UserBaseDao();
//        for (int i = 0; i < jsonArray.length(); i++) {
//            JSONObject jsonObject = jsonArray.optJSONObject(i);
//            try {
//                //将数据放到对应的我的联系人entity中
//                String uid = jsonObject.getString("uid");
//                UserBaseEntity userBaseEntity = UserBaseBuffer.getInstance().get(uid);
//                if(userBaseEntity == null){
//                    userBaseEntity = new UserBaseEntity();
//                }
//                userBaseEntity.setUserId(jsonObject.getString("uid"));
//                userBaseEntity.setSignature(jsonObject.getString("pl"));
//                userBaseEntity.setNickName(jsonObject.getString("name"));
//                userBaseEntity.setCatalogId(jsonObject.getString("cid"));
//                userBaseEntity.setContactNote(jsonObject.getString("cnote"));
//                userBaseEntity.setPhotoId(jsonObject.getString("hid"));
//                userBaseEntity.setUserCode(jsonObject.getString("uc"));
//                userBaseEntity.setUserType(jsonObject.getInt("ut"));
//                userBaseEntity.setMsgType(jsonObject.getInt("mt"));
//                userBaseEntity.setUserStatu(jsonObject.getInt("us"));
//                UserBaseBuffer userBaseBuffer = UserBaseBuffer.getInstance();
//                //存数据和缓存
//                userBaseDao.insertOrUpdate(userBaseEntity,userBaseBuffer);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    //获取群组
//    public static void getGroupAndMembers(final Context context) {
//
//        httpGetNoDialog(context, ImEnum.CmdType.GETGROUPANDMEMBER.value, null, new NetRequest.NetCallBack() {
//            @Override
//            public void sucCallback(String str) {
//                if (UtilPub.isEmpty(str)) return;
//                try {
//                    JSONObject jsonObject = new JSONObject(str);
//                    String groups = jsonObject.getString("groups");
//                    String members = jsonObject.getString("members");
//                    if (groups != null && members != null) {
//                        groups = UtilEncode.base64DecodeEx(groups);
//                        members = UtilEncode.base64DecodeEx(members);
//                    }
//                    JSONArray groupsArray = new JSONArray(groups);
//                    JSONArray memberArray = new JSONArray(members);
//                    GroupDao groupDao = new GroupDao();
//                    GroupMemberDao groupMemberDao = new GroupMemberDao();
//                    UserBaseDao userBaseDao = new UserBaseDao();
//                    for (int i = 0; i < groupsArray.length(); i++) {
//                        JSONObject groupObject = groupsArray.optJSONObject(i);
//                        String gid = groupObject.getString("gid");
//                        //将数据放到对应的我的群entity中
//                        GroupEntity groupEntity = GroupBuffer.getInstance().get(gid);
//                        if(groupEntity == null ){
//                            groupEntity = new GroupEntity();
//                        }
//                        groupEntity.setGroupId(gid);
//                        groupEntity.setGroupNo(groupObject.getString("gno"));
//                        groupEntity.setGroupType(groupObject.getInt("gt"));
//                        groupEntity.setGroupMemberType(groupObject.getInt("gmt"));
//                        groupEntity.setGroupPhoto(groupObject.getString("hid"));
//                        groupEntity.setGroupName(groupObject.getString("gname"));
//                        groupEntity.setGroupDesc(groupObject.getString("gdesc"));
//                        groupEntity.setBuildTime(groupObject.getString("btime"));
//                        groupEntity.setGroupNum(groupObject.getInt("gnum"));
//                        GroupBuffer groupBuffer = GroupBuffer.getInstance();
//                        groupDao.insertOrUpdate(groupEntity,groupBuffer);
//
//                    }
//                    for (int k = 0; k < memberArray.length(); k++) {
//                        JSONObject memberObject = memberArray.optJSONObject(k);
//                        //联系人
//                        UserBaseEntity userBaseEntity = UserBaseBuffer.getInstance().get(memberObject.getString("uid"));
//                        if( userBaseEntity == null){
//                            userBaseEntity = new UserBaseEntity();
//                        }
//                        userBaseEntity.setUserId(memberObject.getString("uid"));
//                        userBaseEntity.setUserCode(memberObject.getString("uc"));
//                        userBaseEntity.setPhotoId(memberObject.getString("hid"));
//                        userBaseEntity.setNickName(memberObject.getString("name"));
//                        userBaseEntity.setContactNote(memberObject.getString("cnote"));
//                        userBaseEntity.setUserStatu(memberObject.getInt("us"));
//                        userBaseEntity.setUserType(memberObject.getInt("ut"));
//                        userBaseDao.insertOrUpdate(userBaseEntity,UserBaseBuffer.getInstance());
//
//                        //群成员
//                        String uid = memberObject.getString("uid");
//                        String gid = memberObject.getString("gid");
//                        GroupMemberEntity groupMemberEntity = GroupMemberBuffer.getInstance().get(gid+uid);
//                        if(groupMemberEntity == null){
//                             groupMemberEntity = new GroupMemberEntity();
//                        }
//                        groupMemberEntity.setGroupMemberType(memberObject.getInt("gmt"));
//                        groupMemberEntity.setUserId(memberObject.getString("uid"));
//                        groupMemberEntity.setGoupId(memberObject.getString("gid"));
//
//						groupMemberEntity.setMemberId(gid+uid);
//                        groupMemberEntity.setMemberName(memberObject.getString("gnote"));
//                        GroupMemberBuffer groupMemberBuffer = GroupMemberBuffer.getInstance();
//                        groupMemberDao.insertOrUpdate(groupMemberEntity,groupMemberBuffer);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                //通知界面刷新
//            }
//
//            @Override
//            public void failCallback(int errorNo, String strMsg) {
//                ToastUtils.showToastLong(context, strMsg);
//            }
//        });
//    }
//
//
//    //获取离线消息
//    public static void getOfflineMsg(final Context context) {
//        Map<String, String> params = new HashMap<>();
//        params.put("num", String.valueOf(ImEnum.Consts.GETOFFLISTNUM));
//        httpGet(context, ImEnum.CmdType.GETOFFLINEMSGLIST.value, params, new NetRequest.NetCallBack() {
//            @Override
//            public void sucCallback(String str) {
//                if (UtilPub.isEmpty(str)) return;
//                try {
//                    JSONArray jsonArray = new JSONArray(str);
//                    for (int i = jsonArray.length()-1; i >=0 ; i--) {
//                        //按正常消息执行
//                        JSONObject jo = new JSONObject(jsonArray.getString(i));
//                        JSONObject cmd = new JSONObject();
//                        cmd.put("sc", ImEnum.Consts.DONOT_CHECK_SC);
//                        cmd.put("data", jo);
//                        CmdFactory.getInstance().dealCmd(cmd);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                //通知界面刷新
//            }
//
//            @Override
//            public void failCallback(int errorNo, String strMsg) {
//                ToastUtils.showToastLong(context, strMsg);
//            }
//        });
//    }
//
//    /**
//     * 获取详细信息
//     */
//    public static void getUserDetailInfo(final Context context, final String uid, final IHttpSuccessHandler httpHandler) {
//        Map<String, String> params = new HashMap<>();
//        params.put(ApiConfig.ParamsKey.Uid, uid);
//        httpGet(context, ImEnum.CmdType.GETFRIDETAILBYID.value, params, new NetRequest.NetCallBack() {
//            @Override
//            public void sucCallback(String data) {
//                if (UtilPub.isNotEmpty(data)) {
//                    LogUtil.e("data = "+data);
//					//存userBase表
//                    JSONObject userInfo = null;
//                    try {
//                        userInfo = new JSONObject(data);
//						UserBaseEntity userBaseEntity = UserBaseBuffer.getInstance().get(uid);
//						UserBaseDao userBaseDao = new UserBaseDao();
//                        if (userBaseEntity == null) {
//                            userBaseEntity = new UserBaseEntity();
//                        }
//						userBaseEntity.setUserId(uid);
//						userBaseEntity.setUserCode(userInfo.getString("uc"));
//						userBaseEntity.setNickName(userInfo.getString("name"));
//						userBaseEntity.setSignature(userInfo.getString("pl"));
//                        userBaseEntity.setPhotoId(userInfo.getString("hid"));
//                        userBaseEntity.setBirthday(userInfo.getString("birthday"));
//                        userBaseEntity.setCity(userInfo.getString("ac"));
//						userBaseEntity.setUserStatu(userInfo.getInt("us"));
//
//						userBaseEntity.setUserType(userInfo.getInt("ut"));
//                        userBaseEntity.setSex(userInfo.getInt("sex"));
//
//
//						userBaseDao.insertOrUpdate(userBaseEntity,UserBaseBuffer.getInstance());
//                        if (httpHandler != null) {
//                            httpHandler.success();
//                        }
//                    } catch (Exception e) {
//                        LogUtil.e("出错啦 " +e.getMessage());
//                    }
//                }
//            }
//
//            @Override
//            public void failCallback(int errorNo, String strMsg) {
//                ToastUtils.showToast(HomeApp.getAppContext(), strMsg);
//            }
//        });
//    }
//
//
//    /**
//     * 获取群基本信息
//     */
//    public static void getChatGroupInfo(final Context context, final String gid, final IHttpSuccessHandler httpHandler) {
//        Map<String, String> params = new HashMap<>();
//        params.put(ApiConfig.ParamsKey.Gid, gid);
//        httpGet(context, ImEnum.CmdType.GETGROUPINFOBYID.value, params, new NetRequest.NetCallBack() {
//            @Override
//            public void sucCallback(String str) {
//                if (UtilPub.isNotEmpty(str)) {
//                    //存group表
//                    JSONObject groupInfo = null;
//                    try {
//                        groupInfo = new JSONObject(str);
//                        GroupDao groupDao = new GroupDao();
//                        String gid = groupInfo.getString("gid");
//                        GroupEntity groupEntity = GroupBuffer.getInstance().get(gid);
//                        if(groupEntity == null){
//                            groupEntity = new GroupEntity();
//                        }
//                        groupEntity.setGroupId(gid);
//                        groupEntity.setGroupName(groupInfo.getString(ApiConfig.ParamsKey.Chat_Group_Name));
//                        groupEntity.setGroupPhoto(groupInfo.getString(ApiConfig.ParamsKey.Hid));
//                        groupEntity.setGroupDesc(groupInfo.getString(ApiConfig.ParamsKey.Chat_Group_Desc));
//                        groupEntity.setGroupNo(groupInfo.getString(ApiConfig.ParamsKey.GNo));
//                        groupEntity.setGroupType(groupInfo.getInt("gtype"));
//                        groupEntity.setBuildTime(groupInfo.getString("btime"));
//                        groupDao.insertOrUpdate(groupEntity,GroupBuffer.getInstance());
//                        if (httpHandler != null) {
//                            httpHandler.success();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void failCallback(int errorNo, String strMsg) {
//                ToastUtils.showToast(HomeApp.getAppContext(), strMsg);
//            }
//        });
//    }
//
//
//    /**
//     * 获取群成员基本信息
//     */
//    public static void getGroupMemberInfo(final Context context, final String gid, final IHttpSuccessHandler httpHandler) {
//        Map<String, String> params = new HashMap<>();
//        //通用字段
//        params.put(ApiConfig.ParamsKey.Gid, gid);
//        httpGet(context, ImEnum.CmdType.GETGROMEMBASEINFO.value, params, new NetRequest.NetCallBack() {
//
//            @Override
//            public void sucCallback(String str) {
//                if (UtilPub.isNotEmpty(str)) {
//                    //存group表
//                    JSONArray groupMember = null;
//                    try {
//                        groupMember = new JSONArray(str);
//
//                        for (int i = 0; i < groupMember.length(); i++) {
//                            //按正常消息执行
//                            JSONObject jo = new JSONObject(groupMember.getString(i));
//                            GroupMemberDao groupMemberDao = new GroupMemberDao();
//                            GroupMemberEntity groupMemberEntity = new GroupMemberEntity();
//
//                            UserBaseDao userBaseDao = new UserBaseDao();
//                            UserBaseEntity userBaseEntity = new UserBaseEntity();
//							String gid = jo.getString("gid");
//							String uid = jo.getString("uid");
//							groupMemberEntity.setMemberId(gid+uid);
//							groupMemberEntity.setGoupId(gid);
//                            groupMemberEntity.setGroupMemberType(jo.getInt("gmt"));
//                            groupMemberEntity.setUserId(uid);
//                            groupMemberEntity.setMemberName(jo.getString("gnote"));
//                            groupMemberDao.insertOrUpdate(groupMemberEntity,GroupMemberBuffer.getInstance());
//
//                            userBaseEntity.setUserId(uid);
//                            userBaseEntity.setPhotoId(jo.getString("hid"));
//                            userBaseEntity.setNickName(jo.getString("name"));
//                            userBaseDao.insertOrUpdate(userBaseEntity,UserBaseBuffer.getInstance());
//                        }
//                        if (httpHandler != null) {
//                            httpHandler.success();
//                        }
//                    } catch (Exception e) {
//                        LogUtil.e("返回数据不是JSON格式字符串!");
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void failCallback(int errorNo, String strMsg) {
//                ToastUtils.showToast(HomeApp.getAppContext(), strMsg);
//            }
//        });
//    }
//
//    private static void getOwnInfo(){
//        ImCmdHelper.httpGetNoDialog(HomeApp.getAppContext(), ImEnum.CmdType.GETUSERSINFO.value, null, new NetRequest.NetCallBack() {
//            @Override
//            public void sucCallback(String data) {
//                if (UtilPub.isNotEmpty(data)) {
//                    LogUtil.e("data = "+data);
//                    //存userBase表
//                    JSONObject userInfo = null;
//                    try {
//                        userInfo = new JSONObject(data);
//                        UserBaseEntity userBaseEntity = UserBaseBuffer.getInstance().get(SettingLoader.getUserId(HomeApp.getAppContext()));
//                        UserBaseDao userBaseDao = new UserBaseDao();
//                        if (userBaseEntity == null) {
//                            userBaseEntity = new UserBaseEntity();
//                        }
//                        userBaseEntity.setUserId(SettingLoader.getUserId(HomeApp.getAppContext()));
//                        userBaseEntity.setUserCode(userInfo.getString("uc"));
//                        userBaseEntity.setNickName(userInfo.getString("name"));
//                        userBaseEntity.setSignature(userInfo.getString("pl"));
//                        userBaseEntity.setUserStatu(userInfo.getInt("us"));
//                        userBaseEntity.setPhotoId(userInfo.getString("hid"));
//                        userBaseEntity.setUserType(userInfo.getInt("ut"));
//                        userBaseEntity.setSex(userInfo.getInt("sex"));
//                        userBaseEntity.setBirthday(userInfo.getString("birthday"));
//                        userBaseEntity.setCity(userInfo.getString("ac"));
//                        userBaseEntity.setPname(userInfo.getString("pname"));
//                        userBaseEntity.setPuid(userInfo.getString("puid"));
//                        userBaseEntity.setIa(userInfo.getInt("ia"));
//                        userBaseDao.insertOrUpdate(userBaseEntity, UserBaseBuffer.getInstance());
//                    } catch (Exception e) {
//                        LogUtil.e("返回数据不是JSON格式字符串!");
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void failCallback(int errorNo, String strMsg) {
//
//            }
//        });
//    }
//
//
//
//
//
////    //更新或插入数据库
//    private static <T extends AbsEntity> void saveData(final AbsEntityBuffer<T> absEntityBuffer, final T absEntity, final AbsEntityDao<T> absEntityDao)  {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                T a = absEntityBuffer.get(absEntity.getEntityId());
//                try {
//                    if (a == null) {
//                        absEntityDao.insert(absEntity);
//                    } else {
//                        for(String key :absEntity.getData().keySet() ){
//                            if(absEntity.getData().get(key) !=null && !"".equals(absEntity.getData().get(key))){
//                                a.put(key,absEntity.getData().get(key));
//                            }
//                        }
//                        absEntityDao.update(a);
//                    }
//                    absEntityBuffer.add(absEntity);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//    }

}
