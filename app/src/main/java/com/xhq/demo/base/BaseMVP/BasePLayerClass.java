package com.xhq.demo.base.BaseMVP;

import android.content.Context;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/11/6.
 *     Desc  : Description.
 *     Updt  : Description.
 * </pre>
 */
public abstract class BasePLayerClass implements ProgressInterf{
    //    private ICardCivilInfoConfirmView mViewLayerInterf;
//    private ICardCivilInfoConfirmModel mModelLayerInterf;

    public BasePLayerClass(){}

    public void sendData2Server( HashMap<String, String> viewMapData){
    }

    public void sendData2Server(Context Context, HashMap<String, String> viewMapData){
    }


    public void receiveData2View(String type, Map<String, String> mapResult){
    }


    public void uploadPicHttpRequest(Context context, String picType, String imageUrl){
    }

    public void uploadPicHttpResult(String type, File mFile_small){
    }


    /**
     * handle server json data to object
     *
     * @param jsonStr
     * @param dataBean
     */
    public abstract void handleJsonData(String jsonStr, Map<String, Object> dataBean);

    public abstract void handleJsonData(String jsonStr);


    /**
     * transport to view layer
     *
     * @param str
     */
    public void transport2View(String str){

    }
}
