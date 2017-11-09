package com.mario.library;

import com.coder.mario.library.adapter.SimpleRecyclerAdapter;
import com.coder.mario.library.holder.SimpleRecyclerViewHolder;
import com.coder.mario.library.widget.OvershootLayout;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.View;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private OvershootLayout mOvershootLayoutHorizontal;
    private OvershootLayout mOvershootLayoutVertical;
    private RecyclerView mRecyclerViewHorizontal;
    private RecyclerView mRecyclerViewVertical;
    private TextView mOverScrollHintHorizontal;
    private TextView mOverScrollHintVertical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerViewHorizontal = (RecyclerView) findViewById(R.id.recycler_view_horizontal);
        mRecyclerViewHorizontal.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewHorizontal.setNestedScrollingEnabled(true);
        mRecyclerViewHorizontal.setAdapter(new InnerAdapterHorizontal());

        mRecyclerViewVertical = (RecyclerView) findViewById(R.id.recycler_view_vertical);
        mRecyclerViewVertical.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewVertical.setNestedScrollingEnabled(true);
        mRecyclerViewVertical.setAdapter(new InnerAdapterVertical());

        mOverScrollHintHorizontal = (TextView) findViewById(R.id.over_scroll_hint_h);
        mOverScrollHintVertical = (TextView) findViewById(R.id.over_scroll_hint_v);

        final int mDragOrientationHL = getResources().getColor(android.R.color.holo_blue_light);
        final int mDragOrientationHR = getResources().getColor(android.R.color.holo_purple);
        final int mDragOrientationHC = mOverScrollHintHorizontal.getCurrentTextColor();

        final int mDragOrientationVT = getResources().getColor(android.R.color.holo_orange_dark);
        final int mDragOrientationVB = getResources().getColor(android.R.color.holo_red_light);
        final int mDragOrientationVC = mOverScrollHintVertical.getCurrentTextColor();

        mOvershootLayoutHorizontal = (OvershootLayout) findViewById(R.id.overshoot_layout_h);
        mOvershootLayoutHorizontal.setOverScrollListener(new OvershootLayout.OverScrollListener() {

            @Override
            public void onOverScrolled(int overScrollX, int overScrollY) {
                if (0 == overScrollX) {
                    mOverScrollHintHorizontal.setTextColor(mDragOrientationHC);
                } else if (0 > overScrollX) {
                    mOverScrollHintHorizontal.setTextColor(mDragOrientationHL);
                } else if (0 < overScrollX) {
                    mOverScrollHintHorizontal.setTextColor(mDragOrientationHR);
                }
                mOverScrollHintHorizontal.setText(String.valueOf(overScrollX));
            }
        });
        mOvershootLayoutVertical = (OvershootLayout) findViewById(R.id.overshoot_layout_v);
        mOvershootLayoutVertical.setOverScrollListener(new OvershootLayout.OverScrollListener() {

            @Override
            public void onOverScrolled(int overScrollX, int overScrollY) {
                if (0 == overScrollY) {
                    mOverScrollHintVertical.setTextColor(mDragOrientationVC);
                } else if (0 > overScrollY) {
                    mOverScrollHintVertical.setTextColor(mDragOrientationVT);
                } else if (0 < overScrollY) {
                    mOverScrollHintVertical.setTextColor(mDragOrientationVB);
                }
                mOverScrollHintVertical.setText(String.valueOf(overScrollY));
            }
        });
    }

    /**
     * */
    private class InnerAdapterHorizontal extends SimpleRecyclerAdapter<DemoItem, InnerViewHolder> {

        public InnerAdapterHorizontal() {
            setInnerSet(DemoContentHelper.getSpectrumItems(getResources()));
        }

        @Override
        public InnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_over_scroll_horizontal, parent, false);
            return new InnerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(InnerViewHolder viewHolder, int position) {
            DemoItem demoItem = getInnerSet().get(position);
            viewHolder.initAllDatum(demoItem);
        }

        @Override
        public int getItemCount() {
            return getInnerSet().size();
        }
    }


    /**
     * */
    private class InnerAdapterVertical extends SimpleRecyclerAdapter<DemoItem, InnerViewHolder> {

        public InnerAdapterVertical() {
            setInnerSet(DemoContentHelper.getReverseSpectrumItems(getResources()));
        }

        @Override
        public InnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_over_scroll_vertical, parent, false);
            return new InnerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(InnerViewHolder viewHolder, int position) {
            DemoItem demoItem = getInnerSet().get(position);
            viewHolder.initAllDatum(demoItem);
        }

        @Override
        public int getItemCount() {
            return getInnerSet().size();
        }
    }

    /**
     * */
    private class InnerViewHolder extends SimpleRecyclerViewHolder<DemoItem> {

        public TextView mTextView;

        public InnerViewHolder(View itemView) {
            super(itemView);
            initAllViews(itemView);
        }

        @Override
        public void initAllViews(View itemView) {
            mTextView = (TextView) itemView.findViewById(R.id.item_text_view);
        }

        @Override
        public void initAllDatum(DemoItem demoItem) {
            if (null == demoItem) {
                return;
            }
            mTextView.setBackgroundColor(demoItem.getColor());
            mTextView.setText(demoItem.getColorName());
        }
    }
}
