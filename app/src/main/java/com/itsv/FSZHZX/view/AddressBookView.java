package com.itsv.FSZHZX.view;


import com.hannesdorfmann.mosby.mvp.MvpView;
import com.itsv.FSZHZX.model.GroupBaseModel;
import com.itsv.FSZHZX.model.GroupListModel;
import com.itsv.FSZHZX.model.NameModel;

import java.util.List;

public interface AddressBookView extends MvpView {


    void showNameList(boolean show);

    void initSpinner(List<GroupBaseModel.DataBean> list,String[] array);

    void setNameListData(List<NameModel.DataBean> list);

    void setGroupList(List<GroupListModel.DataBean> list);

    void queryListBySpinner();

    void startRefreshView();

    void stopRefreshView();

    void showNoDataView();

    void hideNodataView();
}
