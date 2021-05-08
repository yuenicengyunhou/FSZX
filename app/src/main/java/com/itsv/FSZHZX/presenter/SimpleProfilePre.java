package com.itsv.FSZHZX.presenter;


import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.itsv.FSZHZX.api.LoginApi;
import com.itsv.FSZHZX.base.ApiHelper;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.ui.activity.SimpleProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimpleProfilePre implements MvpPresenter<SimpleProfileActivity> {
    private SimpleProfileActivity mvpView;

    @Override
    public void attachView(SimpleProfileActivity view) {
        this.mvpView = view;
    }

    @Override
    public void detachView(boolean retainInstance) {

    }

//    public void personalData() {
//        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.UserURL)
//                .createService(UserApi.class);
//        Call<ResponseBody> call = api.personalData(Constant.TOKEN);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                if (null == mvpView) return;
//                if (response.isSuccessful()) {
//                    try {
//                        String params = response.body().string();
//                        Gson gson = new Gson();
//
//                        SimpleModel model = gson.fromJson(params, SimpleModel.class);
//
//
//                        if (model.isSuccess()) {
//                            SimpleModel.DataBean data = model.getData();
//                            String avatarUrl = data.getAvatarUrl();
//                            Log.e("WQ", avatarUrl);
//                            String name = data.getName();
//                            String positionName = data.getPositionName();
//                            String weekCorrectRate = data.getWeekCorrectRate();
//                            mvpView.loadViews( name, positionName, weekCorrectRate,avatarUrl);
//                        } else {
//                            mvpView.showErrorToast(model.getMsg());
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//
//            }
//        });
//    }

    public void logout() {
        LoginApi api = ApiHelper.getInstance().buildRetrofit(Constant.BASEURL)
                .createService(LoginApi.class);
        Call<ResponseBody> logout = api.logout(Constant.TOKEN);
        logout.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (null==mvpView.tvTitle)return;
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        JSONObject object = new JSONObject(params);
                        boolean success = object.getBoolean("success");
                        String data = object.getString("data");
                        if (success) {
                            mvpView.logout();
                        } else {
                            mvpView.showErrorToast(data);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

}
