package com.xhq.demo.base.ui;

import android.app.Fragment;
import android.util.Log;

/**
 * 重点就是在setUserVisibleHint(boolean)中加上isResume()过滤，在onResume()和onPause()中加上getUserVisibleHint()过滤

 有同学该担心了，你上面不是说在普通使用方式中setUserVisibleHint(boolean)压根就不会被调用，那么意味着getUserVisibleHint()一直都是false，最终onResume()和onPause()中的代码根本就不会执行了，这样普通使用方式就不行了啊。

 我要告诉大家的是大家的是Fragment的源码中明确显示，getUserVisibleHint()方法返回的是mUserVisibleHint字段的值，而mUserVisibleHint字段默认值是true

 如果你需要ViewPager嵌套ViewPager，那么你还会遇到Fragment的mUserVisibleHint属性不同步的问题，请查看下一篇文章来解决
 下一篇：[【Android】解决ViewPager嵌套时Fragment的mUserVisibleHint属性不同步的问题](http://www.jianshu.com/p/e7449278e33d)

 *
 *
 *
 *
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/11/16.
 *     Desc  : Description.
 *     Updt  : Description.
 * </pre>
 */
public class MyFragment extends Fragment {
    private String pageName;

    public MyFragment() {
        pageName = getClass().getSimpleName();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getUserVisibleHint()){
            onVisibilityChangedToUser(true, false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if(getUserVisibleHint()){
            onVisibilityChangedToUser(false, false);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isResumed()){
            onVisibilityChangedToUser(isVisibleToUser, true);
        }
    }

    /**
     * 当Fragment对用户的可见性发生了改变的时候就会回调此方法
     * @param isVisibleToUser true：用户能看见当前Fragment；false：用户看不见当前Fragment
     * @param isHappenedInSetUserVisibleHintMethod true：本次回调发生在setUserVisibleHintMethod方法里；false：发生在onResume或onPause方法里
     */
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean isHappenedInSetUserVisibleHintMethod){
        if(isVisibleToUser){
            if(pageName != null){
//                MobclickAgent.onPageStart(pageName);
                Log.i("UmengPageTrack", pageName + " - display - "+(isHappenedInSetUserVisibleHintMethod?"setUserVisibleHint":"onResume"));
            }
        }else{
            if(pageName != null){
//                MobclickAgent.onPageEnd(pageName);
                Log.w("UmengPageTrack", pageName + " - hidden - "+(isHappenedInSetUserVisibleHintMethod?"setUserVisibleHint":"onPause"));
            }
        }
    }
}

