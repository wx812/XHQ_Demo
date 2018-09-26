package com.xhq.demo.tools.uiTools;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;
import android.support.v7.view.ActionMode;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xhq.demo.tools.appTools.AppUtils;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/6/20.
 *     Desc  : view tools , ui related
 *     Updt  : Description.
 * </pre>
 */
public class ViewUtils{


    /**
     * 适用于Adapter中简化ViewHolder相关代码
     *
     * @param convertView
     * @param resId
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T obtainView(View convertView, @IdRes int resId) {
        SparseArray<View> holder = (SparseArray<View>) convertView.getTag();
        if (holder == null) {
            holder = new SparseArray<>();
            convertView.setTag(holder);
        }
        View childView = holder.get(resId);
        if (childView == null) {
            childView = convertView.findViewById(resId);
            holder.put(resId, childView);
        }
        return (T) childView;
    }


    /**
     * view转Bitmap
     *
     * @param view 视图
     * @return bitmap
     */
    public static Bitmap view2Bitmap(View view){
        if(view == null) return null;
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if(bgDrawable != null){
            bgDrawable.draw(canvas);
        }else{
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return bitmap;
    }


    public static void showNotification(Context ctx, Intent intent, String title, String body, int
            drawableId){
        NotificationManager nfMgr = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification nf = new NotificationCompat.Builder(ctx)
                .setSmallIcon(drawableId)
                .setContentTitle(title)
                .setWhen(System.currentTimeMillis())
                .setContentText(body)
                .build();
        nf.flags = Notification.FLAG_AUTO_CANCEL;
        nf.defaults = Notification.DEFAULT_SOUND;
        if(intent == null){
            // FIXEME: category tab is disabled
//			intent = new Intent(ctx, FileViewActivity.class);
        }
        int code = 0;
        PendingIntent pIntent = PendingIntent.getActivity(ctx, code, intent, PendingIntent.FLAG_ONE_SHOT);
//		nf.setLatestEventInfo(ctx, title, body, pIntent);
        nfMgr.notify(drawableId, nf);
    }


    public static void setActionModeTitle(ActionMode mode, @StringRes int resStr, int selectedNum){
        if(mode != null){
            Context ctx = AppUtils.getAppContext();
            mode.setTitle(ctx.getString(resStr, selectedNum));
            if(selectedNum == 0){
                mode.finish();
            }
        }
    }


    public static boolean setText(View view, @IdRes int resId, CharSequence text){
        TextView textView = view.findViewById(resId);
        if(textView == null) return false;
        textView.setText(text);
        return true;
    }


    public static boolean setText(View view, @IdRes int resId, @StringRes int resStr){
        TextView textView = view.findViewById(resId);
        if(textView == null) return false;
        textView.setText(resStr);
        return true;
    }


    /**
     * 测量视图尺寸
     *
     * @param view 视图
     * @return arr[0]: 视图宽度, arr[1]: 视图高度
     */
    public static int[] measureView(View view){
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if(lp == null){
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int widthSpec = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        int lpHeight = lp.height;
        int heightSpec;
        if(lpHeight > 0){
            heightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
        }else{
            heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        view.measure(widthSpec, heightSpec);
        return new int[]{view.getMeasuredWidth(), view.getMeasuredHeight()};
    }


    /**
     * when customize view, call onMeasure(), measure the width and height of the view
     * @param whMeasureSpec widthMeasureSpec or heightMeasureSpec
     * @param defValue the default width or height of the view
     * @return Width or height that has been measured
     */
    public int measureViewWH(int whMeasureSpec, int defValue){
        int length = 0;
        int whSize = View.MeasureSpec.getSize(whMeasureSpec);
        int whMode = View.MeasureSpec.getMode(whMeasureSpec);

        switch(whMode){
            case View.MeasureSpec.EXACTLY:
                length = whSize;
                break;
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.UNSPECIFIED:
                length = defValue;
                break;
        }
        return length;
    }



    /**
     * 获取测量视图宽度
     *
     * @param view 视图
     * @return 视图宽度
     */
    public static int getMeasuredWidth(View view){
        return measureView(view)[0];
    }


    /**
     * 获取测量视图高度
     *
     * @param view 视图
     * @return 视图高度
     */
    public static int getMeasuredHeight(View view){
        return measureView(view)[1];
    }


    // 修改整个界面所有控件的字体
    public static void changeFonts(ViewGroup root, String path, Activity act){
        //path是字体路径
        Typeface tf = Typeface.createFromAsset(act.getAssets(), path);
        for(int i = 0; i < root.getChildCount(); i++){
            View v = root.getChildAt(i);
            if(v instanceof TextView){
                ((TextView)v).setTypeface(tf);
            }else if(v instanceof Button){
                ((Button)v).setTypeface(tf);
            }else if(v instanceof EditText){
                ((EditText)v).setTypeface(tf);
            }else if(v instanceof ViewGroup){
                changeFonts((ViewGroup)v, path, act);
            }
        }
    }


    // 修改整个界面所有控件的字体大小
    public static void changeTextSize(ViewGroup root, int size, Activity act){
        for(int i = 0; i < root.getChildCount(); i++){
            View v = root.getChildAt(i);
            if(v instanceof TextView){
                ((TextView)v).setTextSize(size);
            }else if(v instanceof Button){
                ((Button)v).setTextSize(size);
            }else if(v instanceof EditText){
                ((EditText)v).setTextSize(size);
            }else if(v instanceof ViewGroup){
                changeTextSize((ViewGroup)v, size, act);
            }
        }
    }


    // 不改变控件位置，修改控件大小
    public static void changeWH(View v, int W, int H){
        ViewGroup.LayoutParams params = v.getLayoutParams();
        params.width = W;
        params.height = H;
        v.setLayoutParams(params);
    }


    // 修改控件的高
    public static void changeH(View v, int H){
        ViewGroup.LayoutParams params = v.getLayoutParams();
        params.height = H;
        v.setLayoutParams(params);
    }


    /**
     * @param isShown whether to display system ui
     */
    public static void setSystemUIVisibility(boolean isShown){

        String cmd;
        if(isShown){
            cmd = "am startservice -n com.android.systemui/.SystemUIService \n";  //显示
        }else {
            cmd = "service call activity 42 s16 com.android.systemui \n";  //隐藏
        }
        // try - catch 比较消耗性能
        try{
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream dos = new DataOutputStream(p.getOutputStream());
            dos.writeBytes(cmd);
            dos.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
