package com.itsv.FSZHZX.presenter;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.itsv.FSZHZX.api.UserApi;
import com.itsv.FSZHZX.base.ApiHelper;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.model.GroupBaseModel;
import com.itsv.FSZHZX.model.GroupListModel;
import com.itsv.FSZHZX.model.NameModel;
import com.itsv.FSZHZX.ui.activity.AddressBookActivity;
import com.itsv.FSZHZX.utils.CharacterParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressBookPre implements MvpPresenter<AddressBookActivity> {
    private AddressBookActivity mvpView;
    private UserApi api;


    @Override
    public void attachView(AddressBookActivity view) {
        mvpView = view;
    }

    @Override
    public void detachView(boolean retainInstance) {
        if (!retainInstance) {
            mvpView = null;
        }
    }

    public void querySimpleNameList(String jieci, String name, String typeValue) {
        mvpView.startRefreshView();
        if (null == api) {
            api = ApiHelper.getInstance().buildRetrofit(Constant.UserURL).createService(UserApi.class);
        }
        Call<ResponseBody> call = api.selectUsers(jieci, name, typeValue, "");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (null==mvpView) return;
                mvpView.stopRefreshView();
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        Gson gson = new Gson();
                        NameModel model = gson.fromJson(params, NameModel.class);
                        if (model != null) {
                            List<NameModel.DataBean> data = model.getData();
                            List<NameModel.DataBean> dataBeans = filledData(data);
                            for (int i = 0; i < dataBeans.size(); i++) {
                                String name1 = dataBeans.get(i).getName();
                                Log.e("WQ", "name===" + name1);
                            }
                            if (dataBeans.isEmpty()) {
                                mvpView.showNoDataView();
                            } else {
                                mvpView.hideNodataView();
                            }
                            mvpView.setNameListData(dataBeans);
                        }
                    } catch (IOException | JsonSyntaxException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                mvpView.stopRefreshView();
            }
        });
    }

    public void queryGroupedList(String jieci, String type) {
        mvpView.startRefreshView();
        if (null == api) {
            api = ApiHelper.getInstance().buildRetrofit(Constant.UserURL).createService(UserApi.class);
        }
        Call<ResponseBody> call = api.selectGroupUsers(jieci, type);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (null==mvpView) return;
                mvpView.stopRefreshView();
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        Gson gson = new Gson();
                        GroupListModel model = gson.fromJson(params, GroupListModel.class);
                        if (model != null) {
                            List<GroupListModel.DataBean> data = model.getData();
                            if (data.isEmpty()) {
                                mvpView.showNoDataView();
                            } else {
                                mvpView.hideNodataView();
                            }
                            mvpView.setGroupList(data);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                mvpView.stopRefreshView();
            }
        });
    }

    /*获取第一个筛选按钮内容/界次*/
    public void selectGroupBase() {
        if (null == api) {
            api = ApiHelper.getInstance().buildRetrofit(Constant.UserURL).createService(UserApi.class);
        }
        Call<ResponseBody> call = api.selectGroupBase();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (null==mvpView) return;
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        Gson gson = new Gson();
                        GroupBaseModel model = gson.fromJson(params, GroupBaseModel.class);
                        if (model != null) {
                            List<GroupBaseModel.DataBean> data = model.getData();
                            String[] array = new String[data.size()];
                            for (int i = 0; i < data.size(); i++) {
                                array[i] = data.get(i).getParameter();
                            }
                            mvpView.initSpinner(data, array);
                        }
                    } catch (IOException | JsonSyntaxException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });

    }


    /*得到拼音首字母*/
    private List<NameModel.DataBean> filledData(List<NameModel.DataBean> list) {
        List<NameModel.DataBean> beanList = new ArrayList<>();
        CharacterParser characterParser = CharacterParser.getInstance();
        for (int i = 0; i < list.size(); i++) {
            NameModel.DataBean sortModel = list.get(i);
            String name = sortModel.getName();
            if (name.contains("隗")) {
                Log.e("WQ", "name===" + name );
            }
            String pinyin = characterParser.getSelling(name);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
            beanList.add(sortModel);
        }
        return beanList;
    }

}
