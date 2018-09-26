package com.xhq.demo.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Formatter;
import java.util.Locale;

import cn.jzvd.JZVideoPlayer;


/**
 * Author: YuJunKui
 * Time:2017/11/21 17:19
 * Tips:  全民炫
 */

public class VideoUtils{

    /**
     * 设置界面全屏，对jc播放器进行处理
     *
     * @param activity
     */
    public static void setActivityFullscreen(Activity activity) {
        //设置全屏
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //jc播放器会在内部setFlags和clearFlags
        JZVideoPlayer.TOOL_BAR_EXIST = false;
    }

    public static void cancelActivityFullscreen(Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

    }


    public static void loadWebp4Bitmap(Context context, String url, SimpleTarget<GlideDrawable> callBack) {

        Glide.with(context).load(url).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL).into(callBack);
    }

    public static void loadWebp(Context context, String url, ImageView imageView) {

        drawableRequestBuilder(context, url).into(imageView);

    }

    public static void loadWebp(Context context, String url, int width, int height, ImageView imageView) {


        DrawableRequestBuilder drawableRequestBuilder = drawableRequestBuilder(context, url);

        if (width != 0 && height != 0) {
            drawableRequestBuilder.override(width, height);
        }
        drawableRequestBuilder.into(imageView);

    }

    public static void loadWebp(Context context, String url, ImageView imageView, int loading) {

        // http://image-demo.oss-cn-hangzhou.aliyuncs.com/panda.png?x-oss-process=image/format,jpg
        drawableRequestBuilder(context,
                StringUtils.splitJoint(url, "?x-oss-process=image/format,webp")
        ).placeholder(loading).into(imageView);

    }

    public static DrawableRequestBuilder drawableRequestWebPBuilder(Context context, String url) {

        return Glide.with(context).load(StringUtils.splitJoint(url, "?x-oss-process=image/format,webp")).skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    public static DrawableRequestBuilder drawableRequestBuilder(Context context, String url) {

        return Glide.with(context).load(url).skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    /**
     * 友好显示数量（大于一w显示一位小数）
     *
     * @param number
     * @return
     */
    public static String formatNumberFriendly(int number) {
        DecimalFormat decimalFormat;
        if (number >= 10000) {
            decimalFormat = new DecimalFormat("0.#");
            decimalFormat.setRoundingMode(RoundingMode.DOWN);
            double valueShow = number / 10000d;
            return decimalFormat.format(valueShow) + "w";
        } else {
            return String.valueOf(number);
        }
    }

    public static String formatFFmpegTime(int totalSeconds) {
        if (totalSeconds <= 0 || totalSeconds >= 24 * 60 * 60) {
            return "00:00:00";
        }
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
        return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
    }

    public static String formatMusicDuration(int totalSeconds) {
        if (totalSeconds <= 0 || totalSeconds >= 24 * 60 * 60) {
            return "00:00";
        }
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    /**
     * 获取音乐时长(单位为秒)
     *
     * @param file
     * @return
     */
    public static long getMusicDuration(File file) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(file.getAbsolutePath());
        long fileSize = file.length();
        long bitRate = Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
        long duration = (fileSize * 8) / (bitRate);//单位，秒
        return duration;
    }

    /**
     * 解析时长字符串（eg 01:20:15）
     *
     * @param durationStr
     * @return
     */
    public static long parseDuration(String durationStr) {
        long time = 0;

        String[] splitDurationStr = durationStr.split(":");
        final int size = splitDurationStr.length;
        int[] splitDuration = new int[size];

        try {
            for (int i = 0; i < size; i++) {
                splitDuration[i] = Integer.parseInt(splitDurationStr[i]);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }

        for (int i = 0; i < size; i++) {
            time += (long) (splitDuration[i] * Math.pow(60, size - i - 1));
        }
        return time;
    }


}
