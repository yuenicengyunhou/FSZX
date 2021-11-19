package com.itsv.FSZHZX.ui.activity;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.base.MyBaseMvpActivity;
import com.itsv.FSZHZX.base.TagAliasOperatorHelper;
import com.itsv.FSZHZX.model.HomeFunction;
import com.itsv.FSZHZX.model.ProfileDetailsM;
import com.itsv.FSZHZX.model.SimpleModel;
import com.itsv.FSZHZX.presenter.HomePresenter;
import com.itsv.FSZHZX.ui.adapter.HomeAdapter;
import com.itsv.FSZHZX.utils.DesignUtils;
import com.itsv.FSZHZX.utils.FileUtils;
import com.itsv.FSZHZX.utils.ToastUtils;
import com.itsv.FSZHZX.view.HomeView;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
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
    public RecyclerView recyclerView;
    @BindView(R.id.textTest)
    TextView textTest;
    Drawable vidMeeting;
    Drawable mtNoti;
    Drawable addressBook;
    Drawable material;
    Drawable studyOnline;
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
    private final List<HomeFunction> homeFunctionList = new ArrayList<>();
    private String realName;
    private String positionName;
    private String weekCorrectRate;
    private String avatarUrl;
    public static boolean isForeground = false;
    private String apkDirPath;
    private TextView tvUpdate;
    private ProgressBar progressBar;
    boolean isDownloading = false;
    private ProfileDetailsM.DataBean userInfo;
    private String mEncodeRealName = "";
    private int mYear;
    private HomeAdapter homeAdapter;
    private SharedPreferences preferences;

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
        checkImei();
        readUserInfoCache();
        setJpushAlias();
        initToolbar(toolbar, false);
        initRecycler();
        initDisplayImage();
        presenter.getSimpleProfile();
        questPermission();
        EventBus.getDefault().register(this);
    }


    /**
     * 读取存储的userInfo数据
     */
    private void readUserInfoCache() {
        String userInfoParams = preferences.getString("userInfo", "");
        if (TextUtils.isEmpty(userInfoParams)) {
            return;
        }
        ProfileDetailsM profileDetailsM = new Gson().fromJson(userInfoParams, ProfileDetailsM.class);
        userInfo = profileDetailsM.getData();
        long id = userInfo.getId();
        SharedPreferences.Editor edit = preferences.edit();
        edit.putLong("userId", id);
        edit.apply();
    }

    /**
     * 请求未读数量
     */
    private void refreshUnreadCount() {
        if (null == userInfo) {
            readUserInfoCache();
        }
        try {
            presenter.getUnreadNoticeCount(String.valueOf(userInfo.getId()), URLEncoder.encode(userInfo.getName(), "UTF-8"));
            presenter.getUnreadCppccFileCount(String.valueOf(userInfo.getId()), URLEncoder.encode(userInfo.getName(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setJpushAlias() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        boolean aliasSuccess = sharedPreferences.getBoolean("aliasSuccess", false);
        if (!aliasSuccess) {
            String alias = sharedPreferences.getString("alias", "");
            TagAliasBean tagAliasBean = new TagAliasBean();
            tagAliasBean.action = ACTION_SET;
            tagAliasBean.alias = alias;
            tagAliasBean.isAliasAction = true;
            TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), sequence, tagAliasBean);
        }
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
    }

    @Override
    public void checkImei() {
        preferences = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        if (TextUtils.isEmpty(Constant.IMEI)) {
            String imei = preferences.getString("imei", "");
            if (TextUtils.isEmpty(imei)) {
                ToastUtils.showSingleToast("登录验证失效，请重新登录");
                finish();
            } else {
                Constant.IMEI = imei;
            }
        }
    }

    private void makeDir() {
        FileUtils fileUtils = new FileUtils(this);
        Constant.PDFPath = fileUtils.getCachePath();
        Constant.FilePath = fileUtils.createFileDir();
        apkDirPath = fileUtils.getApkDirPath();
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

    //  "学习材料":"https://www.fszxpt.cn:9530/learning_materials";
    @Override
    public void initRecycler() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        initDrawable();
//        Drawable[] homeIcons = new Drawable[]{vidMeeting, mtNoti, addressBook, studyOnline, quiz, proposal, drawableSituation, drawableScores, drawableFiles, drawableFileExchange, drawableCPPCC, drawableNotice};
        homeAdapter = new HomeAdapter(this, homeFunctionList);
        recyclerView.setAdapter(homeAdapter);
        homeAdapter.setOnFunctionClickListener(title -> {
            if (!title.equals("在线答题") && !title.equals("视频会议") && !title.equals("会议通知") && !title.equals("云通讯录") && !title.equals("在线学习")) {
                if (null == realName || TextUtils.isEmpty(realName) || 0 == userInfo.getId()) {
                    ToastUtils.showSingleToast("用户信息获取失败");
                    return;
                }
            }
            switch (title) {
                case "在线答题":
                    Intent quizItent = new Intent(this, SortQuizActivity.class);
                    startActivity(quizItent);
                    break;
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
                case "提案查询":
                    toWebActivityWithUrl(MessageFormat.format("{0}{1}{2}&userName={3}", Constant.BASE_H5_URL, Constant.TAG_PROPOSAL, Constant.param_proposal, mEncodeRealName));
                    break;
                case "社情民意":
                    toWebActivityWithUrl(MessageFormat.format("{0}{1}{2}&userName={3}&userId={4}", Constant.BASE_H5_URL, Constant.TAG_SITUATION, Constant.param_situation, mEncodeRealName, userInfo.getId()));
                    break;
                case "履职积分":
                    int year = Calendar.getInstance().get(Calendar.YEAR);
                    toWebActivityWithUrl(MessageFormat.format("{0}{1}{2}&year={3}&userName={4}&userId={5}", Constant.BASE_H5_URL, Constant.TAG_DUTY_SCORE, Constant.paramScore, year, mEncodeRealName, userInfo.getId()));
                    break;
                case "履职档案":
                    toWebActivityWithUrl(MessageFormat.format("{0}{1}{2}&year={3}&userName={4}&userId={5}", Constant.BASE_H5_URL, Constant.TAG_DUTY_FILE, Constant.listDutyFile, mYear, mEncodeRealName, userInfo.getId()));
                    break;
                case "文件互传":
                    toWebActivityWithUrl(MessageFormat.format("{0}{1}{2}&userName={3}&userId={4}", Constant.BASE_H5_URL, Constant.TAG_FILE_EXCHANGE, Constant.listFileEXchange, mEncodeRealName, userInfo.getId()));
                    break;
                case "政协文件":
                    toWebActivityWithUrl(MessageFormat.format("{0}{1}{2}&userName={3}&userId={4}", Constant.BASE_H5_URL, Constant.TAG_CPPCC, Constant.listCPPCC, mEncodeRealName, userInfo.getId()));
                    break;
                case "通知公告":
//                    String format = MessageFormat.format("{0}{1}{2}&userName={3}&userId={4}", Constant.BASE_H5_URL, Constant.TAG_NOTICE, Constant.listNotice, mEncodeRealName, userInfo.getId());
                    toWebActivityWithUrl(MessageFormat.format("{0}{1}{2}&userName={3}&userId={4}", Constant.BASE_H5_URL, Constant.TAG_NOTICE, Constant.listNotice, mEncodeRealName, userInfo.getId()));
                    break;
            }
        });
    }

    private void toWebActivityWithUrl(String url) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    private void initDrawable() {
        vidMeeting = ContextCompat.getDrawable(this, R.mipmap.ic_vidmeet);
        mtNoti = ContextCompat.getDrawable(this, R.mipmap.ic_mtnoti);
        addressBook = ContextCompat.getDrawable(this, R.mipmap.ic_addressbook);

//        material = ContextCompat.getDrawable(this, R.mipmap.ic_material);
        studyOnline = ContextCompat.getDrawable(this, R.mipmap.ic_studyonline);
        quiz = ContextCompat.getDrawable(this, R.mipmap.ic_quiz);
        Drawable proposal = ContextCompat.getDrawable(this, R.drawable.ic_proposal);

        Drawable drawableScores = ContextCompat.getDrawable(this, R.drawable.ic_scores);
        Drawable drawableFiles = ContextCompat.getDrawable(this, R.drawable.ic_files);
        Drawable drawableSituation = ContextCompat.getDrawable(this, R.drawable.ic_situation);

        Drawable drawableFileExchange = ContextCompat.getDrawable(this, R.drawable.ic_file_exchange);
        Drawable drawableCPPCC = ContextCompat.getDrawable(this, R.mipmap.ic_cppcc);
        Drawable drawableNotice = ContextCompat.getDrawable(this, R.mipmap.ic_notice);
//        private final String[] titles = {"视频会议", "会议通知", "云通讯录", "在线学习", "在线答题", "提案查询", "社情民意", "履职积分", "履职档案", "文件互传", "政协文件", "通知公告"};

        homeFunctionList.add(new HomeFunction("视频会议", vidMeeting, 0));
        homeFunctionList.add(new HomeFunction("会议通知", mtNoti, 0));
        homeFunctionList.add(new HomeFunction("云通讯录", addressBook, 0));
        homeFunctionList.add(new HomeFunction("在线学习", studyOnline, 0));
        homeFunctionList.add(new HomeFunction("在线答题", quiz, 0));
        homeFunctionList.add(new HomeFunction("提案查询", proposal, 0));
        homeFunctionList.add(new HomeFunction("社情民意", drawableScores, 0));
        homeFunctionList.add(new HomeFunction("履职积分", drawableFiles, 0));
        homeFunctionList.add(new HomeFunction("履职档案", drawableSituation, 0));
        homeFunctionList.add(new HomeFunction("文件互传", drawableFileExchange, 0));
        homeFunctionList.add(new HomeFunction("政协文件", drawableCPPCC, 0));
        homeFunctionList.add(new HomeFunction("通知公告", drawableNotice, 0));
    }


    @Override
    public void initDisplayImage() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DesignUtils.getScreenWidth(this) * 39 / 69);
        ivDisplay.setLayoutParams(params);
    }

    @Override
    public void initAvatar(SimpleModel.DataBean data) {
        avatarUrl = data.getAvatarUrl();
        realName = data.getName();
        Constant.USER_NAME = realName;
        positionName = data.getPositionName();
        weekCorrectRate = data.getWeekCorrectRate();
        Glide.with(this).load(avatarUrl).placeholder(icHead).into(ivHead);
        encodeTwiceRealName(realName);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNewName(String newName) {
        realName = newName;
        Constant.USER_NAME = newName;
        encodeTwiceRealName(realName);
    }

    /**
     * 用户真实姓名url转码两次，为后面的跳转链接做准备
     */
    private void encodeTwiceRealName(String realName) {
        try {
            mEncodeRealName = URLEncoder.encode(URLEncoder.encode(realName, "UTF-8"), "UTF-8");
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("encodeRealName", mEncodeRealName);
            edit.apply();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void updateNoticeCount(int noticeCount) {
        if (null != homeAdapter) {
            homeFunctionList.get(homeFunctionList.size() - 1).setCount(noticeCount);
            homeAdapter.updateList(homeFunctionList, homeFunctionList.size() - 1);
        }
    }

    public void updateCppccCount(int count) {
        if (null != homeAdapter) {
            homeFunctionList.get(homeFunctionList.size() - 2).setCount(count);
            homeAdapter.updateList(homeFunctionList, homeFunctionList.size() - 2);
        }
    }

    @Override
    public void showUpdateDialog(String downloadURL) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_update, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setCancelable(false);
        progressBar = view.findViewById(R.id.update_progress);
        tvUpdate = view.findViewById(R.id.update_text);
        tvUpdate.setOnClickListener(v -> {
            if (isDownloading) {
                ToastUtils.showSingleToast("正在下载，请稍后");
            } else {
                presenter.start_single(apkDirPath, downloadURL, "房山政协.apk");
                isDownloading = true;
            }
        });
        builder.setOnDismissListener(dialog -> {
            if (isDownloading) {
                ToastUtils.showSingleToast("后台下载中...");
            }
        });
        builder.show();
    }

    @Override
    public void setUpdateProgress(int progress) {
        if (null != progressBar) {
            if (progressBar.getVisibility() == View.INVISIBLE) {
                progressBar.setVisibility(View.VISIBLE);
            }
            progressBar.setProgress(progress);
        }
        if (null != tvUpdate) {
            if (progress > 85 && progress < 90) {
                tvUpdate.setText("下载完成后将自行安装");
            } else {
                tvUpdate.setText(MessageFormat.format("{0}%", progress));
            }
        }
    }

    @Override
    public void setDialogViewAfterDownloaded() {
        if (null != tvUpdate) {
            tvUpdate.setText("立即安装");
        }
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
            intent.putExtra("name", realName);
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
        mYear = Calendar.getInstance().get(Calendar.YEAR);
        refreshUnreadCount();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
    }

    @Override
    public void finish() {
        super.finish();
        EventBus.getDefault().unregister(this);
    }
}