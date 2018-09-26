package com.nim.session.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * Administrator
 * 2017/9/18
 * 14:15
 */
public class TransferAccountAttachment extends CustomAttachment {
    private String amount;
    private String id;
    private String title;// 名称
    private String name;
    private String introduce;
    private int transferType;//转账类型 1普通转账  2群转账

    private static final String KEY_ACCOUNT = "amount";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_NAME = "name";
    private static final String KEY_INTRODUCE = "introduce";
    private static final String KEY_TRANSFER_TYPE = "transferType";

    public TransferAccountAttachment() {
        super(CustomAttachmentType.TransferAccounts);
    }

    @Override
    protected void parseData(JSONObject data) {
        amount = data.getString(KEY_ACCOUNT);
        id = data.getString(KEY_ID);
        title = data.getString(KEY_TITLE);
        name = data.getString(KEY_NAME);
        introduce = data.getString(KEY_INTRODUCE);
        transferType = data.getIntValue(KEY_TRANSFER_TYPE);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_ACCOUNT, amount);
        data.put(KEY_ID, id);
        data.put(KEY_TITLE, title);
        data.put(KEY_NAME, name);
        data.put(KEY_INTRODUCE, introduce);
        data.put(KEY_TRANSFER_TYPE, transferType);
        return data;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getTransferType() {
        return transferType;
    }

    public void setTransferType(int transferType) {
        this.transferType = transferType;
    }
}
