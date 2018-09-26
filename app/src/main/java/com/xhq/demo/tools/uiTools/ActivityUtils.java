package com.xhq.demo.tools.uiTools;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.ArrayMap;

import com.xhq.demo.tools.IntentUtils;
import com.xhq.demo.tools.appTools.AppUtils;
import com.xhq.demo.tools.dateTimeTools.DateTimeUtils;
import com.xhq.demo.tools.fileTools.StorageUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/9/23
 *     desc  : Activity相关工具类
 * </pre>
 */
public class ActivityUtils {



//    public <T extends View> T $(int id) {
//        return (T) super.findViewById(id);
//    }
//
//    public <T extends View> T $(View view, int id) {
//        return (T) view.findViewById(id);
//    }


//    下面的代码实现的是当我点击 addView 按钮时添加 view：
//    LayoutInflater inflater= (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
//    view=inflater.inflate(R.layout.other_layout,null);
//    mainLayout.addView(view);
//    这个 view 是添加在 main layout下面的。我想把 view 添加在 addView 按钮的右下边，
//      removeView 按钮的上面，但是不在 main Layout 的底部。
//    如何实现？
//
//    RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//  ViewGroup.LayoutParams.WRAP_CONTENT);
//
//rl .addRule(RelativeLayout.POSITION_BELOW, R.id.btnAdd);
//rl .addRule(RelativeLayout.POSITION_TO_RIGHT, R.id.btnAdd);
//
//rl .addRule(RelativeLayout.POSITION_ABOVE, R.id.btnRemove);
//mainLayout.addView(view,rl);




    /**
     * 浏览器下载文件
     *
     * @param
     */
    public static void openUrl(Context context, String url){
        if(TextUtils.isEmpty(url)){
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri downUrl = Uri.parse(url);
        intent.setData(downUrl);
        try{
            context.startActivity(intent);
        }catch(Exception e){
            String className = "com.android.browser.BrowserActivity";
            intent.setClassName("com.android.browser", className);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }


    /**
     * open the camera and taking pictures
     *
     * @param act activity
     * @param type picture type
     * @return picture uri address
     */
    public static String openCameraAndTakePicture(Activity act, int type){

        //只能识别jpg格式的

        String imgFileUrl;
        File cacheDir = StorageUtils.getAppCacheDir("");
        String imgFileName = "img-" + DateTimeUtils.getCurrDateTime_yMdHms() + ".jpg";

        File imageFile = new File(cacheDir, imgFileName);
        imgFileUrl = imageFile.toString();
        try{
            if(imageFile.exists()) imageFile.delete();
            imageFile.createNewFile();
        }catch(IOException e){
            e.printStackTrace();
        }

        //启动相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            Uri imageUri = FileProvider.getUriForFile(act, "com.jmzw", imageFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            act.startActivityForResult(intent, type);
            return imgFileUrl;
        }else{
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            act.startActivityForResult(intent, type);
            return imgFileUrl;
        }
    }


    /**
     * 功能简述: 4.4及以上拍照后裁剪图片
     * uri拍照获取的图片的uri
     */
    public void cropImageUriAfterKikat(Fragment fragment, Uri uri){

        final String IMGPATH = StorageUtils.getExStoragePubDir(Environment.DIRECTORY_DCIM).getPath();
        final String CROP_IMAGE_FILE_NAME = "crop.jpg";           //用于保存裁剪后图片
        final int SET_ALBUM_PICTURE_KITKAT = 40;
        int mSize = 0;  //区分截图大小

        // 4.4 以下 使用
//        Intent intent = new Intent(Intent.ACTION_PICK, null);
        Intent intent = new Intent("com.android.camera.action.CROP");
        //源文件uri
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        if(mSize == 1){
            intent.putExtra("outputX", 600);
            intent.putExtra("outputY", 600);
        }else{
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
        }

        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false);
        //目标文件uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(IMGPATH, CROP_IMAGE_FILE_NAME)));
        intent.putExtra("noFaceDetection", true);
        if(fragment != null){
            fragment.startActivityForResult(intent, SET_ALBUM_PICTURE_KITKAT);
        }else{
            fragment.getActivity().startActivityForResult(intent, SET_ALBUM_PICTURE_KITKAT);
        }
    }


    /**
     * 功能简述:4.4以上从相册选择图片----------------------
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void selectImageUriAfterKikat(Fragment fragment){

        final int SELECT_A_PICTURE_AFTER_KITKAT = 50;
        //系统相册
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.putExtra("return-data", true);

        // 文件目录
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        if(fragment != null){
            fragment.startActivityForResult(intent, SELECT_A_PICTURE_AFTER_KITKAT);
        }else{
            fragment.getActivity().startActivityForResult(intent, SELECT_A_PICTURE_AFTER_KITKAT);
        }
    }


    public static void startActivity(Context ctx, Class<? extends Activity> clazz) {
        Intent intent = new Intent(ctx, clazz);
        ctx.startActivity(intent);
    }


    public static String getLaunchActivityName(String pkgName){
        // 根据PackageInfo对象取不出其中的主Activity，须用Intent
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setPackage(pkgName);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = AppUtils.getPkgMgr().queryIntentActivities(intent, 0);
        // 一个App中只有一个主Activity，直接取出。注意不是任何包中都有主Activity
        String mainActivityName = "";
        if (resolveInfos != null && resolveInfos.size() >= 1) {
            mainActivityName = resolveInfos.get(0).activityInfo.name;
        }
        return mainActivityName;
    }


    /**
     * open the setting details page
     * @param act current activity
     * @param cls eg : "com.android.settings.WirelessSettings"
     */
    public static void openSettingsDetails(Activity act, @NonNull final String cls){
        Intent intent = new Intent("/");
        final String pkgName = "com.android.settings";
        ComponentName cm = new ComponentName(pkgName, cls);
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        act.startActivityForResult(intent, 0);
    }


    public static void openSettings(Context ctx) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + ctx.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }


    /**
     * 判断是否存在Activity
     *
     * @param packageName 包名
     * @param className   activity全路径类名
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isActivityExists(String packageName, String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        Context ctx = AppUtils.getAppContext();
        return !(ctx.getPackageManager().resolveActivity(intent, 0) == null
                || intent.resolveActivity(ctx.getPackageManager()) == null
                || ctx.getPackageManager().queryIntentActivities(intent, 0).size() == 0);
    }

    /**
     * 打开Activity
     *
     * @param packageName 包名
     * @param className   全类名
     */
    public static void launchActivity(String packageName, String className) {
        launchActivity(packageName, className, null);
    }

    /**
     * 打开Activity
     *
     * @param packageName 包名
     * @param className   全类名
     * @param bundle      bundle
     */
    public static void launchActivity(String packageName, String className, Bundle bundle) {
        AppUtils.getAppContext().startActivity(IntentUtils.getComponentIntent(packageName, className, bundle));
    }

    /**
     * 获取launcher activity
     *
     * @param packageName 包名
     * @return launcher activity
     */
    public static String getLauncherActivity(String packageName) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PackageManager pm = AppUtils.getAppContext().getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo info : infos) {
            if (info.activityInfo.packageName.equals(packageName)) {
                return info.activityInfo.name;
            }
        }
        return "no " + packageName;
    }


    /**
     * 获取栈顶Activity
     *
     * @return 栈顶Activity
     */
    public static Activity getTopActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map mapActThread;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                mapActThread = (HashMap) activitiesField.get(activityThread);
            } else {
                mapActThread = (ArrayMap) activitiesField.get(activityThread);
            }
            for (Object activityRecord : mapActThread.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    return (Activity) activityField.get(activityRecord);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
