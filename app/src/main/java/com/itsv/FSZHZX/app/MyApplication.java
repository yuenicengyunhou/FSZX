package com.itsv.FSZHZX.app;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.utils.AppUtils;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.manis.core.interfaces.ManisApiInterface;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;


public class MyApplication extends Application {

//    private static RefWatcher watcher;

    @Override
    public void onCreate() {
        super.onCreate();
        AppUtils.init(this);
//        CrashHandler.getInstance().init(this);
        initManis();
        initDownloader();
        initWechatShare();
        initJPush();
        initX5();
        CrashHandler.getInstance().init(this);
//        initYCWeb();
//        initTBS();
//        watcher = LeakCanary.install(this);
    }

    private void initX5() {
        // 在调用TBS初始化、创建WebView之前进行如下配置
        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
        //非wifi情况下，主动下载x5内核
        QbSdk.setDownloadWithoutWifi(true);
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
//                Log.e("WQ", "chushihua--" + arg0);
//                SharedPreferences preferences = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
//                SharedPreferences.Editor edit = preferences.edit();
//                edit.putBoolean("hasLoad", arg0);
//                edit.apply();
            }

            @Override
            public void onCoreInitFinished() {

            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                //tbs内核下载完成回调
                //但是只有i等于100才算完成，否则失败
                //此时大概率可能由于网络问题
                //如果失败可增加网络监听器
//                Log.e("WQ", "onDownloadFinish: " + i);
            }

            @Override
            public void onInstallFinish(int i) {
                //内核安装完成回调，通常到这里也算安装完成，但是在
                //极个别情况也会出现加载失败，比如笔者在公司内网下偶现，可以忽略
//                Log.e("WQ", "onInstallFinish: "+i);
            }

            @Override
            public void onDownloadProgress(int i) {
                //下载进度监听
//                Log.e("WQ", "onDownloadProgress: "+i );
            }
        });
    }

    /**
     * 初始化ycwebview，用于打开文档
     */
//    private void initYCWeb() {
//        X5WebUtils.init(this);
//        X5LogUtils.setIsLog(true);
////        X5WebUtils.initCache(this);
////        WebViewCacheDelegate.getInstance().init(new WebViewCacheWrapper.Builder(this));
//
//
//        //1.创建委托对象
//        WebViewCacheDelegate webViewCacheDelegate = WebViewCacheDelegate.getInstance();
//        //2.创建调用处理器对象，实现类
//        WebViewCacheWrapper.Builder builder = new WebViewCacheWrapper.Builder(this);
//        //设置缓存路径，默认getCacheDir，名称CacheWebViewCache
//        builder.setCachePath(new File(this.getCacheDir().toString(),"CacheWebView"))
//                //设置缓存大小，默认100M
//                .setCacheSize(1024*1024*100)
//                //设置本地路径
//                //.setAssetsDir("yc")
//                //设置http请求链接超时，默认20秒
//                .setConnectTimeoutSecond(20)
//                //设置http请求链接读取超时，默认20秒
//                .setReadTimeoutSecond(20)
//                //设置缓存为正常模式，默认模式为强制缓存静态资源
//                .setCacheType(WebCacheType.FORCE);
//        webViewCacheDelegate.init(builder);
//    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
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
