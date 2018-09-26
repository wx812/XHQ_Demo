package com.xhq.demo.tools;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/6/10.
 *     Desc  : video tools.
 *     Updt  : Description.
 * </pre>
 */
public class VideoUtil{



    /**
     * 检测视频是否损坏
     */
    public boolean isVideoDamaged(VideoFileInfo info){
        if(info.getDuration() == 0){
            //数据库获取到的时间为0，使用Retriever再次确认是否损坏
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try{
                retriever.setDataSource(info.getFilePath());
            }catch(Exception e){
                return true;//无法正常打开，也是错误
            }
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            return TextUtils.isEmpty(duration) || Integer.valueOf(duration) == 0;
        }
        return false;
    }

    public class VideoFileInfo implements Serializable{
        private int fileId;
        private String filePath;
        private String fileName;
        private String thumbPath;
        private boolean isSelected = false;
        private long duration;

        public VideoFileInfo() {
        }

        public VideoFileInfo(int fileId, String filePath, String fileName, String thumbPath, int duration) {
            this.fileId = fileId;
            this.filePath = filePath;
            this.fileName = fileName;
            this.thumbPath = thumbPath;
            this.duration = duration;
        }

        public int getFileId() {
            return this.fileId;
        }

        public void setFileId(int fileId) {
            this.fileId = fileId;
        }

        public String getFilePath() {
            return this.filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getFileName() {
            return this.fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public void setSelected(boolean selected) {
            this.isSelected = selected;
        }

        public boolean isSelected() {
            return this.isSelected;
        }

        public void setThumbPath(String thumbPath) {
            this.thumbPath = thumbPath;
        }

        public String getThumbPath() {
            return this.thumbPath;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public long getDuration() {
            return duration;
        }

        @Override
        public String toString() {
            return "TCVideoFileInfo{" +
                    "fileId=" + fileId +
                    ", filePath='" + filePath + '\'' +
                    ", fileName='" + fileName + '\'' +
                    ", thumbPath='" + thumbPath + '\'' +
                    ", isSelected=" + isSelected +
                    ", duration=" + duration +
                    '}';
        }
    }



    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 996
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                           int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    public static String getVideoThumbnailByPath(Context context, String path) {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{MediaStore.Video.Media._ID};
        String selection = MediaStore.Video.Media.DATA + " = ? ";
        String[] selectionArgs = new String[]{path};

        Cursor cursor = null;
        try {
            cursor = query(context, uri, projection, selection, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int mediaId = -1;
        if (cursor.moveToFirst()) {
            int idxId = cursor.getColumnIndex(MediaStore.Video.Media._ID);
            mediaId = cursor.getInt(idxId);
        }
        cursor.close();
        if (mediaId == -1) {
            return null;
        }

        uri = MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI;
        projection = new String[]{MediaStore.Video.Thumbnails.DATA};
        selection = MediaStore.Video.Thumbnails.VIDEO_ID + " =  ? ";
        selectionArgs = new String[]{String.valueOf(mediaId)};

        try {
            cursor = query(context, uri, projection, selection, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String thumbnail = null;
        if (cursor.moveToFirst()) {
            int idxData = cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA);
            thumbnail = cursor.getString(idxData);
        }
        cursor.close();
        return thumbnail;
    }

    private static Cursor query(Context context, Uri uri, String[] projection,
                                String selection, String[] selectionArgs) {
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = cr.query(uri, projection, selection, selectionArgs, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }
}
