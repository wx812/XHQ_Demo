package com.xhq.demo.tools.appTools;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings.Secure;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.xhq.demo.tools.CloseUtils;
import com.xhq.demo.tools.ShellUtils;
import com.xhq.demo.tools.netTools.NetUtils;
import com.xhq.demo.tools.netTools.WiFiUtils;
import com.xhq.demo.tools.uiTools.screen.ScreenUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.net.NetworkInterface;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.xhq.demo.tools.appTools.AppUtils.getAppContext;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2016/6/11.
 *     Desc  : device tool.
 *     Updt  : 2018/4/10.
 * </pre>
 */
public class DeviceUtils{


    public static TelephonyManager getTeleMgr(){
        Context ctx = AppUtils.getAppContext();
        // 返回唯一的用户ID;就是这张卡的编号
        return (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * @return get device id
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String getDeviceId(){
        Context ctx = AppUtils.getAppContext();
        String deviceId = null;
        TelephonyManager tm = getTeleMgr();
        if(tm != null) deviceId = tm.getDeviceId();
        if(deviceId != null) return deviceId;
        deviceId = Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
        if(deviceId != null) return deviceId;
        deviceId = AppUtils.getUUID();
        return deviceId;
    }


    /**
     * @return 设备型号 meizu_mx2
     */
    public static String getDeviceName(){
        return Build.PRODUCT;
    }


    public static int getSDK_VCode(){
        return Build.VERSION.SDK_INT;
    }


    /**
     * @return Android System version 4.2.1
     */
    public static String getAndroid_V(){
        return Build.VERSION.RELEASE;
    }


    /**
     * @return 设备厂商 eg: xiaomi
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }


    /**
     * @return 手机品牌 eg: hongmi
     */
    public static String getBrand(){
        return Build.BRAND;
    }


    /**
     * @return Flyme OS 3.4.1 (A17506)
     */
    public static String getBrand_V(){
        return Build.DISPLAY;
    }


    /**
     * cpu信息 armeabi-v7a
     * <br> This field was deprecated in API level 21
     * <br> Use {@link Build#SUPPORTED_ABIS} instead.
     * <br> see {@link #getMulticoreCPU()}
     */
    @Deprecated
    public static String getCPU(){
        return Build.CPU_ABI;
    }


    /**
     * 设备支持的ABI列表, 第一个时最优选的ABI
     * @return Multicore CPU
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static String[] getMulticoreCPU(){
        return Build.SUPPORTED_ABIS;
    }


    /**
     *
     * @return 设备型号 eg MI2S：小米2s标准版（联通，移动）, MI2SC：小米2s电信机. m3 note : 魅蓝note3
     */
    public static String getModel(){
        String model = Build.MODEL;
        if (model != null) model = model.trim().replaceAll("\\s*", "");
        return model;
    }


    public static String getLanguageDef(){
        Locale locale = Locale.getDefault();
        return locale.getLanguage();
    }


    // 系统语言
    public static String getLocalName(){
        Locale locale = Locale.getDefault();
        return locale.getDisplayName();
    }


    public static String getLocaleCountry(){
        Locale locale = Locale.getDefault();
        return locale.getCountry();
    }


    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param ctx
     * @return true 表示开启
     */
    public static boolean isGpsOPen(final Context ctx) {
        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param ctx
     * @return true 表示开启
     */
    public static boolean isEnableLocale(final Context ctx) {
        LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;
    }


    /**
     * @return SIM 卡是否可用
     */
    public static boolean isSIMAvailable(){
        TelephonyManager tm = NetUtils.getTelMgr();
        return null != tm && tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }


    /**
     * 获取本地app版本号
     */
    public static String getLocalAppVersion(){
        PackageManager pm = AppUtils.getPkgMgr();
        String version = null;
        try{
            version = pm.getPackageInfo("com.gsie.statistic", 0).versionName;
        }catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return version;
    }


    /**
     * @return time zone
     */
    public static String getTimeZone(){
        String offset;
        Calendar cal = Calendar.getInstance();
        int timezone = cal.getTimeZone().getRawOffset() / 3600000;
        String top = "";
        if(Math.abs(timezone) < 10) top = "0";
        String fen = String.valueOf(cal.getTimeZone().getRawOffset() % 3600000 / 60000);
        if(2 > fen.length()){
            fen = "0" + fen;
        }else{
            fen = fen.substring(0, 2);
        }
        if(timezone >= 0){
            offset = "+" + top + timezone + ":" + fen;
        }else{
            offset = "-" + top + Math.abs(timezone) + ":" + fen;
        }
        return offset;
    }


    @SuppressLint("HardwareIds")
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String getIMSI(){
        TelephonyManager tm = NetUtils.getTelMgr();
        if(null != tm) return tm.getSubscriberId();
        return null;
    }


    /**
     * @return is there root authority
     */
    public static boolean isRootAuthority(){
        Process process = null;
        DataOutputStream os = null;
        try{
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            return exitValue == 0;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }finally{
            CloseUtils.closeIO(os);
            if(process != null) process.destroy();
        }
    }


    /**
     * 震动
     * @param milliseconds 振动时长
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @RequiresPermission(Manifest.permission.VIBRATE)
    public static void vibrate(long milliseconds) {
         Context ctx = getAppContext();
         Vibrator vibrator = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
         VibrationEffect ve;
         ve = VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE);
         if(null != vibrator) vibrator.vibrate(ve);
    }


    /**
     * 指定手机以pattern模式振动
     *
     * @param pattern new long[]{400,800,1200,1600}，就是指定在400ms、800ms、1200ms、1600ms这些时间点交替启动、关闭手机振动器
     * @param repeat  指定pattern数组的索引，指定pattern数组中从repeat索引开始的振动进行循环。-1表示只振动一次，非-1表示从 pattern的指定下标开始重复振动。
     */
    @RequiresPermission(Manifest.permission.VIBRATE)
    public static void vibrate(long[] pattern, int repeat) {
        Vibrator vibrator = (Vibrator) getAppContext().getSystemService(Context.VIBRATOR_SERVICE);
        if(null != vibrator) vibrator.vibrate(pattern, repeat);
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    public static void cancelVibrate() {
        ((Vibrator) getAppContext().getSystemService(Context.VIBRATOR_SERVICE)).cancel();
    }


    /**
     * 判断设备是否root
     */
    public static boolean isDeviceRooted(){
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                "/system/bin/failsafe/","/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        for(String location : locations){
            if(new File(location + su).exists()){
                return true;
            }
        }
        return false;
    }


    /**
     * 关机
     * <p>需要root权限或者系统权限 {@code <android:sharedUserId="android.uid.system"/>}</p>
     */
    public static void shutdown() {
        ShellUtils.execCmd("reboot -p", true);
        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getAppContext().startActivity(intent);
    }


    /**
     * 重启
     * <p>需要root权限或者系统权限 {@code <android:sharedUserId="android.uid.system"/>}</p>
     */
    public static void reboot() {
        ShellUtils.execCmd("reboot", true);
        Intent intent = new Intent(Intent.ACTION_REBOOT);
        intent.putExtra("nowait", 1);
        intent.putExtra("interval", 1);
        intent.putExtra("window", 0);
        getAppContext().sendBroadcast(intent);
    }

    /**
     * 重启
     * @param reason  传递给内核来请求特殊的引导模式，如"recovery"
     */
    public static void reboot(String reason) {
        PowerManager pm = (PowerManager) getAppContext().getSystemService(Context.POWER_SERVICE);
        try {
            if(null != pm) pm.reboot(reason);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重启到recovery
     * <p>需要root权限</p>
     */
    public static void reboot2Recovery() {
        ShellUtils.execCmd("reboot recovery", true);
    }

    /**
     * 重启到bootloader
     * <p>需要root权限</p>
     */
    public static void reboot2Bootloader() {
        ShellUtils.execCmd("reboot bootloader", true);
    }


    /**
     * @return get phone mac address
     */
    @RequiresPermission(allOf = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE})
    public static String getMacAddress() {
        String macAddress = getMacAddressByWifiInfo();
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        macAddress = getMacAddressByNetworkInterface();
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        macAddress = getMacAddressByFile();
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        return null;
    }


    /**
     * @return get phone mac address
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
    public static String getMacAddressByWifiInfo(){
        String macAddress = "02:00:00:00:00:00";
        try{
            WifiManager wifiMgr = WiFiUtils.getWifiMgr();
            WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
            if(null != info){
                if(!TextUtils.isEmpty(info.getMacAddress())){
                    macAddress = info.getMacAddress().replace(":", "");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return macAddress;
        }
        return macAddress;
    }


    private static String getMacAddressByNetworkInterface() {
        try {
            List<NetworkInterface> nis = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : nis) {
                if (!ni.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = ni.getHardwareAddress();
                if (macBytes != null && macBytes.length > 0) {
                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02x:", b));
                    }
                    return res1.deleteCharAt(res1.length() - 1).toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    private static String getMacAddressByFile() {
        final String cmdR = "getprop wifi.interface";
        ShellUtils.CommandResult result = ShellUtils.execCmd(cmdR, false);
        if (result.result == 0) {
            String name = result.successMsg;
            if (name != null) {
                final String cmd = "cat /sys/class/net/" + name + "/address";
                result = ShellUtils.execCmd(cmd, false);
                if (result.result == 0) {
                    if (result.successMsg != null) {
                        return result.successMsg;
                    }
                }
            }
        }
        return "02:00:00:00:00:00";
    }


    public static String getDeviceInfo(){
        String info = "<br/>";
        try{
            info += "<br/>应用版本 : " + getLocalAppVersion();
            info += "<br/>设备型号 : " + getDeviceName();
            info += "<br/>系统版本 : " + getAndroid_V();
            info += "<br/>系统API等级  : " + getSDK_VCode();
            info += "<br/>cpu信息 : " + getCPU();
            info += "<br/>版本号 : " + getBrand_V();
            info += "<br/>分辨率 : " + ScreenUtils.getScreenW() + " * " + ScreenUtils.getScreenH();
        }catch(Exception e){
            e.printStackTrace();
        }
        return info + "<br/><br/>";
    }


    /**
     * get information of the device<p>
     *     use reflection to collect device information. <br>
     *         includes various device information in the build class.<br>
     *     例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
     * </p>
     *
     * @return Device Information
     */
    public static Map<String, String> getDeviceInfo(Map<String, String> mExceptionMap){

        Field[] fields = Build.class.getDeclaredFields();
        for(Field field : fields){
            try{
                field.setAccessible(true);
                mExceptionMap.put(field.getName(), field.get("").toString());
            }catch(IllegalArgumentException | IllegalAccessException e){
                e.printStackTrace();
            }
        }
        return mExceptionMap;
    }


}
