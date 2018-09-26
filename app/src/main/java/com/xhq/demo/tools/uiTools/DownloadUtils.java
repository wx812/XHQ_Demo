package com.xhq.demo.tools.uiTools;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;

import com.xhq.demo.tools.appTools.AppUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <uses-permission android:name="android.permission.INTERNET"/>
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 * 如果需要隐藏下载工具的提示和显示，修改代码：
 * request.setShowRunningNotification(false);<br>
 * request.setVisibleInDownloadsUi(false);<br>
 * 加入下面的权限：
 * <uses-permission android:name="android.permission.DOWNLOAD_WITHOUT_NOTIFICATION"/>
 *
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/7/3.
 *     Desc  : Description.
 *     Updt  : Description.
 * </pre>
 */
public class DownloadUtils{

//    registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

//    private BroadcastReceiver receiver = new BroadcastReceiver(){
//        @Override
//        public void onReceive(Context context, Intent intent){
//            //这里可以取得下载的id，这样就可以知道哪个文件下载完成了。适用于多个下载任务的监听
//            Log.v("intent", "" + intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0));
//            queryDownloadStatus();
//        }
//    };


    /**
     * @return use the system's download manager
     */
    public static DownloadManager getDLMgr(){
        Context ctx = AppUtils.getAppContext();
        return (DownloadManager)ctx.getSystemService(Context.DOWNLOAD_SERVICE);
    }


    /**
     * use the system's download manager to download file
     * @param url download url address
     * @param savedFileName name of the file to store
     */
    public static long downLoad(@NonNull String url, @NonNull String savedFileName, String title){
            Uri uri = Uri.parse(encodeGB(url));
            DownloadManager.Request request = new DownloadManager.Request(uri); //开始下载

            request.setAllowedNetworkTypes(Request.NETWORK_WIFI | Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(false);
            MimeTypeMap mtMap = MimeTypeMap.getSingleton();
            String mimeType = mtMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
            request.setMimeType(mimeType);  //设置文件类型
            request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setVisibleInDownloadsUi(true);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, savedFileName);
            request.setTitle(title);

            return getDLMgr().enqueue(request); // downloadId
    }



    /**
     * 如果服务器不支持中文路径的情况下需要转换url的编码。
     *
     * @param string
     * @return
     */
    public static String encodeGB(String string){
        //转换中文编码
        String split[] = string.split("/");
        for(int i = 1; i < split.length; i++){
            try{
                split[i] = URLEncoder.encode(split[i], "GB2312");
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }
            split[0] = split[0] + "/" + split[i];
        }
        split[0] = split[0].replaceAll("\\+", "%20");//处理空格
        return split[0];
    }


    public static String queryDownloadStatus(long downloadId){
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        DownloadManager dlMgr = getDLMgr();
        Cursor c = dlMgr.query(query);
        String status = null;
        if(c.moveToFirst()){
            int statusCode = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch(statusCode){
                case DownloadManager.STATUS_PENDING:
                    status = "等待下载";
                    break;
                case DownloadManager.STATUS_RUNNING:
                    status = "正在下载";
                    break;
                case DownloadManager.STATUS_PAUSED:
                    status = "下载暂停";
                    break;
                case DownloadManager.STATUS_FAILED:
                    status = "下载失败";
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    status = "下载成功";
                    break;
            }
        }
        return status;
    }


    private void queryDownloadTask(int downloadStatus, List containerList) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByStatus(downloadStatus);
        Cursor cursor= getDLMgr().query(query);

        while(cursor.moveToNext()){
            String downId= cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
            String address = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
            //String statuss = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            String size= cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            String sizeTotal = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            Map<String, String> map = new HashMap<>();
            map.put("address", address);
            map.put("downId", downId);
            map.put("title", title);
            map.put("status", sizeTotal+":"+size);
            containerList.add(map);
        }
        cursor.close();
    }


}
