package com.coder.mario.library.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CoderMario on 2017/11/9.
 */

public abstract class SimpleRecyclerAdapter<T extends Object, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private List<T> mInnerSet;

    public List<T> getInnerSet() {
        if (null == mInnerSet) {
            mInnerSet = new ArrayList<>();
        }
        return mInnerSet;
    }

    public void add2InnerSet(T t) {
        getInnerSet().add(t);
    }

    public void setInnerSet(List<T> tSet) {
        this.mInnerSet = tSet;
        notifyDataSetChanged();
    }
}
