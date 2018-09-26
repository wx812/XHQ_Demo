package com.xhq.demo.cmd.buffer.entityBuffer;

import com.xhq.demo.cmd.buffer.AbsEntityBuffer;
import com.xhq.demo.db.db_sql.entity.GroupEntity;
import com.xhq.demo.db.db_sql.entity.RecentContactsDao;
import com.xhq.demo.db.db_sql.entity.RecentContactsEntity;
import com.xhq.demo.db.db_sql.entity.UserBaseEntity;
import com.xhq.demo.db.db_sql.interf.IBeanWorker;
import com.xhq.demo.tools.choppedTools.UtilPub;

import java.util.Comparator;
import java.util.List;

/**
 * Created by zyj on 2017/4/13.
 *
 */

public class RecentContactsBuffer extends AbsEntityBuffer<RecentContactsEntity> {
    @Override
    protected RecentContactsDao getEntityDao() {
        return new RecentContactsDao();
    }

    private static RecentContactsBuffer INSTANCE = null;

    private RecentContactsBuffer() {
    }
    //未读消息个数
    private volatile int totalUnreadMsgNum = 0;

    //未读加群消息
    private volatile int addGroupUnreadNum = 0;


    public static RecentContactsBuffer getInstance() {
        if (INSTANCE == null) {
            synchronized (RecentContactsBuffer.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RecentContactsBuffer();
//                    inst.init();
//                    INSTANCE = inst;
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 本地刷新，所有数据；
     *
     * @throws Exception
     */
    //在初始化缓存的时候把未读消息总数和未读加好友消息总数维护
    protected void localReload() throws Exception{
        clear();
        totalUnreadMsgNum = 0;
        final RecentContactsDao dao = getEntityDao();

        //未读消息总数
        dao.findAll(getSqlAddtion(), new IBeanWorker<RecentContactsEntity>() {
            @Override
            public void doWork(RecentContactsEntity entity) throws Exception{
                try {
                    add(entity);
                    if(entity.getId().equals(entity.getGoupId()+entity.getUserId())){
                        totalUnreadMsgNum += getTotalUnreadGroupMsgNum();
                    }else {
                        totalUnreadMsgNum += entity.getUnreadNum();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });

    }

    //未读加群消息+1
    public void incAddGroupUnreadNum() {
        addGroupUnreadNum++;
    }

    //未读加群消息-1
    public void decAddGroupUnreadNum() {
        addGroupUnreadNum--;
    }

    //未读加群复位
    public void initAddGroupUnreadNum() {
        addGroupUnreadNum = 0;
    }

    public int getTotalUnreadGroupMsgNum() {
        return addGroupUnreadNum < 0 ? 0:addGroupUnreadNum;
    }

    //未读加群消息+1
    public void decAddGroupUnreadNum(int count) {
        addGroupUnreadNum = addGroupUnreadNum - count;
    }

    //未读消息总数+1
    public void incTotalUnreadMsgNum() {
        totalUnreadMsgNum++;
    }

    public void decTotalUnreadMsgNum() {
        totalUnreadMsgNum--;
    }

    public void decTotalUnreadMsgNum(int count) {
        totalUnreadMsgNum -= count;
    }

    public int getTotalUnreadMsgNum() {
        return totalUnreadMsgNum < 0 ? 0:totalUnreadMsgNum;
    }


    //按时间顺序倒序排序
    public List<RecentContactsEntity> getSortedList() {
        return getCacheList(new Comparator<RecentContactsEntity>() {
            @Override
            public int compare(RecentContactsEntity o1, RecentContactsEntity o2) {
                Long v = o1.getLastTime() - o2.getLastTime();
                return v.intValue();
            }
        });
    }

    //获取联系人姓名
    public String getContactName(RecentContactsEntity item) {
        if (UtilPub.isEmptyId(item.getGoupId())) {
            UserBaseEntity user = UserBaseBuffer.getInstance().get(item.getUserId());
            return user != null ? user.getNickName() : item.getUserId();
        }

        GroupEntity user = GroupBuffer.getInstance().get(item.getGoupId());
        return user != null ? user.getGroupName() : item.getGoupId();
    }
}
