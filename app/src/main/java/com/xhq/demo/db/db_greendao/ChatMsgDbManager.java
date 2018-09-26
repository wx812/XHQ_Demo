package com.xhq.demo.db.db_greendao;


import com.xhq.demo.db.db_greendao.entry.ChatMessageBean;

import org.greenrobot.greendao.AbstractDao;


public class ChatMsgDbManager extends BaseManager<ChatMessageBean,String> {
    @Override
    public AbstractDao<ChatMessageBean, String> getAbstractDao() {
        return daoSession.getChatMessageBeanDao();
    }
}
