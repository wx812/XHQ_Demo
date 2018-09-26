package com.xhq.demo.cmd.buffer.entityBuffer;

import com.xhq.demo.cmd.buffer.AbsEntityBuffer;
import com.xhq.demo.db.db_sql.entity.GroupDao;
import com.xhq.demo.db.db_sql.entity.GroupEntity;

/**
 * Created by zyj on 2017/4/12.
 * 群缓存
 */

public class GroupBuffer extends AbsEntityBuffer<GroupEntity> {
    @Override
    protected GroupDao getEntityDao() {
        return new GroupDao();
    }

    private static GroupBuffer INSTANCE = null;

    private GroupBuffer() {
    }

    public static GroupBuffer getInstance() {
        if (INSTANCE == null) {
            synchronized (AttachBuffer.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GroupBuffer();
//                    inst.init();
//                    INSTANCE = inst;
                }
            }
        }
        return INSTANCE;
    }
}
