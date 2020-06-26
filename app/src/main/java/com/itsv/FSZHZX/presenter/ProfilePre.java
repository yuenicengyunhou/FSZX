package com.itsv.FSZHZX.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.itsv.FSZHZX.api.UserApi;
import com.itsv.FSZHZX.base.ApiHelper;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.model.Info;
import com.itsv.FSZHZX.model.Profile;
import com.itsv.FSZHZX.model.ProfileDetailsM;
import com.itsv.FSZHZX.ui.activity.ProfileActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePre implements MvpPresenter<ProfileActivity> {
    private ProfileActivity mvpView;

    @Override
    public void attachView(ProfileActivity view) {
        mvpView = view;

    }

    @Override
    public void detachView(boolean retainInstance) {

    }

    public void userDetailInfo() {
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.UserURL)
                .createService(UserApi.class);
        Call<ResponseBody> call = api.userDetailInfo(Constant.TOKEN);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (null==mvpView) return;
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        Gson gson = new Gson();
                        ProfileDetailsM model = gson.fromJson(params, ProfileDetailsM.class);
                        if (model.isSuccess()) {
                            ProfileDetailsM.DataBean data = model.getData();
                            String deptName = data.getDeptName();
                            String email = data.getEmail();
                            String gender = data.getGender();
                            String name = data.getName();
                            String partyName = data.getPartyName();
                            String phone = data.getPhone();
                            String positionName = data.getPositionName();
                            String avatarUrl = data.getAvatarUrl();
//                            Object roleNames = data.getRoleNames();
                            String tenantDataSourceName = data.getTenantDataSourceName();
                            List<Profile> infoList = initInfoList(avatarUrl, deptName, email, gender, name, partyName, phone, positionName, tenantDataSourceName);
                            mvpView.loadViews(infoList);
                        } else {
                            mvpView.showErrorToast(model.getMsg());
                        }
//                        if (model != null) {
//                            String msg = model.getMsg();
//                            String data = model.getData();
//                            Constant.TOKEN = data;
//                            if (model.isSuccess()) {
//                                loginView.loginTo();
//                            } else {
//                                ToastUtils.showSingleToast(msg);
//                            }
//                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });


    }

    private List<Profile> initInfoList(String avatarUrl, String deptName, String email, String gender, String name, String partyName, String phone
            , String positionName, String tenantDataSourceName) {
        List<Profile> infoList = new ArrayList<>();
//        String url = avatarUrl == null ? "" : avatarUrl;
//        infoList.add(new Profile("头像", url));
        if (null != name) {
            infoList.add(new Profile("姓名", name));
        }
        if (null != positionName) {
            infoList.add(new Profile("职务", positionName));
        }
        if (null != phone) {
            infoList.add(new Profile("电话", phone));
        }
        if (null != gender) {
            infoList.add(new Profile("性别", gender));
        }
        if (null != partyName) {
            infoList.add(new Profile("党派", partyName));
        }
        if (null != email) {
            infoList.add(new Profile("邮箱", email));
        }
//        if (null != deptName) {
//            infoList.add(new Info("部门", deptName));
//        }
//        if (null != deptName) {
//            infoList.add(new Info("部门", deptName));
//        }


        return infoList;
    }
}
