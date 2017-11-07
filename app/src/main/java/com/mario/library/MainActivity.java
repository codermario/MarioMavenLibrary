package com.mario.library;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setAdapter(new InnerAdapter());
        mRecyclerView.setHasFixedSize(true);
    }

    /**
     * */
    private class InnerAdapter extends RecyclerView.Adapter<InnerViewHolder> {

        @Override
        public InnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(MainActivity.this).inflate(android.R.layout.simple_list_item_1, null);
            return new InnerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(InnerViewHolder viewHolder, int position) {
            viewHolder.mTextView.setText(String.format(" ***********  %s", position + 1));
        }

        @Override
        public int getItemCount() {
            return 100;
        }
    }

    /**
     * */
    private class InnerViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public InnerViewHolder(View itemView) {
            super(itemView);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 52, getResources().getDisplayMetrics()));
            itemView.setLayoutParams(layoutParams);
            itemView.setBackgroundColor(Color.WHITE);
            mTextView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
