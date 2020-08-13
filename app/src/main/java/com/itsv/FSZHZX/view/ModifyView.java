package com.itsv.FSZHZX.view;

import com.hannesdorfmann.mosby.mvp.MvpView;

public interface ModifyView extends MvpView {
    void commit();

    void afterModified(String s);

}
