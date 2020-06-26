package com.itsv.FSZHZX.view;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.itsv.FSZHZX.model.MeetingDetaisModel;

import java.util.List;

public interface MtDetailsView extends MvpView {

    void loadViews(String meetingTitle, String meetingStartTime, String meetingContent, List<MeetingDetaisModel.DataBean.MeetingUserListBean> listBeans);
}
