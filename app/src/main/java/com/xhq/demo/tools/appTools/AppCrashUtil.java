package com.xhq.demo.tools.appTools;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.util.ArrayMap;

import com.xhq.demo.HomeApp;
import com.xhq.demo.tools.fileTools.StorageUtils;
import com.xhq.demo.tools.spTools.SPKey;
import com.xhq.demo.tools.spTools.SPUtils;
import com.xhq.demo.tools.uiTools.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/08/31.
 *     Desc  : catch exception of the app
 *     Updt  : Description
 * </pre>
 */
public class AppCrashUtil implements Thread.UncaughtExceptionHandler {
//    private static final String TAG = AppCrashUtil.class.getName();
    // Storage device information and exception information
    private Map<String, String> mExceptionMap;
    private static AppCrashUtil INSTANCE;
    private Context mContext;
    // system default UncaughtException
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

//    /**
//     * 使用Properties来保存设备的信息和错误堆栈信息
//     */
//    private final Properties mDeviceCrashInfo = new Properties();


//    private MyServiceConnection conn;
//
//    private File exceptionFile;

    /**
     * single instance
     */
    private AppCrashUtil() {

    }


    public static synchronized AppCrashUtil getInstance() {
        if (null == INSTANCE) INSTANCE = new AppCrashUtil();
        return INSTANCE;
    }


    public void init() {
        mContext = AppUtils.getAppContext();
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        // set the default CatchExceptionUtil processor for the program
        Thread.setDefaultUncaughtExceptionHandler(this);
//        conn = new MyServiceConnection();
//        mContext.bindService(new Intent(mContext, CatchCrashService.class), conn, BIND_AUTO_CREATE);
    }


    /**
     * the method is called when the UncaughtException is happened
     */
    public void uncaughtException(Thread thread, Throwable ex) {

        //弹出一个对话框
//        new Thread(){
//            public void run() {
//                Looper.prepare();
//                new AlertDialog.Builder(context)
//                        .setTitle("异常")
//                        .setMessage("我出现问题了")
//                        .show();
//                Looper.loop();
//            }
//        }.start();


        mExceptionMap = new ArrayMap<>();
        if (!handleException(ex) && mDefaultExceptionHandler != null) {
            // If the custom is not handled so that the system default exception handler to deal with
            mDefaultExceptionHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000); // 防止文件还未传完
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally{
//            if(null != conn){
//                mContext.unbindService(conn);
//                conn = null;
//            }

                HomeApp.exitApp();
//            android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);


//                Intent intent = new Intent();
//                intent.setAction("com.zkh.vendingcheck");
//                intent.putExtra("info", "stopCheck");
//                mContext.sendBroadcast(intent);
//
//                AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
//                Intent intent1 = new Intent(mContext, MainActivity.class);
//                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent1.putExtra("crash", true);
//                PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0, intent1, PendingIntent.FLAG_ONE_SHOT);
//                Objects.requireNonNull(mgr)
//                       .set(AlarmManager.RTC, System.currentTimeMillis() + 10000, restartIntent); // 10秒钟后重启应用
//
//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(0);
//                System.gc();
            }
        }
    }


    /**
     * handle exception
     *
     * @param ex exception of throwable
     * @return Whether the exception is handled
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) return false;

        mExceptionMap.clear();
        final String msg = ex.getLocalizedMessage();
//        Logger.t("未处理异常").e(ex, "捕捉到的未知异常");

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                ToastUtils.showToast("程序出错啦:" + msg);
                Looper.loop();
            }

        }.start();

        String exceptionStr = collectInfo(ex);

//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.CHINA);
//        String time = format.format(new Date());
//        String name = "Crash-" + accountStr + "-" + time + ".log";
        String accountStr = SPUtils.get(SPKey.USER_CONFIG, SPKey.USER_ACCOUNT, "");
        String fileName = "Crash-" + accountStr + ".log";

        saveExpInfo2File(exceptionStr, fileName);
//        Logger.t("xhq").e("exceptionFile" + "....." + exceptionFile.getAbsoluteFile());
//        listener.uploadExceptionFile(exceptionFile);


//        if(null != exceptionFile){
//            SPUtils.put(SPKey.ERROR_LOG, name, false);
//        }
//        uploadExceptionFile(exceptionFile);

        return true;
    }


    /**
     * collect device information
     *
     * @param ex @see {@link #handleException(Throwable)}
     * @return Collected information
     */
    private String collectInfo(Throwable ex) {

        String versionName = AppUtils.getVersionInfo()[0];
        String versionCode = AppUtils.getVersionInfo()[1];
        mExceptionMap.put("versionName", versionName);
        mExceptionMap.put("versionCode", versionCode);

        mExceptionMap = DeviceUtils.getDeviceInfo(mExceptionMap);

        return getExceptionInfo(ex);
    }


    /**
     * get exception information
     *
     * @param ex @see {@link #handleException(Throwable)}
     * @return Collected exception information
     */
    private String getExceptionInfo(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<String, String>> entries = mExceptionMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\r\n");
        }
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();
        String result = writer.toString();
        sb.append(result);
        return sb.toString();
    }


    /**
     * Copied from "android.util.Log.getStackTraceInfo()" in order to avoid usage of Android stack
     * in unit tests.
     *
     * @return Stack trace in form of String
     */
    static String getStackTraceInfo(Throwable tr){
        if(tr == null) return "";

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while(t != null){
            if(t instanceof UnknownHostException) return "";
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }


    public static String getStackTrace(Throwable tr) {
        // add the class name and any message passed to constructor
        final StringBuilder result = new StringBuilder("BOO-BOO: ");
        result.append(tr.toString());
        final String NEW_LINE = System.getProperty("line.separator");
        result.append(NEW_LINE);

        // add each element of the stack trace
        StackTraceElement[] stackTrace = tr.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            result.append(element);
            result.append(NEW_LINE);
        }

//        //使用反射来收集设备信息.在Build类中包含各种设备信息,
//        //例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
//        //具体信息请参考后面的截图
//        Field[] fields = Build.class.getDeclaredFields();
//        for (Field field : fields) {
//            try {
//                field.setAccessible(true);
//                mDeviceCrashInfo.put(field.getName(), "" + field.get(null));
//                if (DEBUG) {
//                    Log.d(TAG, field.getName() + " : " + field.get(null));
//                }
//            } catch (Exception e) {
//                Log.e(TAG, "Error while collect crash info", e);
//            }
//        }
        return result.toString();
    }


    /**
     * save the catch exception
     *
     * @param exceptionInfo exception information
     * @param fileName      The name of the file to save the exception information
     * @return exception file
     */
    private File saveExpInfo2File(String exceptionInfo, String fileName) {

        File cacheDir = StorageUtils.getAppCacheDir("crash");
        File exceptionFile = new File(cacheDir, fileName);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.CHINA);
        String time = format.format(new Date());

        try {
//            Logger.t(TAG).e("ExceptionFilePath" + "..." + exceptionFile);
            if (!exceptionFile.exists()) exceptionFile.delete();
            exceptionFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(exceptionFile);
            fos.write(time.getBytes());
            fos.write("\r\n".getBytes());
            fos.write("\r\n".getBytes());
            fos.write("======================================================================================".getBytes());
            fos.write("\r\n".getBytes());
            fos.write("\r\n".getBytes());
            fos.write(exceptionInfo.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exceptionFile;
    }

    /**
     * 获取崩溃头
     *
     * @return 崩溃头
     */
    private String getCrashHead() {
        return "\n************* Crash Log Head ****************" +
                "\nDevice Manufacturer: " + Build.MANUFACTURER +// 设备厂商
                "\nDevice Model       : " + Build.MODEL +// 设备型号
                "\nAndroid Version    : " + Build.VERSION.RELEASE +// 系统版本
                "\nAndroid SDK        : " + Build.VERSION.SDK_INT +// SDK版本
//                "\nApp VersionName    : " + versionName +
//                "\nApp VersionCode    : " + versionCode +
                "\n************* Crash Log Head ****************\n\n";
    }

}
