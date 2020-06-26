package com.itsv.FSZHZX.ui.activity;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.widget.ImageView;

import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.base.BaseAppCompatActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ShareActivity extends BaseAppCompatActivity {

    @BindView(R.id.toolbar_share)
    Toolbar toolbar;
    @BindView(R.id.iv_share)
    ImageView ivShare;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_share;
    }

    @Override
    protected void initViewsAndEnvents() {
        initToolbar(toolbar, false);
    }

    @OnClick(R.id.iv_share)
    public void share() {
        Intent wechatIntent = new Intent(Intent.ACTION_SEND);
        wechatIntent.setPackage("com.tencent.mm");
        wechatIntent.setType("text/plain");
        wechatIntent.putExtra(Intent.EXTRA_TEXT, "分享到微信的内容");
        startActivity(wechatIntent);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }
}
