//package com.xhq.demo.widget.VideoView;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.graphics.Point;
//import android.media.MediaPlayer;
//import android.os.Handler;
//import android.os.Message;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.xhq.demo.R;
//
//import org.greenrobot.eventbus.EventBus;
//
//import cn.jzvd.JZMediaManager;
//import cn.jzvd.JZVideoPlayer;
//
///**
// * Author: YuJunKui
// * Time:2017/11/8 15:24
// * Tips:
// */
//
//public class TrillVideoView extends JZVideoPlayer{
//
//    private long mLastClickTime;
//
//    private static final String TAG = "TrillVideoView";
//
//    public ImageView thumbnail;
////    private TrillProgressView trillProgressView;
//
//    private static final int NO_CLICK_DOUBLE = 1002;
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case NO_CLICK_DOUBLE:
//                    onClick(startButton);
//                    startButtonStatus();
////                    KLog.i(TAG, "单击事件判定");
//                    break;
//            }
//        }
//    };
//
//    private void startButtonStatus() {
//        if (currentState == CURRENT_STATE_PAUSE) {
//            startButton.setVisibility(View.VISIBLE);
//        } else {
//            startButton.setVisibility(View.GONE);
//        }
//    }
//
//    public TrillVideoView(Context context) {
//        this(context, null);
//    }
//
//    public TrillVideoView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        widthRatio = 0;
//        heightRatio = 0;
//    }
//
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.video_view_trill_video_view;
//    }
//
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        handler.removeCallbacksAndMessages(null);
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//
////        KLog.i(DazzlePraiseView.TAG, " surface_container FonClick");
//        //屏蔽父类 父类的处理老子啥都不要
//        return false;
//    }
//
//
//    @Override
//    public void onClick(View v) {
//
//        int i = v.getId();
//        if (i == R.id.surface_container) {
//
//            //检测是否产生双击  因为视频详情附带了双击点赞效果
//            long currentTimeMillis = System.currentTimeMillis();
//            if (currentTimeMillis - mLastClickTime < DazzlePraiseView.COOLING_TIME) {
//                //产生符合点赞双击事件
//                handler.removeMessages(NO_CLICK_DOUBLE);//删除点击事件
//            } else {
//                //先启动一个单机事件
//                handler.sendEmptyMessageDelayed(NO_CLICK_DOUBLE, DazzlePraiseView.COOLING_TIME);
//            }
//            mLastClickTime = currentTimeMillis;
//
//        } else {
//            super.onClick(v);
//            startButtonStatus();
//        }
//    }
//
//    @Override
//    public void initTextureView() {
//        KLog.i(TAG, "initTextureView: ");
//        removeTextureView();
//        JZMediaManager.textureView = new TrillTextureView(getContext());
//        JZMediaManager.textureView.setSurfaceTextureListener(JZMediaManager.instance());
//    }
//
//    @Override
//    public void setUiWitStateAndScreen(int state) {
//        super.setUiWitStateAndScreen(state);
//        startButtonStatus();
//    }
//
//    /**
//     * 这个完成是  播放停止
//     */
//    @Override
//    public void onCompletion() {
//        super.onCompletion();
////        KLog.i(TAG, "onCompletion: ");
//        //在切换的时候也会这样
//        thumbnail.setVisibility(View.VISIBLE);
//        startButton.setVisibility(View.VISIBLE);
//        if (onVideoLoadListener != null) {
//            onVideoLoadListener.onVideoLoadSuccess();
//            onVideoLoadListener.onCompletion();
//        }
//    }
//
//    @Override
//    public void setProgressAndText() {
//
//        try {
//            int position = this.getCurrentPositionWhenPlaying();
//            int duration = this.getDuration();
//            int progress = position * 100 / (duration == 0 ? 1 : duration);
////            trillProgressView.setProgress(progress);
//            EventBus.getDefault().post(new TrillVideoProgressEvent(progress));
//            Log.i(TAG, "setProgressAndText() called position=[" + position + "]");
//        } catch (Exception e) {
//            //存在getDuration没有
//        }
//    }
//
//    @Override
//    public void init(Context context) {
//        super.init(context);
////        KLog.i(TAG, "init: ");
//        thumbnail = findViewById(R.id.img_thumbnail);
//        loop = true;
////        trillProgressView = findViewById(R.id.tpv_progress);
//    }
//
//    @Override
//    public void prepareMediaPlayer() {
//        super.prepareMediaPlayer();
////        KLog.i(TAG, "prepareMediaPlayer: ");
//
//        if (onVideoLoadListener != null) onVideoLoadListener.onVideoLoading();
//    }
//
//    @Override
//    public void setUp(String url, int screen, Object... objects) {
//        super.setUp(url, screen, objects);
////        KLog.i(TAG, "setUp: ");
//        thumbnail.setVisibility(View.VISIBLE);
//
//        EventBus.getDefault().post(new TrillVideoProgressEvent(0));
//    }
//
//
//    public static void releaseAllVideos() {
//        JZMediaManager.instance().releaseMediaPlayer();
//        JZMediaManager.textureView = null;
//    }
//
//
//    private OnVideoLoadListener onVideoLoadListener;
//
//
//    public void setOnVideoLoadListener(OnVideoLoadListener onVideoLoadListener) {
//        this.onVideoLoadListener = onVideoLoadListener;
//    }
//
//
//    /**
//     * 加载状态监听
//     */
//    public interface OnVideoLoadListener {
//
//        void onVideoLoading();
//
//        void onVideoLoadSuccess();
//        void onCompletion();
//
//        //加载成功
//        void onVideoPrepared();
//    }
//
//    private OnVideoSizeChangeListener onVideoSizeChangeListener;
//
//    public void setOnVideoSizeChangeListener(OnVideoSizeChangeListener onVideoSizeChangeListener) {
//        this.onVideoSizeChangeListener = onVideoSizeChangeListener;
//    }
//
//    @Override
//    public void onVideoSizeChanged() {
//        super.onVideoSizeChanged();
//        Log.i(TAG, "onVideoSizeChanged: ");
//        int videoW = JZMediaManager.instance().currentVideoWidth;
//        int videoH = JZMediaManager.instance().currentVideoHeight;
//        if (onVideoSizeChangeListener != null && videoW > 0 && videoH > 0) {
//            onVideoSizeChangeListener.onVideoSizeChange(videoW, videoH);
//        }
//    }
//
//    public interface OnVideoSizeChangeListener {
//        void onVideoSizeChange(int width, int height);
//    }
//
////    /**
////     * 播放视频受到MOBILE_NETWORK_PLAY_MODE 控制
////     */
////    public void playVideo() {
////
////        if (MOBILE_NETWORK_PLAY_MODE != false) {
////            startButton.performClick();
////        }
////    }
//
////    /**
////     * 移动网络下播放模式 null提示用户选择模式  false不播放  true直接播放
////     */
////    public static Boolean MOBILE_NETWORK_PLAY_MODE = null;
//
//    public static boolean isShowNetWorkDialog = false;
//
//    @Override
//    public void showWifiDialog() {
//        super.showWifiDialog();
////        KLog.i(TAG, "showWifiDialog: ");
//
////        //因为框架内部问题判断问题(JCVideoPlayer文件196行代码判断问题)
////        //导致当选择停止播放后再次播放依然会提示  期望效果不提示则 抛弃了框架使用的WIFI_TIP_DIALOG_SHOWED值
////
////        if (MOBILE_NETWORK_PLAY_MODE == null) {
//        if (isShowNetWorkDialog) return;
//        isShowNetWorkDialog = true;
//        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
//        Dialog dialog = builder.setMessage("您当前正在使用移动网络，继续播放将消耗流量")
//                .setNegativeButton("停止播放", (dialog1, which) -> {
//                    startButton.setVisibility(View.VISIBLE);
//                    isShowNetWorkDialog = false;
//                }).setPositiveButton("继续播放", (dialog1, which) -> {
//                    prepareMediaPlayer();
//                    startButton.setVisibility(View.GONE);
//                    WIFI_TIP_DIALOG_SHOWED = true;
//                })
//                .create();
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//        dialog.show();
////        } else {
////
////            //这里有两种情况
////            //1、用户选择移动网络继续播放 这里需要继续播放
////            //2、用户选择播放模式为移动网络继续不播放的时候  再次走到这里是因为用户点击了播放按钮
////
////            prepareMediaPlayer();
////            startButton.setVisibility(View.GONE);
////        }
//
//    }
//
//
//    @Override
//    public void onInfo(int what, int extra) {
//        super.onInfo(what, extra);
////        KLog.i(TAG, "onInfo() called with: what = [" + what + "], extra = [" + extra + "]");
//        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
//            thumbnail.setVisibility(View.GONE);
//            if (onVideoLoadListener != null) onVideoLoadListener.onVideoLoadSuccess();
//            if (onVideoLoadListener != null) onVideoLoadListener.onVideoPrepared();
//        }
//    }
//
//
//}