package com.itsv.FSZHZX.base;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.ui.activity.HomeActivity;
import com.itsv.FSZHZX.ui.activity.LoginActivity;
import com.itsv.FSZHZX.utils.DesignUtils;
import com.itsv.FSZHZX.utils.ToastUtils;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class MyBaseMvpActivity<V extends MvpView, P extends MvpPresenter<V>>
        extends MvpActivity implements MvpView {
    //    protected AlertDialog alertDialog;
    protected Unbinder bind;
    private long firstTime;
    protected BaseAppManager appManager;
    protected Toolbar toolbar;
    protected String nextPageParams = "";

    /**
     * Screen information
     */
    protected int mScreenWidth = 0;
    protected int mScreenHeight = 0;
    protected float mScreenDensity = 0.0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MyApplication.getWatcher().watch(this);
        appManager = BaseAppManager.getInstance();
        appManager.addActivity(this);
        if (Constant.SCREEN_DENSITY == 0) {
            getDensity();
        }
//        setWhiteStatusBar();
        transparent19and20();
        if (getLayoutID() == 0) {
            setContentView(getLayoutView());
        } else {
            setContentView(getLayoutID());
        }
        bind = ButterKnife.bind(this);
        initViewsAndEnvents();
        if (!(this instanceof LoginActivity || this instanceof HomeActivity)) {
            ImageView ivBack = findViewById(R.id.iv_back);
            ivBack.setOnClickListener(view -> finish());
        }
    }

    protected View getLayoutView() {
        return null;
    }

    private void getDensity() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mScreenDensity = displayMetrics.density;
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;
        Constant.SCREEN_DENSITY = (int) mScreenDensity;
    }

//    protected void setWhiteStatusBar() {
//        int statusBarColor;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            statusBarColor = ContextCompat.getColor(this, R.color.white);
//            View decor = getWindow().getDecorView();
//            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        } else {
//            statusBarColor = ContextCompat.getColor(this, R.color.colorStatusBar);
//        }
//        StatusBarCompat.compat(this, statusBarColor);
//    }

//    protected void setDarkStatusBar() {
//        int statusBarColor;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            statusBarColor = ContextCompat.getColor(this, R.color.black);
//            View decor = getWindow().getDecorView();
//            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        } else {
//            statusBarColor = ContextCompat.getColor(this, R.color.colorStatusBar);
//        }
//        StatusBarCompat.compat(this, statusBarColor);
//    }

    protected void initToolbar(Toolbar toolbar, boolean showBack) {
        this.toolbar = toolbar;
        if (this.toolbar != null) {
            setSupportActionBar(this.toolbar);
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBack);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    protected void transparent19and20() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected abstract int getLayoutID();

    protected abstract void initViewsAndEnvents();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    // 启动应用的设置
    protected static final String PACKAGE_URL_SCHEME = "package:";

    // 显示缺失权限提示
    protected void showMissingPermissionDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
////        builder.setTitle(R.string.label_help);
////        builder.setMessage(R.string.tips_permissions);
//        // 拒绝, 退出应用
//        builder.setNegativeButton(R.string.quit, (dialog, which) -> {
//            setResult(-100);
//            finish();
//        });
//        builder.setPositiveButton(R.string.label_setting, (dialog, which) -> startAppSettings());
//        builder.setCancelable(false);
//        builder.show();
    }

    protected void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this instanceof LoginActivity || this instanceof HomeActivity) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;// 更新firstTime
                    return true;
                } else {
                    appManager.clear();
                }
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setParams(String params) {
        nextPageParams = params;
    }

    protected void toActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    /**
     * 设置recyclerView 和swipe
     */
    protected void initRecycler(RecyclerView recyclerView, SwipeRefreshLayout refreshLayout, int colorPrimary) {
        refreshLayout.setColorSchemeColors(colorPrimary);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void showErrorToast(String s) {
        ToastUtils.showSingleToast(s);
    }

    protected void showPopWindow(View contentView) {
        PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                DesignUtils.getScreenHeight(this) / 2);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        initPopEvents(contentView, popupWindow);
        popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
    }

    protected void initPopEvents(View contentView, PopupWindow popupWindow) {

    }

    ;
}
