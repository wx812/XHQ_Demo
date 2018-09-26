package com.xhq.commonbase.share.platform;

import android.view.View;

import com.alipay.share.sdk.openapi.APAPIFactory;
import com.alipay.share.sdk.openapi.APMediaMessage;
import com.alipay.share.sdk.openapi.APWebPageObject;
import com.alipay.share.sdk.openapi.IAPApi;
import com.alipay.share.sdk.openapi.SendMessageToZFB;
import com.common.R;
import com.common.utils.share.base.BaseShareArgs;
import com.common.utils.share.base.BaseSharePlatform;

/**
 * Created by ychen on 2017/12/11.
 */

public class ShareAlipay extends BaseSharePlatform<BaseShareArgs>{

    @Override
    public String getName() {
        return "支付宝";
    }

    @Override
    public int getPicResId() {
        return R.drawable.icon_zhifubao;
    }

    @Override
    public void dealClick(BaseShareArgs args,View view) {
        IAPApi api = APAPIFactory.createZFBApi(args.activity, "2016122904705497", false);
        APWebPageObject webPageObject = new APWebPageObject();
        webPageObject.webpageUrl = args.targetUrl;
        ////初始化APMediaMessage ，组装分享消息对象
        APMediaMessage webMessage = new APMediaMessage();
        webMessage.mediaObject = webPageObject;
        webMessage.title = args.title;
        webMessage.description = args.message;
        webMessage.thumbUrl = args.imageUrl;
        SendMessageToZFB.Req webReq = new SendMessageToZFB.Req();
        webReq.message = webMessage;
        webReq.transaction = "WebShare" + String.valueOf(System.currentTimeMillis());
        api.sendReq(webReq);
    }
}
