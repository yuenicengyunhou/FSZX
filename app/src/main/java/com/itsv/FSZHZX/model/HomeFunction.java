package com.itsv.FSZHZX.model;

import android.graphics.drawable.Drawable;

public class HomeFunction {

    private String functionName;
    private Drawable icon;
    private int count;

    public HomeFunction(String functionName, Drawable icon, int count) {
        this.functionName = functionName;
        this.icon = icon;
        this.count = count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFunctionName() {
        return functionName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public int getCount() {
        return count;
    }
}
