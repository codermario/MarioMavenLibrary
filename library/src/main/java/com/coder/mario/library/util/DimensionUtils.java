package com.coder.mario.library.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by CoderMario on 2017-11-14.
 * 关于尺寸相关的帮助类
 */
public class DimensionUtils {

    /**
     * */
    public static int getWidthPixels(Context context) {
        if (null == context) {
            return 0;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * */
    public static int getHeightPixels(Context context) {
        if (null == context) {
            return 0;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    /**
     * */
    public static float dp2valueFloat(Context context, float dp) {
        if (null == context) {
            return 0;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }

    /**
     * */
    public static int dp2valueInt(Context context, float dp) {
        return (int) dp2valueFloat(context, dp);
    }

    /**
     * */
    public static float px2valueFloat(Context context, float px) {
        if (null == context) {
            return 0;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, displayMetrics);
    }

    /**
     * */
    public static int px2valueInt(Context context, float px) {
        return (int) px2valueFloat(context, px);
    }

    /**
     * */
    public static int getStatusBarHeight(Context context) {
        if (null == context) {
            return 0;
        }
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (0 >= resourceId) {
            return 0;
        }
        int value = 0;
        try {
            value = resources.getDimensionPixelSize(resourceId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return value;
        }
    }
}
