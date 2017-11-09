package com.coder.mario.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.coder.mario.library.R;

/**
 * Created by CoderMario on 2017/11/7.
 */
public class OvershootLayout extends RelativeLayout implements NestedScrollingParent, NestedScrollingChild {

    private NestedScrollingParentHelper mParentHelper;
    private NestedScrollingChildHelper mChildHelper;
    private int mMaxOverScrollY = 0 + 420;
    private int mMinOverScrollY = 0 - 420;
    private Scroller mScroller;

    private boolean mNestedScrollTag = false;

    private ScrollOrientationBase mScrollOrientation = getScrollOrientationH();
    private ScrollOrientationBase mScrollOrientationH;
    private ScrollOrientationBase mScrollOrientationV;

    private OverScrollListener mOverScrollListener;

    public OvershootLayout(Context context) {
        super(context);
        initOvershootLayout();
    }

    public OvershootLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initOvershootLayout();
        initOvershootLayoutAttrs(attrs);
    }

    public OvershootLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initOvershootLayout();
        initOvershootLayoutAttrs(attrs);
    }

    /**
     * */
    private void initOvershootLayout() {
        //
    }

    /**
     * */
    private void initOvershootLayoutAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.OvershootLayout);
        if (null == typedArray) {
            return;
        }
        int count = typedArray.getIndexCount();
        if (0 >= count) {
            releaseTypedArray(typedArray);
        }
        for (int i = 0; i < count; i ++) {
            int index = typedArray.getIndex(i);
            if (R.styleable.OvershootLayout_scroll_orientation == index) {
                setScrollOrientation(typedArray.getInt(index, 0));
            }
        }
        releaseTypedArray(typedArray);
    }

    /**
     * */
    private void releaseTypedArray(TypedArray typedArray) {
        if (null == typedArray) {
            return;
        }
        try {
            typedArray.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * */
    public void setOverScrollListener(OverScrollListener scrollListener) {
        this.mOverScrollListener = scrollListener;
    }

    /**
     * */
    private void setScrollOrientation(int orientation) {
        if (0 == orientation) {
            mScrollOrientation = getScrollOrientationH();
        } else if (1 == orientation) {
            mScrollOrientation = getScrollOrientationV();
        }
    }

    /**
     * */
    private ScrollOrientationBase getScrollOrientation() {
        return mScrollOrientation;
    }

    /**
     * */
    private ScrollOrientationBase getScrollOrientationH() {
        if (null == mScrollOrientationH) {
            mScrollOrientationH = new ScrollOrientationH();
        }
        return mScrollOrientationH;
    }

    /**
     * */
    private ScrollOrientationBase getScrollOrientationV() {
        if (null == mScrollOrientationV) {
            mScrollOrientationV = new ScrollOrientationV();
        }
        return mScrollOrientationV;
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
    public void onStopNestedScroll(View child) {
        getParentHelper().onStopNestedScroll(child);
        if (!getNestedScrollTag()) {
            return;
        }
        setNestedScrollTag(false);
        getScroller().startScroll(getScrollX(), getScrollY(), 0 - getScrollX(), 0 - getScrollY());
        postInvalidate();
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        getParentHelper().onNestedScrollAccepted(child, target, axes);
        if (!getScroller().isFinished()) {
            getScroller().forceFinished(true);
        }
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return getScrollOrientation().onStartNestedScroll(nestedScrollAxes);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        getScrollOrientation().onNestedPreScroll(dx, dy, consumed);
    }

    @Override
    public void onNestedScroll(View target, int dxC, int dyC, int dxUnconsumed, int dyUnconsumed) {
        getScrollOrientation().onNestedScroll(dxUnconsumed, dyUnconsumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (0 == getScrollX() && 0 == getScrollY()) {
            return super.onNestedPreFling(target, velocityX, velocityY);
        } else {
            return true;
        }
    }

    @Override
    public void computeScroll() {
        if (getScroller().computeScrollOffset()) {
            int currY = getScroller().getCurrY();
            int currX = getScroller().getCurrX();
            scrollTo(currX, currY);
        } else {
            super.computeScroll();
        }
        if (null != mOverScrollListener) {
            mOverScrollListener.onOverScrolled(getScrollX(), getScrollY());
        }
    }

    /**
     * */
    private abstract class ScrollOrientationBase {

        public boolean onStartNestedScroll(int nestedScrollAxes) {
            return false;
        }

        public void onNestedPreScroll(int dx, int dy, int[] consumed) {
            setNestedScrollTag(true);
        }

        public void onNestedScroll(int dxUnconsumed, int dyUnconsumed) {
            //
        }
    }

    /**
     * */
    private class ScrollOrientationH extends ScrollOrientationBase {

        @Override
        public boolean onStartNestedScroll(int nestedScrollAxes) {
            return ViewCompat.SCROLL_AXIS_HORIZONTAL == nestedScrollAxes;
        }

        @Override
        public void onNestedPreScroll(int dx, int dy, int[] consumed) {
            super.onNestedPreScroll(dx, dy, consumed);
            int scrollX = getScrollX();
            if (0 < dx && 0 > scrollX) {
                if (0 >= scrollX + dx) {
                    consumed[0] = dx;
                } else {
                    consumed[0] = 0 - scrollX;
                }
                scrollBy(consumed[0], 0);
                return;
            }
            if (0 > dx && 0 < scrollX) {
                if (0 <= scrollX + dx) {
                    consumed[0] = dx;
                } else {
                    consumed[0] = 0 - scrollX;
                }
                scrollBy(consumed[0], 0);
                return;
            }
        }

        @Override
        public void onNestedScroll(int dxUnconsumed, int dyUnconsumed) {
            super.onNestedScroll(dxUnconsumed, dyUnconsumed);
            if (0 == dxUnconsumed) {
                return;
            }
            int scrollX = getScrollX();
            dxUnconsumed = (dxUnconsumed / Math.abs(dxUnconsumed)) * Math.abs(dxUnconsumed) / (scrollX < getMaxOverScrollY() / 2 ? 3 : 5); // 阻尼效果
//            dxUnconsumed = (dxUnconsumed / Math.abs(dxUnconsumed)) * Math.abs(dxUnconsumed) / 3; // 阻尼效果
            if (0 < dxUnconsumed) {
                if (getMaxOverScrollY() >= scrollX + dxUnconsumed) {
                    scrollBy(dxUnconsumed, 0);
                } else {
                    scrollBy(getMaxOverScrollY() - scrollX, 0);
                }
                return;
            }
            if (0 > dxUnconsumed) {
                if (getMinOverScrollY() <= scrollX + dxUnconsumed) {
                    scrollBy(dxUnconsumed, 0);
                } else {
                    scrollBy(getMinOverScrollY() - scrollX, 0);
                }
                return;
            }
        }
    }

    /**
     * */
    private class ScrollOrientationV extends ScrollOrientationBase {

        @Override
        public boolean onStartNestedScroll(int nestedScrollAxes) {
            return ViewCompat.SCROLL_AXIS_VERTICAL == nestedScrollAxes;
        }

        @Override
        public void onNestedPreScroll(int dx, int dy, int[] consumed) {
            super.onNestedPreScroll(dx, dy, consumed);
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
        public void onNestedScroll(int dxUnconsumed, int dyUnconsumed) {
            super.onNestedScroll(dyUnconsumed, dxUnconsumed);
            if (0 == dyUnconsumed) {
                return;
            }
            int scrollY = getScrollY();
            dyUnconsumed = (dyUnconsumed / Math.abs(dyUnconsumed)) * Math.abs(dyUnconsumed) / (scrollY < getMaxOverScrollY() / 2 ? 3 : 5); // 阻尼效果
//            dyUnconsumed = (dyUnconsumed / Math.abs(dyUnconsumed)) * Math.abs(dyUnconsumed) / 3; // 阻尼效果
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
    }

    /**
     * */
    public interface OverScrollListener {

        void onOverScrolled(int overScrollX, int overScrollY);
    }
}
