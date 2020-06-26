package com.itsv.FSZHZX.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.just.agentweb.AgentWeb;

public abstract class BaseFullScreenActivity extends AppCompatActivity {

    protected AgentWeb agentWeb;
    private long firstTime;
    protected BaseAppManager appManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appManager = BaseAppManager.getInstance();
        appManager.addActivity(this);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(getLayoutId());
        if (initWeb()) {
            initAgent();
        }
        initViewsAndEnvents();
    }

    protected abstract void initViewsAndEnvents();

    protected abstract int getLayoutId();

    protected abstract boolean initWeb();

    private void initAgent() {
//        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
//        agentWeb = AgentWeb.with(this)
//                .setAgentWebParent(relativeLayout, new RelativeLayout.LayoutParams(-1, -1))
//                .closeIndicator()
//                .setMainFrameErrorView(R.layout.error_page, -1)
//                .interceptUnkownUrl()
//                .createAgentWeb()
//                .ready()
//                .go(Constant.WEBURL+"login/");
//        IAgentWebSettings agentWebSettings = agentWeb.getAgentWebSettings();
//        WebSettings webSettings = agentWebSettings.getWebSettings();
//        String userAgentString = webSettings.getUserAgentString();
//        webSettings.setUserAgentString(userAgentString+" ZTGH/Cinderella");
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        WebView webView = agentWeb.getWebCreator().getWebView();
//        webView.setOnLongClickListener(v -> true);
//        //注入对象
//        agentWeb.getJsInterfaceHolder().addJavaObject("android", new JsCallAndroid(this, agentWeb));
    }


    @Override
    protected void onPause() {
        if (null != agentWeb) {
            agentWeb.getWebLifeCycle().onPause();
        }
        super.onPause();

    }

    @Override
    protected void onResume() {
        if (agentWeb != null) {
            agentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (this instanceof LoginActivity) {
//            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
//                    && event.getAction() == KeyEvent.ACTION_DOWN) {
//                long secondTime = System.currentTimeMillis();
//                if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
//                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//                    firstTime = secondTime;// 更新firstTime
//                    return true;
//                } else {
//                    appManager.clear();
//                }
//                return false;
//            }
//        }
        return super.onKeyDown(keyCode, event);
    }
}
