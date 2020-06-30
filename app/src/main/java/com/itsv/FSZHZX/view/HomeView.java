package com.itsv.FSZHZX.view;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.itsv.FSZHZX.model.SimpleModel;

import java.io.File;

public interface HomeView extends MvpView {

    void checkToken();
    void questPermission();

    void initRecycler();

    void initDisplayImage();

    void showUpdateDialog(File file);

    void initAvatar(SimpleModel.DataBean dataBean);

    void setJpushAlias();
}
