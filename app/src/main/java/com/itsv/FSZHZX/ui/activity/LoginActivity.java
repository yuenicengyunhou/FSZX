package com.itsv.FSZHZX.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.base.MyBaseMvpActivity;
import com.itsv.FSZHZX.databinding.ActivityLoginBinding;
import com.itsv.FSZHZX.presenter.LoginPresenter;
import com.itsv.FSZHZX.utils.ToastUtils;
import com.itsv.FSZHZX.view.LoginView;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class LoginActivity extends MyBaseMvpActivity<LoginActivity, LoginPresenter> implements LoginView {

    private LoginPresenter loginPresenter;
    private SharedPreferences fszx;
    public ActivityLoginBinding binding;

    @Override
    protected int getLayoutID() {
        return 0;
    }

    @Override
    protected View getLayoutView() {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void initViewsAndEnvents() {
        fszx = getSharedPreferences("fszx", MODE_PRIVATE);
        boolean autoLog = fszx.getBoolean("autoLog", false);
        binding.checkbox.setChecked(autoLog);
        if (autoLog) {
            Constant.TOKEN = fszx.getString("token", "");
            if (TextUtils.isEmpty(Constant.TOKEN)) {
                SharedPreferences.Editor edit = fszx.edit();
                edit.clear();
                edit.apply();
                binding.checkbox.setChecked(false);
            } else {
                loginTo();
            }
        } else {
            checkPermisson();
        }

        binding.loginBtn.setOnClickListener(v -> clickLogin());
    }

    private void checkPermisson() {
        if (Build.VERSION.SDK_INT >= 23 && getApplicationInfo().targetSdkVersion >= 23) {
            LoginActivityPermissionsDispatcher.allowedWithPermissionCheck(this);
        } else {
            Constant.IMEI = getIMEI();
        }
    }

    public String getIMEI() {
        if (Build.VERSION.SDK_INT >= 29 && getApplicationInfo().targetSdkVersion >= 29) {
            return Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            @SuppressLint("HardwareIds") String deviceId = telephonyManager.getDeviceId();
            //android 10以上已经获取不了imei了 用 android id代替
            if (TextUtils.isEmpty(deviceId)) {
                deviceId = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            return deviceId;
        }

    }

    private String getEditContent(EditText editText) {
        return editText.getText().toString().trim();
    }

    private void clickLogin() {
        String userName = getEditContent(binding.loginEditUser);
        String psd = getEditContent(binding.loginEditPsw);
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(psd)) {
            ToastUtils.showSingleToast("请输入用户名或密码");
        } else {
            loginPresenter.login(userName, psd);
        }
    }

    public void saveToken(String token, String alias) {
        Constant.TOKEN = token;
        SharedPreferences.Editor edit = fszx.edit();
        edit.putBoolean("autoLog", binding.checkbox.isChecked());
        edit.putString("token", token);
        edit.putString("imei", Constant.IMEI);
        edit.putString("alias", alias);
        edit.apply();
    }

    public void saveUserInfo(String json) {
        SharedPreferences.Editor edit = fszx.edit();
        edit.putString("userInfo", json);
        edit.apply();
    }


    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        loginPresenter = new LoginPresenter();
        return loginPresenter;
    }

    @Override
    public void onLoading(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void loginTo() {
        toActivity(HomeActivity.class);
    }

    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    void allowed() {
        Constant.IMEI = getIMEI();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LoginActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied(Manifest.permission.READ_PHONE_STATE)
    void denied() {
    }
}
