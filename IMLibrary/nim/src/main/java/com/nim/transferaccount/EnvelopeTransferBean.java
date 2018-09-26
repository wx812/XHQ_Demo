package com.nim.transferaccount;

import java.io.Serializable;

/**
 * Administrator
 * 2018/2/1
 * 14:58
 */
public class EnvelopeTransferBean implements Serializable {
    private String transferId;
    private String transferMessage;
    private String transferAmount;
    private int transferType;

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public String getTransferMessage() {
        return transferMessage;
    }

    public void setTransferMessage(String transferMessage) {
        this.transferMessage = transferMessage;
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public int getTransferType() {
        return transferType;
    }

    public void setTransferType(int transferType) {
        this.transferType = transferType;
    }
}
