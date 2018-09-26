package com.xhq.demo.tools.uiTools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.xhq.demo.R;
import com.xhq.demo.tools.appTools.AppUtils;

import java.util.Objects;


/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/12/7.
 *     Desc  : dialog tools.
 *     Updt  : Description.
 * </pre>
 */
public class DialogUtils{


    /**
     * get Screen Width(px)
     */
    public static int getScreenW(){
        Resources res = AppUtils.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        return dm.widthPixels;
    }


    /**
     * get Screen Height(px) 这个高度表示…屏幕有效的高度
     * (getScreenH的高度) = DecorView的高度去掉导航栏的高度
     */
    public static int getScreenH(){
        Resources res = AppUtils.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        return dm.heightPixels;
    }


    /**
     * get dialog width
     *
     * @param offset 相对于屏幕的宽度偏移量
     * @return dialog width
     */
    public static int getDialogWidthByScreen(int offset){
        return getScreenW() - offset;
    }


    public static void setWindowAttributes(Dialog d){
        Window window = d.getWindow();
        WindowManager.LayoutParams lp= Objects.requireNonNull(window).getAttributes();
        lp.alpha=0.5f;
        window.setAttributes(lp);
    }

    /*
     * 将对话框的大小按屏幕大小的百分比设置
     */
    public static void setDialog(Dialog d){
        Window window = d.getWindow();
//        WindowManager m = window.getWindowManager();
//        Display display = m.getDefaultDisplay();
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.width = (int)(getScreenW() * 0.65); // 宽度设置为屏幕的0.95
        p.height = (int)(getScreenH() * 0.6); // 高度设置为屏幕的0.6
        window.setAttributes(p);
    }


    /** Not global warming, it's global change warning. */
    public static Dialog buildGlobalChangeWarningDialog(final Context ctx, @StringRes int titleResId,
                                                        final Runnable positiveAction) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(titleResId);
        builder.setMessage(R.string.global_change_warning);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                positiveAction.run();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }


    // Create (or recycle existing) and show disconnect dialog.
    public static AlertDialog showDisconnectDialog(Context context, android.app.AlertDialog dialog,
                                                   DialogInterface.OnClickListener disconnectListener, CharSequence title,
                                                   CharSequence message) {
        if (dialog == null) {
            dialog = new android.app.AlertDialog.Builder(context)
                    .setPositiveButton(android.R.string.ok, disconnectListener)
                    .setNegativeButton(android.R.string.cancel, null).create();
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            // use disconnectListener for the correct profile(s)
            CharSequence okText = context.getText(android.R.string.ok);
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, okText, disconnectListener);
        }
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
        return dialog;
    }



    private void showErrorDialog(Context ctx, String msg, @StyleRes int styleResId){
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(ctx, styleResId);
        normalDialog.setMessage(msg);
        normalDialog.setCancelable(false);
        normalDialog.setPositiveButton("知道了", null);
        normalDialog.show();
    }


    	/**
	 * 创建自定义ProgressDialog
	 *
	 * @param context
	 * @return
	 */
	public static void showProgressDialog(Context context) {

		LayoutInflater inflater = LayoutInflater.from(context);
//		View v = inflater.inflate(R.layout.layout_loading_dialog, null);        // 得到加载view
//		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);  // 加载布局
//		loadingDialog = new Dialog(context, R.style.loading_dialog);            // 创建自定义样式dialog
//		loadingDialog.setCancelable(true);                                    // 可以用"返回键"取消
//		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT));
//		loadingDialog.show();
	}


    private void getGeneralDialog(Context context, @StyleRes int styleResId, @LayoutRes int layoutResId){

        if(styleResId == 0){
//            styleResId = R.style.dialog;
        }

        AlertDialog mOperationDialog = new AlertDialog.Builder(context, styleResId).create();
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(layoutResId, null);
        mOperationDialog.setView(view);
        mOperationDialog.setCancelable(false);
        mOperationDialog.show();
    }


    public static void hideProgressDialog(Dialog dialog) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }


    /**
     * 弹出更新dialog
     */
//	public static void showUpdateDialog(final Context context, final VersionEntity version) {
//		ObdDialog tipsDialog = new ObdDialog(context, true, false, ObdDialog.TWO_BUTTON)
//				.setTitle("发现新版本"+version.getVs_no())
//				.setMessage(context.getResources().getString(R.string.update_content))
//				.setNegativeButton(context.getResources().getString(R.string.app_update_cancel), new ObdDialog.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//					}
//
//				})
//				.setPositiveButton(context.getResources().getString(R.string.app_update_ok), new ObdDialog.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						Utils.openUrl(context, ApiConfig.HOST_URL+version.getVs_uri());
//						dialog.dismiss();
//					}
//				});
//
//		tipsDialog.setCancelable(false);
//		if (!((Activity) context).isFinishing()) {
//			tipsDialog.show();
//		}
//	}


    public static void showMessageOKCancel(final Context context, String message, DialogInterface.OnClickListener
            okListener) {
        new android.app.AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("确定", okListener)
                .setNegativeButton("取消", (dialog, which) -> {
                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }
                })
                .create()
                .show();
    }

    public static ProgressDialog proDialog = null;
    public static void showProgressDialog(final Context context, String msg){
        proDialog = android.app.ProgressDialog.show(context, "", msg);
    }

    public static void hideProgressDialog(){
        if(proDialog != null){
            if(proDialog.isShowing()){
                proDialog.dismiss();
                proDialog = null;
            }
        }
    }


}
