package com.itsv.FSZHZX.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.github.barteksc.pdfviewer.util.Constants;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.base.BaseAppCompatActivity;
import com.itsv.FSZHZX.utils.FileUtils;
import com.itsv.FSZHZX.utils.ToastUtils;
import com.tencent.smtt.sdk.TbsReaderView;

public class FileReadActivity extends BaseAppCompatActivity {

//    private FileReaderView fileReaderView;
//    private final String dirPath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + "DocumentCache" + File.separator;
//    private String mSinglePath = "";
//    private UIHandler handler;
    private FrameLayout frameLayout;
    private TbsReaderView readerView;

//    @SuppressLint("HandlerLeak")
//    private class UIHandler extends Handler {
//        WeakReference<Activity> weakReference;
//
//        public  UIHandler(Activity activity) {
//            weakReference = new WeakReference<>(activity);
//        }
//        @SuppressLint("HandlerLeak")
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            Activity activity = weakReference.get();
//            if (null==activity)return;
//            if (msg.what == 0) {
//
//            } else if (msg.what == 1) {
//                String fileUrl = (String) msg.obj;
//                Toast.makeText(FileReadActivity.this, "下载失败,正在尝试浏览器下载", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.VIEW");
//                Uri uri = Uri.parse(fileUrl);
//                intent.setData(uri);
//                startActivity(intent);
//            }
//        }
//    };
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

    }
    private void openFile(String filePath, String fileType) {
        //通过bundle把文件传给x5,打开的事情交由x5处理
        Bundle bundle = new Bundle();
        //传递文件路径
        bundle.putString("filePath", filePath);
        //临时的路径
//        bundle.putString("tempPath", Constants.DOCUMENT_DOWNLOAD_PATH);
        bundle.putString("tempPath", Environment.getExternalStorageDirectory()
                .getPath()+"/dsadsa");
        readerView = new TbsReaderView(this, (integer, o, o1) -> {
        });
        //加载文件前的初始化工作,加载支持不同格式的插件
        boolean result = readerView.preOpen(fileType, false);
        ToastUtils.showSingleToast("result--" + result);
        if (result) {
            readerView.openFile(bundle);
        }
        // 往容器里添加TbsReaderView控件
        frameLayout.addView(readerView);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != readerView) {
            readerView.onStop();
        }
    }

}

