package com.itsv.FSZHZX.ui.activity;



import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.base.BaseAppCompatActivity;
import com.itsv.FSZHZX.base.Constant;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PdfActivity extends BaseAppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener {

    private String mSinglePath;
    private int percent;

    @SuppressLint("HandlerLeak")
    private class UIHandler extends Handler {
        private WeakReference<PdfActivity> mActivity;
        public UIHandler(PdfActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            PdfActivity roomActivity = mActivity.get();
            if (roomActivity == null) return;
            switch (msg.what) {
                case 0:
                    if (null != pdfView) {
//                        progressBar.setVisibility(View.GONE);
//                        textView.setVisibility(View.GONE);
                        File file = new File(mSinglePath);
                        pdfView.fromFile(file)
                                //                .defaultPage(pageNumber)
//                .onPageChange(this)
//                .enableAnnotationRendering(true)
//                .onLoad(this)
//                .scrollHandle(new DefaultScrollHandle(this))
//                .spacing(10) // in dp
//                .onPageError(this)
                                .load();
                    }
                    break;
                case 1:
//                    if (null != textView) {
//                        textView.setText("解析进度" + (percent * 100) + "%");
//                    }
                    break;
            }
        }
    }

    private int pageNumber;

    @BindView(R.id.pdfView)
    PDFView pdfView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
//    @BindView(R.id.progressBar)
//    ProgressBar progressBar;
//    @BindView(R.id.pdf_text)
//    TextView textView;
    private String url;
    private Handler handler = new UIHandler(this);

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_pdf;
    }

    @Override
    protected void initViewsAndEnvents() {
        tvTitle.setText("附件详情");
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        String fileId = intent.getStringExtra("fileId");
        if (Constant.FilePath.equals("")) {
            start_single(Constant.PDFPath, url, fileId + ".pdf");
        } else {
            start_single(Constant.FilePath, url, fileId + ".pdf");
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
    }

    @Override
    public void loadComplete(int nbPages) {
        Log.e("WQ", "Cannooadpage ");
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
//        Log.e(TAG, "title = " + meta.getTitle());
//        Log.e(TAG, "author = " + meta.getAuthor());
//        Log.e(TAG, "subject = " + meta.getSubject());
//        Log.e(TAG, "keywords = " + meta.getKeywords());
//        Log.e(TAG, "creator = " + meta.getCreator());
//        Log.e(TAG, "producer = " + meta.getProducer());
//        Log.e(TAG, "creationDate = " + meta.getCreationDate());
//        Log.e(TAG, "modDate = " + meta.getModDate());
        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

//            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Log.e("WQ", "Cannot load page " + page);
    }

    public void start_single(String dirPath, String url, String fileName) {
        if (dirPath.equals("")) {
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            return;
        }
        mSinglePath = dirPath
                + File.separator + fileName;
        //.setTag()
        BaseDownloadTask singleTask = FileDownloader.getImpl().create(url)
                .setPath(mSinglePath, false)
                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400)
                //.setTag()
                .setListener(new FileDownloadSampleListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//                        Log.e("feifei", "pending:-----"  + ",soFarBytes:" + soFarBytes + ",totalBytes:" + totalBytes );

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.e("feifei", "progress" + task.getId() + ",soFarBytes:" + soFarBytes + ",totalBytes:"+totalBytes);
                        percent = soFarBytes / totalBytes;
                        handler.sendEmptyMessage(1);
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                        Log.e("feifei", "blockComplete taskId:" + task.getId() + ",filePath:" + task.getPath() + ",fileName:" + task.getFilename() + ",speed:" + task.getSpeed() + ",isReuse:" + task.reuse());
//                        Intent intent = new Intent(MtNotifyActivity.this, PdfActivity.class);
//                        intent.putExtra("url", mSinglePath);
//                        startActivity(intent);
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
}