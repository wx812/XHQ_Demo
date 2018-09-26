package com.xhq.demo.cmd.buffer.entityBuffer;

import com.xhq.demo.cmd.buffer.AbsEntityBuffer;
import com.xhq.demo.constant.apiconfig.ApiEnum;
import com.xhq.demo.db.db_sql.entity.AddFriendDao;
import com.xhq.demo.db.db_sql.entity.AddFriendEntity;
import com.xhq.demo.db.db_sql.interf.IBeanWorker;
import com.xhq.demo.tools.choppedTools.UtilPub;

/**
 * Created by zyj on 2017/4/22.
 *
 */

public class AddFriendBuffer extends AbsEntityBuffer<AddFriendEntity> {
    @Override
    protected AddFriendDao getEntityDao() {
        return new AddFriendDao() ;
    }

    private static AddFriendBuffer INSTANCE = null;

    private AddFriendBuffer() {
    }

    public static AddFriendBuffer getInstance() {
        if (INSTANCE == null) {
            synchronized (AddFriendBuffer.class) {
                if (INSTANCE == null) {
                    AddFriendBuffer inst = new AddFriendBuffer();
//                    inst.init();
                    INSTANCE = inst;
                }
            }
        }
        return INSTANCE;
    }
    //未读加群消息
    private volatile int addGroupUnreadNum = 0;

    //未读加好友消息个数
    private volatile int totalUnreadFriendsNum = 0;

    //未读加好友消息个数
    public void incTotalUnreadFriendsNum() {
        totalUnreadFriendsNum++;
    }

    public void decTotalUnreadFriendsNum() {
        totalUnreadFriendsNum--;
    }

    public int getTotalUnreadFriendsNum() {
        return totalUnreadFriendsNum < 0 ? 0:totalUnreadFriendsNum;
    }

    public void clearUnreadFriendsNum(){
        totalUnreadFriendsNum = 0;
    }

    //未读加群消息+1
    public void incAddGroupUnreadNum() {
        addGroupUnreadNum++;
    }

    public int getAddGroupUnreadNum() {
        return addGroupUnreadNum < 0 ? 0 :addGroupUnreadNum;
    }

    //未读加群消息-1
    public void decAddGroupUnreadNum() {
        addGroupUnreadNum--;
    }

    //未读加群消息置零
    public void initAddGroupUnreadNum() {
        addGroupUnreadNum = 0;
    }

    //未读加群消息减少
    public void decAddGroupUnreadNum(int count) {
        addGroupUnreadNum = addGroupUnreadNum - count;
    }

    protected void localReload() throws Exception{
        totalUnreadFriendsNum = 0;

        final AddFriendDao dao = getEntityDao();
        dao.findAll(getSqlAddtion(), new IBeanWorker<AddFriendEntity>() {
            @Override
            public void doWork(AddFriendEntity entity) throws Exception{
                try {
                    add(entity);
                    if(ApiEnum.ReadTypeEnum.UNREAD.value == entity.getIsRead()){
                        if(UtilPub.isNotEmpty(entity.getGoupId())){
                            incAddGroupUnreadNum();
                        }else {
                            incTotalUnreadFriendsNum();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }
}
