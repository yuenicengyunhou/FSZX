package com.itsv.FSZHZX.view;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.itsv.FSZHZX.model.MyAttachModel;
import com.itsv.FSZHZX.model.MyMeetingModel;
import com.itsv.FSZHZX.model.NotiModel;

import java.util.List;

public interface MtNofityView extends MvpView {

    void loading();

    void loadingComplete();

    void loadingEnd();

    void starRefresh();

    void stopRefresh();

    void loadMeetingList(List<MyMeetingModel.DataBean> list);

    void loadNotiList(List<NotiModel.DataBean> list);
                                                           //会议内容的dialog用reason的dialog,这里加一个判断
    void showReasonDialog(long id,int position,String reason,boolean isReason);

    void notifyList(int position, String isCanhui);

    //判断一个附件直接点击打开，多个弹窗选择
    void checkAttachCount(List<MyAttachModel> list,String[] strings);
    //附件选择框
    void showAttachDialog(List<MyAttachModel> fileListBeans,String[] strings);

    void showLoading();

    void hideLoading();

    void setDateButton();

    void showMeetingContentDialog(String content);

    void showNoDataView();

    void hideNoDataView();

    void tempMeeting(String roomNum);

//    void showNotiAttachDialog(List<NotiModel.DataBean.MeetingBaseResultBean.FileListBean> list);

}
