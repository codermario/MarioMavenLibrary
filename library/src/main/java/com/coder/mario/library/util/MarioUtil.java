package com.coder.mario.library.util;

import android.os.Handler;
import android.view.View;

/**
 * Created by CoderMario on 2017-11-15.
 */
public class MarioUtil {

    /**
     * 去抖动
     * */
    public static void antiShake(final View view) {
        if (null == view) {
            return;
        }
        view.setEnabled(false);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (null != view) {
                    view.setEnabled(true);
                }
            }
        }, 420);
    }

    /**
     * String转Int
     * */
    public static int parseInt(String value, int defValue) {
        try {
            defValue = Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return defValue;
        }
    }

    /**
     * String转Long
     * */
    public static long parseLong(String value, long defValue) {
        try {
            defValue = Long.parseLong(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return defValue;
        }
    }
}
