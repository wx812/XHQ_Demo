package com.xhq.demo.db.db_greendao;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mao Jiqing on 2016/10/15.
 */

public class ChatConst{
    //发送中
    public static final int SENDING = 0;
    //发送成功
    public static final int COMPLETED = 1;
    //发送失败
    public static final int SENDERROR = 2;

    @IntDef({SENDING, COMPLETED, SENDERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SendState {
    }
}
