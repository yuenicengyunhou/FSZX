package com.itsv.FSZHZX.presenter;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.itsv.FSZHZX.api.UserApi;
import com.itsv.FSZHZX.base.ApiHelper;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.model.MeetingDetaisModel;
import com.itsv.FSZHZX.model.NotiModel;
import com.itsv.FSZHZX.ui.activity.MeetingDetailsActivity;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MtDetailsPre implements MvpPresenter<MeetingDetailsActivity> {

    private MeetingDetailsActivity mvpView;
    @Override
    public void attachView(MeetingDetailsActivity view) {
        this.mvpView = view;

    }

    @Override
    public void detachView(boolean retainInstance) {

    }
    public void adviceList(long id) {
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.meetingURL)
                .createService(UserApi.class);
        Call<ResponseBody> call = api.detail(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (null==mvpView.tvTitle) return;
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        Gson gson = new Gson();
                        MeetingDetaisModel model = gson.fromJson(params, MeetingDetaisModel.class);
                        MeetingDetaisModel.DataBean bean = model.getData();
                        String meetingTitle     = bean.getMeetingTitle();
                        String meetingStartTime = bean.getMeetingStartTime();
                        String meetingContent    = bean.getMeetingContent();
                        List<MeetingDetaisModel.DataBean.MeetingUserListBean> meetingUserList = bean.getMeetingUserList();
                        mvpView.loadViews(meetingTitle, meetingStartTime, meetingContent,meetingUserList);
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
}
