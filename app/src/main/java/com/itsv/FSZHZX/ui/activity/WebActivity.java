package com.itsv.FSZHZX.ui.activity;


import android.content.Intent;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.base.BaseWebActivity;
import com.itsv.FSZHZX.base.Constant;

import com.itsv.FSZHZX.utils.WxShareUtils;
import com.tencent.smtt.sdk.QbSdk;


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
        if (null == url) return "";
        hideShareButton(url);
        if (title != null) {
            hideShareButton(title);
//            if (!title.equals("学习材料")) {
                tvTitle.setText(title);
//            }
        }
        return url;
    }

    private void hideShareButton(String string) {
        if (string.equals("在线学习") || string.equals("学习资料") || string.equals("使用帮助") || string.contains("learning_materials") || string.contains("getTianListView")
                || string.contains("getJifenByUserName") || string.contains("getDanganByUserName") || string.contains("getFileListView") || string.contains("getSqmyListView")||string.contains(Constant.TAG_CPPCC)||string.contains(Constant.TAG_NOTICE)) {
            ivShare.setVisibility(View.GONE);
            if (string.contains(Constant.TAG_CPPCC) || string.contains(Constant.TAG_NOTICE)) {
                initX5();
            }
        }
    }

    @Override
    protected void beforeWebInit() {
        initToolbar(toolbarShare, false);
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
                WxShareUtils.shareWeb(this, Constant.APP_ID, url, webTitle, "", appIcon);
                break;
            case R.id.iv_back:
                mFinsh();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        mFinsh();
    }

    private void mFinsh() {
        if (url.contains("getTianListView")||url.contains(Constant.TAG_FILE_EXCHANGE)||url.contains(Constant.TAG_CPPCC)||url.contains(Constant.TAG_NOTICE)) {
            boolean back = agentWeb.back();
            if (!back) {
                finish();
            }
        } else {
            finish();
        }
    }
    private void initX5() {
        //非wifi情况下，主动下载x5内核
        QbSdk.setDownloadWithoutWifi(true);
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            }

            @Override
            public void onCoreInitFinished() {

            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(this, cb);
    }
}
