package com.nim.transferaccount;

import android.content.Intent;

/**
 * Administrator
 * 2018/2/1
 * 15:02
 */
public class TransferClient {

    public static EnvelopeTransferBean getEnvelopeTransferInfo(Intent intent) {
        if (intent == null) {
            return null;
        } else {
            EnvelopeTransferBean bean = new EnvelopeTransferBean();
            bean.setTransferId(intent.getStringExtra("transferId"));
            bean.setTransferMessage(intent.getStringExtra("transferMessage"));
            bean.setTransferAmount(intent.getStringExtra("transferAmount"));
            bean.setTransferType(intent.getIntExtra("transferType", 0));
            return bean;
        }
    }
}
