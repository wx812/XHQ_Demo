package com.xhq.demo.tools.uiTools;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.xhq.demo.tools.appTools.AppUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/1/17
 *     desc  : Fragment相关工具类
 * </pre>
 */
public class FragmentUtils {


    private static final int TYPE_ADD_FRAGMENT       = 0x01;
    private static final int TYPE_CHECK_ADD_FRAGMENT = 0x01 << 1;
    private static final int TYPE_REMOVE_FRAGMENT    = 0x01 << 2;
    private static final int TYPE_REMOVE_TO_FRAGMENT = 0x01 << 3;
    private static final int TYPE_REPLACE_FRAGMENT   = 0x01 << 4;
    private static final int TYPE_POP_ADD_FRAGMENT   = 0x01 << 5;
    private static final int TYPE_HIDE_FRAGMENT      = 0x01 << 6;
    private static final int TYPE_SHOW_FRAGMENT      = 0x01 << 7;
    private static final int TYPE_HIDE_SHOW_FRAGMENT = 0x01 << 8;

    private static final String ARGS_ID           = "args_id";
    private static final String ARGS_IS_HIDE      = "args_is_hide";
    private static final String ARGS_IS_ADD_STACK = "args_is_add_stack";

    /**
     * 新增fragment
     *
     * @param fm fragment管理器
     * @param containerId     布局Id
     * @param frg        frg
     * @return frg
     */
    public static Fragment addFragment(@NonNull FragmentManager fm,
                                       @NonNull Fragment frg,
                                       @IdRes int containerId) {
        return addFragment(fm, frg, containerId, false);
    }

    /**
     * 新增fragment
     *
     * @param fm fragment管理器
     * @param containerId     布局Id
     * @param frg        frg
     * @param isHide          是否隐藏
     * @return frg
     */
    public static Fragment addFragment(@NonNull FragmentManager fm,
                                       @NonNull Fragment frg,
                                       @IdRes int containerId,
                                       boolean isHide) {
        return addFragment(fm, frg, containerId, isHide, false);
    }

    /**
     * 新增fragment
     *
     * @param fm fragment管理器
     * @param containerId     布局Id
     * @param frg        frg
     * @param isHide          是否隐藏
     * @param isAddStack      是否入回退栈
     * @return frg
     */
    public static Fragment addFragment(@NonNull FragmentManager fm,
                                       @NonNull Fragment frg,
                                       @IdRes int containerId,
                                       boolean isHide,
                                       boolean isAddStack) {
        putArgs(frg, new Args(containerId, isHide, isAddStack));
        return operateFragment(fm, null, frg, TYPE_ADD_FRAGMENT);
    }

    /**
     * 新增fragment
     *
     * @param fm fragment管理器
     * @param containerId     布局Id
     * @param frg        frg
     * @param isHide          是否隐藏
     * @param isAddStack      是否入回退栈
     * @return frg
     */
    public static Fragment addFragment(@NonNull FragmentManager fm,
                                       @NonNull Fragment frg,
                                       @IdRes int containerId,
                                       boolean isHide,
                                       boolean isAddStack,
                                       SharedElement... sharedElement) {
        putArgs(frg, new Args(containerId, isHide, isAddStack));
        return operateFragment(fm, null, frg, TYPE_ADD_FRAGMENT, sharedElement);
    }

    /**
     * 新增fragment
     *
     * @param fm fragment管理器
     * @param containerId     布局Id
     * @param frg        frg
     * @param isHide          是否隐藏
     * @param isAddStack      是否入回退栈
     * @return frg
     */
    public static Fragment checkAddFragment(@NonNull FragmentManager fm,
                                            @NonNull Fragment frg,
                                            @IdRes int containerId,
                                            boolean isHide,
                                            boolean isAddStack) {
        putArgs(frg, new Args(containerId, isHide, isAddStack));
        return operateFragment(fm, null, frg, TYPE_CHECK_ADD_FRAGMENT);
    }

    /**
     * 新增fragment
     *
     * @param fm fragment管理器
     * @param containerId     布局Id
     * @param hideFragment    要隐藏的fragment
     * @param addFragment     新增的fragment
     * @param isHide          是否隐藏
     * @param isAddStack      是否入回退栈
     * @return frg
     */
    public static Fragment hideAddFragment(@NonNull FragmentManager fm,
                                           @NonNull Fragment hideFragment,
                                           @NonNull Fragment addFragment,
                                           @IdRes int containerId,
                                           boolean isHide,
                                           boolean isAddStack,
                                           SharedElement... sharedElement) {
        putArgs(addFragment, new Args(containerId, isHide, isAddStack));
        return operateFragment(fm, hideFragment, addFragment, TYPE_ADD_FRAGMENT, sharedElement);
    }

    /**
     * 新增多个fragment
     *
     * @param fm fragment管理器
     * @param frgList       frgList
     * @param containerId     布局Id
     * @param showIndex       要显示的fragment索引
     * @return 要显示的fragment
     */
    public static Fragment addFragments(@NonNull FragmentManager fm,
                                        @NonNull List<Fragment> frgList,
                                        @IdRes int containerId,
                                        int showIndex) {
        for (int i = 0, size = frgList.size(); i < size; ++i) {
            Fragment frg = frgList.get(i);
            if (frg != null) {
                addFragment(fm, frg, containerId, showIndex != i, false);
            }
        }
        return frgList.get(showIndex);
    }

    /**
     * 新增多个fragment
     *
     * @param fm fragment管理器
     * @param frgList       frgList
     * @param containerId     布局Id
     * @param showIndex       要显示的fragment索引
     * @return 要显示的fragment
     */
    public static Fragment addFragments(@NonNull FragmentManager fm,
                                        @NonNull List<Fragment> frgList,
                                        @IdRes int containerId,
                                        int showIndex,
                                        @NonNull List<SharedElement>... lists) {
        for (int i = 0, size = frgList.size(); i < size; ++i) {
            Fragment frg = frgList.get(i);
            List<SharedElement> list = lists[i];
            if (frg != null) {
                if (list != null) {
                    putArgs(frg, new Args(containerId, showIndex != i, false));
                    return operateFragment(fm, null, frg, TYPE_ADD_FRAGMENT, list.toArray(new SharedElement[0]));
                }
            }
        }
        return frgList.get(showIndex);
    }

    /**
     * 移除fragment
     *
     * @param frg frg
     */
    public static void removeFragment(@NonNull Fragment frg) {
        operateFragment(frg.getFragmentManager(), null, frg, TYPE_REMOVE_FRAGMENT);
    }

    /**
     * 移除到指定fragment
     *
     * @param frg      frg
     * @param isIncludeSelf 是否包括Fragment类自己
     */
    public static void removeToFragment(@NonNull Fragment frg, boolean isIncludeSelf) {
        operateFragment(frg.getFragmentManager(), isIncludeSelf ? frg : null, frg, TYPE_REMOVE_TO_FRAGMENT);
    }

    /**
     * 移除同级别fragment
     */
    public static void removeFragments(@NonNull FragmentManager fm) {
        List<Fragment> frgList = getFragments(fm);
        if (frgList.isEmpty()) return;
        for (int i = frgList.size() - 1; i >= 0; --i) {
            Fragment frg = frgList.get(i);
            if (frg != null) removeFragment(frg);
        }
    }

    /**
     * 移除所有fragment
     */
    public static void removeAllFragments(@NonNull FragmentManager fm) {
        List<Fragment> frgList = getFragments(fm);
        if (frgList.isEmpty()) return;
        for (int i = frgList.size() - 1; i >= 0; --i) {
            Fragment frg = frgList.get(i);
            if (frg != null) {
                removeAllFragments(frg.getChildFragmentManager());
                removeFragment(frg);
            }
        }
    }

    /**
     * 替换fragment
     *
     * @param srcFragment  源fragment
     * @param destFragment 目标fragment
     * @param isAddStack   是否入回退栈
     * @return 目标fragment
     */
    public static Fragment replaceFragment(@NonNull Fragment srcFragment,
                                           @NonNull Fragment destFragment,
                                           boolean isAddStack) {
        if (srcFragment.getArguments() == null) return null;
        int containerId = srcFragment.getArguments().getInt(ARGS_ID);
        if (containerId == 0) return null;
        return replaceFragment(srcFragment.getFragmentManager(), destFragment, containerId, isAddStack);
    }

    /**
     * 替换fragment
     *
     * @param srcFragment   源fragment
     * @param destFragment  目标fragment
     * @param isAddStack    是否入回退栈
     * @param sharedElement 共享元素
     * @return 目标fragment
     */
    public static Fragment replaceFragment(@NonNull Fragment srcFragment,
                                           @NonNull Fragment destFragment,
                                           boolean isAddStack,
                                           SharedElement... sharedElement) {
        if (srcFragment.getArguments() == null) return null;
        int containerId = srcFragment.getArguments().getInt(ARGS_ID);
        if (containerId == 0) return null;
        return replaceFragment(srcFragment.getFragmentManager(), destFragment, containerId, isAddStack, sharedElement);
    }

    /**
     * 替换fragment
     *
     * @param fm fragment管理器
     * @param containerId     布局Id
     * @param frg        frg
     * @param isAddStack      是否入回退栈
     * @return frg
     */
    public static Fragment replaceFragment(@NonNull FragmentManager fm,
                                           @NonNull Fragment frg,
                                           @IdRes int containerId,
                                           boolean isAddStack) {
        putArgs(frg, new Args(containerId, false, isAddStack));
        return operateFragment(fm, null, frg, TYPE_REPLACE_FRAGMENT);
    }

    /**
     * 替换fragment
     *
     * @param fm fragment管理器
     * @param containerId     布局Id
     * @param frg        frg
     * @param isAddStack      是否入回退栈
     * @param sharedElement   共享元素
     * @return frg
     */
    public static Fragment replaceFragment(@NonNull FragmentManager fm,
                                           @NonNull Fragment frg,
                                           @IdRes int containerId,
                                           boolean isAddStack,
                                           SharedElement... sharedElement) {
        putArgs(frg, new Args(containerId, false, isAddStack));
        return operateFragment(fm, null, frg, TYPE_REPLACE_FRAGMENT, sharedElement);
    }

    /**
     * 出栈fragment
     *
     * @param fm fragment管理器
     * @return {@code true}: 出栈成功<br>{@code false}: 出栈失败
     */
    public static boolean popFragment(@NonNull FragmentManager fm) {
        return fm.popBackStackImmediate();
    }

    /**
     * 出栈到指定fragment
     *
     * @param fm fragment管理器
     * @param fragmentClass   Fragment类
     * @param isIncludeSelf   是否包括Fragment类自己
     * @return {@code true}: 出栈成功<br>{@code false}: 出栈失败
     */
    public static boolean popToFragment(@NonNull FragmentManager fm,
                                        Class<? extends Fragment> fragmentClass,
                                        boolean isIncludeSelf) {
        return fm.popBackStackImmediate(fragmentClass.getName(), isIncludeSelf ? FragmentManager.POP_BACK_STACK_INCLUSIVE : 0);
    }

    /**
     * 出栈同级别fragment
     *
     * @param fm fragment管理器
     */
    public static void popFragments(@NonNull FragmentManager fm) {
        while (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
        }
    }

    /**
     * 出栈所有fragment
     *
     * @param fm fragment管理器
     */
    public static void popAllFragments(@NonNull FragmentManager fm) {
        List<Fragment> frgList = getFragments(fm);
        if (frgList.isEmpty()) return;
        for (int i = frgList.size() - 1; i >= 0; --i) {
            Fragment frg = frgList.get(i);
            if (frg != null) popAllFragments(frg.getChildFragmentManager());
        }
        while (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
        }
    }

    /**
     * 先出栈后新增fragment
     *
     * @param fm fragment管理器
     * @param containerId     布局Id
     * @param frg        frg
     * @param isAddStack      是否入回退栈
     * @return frg
     */
    public static Fragment popAddFragment(@NonNull FragmentManager fm,
                                          @NonNull Fragment frg,
                                          @IdRes int containerId,
                                          boolean isAddStack) {
        putArgs(frg, new Args(containerId, false, isAddStack));
        return operateFragment(fm, null, frg, TYPE_POP_ADD_FRAGMENT);
    }

    /**
     * 先出栈后新增fragment
     *
     * @param fm fragment管理器
     * @param containerId     布局Id
     * @param frg        frg
     * @param isAddStack      是否入回退栈
     * @return frg
     */
    public static Fragment popAddFragment(@NonNull FragmentManager fm,
                                          @NonNull Fragment frg,
                                          @IdRes int containerId,
                                          boolean isAddStack,
                                          SharedElement... sharedElements) {
        putArgs(frg, new Args(containerId, false, isAddStack));
        return operateFragment(fm, null, frg, TYPE_POP_ADD_FRAGMENT, sharedElements);
    }

    /**
     * 隐藏fragment
     *
     * @param frg frg
     * @return 隐藏的Fragment
     */
    public static Fragment hideFragment(@NonNull Fragment frg) {
        Args args = getArgs(frg);
        if (args != null) {
            putArgs(frg, new Args(args.id, true, args.isAddStack));
        }
        return operateFragment(frg.getFragmentManager(), null, frg, TYPE_HIDE_FRAGMENT);
    }

    /**
     * 隐藏同级别fragment
     *
     * @param fm fragment管理器
     */
    public static void hideFragments(@NonNull FragmentManager fm) {
        List<Fragment> frgList = getFragments(fm);
        if (frgList.isEmpty()) return;
        for (int i = frgList.size() - 1; i >= 0; --i) {
            Fragment frg = frgList.get(i);
            if (frg != null) hideFragment(frg);
        }
    }

    /**
     * 显示fragment
     *
     * @param frg frg
     * @return show的Fragment
     */
    public static Fragment showFragment(@NonNull Fragment frg) {
        Args args = getArgs(frg);
        if (args != null) {
            putArgs(frg, new Args(args.id, false, args.isAddStack));
        }
        return operateFragment(frg.getFragmentManager(), null, frg, TYPE_SHOW_FRAGMENT);
    }

    /**
     * 显示fragment
     *
     * @param frg frg
     * @return show的Fragment
     */
    public static Fragment hideAllShowFragment(@NonNull Fragment frg) {
        hideFragments(frg.getFragmentManager());
        return operateFragment(frg.getFragmentManager(), null, frg, TYPE_SHOW_FRAGMENT);
    }

    /**
     * 先隐藏后显示fragment
     *
     * @param hideFragment 需要隐藏的Fragment
     * @param showFragment 需要显示的Fragment
     * @return 显示的Fragment
     */
    public static Fragment hideShowFragment(@NonNull Fragment hideFragment,
                                            @NonNull Fragment showFragment) {
        Args args = getArgs(hideFragment);
        if (args != null) {
            putArgs(hideFragment, new Args(args.id, true, args.isAddStack));
        }
        args = getArgs(showFragment);
        if (args != null) {
            putArgs(showFragment, new Args(args.id, false, args.isAddStack));
        }
        return operateFragment(showFragment.getFragmentManager(), hideFragment, showFragment, TYPE_HIDE_SHOW_FRAGMENT);
    }

    /**
     * 传参
     *
     * @param frg frg
     * @param args     参数
     */
    private static void putArgs(@NonNull Fragment frg, Args args) {
        Bundle bundle = frg.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            frg.setArguments(bundle);
        }
        bundle.putInt(ARGS_ID, args.id);
        bundle.putBoolean(ARGS_IS_HIDE, args.isHide);
        bundle.putBoolean(ARGS_IS_ADD_STACK, args.isAddStack);
    }

    /**
     * 获取参数
     *
     * @param frg frg
     */
    private static Args getArgs(@NonNull Fragment frg) {
        Bundle bundle = frg.getArguments();
        if (bundle == null || bundle.getInt(ARGS_ID) == 0) return null;
        return new Args(bundle.getInt(ARGS_ID), bundle.getBoolean(ARGS_IS_HIDE), bundle.getBoolean(ARGS_IS_ADD_STACK));
    }

    /**
     * 操作fragment
     *
     * @param fm fragment管理器
     * @param srcFragment     源fragment
     * @param destFragment    目标fragment
     * @param type            操作类型
     * @param sharedElements  共享元素
     * @return destFragment
     */
    private static Fragment operateFragment(@NonNull FragmentManager fm,
                                            Fragment srcFragment,
                                            @NonNull Fragment destFragment,
                                            int type,
                                            SharedElement... sharedElements) {
        if (srcFragment == destFragment) return null;
        if (srcFragment != null && srcFragment.isRemoving()) {
//            LogUtils.e(srcFragment.getClass().getName() + " is isRemoving");
            return null;
        }
        String name = destFragment.getClass().getName();
        Bundle args = destFragment.getArguments();

        FragmentTransaction ft = fm.beginTransaction();
        if (sharedElements == null || sharedElements.length == 0) {
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        } else {
            for (SharedElement element : sharedElements) {// 添加共享元素动画
                ft.addSharedElement(element.sharedElement, element.name);
            }
        }
        switch (type) {
            case TYPE_ADD_FRAGMENT:
                if (srcFragment != null) ft.hide(srcFragment);
                ft.add(args.getInt(ARGS_ID), destFragment, name);
                if (args.getBoolean(ARGS_IS_HIDE)) ft.hide(destFragment);
                if (args.getBoolean(ARGS_IS_ADD_STACK)) ft.addToBackStack(name);
                break;
            case TYPE_CHECK_ADD_FRAGMENT:
                List<Fragment> fragmentList = getFragments(fm);
                for (int i = fragmentList.size() - 1; i >= 0; --i) {
                    Fragment frg = fragmentList.get(i);
                    if (frg == destFragment) {
                        if (srcFragment != null) ft.remove(frg);
                        break;
                    }
                }
                ft.add(args.getInt(ARGS_ID), destFragment, name);
                if (args.getBoolean(ARGS_IS_HIDE)) ft.hide(destFragment);
                if (args.getBoolean(ARGS_IS_ADD_STACK)) ft.addToBackStack(name);
                break;
            case TYPE_REMOVE_FRAGMENT:
                ft.remove(destFragment);
                break;
            case TYPE_REMOVE_TO_FRAGMENT:
                List<Fragment> frgList = getFragments(fm);
                for (int i = frgList.size() - 1; i >= 0; --i) {
                    Fragment frg = frgList.get(i);
                    if (frg == destFragment) {
                        if (srcFragment != null) ft.remove(frg);
                        break;
                    }
                    ft.remove(frg);
                }
                break;
            case TYPE_REPLACE_FRAGMENT:
                ft.replace(args.getInt(ARGS_ID), destFragment, name);
                if (args.getBoolean(ARGS_IS_ADD_STACK)) ft.addToBackStack(name);
                break;
            case TYPE_POP_ADD_FRAGMENT:
                popFragment(fm);
                ft.add(args.getInt(ARGS_ID), destFragment, name);
                if (args.getBoolean(ARGS_IS_ADD_STACK)) ft.addToBackStack(name);
                break;
            case TYPE_HIDE_FRAGMENT:
                ft.hide(destFragment);
                break;
            case TYPE_SHOW_FRAGMENT:
                ft.show(destFragment);
                break;
            case TYPE_HIDE_SHOW_FRAGMENT:
                ft.hide(srcFragment).show(destFragment);
                break;
        }
        ft.commitAllowingStateLoss();
        return destFragment;
    }

    /**
     * 获取同级别最后加入的fragment
     *
     * @param fm fragment管理器
     * @return 最后加入的fragment
     */
    public static Fragment getLastAddFragment(@NonNull FragmentManager fm) {
        return getLastAddFragmentIsInStack(fm, false);
    }

    /**
     * 获取栈中同级别最后加入的fragment
     *
     * @param fm fragment管理器
     * @return 最后加入的fragment
     */
    public static Fragment getLastAddFragmentInStack(@NonNull FragmentManager fm) {
        return getLastAddFragmentIsInStack(fm, true);
    }

    /**
     * 根据栈参数获取同级别最后加入的fragment
     *
     * @param fm fragment管理器
     * @param isInStack       是否是栈中的
     * @return 栈中最后加入的fragment
     */
    private static Fragment getLastAddFragmentIsInStack(@NonNull FragmentManager fm,
                                                        boolean isInStack) {
        List<Fragment> frgList = getFragments(fm);
        if (frgList.isEmpty()) return null;
        for (int i = frgList.size() - 1; i >= 0; --i) {
            Fragment frg = frgList.get(i);
            if (frg != null) {
                if (isInStack) {
                    if (frg.getArguments().getBoolean(ARGS_IS_ADD_STACK)) {
                        return frg;
                    }
                } else {
                    return frg;
                }
            }
        }
        return null;
    }

    /**
     * 获取顶层可见fragment
     *
     * @param fm fragment管理器
     * @return 顶层可见fragment
     */
    public static Fragment getTopShowFragment(@NonNull FragmentManager fm) {
        return getTopShowFragmentIsInStack(fm, null, false);
    }

    /**
     * 获取栈中顶层可见fragment
     *
     * @param fm fragment管理器
     * @return 栈中顶层可见fragment
     */
    public static Fragment getTopShowFragmentInStack(@NonNull FragmentManager fm) {
        return getTopShowFragmentIsInStack(fm, null, true);
    }

    /**
     * 根据栈参数获取顶层可见fragment
     *
     * @param fm fragment管理器
     * @param parentFragment  父fragment
     * @param isInStack       是否是栈中的
     * @return 栈中顶层可见fragment
     */
    private static Fragment getTopShowFragmentIsInStack(@NonNull FragmentManager fm,
                                                        Fragment parentFragment,
                                                        boolean isInStack) {
        List<Fragment> frgList = getFragments(fm);
        if (frgList.isEmpty()) return parentFragment;
        for (int i = frgList.size() - 1; i >= 0; --i) {
            Fragment frg = frgList.get(i);
            if (frg != null && frg.isResumed() && frg.isVisible() && frg.getUserVisibleHint()) {
                if (isInStack) {
                    if (frg.getArguments().getBoolean(ARGS_IS_ADD_STACK)) {
                        return getTopShowFragmentIsInStack(frg.getChildFragmentManager(), frg, true);
                    }
                } else {
                    return getTopShowFragmentIsInStack(frg.getChildFragmentManager(), frg, false);
                }
            }
        }
        return parentFragment;
    }

    /**
     * 获取同级别fragment
     *
     * @param fm fragment管理器
     * @return 同级别的fragments
     */
    public static List<Fragment> getFragments(@NonNull FragmentManager fm) {
        return getFragmentsIsInStack(fm, false);
    }

    /**
     * 获取栈中同级别fragment
     *
     * @param fm fragment管理器
     * @return 栈中同级别fragment
     */
    public static List<Fragment> getFragmentsInStack(@NonNull FragmentManager fm) {
        return getFragmentsIsInStack(fm, true);
    }

    /**
     * 根据栈参数获取同级别fragment
     *
     * @param fm fragment管理器
     * @param isInStack       是否是栈中的
     * @return 栈中同级别fragment
     */
    private static List<Fragment> getFragmentsIsInStack(@NonNull FragmentManager fm, boolean isInStack) {
        @SuppressLint("RestrictedApi") List<Fragment> frgList = fm.getFragments();
        if (frgList == null || frgList.isEmpty()) return Collections.emptyList();
        List<Fragment> result = new ArrayList<>();
        for (int i = frgList.size() - 1; i >= 0; --i) {
            Fragment frg = frgList.get(i);
            if (frg != null) {
                if (isInStack) {
                    if (frg.getArguments().getBoolean(ARGS_IS_ADD_STACK)) {
                        result.add(frg);
                    }
                } else {
                    result.add(frg);
                }
            }
        }
        return result;
    }

    /**
     * 获取所有fragment
     *
     * @param fragmentManager fragment管理器
     * @return 所有fragment
     */
    public static List<FragmentNode> getAllFragments(@NonNull FragmentManager fragmentManager) {
        return getAllFragmentsIsInStack(fragmentManager, new ArrayList<>(), false);
    }

    /**
     * 获取栈中所有fragment
     *
     * @param fragmentManager fragment管理器
     * @return 所有fragment
     */
    public static List<FragmentNode> getAllFragmentsInStack(@NonNull FragmentManager fragmentManager) {
        return getAllFragmentsIsInStack(fragmentManager, new ArrayList<>(), true);
    }

    /**
     * 根据栈参数获取所有fragment
     * <p>需之前对fragment的操作都借助该工具类</p>
     *
     * @param fm fragment管理器
     * @param result          结果
     * @param isInStack       是否是栈中的
     * @return 栈中所有fragment
     */
    private static List<FragmentNode> getAllFragmentsIsInStack(@NonNull FragmentManager fm,
                                                               List<FragmentNode> result,
                                                               boolean isInStack) {
        @SuppressLint("RestrictedApi") List<Fragment> frgList = fm.getFragments();
        if (frgList == null || frgList.isEmpty()) return Collections.emptyList();
        for (int i = frgList.size() - 1; i >= 0; --i) {
            Fragment frg = frgList.get(i);
            if (frg != null) {
                if (isInStack) {
                    if (frg.getArguments().getBoolean(ARGS_IS_ADD_STACK)) {
                        result.add(new FragmentNode(frg, getAllFragmentsIsInStack(frg.getChildFragmentManager(), new ArrayList<FragmentNode>(), true)));
                    }
                } else {
                    result.add(new FragmentNode(frg, getAllFragmentsIsInStack(frg.getChildFragmentManager(), new ArrayList<FragmentNode>(), false)));
                }
            }
        }
        return result;
    }

    /**
     * 获取目标fragment的前一个fragment
     *
     * @param destFragment 目标fragment
     * @return 目标fragment的前一个fragment
     */
    public static Fragment getPreFragment(@NonNull Fragment destFragment) {
        FragmentManager fragmentManager = destFragment.getFragmentManager();
        if (fragmentManager == null) return null;
        List<Fragment> frgList = getFragments(fragmentManager);
        boolean flag = false;
        for (int i = frgList.size() - 1; i >= 0; --i) {
            Fragment frg = frgList.get(i);
            if (flag && frg != null) {
                return frg;
            }
            if (frg == destFragment) {
                flag = true;
            }
        }
        return null;
    }

    /**
     * 查找fragment
     *
     * @param fragmentManager fragment管理器
     * @param fragmentClass   fragment类
     * @return 查找到的fragment
     */
    public static Fragment findFragment(@NonNull FragmentManager fragmentManager, Class<? extends Fragment> fragmentClass) {
        List<Fragment> frgList = getFragments(fragmentManager);
        if (frgList.isEmpty()) return null;
        return fragmentManager.findFragmentByTag(fragmentClass.getName());
    }

    /**
     * 处理fragment回退键
     * <p>如果fragment实现了OnBackClickListener接口，返回{@code true}: 表示已消费回退键事件，反之则没消费</p>
     * <p>具体示例见FragmentActivity</p>
     *
     * @param frg frg
     * @return 是否消费回退事件
     */

    public static boolean dispatchBackPress(@NonNull Fragment frg) {
        return dispatchBackPress(frg.getFragmentManager());
    }

    /**
     * 处理fragment回退键
     * <p>如果fragment实现了OnBackClickListener接口，返回{@code true}: 表示已消费回退键事件，反之则没消费</p>
     * <p>具体示例见FragmentActivity</p>
     *
     * @param fm fragment管理器
     * @return 是否消费回退事件
     */
    public static boolean dispatchBackPress(@NonNull FragmentManager fm) {
        @SuppressLint("RestrictedApi") List<Fragment> frgList = fm.getFragments();
        if (frgList == null || frgList.isEmpty()) return false;
        for (int i = frgList.size() - 1; i >= 0; --i) {
            Fragment frg = frgList.get(i);
            if (frg != null
                    && frg.isResumed()
                    && frg.isVisible()
                    && frg.getUserVisibleHint()
                    && frg instanceof OnBackClickListener
                    && ((OnBackClickListener) frg).onBackClick()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置背景色
     *
     * @param frg frg
     * @param color    背景色
     */
    public static void setBackgroundColor(@NonNull Fragment frg, @ColorInt int color) {
        View view = frg.getView();
        if (view != null) {
            view.setBackgroundColor(color);
        }
    }

    /**
     * 设置背景资源
     *
     * @param frg frg
     * @param resId    资源Id
     */
    public static void setBackgroundResource(@NonNull Fragment frg, @DrawableRes int resId) {
        View view = frg.getView();
        if (view != null) {
            view.setBackgroundResource(resId);
        }
    }

    /**
     * 设置背景
     *
     * @param frg   frg
     * @param background 背景
     */
    public static void setBackground(@NonNull Fragment frg, Drawable background) {
        View view = frg.getView();
        if (view != null) {
            if (AppUtils.IS_HIGH_4DOT4_SYSTEM) {
                view.setBackground(background);
            } else {
                view.setBackgroundDrawable(background);
            }
        }
    }

    static class Args {
        int     id;
        boolean isHide;
        boolean isAddStack;

        Args(int id, boolean isHide, boolean isAddStack) {
            this.id = id;
            this.isHide = isHide;
            this.isAddStack = isAddStack;
        }
    }

    public static class SharedElement {
        View   sharedElement;
        String name;

        public SharedElement(View sharedElement, String name) {
            this.sharedElement = sharedElement;
            this.name = name;
        }
    }

    static class FragmentNode {
        Fragment           frg;
        List<FragmentNode> next;

        public FragmentNode(Fragment frg, List<FragmentNode> next) {
            this.frg = frg;
            this.next = next;
        }

        @Override
        public String toString() {
            return frg.getClass().getSimpleName() + "->" + ((next == null || next.isEmpty()) ? "no child" : next.toString());
        }
    }

    public interface OnBackClickListener {
        boolean onBackClick();
    }
}