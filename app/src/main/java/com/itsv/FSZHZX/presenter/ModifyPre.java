package com.itsv.FSZHZX.presenter;


import androidx.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.itsv.FSZHZX.api.UserApi;
import com.itsv.FSZHZX.base.ApiHelper;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.ui.activity.ModifyActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyPre implements MvpPresenter<ModifyActivity> {
    private ModifyActivity mvpView;

    @Override
    public void attachView(ModifyActivity view) {
        mvpView = view;
    }

    @Override
    public void detachView(boolean retainInstance) {

    }

    public void modifyName(String name) {
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.UserURL)
                .createService(UserApi.class);
        Call<ResponseBody> call = api.modifyName(Constant.TOKEN, name);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (null==mvpView) return;
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        JSONObject object = new JSONObject(params);
                        boolean success = object.getBoolean("success");
                        String msg = object.getString("msg");
                        mvpView.showErrorToast(msg);
                        if (success) {
                            mvpView.afterModified(name);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }

    public void modifyGerder(String name) {
        String string = name.equals("男") ? "M" : "F";
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.UserURL)
                .createService(UserApi.class);
        Call<ResponseBody> call = api.modifyGerder(Constant.TOKEN, string);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (null==mvpView) return;
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        JSONObject object = new JSONObject(params);
                        boolean success = object.getBoolean("success");
                        String msg = object.getString("msg");
                        mvpView.showErrorToast(msg);
                        if (success) {
                            mvpView.finish();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }

    public void modifyBirthday(String name) {
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.UserURL)
                .createService(UserApi.class);
        Call<ResponseBody> call = api.modifyBirthday(Constant.TOKEN, name);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (null==mvpView) return;
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        JSONObject object = new JSONObject(params);
                        boolean success = object.getBoolean("success");
                        String msg = object.getString("msg");
                        mvpView.showErrorToast(msg);
                        if (success) {
                            mvpView.finish();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }
}
