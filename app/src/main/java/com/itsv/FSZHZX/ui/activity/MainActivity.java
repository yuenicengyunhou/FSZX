package com.itsv.FSZHZX.ui.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.api.UserApi;
import com.itsv.FSZHZX.base.ApiHelper;
import com.itsv.FSZHZX.base.BaseWebActivity;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.model.SimpleModel;
import com.itsv.FSZHZX.service.DownloadService;
import com.itsv.FSZHZX.utils.DesignUtils;
import com.itsv.FSZHZX.utils.FileUtils;
import com.itsv.FSZHZX.utils.ToastUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.Objects;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@RuntimePermissions
public class MainActivity extends BaseWebActivity {

    @BindView(R.id.toolbar_home)
    Toolbar toolbar;
    @BindView(R.id.iv_head)
    RoundedImageView ivHead;
    @BindDrawable(R.mipmap.head)
    Drawable icHead;
    private String apkDirPath = "";
    private String name = "";
    private String positionName = "";
    private String weekCorrectRate = "";
    private String avatarUrl;
    private UIHandler handler = new UIHandler(this);
    private String finalApkPath;
    private String mSinglePath;
    private BaseDownloadTask singleTask;
    private ServiceConnection serviceConnection;
    private ProgressBar progressBar;
    private TextView tvUpdate;


    @SuppressLint("HandlerLeak")
    private class UIHandler extends Handler {
        private WeakReference<MainActivity> mActivity;

        public UIHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity == null) return;
            if (msg.what == 0) {
                showUpdateDialog(new File(finalApkPath));
            }
        }
    }

    @Override
    protected void beforeWebInit() {
        if (TextUtils.isEmpty(Constant.TOKEN)) {
            SharedPreferences preferences = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
            String token = preferences.getString("token", "");
            if (TextUtils.isEmpty(token)) {
                ToastUtils.showSingleToast("登录验证失效，请重新登录");
                finish();
            } else {
                Constant.TOKEN = token;
            }
        }
        initToolbar(toolbar, false);
        if (Build.VERSION.SDK_INT >= 23 && getApplicationInfo().targetSdkVersion >= 23) {
            MainActivityPermissionsDispatcher.needsPermissionsWithPermissionCheck(this);
        } else {
            makeDir();
        }
    }


    public void start_single(String dirPath, String url, String fileName) {
        if (dirPath.equals("")) {
            Toast.makeText(this, "更新应用路径错误", Toast.LENGTH_SHORT).show();
            return;
        }
        mSinglePath = dirPath
                + File.separator + fileName;
        //.setTag()
        singleTask = FileDownloader.getImpl().create(url)
                .setPath(mSinglePath, false)
                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400)
                //.setTag()
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.e("feifei", "pending:-----" + ",soFarBytes:" + soFarBytes + ",totalBytes:" + totalBytes);

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.e("feifei", "progress" + task.getId() + ",soFarBytes:" + soFarBytes + ",totalBytes:" + singleTask.getSmallFileTotalBytes());
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                        Log.e("feifei", "blockComplete taskId:" + task.getId() + ",filePath:" + task.getPath() + ",fileName:" + task.getFilename() + ",speed:" + task.getSpeed() + ",isReuse:" + task.reuse());
                        finalApkPath = task.getPath();
                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Log.e("feifei", "completed taskId:" + task.getId() + ",isReuse:" + task.reuse());

                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.e("feifei", "paused taskId:" + task.getId() + ",soFarBytes:" + soFarBytes + ",totalBytes:" + totalBytes + ",percent:" + soFarBytes * 1.0 / totalBytes);
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.e("feifei", "error taskId:" + task.getId() + ",e:" + e.getLocalizedMessage());
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        Log.e("feifei", "warn taskId:" + task.getId());
                    }
                });

//        int singleTaskId = singleTask.start();
        singleTask.start();
    }

    @Override
    protected void afterWebInit() {

    }

    private void checkAppUpdate() {
        Log.e("WQ", "-----update");
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.appURL)
                .createService(UserApi.class);
        Call<ResponseBody> call = api.getAppVersion(Constant.TOKEN);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("WQ", "-----response");
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        JSONObject object = new JSONObject(params);
                        boolean success = object.getBoolean("success");
                        if (success) {
                            JSONObject data = object.getJSONObject("data");
                            String downloadURL = data.getString("downloadURL");
                            String version = data.getString("version");
                            downloadApk(downloadURL, version);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("WQ", "-----fail");
            }
        });
    }

    private void downloadApk(String downloadURL, String version) {
        int onlineVersion = Integer.decode(version);
        int appVersionCode = getAppVersionCode(this);
        if (onlineVersion > appVersionCode) {
            start_single(apkDirPath,downloadURL,"房山政协.apk");
//            showNewVirsionDialog(downloadURL, apkDirPath);
        } else {
            File file = new File(apkDirPath + File.separator + "房山政协.apk");
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        }
    }

    @Override
    protected boolean initWeb() {
        return true;
    }

    @Override
    protected String getWebLink(Gson gson) {
        return Constant.WEBURL;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
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
//                toActivity(SimpleProfileActivity.class);
//                agentWeb.clearWebCache();
        }
    }

    private void getSimpleProfile() {
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.BASEURL)
                .createService(UserApi.class);
        Call<ResponseBody> call = api.personalData(Constant.TOKEN);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        Gson gson = new Gson();

                        SimpleModel model = gson.fromJson(params, SimpleModel.class);
                        if (model.isSuccess()) {
                            SimpleModel.DataBean data = model.getData();
                            avatarUrl = data.getAvatarUrl();
                            name = data.getName();
                            Constant.USER_NAME = name;
                            positionName = data.getPositionName();
                            weekCorrectRate = data.getWeekCorrectRate();
                            Glide.with(MainActivity.this).load(avatarUrl).placeholder(icHead).into(ivHead);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }


    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MODIFY_AUDIO_SETTINGS, //扬声器权限
            Manifest.permission.CHANGE_NETWORK_STATE, // 改变网络状态权限
            Manifest.permission.ACCESS_NETWORK_STATE, // 获取网络状态权限
            Manifest.permission.BLUETOOTH})
    void needsPermissions() {
        makeDir();
    }

    private void makeDir() {
        FileUtils fileUtils = new FileUtils(this);
        Constant.PDFPath = fileUtils.getCachePath();
        Constant.FilePath = fileUtils.createFileDir();
        apkDirPath = fileUtils.getApkDirPath();
        getSimpleProfile();
        checkAppUpdate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void denied() {
    }


    private void myDownloadApk(final String versionURL,String apkLocation) {
        if (null == serviceConnection) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    DownloadService.DownloadBinder binder = (DownloadService.DownloadBinder) service;
                    DownloadService mService = binder.getService();
                    mService.downApk(versionURL,apkLocation ,"房山政协.apk",false, new DownloadService.DownloadCallback() {
                        @Override
                        public void onPrepare() {

                        }

                        @Override
                        public void onProgress(int progress) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.VISIBLE);
                                tvUpdate.setText(MessageFormat.format("正在下载：{0}%", progress));
                                tvUpdate.setEnabled(false);
                                progressBar.setProgress(progress);
                            }
                        }

                        @Override
                        public void onComplete(File file) {
                            install(file);
//                            if (null != dialog) {
//                                dialog.dismiss();
//                            }
//                            updateAppCount();
//                            installAPK(file, HomeActivity.this);
                        }

                        @Override
                        public void onFail(String msg) {
                            ToastUtils.showSingleToast(msg);
                        }
                    });
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    ToastUtils.showSingleToast("服务已断开，更新失败");
                }
            };
        } else {
            serviceConnection = null;
            myDownloadApk(versionURL,apkDirPath);
        }
        Intent intent = new Intent(this, DownloadService.class);
        bindService(intent, this.serviceConnection, Service.BIND_AUTO_CREATE);
    }


    /**
     * 获取当前版本号
     */
    private int getAppVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    private void showNewVirsionDialog(String url,String path) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.dialog_update, null);
        progressBar = view.findViewById(R.id.update_progress);
        tvUpdate = view.findViewById(R.id.update_text);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        Objects.requireNonNull(window).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        Objects.requireNonNull(window).setLayout(3 * DesignUtils.getScreenWidth(this) / 4, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvUpdate.setOnClickListener(v -> {
            myDownloadApk(url,path);
        });
    }
}
