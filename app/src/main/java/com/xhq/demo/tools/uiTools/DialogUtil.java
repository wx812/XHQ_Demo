package com.xhq.demo.tools.uiTools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.Window;

import com.xhq.demo.R;


/**
 *
 * @author mazhuang
 * @date 2017/6/12
 */

public class DialogUtil{

    private DialogUtil() {}

    private static AlertDialog sDialog;

    public static void startLoading(Context context) {
        if (sDialog != null && sDialog.isShowing()) {
            return;
        }

        if (sDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setView(R.layout.window_loading)
                    .setCancelable(false);
            sDialog = builder.create();
            Window window = sDialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
        sDialog.show();
    }

    public static void stopLoading() {
        if (sDialog != null && sDialog.isShowing()) {
            sDialog.dismiss();
            sDialog = null;
        }
    }

    public static boolean isLoading() {
        return sDialog != null && sDialog.isShowing();
    }
}
