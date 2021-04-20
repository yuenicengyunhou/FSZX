package com.itsv.FSZHZX.presenter;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.itsv.FSZHZX.api.UserApi;
import com.itsv.FSZHZX.base.ApiHelper;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.model.SimpleModel;
import com.itsv.FSZHZX.ui.activity.PasswordActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PsdPresenter implements MvpPresenter<PasswordActivity> {
    private PasswordActivity mvpView;

    @Override
    public void attachView(PasswordActivity view) {
        this.mvpView = view;
    }

    @Override
    public void detachView(boolean retainInstance) {

    }

    public void editPassword(String oldPsd, String newPsd) {
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.UserURL)
                .createService(UserApi.class);
        Call<ResponseBody> call = api.editPassword(Constant.TOKEN, oldPsd, newPsd);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (null==mvpView.tvTitle) return;
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        JSONObject object = new JSONObject(params);
                        String msg = object.getString("msg");
                        boolean success = object.getBoolean("success");
                        String data = object.getString("data");
                        if (success) {
                            mvpView.showErrorToast(data);
                            mvpView.finish();
                        } else {
                            mvpView.showErrorToast(msg);
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
