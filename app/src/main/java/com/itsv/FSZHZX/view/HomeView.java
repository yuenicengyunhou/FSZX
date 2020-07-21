package com.itsv.FSZHZX.view;

import android.content.SharedPreferences;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.itsv.FSZHZX.model.SimpleModel;

import java.io.File;

public interface HomeView extends MvpView {

    void checkToken();

    void checkImei();
    void questPermission();

    void initRecycler();

    void initDisplayImage();

//    void showUpdateDialog(File file);

    void showUpdateDialog(String downloadURL);

    void initAvatar(SimpleModel.DataBean dataBean);

    void setJpushAlias();

    void setDialogViewAfterDownloaded();

    void setUpdateProgress(int progress);
}
