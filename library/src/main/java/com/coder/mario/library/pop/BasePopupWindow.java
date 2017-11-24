package com.coder.mario.library.pop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.animation.AnimatorSet;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;

import com.coder.mario.library.R;

/**
 * Created by CoderMario on 2017-11-15.
 */
public abstract class BasePopupWindow extends PopupWindow implements BasePopupWindowImp {

    private Context mContext;
    private LayoutInflater mInflater;

    private long mPopupWindowHoldToken;

    private int mMaskLayerColor = Color.TRANSPARENT;

    private AnimatorSet mMaskLayerShowAnim = null;
    private AnimatorSet mMaskLayerHideAnim = null;
    private Animation mWindowShowAnimation = null;
    private Animation mWindowHideAnimation = null;

    private View mWindowMaskLayer;

    public BasePopupWindow(Context context) {
        this.mContext = context;
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setAnimationStyle(R.style.Animation_PopupWindow_Fade);
    }

    @Override
    public final void setMaskLayerColor(int color) {
        this.mMaskLayerColor = color;
    }

    @Override
    public final int getMaskLayerColor() {
        return this.mMaskLayerColor;
    }

    @Override
    public void setContentView(View contentView) {
        super.setContentView(contentView);
        if (null == contentView) {
            return;
        }
        contentView.setBackgroundColor(Color.RED);
        ViewTreeObserver viewTreeObserver = contentView.getViewTreeObserver();
        if (null == viewTreeObserver) {
            return;
        }
        viewTreeObserver.addOnGlobalFocusChangeListener(new InnerGlobalFocusListener());
    }

    @Override
    public void setAnimationStyle(int animationStyle) {
        super.setAnimationStyle(animationStyle);
        parseAnimationStyle(animationStyle);
    }

    @Override
    public void parseAnimationStyle(int animationStyle) {
        TypedArray typedArray = null;
        try {
            typedArray = getContext().obtainStyledAttributes(animationStyle, ANIMATION_STYLE_ATTRS);
            if (null == typedArray) {
                return;
            }
            for(int i = 0, count = typedArray.getIndexCount(); i < count; i ++) {
                int resourceId = typedArray.getResourceId(i, 0);
                Animation animation = AnimationUtils.loadAnimation(getContext(), resourceId);
                if(null == animation) {
                    return;
                }
                if(0 == i) {
                    this.mWindowShowAnimation = animation; // 显示动画
                } else if(1 == i) {
                    this.mWindowHideAnimation = animation; // 隐藏动画
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null == typedArray) {
                return;
            }
            typedArray.recycle();
        }
    }

    @Override
    public Animation getWindowShowAnimation() {
        return this.mWindowShowAnimation;
    }

    @Override
    public long getWindowShowAnimationDuration() {
        Animation animation = getWindowShowAnimation();
        if (null != animation) {
            return animation.getDuration();
        } else {
            return 0;
        }
    }

    @Override
    public Interpolator getWindowShowAnimationInterpolator() {
        Animation animation = getWindowShowAnimation();
        if (null != animation) {
            return animation.getInterpolator();
        } else {
            return null;
        }
    }

    @Override
    public Animation getWindowHideAnimation() {
        return this.mWindowHideAnimation;
    }

    @Override
    public long getWindowHideAnimationDuration() {
        Animation animation = getWindowHideAnimation();
        if (null != animation) {
            return animation.getDuration();
        } else {
            return 0;
        }
    }

    @Override
    public Interpolator getWindowHideAnimationInterpolator() {
        Animation animation = getWindowHideAnimation();
        if (null != animation) {
            return animation.getInterpolator();
        } else {
            return null;
        }
    }

    @Override
    public final LayoutInflater getInflater() {
        if (null == this.mInflater) {
            this.mInflater = LayoutInflater.from(getContext());
        }
        return mInflater;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        hideMaskLayerWithAnimation();
    }

    @Override
    public void showAsDropDown(View anchor, int xOff, int yOff) {
        super.showAsDropDown(anchor, xOff, yOff);
        if (Build.VERSION_CODES.KITKAT > Build.VERSION.SDK_INT) {
            showMaskLayerWithAnimation();
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        showMaskLayerWithAnimation();
    }

    @Override
    public void showAsDropDown(View anchor, int xOff, int yOff, int gravity) {
        super.showAsDropDown(anchor, xOff, yOff, gravity);
        if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
            showMaskLayerWithAnimation();
        }
    }

    /**
     * @hide
     * */
    private void showMaskLayerWithAnimation() {
        AnimatorSet maskLayerShowAnim = getMaskLayerShowAnim();
        if (null == maskLayerShowAnim || maskLayerShowAnim.isRunning()) {
            return;
        }
        AnimatorSet maskLayerHideAnim = getMaskLayerHideAnimWithoutInit();
        if (null != maskLayerHideAnim && maskLayerHideAnim.isRunning()) {
            maskLayerHideAnim.cancel();
        }
        maskLayerShowAnim.start();
    }

    /**
     * */
    private void hideMaskLayerWithAnimation() {
        AnimatorSet maskLayerHideAnim = getMaskLayerHideAnim();
        if (null == maskLayerHideAnim || maskLayerHideAnim.isRunning()) {
            return;
        }
        AnimatorSet maskLayerShowAnim = getMaskLayerShowAnimWithoutInit();
        if (null != maskLayerShowAnim && maskLayerShowAnim.isRunning()) {
            maskLayerShowAnim.cancel();
        }
        maskLayerHideAnim.start();
    }

    /**
     * */
    private AnimatorSet getMaskLayerShowAnim() {
        if (null == this.mMaskLayerShowAnim) {
            this.mMaskLayerShowAnim = new AnimatorSet();
            initMaskLayerShowAnim(this.mMaskLayerShowAnim);
        }
        return this.mMaskLayerShowAnim;
    }

    /**
     * */
    private AnimatorSet getMaskLayerShowAnimWithoutInit() {
        return this.mMaskLayerShowAnim;
    }

    /**
     * */
    private AnimatorSet getMaskLayerHideAnim() {
        if (null == this.mMaskLayerHideAnim) {
            this.mMaskLayerHideAnim = new AnimatorSet();
            initMaskLayerHideAnim(this.mMaskLayerHideAnim);
        }
        return this.mMaskLayerHideAnim;
    }

    /**
     * */
    private AnimatorSet getMaskLayerHideAnimWithoutInit() {
        return this.mMaskLayerHideAnim;
    }

    /**
     * */
    private void initMaskLayerShowAnim(AnimatorSet animatorSet) {
        if (null == animatorSet) {
            return;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(getWindowMaskLayer(), "alpha", 0.f, 1.f);
        animatorSet.play(animator);
        animatorSet.setDuration(getWindowShowAnimationDuration());
        animatorSet.addListener(new MaskLayerShowAnimationListener());
        animatorSet.setInterpolator(getWindowShowAnimationInterpolator());
    }

    /**
     * */
    private void initMaskLayerHideAnim(AnimatorSet animatorSet) {
        if (null == animatorSet) {
            return;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(getWindowMaskLayer(), "alpha", 1.f, 0.f);
        animatorSet.play(animator);
        animatorSet.setDuration(getWindowHideAnimationDuration());
        animatorSet.addListener(new MaskLayerHideAnimationListener());
        animatorSet.setInterpolator(getWindowHideAnimationInterpolator());
    }

    /**
     * */
    private class MaskLayerShowAnimationListener extends AnimatorListenerAdapter {

        @Override
        public void onAnimationStart(Animator animation) {
            addMaskLayerNecessary();
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            onPopupWindowShowCompleted();
        }
    }

    /**
     * */
    private class MaskLayerHideAnimationListener extends AnimatorListenerAdapter {

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            onPopupWindowHideCompleted();
            removeMaskLayerNecessary();
        }
    }

    /**
     * */
    private final void addMaskLayerNecessary() {
        if (null == getWindowMaskLayerWithoutInit() || null == getContext()) {
            return ;
        }
        Activity activity = findActivityByContext(getContext());
        if (null == activity) {
            return;
        }
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        if (null == decorView) {
            return ;
        }
        if (null != decorView.findViewById(ID_WINDOW_MASK_LAYER)) {
            return ;
        }
        View maskLayer = getWindowMaskLayer();
        if (null == maskLayer) {
            return;
        }
        decorView.addView(maskLayer);
    }

    /**
     * */
    private final void removeMaskLayerNecessary() {
        if (null == getWindowMaskLayerWithoutInit() || null == getContext()) {
            return ;
        }
        Activity activity = findActivityByContext(getContext());
        if (null == activity) {
            return;
        }
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        if (null == decorView) {
            return;
        }
        View maskLayer = decorView.findViewById(ID_WINDOW_MASK_LAYER);
        if (null == maskLayer) {
            return;
        }
        Object tag = maskLayer.getTag();
        if (null == tag || !(tag instanceof Long)) {
            return;
        }
        long token = (long) tag;
        if (token != getPopupWindowHoldToken()) {
            return;
        }
        decorView.removeView(maskLayer);
    }

    /**
     * */
    private View getWindowMaskLayerWithoutInit() {
        return this.mWindowMaskLayer;
    }

    /**
     * */
    private View getWindowMaskLayer() {
        if (null == this.mWindowMaskLayer) {
            this.mWindowMaskLayer = new InnerMaskLayer(getContext());
            initWindowMaskLayer(this.mWindowMaskLayer);
        }
        return this.mWindowMaskLayer;
    }

    /**
     * */
    private void initWindowMaskLayer(View maskLayer) {
        if (null == maskLayer) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        maskLayer.setLayoutParams(layoutParams);
        maskLayer.setBackgroundColor(getMaskLayerColor());
        maskLayer.setId(ID_WINDOW_MASK_LAYER);
        long millis = System.currentTimeMillis();
        setPopupWindowHoldToken(millis); // 设置移除layer的唯一标识
        maskLayer.setTag(millis);
    }

    /**
     * */
    private Activity findActivityByContext(Context context) {
        if (null == context) {
            throw new IllegalArgumentException("You cannot start a pop on a null Context");
        } else if (context instanceof Activity) {
            return (Activity) context;
        } else {
            return null;
        }
    }

    @Override
    public void onPopupWindowShowCompleted() {

    }

    @Override
    public void onPopupWindowHideCompleted() {

    }

    /**
     * */
    private long getPopupWindowHoldToken() {
        return this.mPopupWindowHoldToken;
    }

    /**
     * */
    private void setPopupWindowHoldToken(long token) {
        this.mPopupWindowHoldToken = token;
    }

    @Override
    public final Context getContext() {
        return this.mContext;
    }

    private class InnerMaskLayer extends View {

        public InnerMaskLayer(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            return super.dispatchKeyEvent(event);
        }
    }

    /**
     * */
    private class InnerGlobalFocusListener implements ViewTreeObserver.OnGlobalFocusChangeListener {

        @Override
        public void onGlobalFocusChanged(View oFocus, View nFocus) {
            Log.e(getClass().getName(), String.format(" **************  oFocus _> %s, nFocus _> %s  ", oFocus, nFocus));
        }
    }
}
