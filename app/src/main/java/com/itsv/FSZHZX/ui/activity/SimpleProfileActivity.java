package com.itsv.FSZHZX.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.base.MyBaseMvpActivity;
import com.itsv.FSZHZX.presenter.SimpleProfilePre;
import com.itsv.FSZHZX.view.SimpleView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.MessageFormat;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

public class SimpleProfileActivity extends MyBaseMvpActivity<SimpleProfileActivity, SimpleProfilePre> implements SimpleView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.headImage)
    RoundedImageView headImage;
    @BindView(R.id.simple_name)
    TextView simpleName;
    @BindView(R.id.simple_position)
    TextView simplePosition;
    @BindView(R.id.simple_rate)
    TextView simpleRate;
    @BindView(R.id.simple_modify)
    TextView simpleModify;
    @BindView(R.id.simple_logout)
    Button simpleLogout;
    @BindString(R.string.mine)
    String profile;
    @BindDrawable(R.mipmap.headbg)
    Drawable icHead;

    private SimpleProfilePre presenter;


    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        presenter=new SimpleProfilePre();
        return presenter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_simple_profile;
    }

    @Override
    protected void initViewsAndEnvents() {
        tvTitle.setText(profile);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String positionName = intent.getStringExtra("positionName");
        String weekCorrectRate = intent.getStringExtra("weekCorrectRate");
        String avatarUrl = intent.getStringExtra("avatarUrl");
        loadViews( name, positionName, weekCorrectRate, avatarUrl);
    }

    @OnClick({ R.id.simple_modify, R.id.simple_logout,R.id.toProfile})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.toProfile:
                toActivity(ProfileActivity.class);
                break;
            case R.id.simple_modify:
                toActivity(PasswordActivity.class);
                break;
            case R.id.simple_logout:
                presenter.logout();
                break;
        }
    }



    @Override
    public void loadViews( String name, String positionName, String rate,String avatarUrl) {
        simpleName.setText(name);
        Glide.with(this).load(avatarUrl).placeholder(icHead).into(headImage);
        simplePosition.setText(positionName);
        simpleRate.setText(MessageFormat.format("周学习答题正确率：{0}", rate));
    }

    @Override
    public void logout() {
        SharedPreferences fszx = getSharedPreferences("fszx", MODE_PRIVATE);
        SharedPreferences.Editor edit = fszx.edit();
        edit.clear();
        edit.apply();
        appManager.clearExceptFirst();
    }
}
