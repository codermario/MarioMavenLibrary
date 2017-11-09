package com.mario.library;

public class DemoItem {

    private int mColor;
    private String mColorName;

    public DemoItem(int color, String colorName) {
        mColorName = colorName;
        mColor = color;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public String getColorName() {
        return mColorName;
    }

    public void setColorName(String colorName) {
        mColorName = colorName;
    }
}
