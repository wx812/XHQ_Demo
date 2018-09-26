package com.xhq.demo.cmd.buffer;

import com.xhq.demo.cmd.buffer.entityBuffer.AttachBuffer;
import com.xhq.demo.cmd.buffer.entityBuffer.CatalogBuffer;
import com.xhq.demo.cmd.buffer.entityBuffer.GroupBuffer;
import com.xhq.demo.cmd.buffer.entityBuffer.GroupMemberBuffer;
import com.xhq.demo.cmd.buffer.entityBuffer.UserBaseBuffer;

/**
 * Created by zyj on 2017/4/13.
 * 注册entity缓存
 */

public class BufferFactory {
    public static void init(){
        AttachBuffer.getInstance();
        CatalogBuffer.getInstance();
        GroupBuffer.getInstance();
        GroupMemberBuffer.getInstance();
        UserBaseBuffer.getInstance();
    }
}
