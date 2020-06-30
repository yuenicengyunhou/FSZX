package com.itsv.FSZHZX.ui.activity;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.base.MyBaseMvpActivity;
import com.itsv.FSZHZX.base.TagAliasOperatorHelper;
import com.itsv.FSZHZX.model.SimpleModel;
import com.itsv.FSZHZX.presenter.HomePresenter;
import com.itsv.FSZHZX.ui.adapter.HomeAdapter;
import com.itsv.FSZHZX.utils.DesignUtils;
import com.itsv.FSZHZX.utils.FileUtils;
import com.itsv.FSZHZX.utils.ToastUtils;
import com.itsv.FSZHZX.view.HomeView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

import static com.itsv.FSZHZX.base.TagAliasOperatorHelper.ACTION_SET;
import static com.itsv.FSZHZX.base.TagAliasOperatorHelper.TagAliasBean;
import static com.itsv.FSZHZX.base.TagAliasOperatorHelper.sequence;

@RuntimePermissions
public class HomeActivity extends MyBaseMvpActivity<HomeActivity, HomePresenter> implements HomeView {
    @BindView(R.id.homeRecycler)
    RecyclerView recyclerView;
    @BindDrawable(R.mipmap.ic_vidmeet)
    Drawable vidMeeting;
    @BindDrawable(R.mipmap.ic_mtnoti)
    Drawable mtNoti;
    @BindDrawable(R.mipmap.ic_addressbook)
    Drawable addressBook;
    @BindDrawable(R.mipmap.ic_material)
    Drawable material;
    @BindDrawable(R.mipmap.ic_studyonline)
    Drawable studyOnline;
    @BindDrawable(R.mipmap.ic_quiz)
    Drawable quiz;
    @BindView(R.id.ivDisplay)
    ImageView ivDisplay;
    @BindView(R.id.toolbar_home)
    Toolbar toolbar;
    @BindView(R.id.iv_head)
    RoundedImageView ivHead;
    @BindDrawable(R.mipmap.head)
    Drawable icHead;

    private HomePresenter presenter;
    private String[] titles = {"视频会议", "会议通知", "云通讯录", "学习材料", "在线学习", "在线答题"};
    private String name;
    private String positionName;
    private String weekCorrectRate;
    private String avatarUrl;
    public static boolean isForeground = false;

    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        presenter = new HomePresenter();
        return presenter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_home;
    }

    @Override
    protected void initViewsAndEnvents() {
        checkToken();
        setJpushAlias();
        initToolbar(toolbar, false);
        initRecycler();
        initDisplayImage();
        presenter.getSimpleProfile();
        questPermission();
    }

    @Override
    public void setJpushAlias() {
        if (TextUtils.isEmpty(Constant.IMEI)) {
            ToastUtils.showSingleToast("设备码获取失败");
            finish();
            return;
        }
//        Log.e("WQ", "-----" + Constant.IMEI);
//        TagAliasBean tagAliasBean = new TagAliasBean();
//        tagAliasBean.action = ACTION_SET;
//        tagAliasBean.alias = Constant.IMEI;
//        tagAliasBean.isAliasAction = true;
//        TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), sequence, tagAliasBean);
    }

    @Override
    public void checkToken() {
        SharedPreferences preferences = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        if (TextUtils.isEmpty(Constant.TOKEN)) {
            String token = preferences.getString("token", "");
            if (TextUtils.isEmpty(token)) {
                ToastUtils.showSingleToast("登录验证失效，请重新登录");
                finish();
            } else {
                Constant.TOKEN = token;
            }
        }
        if (TextUtils.isEmpty(Constant.IMEI)) {
            String token = preferences.getString("imei", "");
            if (TextUtils.isEmpty(token)) {
                ToastUtils.showSingleToast("登录验证失效，请重新登录");
                finish();
            } else {
                Constant.IMEI = token;
            }
        }
    }


    private void makeDir() {
        FileUtils fileUtils = new FileUtils(this);
        Constant.PDFPath = fileUtils.getCachePath();
        Constant.FilePath = fileUtils.createFileDir();
        String apkDirPath = fileUtils.getApkDirPath();
        presenter.checkAppUpdate(apkDirPath);
    }

    @Override
    public void questPermission() {
        if (Build.VERSION.SDK_INT >= 23 && getApplicationInfo().targetSdkVersion >= 23) {
            HomeActivityPermissionsDispatcher.allowedWithPermissionCheck(this);
        } else {
            makeDir();
        }
    }

    @Override
    public void initRecycler() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        List<Drawable> icons = new ArrayList<>();
        icons.add(vidMeeting);
        icons.add(mtNoti);
        icons.add(addressBook);
        icons.add(material);
        icons.add(studyOnline);
        icons.add(quiz);
        HomeAdapter homeAdapter = new HomeAdapter(this, titles, icons);
        recyclerView.setAdapter(homeAdapter);
        homeAdapter.setOnFunctionClickListener(title -> {
            switch (title) {
//                case "在线答题":
//                    ToastUtils.showSingleToast("功能建设中，后续开放");
//                    break;
//                case "视频会议":
//                    toMeeting(title);
//                    break;
                case "视频会议":
                case "会议通知":
                    toMeeting(title);
                    break;
                case "云通讯录":
                    toAddress();
                    break;
                case "在线学习":
                    Intent intent = new Intent(this, WebActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("url", "https://www.fszxpt.cn:9530/online_learning");
                    startActivity(intent);
                    break;
                case "在线答题":
                case "学习材料":
                    ToastUtils.showSingleToast("功能建设中，后续开放");
                    break;
            }
        });
    }

    @Override
    public void initDisplayImage() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DesignUtils.getScreenWidth(this) * 39 / 69);
        ivDisplay.setLayoutParams(params);
    }

    @Override
    public void initAvatar(SimpleModel.DataBean data) {
        avatarUrl = data.getAvatarUrl();
        name = data.getName();
        Constant.USER_NAME = name;
        positionName = data.getPositionName();
        weekCorrectRate = data.getWeekCorrectRate();
        Glide.with(this).load(avatarUrl).placeholder(icHead).into(ivHead);
    }

    @Override
    public void showUpdateDialog(File file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("版本更新");
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        builder.setPositiveButton("立即更新", (dialog, which) -> {
            presenter.install(file);
            dialog.dismiss();
        });
        builder.show();
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MODIFY_AUDIO_SETTINGS, //扬声器权限
            Manifest.permission.CHANGE_NETWORK_STATE, // 改变网络状态权限
            Manifest.permission.ACCESS_NETWORK_STATE, // 获取网络状态权限
            Manifest.permission.BLUETOOTH})
    void allowed() {
        makeDir();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HomeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MODIFY_AUDIO_SETTINGS, //扬声器权限
            Manifest.permission.CHANGE_NETWORK_STATE, // 改变网络状态权限
            Manifest.permission.ACCESS_NETWORK_STATE, // 获取网络状态权限
            Manifest.permission.BLUETOOTH})
    void denied() {
    }

    @OnClick({R.id.iv_head})
    public void onClick(View v) {
        if (v.getId() == R.id.iv_head) {
            Intent intent = new Intent(this, SimpleProfileActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("positionName", positionName);
            intent.putExtra("weekCorrectRate", weekCorrectRate);
            intent.putExtra("avatarUrl", avatarUrl);
            startActivity(intent);
        }
    }

    private void toMeeting(String title) {
        Intent intent = new Intent(this, MtNotifyActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("userName", Constant.USER_NAME);
        startActivity(intent);
    }

    private void toAddress() {
        Intent intent = new Intent(this, AddressBookActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
    }
}