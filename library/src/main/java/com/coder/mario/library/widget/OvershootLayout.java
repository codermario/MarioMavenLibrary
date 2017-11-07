package com.coder.mario.library.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by CoderMario on 2017/11/7.
 */
public class OvershootLayout extends RelativeLayout implements NestedScrollingParent, NestedScrollingChild {

    private NestedScrollingParentHelper mParentHelper;
    private NestedScrollingChildHelper mChildHelper;
    private int mMaxOverScrollY = 0 + 1960;
    private int mMinOverScrollY = 0 - 1960;
    private Scroller mScroller;

    private boolean mNestedScrollTag = false;

    public OvershootLayout(Context context) {
        super(context);
        initOvershootLayout();
    }

    public OvershootLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initOvershootLayout();
    }

    public OvershootLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initOvershootLayout();
    }

    /**
     * */
    private void initOvershootLayout() {
        setBackgroundColor(Color.WHITE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (null != event && MotionEvent.ACTION_DOWN == event.getAction()) {
            getScroller().forceFinished(true);
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * */
    private NestedScrollingParentHelper getParentHelper() {
        if (null == mParentHelper) {
            mParentHelper = new NestedScrollingParentHelper(this);
        }
        return mParentHelper;
    }

    /**
     * */
    private NestedScrollingChildHelper getChildHelper() {
        if (null == mChildHelper) {
            mChildHelper = new NestedScrollingChildHelper(this);
        }
        return mChildHelper;
    }

    /**
     * */
    private int getMaxOverScrollY() {
        return mMaxOverScrollY;
    }

    /**
     * */
    private int getMinOverScrollY() {
        return mMinOverScrollY;
    }

    /**
     * */
    private boolean getNestedScrollTag() {
        return mNestedScrollTag;
    }

    /**
     * */
    private void setNestedScrollTag(boolean tag) {
        this.mNestedScrollTag = tag;
    }

    /**
     * */
    private Scroller getScroller() {
        if (null == mScroller) {
            mScroller = new Scroller(getContext(), new DecelerateInterpolator());
        }
        return mScroller;
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onStopNestedScroll(View child) {
        if (!getNestedScrollTag()) {
            return;
        }
        setNestedScrollTag(false);
        getScroller().startScroll(getScrollX(), getScrollY(), 0, 0 - getScrollY(), 120);
        postInvalidate();
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        setNestedScrollTag(true);
        int scrollY = getScrollY();
        if (0 < dy && 0 > scrollY) {
            if (0 >= scrollY + dy) {
                consumed[1] = dy;
            } else {
                consumed[1] = 0 - scrollY;
            }
            scrollBy(0, consumed[1]);
            return;
        }
        if (0 > dy && 0 < scrollY) {
            if (0 <= scrollY + dy) {
                consumed[1] = dy;
            } else {
                consumed[1] = 0 - scrollY;
            }
            scrollBy(0, consumed[1]);
            return;
        }
    }

    @Override
    public void onNestedScroll(View target, int dxC, int dyConsumed, int dxU, int dyUnconsumed) {
        if (0 == dyUnconsumed) {
            return;
        }
        int scrollY = getScrollY();
        dyUnconsumed = (dyUnconsumed / Math.abs(dyUnconsumed)) * Math.max(1, Math.abs(dyUnconsumed) / 3); // 阻尼效果
        if (0 < dyUnconsumed) {
            if (getMaxOverScrollY() >= scrollY + dyUnconsumed) {
                scrollBy(0, dyUnconsumed);
            } else {
                scrollBy(0, getMaxOverScrollY() - scrollY);
            }
            return;
        }
        if (0 > dyUnconsumed) {
            if (getMinOverScrollY() <= scrollY + dyUnconsumed) {
                scrollBy(0, dyUnconsumed);
            } else {
                scrollBy(0, getMinOverScrollY() - scrollY);
            }
            return;
        }
    }

    @Override
    public void computeScroll() {
        if (getScroller().computeScrollOffset()) {
            int currY = getScroller().getCurrY();
            scrollTo(0, currY);
        } else {
            super.computeScroll();
        }
    }
}
