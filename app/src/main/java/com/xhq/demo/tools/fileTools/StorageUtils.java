package com.xhq.demo.tools.fileTools;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import com.xhq.demo.tools.CloseUtils;
import com.xhq.demo.tools.appTools.AppUtils;
import com.xhq.demo.tools.dateTimeTools.DateTimeUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * question : -->  Environment.getExternalStorageState()，却返回 ”removed“；<br>
 * Environment.getExternalStorageDirectory()，返回：“/mnt/sdcard”
 * <p>
 * 不同的设备上，调用getExternalStorageDirectory()返回值却不一样。
 * 查询了Android的文档，才找到原因，原来这个方法返回的是当前设备厂商所认为的“外部存储”，
 * 有可能返回外置的SD卡目录（Micro SD Card），也可能返回内置的存储目（eMMC）。
 * <p>
 * <ul>
 * 内部存储
 * <li> Environment.getDataDirectory()	/data
 * <p>
 * <li> Environment.getDataDirectory().getParentFile()	/
 * <p>
 * <li> Environment.getDownloadCacheDirectory()	/cache
 * <p>
 * <li> Environment.getRootDirectory()	/system
 * <p>
 * <li> context.getDir("temp", Context.MODE_PRIVATE) = /data/data/package_name/app_temp
 * <p>
 * <li> Context.getDatabasePath()  /data/data/package_name/databases/(config.db)
 * <p>
 * <li> Context.getPackageCodePath():  /data/app/com.zhd-1.apk
 * <p>
 * <li>Context.getPackageResourcePath():  /data/app/com.zhd-1.apk
 * <p>
 * </ul>
 * <p>
 * <ul>
 * 外部存储
 * <li>Environment.getExternalStorageDirectory() = /storage/emulated/0
 * <li> Context.getObbDir().getPath() = /storage/emulated/0/Android/obb/package_name/
 * <p>
 * <li>Environment.getExternalStoragePublicDirectory(DIRECTORY_ALARMS)	/storage/sdcard0/Alarms
 * <li>Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM)	/storage/sdcard0/DCIM
 * <li>Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS)	/storage/sdcard0/Download
 * <li>Environment.getExternalStoragePublicDirectory(DIRECTORY_MOVIES)	/storage/sdcard0/Movies
 * <li>Environment.getExternalStoragePublicDirectory(DIRECTORY_MUSIC)	/storage/sdcard0/Music
 * <li>Environment.getExternalStoragePublicDirectory(DIRECTORY_NOTIFICATIONS)	/storage/sdcard0/Notifications
 * <li>Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES)	/storage/sdcard0/Pictures
 * <li>Environment.getExternalStoragePublicDirectory(DIRECTORY_PODCASTS)	/storage/sdcard0/Podcasts
 * <li>Environment.getExternalStoragePublicDirectory(DIRECTORY_RINGTONES)	/storage/sdcard0/Ringtones
 * </ul>
 * <p>
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/8/26.
 *     Desc  : sd card tools.
 *     Updt  : 2017/12/22.
 * </pre>
 */
public class StorageUtils{

//        private static final String TAG = StorageUtils.class.getName();


    /**
     * Judgment sd card is available
     *
     * @return {@code true:} SD Available<br>{@code false:} unavailable
     */
//    @RequiresPermission(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)
    public static boolean isEnableSDCard(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


    /**
     * Judgment external storage is available
     */
    public static boolean isEnableExternalStorage(){
        return isEnableSDCard() || null != Environment.getExternalStorageDirectory();
    }


    public static boolean isExStorageAvailable() {
        if(!isEnableSDCard() && Environment.isExternalStorageRemovable()) {
            return false;
        } else {
            try {
                new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
                return true;
            } catch (Exception var1) {
                return false;
            }
        }
    }


    public static boolean isExStorageSpaceEnough(long fileSize) {
        String sdcard = getExStorageDir().getAbsolutePath();
        return getAvailableSize(sdcard) > fileSize;
    }

    //==========================================Storage Directory===========================================


    /**
     * Get system storage path
     *
     * @return String Return type --> /system
     */
    public static File getRootDirPath(){
        return Environment.getRootDirectory().getAbsoluteFile();
    }


    /**
     * return internal storage dir --> /data<br>
     */
    public static String getDataPath(){
        if(!isEnableSDCard()) return null;
//        return getSDPath() + File.separator + "data" + File.separator;
        return Environment.getDataDirectory().getAbsolutePath(); // 获取/data目录
    }


    /**
     * @return external storage dir --> /storage/emulated/
     */
    public static File getExStorageDir(){
        return Environment.getExternalStorageDirectory().getAbsoluteFile();
    }


    /**
     * @param dirName eg: see {@link Environment#DIRECTORY_DCIM}
     */
    public static File getExStoragePubDir(String dirName){
        return Environment.getExternalStoragePublicDirectory(dirName);
    }


    /**
     * get sd card path
     * <p>先用普通方法失败,再用shell，shell获取，一般是/storage/emulated/0/</p>
     *
     * @return SD card path
     */
    public static String getSDPath(){
        if(!isEnableSDCard()) return null;
        String sdPath = getExStorageDir().getAbsolutePath();
        if(!TextUtils.isEmpty(sdPath)) return sdPath;
        // 使用 shell 命令去获取
        final String cmd = "cat /proc/mounts";
        final String targetStr = "/.android_secure";
        Runtime run = Runtime.getRuntime();
        BufferedReader br = null;
        try{
            Process p = run.exec(cmd);
            br = new BufferedReader(new InputStreamReader(new BufferedInputStream(p.getInputStream())));
            String lineStr;
            while((lineStr = br.readLine()) != null){
                if(lineStr.contains("sdcard") && lineStr.contains(".android_secure")){
                    String[] strArray = lineStr.split(" ");
                    if(strArray.length >= 5){
                        return strArray[1].replace(targetStr, "") + File.separator;
                    }
                }
                if(p.waitFor() != 0 && p.exitValue() == 1) break;
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            CloseUtils.closeIO(br);
        }
        return sdPath;
    }


//======================================App Storage Directory===========================================

    /**
     * get cache directory of the application
     * <ul>
     * appCacheDir
     * <li>ExternalStorageDir{@code :null:} /storage/emulated/0/Android/data/package_name/cache/subPath</li>
     * <li>InternalStorageDir{@code :null:} /data/data/package_name/cache/subPath</li>
     * </ul>
     * delete the cache directory when the app is uninstalled, and return the external cache dir
     *
     * @param subPath Subdirectory of the cacheDir to be obtained
     * @return cacheDir path when subPath is null or the subPath Absolute path
     */
    public static File getAppCacheDir(@Nullable String subPath){
        File appCacheDir = getAppExStorageDir(subPath, true);
        if(appCacheDir == null) appCacheDir = getAppInternalStorageDir(subPath, true);

        appCacheDir = new File(appCacheDir + File.separator);
        FileUtils.makeDirs(appCacheDir.getAbsolutePath());
        if(!appCacheDir.exists() && !appCacheDir.mkdirs()){
            Log.e("xhq", "getCacheDirectory fail ,the reason is make directory fail !");
        }
        return appCacheDir;
    }


    /**
     * get directory of the External Storage<br>
     * see {@link #getAppInternalStorageDir(String, boolean)}
     * <ul>
     * subPath can be empty. cacheDirOrFilesDir : true, false,
     * <li>{@code true:} /storage/emulated/0/Android/data/package_name/cache/subPath</li>
     * <li>{@code false:} /storage/emulated/0/Android/data/package_name/files/subPath</li>
     * </ul>
     * <p>
     * eg: subPath --> Environment.DIRECTORY_PICTURES<br>
     * /storage/emulated/0/Android/data/package_name/files/subPath/Pictures<br>
     * {@link Environment#DIRECTORY_MUSIC}, {@link Environment#DIRECTORY_PODCASTS},
     * {@link Environment#DIRECTORY_RINGTONES}, {@link Environment#DIRECTORY_ALARMS},
     * {@link Environment#DIRECTORY_NOTIFICATIONS}, {@link Environment#DIRECTORY_PICTURES}, or
     * {@link Environment#DIRECTORY_MOVIES}.
     *
     * @return external storage directory (cache or files) .
     */
//    @RequiresPermission(allOf =
//            {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public static File getAppExStorageDir(@Nullable String subPath, boolean cacheDirOrFilesDir){
        File exStorageDir = null;
        if(StorageUtils.isEnableExternalStorage()){
            exStorageDir = cacheDirOrFilesDir ? getAppExCacheDir(subPath) : getAppExFilesDir(subPath);
            //  Some phones need to customize the directory
            if(exStorageDir == null) exStorageDir = customizeAppExCacheDir(subPath);
        }else{
            Log.e("xhq", "getAppExStorageDir fail ,the reason is sdCard nonexistence or sdCard mount fail !");
        }
        return exStorageDir;
    }


    /**
     * customize external cache fold.<br>
     * see {@link #getAppExCacheDir(String)}
     */
    public static File customizeAppExCacheDir(@Nullable String subPath){
        File exCacheDir;
        StringBuilder sb = new StringBuilder();
        File externalStorageDir = StorageUtils.getExStorageDir();
        String packageName = AppUtils.getAppContext().getPackageName();
        sb.append("Android/data/").append(packageName).append(File.separator)
          .append("cache").append(File.separator).append(subPath);

        exCacheDir = new File(externalStorageDir, sb.toString());
        if(!exCacheDir.exists() && !exCacheDir.mkdirs()){
            Log.e("xhq", "customizeAppExCacheDir fail ,the reason is make directory fail !");
        }
        return exCacheDir;
    }


    /**
     * see {@link #getAppInternalCacheDir(String)}
     *
     * @param subPath subPath
     * @return /storage/emulated/0/Android/data/app_package_name/cache/subPath
     */
    public static File getAppExCacheDir(@Nullable String subPath){
        File externalCacheDir = AppUtils.getAppContext().getExternalCacheDir();
        File cacheDir = TextUtils.isEmpty(subPath) ? externalCacheDir : new File(externalCacheDir, subPath);

        // Logger.t("xhq").e("ExternalCacheDir" + "..." + appCacheDir);
        assert cacheDir != null;
        if(!cacheDir.exists() && !cacheDir.mkdirs()){
            Log.e("xhq", "getExternalCacheDirectory fail ,the reason is make directory fail !");
        }
        return cacheDir;
    }


    /**
     * see {@link #getAppExStorageDir(String, boolean)}
     *
     * @param subPath subPath
     * @return /storage/emulated/0/Android/data/package_name/files/subPath/
     */
    public static File getAppExFilesDir(@Nullable String subPath){
        File externalFilesDir = AppUtils.getAppContext().getExternalFilesDir(null);
        File filesDir = TextUtils.isEmpty(subPath) ? externalFilesDir : new File(externalFilesDir, subPath);

        // Logger.t("xhq").e("ExternalFilesDir" + "..." + appCacheDir);
        assert filesDir != null;
        if(!filesDir.exists() && !filesDir.mkdirs()){
            Log.e("xhq", "getExternalFilesDirectory fail ,the reason is make directory fail !");
        }
        return filesDir;
    }


    /**
     * get directory of the Internal storage,the cache directory Can only use own
     * <ul>
     * subPath can be empty. cacheDirOrFilesDir : true, false,
     * <li>{@code true:} /data/data/package_name/cache/subPath</li>
     * <li>{@code false:} /data/data/package_name/files/subPath/</li>
     * </ul>
     *
     * @param subPath subdirectory
     * @param cacheDirOrFilesDir <ul>
     * <li>{@code true:}internal cache directory</li>
     * <li>{@code false:}internal Files directory</li>
     * </ul>
     * @return File --> application private cache directory, can't see and access if without root permission.
     */
    public static File getAppInternalStorageDir(@Nullable String subPath, boolean cacheDirOrFilesDir){
        return cacheDirOrFilesDir ? getAppInternalCacheDir(subPath) : getAppInternalFilesDir(subPath);
    }


    /**
     * see {@link #getAppInternalStorageDir(String, boolean)}
     *
     * @param subPath subPath
     * @return /data/data/package_name/cache/subPath/
     */
    public static File getAppInternalCacheDir(@Nullable String subPath){
        File internalCacheDir = AppUtils.getAppContext().getCacheDir().getAbsoluteFile();
        File cacheDir = TextUtils.isEmpty(subPath) ? internalCacheDir : new File(internalCacheDir, subPath);

        // Logger.t("xhq").e("InternalCacheDir" + "..." + appCacheDir);
        if(!cacheDir.exists() && !cacheDir.mkdirs()){
            Log.e("xhq", "getInternalCacheDirectory fail ,the reason is make directory fail !");
        }
        return cacheDir;
    }


    /**
     * see {@link #getAppInternalStorageDir(String, boolean)}
     *
     * @param subPath subPath
     * @return /data/data/package_name/files/subPath/
     */
    public static File getAppInternalFilesDir(@Nullable String subPath){
        File internalFileDir = AppUtils.getAppContext().getFilesDir().getAbsoluteFile();
        File filesDir = TextUtils.isEmpty(subPath) ? internalFileDir : new File(internalFileDir, subPath);

        // Logger.t("xhq").e("InternalFilesDir" + "..." + appCacheDir);
        if(!filesDir.exists() && !filesDir.mkdirs()){
            Log.e("xhq", "getInternalFilesDirectory fail ,the reason is make directory fail !");
        }
        return filesDir;
    }

    //==========================================Storage Directory===========================================


    /**
     * get sd card free space
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getSDFreeSpace(){
        return getAvailableSize(getAvailableSize(getSDPath()));
    }


    /**
     * get free space for internal storage
     */
    public static String getInternalFreeSpace(){
        return getAvailableSize(getAvailableSize(getDataPath()));
    }


    /**
     * the available size according to the target Path.
     * (The number of blocks that are free on the file system and available to applications.)
     *
     * @param targetPath target path
     * @return string
     */
    public static long getAvailableSize(String targetPath){
        StatFs statFs = new StatFs(targetPath);
        // added Api 18.  java.lang.NoSuchMethodError
//         long availableBlocks = statFs.getAvailableBlocksLong() - 4;
        long availableBlocks = statFs.getAvailableBlocksLong();
        long blockSize = statFs.getBlockSizeLong();
        return availableBlocks * blockSize;
    }


    public static String getAvailableSize(long fileSize){
        return Formatter.formatFileSize(AppUtils.getAppContext(), fileSize);
    }


    /**
     * get sd card info
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static StorageUtils.SDCardInfo getSDCardInfo(){
        if(!isEnableSDCard()) return null;
        StorageUtils.SDCardInfo sd = new StorageUtils.SDCardInfo();
        sd.isExist = true;
        StatFs sf = new StatFs(getSDPath());
        sd.totalBlocks = sf.getBlockCountLong();
        sd.blockByteSize = sf.getBlockSizeLong();
        sd.availableBlocks = sf.getAvailableBlocksLong();
        sd.availableBytes = sf.getAvailableBytes();
        sd.freeBlocks = sf.getFreeBlocksLong();
        sd.freeBytes = sf.getFreeBytes();
        sd.totalBytes = sf.getTotalBytes();
//        return sd.toString();
        return sd;
    }


    public static class SDCardInfo{
        boolean isExist;
        long totalBlocks;
        long freeBlocks;
        long availableBlocks;
        long blockByteSize;
        long totalBytes;
        long freeBytes;
        long availableBytes;


        @Override
        public String toString(){
            return "isExist=" + isExist + "\ntotalBlocks=" + totalBlocks
                    + "\nfreeBlocks=" + freeBlocks + "\navailableBlocks=" + availableBlocks
                    + "\nblockByteSize=" + blockByteSize + "\ntotalBytes=" + totalBytes
                    + "\nfreeBytes=" + freeBytes + "\navailableBytes=" + availableBytes;
        }
    }


    /**
     * 获取扩展SD卡存储目录
     * <p>
     * 如果有外接的SD卡，并且已挂载，则返回这个外置SD卡目录
     * 否则：返回内置SD卡目录
     */
    public static String getExSDCardPath(){
        File sdCardFile;
        String path = null;
        if(StorageUtils.isEnableExternalStorage()){
            sdCardFile = new File(getExStorageDir().getAbsolutePath());
            return sdCardFile.getAbsolutePath();
        }

        ArrayList<String> devMountList = getDevMountList();
        for(String devMount : devMountList){
            File file = new File(devMount);

            if(file.isDirectory() && file.canWrite()){
                path = file.getAbsolutePath();

                String timeStamp = DateTimeUtils.getCurrDateTime_yMdHms();
                File testWritable = new File(path, "test_" + timeStamp);

                if(testWritable.mkdirs()){
                    testWritable.delete();
                }else{
                    path = null;
                }
            }
        }

        if(path != null){
            sdCardFile = new File(path);
            return sdCardFile.getAbsolutePath();
        }
        return null;
    }


    /**
     * "system/etc/vold.fstab” 只是一个简单的配置文件，它描述了Android的挂载点信息。
     * <p>
     * iteration "system/etc/vold.fstab” file，obtained Android mount information
     */
    public static ArrayList<String> getDevMountList(){
        String configPath = "/etc/vold.fstab";
        File file = new File(configPath);
        StringBuilder sb = FileUtils.readFile(file, null);
        if(null == sb) return null;
        String[] toSearch = sb.toString().split(" ");
        ArrayList<String> out = new ArrayList<>();
        for(int i = 0; i < toSearch.length; i++){
            if(toSearch[i].contains("dev_mount")){
                if(new File(toSearch[i + 2]).exists()){
                    out.add(toSearch[i + 2]);
                }
            }
        }
        return out;
    }

}
