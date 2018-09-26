package com.xhq.demo.cmd.buffer.entityBuffer;


import com.xhq.demo.cmd.buffer.AbsEntityBuffer;
import com.xhq.demo.db.db_sql.entity.UserBaseDao;
import com.xhq.demo.db.db_sql.entity.UserBaseEntity;
import com.xhq.demo.tools.StringUtils;
import com.xhq.demo.tools.choppedTools.UtilPub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zyj on 2017/4/12.
 * 用户缓存
 */

public class UserBaseBuffer extends AbsEntityBuffer<UserBaseEntity>{
    @Override
    protected UserBaseDao getEntityDao() {
        return new UserBaseDao();
    }

    private static UserBaseBuffer INSTANCE = null;

    private UserBaseBuffer() {
    }

    public static UserBaseBuffer getInstance() {
        if (INSTANCE == null) {
            synchronized (GroupMemberBuffer.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserBaseBuffer();
//                    inst.init();
//                    INSTANCE = inst;
                }
            }
        }
        return INSTANCE;
    }

    public List<UserBaseEntity> getFriends() {
        List<UserBaseEntity> list = new ArrayList<>();
        for (UserBaseEntity user: mapAll.values()) {
            if (UtilPub.isNotEmptyId(user.getCatalogId())) {
                list.add(user);
            }
        }
        Collections.sort(list, new Comparator<UserBaseEntity>() {
            @Override
            public int compare(UserBaseEntity o1, UserBaseEntity o2) {
                int v = (o2.getUserStatu())-o1.getUserStatu();
                if (v != 0) return v;
                else{
                    int v1 = o1.getCatalogId().compareTo(o2.getCatalogId());
                    if (v1 != 0) return v1;
                    return StringUtils.chineseCompare(o1.getNickName(), o2.getNickName());
                }
            }
        });
        return list;
    }
}
