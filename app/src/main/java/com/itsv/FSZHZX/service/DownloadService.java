package com.itsv.FSZHZX.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.itsv.FSZHZX.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by snail
 * on 2017/12/7.
 * Todo 下载的服务
 */

public class DownloadService extends Service {

    //定义notify的id，避免与其它的notification的处理冲突
    private static final int NOTIFY_ID = 0;
    private static final String CHANNEL = "update";

    private final DownloadBinder binder = new DownloadBinder();
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private DownloadCallback callback;
    private boolean showNoti;

    //定义个更新速率，避免更新通知栏过于频繁导致卡顿
    private float rate = .0f;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        mNotificationManager.cancelAll();
        mNotificationManager = null;
        mBuilder = null;
    }

    /**
     * 和activity通讯的binder
     */
    public class DownloadBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    private void setNotification() {
        if (mNotificationManager == null)
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this, CHANNEL);
        mBuilder.setContentTitle("开始下载")
                .setContentText("正在连接服务器")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setOngoing(true)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 通知渠道的idhannel
            String id = "huiguniang";
            // 用户可以看到的通知渠道的名字.
            CharSequence name = "灰姑娘的更新通道";
            // 用户可以看到的通知渠道的描述
            String description = "用于显示更新进度";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(false);
//            mChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(false);
//            mChannel.setVibrationPattern(new long[]{200, 200, 200});
//            mChannel.setShowBadge(true);
            //最后在notificationmanager中创建该通知渠道
            mNotificationManager.createNotificationChannel(mChannel);
            mBuilder.setChannelId(id);
        }
        mNotificationManager.notify(NOTIFY_ID, mBuilder.build());
    }

    /**
     * 下载完成
     */
    private void complete(String msg) {
        if (mBuilder != null) {
            mBuilder.setContentTitle("新版本").setContentText(msg);
            Notification notification = mBuilder.build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            mNotificationManager.notify(NOTIFY_ID, notification);
        }
        stopSelf();
    }

    /**
     * 开始下载apk
     */
    public void downApk(String url, String fileName, DownloadCallback callback) {
        String fileLocation = Environment.getExternalStorageDirectory().getPath();
        if (fileLocation == null || TextUtils.isEmpty(fileLocation)) {
            return;
        }
        downApk(url, fileLocation, fileName, true, callback);
    }

    public void downApk(String url, String fileLocation, String fileName, boolean showNoti, DownloadCallback callback) {
        this.showNoti = showNoti;
        this.callback = callback;
        if (TextUtils.isEmpty(url)) {
            complete("下载路径错误");
            return;
        }
        if (showNoti) {
            setNotification();
        }
        handler.sendEmptyMessage(0);
        Request request = new Request.Builder().url(url).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Message message = Message.obtain();
                message.what = 1;
                message.obj = e.getMessage();
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.body() == null) {
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = "下载错误";
                    handler.sendMessage(message);
                    return;
                }

                InputStream is = null;
                byte[] buff = new byte[2048];
                int len;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = createFile(fileLocation, fileName);
                    fos = new FileOutputStream(file);
                    long sum = 0;

                    while ((len = is.read(buff)) != -1) {
                        fos.write(buff, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        if (rate != progress) {
                            Message message = Message.obtain();
                            message.what = 2;
                            message.obj = progress;
                            handler.sendMessage(message);
                            rate = progress;
                        }
                    }
                    fos.flush();
                    Message message = Message.obtain();
                    message.what = 3;
                    if (file == null) return;
                    message.obj = file.getAbsoluteFile();
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                        if (fos != null)
                            fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 路径为根目录
     * 创建文件名称为 updateDemo.apk
     */
    private File createFile(String filePath, String fileName) {
        File file = new File(filePath, fileName);
        if (file.exists())
            file.delete();
        try {
            file.createNewFile();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把处理结果放回ui线程
     */
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    callback.onPrepare();
                    break;

                case 1:
//                    mNotificationManager.cancel(NOTIFY_ID);
                    callback.onFail((String) msg.obj);
                    stopSelf();
                    break;

                case 2: {
                    int progress = (int) msg.obj;
                    callback.onProgress(progress);
                    if (showNoti) {
                        mBuilder.setContentTitle("正在下载：新版本...")
                                .setContentText(String.format(Locale.CHINESE, "%d%%", progress))
                                .setProgress(100, progress, false)
                                .setWhen(System.currentTimeMillis())
                                .setOnlyAlertOnce(true)
                                .setDefaults(Notification.FLAG_ONLY_ALERT_ONCE);
                        Notification notification = mBuilder.build();
                        notification.flags = Notification.FLAG_AUTO_CANCEL;
                        if (null != mNotificationManager) {
                            mNotificationManager.notify(NOTIFY_ID, notification);
                        }
                    }
                }
                break;

                case 3: {
                    callback.onComplete((File) msg.obj);
                    if (showNoti) {
                        //app运行在界面,直接安装
                        //否则运行在后台则通知形式告知完成
                        if (null != mNotificationManager) {
                            mNotificationManager.cancel(NOTIFY_ID);
                        }
                        if (onFront()) {
                            Log.e("Init", "前台");
//                            installAPK((File) msg.obj);
                        } else {
                            Intent intent = installIntent((File) msg.obj);
                            PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext()
                                    , 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            mBuilder.setContentIntent(pIntent)
                                    .setContentTitle(getPackageName())
                                    .setContentText("下载完成，点击安装")
                                    .setProgress(0, 0, false)
                                    .setDefaults(Notification.DEFAULT_ALL);
                            Notification notification = mBuilder.build();
                            notification.flags = Notification.FLAG_AUTO_CANCEL;
                            mNotificationManager.notify(NOTIFY_ID, notification);
                        }
                    }
                    stopSelf();
                }
                break;
            }
            return false;
        }
    });


    /**
     * 是否运行在用户前面
     */
    private boolean onFront() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null || appProcesses.isEmpty())
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(getPackageName()) &&
                    appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    private void installAPK(File file) {
        if (!file.exists()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //在服务中开启actiivity必须设置flag
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.itsv.FSZHZX", file);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            Uri uri = Uri.parse("file://" + file.toString());
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        getApplicationContext().startActivity(intent);
    }


    /**
     * 安装
     * 7.0 以上记得配置 fileProvider
     */
    private Intent installIntent(File file) {
        try {
            Uri fileUri = FileProvider.getUriForFile(getApplicationContext(), "com.itsv.FSZHZX", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            return intent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 销毁时清空一下对notify对象的持有
     */
    @Override
    public void onDestroy() {
        mNotificationManager = null;
        super.onDestroy();
    }


    /**
     * 定义一下回调方法
     */
    public interface DownloadCallback {
        void onPrepare();

        void onProgress(int progress);

        void onComplete(File file);

        void onFail(String msg);
    }
}
