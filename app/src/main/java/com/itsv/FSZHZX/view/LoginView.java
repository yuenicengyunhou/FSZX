package com.itsv.FSZHZX.view;

import com.hannesdorfmann.mosby.mvp.MvpView;

public interface LoginView extends MvpView {

    void loginTo();

    void onLoading(boolean show);

}
