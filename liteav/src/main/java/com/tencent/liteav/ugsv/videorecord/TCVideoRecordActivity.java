package com.tencent.liteav.ugsv.videorecord;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.liteav.basic.log.TXCLog;
import com.tencent.liteav.ugsv.R;
import com.tencent.liteav.ugsv.common.activity.videopreview.TCVideoPreviewActivity;
import com.tencent.liteav.ugsv.common.utils.FileUtils;
import com.tencent.liteav.ugsv.common.utils.TCConstants;
import com.tencent.liteav.ugsv.common.widget.AutoDragHorizontalView;
import com.tencent.liteav.ugsv.common.widget.BeautySettingPanel;
import com.tencent.liteav.ugsv.common.widget.ClickToRecordView;
import com.tencent.liteav.ugsv.common.widget.CountdownPanel;
import com.tencent.liteav.ugsv.common.widget.CustomProgressDialog;
import com.tencent.liteav.ugsv.common.widget.VideoRecordButton;
import com.tencent.liteav.ugsv.shortvideo.choose.TCVideoChooseActivity;
import com.tencent.liteav.ugsv.shortvideo.editor.TCVideoPreprocessActivity;
import com.tencent.liteav.ugsv.shortvideo.view.RecordProgressView;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.ugc.TXRecordCommon;
import com.tencent.ugc.TXUGCRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;

/**
 * UGC小视频录制界面
 */
public class TCVideoRecordActivity extends Activity implements View.OnClickListener, BeautySettingPanel.IOnBeautyParamsChangeListener
        , TXRecordCommon.ITXVideoRecordListener, View.OnTouchListener, GestureDetector.OnGestureListener,
        ScaleGestureDetector.OnScaleGestureListener{

    private static final String TAG = "TCVideoRecordActivity";
    private static final String OUTPUT_DIR_NAME = "TXUGC";
    private boolean mRecording = false;
    private boolean mStartPreview = false;
    private boolean mFront = true;
    private TXUGCRecord mTXCameraRecord;
    private TXRecordCommon.TXRecordResult mTXRecordResult;
    private long mDuration; // 视频总时长

    private BeautySettingPanel.BeautyParams mBeautyParams = new BeautySettingPanel.BeautyParams();
    private TXCloudVideoView mVideoView;

    private ImageView iv_btn_camera_back;


    private ImageView iv_confirm;
    private TextView tv_progress_time;
    private Button btn_orientation;
    //    private ProgressDialog mCompleteProgressDialog;
    private CustomProgressDialog mCustomProgressDialog;
    private ImageView iv_btn_torch;
    private ImageView iv_btn_countdown;
    private ImageView iv_music_panel;
    private ImageView iv_filter_effects;
    private ImageView iv_scale;

    protected long bgMusicStart, bgMusicEnd;  //-1为原来长度
    protected String bgMusicPath;

    private ObjectAnimator scaleXAnimator;
    private AnimatorSet mAnimatorSet;


    private ComposeRecordBtn mComposeRecordBtn;
    private VideoRecordButton mVideoRecordBtn;
    private ClickToRecordView mClickToRecordView;
    private CountdownPanel countdown_panel;


    private RelativeLayout layout_record_btns;
    private LinearLayout ll_camera_type;

    private RelativeLayout rl_camera_function;
    private RelativeLayout rl_scale_aspect;
    private RelativeLayout rl_aspect_select;
    private ImageView iv_scale_first;
    private ImageView iv_scale_second;
    private ImageView iv_scale_mask;
    private boolean mAspectSelectShow = false;

    private BeautySettingPanel mBeautyPanelView;
    private AudioManager mAudioManager;
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusListener;
    private boolean mPause = false;
    private TCAudioControl mAudioCtrl;
    private int mCurrentAspectRatio;
    private int mFirstSelectScale;
    private int mSecondSelectScale;
    private RelativeLayout rl_record = null;
    private FrameLayout fl_mask;
    private RecordProgressView mRecordProgressView;
    private ImageView iv_delete_last_part;
    private boolean isSelected = false; // 回删状态
    private long mLastClickTime;
    private boolean mIsTorchOpen = false; // 闪光灯的状态

    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor;
    private float mLastScaleFactor;

    private int mRecommendQuality = TXRecordCommon.VIDEO_QUALITY_MEDIUM;
    private int mMinDuration;
    private int mMaxDuration;
    private int mAspectRatio; // 视频比例
    private int mRecordResolution; // 录制分辨率
    private int mHomeOrientation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN; // 录制方向
    private int mRenderRotation = TXLiveConstants.RENDER_ROTATION_PORTRAIT; // 渲染方向
    private int mBiteRate; // 码率
    private int mFps; // 帧率
    private int mGop; // 关键帧间隔
    private String mBGMPath;
    private String mBGMPlayingPath;
    private int mBGMDuration;
    private ImageView iv_music_mask;
    private RadioGroup mRadioGroup;
    private int mRecordSpeed = TXRecordCommon.RECORD_SPEED_NORMAL;
    private boolean mNeedEditer;
    private boolean mPortrait = true;
    private Button btn_snapshot;
    private RelativeLayout rl_back_next;

    private boolean isThreeSMode;

    private ImageView jinshan_iv_record_import;


    String[] cameraTypeStr = new String[]{"拍照", "长按拍摄", "单击拍摄"};
    String typeStr;
//    String[] cameraTypeStr = new String[]{"长按拍摄", "单击拍摄"};
    List<String> typeList;
    private RVCameraTypeAdapter typeAdapter;
    private AutoDragHorizontalView rv_auto_drag;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //拍摄页面全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video_record);

        rl_back_next = findViewById(R.id.rl_back_next);
        iv_btn_camera_back = findViewById(R.id.iv_btn_camera_back);
        iv_btn_camera_back.setOnClickListener(this);

        initViews();

        getData();
    }


    @Override
    protected void onStart(){
        super.onStart();
        setSelectAspect();

        onActivityRotation();

        if(hasPermission()){
            startCameraPreview();
        }
    }


    private void initViews(){

        fl_mask = findViewById(R.id.fl_mask);
        fl_mask.setOnTouchListener(this);

        iv_confirm = findViewById(R.id.iv_confirm);
        iv_confirm.setOnClickListener(this);
        iv_confirm.setImageResource(R.drawable.ugc_confirm_disable);
        iv_confirm.setEnabled(false);

        mBeautyPanelView = findViewById(R.id.beauty_panel);
        mBeautyPanelView.setBeautyParamsChangeListener(this);
        mBeautyPanelView.disableExposure();

        mAudioCtrl = findViewById(R.id.layoutAudioControl);
        mAudioCtrl.setOnItemClickListener(path -> {
            mBGMPath = path;
            mBGMDuration = mTXCameraRecord.setBGM(path);
            // 在选择音乐的时候试听一下
            if(!TextUtils.isEmpty(mBGMPath)){
                // 保证在试听的时候音乐是正常播放的
                mTXCameraRecord.setRecordSpeed(TXRecordCommon.RECORD_SPEED_NORMAL);
                mTXCameraRecord.playBGMFromTime(0, mBGMDuration);
            }
        });


        countdown_panel = findViewById(R.id.countdown_panel);

        countdown_panel.setMyOnclickListener(new CountdownPanel.MyOnclickListener(){
            @Override
            public void onClick(View view, Object obj){

                if (isRecordToMax()) {
                    //录制到最大时长
                    return;
                }

                countdown_panel.setVisibility(View.GONE);

//            if (!mKSYRecordKit.isRecording() && !mKSYRecordKit.isFileRecording()) {
//                //没开始录制才能点击
                startCountDownAnimator();
//            }

                isThreeSMode = !isThreeSMode;
                iv_btn_countdown.setImageResource(isThreeSMode ? R.drawable.ic_camera_countdown_selected : R.drawable.ic_camera_countdown_normal);
            }
        });

        mAudioCtrl.setReturnListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // 选择完音乐返回时试听结束
                if(!TextUtils.isEmpty(mBGMPath)){
                    mTXCameraRecord.stopBGM();
                    // 在试听结束时，再设置回原来的速度
                    mTXCameraRecord.setRecordSpeed(mRecordSpeed);
                }

                mAudioCtrl.mMusicSelectView.setVisibility(View.GONE);
                mAudioCtrl.setVisibility(View.GONE);
//            iv_music_panel.setImageResource(R.drawable.ugc_record_music);
                rl_record.setVisibility(View.VISIBLE);
            }
        });

        mVideoView = (TXCloudVideoView)findViewById(R.id.video_view);
        mVideoView.enableHardwareDecode(true);

        tv_progress_time = findViewById(R.id.tv_progress_time);
        iv_delete_last_part = findViewById(R.id.iv_delete_last_part);
        iv_delete_last_part.setOnClickListener(this);


        rl_camera_function = findViewById(R.id.rl_camera_function);

        iv_scale = findViewById(R.id.iv_scale);
        iv_scale_mask = findViewById(R.id.iv_scale_mask);
        iv_scale_first = findViewById(R.id.iv_scale_first);
        iv_scale_second = findViewById(R.id.iv_scale_second);
        rl_scale_aspect = findViewById(R.id.rl_scale_aspect);
        rl_aspect_select = findViewById(R.id.rl_aspect_select);

        iv_btn_countdown = findViewById(R.id.iv_btn_countdown);

        iv_music_panel = findViewById(R.id.iv_music_panel);
        iv_music_mask = findViewById(R.id.iv_music_mask);

        iv_filter_effects = findViewById(R.id.iv_filter_effects);

        rl_record = findViewById(R.id.rl_record);
        mRecordProgressView = findViewById(R.id.record_progress_view);

        mGestureDetector = new GestureDetector(this, this);
        mScaleGestureDetector = new ScaleGestureDetector(this, this);

        mCustomProgressDialog = new CustomProgressDialog();
        mCustomProgressDialog.createLoadingDialog(this, "");
        mCustomProgressDialog.setCancelable(false); // 设置是否可以通过点击Back键取消
        mCustomProgressDialog.setCanceledOnTouchOutside(false); // 设置在点击Dialog外是否取消Dialog进度条

        iv_btn_torch = findViewById(R.id.iv_btn_torch);
        iv_btn_torch.setOnClickListener(this);

        if(mFront){
            iv_btn_torch.setImageResource(R.drawable.ugc_torch_disable);
            iv_btn_torch.setEnabled(false);
        }else{
            iv_btn_torch.setImageResource(R.drawable.selector_camera_torch);
            iv_btn_torch.setEnabled(true);
        }


        mComposeRecordBtn = findViewById(R.id.compose_record_btn);

        // 录制标准 极慢, 极快
        mRadioGroup = findViewById(R.id.rg_record_speed);
        ((RadioButton)findViewById(R.id.rb_normal)).setChecked(true);
        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == R.id.rb_fast){
                mRecordSpeed = TXRecordCommon.RECORD_SPEED_FAST;

            }else if(checkedId == R.id.rb_fastest){
                mRecordSpeed = TXRecordCommon.RECORD_SPEED_FASTEST;

            }else if(checkedId == R.id.rb_normal){
                mRecordSpeed = TXRecordCommon.RECORD_SPEED_NORMAL;

            }else if(checkedId == R.id.rb_slow){
                mRecordSpeed = TXRecordCommon.RECORD_SPEED_SLOW;

            }else if(checkedId == R.id.rb_slowest){
                mRecordSpeed = TXRecordCommon.RECORD_SPEED_SLOWEST;

            }
            mTXCameraRecord.setRecordSpeed(mRecordSpeed);
        });

        btn_orientation = findViewById(R.id.btn_orientation);

        btn_snapshot = findViewById(R.id.btn_snapshot);
        btn_snapshot.setOnClickListener(this);
//        setRecordRotatioinListener();


        layout_record_btns = findViewById(R.id.layout_record_btns);

        jinshan_iv_record_import = findViewById(R.id.iv_import_video);
        jinshan_iv_record_import.setOnClickListener(this);


        // TODO XHQ
        mVideoRecordBtn = findViewById(R.id.video_record_btn);
        mVideoRecordBtn.setOnHoldDownListener(onDown -> {

            iv_btn_camera_back.setVisibility(onDown ? GONE : View.VISIBLE);
            rl_camera_function.setVisibility(onDown ? GONE : View.VISIBLE);
            mRadioGroup.setVisibility(onDown ? GONE : View.VISIBLE);
            layout_record_btns.setVisibility(onDown ? GONE : View.VISIBLE);
            ll_camera_type.setVisibility(onDown ? GONE : View.VISIBLE);

            if(onDown){
                if(mAspectSelectShow){
                    TCVideoRecordActivity.this.hideAspectSelectAnim();
                    mAspectSelectShow = !mAspectSelectShow;
                }
                TCVideoRecordActivity.this.switchRecord();

            }else{
                TCVideoRecordActivity.this.pauseRecord();
            }
        });


        mClickToRecordView = findViewById(R.id.click_to_record_view);
        ll_camera_type = findViewById(R.id.ll_camera_type);

        mClickToRecordView.setMyOnclickListener(new ClickToRecordView.MyOnClickListener(){
            @Override
            public void onClick(View view, boolean isAnimatorRun){

                iv_btn_camera_back.setVisibility(isAnimatorRun ? GONE : View.VISIBLE);
                rl_camera_function.setVisibility(isAnimatorRun ? GONE : View.VISIBLE);
                mRadioGroup.setVisibility(isAnimatorRun ? GONE : View.VISIBLE);
                layout_record_btns.setVisibility(isAnimatorRun ? GONE : View.VISIBLE);
                ll_camera_type.setVisibility(isAnimatorRun ? GONE : View.VISIBLE);

                if(isAnimatorRun){

                    if(mAspectSelectShow){
                        TCVideoRecordActivity.this.hideAspectSelectAnim();
                        mAspectSelectShow = !mAspectSelectShow;
                    }
                    TCVideoRecordActivity.this.switchRecord();

                }else {
                    TCVideoRecordActivity.this.pauseRecord();

                    rl_back_next.setVisibility(View.VISIBLE);
                    if(cameraTypeStr[1].equals(typeStr)){

                        mClickToRecordView.setVisibility(GONE);
                        mVideoRecordBtn.setVisibility(View.VISIBLE);
                    }else if(cameraTypeStr[2].equals(typeStr)){
                        mVideoRecordBtn.setVisibility(GONE);
                        mClickToRecordView.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        rv_auto_drag = findViewById(R.id.rv_auto_drag_horizontal);
        typeList = new ArrayList<>();
        typeList.addAll(Arrays.asList(cameraTypeStr));
        typeAdapter = new RVCameraTypeAdapter(this, typeList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_auto_drag.setLayoutManager(linearLayoutManager);

        rv_auto_drag.setOnItemClickListener(new AutoDragHorizontalView.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position){
//                KLog.e("xhq - rv_auto_drag", "pos:" + position);

                if(0 == position){          // 拍照

                }else if(1 == position){    // 长按拍摄
                    typeStr = cameraTypeStr[1];
                    mVideoRecordBtn.setVisibility(View.VISIBLE);
                    mClickToRecordView.setVisibility(GONE);
                }else if(2 == position){    //单击拍摄
                    typeStr = cameraTypeStr[2];
                    mVideoRecordBtn.setVisibility(GONE);
                    mClickToRecordView.setVisibility(View.VISIBLE);
                }
            }
        });

        rv_auto_drag.setOnSelectedPositionChangedListener(new AutoDragHorizontalView.OnItemSelectedChangedListener(){
            @Override
            public void onItemSelectedChanged(int position){
//                KLog.e("xhq - TCVideoRecord", "pos:" + position);

                if(0 == position){          // 拍照

                }else if(1 == position){    // 长按拍摄
                    mVideoRecordBtn.setVisibility(View.VISIBLE);
                    mClickToRecordView.setVisibility(GONE);
                }else if(2 == position){    //单击拍摄
                    mVideoRecordBtn.setVisibility(GONE);
                    mClickToRecordView.setVisibility(View.VISIBLE);
                }
            }
        });

        rv_auto_drag.setDefSelectPosition(1);
        rv_auto_drag.setAdapter(typeAdapter);
    }


    private void getData(){
        Intent intent = getIntent();
        if(intent == null){
            TXCLog.e(TAG, "intent is null");
            return;
        }
        mMinDuration = intent.getIntExtra(TCVideoSettingActivity.RECORD_CONFIG_MIN_DURATION, 5 * 1000);
        mMaxDuration = intent.getIntExtra(TCVideoSettingActivity.RECORD_CONFIG_MAX_DURATION, 60 * 1000);
        mAspectRatio = intent.getIntExtra(TCVideoSettingActivity.RECORD_CONFIG_ASPECT_RATIO, TXRecordCommon.VIDEO_ASPECT_RATIO_9_16);
        mRecommendQuality = intent.getIntExtra(TCVideoSettingActivity.RECORD_CONFIG_RECOMMEND_QUALITY, -1);
        mNeedEditer = intent.getBooleanExtra(TCVideoSettingActivity.RECORD_CONFIG_NEED_EDITER, true);

        mCurrentAspectRatio = mAspectRatio;
        setSelectAspect();

        mRecordProgressView.setMaxDuration(mMaxDuration);
        mRecordProgressView.setMinDuration(mMinDuration);

        if(mRecommendQuality != -1){
            // 使用了推荐的视频质量设置，用TXUGCSimpleConfig
            TXCLog.i(TAG, "mRecommendQuality = " + mRecommendQuality);
            return;
        }
        // 自定义视频质量设置，用TXUGCCustomConfig
        mRecordResolution = intent.getIntExtra(TCVideoSettingActivity.RECORD_CONFIG_RESOLUTION, TXRecordCommon.VIDEO_RESOLUTION_540_960);
        mBiteRate = intent.getIntExtra(TCVideoSettingActivity.RECORD_CONFIG_BITE_RATE, 1800);
        mFps = intent.getIntExtra(TCVideoSettingActivity.RECORD_CONFIG_FPS, 20);
        mGop = intent.getIntExtra(TCVideoSettingActivity.RECORD_CONFIG_GOP, 3);

        TXCLog.d(TAG, "mMinDuration = " + mMinDuration + ", mMaxDuration = " + mMaxDuration + ", mAspectRatio = " + mAspectRatio +
                ", mRecommendQuality = " + mRecommendQuality + ", mRecordResolution = " + mRecordResolution + ", mBiteRate = " + mBiteRate +
                ", mFps = " + mFps + ", mGop = " + mGop);
    }


    private void setRecordRotatioinListener(){
        // 如果想保持activity为竖屏，并且要home在右横屏录制，首先把mBtnRecordRotation的点击监听代码打开，把btn_orientation控件可见，然后在manifest中把该activity设置为竖屏android
        // :screenOrientation="portrait"
        btn_orientation.setVisibility(View.VISIBLE);
        btn_orientation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mTXCameraRecord.stopCameraPreview();
                mStartPreview = false;
                mPortrait = !mPortrait;
                if(mPortrait){
                    Toast.makeText(TCVideoRecordActivity.this, "竖屏录制", Toast.LENGTH_SHORT).show();
                    mHomeOrientation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
                    mRenderRotation = TXLiveConstants.RENDER_ROTATION_PORTRAIT;
                }else{
                    Toast.makeText(TCVideoRecordActivity.this, "横屏录制", Toast.LENGTH_SHORT).show();
                    mHomeOrientation = TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT;
                    mRenderRotation = TXLiveConstants.RENDER_ROTATION_LANDSCAPE;
                }
                startCameraPreview();
            }
        });
    }


    private void startCameraPreview(){
        if(mStartPreview) return;
        mStartPreview = true;

        mTXCameraRecord = TXUGCRecord.getInstance(this.getApplicationContext());
        mTXCameraRecord.setVideoRecordListener(this);

        // activity竖屏模式，竖屏录制 :
        //                           setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_DOWN);
        //                           setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        // activity横屏模式，home在右横屏录制(activity随着重力感应旋转)：
        //                           setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT);
        //                           setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        // activity横屏模式，home在左横屏录制(activity随着重力感应旋转)：
        //                           setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_LEFT);
        //                           setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        // 如果想保持activity为竖屏，并且要home在右横屏录制，那么可以用下面的方式：
        // activity竖屏模式，home在右横屏录制(锁定Activity不旋转，比如在manefest设置activity的 android:screenOrientation="portrait")：
        //                           setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT);
        //                           setRenderRotation(TXLiveConstants.RENDER_ROTATION_LANDSCAPE);
        //
        mTXCameraRecord.setHomeOrientation(mHomeOrientation);
        mTXCameraRecord.setRenderRotation(mRenderRotation);
        // 推荐配置
        if(mRecommendQuality >= 0){
            TXRecordCommon.TXUGCSimpleConfig simpleConfig = new TXRecordCommon.TXUGCSimpleConfig();
            simpleConfig.videoQuality = mRecommendQuality;
            simpleConfig.minDuration = mMinDuration;
            simpleConfig.maxDuration = mMaxDuration;
            simpleConfig.isFront = mFront;
            simpleConfig.needEdit = mNeedEditer;

            mTXCameraRecord.setRecordSpeed(mRecordSpeed);
            mTXCameraRecord.startCameraSimplePreview(simpleConfig, mVideoView);
            mTXCameraRecord.setAspectRatio(mCurrentAspectRatio);
        }else{
            // 自定义配置
            TXRecordCommon.TXUGCCustomConfig customConfig = new TXRecordCommon.TXUGCCustomConfig();
            customConfig.videoResolution = mRecordResolution;
            customConfig.minDuration = mMinDuration;
            customConfig.maxDuration = mMaxDuration;
            customConfig.videoBitrate = mBiteRate;
            customConfig.videoGop = mGop;
            customConfig.videoFps = mFps;
            customConfig.isFront = mFront;
            customConfig.needEdit = mNeedEditer;

            mTXCameraRecord.setRecordSpeed(mRecordSpeed);
            mTXCameraRecord.startCameraCustomPreview(customConfig, mVideoView);
            mTXCameraRecord.setAspectRatio(mCurrentAspectRatio);
        }

        mTXCameraRecord.setBeautyDepth(mBeautyParams.mBeautyStyle, mBeautyParams.mBeautyLevel, mBeautyParams.mWhiteLevel,
                                       mBeautyParams.mRuddyLevel);
        mTXCameraRecord.setFaceScaleLevel(mBeautyParams.mFaceSlimLevel);
        mTXCameraRecord.setEyeScaleLevel(mBeautyParams.mBigEyeLevel);
        mTXCameraRecord.setFilter(mBeautyParams.mFilterBmp);
        mTXCameraRecord.setGreenScreenFile(mBeautyParams.mGreenFile, true);
        mTXCameraRecord.setMotionTmpl(mBeautyParams.mMotionTmplPath);
        mTXCameraRecord.setFaceShortLevel(mBeautyParams.mFaceShortLevel);
        mTXCameraRecord.setFaceVLevel(mBeautyParams.mFaceVLevel);
        mTXCameraRecord.setChinLevel(mBeautyParams.mChinSlimLevel);
        mTXCameraRecord.setNoseSlimLevel(mBeautyParams.mNoseScaleLevel);
    }


    /**
     * 用来在activity随着重力感应切换方向时，切换横竖屏录制
     * 注意：使用时，录制过程中或暂停后不允许切换横竖屏，如果开始录制时使用的是横屏录制，那么整段录制都要用横屏，否则录制失败。
     */
    protected void onActivityRotation(){
        // 自动旋转打开，Activity随手机方向旋转之后，需要改变录制方向
        int mobileRotation = this.getWindowManager().getDefaultDisplay().getRotation();
        mRenderRotation = TXLiveConstants.RENDER_ROTATION_PORTRAIT; // 渲染方向，因为activity也旋转了，本地渲染相对正方向的角度为0。
        mHomeOrientation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
        switch(mobileRotation){
            case Surface.ROTATION_0:
                mHomeOrientation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
                break;
            case Surface.ROTATION_90:
                mHomeOrientation = TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT;
                break;
            case Surface.ROTATION_270:
                mHomeOrientation = TXLiveConstants.VIDEO_ANGLE_HOME_LEFT;
                break;
            default:
                break;
        }
    }


    @Override
    public void onClick(View view){
        int i = view.getId();

        if (i == R.id.iv_btn_camera_back){
            back();

        }else if(i == R.id.btn_next){


//                ================// 右侧栏 设置按钮======================

//        } else if (i == R.id.iv_scale) {
//            scaleDisplay();

//        } else if (i == R.id.iv_scale_first) {
//            selectAnotherAspect(mFirstSelectScale);
//
//        } else if (i == R.id.iv_scale_second) {
//            selectAnotherAspect(mSecondSelectScale);

        } else if (i == R.id.iv_filter_effects){
            mBeautyPanelView.setVisibility(mBeautyPanelView.getVisibility() == View.VISIBLE ? GONE : View.VISIBLE);
//            iv_filter_effects.setImageResource(
//                    mBeautyPanelView.getVisibility() == View.VISIBLE ? R.drawable.ugc_record_beautiful_girl_hover : R.drawable
//                            .ugc_record_beautiful_girl);
            rl_record.setVisibility(mBeautyPanelView.getVisibility() == View.VISIBLE ? GONE : View.VISIBLE);
            mVideoRecordBtn.setVisibility(mBeautyPanelView.getVisibility() == View.VISIBLE ? GONE : View.VISIBLE);
            mClickToRecordView.setVisibility(mBeautyPanelView.getVisibility() == View.VISIBLE ? GONE : View.VISIBLE);
            ll_camera_type.setVisibility(mBeautyPanelView.getVisibility() == View.VISIBLE ? GONE : View.VISIBLE);

            if(mAudioCtrl.getVisibility() == View.VISIBLE){
                mAudioCtrl.setVisibility(GONE);
//                iv_music_panel.setImageResource(R.drawable.ugc_record_music);
            }

        }else if(i == R.id.iv_btn_countdown){   // 拍摄倒计时

            isThreeSMode = !isThreeSMode;

            countdown_panel.setVisibility(isVisibleAntonym(countdown_panel));

            rl_back_next.setVisibility(isVisibleAntonym(countdown_panel));

//            iv_btn_camera_back.setVisibility(isVisibleAntonym(countdown_panel));

            rl_camera_function.setVisibility(isVisibleAntonym(countdown_panel));

            mRadioGroup.setVisibility(isVisibleAntonym(countdown_panel));

            layout_record_btns.setVisibility(isVisibleAntonym(countdown_panel));
            mVideoRecordBtn.setVisibility(isVisibleAntonym(countdown_panel));
            mClickToRecordView.setVisibility(isVisibleAntonym(countdown_panel));
            ll_camera_type.setVisibility(isVisibleAntonym(countdown_panel));

            mBeautyPanelView.setVisibility(isVisibleAntonym(countdown_panel));

        } else if (i == R.id.iv_music_panel) {
            mAudioCtrl.setPusher(mTXCameraRecord);

            mAudioCtrl.setVisibility(mAudioCtrl.getVisibility() == View.VISIBLE ? GONE : View.VISIBLE);
//            iv_music_panel.setImageResource(
//                    mAudioCtrl.getVisibility() == View.VISIBLE ? R.drawable.ugc_record_music_hover : R.drawable.ugc_record_music);
            rl_record.setVisibility(mAudioCtrl.getVisibility() == View.VISIBLE ? GONE : View.VISIBLE);

            if (mBeautyPanelView.getVisibility() == View.VISIBLE) {
                mBeautyPanelView.setVisibility(GONE);
//                iv_filter_effects.setImageResource(R.drawable.ugc_record_beautiful_girl);
            }


//          ==========底部 拍摄功能按钮===============================
        } else if (i == R.id.iv_btn_torch) {
            toggleTorch();

        } else if (i == R.id.iv_btn_camera_switch){
            mFront = !mFront;
            mIsTorchOpen = false;
            if(mFront){
                iv_btn_torch.setImageResource(R.drawable.ugc_torch_disable);
                iv_btn_torch.setEnabled(false);
            }else{
                iv_btn_torch.setImageResource(R.drawable.selector_camera_torch);
                iv_btn_torch.setEnabled(true);
            }
            if(mTXCameraRecord != null){
                TXCLog.i(TAG, "switchCamera = " + mFront);
                mTXCameraRecord.switchCamera(mFront);
            }

        }else if(i == R.id.iv_import_video){
            Intent intent = new Intent(TCVideoRecordActivity.this, TCVideoChooseActivity.class);
            intent.putExtra("CHOOSE_TYPE", TCVideoChooseActivity.TYPE_SINGLE_CHOOSE);
            startActivity(intent);

        } else if (i == R.id.iv_delete_last_part){
            deleteLastPart();

        } else if (i == R.id.iv_confirm) {
            mCustomProgressDialog.show();
            stopRecord();

        } else if (i == R.id.btn_snapshot) {
            snapshot();

        } else if (i == R.id.video_record_btn) {// TODO XHQ Start and pause
            if (mAspectSelectShow) {
                hideAspectSelectAnim();
                mAspectSelectShow = !mAspectSelectShow;
            }
            switchRecord();
//        }else if(i == R.id.compose_record_btn){
//
////          ==========录制===============================
//            if(mAspectSelectShow){
//                hideAspectSelectAnim();
//                mAspectSelectShow = !mAspectSelectShow;
//            }
//            switchRecord();
        }
    }


    private int isVisibleAntonym(View view){
        return view.isShown() ? GONE : View.VISIBLE;
    }

    private boolean isRecordToMax() {
//        return mRecordProgressView.getProgress() >= MAX_DURATION;
        return false;
    }


    private void startCountDownAnimator() {

        stop3SAnimator();

        ImageView iv_countdown = findViewById(R.id.iv_countdown);


        final int[] resId = new int[]{R.drawable.ic_num_three, R.drawable.ic_num_two, R.drawable.ic_num_one};
        //沿x轴缩小
        scaleXAnimator = ObjectAnimator.ofFloat(iv_countdown, "scaleX", 1f, 0.5f, 0.2f);
        scaleXAnimator.setRepeatCount(2);
        //沿y轴缩小
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(iv_countdown, "scaleY", 1f, 0.5f, 0.2f);
        scaleYAnimator.setRepeatCount(2);
        //透明度动画
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(iv_countdown, "alpha", 1f, 0.5f);
        alphaAnimator.setRepeatCount(2);
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.play(scaleXAnimator).with(scaleYAnimator).with(alphaAnimator);
        scaleXAnimator.addListener(new Animator.AnimatorListener() {
            int index = 0;

            @Override
            public void onAnimationStart(Animator animation) {
//                if (iv_btn_countdown.getVisibility() != View.VISIBLE) {
//                    iv_btn_countdown.setVisibility(View.VISIBLE);
//                }

                if(!iv_countdown.isShown()){
                    iv_countdown.setVisibility(View.VISIBLE);
                }
//                iv_btn_countdown.setImageDrawable(getResources().getDrawable(resId[0]));
                iv_countdown.setImageDrawable(getResources().getDrawable(resId[0]));
                index++;
            }

            @Override
            public void onAnimationEnd(Animator animation) {

//                iv_btn_countdown.setVisibility(View.GONE);

                if(!mClickToRecordView.isShown()){
                    iv_countdown.setVisibility(GONE);
                    mClickToRecordView.setVisibility(View.VISIBLE);
                    mClickToRecordView.startAnimation(true);
                }

                startRecord();
//                if (!TextUtils.isEmpty(bgMusicPath)) {
//                    if (bgMusicStart != -1 && bgMusicEnd != -1) {
//                        mKSYRecordKit.getAudioPlayerCapture().setPlayableRanges(bgMusicStart, bgMusicEnd);
//                    } else {
//                        mKSYRecordKit.getAudioPlayerCapture().setPlayableRanges(0, Long.MAX_VALUE);
//                    }
//                    mKSYRecordKit.startBgm(bgMusicPath, true);
//                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (index >= 0 && index < 3) {
                    iv_countdown.setImageDrawable(getResources().getDrawable(resId[index]));
                    index++;
                }
            }
        });
        mAnimatorSet.setDuration(1000).start();
    }


    /**
     * 开始/停止录制
     */
    private void onRecordClick() {

        //倒计时录制正在运行
        if (scaleXAnimator != null && scaleXAnimator.isRunning()) {
            scaleXAnimator.end();
            //end里面有开始录制的代码

        } else {

//            if (mIsFileRecording) {
//                stopRecord(false);
//
//                if (mRecordProgressView.getProgress() >= MIN_DURATION) {
//                    mNextView.setVisibility(View.VISIBLE);
//                }
//
//                mBackView.setImageResource(R.drawable.record_back);
//            } else {
//
//                imgMusic.setVisibility(View.GONE);
//
//                //如果录制到最大时长
//                if (isRecordToMax()) {
//                    onNextClick();
//                    return;
//                }
//
//                //是否第一次录制并且三秒模式
//                if (mRecordProgressView.getProgress() == 0 && isThreeSMode) {
//                    startCountDownAnimator();
//                    return;
//                }
//
//                //如果不是第一次录制并且是有音乐的情况下
//                if (mRecordProgressView.getProgress() != 0 && !TextUtils.isEmpty(bgMusicPath) && isDeleteFragment) {
//                    isDeleteFragment = false;
//                    long msec = mRecordProgressView.getProgress() % (bgMusicEnd - bgMusicStart) + bgMusicStart;
//                    mKSYRecordKit.getAudioPlayerCapture().getMediaPlayer().seekTo(msec);
//                    mKSYRecordKit.getAudioPlayerCapture().getMediaPlayer().setOnSeekCompleteListener(mp -> {
//                        mKSYRecordKit.getAudioPlayerCapture().resume();
//                        startRecord();
//                    });
//                } else if (!TextUtils.isEmpty(bgMusicPath)) {
//                    //有音乐恢复音乐
//                    mKSYRecordKit.getAudioPlayerCapture().resume();
//                    startRecord();
//                } else {
//
//                    startRecord();
//                }
//            }
//
//            //清除back按钮的状态
//            clearBackoff();
        }
    }


    private void stop3SAnimator() {
        if (mAnimatorSet != null) {
            scaleXAnimator.removeAllListeners();
            mAnimatorSet.cancel();
        }
    }


    private void switchRecord(){
        long currentClickTime = System.currentTimeMillis();
        if(currentClickTime - mLastClickTime < 100){
            return;
        }
        if(mRecording){
            if(mPause){
                if(mTXCameraRecord.getPartsManager().getPartsPathList().size() == 0){
                    startRecord();
                }else{
                    resumeRecord();
                }
            }else{
                pauseRecord();
            }
        }else{
            startRecord();
        }
        mLastClickTime = currentClickTime;
    }


    private void scaleDisplay(){
        if(!mAspectSelectShow){
            showAspectSelectAnim();
        }else{
            hideAspectSelectAnim();
        }
        mAspectSelectShow = !mAspectSelectShow;
    }


    private void selectAnotherAspect(int targetScale){
        if(mTXCameraRecord != null){
            scaleDisplay();

            mCurrentAspectRatio = targetScale;

            if(mCurrentAspectRatio == TXRecordCommon.VIDEO_ASPECT_RATIO_9_16){
                mTXCameraRecord.setAspectRatio(TXRecordCommon.VIDEO_ASPECT_RATIO_9_16);

            }else if(mCurrentAspectRatio == TXRecordCommon.VIDEO_ASPECT_RATIO_3_4){
                mTXCameraRecord.setAspectRatio(TXRecordCommon.VIDEO_ASPECT_RATIO_3_4);

            }else if(mCurrentAspectRatio == TXRecordCommon.VIDEO_ASPECT_RATIO_1_1){
                mTXCameraRecord.setAspectRatio(TXRecordCommon.VIDEO_ASPECT_RATIO_1_1);
            }

            setSelectAspect();
        }
    }


    private void hideAspectSelectAnim(){
        ObjectAnimator showAnimator = ObjectAnimator.ofFloat(rl_aspect_select, "translationX", 0f, 2 * (getResources().getDimension(
                R.dimen.ugc_aspect_divider) + getResources().getDimension(
                R.dimen.ugc_aspect_width)));
        showAnimator.setDuration(80);
        showAnimator.addListener(new Animator.AnimatorListener(){
            @Override
            public void onAnimationStart(Animator animator){

            }


            @Override
            public void onAnimationEnd(Animator animator){
                rl_aspect_select.setVisibility(GONE);
            }


            @Override
            public void onAnimationCancel(Animator animator){

            }


            @Override
            public void onAnimationRepeat(Animator animator){

            }
        });
        showAnimator.start();
    }


    private void toggleTorch(){
        if(mIsTorchOpen){
            mTXCameraRecord.toggleTorch(false);
            iv_btn_torch.setImageResource(R.drawable.ic_camera_torch_normal);
        }else{
            mTXCameraRecord.toggleTorch(true);
            iv_btn_torch.setImageResource(R.drawable.ic_camera_torch_selected);
        }
        mIsTorchOpen = !mIsTorchOpen;
    }


    private void deleteLastPart(){
        if(mRecording && !mPause){
            return;
        }
        if(!isSelected){
            isSelected = true;
            mRecordProgressView.selectLast();
        }else{
            isSelected = false;
            mRecordProgressView.deleteLast();
            mTXCameraRecord.getPartsManager().deleteLastPart();
            int timeSecond = mTXCameraRecord.getPartsManager().getDuration() / 1000;
            tv_progress_time.setText(String.format(Locale.CHINA, "00:%02d", timeSecond));
            if(timeSecond < mMinDuration / 1000){
                iv_confirm.setImageResource(R.drawable.ugc_confirm_disable);
                iv_confirm.setEnabled(false);
            }else{
                iv_confirm.setImageResource(R.drawable.selector_record_confirm);
                iv_confirm.setEnabled(true);
            }

            if(mTXCameraRecord.getPartsManager().getPartsPathList().size() == 0){
                iv_scale_mask.setVisibility(GONE);
                iv_music_mask.setVisibility(GONE);
            }
        }
    }


    private void stopRecord(){
        if(mTXCameraRecord != null){
            mTXCameraRecord.stopBGM();
            mTXCameraRecord.stopRecord();
        }
        ImageView liveRecord = findViewById(R.id.record);
        if(liveRecord != null) liveRecord.setBackgroundResource(R.drawable.start_record);
        mRecording = false;
        mPause = false;
        abandonAudioFocus();

        mRadioGroup.setVisibility(View.VISIBLE);
    }


    private void snapshot(){
        if(mTXCameraRecord != null){
            mTXCameraRecord.snapshot(new TXRecordCommon.ITXSnapshotListener(){
                @Override
                public void onSnapshot(Bitmap bmp){
                    saveBitmap(bmp);
                }
            });
        }
    }


    public static void saveBitmap(Bitmap bitmap){
        File dir = new File("/sdcard/TXUGC/");
        if(!dir.exists())
            dir.mkdirs();
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
        String time = sdf.format(new Date(currentTime));
        File f = new File(dir, String.valueOf(time) + ".jpg");
        if(f.exists()){
            f.delete();
        }
        try{
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    private void setSelectAspect(){
        if(mCurrentAspectRatio == TXRecordCommon.VIDEO_ASPECT_RATIO_9_16){
            iv_scale.setImageResource(R.drawable.selector_aspect169);
            mFirstSelectScale = TXRecordCommon.VIDEO_ASPECT_RATIO_1_1;
            iv_scale_first.setImageResource(R.drawable.selector_aspect11);

            mSecondSelectScale = TXRecordCommon.VIDEO_ASPECT_RATIO_3_4;
            iv_scale_second.setImageResource(R.drawable.selector_aspect43);
        }else if(mCurrentAspectRatio == TXRecordCommon.VIDEO_ASPECT_RATIO_1_1){
            iv_scale.setImageResource(R.drawable.selector_aspect11);
            mFirstSelectScale = TXRecordCommon.VIDEO_ASPECT_RATIO_3_4;
            iv_scale_first.setImageResource(R.drawable.selector_aspect43);

            mSecondSelectScale = TXRecordCommon.VIDEO_ASPECT_RATIO_9_16;
            iv_scale_second.setImageResource(R.drawable.selector_aspect169);
        }else{
            iv_scale.setImageResource(R.drawable.selector_aspect43);
            mFirstSelectScale = TXRecordCommon.VIDEO_ASPECT_RATIO_1_1;
            iv_scale_first.setImageResource(R.drawable.selector_aspect11);

            mSecondSelectScale = TXRecordCommon.VIDEO_ASPECT_RATIO_9_16;
            iv_scale_second.setImageResource(R.drawable.selector_aspect169);
        }
    }


    /**
     * animation pause record
     */
    private void showAspectSelectAnim(){
        ObjectAnimator showAnimator = ObjectAnimator.ofFloat(rl_aspect_select, "translationX", 2 * (getResources().getDimension(
                R.dimen.ugc_aspect_divider) + getResources().getDimension(
                R.dimen.ugc_aspect_width)), 0f);
        showAnimator.setDuration(80);
        showAnimator.addListener(new Animator.AnimatorListener(){
            @Override
            public void onAnimationStart(Animator animator){
                rl_aspect_select.setVisibility(View.VISIBLE);
            }


            @Override
            public void onAnimationEnd(Animator animator){

            }


            @Override
            public void onAnimationCancel(Animator animator){

            }


            @Override
            public void onAnimationRepeat(Animator animator){

            }
        });
        showAnimator.start();
    }


    private void startRecord(){
        // 在开始录制的时候，就不能再让activity旋转了，否则生成视频出错
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if(mTXCameraRecord == null){
            mTXCameraRecord = TXUGCRecord.getInstance(this.getApplicationContext());
        }

        String customVideoPath = getCustomVideoOutputPath();
        String customCoverPath = customVideoPath.replace(".mp4", ".jpg");

        int result = mTXCameraRecord.startRecord(customVideoPath, customCoverPath);
        if(result != TXRecordCommon.START_RECORD_OK){
            if(result == TXRecordCommon.START_RECORD_ERR_NOT_INIT){
                Toast.makeText(TCVideoRecordActivity.this.getApplicationContext(), "别着急，画面还没出来", Toast.LENGTH_SHORT).show();
            }else if(result == TXRecordCommon.START_RECORD_ERR_IS_IN_RECORDING){
                Toast.makeText(TCVideoRecordActivity.this.getApplicationContext(), "还有录制的任务没有结束", Toast.LENGTH_SHORT).show();
            }else if(result == TXRecordCommon.START_RECORD_ERR_VIDEO_PATH_IS_EMPTY){
                Toast.makeText(TCVideoRecordActivity.this.getApplicationContext(), "传入的视频路径为空", Toast.LENGTH_SHORT).show();
            }else if(result == TXRecordCommon.START_RECORD_ERR_API_IS_LOWER_THAN_18){
                Toast.makeText(TCVideoRecordActivity.this.getApplicationContext(), "版本太低", Toast.LENGTH_SHORT).show();
            }
//            mTXCameraRecord.setVideoRecordListener(null);
//            mTXCameraRecord.stopRecord();
            return;
        }

        // TODO XHQ
//        mComposeRecordBtn.startRecord();
//        mComposeRecordBtn.startRecord();
        iv_scale_mask.setVisibility(View.VISIBLE);   // 开始录制的时候不可修改屏比
        iv_delete_last_part.setImageResource(R.drawable.ugc_delete_last_part_disable);
        iv_delete_last_part.setEnabled(false);    // 删除按钮不可用

        if(!TextUtils.isEmpty(mBGMPath)){
            mBGMDuration = mTXCameraRecord.setBGM(mBGMPath);
            mTXCameraRecord.playBGMFromTime(0, mBGMDuration);
            mBGMPlayingPath = mBGMPath;
            TXCLog.i(TAG, "music duration = " + mTXCameraRecord.getMusicDuration(mBGMPath));
        }

        mAudioCtrl.setPusher(mTXCameraRecord);
        mRecording = true;
        mPause = false;
        ImageView liveRecord = findViewById(R.id.record);
        if(liveRecord != null) liveRecord.setBackgroundResource(R.drawable.video_stop);
        requestAudioFocus();

        iv_music_mask.setVisibility(View.VISIBLE);
        mRadioGroup.setVisibility(GONE);

        iv_delete_last_part.setVisibility(View.VISIBLE);
        iv_confirm.setVisibility(View.VISIBLE);
        jinshan_iv_record_import.setVisibility(GONE);
    }


    private void pauseRecord(){
        mComposeRecordBtn.pauseRecord();
        ImageView liveRecord = findViewById(R.id.record);
        if(liveRecord != null){
            liveRecord.setBackgroundResource(R.drawable.start_record);
        }
        mPause = true;
        iv_delete_last_part.setImageResource(R.drawable.selector_delete_last_part);
        iv_delete_last_part.setEnabled(true);

        if(mTXCameraRecord != null){
            if(!TextUtils.isEmpty(mBGMPlayingPath)){
                mTXCameraRecord.pauseBGM();
            }
            mTXCameraRecord.pauseRecord();
        }
        abandonAudioFocus();

        mRadioGroup.setVisibility(View.VISIBLE);
    }


    private void resumeRecord(){
        if(mTXCameraRecord == null){
            return;
        }
        int startResult = mTXCameraRecord.resumeRecord();
        if(startResult != TXRecordCommon.START_RECORD_OK){
            TXCLog.i(TAG, "resumeRecord, startResult = " + startResult);
            if(startResult == TXRecordCommon.START_RECORD_ERR_NOT_INIT){
                Toast.makeText(TCVideoRecordActivity.this.getApplicationContext(), "别着急，画面还没出来", Toast.LENGTH_SHORT).show();
            }else if(startResult == TXRecordCommon.START_RECORD_ERR_IS_IN_RECORDING){
                Toast.makeText(TCVideoRecordActivity.this.getApplicationContext(), "还有录制的任务没有结束", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if(!TextUtils.isEmpty(mBGMPath)){
            if(mBGMPlayingPath == null || !mBGMPath.equals(mBGMPlayingPath)){
                mTXCameraRecord.setBGM(mBGMPath);
                mTXCameraRecord.playBGMFromTime(0, mBGMDuration);
                mBGMPlayingPath = mBGMPath;
            }else{
                mTXCameraRecord.resumeBGM();
            }
        }

        mComposeRecordBtn.startRecord();
        ImageView liveRecord = findViewById(R.id.record);
        if(liveRecord != null){
            liveRecord.setBackgroundResource(R.drawable.video_stop);
        }
        iv_delete_last_part.setImageResource(R.drawable.ugc_delete_last_part_disable);
        iv_delete_last_part.setEnabled(false);
        iv_scale_mask.setVisibility(View.VISIBLE);

        mPause = false;
        isSelected = false;
        requestAudioFocus();

        mRadioGroup.setVisibility(GONE);
    }


    private String getCustomVideoOutputPath(){
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
        String time = sdf.format(new Date(currentTime));
        String outputDir = Environment.getExternalStorageDirectory() + File.separator + OUTPUT_DIR_NAME;
        File outputFolder = new File(outputDir);
        if(!outputFolder.exists()){
            outputFolder.mkdir();
        }
        String tempOutputPath = outputDir + File.separator + "TXUGC_" + time + ".mp4";
        return tempOutputPath;
    }


    private void startPreview(){
        if(mTXRecordResult != null && (mTXRecordResult.retCode == TXRecordCommon.RECORD_RESULT_OK
                || mTXRecordResult.retCode == TXRecordCommon.RECORD_RESULT_OK_REACHED_MAXDURATION
                || mTXRecordResult.retCode == TXRecordCommon.RECORD_RESULT_OK_LESS_THAN_MINDURATION)){
            Intent intent = new Intent(getApplicationContext(), TCVideoPreviewActivity.class);
            intent.putExtra(TCConstants.VIDEO_RECORD_TYPE, TCConstants.VIDEO_RECORD_TYPE_UGC_RECORD);
            intent.putExtra(TCConstants.VIDEO_RECORD_RESULT, mTXRecordResult.retCode);
            intent.putExtra(TCConstants.VIDEO_RECORD_DESCMSG, mTXRecordResult.descMsg);
            intent.putExtra(TCConstants.VIDEO_RECORD_VIDEPATH, mTXRecordResult.videoPath);
            intent.putExtra(TCConstants.VIDEO_RECORD_COVERPATH, mTXRecordResult.coverPath);
            intent.putExtra(TCConstants.VIDEO_RECORD_DURATION, mDuration);
            if(mRecommendQuality == TXRecordCommon.VIDEO_QUALITY_LOW){
                intent.putExtra(TCConstants.VIDEO_RECORD_RESOLUTION, TXRecordCommon.VIDEO_RESOLUTION_360_640);
            }else if(mRecommendQuality == TXRecordCommon.VIDEO_QUALITY_MEDIUM){
                intent.putExtra(TCConstants.VIDEO_RECORD_RESOLUTION, TXRecordCommon.VIDEO_RESOLUTION_540_960);
            }else if(mRecommendQuality == TXRecordCommon.VIDEO_QUALITY_HIGH){
                intent.putExtra(TCConstants.VIDEO_RECORD_RESOLUTION, TXRecordCommon.VIDEO_RESOLUTION_720_1280);
            }else{
                intent.putExtra(TCConstants.VIDEO_RECORD_RESOLUTION, mRecordResolution);
            }
            startActivity(intent);
            finish();
        }
    }


    private void startEditVideo(){
        Intent intent = new Intent(this, TCVideoPreprocessActivity.class);
//        fileInfo.setThumbPath(mTXRecordResult.coverPath);
//
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(mTXRecordResult.videoPath);
//        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//        fileInfo.setDuration(Integer.valueOf(duration) );
        FileUtils.deleteFile(mTXRecordResult.coverPath);
        intent.putExtra(TCConstants.VIDEO_RECORD_TYPE, TCConstants.VIDEO_RECORD_TYPE_UGC_RECORD);
        intent.putExtra(TCConstants.VIDEO_EDITER_PATH, mTXRecordResult.videoPath);
        intent.putExtra(TCConstants.VIDEO_RECORD_COVERPATH, mTXRecordResult.coverPath);
        intent.putExtra(TCConstants.VIDEO_RECORD_RESOLUTION, mRecordResolution);
        startActivity(intent);
        finish();
    }


    @Override
    public void onRecordEvent(int event, Bundle param){
        TXCLog.d(TAG, "onRecordEvent event id = " + event);
        if(event == TXRecordCommon.EVT_ID_PAUSE){
            mRecordProgressView.clipComplete();
        }else if(event == TXRecordCommon.EVT_CAMERA_CANNOT_USE){
            Toast.makeText(this, "摄像头打开失败，请检查权限", Toast.LENGTH_SHORT).show();
        }else if(event == TXRecordCommon.EVT_MIC_CANNOT_USE){
            Toast.makeText(this, "麦克风打开失败，请检查权限", Toast.LENGTH_SHORT).show();
        }else if(event == TXRecordCommon.EVT_ID_RESUME){

        }
    }


    @Override
    public void onRecordProgress(long milliSecond){
        TXCLog.i(TAG, "onRecordProgress, mRecordProgressView = " + mRecordProgressView);
        if(mRecordProgressView == null){
            return;
        }
        mRecordProgressView.setProgress((int)milliSecond);
        float timeSecondFloat = milliSecond / 1000f;
        int timeSecond = Math.round(timeSecondFloat);
        tv_progress_time.setText(String.format(Locale.CHINA, "00:%02d", timeSecond));
        if(timeSecondFloat < mMinDuration / 1000){
            iv_confirm.setImageResource(R.drawable.ugc_confirm_disable);
            iv_confirm.setEnabled(false);
        }else{
            iv_confirm.setImageResource(R.drawable.selector_record_confirm);
            iv_confirm.setEnabled(true);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        /** attention to this below ,must add this**/
        if(resultCode == RESULT_OK){//是否选择，没选择就不会继续
            if(requestCode == mAudioCtrl.REQUESTCODE){
                if(data == null){
                    Log.e(TAG, "null data");
                }else{
                    Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
                    if(mAudioCtrl != null){
                        mAudioCtrl.processActivityResult(uri);
                    }else{
                        Log.e(TAG, "NULL Pointer! Get Music Failed");
                    }
                }
            }
        }
    }


    @Override
    public void onRecordComplete(TXRecordCommon.TXRecordResult result){
        mCustomProgressDialog.dismiss();

        mTXRecordResult = result;

        TXCLog.i(TAG,
                 "onRecordComplete, result retCode = " + result.retCode + ", descMsg = " + result.descMsg + ", videoPath + " + result
                         .videoPath + ", coverPath = " + result.coverPath);
        if(mTXRecordResult.retCode < 0){
            ImageView liveRecord = findViewById(R.id.record);
            if(liveRecord != null) liveRecord.setBackgroundResource(R.drawable.start_record);
            mRecording = false;

            int timeSecond = mTXCameraRecord.getPartsManager().getDuration() / 1000;
            tv_progress_time.setText(String.format(Locale.CHINA, "00:%02d", timeSecond));
            Toast.makeText(TCVideoRecordActivity.this.getApplicationContext(), "录制失败，原因：" + mTXRecordResult.descMsg, Toast.LENGTH_SHORT).show();
        }else{
            mDuration = mTXCameraRecord.getPartsManager().getDuration();
            if(mTXCameraRecord != null){
                mTXCameraRecord.getPartsManager().deleteAllParts();
            }
            if(mNeedEditer){
                startEditVideo();
            }else{
                startPreview();
            }
        }
    }


    private void requestAudioFocus(){
        if(null == mAudioManager){
            mAudioManager = (AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        }

        if(null == mOnAudioFocusListener){
            mOnAudioFocusListener = new AudioManager.OnAudioFocusChangeListener(){

                @Override
                public void onAudioFocusChange(int focusChange){
                    try{
                        TXCLog.i(TAG, "requestAudioFocus, onAudioFocusChange focusChange = " + focusChange);

                        if(focusChange == AudioManager.AUDIOFOCUS_LOSS){
                            pauseRecord();
                        }else if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT){
                            pauseRecord();
                        }else if(focusChange == AudioManager.AUDIOFOCUS_GAIN){

                        }else{
                            pauseRecord();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            };
        }
        try{
            mAudioManager.requestAudioFocus(mOnAudioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    private void abandonAudioFocus(){
        try{
            if(null != mAudioManager && null != mOnAudioFocusListener){
                mAudioManager.abandonAudioFocus(mOnAudioFocusListener);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void onBeautyParamsChange(BeautySettingPanel.BeautyParams params, int key){
        switch(key){
            case BeautySettingPanel.BEAUTYPARAM_BEAUTY:
                mBeautyParams.mBeautyLevel = params.mBeautyLevel;
                if(mTXCameraRecord != null){
                    mTXCameraRecord.setBeautyDepth(mBeautyParams.mBeautyStyle, mBeautyParams.mBeautyLevel, mBeautyParams.mWhiteLevel,
                                                   mBeautyParams.mRuddyLevel);
                }
                break;
            case BeautySettingPanel.BEAUTYPARAM_WHITE:
                mBeautyParams.mWhiteLevel = params.mWhiteLevel;
                if(mTXCameraRecord != null){
                    mTXCameraRecord.setBeautyDepth(mBeautyParams.mBeautyStyle, mBeautyParams.mBeautyLevel, mBeautyParams.mWhiteLevel,
                                                   mBeautyParams.mRuddyLevel);
                }
                break;
            case BeautySettingPanel.BEAUTYPARAM_FACE_LIFT:
                mBeautyParams.mFaceSlimLevel = params.mFaceSlimLevel;
                if(mTXCameraRecord != null){
                    mTXCameraRecord.setFaceScaleLevel(params.mFaceSlimLevel);
                }
                break;
            case BeautySettingPanel.BEAUTYPARAM_BIG_EYE:
                mBeautyParams.mBigEyeLevel = params.mBigEyeLevel;
                if(mTXCameraRecord != null){
                    mTXCameraRecord.setEyeScaleLevel(params.mBigEyeLevel);
                }
                break;
            case BeautySettingPanel.BEAUTYPARAM_FILTER:
                mBeautyParams.mFilterBmp = params.mFilterBmp;
                if(mTXCameraRecord != null){
                    mTXCameraRecord.setFilter(params.mFilterBmp);
                }
                break;
            case BeautySettingPanel.BEAUTYPARAM_MOTION_TMPL:
                mBeautyParams.mMotionTmplPath = params.mMotionTmplPath;
                if(mTXCameraRecord != null){
                    mTXCameraRecord.setMotionTmpl(params.mMotionTmplPath);
                }
                break;
            case BeautySettingPanel.BEAUTYPARAM_GREEN:
                mBeautyParams.mGreenFile = params.mGreenFile;
                if(mTXCameraRecord != null){
                    mTXCameraRecord.setGreenScreenFile(params.mGreenFile, true);
                }
                break;
            case BeautySettingPanel.BEAUTYPARAM_RUDDY:
                mBeautyParams.mRuddyLevel = params.mRuddyLevel;
                if(mTXCameraRecord != null){
                    mTXCameraRecord.setBeautyDepth(mBeautyParams.mBeautyStyle, mBeautyParams.mBeautyLevel, mBeautyParams.mWhiteLevel,
                                                   mBeautyParams.mRuddyLevel);
                }
                break;
            case BeautySettingPanel.BEAUTYPARAM_BEAUTY_STYLE:
                if(mTXCameraRecord != null){
                    mTXCameraRecord.setBeautyStyle(params.mBeautyStyle);
                }
                break;
            case BeautySettingPanel.BEAUTYPARAM_FACEV:
                if(mTXCameraRecord != null){
                    mTXCameraRecord.setFaceVLevel(params.mFaceVLevel);
                }
                break;
            case BeautySettingPanel.BEAUTYPARAM_FACESHORT:
                if(mTXCameraRecord != null){
                    mTXCameraRecord.setFaceShortLevel(params.mFaceShortLevel);
                }
                break;
            case BeautySettingPanel.BEAUTYPARAM_CHINSLIME:
                if(mTXCameraRecord != null){
                    mTXCameraRecord.setChinLevel(params.mChinSlimLevel);
                }
                break;
            case BeautySettingPanel.BEAUTYPARAM_NOSESCALE:
                if(mTXCameraRecord != null){
                    mTXCameraRecord.setNoseSlimLevel(params.mNoseScaleLevel);
                }
                break;
            case BeautySettingPanel.BEAUTYPARAM_FILTER_MIX_LEVEL:
                if(mTXCameraRecord != null){
                    mTXCameraRecord.setSpecialRatio(params.mFilterMixLevel / 10.f);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 100:
                for(int ret : grantResults){
                    if(ret != PackageManager.PERMISSION_GRANTED){
                        return;
                    }
                }
                startCameraPreview();
                break;
            default:
                break;
        }
    }


    private boolean hasPermission(){
        if(Build.VERSION.SDK_INT >= 23){
            List<String> permissions = new ArrayList<>();
            if(PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)){
                permissions.add(Manifest.permission.CAMERA);
            }
            if(PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)){
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if(permissions.size() != 0){
                ActivityCompat.requestPermissions(this, permissions.toArray(new String[0]), 100);
                return false;
            }
        }

        return true;
    }



    @Override
    protected void onStop(){
        super.onStop();
        if(mTXCameraRecord != null){
            mTXCameraRecord.setVideoProcessListener(null); // 这里要取消监听，否则在上面的回调中又会重新开启预览
            mTXCameraRecord.stopCameraPreview();
            mStartPreview = false;
            // 设置闪光灯的状态为关闭
            if(mIsTorchOpen){
                mIsTorchOpen = false;
                if(mFront){
//                    iv_btn_torch.setImageResource(R.drawable.ugc_torch_disable);
                    iv_btn_torch.setEnabled(false);
                }else{
//                    iv_btn_torch.setImageResource(R.drawable.selector_torch_close);
                    iv_btn_torch.setEnabled(true);
                }
            }
        }
        if(mRecording && !mPause){
            pauseRecord();
        }
        if(mTXCameraRecord != null){
            mTXCameraRecord.pauseBGM();
        }
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();

        TXCLog.i(TAG, "onDestroy");
        if(mRecordProgressView != null){
            mRecordProgressView.release();
        }

        if(mTXCameraRecord != null){
            mTXCameraRecord.stopBGM();
            mTXCameraRecord.stopCameraPreview();
            mTXCameraRecord.setVideoRecordListener(null);
            mTXCameraRecord.getPartsManager().deleteAllParts();
            mTXCameraRecord.release();
            mTXCameraRecord = null;
            mStartPreview = false;
        }
        abandonAudioFocus();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);

        onActivityRotation();
        if(mTXCameraRecord != null){
            mTXCameraRecord.stopCameraPreview();
        }

        if(mRecording && !mPause){
            pauseRecord();
        }
        if(mTXCameraRecord != null){
            mTXCameraRecord.pauseBGM();
        }
        mStartPreview = false;

        startCameraPreview();
    }


    public interface OnItemClickListener{
        void onBGMSelect(String path);
    }

    public interface OnSpeedItemClickListener{
        void onRecordSpeedItemSelected(int pos);
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent){
        if(view == fl_mask){
            if(motionEvent.getPointerCount() >= 2){
                mScaleGestureDetector.onTouchEvent(motionEvent);
            }else if(motionEvent.getPointerCount() == 1){
                mGestureDetector.onTouchEvent(motionEvent);
            }
        }

        return view != mVideoRecordBtn;
    }


    // OnGestureListener回调start
    @Override
    public boolean onDown(MotionEvent motionEvent){
        return false;
    }


    @Override
    public void onShowPress(MotionEvent motionEvent){

    }


    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent){
        if(mBeautyPanelView.isShown()){
//            iv_filter_effects.setImageResource(R.drawable.ugc_record_beautiful_girl);

            mBeautyPanelView.setVisibility(GONE);
            mVideoRecordBtn.setVisibility(View.VISIBLE);
            rl_record.setVisibility(View.VISIBLE);
            ll_camera_type.setVisibility(View.VISIBLE);
        }
        if(mAudioCtrl.isShown()){
//            iv_music_panel.setImageResource(R.drawable.ugc_record_music);

            mAudioCtrl.setVisibility(GONE);

            if(cameraTypeStr[1].equals(typeStr)){
                mVideoRecordBtn.setVisibility(View.VISIBLE);
                mClickToRecordView.setVisibility(GONE);
            }else if(cameraTypeStr[1].equals(typeStr)){
                mVideoRecordBtn.setVisibility(GONE);
                mClickToRecordView.setVisibility(View.VISIBLE);
            }

            rl_record.setVisibility(View.VISIBLE);
            ll_camera_type.setVisibility(View.VISIBLE);
        }

        if(countdown_panel.isShown()){
            countdown_panel.setVisibility(GONE);
            rl_back_next.setVisibility(View.VISIBLE);
            rl_camera_function.setVisibility(View.VISIBLE);

            mRadioGroup.setVisibility(View.VISIBLE);

            layout_record_btns.setVisibility(View.VISIBLE);

            mVideoRecordBtn.setVisibility(View.VISIBLE);
            rl_record.setVisibility(View.VISIBLE);
            ll_camera_type.setVisibility(View.VISIBLE);
        }

        return true;
    }


    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1){
        return false;
    }


    @Override
    public void onLongPress(MotionEvent motionEvent){

    }


    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1){
        return false;
    }
    // OnGestureListener回调end


    // OnScaleGestureListener回调start
    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector){
        int maxZoom = mTXCameraRecord.getMaxZoom();
        if(maxZoom == 0){
            TXCLog.i(TAG, "camera not support zoom");
            return false;
        }

        float factorOffset = scaleGestureDetector.getScaleFactor() - mLastScaleFactor;

        mScaleFactor += factorOffset;
        mLastScaleFactor = scaleGestureDetector.getScaleFactor();
        if(mScaleFactor < 0){
            mScaleFactor = 0;
        }
        if(mScaleFactor > 1){
            mScaleFactor = 1;
        }

        int zoomValue = Math.round(mScaleFactor * maxZoom);
        mTXCameraRecord.setZoom(zoomValue);
        return false;
    }


    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector){
        mLastScaleFactor = scaleGestureDetector.getScaleFactor();
        return true;
    }


    @Override
    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector){

    }
    // OnScaleGestureListener回调end


    private void back(){
        if(!mRecording){
            finish();
        }
        if(mPause){
            if(mTXCameraRecord != null){
                mTXCameraRecord.getPartsManager().deleteAllParts();
            }
            finish();
        }else{
            pauseRecord();
        }
    }


    @Override
    public void onBackPressed(){
        back();
    }
}
