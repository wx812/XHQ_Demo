package com.xhq.demo.tools.uiTools;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.xhq.demo.tools.appTools.AppUtils;

/**
 * 避免输入法面板遮挡
 * <p>在manifest.xml中activity中设置</p>
 * <p>android:windowSoftInputMode="adjustPan"</p>
 *
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/6/11.
 *     Desc  : Soft keyboard Tools.
 *     Updt  : Description.
 * </pre>
 *
 * @author xhq
 * @author Blankj
 */
public class KeyboardUtil{

    public static InputMethodManager getInputMethodMgr(){
        Context ctx = AppUtils.getAppContext();
        return (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * get string when enter key of the keyboard
     *
     * @param et editText
     * @return content of editText
     */
    public static String getStrEnterKey(EditText et){
        final String[] enter_content = new String[1];
        et.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
                    && KeyEvent.ACTION_DOWN == event.getAction())){
                enter_content[0] = v.getText().toString().trim();
            }
            return false;
        });

        return enter_content[0];
    }


    /**
     * 动态隐藏软键盘
     *
     * @param view 视图
     */
    public static void hideSoftInput(View view){
        InputMethodManager imm = getInputMethodMgr();
        if(imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }


    /**
     * 动态显示软键盘
     */
    public static void showSoftInput(View view){
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager imm = getInputMethodMgr();
        if(imm == null) return;
        imm.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }



    /**
     * 动态隐藏软键盘
     *
     * @param act activity
     */
    public static void hideSoftInput(Activity act){
        View view = act.getCurrentFocus();
        if(view == null) view = new View(act);
        InputMethodManager imm = (InputMethodManager)act.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }



    /**
     * open Soft keyboard
     *
     * @param et EditText
     */
    public static void openKeybord(EditText et){
        InputMethodManager imm = getInputMethodMgr();
        if(imm == null) return;
        imm.showSoftInput(et, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    /**
     * This method toggles the input method window display
     * @param showFlags 0 or 1 or 2 ({@link InputMethodManager#RESULT_UNCHANGED_SHOWN} or
     * {@link InputMethodManager#SHOW_IMPLICIT}) or {@link InputMethodManager#SHOW_FORCED}
     * @param hideFlags 0 or 1 or 2 ({@link InputMethodManager#RESULT_UNCHANGED_SHOWN} or
     * {@link InputMethodManager#HIDE_IMPLICIT_ONLY} or {@link InputMethodManager#HIDE_NOT_ALWAYS} )
     */
    public static void toggleSoftInput(int showFlags, int hideFlags){
        InputMethodManager imm = getInputMethodMgr();
        if(imm == null) return;
//        imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN, InputMethodManager.HIDE_NOT_ALWAYS);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_UNCHANGED_SHOWN);
//        imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN, InputMethodManager.SHOW_FORCED);
        imm.toggleSoftInput(showFlags, hideFlags);
    }


    /**
     * 点击屏幕空白区域隐藏软键盘
     * <p>根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘</p>
     * <p>需重写dispatchTouchEvent</p>
     * <p>参照以下注释代码</p>
     */
    public static void clickBlankArea2HideSoftInput(){
        Log.d("tips", "U should copy the following code.");
        /*
        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideKeyboard(v, ev)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
            return super.dispatchTouchEvent(ev);
        }

        // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
        private boolean isShouldHideKeyboard(View v, MotionEvent event) {
            if (v != null && (v instanceof EditText)) {
                int[] l = {0, 0};
                v.getLocationInWindow(l);
                int left = l[0],
                        top = l[1],
                        bottom = top + v.getHeight(),
                        right = left + v.getWidth();
                return !(event.getX() > left && event.getX() < right
                        && event.getY() > top && event.getY() < bottom);
            }
            return false;
        }
        */
    }

}
