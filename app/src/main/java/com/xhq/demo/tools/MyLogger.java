package com.xhq.demo.tools;

import android.os.Environment;
import android.util.Log;

import com.xhq.demo.tools.appTools.AppUtils;
import com.xhq.demo.tools.fileTools.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

/**
 * The class for print log
 *
 * @author kesenhoo
 */
public class MyLogger{
    private static final boolean OPEN_LOG = true;

    public static final String tag = "AppName";
    private static final int LOG_LEVEL = Log.VERBOSE;
    private static Hashtable<String, MyLogger> mLoggerTable = new Hashtable<>();
    private String mClassName;
    //不同开发人员的日志使用对象
    private static MyLogger jlog;
    private static MyLogger xLog;
    //开发人员的名字
    private static final String JAMES = "@james@ ";
    private static final String XHQ = "@xhq@ ";


    private static int     stackIndex     = 0;
    private static char    logFilter      = 'v';


    private MyLogger(String name){
        mClassName = name;
    }


    /**
     * @param userName developer
     * @return MyLogger
     */
    public static MyLogger getLogger(String userName){
        MyLogger myLogger = mLoggerTable.get(userName);
        if(myLogger == null){
            myLogger = new MyLogger(userName);
            mLoggerTable.put(userName, myLogger);
        }
        return myLogger;
    }


    /**
     * Purpose:Mark user one
     */
    public static MyLogger xLog(){
        if(xLog == null) xLog = new MyLogger(XHQ);
        return xLog;
    }


    /**
     * Purpose:Mark user two
     */
    public static MyLogger jLog(){
        if(jlog == null) jlog = new MyLogger(JAMES);
        return jlog;
    }


//    public static String setLogLevel(int value) {
//        switch (value) {
//            case Log.VERBOSE:
//                return "VERBOSE";
//            case Log.DEBUG:
//                return "DEBUG";
//            case Log.INFO:
//                return "INFO";
//            case Log.WARN:
//                return "WARN";
//            case Log.ERROR:
//                return "ERROR";
//            case Log.ASSERT:
//                return "ASSERT";
//            default:
//                return "UNKNOWN";
//        }
//    }


    /**
     * Get The Current Function Name
     */
    private String getFunctionName(){
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if(sts == null){
            return null;
        }
        for(StackTraceElement st : sts){
            if(st.isNativeMethod() || st.getClassName().equals(Thread.class.getName())
                    || st.getClassName().equals(this.getClass().getName()))
                continue;
            return mClassName + "[ " + Thread.currentThread().getName() + ": "
                    + st.getFileName() + ":" + st.getLineNumber() + " "
                    + st.getMethodName() + " ]";
        }
        return null;
    }


    /**
     * The Log Level:i
     */
    public void i(Object str){
        if(OPEN_LOG){
            if(LOG_LEVEL <= Log.INFO){
                String name = getFunctionName();
                Log.i(tag, name != null ? name + " - " + str : str.toString());
            }
        }

    }


    /**
     * The Log Level:d
     */
    public void d(Object str){
        if(OPEN_LOG){
            if(LOG_LEVEL <= Log.DEBUG){
                String name = getFunctionName();
                Log.d(tag, name != null ? name + " - " + str : str.toString());
            }
        }
    }


    /**
     * The Log Level:V
     */
    public void v(Object str){
        if(OPEN_LOG){
            if(LOG_LEVEL <= Log.VERBOSE){
                String name = getFunctionName();
                Log.v(tag, name != null ? name + " - " + str : str.toString());
            }
        }
    }


    /**
     * The Log Level:w
     */
    public void w(Object str){
        if(OPEN_LOG){
            if(LOG_LEVEL <= Log.WARN){
                String name = getFunctionName();
                Log.w(tag, name != null ? name + " - " + str : str.toString());
            }
        }
    }


    /**
     * The Log Level:e
     */
    public void e(Object str){
        if(OPEN_LOG){
            if(LOG_LEVEL <= Log.ERROR){
                String name = getFunctionName();
                Log.e(tag, name != null ? name + " - " + str : str.toString());
            }
        }
    }


    /**
     * The Log Level:e
     */
    public void e(Exception ex){
        if(OPEN_LOG){
            if(LOG_LEVEL <= Log.ERROR) Log.e(tag, "error", ex);
        }
    }


    /**
     * The Log Level:e
     */
    public void e(String log, Throwable tr){
        if(OPEN_LOG){
            String line = getFunctionName();
            Log.e(tag, "{Thread:" + Thread.currentThread().getName() + "}"
                    + "[" + mClassName + line + ":] " + log + "\n", tr);
        }
    }


    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tag  标签
     * @param msg  消息
     * @param tr   异常
     * @param type 日志类型
     */
    private static void log(String tag, String msg, Throwable tr, char type, boolean log2FileSwitch) {
        if (msg == null || msg.isEmpty()) return;
        if (OPEN_LOG) {
            if ('e' == type && ('e' == logFilter || 'v' == logFilter)) {
                printLog(generateTag(tag), msg, tr, 'e');
            } else if ('w' == type && ('w' == logFilter || 'v' == logFilter)) {
                printLog(generateTag(tag), msg, tr, 'w');
            } else if ('d' == type && ('d' == logFilter || 'v' == logFilter)) {
                printLog(generateTag(tag), msg, tr, 'd');
            } else if ('i' == type && ('d' == logFilter || 'v' == logFilter)) {
                printLog(generateTag(tag), msg, tr, 'i');
            }
            if (log2FileSwitch) {
                log2File(type, generateTag(tag), msg + '\n' + Log.getStackTraceString(tr));
            }
        }
    }

    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tag  标签
     * @param msg  消息
     * @param tr   异常
     * @param type 日志类型
     */
    private static void printLog(final String tag, final String msg, Throwable tr, char type) {
        final int maxLen = 4000;
        for (int i = 0, len = msg.length(); i * maxLen < len; ++i) {
            String subMsg = msg.substring(i * maxLen, (i + 1) * maxLen < len ? (i + 1) * maxLen : len);
            switch (type) {
                case 'e':
                    Log.e(tag, subMsg, tr);
                    break;
                case 'w':
                    Log.w(tag, subMsg, tr);
                    break;
                case 'd':
                    Log.d(tag, subMsg, tr);
                    break;
                case 'i':
                    Log.i(tag, subMsg, tr);
                    break;
            }
        }
    }

    /**
     * 打开日志文件并写入日志
     *
     * @param type 日志类型
     * @param tag  标签
     * @param msg  信息
     **/
    private synchronized static void log2File(final char type, final String tag, final String msg) {
        String dir;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dir = AppUtils.getAppContext().getExternalCacheDir().getPath() + File.separator + "log" + File.separator;
        } else {
            dir = AppUtils.getAppContext().getCacheDir().getPath() + File.separator + "log" + File.separator;
        }

        Date now = new Date();
        String date = new SimpleDateFormat("MM-dd", Locale.getDefault()).format(now);
        final String fullPath = dir + date + ".txt";
        if (!FileUtils.createFile(fullPath)) return;
        String time = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(now);
        final String dateLogContent = time + ":" + type + ":" + tag + ":" + msg + '\n';
        new Thread(() -> {
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new FileWriter(fullPath, true));
                bw.write(dateLogContent);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                CloseUtils.closeIO(bw);
            }
        }).start();
    }

    /**
     * 产生tag
     *
     * @return tag
     */
    private static String generateTag(String tag) {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        if (stackIndex == 0) {
            while (!stacks[stackIndex].getMethodName().equals("generateTag")) {
                ++stackIndex;
            }
            stackIndex += 3;
        }
        StackTraceElement caller = stacks[stackIndex];
        String cClazzName = caller.getClassName();
        cClazzName = cClazzName.substring(cClazzName.lastIndexOf(".") + 1);
        String cMethodName = caller.getMethodName();
        int cLineName = caller.getLineNumber();
        String format = "Tag[" + tag + "] %s[%s, %d]";
        return String.format(Locale.getDefault(), format, cClazzName, cMethodName, cLineName);
    }

}  
