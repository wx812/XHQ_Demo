package com.nim.redpacket.bean;

import java.io.Serializable;

/**
 * Administrator
 * 2018/1/24
 * 14:01
 */
public class EnvelopeRpBean implements Serializable {
    private String envelopesID;
    private String envelopeMessage;
    private String envelopeName;

    public EnvelopeRpBean() {

    }

    public String getEnvelopesID() {
        return envelopesID;
    }

    public void setEnvelopesID(String envelopesID) {
        this.envelopesID = envelopesID;
    }

    public String getEnvelopeMessage() {
        return envelopeMessage;
    }

    public void setEnvelopeMessage(String envelopeMessage) {
        this.envelopeMessage = envelopeMessage;
    }

    public String getEnvelopeName() {
        return envelopeName;
    }

    public void setEnvelopeName(String envelopeName) {
        this.envelopeName = envelopeName;
    }

}
