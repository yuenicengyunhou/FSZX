package com.itsv.FSZHZX.presenter;


import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.itsv.FSZHZX.api.UserApi;
import com.itsv.FSZHZX.base.ApiHelper;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.model.MyMeetingModel;
import com.itsv.FSZHZX.model.NotiModel;
import com.itsv.FSZHZX.model.ProfileDetailsM;
import com.itsv.FSZHZX.ui.activity.MtNotifyActivity;
import com.itsv.FSZHZX.utils.ToastUtils;
import com.manis.core.interfaces.ManisApiInterface;
import com.manis.retrofit.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MtNotifyPre implements MvpPresenter<MtNotifyActivity> {
    private MtNotifyActivity mvpView;

    @Override
    public void attachView(MtNotifyActivity view) {
        mvpView = view;
    }

    @Override
    public void detachView(boolean retainInstance) {

    }

    // 会议通知
    public void adviceList(String token, int currentPage, int pageSize) {
        if (currentPage == 1) {
            mvpView.starRefresh();
        } else {
            mvpView.loading();
        }
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.notifiURL)
                .createService(UserApi.class);
        Call<ResponseBody> call = api.adviceList(token, currentPage, pageSize);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (null == mvpView) return;
                if (currentPage == 1) {
                    mvpView.stopRefresh();
                } else {
                    mvpView.loadingComplete();
                }
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        Gson gson = new Gson();
                        NotiModel model = gson.fromJson(params, NotiModel.class);
                        List<NotiModel.DataBean> data = model.getData();
                        if (null == data) return;
                        if (data.isEmpty() && !(currentPage == 1)) {
                            mvpView.loadingEnd();
                        }
                        mvpView.loadNotiList(data);
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

    //视频会议 个人会议
    public void userIndexList(String token, int currentPage, int pageSize
            , String meetingTitle, String meetingOwnerUserName, String meetingTime) {
        if (currentPage == 1) {
            mvpView.starRefresh();
        } else {
            mvpView.loading();
        }
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.meetingURL)
                .createService(UserApi.class);
        Call<ResponseBody> call = api.userIndexList(token, currentPage, pageSize, meetingTitle, meetingOwnerUserName, meetingTime);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (null == mvpView) return;
                if (currentPage == 1) {
                    mvpView.stopRefresh();
                } else {
                    mvpView.loadingComplete();
                }
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        Gson gson = new Gson();
                        MyMeetingModel model = gson.fromJson(params, MyMeetingModel.class);
                        List<MyMeetingModel.DataBean> data = model.getData();
                        if (null == data) return;
                        if (data.isEmpty() && !(currentPage == 1)) {
                            mvpView.loadingEnd();
                        }
                        mvpView.loadMeetingList(data);
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

    //提交是否参会

    public void editItem(long id, String isCanhui, String content, int position) {
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.notifiURL)
                .createService(UserApi.class);
        Call<ResponseBody> call = api.editItem(id, isCanhui, content);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (null == mvpView) return;
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        JSONObject object = new JSONObject(params);
                        boolean success = object.getBoolean("success");
                        String msg = object.getString("msg");
                        String data = object.getString("data");
                        if (success) {
                            mvpView.showErrorToast(data);
                            mvpView.notifyList(position, isCanhui);
                        } else {
                            mvpView.showErrorToast(msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }

//    public void personalData() {
//        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.UserURL)
//                .createService(UserApi.class);
//        Call<ResponseBody> call = api.personalData(Constant.TOKEN);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    try {
//                        String params = response.body().string();
//                        Gson gson = new Gson();
//
//                        SimpleModel model = gson.fromJson(params, SimpleModel.class);
//                        if (model.isSuccess()) {
//                            SimpleModel.DataBean data = model.getData();
//                            String name = data.getName();
//                            mvpView.setUserName(name);
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

    //创建临时会议
    public void createTempConference() {
        mvpView.showLoading();
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.meetingURL)
                .createService(UserApi.class);
        Call<ResponseBody> call = api.createTempConference(Constant.TOKEN);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        JSONObject object = new JSONObject(params);
                        boolean success = object.getBoolean("success");
                        if (success) {
                            String data = object.getString("data");
                            mvpView.tempMeeting(data);
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

    public void validEnableInto(String roomNum, String userName, String sdkCid, boolean isModerator, String psw, String mId) {
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.meetingURL)
                .createService(UserApi.class);
        Call<ResponseBody> call = api.validEnableInto(Constant.TOKEN, roomNum);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        JSONObject object = new JSONObject(params);
                        boolean success = object.getBoolean("success");
                        if (success) {
//                            String data = object.getString("data");
                           questStage(roomNum,userName,sdkCid,isModerator,psw,mId);
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

    public void closeMeeting(String roomNum) {
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.meetingURL)
                .createService(UserApi.class);
        Call<ResponseBody> call = api.closeMeeting(Constant.TOKEN, roomNum);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
//                        JSONObject object = new JSONObject(params);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

    public void userDetailInfo() {
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.UserURL)
                .createService(UserApi.class);
        Call<ResponseBody> call = api.userDetailInfo(Constant.TOKEN);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (null == mvpView) return;
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        Gson gson = new Gson();
                        ProfileDetailsM model = gson.fromJson(params, ProfileDetailsM.class);
                        if (model.isSuccess()) {
                            ProfileDetailsM.DataBean data = model.getData();
                            long id = data.getId();
                            mvpView.setmUserId(id);
                        }
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

    private void questStage(String roomNum, String userName, String sdkCid, boolean isModerator, String psw, String mId) {
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.meetingURL).createService(UserApi.class);
        Call<ResponseBody> call = api.getStageByRoomNumber(roomNum);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        JSONObject object = new JSONObject(params);
                        boolean success = object.getBoolean("success");
                        String data = object.getString("data");
                        if (success) {
                            mvpView.setStage(data.equals(mId));
                            mvpView.hideLoading();
                            mvpView.joinMeetingRoom(roomNum, userName, sdkCid, isModerator, psw, mId);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                ToastUtils.showSingleToast("获取主屏失败");
            }
        });
    }

}

