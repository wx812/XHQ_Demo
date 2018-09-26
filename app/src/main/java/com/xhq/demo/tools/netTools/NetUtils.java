package com.xhq.demo.tools.netTools;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;

import com.xhq.demo.tools.ShellUtils;
import com.xhq.demo.tools.StringUtils;
import com.xhq.demo.tools.appTools.AppUtils;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/8/2
 *     desc  : 网络相关工具类
 * </pre>
 */
public class NetUtils{

    public static final int NET_TYPE_NO = -1;
    public static final int NET_TYPE_WIFI = 0;
    public static final int NET_TYPE_4G = 1;
    public static final int NET_TYPE_3G = 2;
    public static final int NET_TYPE_2G = 3;
    public static final int NET_TYPE_UNKNOWN = 4;


    public static ConnectivityManager getConnMgr(){
        Context ctx = AppUtils.getAppContext();
        return (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
    }


    public static TelephonyManager getTelMgr(){
        Context ctx = AppUtils.getAppContext();
        // 返回唯一的用户ID;就是这张卡的编号
        return (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
    }


    /**
     * get activate network information
     *
     * @return NetworkInfo
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static NetworkInfo getActiveNetworkInfo(){
        return getConnMgr().getActiveNetworkInfo();
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isWifiConnected(){
        return getActiveNetworkType() == NET_TYPE_WIFI;
    }

    /**
     * is it a mobile data connected
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isMobileDataConnected() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isAvailable() && info.getType() == ConnectivityManager.TYPE_MOBILE;
    }


    /**
     * 判断网络是否是4G
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean is4GConnected(){
        return getActiveNetworkType() == NET_TYPE_4G;
    }


    /**
     * 判断移动数据是否打开
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isDataEnabled(){
        try{
            TelephonyManager tm = getTelMgr();
            Method dataEnabled = tm.getClass().getDeclaredMethod("getDataEnabled");
            if(null != dataEnabled){
                return (boolean)dataEnabled.invoke(tm);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 打开或关闭移动数据
     *
     * @param enabled {@code true}: 打开<br>{@code false}: 关闭
     */
    @RequiresPermission(Manifest.permission.MODIFY_PHONE_STATE)
    public static void setDataEnable(boolean enabled){
        try{
            TelephonyManager tm = getTelMgr();
            Method dataEnabled = tm.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            if(null != dataEnabled){
                dataEnabled.invoke(tm, enabled);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    /**
     * 判断网络是否可用
     */
    @RequiresPermission(Manifest.permission.INTERNET)
    public static boolean isAvailableByPing(){
        final String cmd = "ping -c 1 -w 1 223.5.5.5";
        ShellUtils.CommandResult result = ShellUtils.execCmd(cmd, false);
//        if(result.errorMsg != null){
//            Log.d("isAvailableByPing", result.errorMsg);
//        }
        return result.result == 0;
    }


    /**
     * 判断网络是否连接
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isConnected(){
        NetworkInfo info = getActiveNetworkInfo();
        boolean netState = info.getState() == NetworkInfo.State.CONNECTED;
        return info.isConnected() && netState;
    }


    /**
     * 检测网络是否连接, 获取所有的网络信息, 检查已经连接的网络类型
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isNetConnected() {
        ConnectivityManager cm = getConnMgr();
        if (cm != null) {
            NetworkInfo[] infos = cm.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo ni : infos) {
                    if (ni.isConnected()) return true;
                }
            }
        }
        return false;
    }


    /**
     * 判断以太网 是否连接
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isConnectedEth(){
        ConnectivityManager connMgr = getConnMgr();
        NetworkInfo.State mEthState = connMgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET).getState();
        // State mPppState = connManager.getNetworkInfo(ConnectivityManager.TYPE_PPPOE).getState();
        /* || State.CONNECTED == mPppState */
        return NetworkInfo.State.CONNECTED == mEthState;
    }


    /**
     * 判断网址是否有效
     */
    public static boolean isValidUrl(String link) {
        String regex = "^(http://|https://)?((?:[A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\\.)+([A-Za-z]+)" +
                "[/\\?\\:]?.*$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(link);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }


    /**
     * 获取网络运营商名称
     * <p>中国移动、如中国联通、中国电信</p>
     *
     * @return 运营商名称
     */
    public static String getOperatorName(){
        TelephonyManager tm = getTelMgr();
        return tm != null ? tm.getNetworkOperatorName() : null;
    }


    /**
     * 获取运营商名称
     * IMSI号前面3位460是国家，紧接着后面2位00. 01是中国联通，02是中国移动，03是中国电信。
     *
     * @return operator name
     */
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String getOperatorByIMSI() {
        String providersName = null;
        TelephonyManager tm = getTelMgr();
        @SuppressLint("HardwareIds") String IMSI = tm.getSubscriberId();
        if (StringUtils.isNullOrEmpty(IMSI)) return "未知";
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            providersName = "中国移动";
        } else if (IMSI.startsWith("46001")) {
            providersName = "中国联通";
        } else if (IMSI.startsWith("46003")) {
            providersName = "中国电信";
        }
        return providersName;
    }


    /**
     * @return network type
     * <ul>
     * <li>{@link #NET_TYPE_WIFI   } </li>
     * <li>{@link #NET_TYPE_4G     } </li>
     * <li>{@link #NET_TYPE_3G     } </li>
     * <li>{@link #NET_TYPE_2G     } </li>
     * <li>{@link #NET_TYPE_UNKNOWN} </li>
     * <li>{@link #NET_TYPE_NO     } </li>
     * </ul>
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static int getActiveNetworkType(){
        int netType = NET_TYPE_NO;
        NetworkInfo networkInfo = getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isAvailable()){
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                netType = NET_TYPE_WIFI;
            }else if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){

                final int NETWORK_TYPE_GSM = 16;
                final int NETWORK_TYPE_TD_SCDMA = 17;
                final int NETWORK_TYPE_IWLAN = 18;

                switch(networkInfo.getSubtype()){

                    case NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        netType = NET_TYPE_2G;
                        break;

                    case NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        netType = NET_TYPE_3G;
                        break;

                    case NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        netType = NET_TYPE_4G;
                        break;
                    default:
                        String subtypeName = networkInfo.getSubtypeName();
                        if(subtypeName.equalsIgnoreCase("TD-SCDMA")
                                || subtypeName.equalsIgnoreCase("WCDMA")
                                || subtypeName.equalsIgnoreCase("CDMA2000")){
                            netType = NET_TYPE_3G;
                        }else{
                            netType = NET_TYPE_UNKNOWN;
                        }
                        break;
                }
            }else netType = NET_TYPE_UNKNOWN;
        }
        return netType;
    }


}