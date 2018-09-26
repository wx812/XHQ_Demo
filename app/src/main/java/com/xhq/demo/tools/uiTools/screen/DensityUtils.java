package com.xhq.demo.tools.uiTools.screen;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.xhq.demo.tools.appTools.AppUtils;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/9/1.
 *     Desc  : Auxiliary class for unit conversion
 *     Updt	 : Description
 * </pre>
 */
public class DensityUtils{

	private DensityUtils() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}


	/**
	 * @return screen density
	 */
	public static int getDensityDpi(){
		Context ctx = AppUtils.getAppContext();
		DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
//		WindowManager wm = (WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE);
//		DisplayMetrics dm = new DisplayMetrics();
//		wm.getDefaultDisplay().getMetrics(dm);
		return dm.densityDpi;
	}


	/**
	 * 另一种方式 转换 非标准单位 为 px单位<br>
	 * 标准单位: px (px是安卓系统内部使用的单位, dp是与设备无关的尺寸单位 ) 非标准单位: dp, in, mm, pt, sp.<p>
	 * TypedValue.applyDimension(unit, value, metrics)方法的功能就是把非标准尺寸转换成标准尺寸, 如:<br>
	 * <ul>
	 * <li>
	 * dp->px: ...(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics());
	 * <li>
	 * in->px: ...(TypedValue.COMPLEX_UNIT_IN, 20, ...);
	 * <li>
	 * mm->px: ...(TypedValue.COMPLEX_UNIT_MM, 20, ...);
	 * <li>
	 * pt->px: ...(TypedValue.COMPLEX_UNIT_PT, 20, ...);
	 * <li>
	 * sp->px: ...(TypedValue.COMPLEX_UNIT_SP, 20, ...);
	 * </ul>
	 *
	 * @param unit the unit of converted value
	 * @param sourceValue source value need to be converted
	 * @return expected px value
	 */
	private static int convertUnit(int unit, float sourceValue){
		DisplayMetrics dm = AppUtils.getResources().getDisplayMetrics();
		return (int)TypedValue.applyDimension(unit, sourceValue, dm);
	}


	/**
	 * see {@link #convertUnit(int, float)}
	 */
	public static int dp2Px(float dpValue){
		return convertUnit(TypedValue.COMPLEX_UNIT_DIP, dpValue);
	}

	@Deprecated
	public static int dpTopx(float dpValue) {
		final float scale = AppUtils.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}


	/**
	 * px converted to dp
	 *
	 * @param pxValue px value
	 * @return dp value
	 */
	public static int px2dp(float pxValue) {
		final float scale = AppUtils.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}


	/**
	 * px converted to sp
	 *
	 * @param pxValue px value
	 * @return sp value
	 */
	public static int px2sp(float pxValue) {
		final float fontScale = AppUtils.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	@Deprecated
	public static int spTopx(float spValue) {
		final float fontScale = AppUtils.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}


	/**
	 * see {@link #convertUnit(int, float)}
	 */
	public static int sp2px(float spValue) {
		return convertUnit(TypedValue.COMPLEX_UNIT_SP, spValue);
	}
}