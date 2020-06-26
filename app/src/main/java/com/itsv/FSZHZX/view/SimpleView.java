package com.itsv.FSZHZX.view;

import com.hannesdorfmann.mosby.mvp.MvpView;

public interface SimpleView extends MvpView {

    void loadViews( String name, String positionName, String rate,String avatarUrl);

    void logout();
}
