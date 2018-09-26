package com.nim.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.nim.R;

import java.util.Random;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public class GuessAttachment extends CustomAttachment {

    public enum Guess {
        Shitou(1, "石头", R.drawable.shitou),
        Jiandao(2, "剪刀", R.drawable.jiandao),
        Bu(3, "布", R.drawable.bu),;

        private int value;
        private String desc;
        private int resId;

        Guess(int value, String desc, int resId) {
            this.value = value;
            this.desc = desc;
            this.resId = resId;
        }

        static Guess enumOfValue(int value) {
            for (Guess direction : values()) {
                if (direction.getValue() == value) {
                    return direction;
                }
            }
            return Shitou;
        }

        public int getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }

        public int getResId() {
            return resId;
        }
    }

    private Guess value;

    public GuessAttachment() {
        super(CustomAttachmentType.Guess);
        random();
    }

    @Override
    protected void parseData(JSONObject data) {
        value = Guess.enumOfValue(data.getIntValue("value"));
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("value", value.getValue());
        return data;
    }

    private void random() {
        int value = new Random().nextInt(3) + 1;
        this.value = Guess.enumOfValue(value);
    }

    public Guess getValue() {
        return value;
    }


}
