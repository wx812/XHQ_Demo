package com.xhq.demo.tools;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

public class AnimationUtil{
	public static final int SLIDE_UP = 1;
	public static final int SLIDE_DOWN = 2;
	public static final int ZOOM_IN = 3;
	public static final int ZOOM_OUT = 4;
	public static final int SHAKE = 5;
	public static AnimationSet mAnimationSet;
	public static TranslateAnimation mTranAnim;
	public static Animation mAnimation;

	public static void startAnim(Context context, View view, int type) {

		if (null != mTranAnim && SHAKE != type) {
			mTranAnim.cancel();
			if (null != mAnimation) {
				mAnimation.cancel();
			}
		}

		switch (type) {
		case SLIDE_UP:
			mTranAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
			        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
			        Animation.RELATIVE_TO_SELF, -0.1f);
			mTranAnim.setFillAfter(true);
			mTranAnim.setDuration(300);
			if(null != view) view.startAnimation(mTranAnim);
			break;

		case SLIDE_DOWN:
			mTranAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
			        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
			        Animation.RELATIVE_TO_SELF, 0.1f);
			mTranAnim.setFillAfter(true);
			mTranAnim.setDuration(300);
			if(null != view) view.startAnimation(mTranAnim);
			break;

		case ZOOM_IN:

			break;

		case ZOOM_OUT:

			break;

		case SHAKE:
//			mAnimation = AnimationUtils.loadAnimation(context, R.anim.shake);
			mAnimation.setDuration(1000);
			if(null != view) view.startAnimation(mAnimation);

			break;

		default:
			break;
		}

	}

}
