package com.itsv.FSZHZX.base;

import static com.itsv.FSZHZX.base.Constant.listCPPCC;
import static com.itsv.FSZHZX.base.Constant.listFileEXchange;
import static com.itsv.FSZHZX.base.Constant.listNotice;
import static com.itsv.FSZHZX.base.Constant.param_proposal;
import static com.itsv.FSZHZX.base.Constant.param_situation;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.ui.js.JsCallAndroid;
import com.itsv.FSZHZX.utils.ToastUtils;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.WebCreator;

import butterknife.BindView;

public abstract class BaseWebActivity extends BaseAppCompatActivity {

    @BindView(R.id.tv_title)
    TextView barTitle;
    /*@BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.toolbar_share)
    Toolbar toolbar;*/
    protected AgentWeb agentWeb;
    protected SharedPreferences preferences;
    protected boolean barCommmitVisible = false;
    private RelativeLayout relativeLayout;
    private WebView webView;
    protected String webTitle;
    private int webFirstResume=1;

    @Override
    protected void initViewsAndEnvents() {
        Gson gson = new Gson();
        String link = getWebLink(gson);
        beforeWebInit();
        if (initWeb() && (link != null) && (!TextUtils.isEmpty(link))) {
            initAgent(link);
        } else {
            ToastUtils.showSingleToast("网页链接错误");
        }
//        initToolbar(toolbar,false);
        afterWebInit();
    }

    protected SharedPreferences getPreferences() {
        return getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
    }
    protected abstract void beforeWebInit();

    protected abstract void afterWebInit();

    protected void initToolbar(Toolbar toolbar,boolean homeButtonEnabled) {
        if (null == toolbar) return;
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar == null) return;
        supportActionBar.setHomeButtonEnabled(homeButtonEnabled);
        supportActionBar.setDisplayShowTitleEnabled(false);
    }

    protected abstract boolean initWeb();

    protected abstract String getWebLink(Gson gson);


    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initAgent(String link) {
        relativeLayout =  findViewById(R.id.relativeLayout);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(relativeLayout, new RelativeLayout.LayoutParams(-1, -1))
                .closeIndicator()
                .setMainFrameErrorView(R.layout.error_page, -1)
                .interceptUnkownUrl()
                .setWebViewClient(mWebViewClient)
                .setWebChromeClient(webChromeClient)
                .createAgentWeb()
                .ready()
                .go(link);
        WebCreator webCreator = agentWeb.getWebCreator();
        webView = webCreator.getWebView();
        webView.setVerticalScrollBarEnabled(false);
        webView.setOnLongClickListener(v -> true);
        IAgentWebSettings agentWebSettings = agentWeb.getAgentWebSettings();
        WebSettings webSettings = agentWebSettings.getWebSettings();
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //自适应屏幕
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);//支持同时加载https和http混合模式（有的手机图片显示不了）
        }
        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
        });
        //注入对象
        JsCallAndroid jsCallAndroid = new JsCallAndroid(this, agentWeb);
        agentWeb.getJsInterfaceHolder().addJavaObject("android", jsCallAndroid);
        jsCallAndroid.setActivity(this);
    }

    private WebChromeClient webChromeClient = new WebChromeClient() {
       /* private View mCustomView;
        private CustomViewCallback mCustomViewCallback;*/

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if ((view != null) && (barTitle != null) && (!title.contains("http"))) {
                if (!title.equals("房山智慧政协学习平台")) {
                    barTitle.setText(title);
                    webTitle = title;
                }
            }
        }
    };


    /*设置加载首页的loading效果*/

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view,
                                       SslErrorHandler handler, SslError error) {
            handler.proceed(); // 接受所有证书
        }

        //这个方法会走两次，10%走一次，100%走一次
        public void onPageFinished(WebView view, String url) {
            //因为页面加载完成之前，点击右上角按钮不能与web交互而不能跳转，所以等加载完成之后再显示按钮
            if (url.contains(listNotice) || url.contains(listCPPCC) || url.contains(listFileEXchange)
                    || url.contains(param_situation) || url.contains(param_proposal)) {
                webFirstResume--;
                if (webFirstResume < 0) {
                    agentWeb.getUrlLoader().reload();
                    webFirstResume = 1;
                }
            }
            /*if (barCommmitVisible && (view != null) && (view.getProgress() == 100) && (ivShare != null)) {
                ivShare.setVisibility(View.VISIBLE);
            }*/
        }

    };

    @Override
    protected void onRestart() {
        super.onRestart();
        if ((nextPageParams != null) && (!TextUtils.isEmpty(nextPageParams)) && nextPageParams.contains("reset") && (agentWeb != null)) {
            agentWeb.getJsAccessEntrace().quickCallJs("reset");
        }
    }

    @Override
    protected void onResume() {
        if (agentWeb != null) {
            agentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (null != agentWeb) {
            agentWeb.getWebLifeCycle().onPause();
        }
        super.onPause();
    }
}
