package com.coder.mario.library.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by CoderMario on 2017/11/9.
 */
public abstract class SimpleRecyclerViewHolder<T extends Object> extends RecyclerView.ViewHolder {

    public SimpleRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public void initAllViews(View itemView) {

    }

    public void initAllDatum(T t) {

    }
}
