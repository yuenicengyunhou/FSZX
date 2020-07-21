package com.itsv.FSZHZX.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.itsv.FSZHZX.api.UserApi;
import com.itsv.FSZHZX.base.ApiHelper;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.model.SimpleModel;
import com.itsv.FSZHZX.ui.activity.HomeActivity;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenter implements MvpPresenter<HomeActivity> {
    private HomeActivity mvpView;
    private UIHandler handler;
    private String finalApkPath;
    private int sofar;
    private int totalByte;

    @SuppressLint("HandlerLeak")
    private class UIHandler extends Handler {
        private WeakReference<HomeActivity> mActivity;

        public UIHandler(HomeActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            HomeActivity activity = mActivity.get();
            if (activity == null) return;
            switch (msg.what) {
                case 0:
                    install(new File(finalApkPath));
//                    mvpView.showUpdateDialog(new File(finalApkPath));
                    break;
                case 1:
                    mvpView.setUpdateProgress(sofar*100/totalByte);
                    break;
            }
        }
    }

    @Override
    public void attachView(HomeActivity view) {
        mvpView = view;
        handler = new UIHandler(mvpView);
    }
    public void getSimpleProfile() {
//        mvpView.checkToken();
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.UserURL)
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
                            mvpView.initAvatar(data);
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

    public void checkAppUpdate(String apkPath) {
//        mvpView.checkToken();
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.appURL)
                .createService(UserApi.class);
        Call<ResponseBody> call = api.getAppVersion(Constant.TOKEN);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        JSONObject object = new JSONObject(params);
                        boolean success = object.getBoolean("success");
                        if (success) {
                            JSONObject data = object.getJSONObject("data");
                            String downloadURL = data.getString("downloadURL");
                            String version = data.getString("version");
                            long size = data.getLong("size");
                            totalByte = (int) size;
                            downloadApk(apkPath, downloadURL, version);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

    private void downloadApk(String apkDirPath, String downloadURL, String version) {
        int onlineVersion = Integer.decode(version);
        int appVersionCode = getAppVersionCode(mvpView);
        if (onlineVersion > appVersionCode) {
            mvpView.showUpdateDialog(downloadURL);
//            start_single(apkDirPath, downloadURL, "房山政协.apk");
//            showNewVirsionDialog(downloadURL, apkDirPath);
        } else {
            File file = new File(apkDirPath + File.separator + "房山政协.apk");
            if (file.exists() && file.isFile()) {
                boolean delete = file.delete();
                if (!delete) {
                    Log.e("WQ", "删除旧版安装包失败");
                }
            }
        }
    }

    public void start_single(String dirPath, String url, String fileName) {
        if (dirPath.equals("")) {
            mvpView.showErrorToast("更新应用路径错误");
            return;
        }
        String mSinglePath = dirPath
                + File.separator + fileName;
        //.setTag()
        BaseDownloadTask singleTask = FileDownloader.getImpl().create(url)
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
                        Log.e("feifei", "progress ==:" + soFarBytes+"---total"+totalByte);
                        sofar = soFarBytes/1024;
                        handler.sendEmptyMessage(1);
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                        Log.e("feifei", "blockComplete taskId:" + task.getId() + ",filePath:" + task.getPath() + ",fileName:" + task.getFilename() + ",speed:" + task.getSpeed() + ",isReuse:" + task.reuse());
                        finalApkPath = task.getPath();
                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {

                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                });

//        int singleTaskId = singleTask.start();
        singleTask.start();
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

    /**
     * 安装apk
     */
    public void install(File file) {
        if (!file.exists()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //在服务中开启actiivity必须设置flag
            Uri contentUri = FileProvider.getUriForFile(mvpView, "com.itsv.FSZHZX", file);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            Uri uri = Uri.parse("file://" + file.toString());
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        mvpView.startActivity(intent);
    }
    @Override
    public void detachView(boolean retainInstance) {
    }


}
