package com.itsv.FSZHZX.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class VideoSpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int top, left, right, bottom;

    public VideoSpacesItemDecoration(int top, int left, int right, int bottom) {
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
    }

    public void setSpaces(int top, int left, int right, int bottom) {
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = top;
        outRect.left = left;
        outRect.right = right;
        outRect.bottom = bottom;
    }
}