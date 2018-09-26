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
 *
 */
public class KeyboardUtils{


	/**
	 * get string when enter key of the keyboard
	 * @param et editText
	 * @return content of editText
	 */
	public static String getStrEnterKey(EditText et){
		final String[] enter_content = new String[1];
		et.setOnEditorActionListener((v, actionId, event) -> {
			if(actionId == EditorInfo.IME_ACTION_SEND
					|| actionId == EditorInfo.IME_ACTION_DONE
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
	 * @param context 上下文
	 * @param view    视图
	 */
	public static void hideSoftInput(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm == null) return;
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * open Soft keyboard
	 * 
	 * @param mEditText
	 * @param mContext
	 */
	public static void openKeybord(EditText mEditText, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	/**
	 * close Soft keyboard
	 * 
	 * @param mEditText
	 * @param mContext
	 */
	public static void closeKeybord(EditText mEditText, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
	}
	
	/**
     * 隐藏键盘
     */
    public static void hideKeyBoard(Context context){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 显示键盘
     */
    public static void showKeyBoard(Context context){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

	/**
	 * 动态隐藏软键盘
	 *
	 * @param act activity
	 */
	public static void hideSoftInput(Activity act) {
		View view = act.getCurrentFocus();
		if (view == null) view = new View(act);
		InputMethodManager imm = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
		if (imm == null) return;
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * 切换键盘显示与否状态
	 */
	public static void toggleSoftInput() {
		Context ctx = AppUtils.getAppContext();
		InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm == null) return;
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}


	/**
	 * 动态显示软键盘
	 *
	 * @param edit 输入框
	 */
	public static void showSoftInput(EditText edit) {
		edit.setFocusable(true);
		edit.setFocusableInTouchMode(true);
		edit.requestFocus();
		Context ctx = AppUtils.getAppContext();
		InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm == null) return;
		imm.showSoftInput(edit, 0);
	}

	/**
	 * 点击屏幕空白区域隐藏软键盘
	 * <p>根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘</p>
	 * <p>需重写dispatchTouchEvent</p>
	 * <p>参照以下注释代码</p>
	 */
	public static void clickBlankArea2HideSoftInput() {
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
