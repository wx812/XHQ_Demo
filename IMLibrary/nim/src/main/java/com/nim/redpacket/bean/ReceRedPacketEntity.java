package com.nim.redpacket.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Administrator
 * 2018/2/2
 * 10:49
 */
public class ReceRedPacketEntity implements Serializable {


    /**
     * recSumPacketMoney : 0
     * packetDetailList : []
     * state : 15
     * redPacket : {"userId":80,"packetTotalAmount":1,"packetContent":"恭喜发财，大吉大利","packetAmountReturn":0,"endTimeStart":null,"endTimeEnd":null,"createTime":"2018-02-02 10:36:22","packetNumReceive":0,"packetNum":1,"packetStatus":"0","packetType":"0","packetAppointUserId":null,"userType":"0","userCode":"13260698507","createTimeEnd":null,"createTimeStart":null,"userPhone":"13260698507","serialNo":"sandboxHB20180202000010","userPic":"http://tsnrhapp.oss-cn-hangzhou.aliyuncs.com/1475563053573.jpg","userTeam":"1095","id":343,"endTime":"2018-02-03 10:36:22","userName":"teacher"}
     */

    private String recSumPacketMoney;
    private String myPacketMoney;
    private int state;
    private RedPacketBean redPacket;
    private List<PacketDetailBean> packetDetailList;

    public String getMyPacketMoney() {
        return myPacketMoney;
    }

    public void setMyPacketMoney(String myPacketMoney) {
        this.myPacketMoney = myPacketMoney;
    }

    public String getRecSumPacketMoney() {
        return recSumPacketMoney;
    }

    public void setRecSumPacketMoney(String recSumPacketMoney) {
        this.recSumPacketMoney = recSumPacketMoney;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public RedPacketBean getRedPacket() {
        return redPacket;
    }

    public void setRedPacket(RedPacketBean redPacket) {
        this.redPacket = redPacket;
    }

    public List<PacketDetailBean> getPacketDetailList() {
        return packetDetailList;
    }

    public void setPacketDetailList(List<PacketDetailBean> packetDetailList) {
        this.packetDetailList = packetDetailList;
    }

    public static class RedPacketBean implements Serializable {
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

    public static class PacketDetailBean implements Serializable {

        /**
         * id : 11
         * packetId : 2
         * userId : 1041
         * userCode : fasongrn
         * userPhone : 15896352147
         * userName : mingcheng
         * userPic : httpm/sysImg/01.jpg
         * packetType : 1
         * packetMoney : 100
         * receiveUserId : 2
         * receiveUserName : 张三1
         * receiveUserPhone : 13913852520
         * receiveUserCode : zhangsan
         * receiveUserPic : httpm/sysImg/02.jpg
         * receiveTime : 2018-01-08 15:29:33
         * luck : 1
         * status : 1
         */

        private String id;
        private String packetId;
        private String userId;
        private String userCode;
        private String userPhone;
        private String userName;
        private String userPic;
        private String packetType;
        private String packetMoney;
        private String receiveUserId;
        private String receiveUserName;
        private String receiveUserPhone;
        private String receiveUserCode;
        private String receiveUserPic;
        private String receiveTime;
        private String luck;
        private String status;

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPic() {
            return userPic;
        }

        public void setUserPic(String userPic) {
            this.userPic = userPic;
        }

        public String getPacketType() {
            return packetType;
        }

        public void setPacketType(String packetType) {
            this.packetType = packetType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPacketId() {
            return packetId;
        }

        public void setPacketId(String packetId) {
            this.packetId = packetId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPacketMoney() {
            return packetMoney;
        }

        public void setPacketMoney(String packetMoney) {
            this.packetMoney = packetMoney;
        }

        public String getReceiveUserId() {
            return receiveUserId;
        }

        public void setReceiveUserId(String receiveUserId) {
            this.receiveUserId = receiveUserId;
        }

        public String getReceiveUserName() {
            return receiveUserName;
        }

        public void setReceiveUserName(String receiveUserName) {
            this.receiveUserName = receiveUserName;
        }

        public String getReceiveUserPhone() {
            return receiveUserPhone;
        }

        public void setReceiveUserPhone(String receiveUserPhone) {
            this.receiveUserPhone = receiveUserPhone;
        }

        public String getReceiveUserCode() {
            return receiveUserCode;
        }

        public void setReceiveUserCode(String receiveUserCode) {
            this.receiveUserCode = receiveUserCode;
        }

        public String getReceiveUserPic() {
            return receiveUserPic;
        }

        public void setReceiveUserPic(String receiveUserPic) {
            this.receiveUserPic = receiveUserPic;
        }

        public String getReceiveTime() {
            return receiveTime;
        }

        public void setReceiveTime(String receiveTime) {
            this.receiveTime = receiveTime;
        }

        public String getLuck() {
            return luck;
        }

        public void setLuck(String luck) {
            this.luck = luck;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
