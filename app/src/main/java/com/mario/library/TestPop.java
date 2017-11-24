package com.mario.library;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.coder.mario.library.pop.BasePopupWindow;
import com.coder.mario.library.util.DimensionUtils;

/**
 * Created by CoderMario on 2017-11-22.
 */
public class TestPop extends BasePopupWindow {

    public TestPop(Context context) {
        super(context);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(DimensionUtils.dp2valueInt(context, 96));
        setMaskLayerColor(Color.parseColor("#80000000"));
        setAnimationStyle(R.style.Animation_PopupWindow_Input_Method);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.pop_test, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(view);

        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
