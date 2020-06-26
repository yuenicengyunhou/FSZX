package com.itsv.FSZHZX.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.ui.activity.MainActivity;
import com.itsv.FSZHZX.utils.StatusBarCompat;

import java.io.File;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseAppCompatActivity extends AppCompatActivity {

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
        appManager = BaseAppManager.getInstance();
        appManager.addActivity(this);
//        setWhiteStatusBar();
        transparent19and20();
        setContentView(getLayoutID());
        bind = ButterKnife.bind(this);
        initViewsAndEnvents();
        getDensity();
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

    protected void setDarkStatusBar() {
        int statusBarColor;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusBarColor = ContextCompat.getColor(this, R.color.colorRoomToolbar);
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            statusBarColor = ContextCompat.getColor(this, R.color.colorStatusBar);
        }
        StatusBarCompat.compat(this, statusBarColor);
    }

    protected void initToolbar(Toolbar toolbar,boolean showBackIcon) {
        this.toolbar = toolbar;
        if (this.toolbar != null) {
            setSupportActionBar(this.toolbar);
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBackIcon);
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
        if (this instanceof MainActivity) {
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

    protected void showUpdateDialog(File file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("版本更新");
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                install(file);
                dialog.dismiss();
            }
        });
        builder.show();
    }


    /**
     * 安装apk
     */
    protected void install(File file) {
        if (!file.exists()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //在服务中开启actiivity必须设置flag
            Uri contentUri = FileProvider.getUriForFile(this, "com.itsv.FSZHZX", file);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            Uri uri = Uri.parse("file://" + file.toString());
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }

}
