package com.nim.redpacket.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 领取红包Entity
 * author：shiyang
 * date: 2018/1/15
 */

public class RedPacketPickDetailEntity implements Serializable {
    private int state;
    private String msg;
    private String recSumPacketMoney;
    private String myPacketMoney;
    private RedPacketBean redPacket; //红包详情
    private List<PacketDetailListBean> packetDetailList;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RedPacketBean getRedPacket() {
        return redPacket;
    }

    public void setRedPacket(RedPacketBean redPacket) {
        this.redPacket = redPacket;
    }

    public void setPacketDetailList(List<PacketDetailListBean> packetDetailList) {
        this.packetDetailList = packetDetailList;
    }

    public String getRecSumPacketMoney() {
        return recSumPacketMoney;
    }

    public void setRecSumPacketMoney(String recSumPacketMoney) {
        this.recSumPacketMoney = recSumPacketMoney;
    }

    public String getMyPacketMoney() {
        return myPacketMoney;
    }

    public void setMyPacketMoney(String myPacketMoney) {
        this.myPacketMoney = myPacketMoney;
    }

    public List<PacketDetailListBean> getPacketDetailList() {
        return packetDetailList;
    }

    public static class RedPacketBean {

        /**
         * userId : 80
         * packetTotalAmount : 1
         * packetContent : 恭喜发财，大吉大利
         * packetAmountReturn : 0
         * endTimeStart : null
         * endTimeEnd : null
         * createTime : 2018-02-02 10:36:22
         * packetNumReceive : 0
         * packetNum : 1
         * packetStatus : 0
         * packetType : 0
         * packetAppointUserId : null
         * userType : 0
         * userCode : 13260698507
         * createTimeEnd : null
         * createTimeStart : null
         * userPhone : 13260698507
         * serialNo : sandboxHB20180202000010
         * userPic : http://tsnrhapp.oss-cn-hangzhou.aliyuncs.com/1475563053573.jpg
         * userTeam : 1095
         * id : 343
         * endTime : 2018-02-03 10:36:22
         * userName : teacher
         */

        private String userId;
        private String packetTotalAmount;
        private String packetContent;
        private int packetAmountReturn;
        private Object endTimeStart;
        private Object endTimeEnd;
        private String createTime;
        private int packetNumReceive;
        private int packetNum;
        private int packetStatus;
        private String packetType;
        private Object packetAppointUserId;
        private String userType;
        private String userCode;
        private Object createTimeEnd;
        private Object createTimeStart;
        private String userPhone;
        private String serialNo;
        private String userPic;
        private String userTeam;
        private String id;
        private String endTime;
        private String userName;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPacketTotalAmount() {
            return packetTotalAmount;
        }

        public void setPacketTotalAmount(String packetTotalAmount) {
            this.packetTotalAmount = packetTotalAmount;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPacketContent() {
            return packetContent;
        }

        public void setPacketContent(String packetContent) {
            this.packetContent = packetContent;
        }

        public int getPacketAmountReturn() {
            return packetAmountReturn;
        }

        public void setPacketAmountReturn(int packetAmountReturn) {
            this.packetAmountReturn = packetAmountReturn;
        }

        public Object getEndTimeStart() {
            return endTimeStart;
        }

        public void setEndTimeStart(Object endTimeStart) {
            this.endTimeStart = endTimeStart;
        }

        public Object getEndTimeEnd() {
            return endTimeEnd;
        }

        public void setEndTimeEnd(Object endTimeEnd) {
            this.endTimeEnd = endTimeEnd;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getPacketNumReceive() {
            return packetNumReceive;
        }

        public void setPacketNumReceive(int packetNumReceive) {
            this.packetNumReceive = packetNumReceive;
        }

        public int getPacketNum() {
            return packetNum;
        }

        public void setPacketNum(int packetNum) {
            this.packetNum = packetNum;
        }

        public int getPacketStatus() {
            return packetStatus;
        }

        public void setPacketStatus(int packetStatus) {
            this.packetStatus = packetStatus;
        }

        public String getPacketType() {
            return packetType;
        }

        public void setPacketType(String packetType) {
            this.packetType = packetType;
        }

        public Object getPacketAppointUserId() {
            return packetAppointUserId;
        }

        public void setPacketAppointUserId(Object packetAppointUserId) {
            this.packetAppointUserId = packetAppointUserId;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public Object getCreateTimeEnd() {
            return createTimeEnd;
        }

        public void setCreateTimeEnd(Object createTimeEnd) {
            this.createTimeEnd = createTimeEnd;
        }

        public Object getCreateTimeStart() {
            return createTimeStart;
        }

        public void setCreateTimeStart(Object createTimeStart) {
            this.createTimeStart = createTimeStart;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getSerialNo() {
            return serialNo;
        }

        public void setSerialNo(String serialNo) {
            this.serialNo = serialNo;
        }

        public String getUserPic() {
            return userPic;
        }

        public void setUserPic(String userPic) {
            this.userPic = userPic;
        }

        public String getUserTeam() {
            return userTeam;
        }

        public void setUserTeam(String userTeam) {
            this.userTeam = userTeam;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }

    public static class PacketDetailListBean {
        /**
         * id : 74
         * userName : Mark
         * userId : 1095
         * status : 1
         * receiveTimeStart : null
         * receiveTimeEnd : null
         * receiveUserPic : http://tsnrhapp.oss-cn-hangzhou.aliyuncs.com/1472198111108.jpg
         * receiveUserName : Mark
         * packetId : 40
         * packetMoney : 0.65
         * luck : 0 //手气最佳(0否1是)
         * packetType : 1
         * userPic : http://tsnrhapp.oss-cn-hangzhou.aliyuncs.com/1472198111108.jpg
         * receiveUserId : 1095
         * receiveUserPhone : 18520122598
         * userPhone : 18520122598
         * receiveTime : 2018-01-15 10:30:45
         */

        private int id;
        private String userName;
        private int userId;
        private String userCode;//发红包usercode
        private String receiveUserCode;//收红包usercode
        private String status;
        private Object receiveTimeStart;
        private Object receiveTimeEnd;
        private String receiveUserPic;
        private String receiveUserName;
        private int packetId;
        private double packetMoney;
        private String luck;
        private String packetType;
        private String userPic;
        private int receiveUserId;
        private String receiveUserPhone;
        private String userPhone;
        private String receiveTime;

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public String getReceiveUserCode() {
            return receiveUserCode;
        }

        public void setReceiveUserCode(String receiveUserCode) {
            this.receiveUserCode = receiveUserCode;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Object getReceiveTimeStart() {
            return receiveTimeStart;
        }

        public void setReceiveTimeStart(Object receiveTimeStart) {
            this.receiveTimeStart = receiveTimeStart;
        }

        public Object getReceiveTimeEnd() {
            return receiveTimeEnd;
        }

        public void setReceiveTimeEnd(Object receiveTimeEnd) {
            this.receiveTimeEnd = receiveTimeEnd;
        }

        public String getReceiveUserPic() {
            return receiveUserPic;
        }

        public void setReceiveUserPic(String receiveUserPic) {
            this.receiveUserPic = receiveUserPic;
        }

        public String getReceiveUserName() {
            return receiveUserName;
        }

        public void setReceiveUserName(String receiveUserName) {
            this.receiveUserName = receiveUserName;
        }

        public int getPacketId() {
            return packetId;
        }

        public void setPacketId(int packetId) {
            this.packetId = packetId;
        }

        public double getPacketMoney() {
            return packetMoney;
        }

        public void setPacketMoney(double packetMoney) {
            this.packetMoney = packetMoney;
        }

        public String getLuck() {
            return luck;
        }

        public void setLuck(String luck) {
            this.luck = luck;
        }

        public String getPacketType() {
            return packetType;
        }

        public void setPacketType(String packetType) {
            this.packetType = packetType;
        }

        public String getUserPic() {
            return userPic;
        }

        public void setUserPic(String userPic) {
            this.userPic = userPic;
        }

        public int getReceiveUserId() {
            return receiveUserId;
        }

        public void setReceiveUserId(int receiveUserId) {
            this.receiveUserId = receiveUserId;
        }

        public String getReceiveUserPhone() {
            return receiveUserPhone;
        }

        public void setReceiveUserPhone(String receiveUserPhone) {
            this.receiveUserPhone = receiveUserPhone;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getReceiveTime() {
            return receiveTime;
        }

        public void setReceiveTime(String receiveTime) {
            this.receiveTime = receiveTime;
        }
    }

}
