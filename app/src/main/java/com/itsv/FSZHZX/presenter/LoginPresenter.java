package com.itsv.FSZHZX.presenter;


import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.itsv.FSZHZX.api.LoginApi;
import com.itsv.FSZHZX.api.UserApi;
import com.itsv.FSZHZX.base.ApiHelper;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.model.LoginModel;
import com.itsv.FSZHZX.model.ProfileDetailsM;
import com.itsv.FSZHZX.utils.ToastUtils;
import com.itsv.FSZHZX.ui.activity.LoginActivity;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter implements MvpPresenter<LoginActivity> {

    private LoginActivity loginView;
    private Call<ResponseBody> call;

    @Override
    public void attachView(LoginActivity view) {
        loginView = view;
    }

    @Override
    public void detachView(boolean retainInstance) {
        if (!retainInstance) {
            loginView = null;
            if ((null != call) && (!call.isCanceled())) {
                call.cancel();
                call = null;
            }
        }
    }

    public void login(String userName, String passWord) {
        if (TextUtils.isEmpty(Constant.IMEI)) {
            String imei = loginView.getIMEI();
            if (TextUtils.isEmpty(imei)) {
                ToastUtils.showSingleToast("获取设备码失败");
                return;
            } else {
                Constant.IMEI = imei;
            }
        }
        loginView.onLoading(true);
        LoginApi api = ApiHelper.getInstance().buildRetrofit(Constant.BASEURL)
                .createService(LoginApi.class);
        call = api.login(userName, passWord, Constant.IMEI);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (null == loginView.binding) return;
                loginView.onLoading(false);
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        Gson gson = new Gson();
                        LoginModel model = gson.fromJson(params, LoginModel.class);
                        if (model != null) {
                            String msg = model.getMsg();
                            String data = model.getData();
                            if (model.isSuccess()) {
                                LoginModel.InfoBean info = model.getInfo();
                                String alias = info.getAlias();
                                loginView.saveToken(data, alias);
                                getUserInfo(data);
                            } else {
                                ToastUtils.showSingleToast(msg);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                loginView.showErrorToast("网络错误");
                if (null == loginView.binding) return;
                loginView.onLoading(false);
            }
        });


    }

    public void getUserInfo(String token) {
        loginView.onLoading(true);
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.BASEURL)
                .createService(UserApi.class);
        Call<ResponseBody> call = api.userDetailInfo(token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (null == loginView.binding) return;
                loginView.onLoading(false);
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        Gson gson = new Gson();
                        ProfileDetailsM model = gson.fromJson(params, ProfileDetailsM.class);
                        if (model.isSuccess()) {
                            loginView.saveUserInfo(params);
                            loginView.loginTo();
                        } else {
                            ToastUtils.showSingleToast(model.getMsg());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                loginView.onLoading(false);
                if (null == loginView.binding) return;
                ToastUtils.showSingleToast("网络连接失败");
            }
        });


    }


}
