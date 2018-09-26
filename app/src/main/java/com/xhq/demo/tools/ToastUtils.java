package com.xhq.demo.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.xhq.demo.tools.appTools.AppUtils;


/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2016/8/13.
 *     Desc  : Toast tools
 *     Updt  : 2018/5/6.
 * </pre>
 */
public class ToastUtils {

    private static Toast mToast;

    public static void showToast(@NonNull CharSequence msg){
        showTextToast(msg);
    }

    public static void showToastLong(@NonNull CharSequence msg){
        showTextToastLong(msg);
    }

    /**
     * @param resId string id. eg：R.string.resId
     */
    public static void showToast(@StringRes int resId){
        String msg = AppUtils.getResources().getString(resId);
        showTextToast(msg);
    }

    public static void showToastLong(@StringRes int resId){
        String msg = AppUtils.getResources().getString(resId);
        showTextToastLong(msg);
    }


    private static void showTextToast(@NonNull CharSequence msg){
        showText(msg, Toast.LENGTH_SHORT, Gravity.CENTER);
    }

    private static void showTextToastLong(@NonNull CharSequence msg){
        showText(msg, Toast.LENGTH_LONG, Gravity.CENTER);
    }

    /**
     * see {@link #showText(CharSequence, int, int)}
     */
    public static void showToast(@NonNull CharSequence msg, int shortOrLong, int gravity){
        showText(msg, shortOrLong, gravity);
    }

    /**
     * show toast
     * @param msg message
     * @param shortOrLong LENGTH_SHORT or LENGTH_LONG
     * @param gravity Gravity.TOP|Gravity.CENTER|Gravity.BOTTOM
     */
    @SuppressLint("ShowToast")
    private static void showText(CharSequence msg, int shortOrLong, int gravity){
        if(TextUtils.isEmpty(msg)) return;
        if(mToast == null){
            mToast = Toast.makeText(AppUtils.getAppContext(), msg, shortOrLong);
        }else{
            mToast.setText(msg);
            mToast.setDuration(shortOrLong);
        }
        int yOffset = 0;
        if(Gravity.TOP == gravity) yOffset = -50;
        if(Gravity.BOTTOM == gravity) yOffset = 50;
        mToast.setGravity(gravity, 0, yOffset);
        mToast.show();
    }


    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

    public static void showToastShort(final Activity activity, final String msg){
//        Handler sHandler = new Handler(Looper.getMainLooper());
        activity.runOnUiThread(() -> {
            final Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
            toast.show();
            Handler handler = new Handler();
            handler.postDelayed(toast:: cancel, 500);
        });
    }



//    public static void showToast(@NonNull CharSequence text, boolean center) {
//        if (action == null)
//            action = new ToastRunnable();
//        activity = HomeApp.getCurrentActivity();
//        action.setToast(text, center, activity);
//        activity.runOnUiThread(action);
//    }

    public static class ToastRunnable implements Runnable {
        private boolean center;
        private CharSequence text;

        void setToast(CharSequence text, boolean center) {
            this.text = text;
            this.center = center;
        }

        @SuppressLint("ShowToast")
        @Override
        public void run() {
            if (mToast == null) {
                mToast = Toast.makeText(AppUtils.getAppContext(), text, Toast.LENGTH_SHORT);
                if (center)
                    mToast.setGravity(Gravity.CENTER, 0, 0);
                else
                    mToast.setGravity(Gravity.BOTTOM, 0, 50);
            } else {
                mToast.setText(text);
                if (center)
                    mToast.setGravity(Gravity.CENTER, 0, 0);
                else
                    mToast.setGravity(Gravity.BOTTOM, 0, 50);
            }
            mToast.show();
        }

    }



    private void showCustomToast(Activity act, @LayoutRes int resId) {
        LayoutInflater inflater = act.getLayoutInflater();
        View view = inflater.inflate(resId, null);
//        image = (ImageView) view.findViewById(R.id.image);
//        title = (TextView) view.findViewById(R.id.title);
//        content = (TextView) view.findViewById(R.id.content);
//        image.setBackgroundResource(R.drawable.ic_launcher);
//        title.setText("自定义toast");
//        content.setText("hello,self toast");
        Toast toast = new Toast(AppUtils.getAppContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

}
