package com.xhq.demo.tools.netTools;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.RequiresPermission;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.xhq.demo.tools.appTools.AppUtils.getAppContext;
import static com.xhq.demo.tools.netTools.NetUtils.getConnMgr;
import static com.xhq.demo.tools.netTools.NetUtils.isAvailableByPing;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/6/14.
 *     Desc  : Description.
 *     Updt  : Description.
 * </pre>
 */
public class WiFiUtils{

    @SuppressLint("WifiManagerLeak")
    public static WifiManager getWifiMgr(){
        Context ctx = getAppContext();
        return (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 打开网络设置界面
     * <p>3.0以下打开设置界面</p>
     */
    @SuppressLint("ObsoleteSdkInt")
    public static void openWirelessSettings(){
        Context ctx = getAppContext();
        if(android.os.Build.VERSION.SDK_INT > 10){
            ctx.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
                                      .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }else{
            ctx.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS)
                                      .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE})
    public int openWifi() {

        WifiManager wifiMgr = getWifiMgr();
        int state = wifiMgr.getWifiState();
        if (state == WifiManager.WIFI_STATE_DISABLED || state == WifiManager.WIFI_STATE_DISABLING
                || state == WifiManager.WIFI_STATE_UNKNOWN) {
            wifiMgr.setWifiEnabled(true);
        }

        if (!wifiMgr.isWifiEnabled()) wifiMgr.setWifiEnabled(true);
        return wifiMgr.getWifiState();
    }


    @RequiresPermission(allOf = {Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE})
    public static int closeWifi() {
        WifiManager wifiMgr = getWifiMgr();
        int state = wifiMgr.getWifiState();
        if (state == WifiManager.WIFI_STATE_ENABLED || state == WifiManager.WIFI_STATE_ENABLING) {
            wifiMgr.setWifiEnabled(false);
        }
        return wifiMgr.getWifiState();
    }


    @RequiresPermission(Manifest.permission.CHANGE_WIFI_STATE)
    public static void disableWifi(WifiConfiguration existingConfig) {
        getWifiMgr().disableNetwork(existingConfig.networkId);
    }


    @RequiresPermission(Manifest.permission.CHANGE_WIFI_STATE)
    public boolean startScan() {
        WifiManager wifiMgr = getWifiMgr();
        return wifiMgr.startScan();
    }

    public static int getWifiLevel(ScanResult result) {
        return result.level;
    }

    @RequiresPermission(Manifest.permission.CHANGE_WIFI_STATE)
    public static boolean disconnect() {
        return getWifiMgr().disconnect();
    }

    @RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
    public static WifiInfo getCurrentWifiInfo() {
        return getWifiMgr().getConnectionInfo();
    }

    @RequiresPermission(Manifest.permission.CHANGE_WIFI_STATE)
    public static boolean saveConfiguration() {
        return getWifiMgr().saveConfiguration();
    }

    @RequiresPermission(Manifest.permission.CHANGE_WIFI_STATE)
    public static void removeWifiConfig(WifiConfiguration existingConfig) {
        getWifiMgr().removeNetwork(existingConfig.networkId);
    }


    /**
     * 判断wifi是否打开
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    @RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
    public static boolean getWifiEnabled() {
        WifiManager wifiManager = getWifiMgr();
        return wifiManager.isWifiEnabled();
    }



    /**
     * 打开或关闭wifi
     *
     * @param enabled {@code true}: 打开<br>{@code false}: 关闭
     */
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE})
    public static void setWifiEnabled(boolean enabled){
        WifiManager wifiManager = getWifiMgr();
        if(wifiManager == null) throw new RuntimeException("get 'wifi_service' exception");
        if(enabled){
            if(!wifiManager.isWifiEnabled()){
                wifiManager.setWifiEnabled(true);
            }
        }else{
            if(wifiManager.isWifiEnabled()){
                wifiManager.setWifiEnabled(false);
            }
        }
    }


    @Deprecated
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isWifiConnected() {
        ConnectivityManager cm = getConnMgr();
        return cm != null && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    @Deprecated
    private static int getWiFiAPState() {
        WifiManager wifiMgr = getWifiMgr();
        int apState = 0;
        if (wifiMgr != null) {
//            apState = wifiMgr.getWifiApState();
//            LogDebug.d(TAG, "WIFI AP STATE:" + apState);
            return apState;
        }
//        apState = WifiManager.WIFI_AP_STATE_DISABLED;
        return 11;
    }


    /**
     * judgment whether a wifi connection status
     *
     * @return {@code true}: connected<br>{@code false}: not connected
     */
    @RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
    public static boolean isConnectedWifi(){
        WifiManager wifiManager = getWifiMgr();
        if(wifiManager != null && wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED){
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if(wifiInfo != null){
                boolean b1 = wifiInfo.getIpAddress() != 0;
                boolean b2 = wifiInfo.getSupplicantState() == SupplicantState.COMPLETED;
                return b1 && b2;
            }
        }
        return false;
    }

    /**
     * 判断wifi数据是否可用
     * @return {@code true}: 是<br>{@code false}: 否
     */
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.INTERNET})
    public static boolean isWifiAvailable() {
        return getWifiEnabled() && isAvailableByPing();
    }


    @RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
    public List<ScanResult> getWifiAccessPointList() {
        ArrayList<ScanResult> list = new ArrayList<>();
        ArrayList<ScanResult> new_list = new ArrayList<>();
        list.clear();
        new_list.clear();
        list = (ArrayList<ScanResult>) getWifiMgr().getScanResults();

        for (ScanResult result : list) {
            if (result.SSID == null || result.SSID.length() == 0
                    || result.capabilities.contains("[IBSS]")) {
                continue;
            }
            new_list.add(result);
        }
        // Collections.sort(new_list, new sortByLevel());
        return removeDuplicateWithOrder(new_list);
    }


    @Deprecated
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isNetworkEnable() {
        if (getWiFiAPState()== 12) {
            return false;
        }
        if (getWiFiAPState()== 13) {
            return true;
        }
        ConnectivityManager connMgr = getConnMgr();

        NetworkInfo info = connMgr.getNetworkInfo(1);

        if ((info != null) && (info.isConnected())) {
//            LogDebug.d(TAG, "isNetworkEnable 1");
            return true;
        }
        info = connMgr.getNetworkInfo(6);
        if (info == null) {
            info = connMgr.getNetworkInfo(9);
        }

        return (info != null) && (info.isConnected());
    }


//    /**
//     * Returns the WIFI IP Addresses, if any, taking into account IPv4 and IPv6
//     * style addresses.
//     *
//     * @param context
//     *            the application context
//     * @return the formatted and newline-separated IP addresses, or null if
//     *         none.
//     */
//    public static String getWifiIpAddresses(Context context) {
//        ConnectivityManager cm = (ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        LinkProperties prop = cm.getLinkProperties(ConnectivityManager.TYPE_WIFI);
//        return formatIpAddresses(prop);
//    }
//
//    private static String formatIpAddresses(LinkProperties prop) {
//        if (prop == null)
//            return null;
//        Iterator<InetAddress> iter = prop.getAllAddresses().iterator();
//        // If there are no entries, return null
//        if (!iter.hasNext())
//            return null;
//        // Concatenate all available addresses, comma separated
//        String addresses = "";
//        while (iter.hasNext()) {
//            addresses += iter.next().getHostAddress();
//            if (iter.hasNext())
//                addresses += "\n";
//        }
//        return addresses;
//    }


    private ArrayList<ScanResult> removeDuplicateWithOrder(ArrayList<ScanResult> list) {
        Set set = new HashSet();
        ArrayList<ScanResult> newList = new ArrayList<>();
        for(ScanResult element : list){
            if(set.add(element)) newList.add(element);
        }
        return newList;
    }

    public WifiConfiguration getSavedWifiConfig(String SSID, List<WifiConfiguration> existingConfigs) {
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) return existingConfig;
        }
        return null;
    }


    @RequiresPermission(allOf = {Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE})
    public void connect2AccessPoint(ScanResult scanResult, String wifiPWD) {
//        mPassWord = wifiPWD;
//        mApName = scanResult.SSID;
        int securityType = getSecurityType(scanResult);
        WifiManager wifiMgr = getWifiMgr();
        List<WifiConfiguration> configs = wifiMgr.getConfiguredNetworks();

        WifiConfiguration config = getSavedWifiConfig(scanResult.SSID, configs);

        if (config == null) {
            config = new WifiConfiguration();
            // config.BSSID = scanResult.BSSID;
            config.SSID = "\"" + scanResult.SSID + "\"";
            config = getConfigBySecurityType(config, securityType);
            // config.priority = 1;
            config.status = WifiConfiguration.Status.ENABLED;
            int netId = wifiMgr.addNetwork(config);
            wifiMgr.enableNetwork(netId, true);
            wifiMgr.saveConfiguration();
        } else {
            config.status = WifiConfiguration.Status.ENABLED;
            config = getConfigBySecurityType(config, securityType);
            wifiMgr.enableNetwork(config.networkId, true);
            wifiMgr.updateNetwork(config);
            wifiMgr.saveConfiguration(); // if ap is changed(such as
            // pwd, securitytype), need
            // to save
        }

        config.preSharedKey = wifiPWD;
    }


    private final int SECURITY_OPEN = 0;
    private final int SECURITY_WPA = 1;
    private final int SECURITY_WEP = 2;
    private final int SECURITY_EAP = 3;
    private final int SECURITY_UNKNOW = 4;

    public int getSecurityType(ScanResult result) {
        if (result.capabilities == null) {
            return SECURITY_OPEN;
        }
        if (result.capabilities.contains("WPA")) {
            return SECURITY_WPA;
        } else if (result.capabilities.contains("WEP")) {
            return SECURITY_WEP;
        } else if (result.capabilities.contains("EAP")) {
            return SECURITY_EAP;
        } else {
            return SECURITY_UNKNOW;
        }
    }

    private WifiConfiguration getConfigBySecurityType(WifiConfiguration config, int securityType) {

        switch (securityType) {
            case SECURITY_OPEN:
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                break;

            case SECURITY_WPA:
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
//                config.preSharedKey = "\"" + getPassWord() + "\"";
                break;
            case SECURITY_WEP:
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
//                String password = getPassWord();
//                int length = password.length();
//                // WEP-40, WEP-104, and 256-bit WEP (WEP-232?)
//                if ((length == 10 || length == 26 || length == 58) && password.matches("[0-9A-Fa-f]*")) {
//                    config.wepKeys[0] = password;
//                } else {
//                    config.wepKeys[0] = '"' + password + '"';
//                }
                break;
            case SECURITY_EAP:
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
//                config.preSharedKey = "\"" + getPassWord() + "\"";
                break;
            default:
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                break;
        }
        return config;
    }
}
