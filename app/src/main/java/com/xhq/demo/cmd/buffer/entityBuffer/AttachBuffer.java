package com.xhq.demo.cmd.buffer.entityBuffer;

import com.xhq.demo.cmd.buffer.AbsEntityBuffer;
import com.xhq.demo.db.db_sql.entity.AttachDao;
import com.xhq.demo.db.db_sql.entity.AttachEntity;

/**
 * Created by zyj on 2017/4/12.
 * 附件表缓存
 */

public class AttachBuffer extends AbsEntityBuffer<AttachEntity> {
    @Override
    protected AttachDao getEntityDao() {
        return new AttachDao();
    }

    private static AttachBuffer INSTANCE = null;

    private AttachBuffer() {
    }

    public static AttachBuffer getInstance() {
        if (INSTANCE == null) {
            synchronized (AttachBuffer.class) {
                if (INSTANCE == null) {
                    AttachBuffer inst = new AttachBuffer();
//                    inst.init();
                    INSTANCE = inst;
                }
            }
        }
        return INSTANCE;
    }
}
