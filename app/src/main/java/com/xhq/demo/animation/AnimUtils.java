package com.xhq.demo.animation;

import android.animation.Animator;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/7/3.
 *     Desc  : Description.
 *     Updt  : Description.
 * </pre>
 */
public class AnimUtils{

    private ViewGroup clayout; // 父laoyout
    private List<ViewPropertyAnimator> viewAnimators = new ArrayList<>();

    private class AnimListener implements Animator.AnimatorListener{
        private View target;
        private boolean isOpen = false;

        public AnimListener(View _target) {
            target = _target;
        }

        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (!isOpen) {
                target.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }


    public void startAnimationsOut(int durationMillis) {
        for (int i = 0; i < clayout.getChildCount(); i++) {
            final LinearLayout imgButton = (LinearLayout) clayout.getChildAt(i);
            ViewPropertyAnimator viewPropertyAnimator = viewAnimators.get(i);
            viewPropertyAnimator.setListener(null);
            viewPropertyAnimator.x((float) imgButton.getLeft()).y((float) imgButton.getTop());
            viewPropertyAnimator.setListener(new AnimListener(imgButton));
        }
    }


    public static Animation getRotateAnimation(float fromDegrees,
                                               float toDegrees, int durationMillis) {
        RotateAnimation rotate = new RotateAnimation(fromDegrees, toDegrees,
                                                     Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(durationMillis);
        rotate.setFillAfter(true);
        return rotate;
    }
}
