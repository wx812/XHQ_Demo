package com.xhq.demo.tools;

import android.content.res.Resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xhq.demo.constant.apiconfig.ApiEnum;
import com.xhq.demo.constant.apiconfig.ApiKey;
import com.xhq.demo.db.DBService;
import com.xhq.demo.tools.appTools.AppUtils;
import com.xhq.demo.tools.dateTimeTools.DateTimeUtils;
import com.xhq.demo.tools.encodeTools.EncryptUtils;
import com.xhq.demo.tools.fileTools.FileOperateUtils;
import com.xhq.demo.tools.spTools.SPKey;
import com.xhq.demo.tools.spTools.SPUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;


/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/8/8.
 *     Desc  : Map Unique Tools.
 *     Updt  : Description.
 * </pre>
 */
public class MapUtils{


    public static void reStoreParameters(){
        Properties props;
        props = FileOperateUtils.getProperties("appConfig.properties");
//        Transfer.mCity = props.getProperty("city");
//        Transfer.mUserName = props.getProperty("userName");
//        Transfer.mNickName = props.getProperty("nickName");
//        Transfer.mToken = props.getProperty("token");
//        Transfer.isTokenavailable =Integer.parseInt(props.getProperty("isTokenAvailable"));
//
//        Transfer.mWidth = Integer.parseInt(props.getProperty("width"));
//        Transfer.mHight = Integer.parseInt(props.getProperty("height"));
//        Transfer.mMinilen=Transfer.mWidth/60;
        DBService labelDB = new DBService();
        Resources res = AppUtils.getResources();
//        Transfer.mDefaultHead = BitmapFactory.decodeResource(res, R.drawable.bg_login_avatar);
    }



    public static void saveParameters(int tokenAvailable){
        Properties properties;
        OutputStream os;
        properties = FileOperateUtils.getProperties("appConfig.properties");
        os = FileOperateUtils.openPropsFile("appConfig.properties");
        if(properties != null && os != null){
//            properties.setProperty("userName", Transfer.mUserName);
//            properties.setProperty("nickName", Transfer.mNickName);
//            properties.setProperty("token", Transfer.mToken);
            properties.setProperty("isTokenAvailable", "" + tokenAvailable);
            try{
                properties.store(os, null);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * set necessary field of the general request
     *
     * @param mapData param container
     * @param port Transaction code
     * @return param container
     */
    public static Map<String, String> setNecessaryField(Map<String, String> mapData, String port){
        mapData.put(ApiKey.CommonUrlKey.sk, SPUtils.get(SPKey.USER_CONFIG, ApiKey.CommonUrlKey.sk, ""));// 登录令牌
        mapData.put(ApiKey.CommonUrlKey._sk,
                    EncryptUtils.decryptPerson(SPUtils.get(SPKey.USER_CONFIG, ApiKey.CommonUrlKey.sk, "")));
        mapData.put(ApiKey.CommonUrlKey.mtype, port); // 请求类型
        mapData.put(ApiKey.CommonUrlKey.time, DateTimeUtils.getCurrDateTime("yyyyMMddHHmmss")); // 请求的时间
        mapData.put(ApiKey.CommonUrlKey.ct, "3"); // 客户端类型 app 3
        return mapData;
    }


    /**
     * set necessary field of the general request and opt_user_id
     *
     * @param mapData param container
     * @param port Transaction code
     * @return param container
     */
    public static Map<String, String> setNecessaryFieldAddUID(Map<String, String> mapData, String port){
        mapData.put(ApiKey.CommonUrlKey.sk, SPUtils.get(SPKey.USER_CONFIG, ApiKey.CommonUrlKey.sk, ""));// 登录令牌
        mapData.put(ApiKey.CommonUrlKey._sk,
                    EncryptUtils.decryptPerson(SPUtils.get(SPKey.USER_CONFIG, ApiKey.CommonUrlKey.sk, "")));
        mapData.put(ApiKey.CommonUrlKey.OPT_USER_ID, SPUtils.get(SPKey.USER_CONFIG, SPKey.UID, "")); //USER_ID
        mapData.put(ApiKey.CommonUrlKey.mtype, port); // 请求类型
        mapData.put(ApiKey.CommonUrlKey.time, DateTimeUtils.getCurrDateTime("yyyyMMddHHmmss")); // 请求的时间
        mapData.put(ApiKey.CommonUrlKey.ct, "3"); // 客户端类型 app 3
        return mapData;
    }


    /**
     * set necessary field of the upload file
     *
     * @param mapFileData param container
     * @param filePath upload file path
     * @return param container
     */
    public static Map<String, String> setFileNecessaryField(Map<String, String> mapFileData,
                                                            String filePath, String fileType){
        mapFileData.put(ApiKey.CommonUrlKey.sk, SPUtils.get(SPKey.USER_CONFIG, ApiKey.CommonUrlKey.sk, ""));
        mapFileData.put(ApiKey.CommonUrlKey._sk, SPUtils.get(SPKey.USER_CONFIG, ApiKey.CommonUrlKey.sk, ""));
        mapFileData.put(ApiKey.CommonUrlKey.ownerId, SPUtils.get(SPKey.USER_CONFIG, SPKey.UID, ""));
        mapFileData.put(ApiKey.CommonUrlKey.attachId, "");
        mapFileData.put(ApiKey.UpImgKey.path, filePath);
        mapFileData.put(ApiKey.UpImgKey.type, fileType);
        return mapFileData;
    }


    /**
     * set encrypt data of the mapData
     *
     * @param mBuilder Gson Builder
     * @param mapData param container
     * @return param container
     */
    public static Map<String, String> setEncryptData(GsonBuilder mBuilder, Map<String, String> mapData){
        Gson gson = mBuilder.create();
        String jsonStr = gson.toJson(mapData);
        mapData.clear();
        mapData.put(ApiKey.CommonUrlKey.data, EncryptUtils.encryptPerson(jsonStr));
        mapData.put(ApiKey.CommonUrlKey.sc, EncryptUtils.getSecurityCode(EncryptUtils.encryptPerson(jsonStr)));
        return mapData;
    }


    /**
     * card type
     *
     * @param cardType card type
     * @return After conversion card type
     */
    public static String getCardType(String cardType){
        String card_Type;

        card_Type = ApiEnum.CardType.getNameByValue(cardType);

        return card_Type;
    }


    /**
     * card status conversion of number to string
     *
     * @param cardStatus card status
     * @return After conversion card type
     */
    public static String getCardStatus(String cardStatus){
        String card_Status;
//        Integer cardStatusNum = Integer.valueOf(cardStatus);
//        Integer.parseInt(cardStatus);

        card_Status = ApiEnum.CardStatus.getNameByValue(cardStatus);


        /*
        switch(cardStatus){
            case ConstantValue.DB_DQS:
                card_Status = ApiEnum.CardStatus.DB_DQS.name;
                break;
            case ConstantValue.CARD_WAIT:
                card_Status = ApiEnum.CardStatus.CARD_WAIT.name;
                break;
            case ConstantValue.GET_CARD_FAIL:
                card_Status = ApiEnum.CardStatus.GET_CARD_FAIL.name;
                break;
            case ConstantValue.ALREADY_CARD:
                card_Status = ApiEnum.CardStatus.ALREADY_CARD.name;
                break;
            case ConstantValue.DEFAULT:
                card_Status = ApiEnum.CardStatus.DEFAULT.name;
                break;
            case ConstantValue.UNDOING:
                card_Status = ApiEnum.CardStatus.UNDOING.name;
                break;
            case ConstantValue.ALREADY_UNDO:
                card_Status = ApiEnum.CardStatus.ALREADY_UNDO.name;
                break;
            case ConstantValue.LOSTING:
                card_Status = ApiEnum.CardStatus.LOSTING.name;
                break;
            case ConstantValue.MAKE:
                card_Status = ApiEnum.CardStatus.MAKE.name;
                break;
            case ConstantValue.CANCEL_UNDO:
                card_Status = ApiEnum.CardStatus.CANCEL_UNDO.name;
                break;
            case ConstantValue.CANCEL_LOSS:
                card_Status = ApiEnum.CardStatus.CANCEL_LOSS.name;
                break;
            case ConstantValue.CANCEL_MAKE:
                card_Status = ApiEnum.CardStatus.CANCEL_MAKE.name;
                break;
            case ConstantValue.ACCEPTED_MAKE:
                card_Status = ApiEnum.CardStatus.ACCEPTED_MAKE.name;
                break;
        }
        */
        return card_Status;
    }
}
