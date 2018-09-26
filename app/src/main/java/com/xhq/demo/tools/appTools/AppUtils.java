package com.xhq.demo.tools.appTools;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.xhq.demo.HomeApp;
import com.xhq.demo.tools.ProcessUtils;
import com.xhq.demo.tools.ShellUtils;
import com.xhq.demo.tools.StringUtils;
import com.xhq.demo.tools.encodeTools.EncryptUtils;
import com.xhq.demo.tools.fileTools.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/8/31.
 *     Desc  : application package tool
 *     Updt  : Description
 * </pre>
 */
public class AppUtils{

//    private static LinkedList<Activity> allActivity = new LinkedList<>();
    public static final boolean IS_HIGH_4DOT4_SYSTEM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    private AppUtils(){
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    /**
     * @return current application context
     */
    public static Context getAppContext(){
        return HomeApp.getAppContext();
    }

    public static Resources getResources(){
        return getAppContext().getResources();
    }

    public static PackageManager getPkgMgr(){
        return getAppContext().getPackageManager();
    }


    /**
     *
     * UUID含义是通用唯一识别码 (Universally Unique Identifier)，这是一个软件建构的标准，
     * 也是被开源软件基金会 (Open Software Foundation, OSF) 的组织在
     * 分布式计算环境 (Distributed Computing Environment, DCE) 领域的一部份。UUID 的目的，
     * 是让分布式系统中的所有元素，都能有唯一的辨识资讯，而不需要透过中央控制端来做辨识资讯的指定。
     * 如此一来，每个人都可以建立不与其它人冲突的 UUID。在这样的情况下，就不需考虑数据库建立时的名称重复问题。
     *
     * @return UUID String
     */
    public synchronized static String getUUID() {
        final String UUID;
        final String INSTALLATION = "INSTALLATION";
        File file = new File(getAppContext().getFilesDir(), INSTALLATION);
        try {
            if (!file.exists()) writeInstallationFile(file);
            UUID = readInstallationFile(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return UUID;
    }


    /**
     * Generate a value suitable for use in #setId(int).
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public static int generateViewId() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) { // 4.2
            final AtomicInteger nextGeneratedId = new AtomicInteger(1);
            for (; ; ) {
                final int result = nextGeneratedId.get();
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
                if (nextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }
    }


    private static String readInstallationFile(File installation) throws IOException{
        RandomAccessFile raf = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) raf.length()];
        raf.readFully(bytes);
        raf.close();
        return new String(bytes,"UTF-8");
    }

    private static void writeInstallationFile(File installation)throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes("UTF-8"));
        out.close();
    }

//    ====================================================================================================

    /**
     * see {@link #getInstallAppIntent(File)}
     */
    public static void installApk(Activity act, @NonNull String apkFilePath){
        File file = FileUtils.getFileByPath(apkFilePath);
        installApk(act, file);
    }

    public static void installApk(Activity act, File apkFile){
        act.startActivity(getInstallAppIntent(apkFile));
    }

    public static void installApk(Activity act, File apkFile, int requestCode) {
        act.startActivityForResult(getInstallAppIntent(apkFile), requestCode);
    }


    /**
     * 获取安装App(支持6.0)的意图<p>
     *     Android 7.0的新特性规定，对于android 7.0应用(仅仅对于android 7.0版本的sdk而言，
     *     若是编译版本低于25仍然不会受到影响)，android框架使用StrictMode Api禁止我们的应用
     *     对外部(跨越应用分享)公开file://,若使用file://格式共享文件则会报FileUriExposedException异常，
     *     android 7.0应用间的文件共享需要使用content://类型的URI分享，并且需要为其提供临时的文件访问权限
     (Intent.FLAG_GRANT_READ_URI_PERMISSION和Intent.FLAG_GRANT_WRITE_URI_PERMISSION),
     对此，官方给我们的建议是使用FileProvider类进行分享.
     * </p>
     *  <ul>
     *      manifest 配置信息
     *      <br> <application>
     *      <br> ...
     *      <br> <provider
     *      <br> android:name="android.support.v4.content.FileProvider" //provider的类名
     *      <br> android:authorities="package name" //没有特定要求，一般与包名相同
     *      <br> android:exported="false"           //不建议设置未true
     *      <br> android:grantUriPermissions="true">  //允许你有给其赋予临时访问权限的权力
     *
     *      <br> <meta-data
     *      <br> android:name="android.support.FILE_PROVIDER_PATHS"
     *      <br> android:resource="@xml/file_paths" />
     *
     *      <br> </provider>
     *      <br> ...
     *      <br> </application>
     * </ul>
     *
     * @param file 文件
     * @return intent
     */
    private static Intent getInstallAppIntent(File file) {
        if (!FileUtils.isFileExist(file)) return null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            type = "application/vnd.android.package-archive";
        } else {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(FileUtils.getFileExtension(file.getPath()));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String auth = getPkgName() + ".fileProvider";
            Uri contentUri = FileProvider.getUriForFile(getAppContext(), auth, file);
            intent.setDataAndType(contentUri, type);
        }
        intent.setDataAndType(Uri.fromFile(file), type);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }


    /**
     * silent install apk
     * @param apkFilePath apk file path
     * @return {@code true}: success<br>{@code false}: failure
     */
    @RequiresPermission(Manifest.permission.INSTALL_PACKAGES)
    public static boolean installApkSilent(String apkFilePath) {
        File file = FileUtils.getFileByPath(apkFilePath);
        if (!FileUtils.isFileExist(file)) return false;
        String command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install " + apkFilePath;
        boolean b = isSystemApp(getPkgName());
        ShellUtils.CommandResult cmdResult = ShellUtils.execCmd(command, !b, true);
        return cmdResult.successMsg != null && cmdResult.successMsg.toLowerCase().contains("success");
    }


    /**
     * @param pkgName the package name that to be uninstalled
     */
    private static Intent getUninstallAppIntent(String pkgName) {
        if(TextUtils.isEmpty(pkgName)) return null;
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + pkgName));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }


    public static void uninstallApp(Context ctx, String pkgName) {
        ctx.startActivity(getUninstallAppIntent(pkgName));
    }

    public static void uninstallApp(Activity act, String pkgName, int requestCode) {
        act.startActivityForResult(getUninstallAppIntent(pkgName), requestCode);
    }

    /**
     * silent uninstall App
     * @param pkgName the package name that to be uninstalled
     * @param isKeepData  whether to keep the data of the pkgName
     */
    @RequiresPermission(Manifest.permission.DELETE_PACKAGES)
    public static boolean uninstallAppSilent(String pkgName, boolean isKeepData) {
        if (TextUtils.isEmpty(pkgName)) return false;
        String cmd = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm uninstall " + (isKeepData ? "-k " : "") + pkgName;
        boolean isRoot = isSystemApp(getPkgName());
        ShellUtils.CommandResult cmdResult = ShellUtils.execCmd(cmd, !isRoot, true);
        return cmdResult.successMsg != null && cmdResult.successMsg.toLowerCase().contains("success");
    }


    /**
     * 判断App是否有root权限
     */
    public static boolean isAppRoot() {
        ShellUtils.CommandResult result = ShellUtils.execCmd("echo root", true);
        return result.result == 0;
    }


    private static Intent getLaunchAppIntent(String pkgName) {
        if(TextUtils.isEmpty(pkgName)) return null;
        return AppUtils.getAppContext().getPackageManager().getLaunchIntentForPackage(pkgName);
    }


    public static void launchApp(Activity act, String pkgName) {
        act.startActivity(getLaunchAppIntent(pkgName));
    }

    /**
     * open App
     *
     * @param act current activity
     * @param pkgName the app of need to open
     * @param requestCode request code
     */
    public static void launchApp(Activity act, String pkgName, int requestCode) {
        act.startActivityForResult(getLaunchAppIntent(pkgName), requestCode);
    }


    /**
     * get phone number of the running process
     */
    public static int getSystemRunningProcessSize() {
        ActivityManager am = (ActivityManager) getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
        return am != null ? am.getRunningAppProcesses().size() : 0;
    }


    /**
     * access to mobile phone available memory
     *
     * @return
     */
    public static String getPhoneFreeMem(){
        try{
            File file = new File("/proc/meminfo");
            BufferedReader br = new BufferedReader(new FileReader(file));
            // MemTotal: 516452 kB
            // MemFree: 376680 kB 第二行数据才是
            br.readLine();// 读取第一行
            String content = br.readLine();// 读取第二行
            // content.split(":")[1] 使用:号进行分割 获取第二部分 516452 kB
            // .trim() 去除前面的空格
            // .split(" ")[0] 使用空格进行分割 获取第一部分516452 单位是KB
            // *1024变为字节
            int mem = Integer.parseInt(content.split(":")[1].trim().split(" ")[0]) * 1024;
            return Formatter.formatFileSize(getAppContext(), mem);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * to obtain the total memory of the phone
     */
    public static String getPhoneTotalMem(){
        try{
            File file = new File("/proc/meminfo");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String content = br.readLine();
            int mem = Integer.parseInt(content.split(":")[1].trim().split(" ")[0]) * 1024;
            return Formatter.formatFileSize(getAppContext(), mem);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * judgment the app is a third part application
     *
     * @param appInfoFlags	ApplicationInfo.flags值奇数代表用户应用偶数代表系统应用<br>
     */
    public static boolean isUserApp(int appInfoFlags) {
        return (appInfoFlags & ApplicationInfo.FLAG_SYSTEM) != ApplicationInfo.FLAG_SYSTEM;
    }


    /**
     * judgment the app is a system application
     */
    public static boolean isSystemApp(@NonNull String pkgName){
        ApplicationInfo appInfo = getAppInfo(pkgName, 0);
        return appInfo != null && (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }


    /**
     * 是否安装在sd卡
     */
    public static boolean isInstallSD(int appInfoFlags) {
        return (appInfoFlags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE;
    }


    public static boolean isInstall(@NonNull String packageName) {
        return getLaunchAppIntent(packageName) != null;
    }


    /**
     * Gets the application version name and version code
     *
     * @return array of the version information (0 --> versionName, 1 --> versionCode)
     */
    public static String[] getVersionInfo(){
        String[] versionStr = new String[2];
        PackageInfo pkgInfo = getPkgInfo();
        if(null != pkgInfo){
            String versionName = String.valueOf(pkgInfo.versionName == null ? "null" : pkgInfo.versionName);
            String versionCode = String.valueOf(pkgInfo.versionCode);
            versionStr[0] = versionName;
            versionStr[1] = versionCode;
        }

        return versionStr;
    }


    /**
     *
     */
    public static String getPkgName(){
        return getAppContext().getPackageName();
    }


    /**
     * @return get the current App package name by the intent
     */
    public static String getPkgNameByIntent(Intent intent) {
        if (intent == null) return "";
        String pkgName = "";
        try {
            PackageManager pm = getAppContext().getPackageManager();
            ComponentName cn = intent.resolveActivity(pm);
            if (cn != null) pkgName = cn.getPackageName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pkgName;
    }


    /**
     * return information about activities in the package in activities.
     *
     * @return package information
     */
    public static PackageInfo getPkgInfo(){
        PackageManager pm = getPkgMgr();
        try{
            return pm.getPackageInfo(getPkgName(), PackageManager.GET_ACTIVITIES);
        }catch(NameNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * return information about activities in the package in activities.
     * @return ApplicationInfo
     */
    public static ApplicationInfo getAppInfo(){
        PackageInfo pkgInfo = getPkgInfo();
        if(null != pkgInfo)return pkgInfo.applicationInfo;
        return null;
    }


    /**
     * @param flags usually 0<br>
     *              <ul>
     *                  {@link PackageManager#GET_META_DATA}
     *                  {@link PackageManager#GET_SHARED_LIBRARY_FILES}
     *                  {@link PackageManager#MATCH_SYSTEM_ONLY}
     *                  {@link PackageManager#MATCH_UNINSTALLED_PACKAGES}
     *              </ul>
     * @return applicationInfo
     */
    public static ApplicationInfo getAppInfo(@NonNull String pkgName, int flags){
        ApplicationInfo appInfo = null;
        try{
            appInfo = getPkgMgr().getApplicationInfo(pkgName, flags);
        }catch(NameNotFoundException e){
            e.printStackTrace();
        }
        return appInfo;
    }


    /**
     * @return the application name
     */
    public static String getAppName(){
        PackageInfo pkgInfo = getPkgInfo();
        String appName = null;
        if(null != pkgInfo){
            int labelRes = pkgInfo.applicationInfo.labelRes;
            appName = getResources().getString(labelRes);
        }
        return appName;
    }


    /**
     * 获取App图标
     */
    public static Drawable getAppIcon(){
        PackageManager pm = getPkgMgr();
        PackageInfo pkgInfo = getPkgInfo();
        return pkgInfo == null ? null : pkgInfo.applicationInfo.loadIcon(pm);
    }


    /**
     * @return App路径
     */
    public static String getAppPath() {
            PackageInfo pkgInfo = getPkgInfo();
            return pkgInfo == null ? null : pkgInfo.applicationInfo.sourceDir;
    }


    /**
     * 判断App是否是Debug版本
     */
    public static boolean isAppDebug(Context ctx, @NonNull String pkgName) {
        try {
            PackageManager pm = ctx.getPackageManager();
            ApplicationInfo pkgInfo = pm.getApplicationInfo(pkgName, 0);
            return pkgInfo != null && (pkgInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取App签名
     */
    @SuppressLint("PackageManagerGetSignatures")
    public static Signature[] getAppSignature(Context ctx, @NonNull String pkgName) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pkgInfo = pm.getPackageInfo(pkgName, PackageManager.GET_SIGNATURES);
            return pkgInfo == null ? null : pkgInfo.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取应用签名的的SHA1值
     * <p>可据此判断高德，百度地图key是否正确</p>
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 应用签名的SHA1字符串, 比如：53:FD:54:DC:19:0F:11:AC:B5:22:9E:F1:1A:68:88:1B:8B:E8:54:42
     */
    public static String getAppSignatureSHA1(Context context, String packageName) {
        Signature[] signature = getAppSignature(context, packageName);
        if (signature == null) return null;
        return EncryptUtils.encryptHash2String(signature[0].toByteArray(), "SHA1").
                replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
    }



    /**
     * 判断App是否处于前台
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isAppForeground() {
        Context ctx = AppUtils.getAppContext();
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        if(am == null) return false;
        List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        if (infos == null || infos.size() == 0) return false;
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return info.processName.equals(getPkgName());
            }
        }
        return false;
    }



    /**
     * 判断App是否处于前台
     * <p>当不是查看当前App，且SDK大于21时，
     * 需添加权限 {@code <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS"/>}</p>
     *
     * @param pkgName 包名
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isAppForeground(String pkgName) {
        return !StringUtils.isSpace(pkgName) && pkgName.equals(ProcessUtils.getForegroundProcessName());
    }



    /**
     * 清除App所有数据
     *
     * @param context  上下文
     * @param dirPaths 目录路径
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean cleanAppData(Context context, String... dirPaths) {
        File[] dirs = new File[dirPaths.length];
        int i = 0;
        for (String dirPath : dirPaths) {
            dirs[i++] = new File(dirPath);
        }
        return cleanAppData(dirs);
    }

    /**
     * 清除App所有数据
     *
     * @param dirs 目录
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean cleanAppData(File... dirs) {
        boolean isSuccess = CleanUtils.cleanInternalCache();
        isSuccess &= CleanUtils.cleanInternalDbs();
        isSuccess &= CleanUtils.cleanInternalSP();
        isSuccess &= CleanUtils.cleanInternalFiles();
        isSuccess &= CleanUtils.cleanExternalCache();
        for (File dir : dirs) {
            isSuccess &= CleanUtils.cleanCustomCache(dir);
        }
        return isSuccess;
    }


    public static AppInfo getAppInfo(Context ctx, String pkgName) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(pkgName, 0);
            return getAppInfo(pm, pi);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static AppInfo getAppInfo(PackageManager pm, PackageInfo pi) {
        if (pm == null || pi == null) return null;
        ApplicationInfo ai = pi.applicationInfo;
        String packageName = pi.packageName;
        String name = ai.loadLabel(pm).toString();
        Drawable icon = ai.loadIcon(pm);
        String packagePath = ai.sourceDir;
        String versionName = pi.versionName;
        int versionCode = pi.versionCode;
        boolean isSystem = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != 0;
        return new AppInfo(packageName, name, icon, packagePath, versionName, versionCode, isSystem);
    }


    /**
     * 获取所有已安装App信息
     * <p>{@link #getAppInfo(PackageManager, PackageInfo)}（名称，图标，包名，包路径，版本号，版本Code，是否系统应用）</p>
     * <p>依赖上面的getBean方法</p>
     *
     * @param context 上下文
     * @return 所有已安装的AppInfo列表
     */
    public static List<AppInfo> getAppsInfo(Context context) {
        List<AppInfo> list = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        // 获取系统中安装的所有软件信息
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo pi : installedPackages) {
            AppInfo appInfo = getAppInfo(pm, pi);
            if (appInfo == null) continue;
            list.add(appInfo);
        }
        return list;
    }

//    /**
//     * 获取apk包的信息：版本号，名称，图标等
//     *
//     * @param apkAbsPath apk包的绝对路径
//     */
//    public static Bitmap getApkPkgInfo(String apkAbsPath) {
//        PackageManager pm = AppUtils.getAppContext().getPackageManager();
//        PackageInfo pkgInfo = pm.getPackageArchiveInfo(apkAbsPath, PackageManager.GET_ACTIVITIES);
//        if (pkgInfo != null) {
//            ApplicationInfo appInfo = pkgInfo.applicationInfo;
//            /* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
//            appInfo.sourceDir = apkAbsPath;
//            appInfo.publicSourceDir = apkAbsPath;
//            String appName = pm.getApplicationLabel(appInfo).toString();// 得到应用名
//            String packageName = appInfo.packageName; // 得到包名
//            String version = pkgInfo.versionName; // 得到版本信息
//            /* icon1和icon2其实是一样的 */
//            Drawable icon1 = pm.getApplicationIcon(appInfo);// 得到图标信息
////			Drawable icon2 = appInfo.loadIcon(pm);
//            String pkgInfoStr = String.format("PackageName:%s, Vesion: %s, AppName: %s, Icon: %s",
//                                              packageName, version, appName, icon1);
//            Log.i("println", String.format("PkgInfo: %s", pkgInfoStr));
//            return ImageUtil.drawable2Bitmap(icon1);
//        }
//        return null;
//    }


    /*
     * 采用了新的办法获取APK图标，之前的失败是因为android中存在的一个BUG,
     * 通过 appInfo.publicSourceDir = apkPath;来修正这个问题，详情参见:
     * http://code.google.com/p/android/issues/detail?id=9151
     */
    public static Drawable getApkPkgIcon(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                return appInfo.loadIcon(pm);
            } catch (OutOfMemoryError e) {
//				LogDebug.e(TAG, e.toString());
            }
        }
        return null;
    }


//    public void add(Activity activity){
//        if(allActivity != null){
//            allActivity.add(activity);
//        }
//    }

//    public static void exitApp(){
//
//        if(allActivity != null && allActivity.size() != 0){
//            for(Activity activity : allActivity){
//                activity.finish();
//            }
//            allActivity.clear();
//        }
//
////        HomeFragment.resetFlag();
//
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(1);
//    }

}