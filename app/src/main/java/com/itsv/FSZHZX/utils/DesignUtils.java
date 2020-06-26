package com.itsv.FSZHZX.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * anthor:Created by Xiaoqiang on 2017/3/28.
 * email:963181974@qq.com
 * 适配工具类
 */

public class DesignUtils {
    /**
     * dp转换px
     *
     * @param dp      dp值
     * @param context 上下文
     * @return 转换后的px值
     */
    public static int dp2px(int dp, Context context) {
        // px = dp * 密度比
        float density = context.getApplicationContext().getResources().getDisplayMetrics().density; // 0.75 1 1.5 2
        return (int) (dp * density + 0.5f);// 4舍5入
    }

    /**
     * 获取屏幕方向
     *
     * @param context
     * @return
     */
    public static int getOrientation(Context context) {
        if (context == null) {
            return Configuration.ORIENTATION_PORTRAIT;
        }
        return context.getApplicationContext().getResources().getConfiguration().orientation;

    }

    /**
     * 获取屏幕高度
     *
     * @param context 上下文
     * @return 屏幕高度
     */
    public static int getScreenHeight(Context context) {
        return context == null ? 0 : context.getApplicationContext().getResources().getDisplayMetrics().heightPixels; // 屏幕高（像素，如：1280px）
    }

    /**
     * 获取屏幕宽度
     *
     * @param context 上下文
     * @return 屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        return context == null ? 0 : context.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 隐藏软键盘
     */
    public static boolean check_keyboard_isShow(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && view != null) {
            if (inputMethodManager.isActive(view)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 判断键盘是否显示
     */
    public static void hide_keyboard_from(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 显示软键盘
     */
    public void show_keyboard_from(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && view != null) {
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 键盘泄漏问题
     *
     * @param destContext 上下文
     */
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f;
        Object obj_get;
        for (String param : arr) {
            try {
                f = imm.getClass().getDeclaredField(param);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public static void setImageGreyUnEnabled(ImageView imageView) {
//        ColorMatrix cm = new ColorMatrix();
//        cm.setSaturation(20f); // 设置饱和度
//        ColorMatrixColorFilter grayColorFilter = new ColorMatrixColorFilter(cm);
//        imageView.setColorFilter(grayColorFilter);
        imageView.setColorFilter(Color.GRAY);
        imageView.setEnabled(false);
    }

    public static void setImageGrey(ImageView imageView) {
//        ColorMatrix cm = new ColorMatrix();
//        cm.setSaturation(20f); // 设置饱和度
//        ColorMatrixColorFilter grayColorFilter = new ColorMatrixColorFilter(cm);
//        imageView.setColorFilter(grayColorFilter);
        imageView.setColorFilter(Color.GRAY);
        imageView.setEnabled(true);
    }

    public static void setImageNormal(ImageView imageView) {
//        ColorMatrix cm = new ColorMatrix();
//        cm.setSaturation(0); // 设置饱和度
//        ColorMatrixColorFilter grayColorFilter = new ColorMatrixColorFilter(cm);
        imageView.setColorFilter(null);
        imageView.setEnabled(true);
    }

    public static int getViewWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredWidth();
    }

    public static int getViewHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }

    public static String SBCToDBC(String input) {
        if (input == null || "".equals(input)) {
            return "";
        }
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public static String DBCToSBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }
            if (c[i] < 127)
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    public static boolean hasNotchAtHuawei(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class HwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("Notch", "hasNotchAtHuawei ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("Notch", "hasNotchAtHuawei NoSuchMethodException");
        } catch (Exception e) {
            Log.e("Notch", "hasNotchAtHuawei Exception");
        } finally {
            return ret;
        }
    }

    //获取刘海尺寸：width、height
    //int[0]值为刘海宽度 int[1]值为刘海高度
    public static int[] getNotchSizeAtHuawei(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("Notch", "getNotchSizeAtHuawei ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("Notch", "getNotchSizeAtHuawei NoSuchMethodException");
        } catch (Exception e) {
            Log.e("Notch", "getNotchSizeAtHuawei Exception");
        } finally {
            return ret;
        }
    }

    public static final int VIVO_NOTCH = 0x00000020;//是否有刘海
    public static final int VIVO_FILLET = 0x00000008;//是否有圆角

    public static boolean hasNotchAtVivo(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class FtFeature = classLoader.loadClass("android.util.FtFeature");
            Method method = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) method.invoke(FtFeature, VIVO_NOTCH);
        } catch (ClassNotFoundException e) {
            Log.e("Notch", "hasNotchAtVivo ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("Notch", "hasNotchAtVivo NoSuchMethodException");
        } catch (Exception e) {
            Log.e("Notch", "hasNotchAtVivo Exception");
        } finally {
            return ret;
        }
    }

    public static boolean hasNotchAtOPPO(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }


    public static boolean hasNotchAtXiaoMi(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "force_black", 0) == 1;
    }

    public static boolean hasNotch(Context context) {
        String vendor = android.os.Build.BRAND;
        if (TextUtils.isEmpty(vendor))
            return false;
        if (vendor.toLowerCase().contains("huawei")) {
            return hasNotchAtHuawei(context.getApplicationContext());
        } else {
            return false;
        }
    }

}
