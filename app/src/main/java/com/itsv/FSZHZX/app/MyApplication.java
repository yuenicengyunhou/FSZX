package com.itsv.FSZHZX.app;

import android.app.Application;

import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.utils.AppUtils;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.manis.core.interfaces.ManisApiInterface;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.jpush.android.api.JPushInterface;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppUtils.init(this);
        CrashHandler.getInstance().init(this);
        initManis();
        initDownloader();
        initWechatShare();
        initJPush();
    }

    private void initJPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    private void initManis() {
        ManisApiInterface.init(this, "https://bj189.scmeeting.com/");
    }

    private void initWechatShare() {
        IWXAPI mWxApi = WXAPIFactory.createWXAPI(this, Constant.APP_ID, true);
// 注册
        mWxApi.registerApp(Constant.APP_ID);
    }

    private void initDownloader() {
        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                ))
                .commit();
    }

}
