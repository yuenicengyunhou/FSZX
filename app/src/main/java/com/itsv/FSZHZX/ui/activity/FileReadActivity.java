package com.itsv.FSZHZX.ui.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.base.BaseAppCompatActivity;
import com.itsv.FSZHZX.ui.js.JsCallAndroid;
import com.itsv.FSZHZX.utils.FileUtils;
import com.itsv.FSZHZX.utils.ToastUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.tencent.smtt.sdk.TbsReaderView;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
//import com.ycbjie.webviewlib.widget.FileReaderView;

import org.w3c.dom.Text;

import java.io.File;
import java.lang.ref.WeakReference;

public class FileReadActivity extends BaseAppCompatActivity {

//    private FileReaderView fileReaderView;
    private final String dirPath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + "DocumentCache" + File.separator;
    private String mSinglePath = "";
    private UIHandler handler;
    private FrameLayout frameLayout;
    private TbsReaderView readerView;

    @SuppressLint("HandlerLeak")
    private class UIHandler extends Handler {
        WeakReference<Activity> weakReference;

        public  UIHandler(Activity activity) {
            weakReference = new WeakReference<>(activity);
        }
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            Activity activity = weakReference.get();
            if (null==activity)return;
            if (msg.what == 0) {

            } else if (msg.what == 1) {
                String fileUrl = (String) msg.obj;
                Toast.makeText(FileReadActivity.this, "下载失败,正在尝试浏览器下载", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri uri = Uri.parse(fileUrl);
                intent.setData(uri);
                startActivity(intent);
            }
        }
    };
    @Override
    protected int getLayoutID() {
        return R.layout.activity_file_read;
    }

    @Override
    protected void initViewsAndEnvents() {
        frameLayout = findViewById(R.id.frameLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        initToolbar(toolbar, false);
        TextView tvTitle = findViewById(R.id.tv_title);
        ImageView ivBack = findViewById(R.id.iv_back);
        ImageView ivDownload = findViewById(R.id.iv_download);
        ivBack.setOnClickListener(v -> finish());
        tvTitle.setText("附件详情");
        Intent intent = getIntent();
        String filePath = intent.getStringExtra("filePath");
        String fileType = intent.getStringExtra("fileType");
        String fileName = intent.getStringExtra("fileName");
        openFile(filePath,fileType);
        ivDownload.setOnClickListener(view -> {
             FileUtils.copyFileToDownloadDir(this, filePath, fileName);
        });
//        initX5Settings();
//        intent.getStringExtra("")
//        fileReaderView.show(filePath);

    }
    private void openFile(String filePath, String fileType) {
        //通过bundle把文件传给x5,打开的事情交由x5处理
        Bundle bundle = new Bundle();
        //传递文件路径
        bundle.putString("filePath", filePath);
        //临时的路径
//        bundle.putString("tempPath", Constants.DOCUMENT_DOWNLOAD_PATH);
        readerView = new TbsReaderView(this, (integer, o, o1) -> {
        });
        //加载文件前的初始化工作,加载支持不同格式的插件
        boolean b = readerView.preOpen(fileType, false);
        if (b) {
            readerView.openFile(bundle);
        }
        // 往容器里添加TbsReaderView控件
        frameLayout.addView(readerView);
    }

//    private String getFileType(String path) {
//        return null;
//    }

//    private void initX5Settings() {
//        webView.getSettings().setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
//        webView.getSettings().setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
//        webView.getSettings().setDisplayZoomControls(true); //隐藏原生的缩放控件
//        webView.getSettings().setBlockNetworkImage(false);//解决图片不显示
//        webView.getSettings().setLoadsImagesAutomatically(true); //支持自动加载图片
//        webView.getSettings().setDefaultTextEncodingName("utf-8");//设置编码格式
//
////        String url="http://47.95.243.116/#/bigScreen";
////        webView.loadUrl(url);
//////        Logger.d("监控界面加载的url为: " + url);
////
////        //该界面打开更多链接
////        webView.setWebViewClient(new WebViewClient() {
////
////            @Override
////            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
////                webView.loadUrl(s);
////                return true;
////            }
////        });
////        //监听网页的加载进度
////        webView.setWebChromeClient(new WebChromeClient() {
////            @Override
////            public void onProgressChanged(WebView webView, int i) {
////            }
////        });
//
//
//    }



    @Override
    protected void onStop() {
        super.onStop();
        if (null != readerView) {
            readerView.onStop();
        }
    }

    public void start_single(String url, String fileName) {
        mSinglePath = dirPath
                + File.separator + fileName;
        //.setTag()
        BaseDownloadTask singleTask = FileDownloader.getImpl().create(url)
                .setPath(mSinglePath, false)
                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.e("feifei", "pending:-----" + ",soFarBytes:" + soFarBytes + ",totalBytes:" + totalBytes);

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                        Log.e("feifei", "blockComplete taskId:" + task.getId() + ",filePath:" + task.getPath() + ",fileName:" + task.getFilename() + ",speed:" + task.getSpeed() + ",isReuse:" + task.reuse());
                        handler.sendEmptyMessage(0);
//                        readFile(mSinglePath);
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
//                        handler.sendEmptyMessage(1);
                        Message message = new Message();
                        message.what = 1;
                        message.obj = url;
                        handler.sendMessage(message);
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        Message message = new Message();
                        message.what = 1;
                        message.obj = url;
                        handler.sendMessage(message);
                        Log.e("feifei", "warn taskId:" + task.getId());
                    }
                });

//        int singleTaskId = singleTask.start();
        singleTask.start();
    }




//    //销毁 放置内存泄漏
//    @Override
//    public void onDestroy() {
//        if (this.webView != null) {
//            webView.destroy();
//        }
//        super.onDestroy();
//    }

}

