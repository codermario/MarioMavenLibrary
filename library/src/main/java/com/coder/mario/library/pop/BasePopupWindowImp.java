package com.coder.mario.library.pop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.Interpolator;

import com.coder.mario.library.R;

/**
 * Created by CoderMario on 2017-11-15.
 */
public interface BasePopupWindowImp {

    int[] ANIMATION_STYLE_ATTRS = {android.R.attr.windowEnterAnimation, android.R.attr.windowExitAnimation};
    int ID_WINDOW_MASK_LAYER = R.id.mario_id_pop_mask_layout;

    /**
     * */
    void onPopupWindowShowCompleted();

    /**
     * */
    void onPopupWindowHideCompleted();

    /**
     * 解析动画  主要用以获得动画时长和动画估值器
     * */
    void parseAnimationStyle(int animationStyle);

    /**
     * @hide
     * */
    Animation getWindowShowAnimation();

    /**
     * */
    long getWindowShowAnimationDuration();

    /**
     * */
    Interpolator getWindowShowAnimationInterpolator();

    /**
     * */
    Animation getWindowHideAnimation();

    /**
     * */
    long getWindowHideAnimationDuration();

    /**
     * */
    Interpolator getWindowHideAnimationInterpolator();

    /**
     * */
    void setMaskLayerColor(int color);

    /**
     * */
    int getMaskLayerColor();

    /**
     * */
    LayoutInflater getInflater();

    /**
     * */
    Context getContext();
}
