package com.itsv.FSZHZX.view;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.itsv.FSZHZX.model.Profile;

import java.util.List;

public interface ProfileView extends MvpView {

    void queryProfile();

    void loadViews(List<Profile> profiles);
}
