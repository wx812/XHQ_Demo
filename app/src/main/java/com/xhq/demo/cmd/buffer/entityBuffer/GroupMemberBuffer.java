package com.xhq.demo.cmd.buffer.entityBuffer;

import com.xhq.demo.cmd.buffer.AbsEntityBuffer;
import com.xhq.demo.db.db_sql.entity.GroupMemberDao;
import com.xhq.demo.db.db_sql.entity.GroupMemberEntity;
import com.xhq.demo.tools.StringUtils;
import com.xhq.demo.tools.choppedTools.UtilPub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zyj on 2017/4/12.
 * 群成员缓存
 */

public class GroupMemberBuffer extends AbsEntityBuffer<GroupMemberEntity> {
    @Override
    protected GroupMemberDao getEntityDao() {
        return new GroupMemberDao();
    }

    private static GroupMemberBuffer INSTANCE = null;

    private GroupMemberBuffer() {
    }

    public static GroupMemberBuffer getInstance() {
        if (INSTANCE == null) {
            synchronized (GroupMemberBuffer.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GroupMemberBuffer();
//                    inst.init();
//                    INSTANCE = inst;
                }
            }
        }
        return INSTANCE;
    }

    public List<GroupMemberEntity> getGroupMember(String gid) {
        if(UtilPub.isEmpty(gid)) return null;
        List<GroupMemberEntity> list = new ArrayList<>();
        for (GroupMemberEntity gm: mapAll.values()) {
            if (gid.equals(gm.getGoupId())) {
                list.add(gm);
            }
        }
        Collections.sort(list, new Comparator<GroupMemberEntity>() {
            @Override
            public int compare(GroupMemberEntity o1, GroupMemberEntity o2) {
                int v = (o2.getGroupMemberType())-o1.getGroupMemberType();//先比较身份
                if (v != 0) return v;
                else{
                    int v1 = o1.getMemberName().compareTo(o2.getMemberName());
                    if (v1 != 0) return v1;
                    return StringUtils.chineseCompare(o1.getMemberName(), o2.getMemberName());
                }
            }
        });
        return list;
    }

}
