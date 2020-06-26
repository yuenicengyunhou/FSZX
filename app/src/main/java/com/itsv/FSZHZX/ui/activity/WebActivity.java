package com.itsv.FSZHZX.ui.activity;



import android.content.Intent;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.base.BaseWebActivity;
import com.itsv.FSZHZX.base.Constant;

import com.itsv.FSZHZX.utils.WxShareUtils;


import butterknife.BindBitmap;
import butterknife.BindView;
import butterknife.OnClick;

public class WebActivity extends BaseWebActivity {

    //    private WebInfor webInfor;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.toolbar_share)
    Toolbar toolbarShare;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindBitmap(R.mipmap.app_icon)
    Bitmap appIcon;
    private String url;

    @Override
    protected boolean initWeb() {
        return true;
    }


    @Override
    protected String getWebLink(Gson gson) {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        url = intent.getStringExtra("url");
        hideShareButton(url);
        if (title != null) {
            hideShareButton(title);
            if (!title.equals("学习材料")) {
                tvTitle.setText(title);
            }
        }
        return url;
    }

    private void hideShareButton(String string) {
        if (string.equals("在线学习") || string.equals("学习材料") || string.equals("使用帮助") || string.contains("learning_materials")) {
            ivShare.setVisibility(View.GONE);
        }
    }

    @Override
    protected void beforeWebInit() {
        initToolbar(toolbarShare, false);
//
    }

    @Override
    protected void afterWebInit() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_web;
    }

    @OnClick({R.id.iv_share, R.id.iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_share:
                WxShareUtils.shareWeb(this,Constant.APP_ID,url,webTitle,"",appIcon);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

}
