package com.itsv.FSZHZX.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.base.MyBaseMvpActivity;
import com.itsv.FSZHZX.model.GroupBaseModel;
import com.itsv.FSZHZX.model.GroupListModel;
import com.itsv.FSZHZX.model.NameModel;
import com.itsv.FSZHZX.presenter.AddressBookPre;
import com.itsv.FSZHZX.ui.adapter.ContactAdapter;
import com.itsv.FSZHZX.ui.adapter.GroupListAdapter;
import com.itsv.FSZHZX.utils.SideBar;
import com.itsv.FSZHZX.view.AddressBookView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;

public class AddressBookActivity extends MyBaseMvpActivity<AddressBookActivity, AddressBookPre> implements AddressBookView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.spinner_type)
    Spinner spinnerType;
    @BindView(R.id.spinner_number)
    Spinner spinnerNumber;
    @BindView(R.id.edit)
    EditText edit;
    @BindView(R.id.dialog)
    TextView dialog;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tv_nodata)
    TextView tvNoData;
    @BindView(R.id.groupList)
    ExpandableListView expandableListView;
    @BindView(R.id.sideBar)
    SideBar sideBar;
    @BindView(R.id.tv_capital)
    TextView tvCapital;
    @BindView(R.id.nameSwipe)
    SwipeRefreshLayout refreshLayout;
    @BindString(R.string.address_book)
    String addressBook;
    @BindColor(R.color.colorPrimary)
    int colorPrimary;

    private String[] types = {"姓名", "委员会", "党派", "界别"};
    private String[] typeCodes = {"", "COMMITTEE", "PARTY", "CIRCLES"};
    private AddressBookPre presenter;
    private List<GroupBaseModel.DataBean> jieciList;
    private List<GroupListModel.DataBean> list;
    private List<NameModel.DataBean> nameList;
    private int numPosition;
    private int typePosition;
    private ContactAdapter adapter;
    private GroupListAdapter groupListAdapter;

    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        presenter = new AddressBookPre();
        return presenter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_address_book;
    }

    @Override
    protected void initViewsAndEnvents() {
        showNameList(true);
        //获取界次
        presenter.selectGroupBase();
        tvTitle.setText(addressBook);
        list = new ArrayList<>();
        groupListAdapter = new GroupListAdapter(list,this);
        expandableListView.setAdapter(groupListAdapter);
        initEdit();
        initSwipeRefresh();
        initRecycler();
    }

    private void initSwipeRefresh() {
        refreshLayout.setColorSchemeColors(colorPrimary);
        refreshLayout.setOnRefreshListener(this);
    }

    private void initEdit() {
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String editContent = getEditContent();
                presenter.querySimpleNameList(String.valueOf(jieciList.get(numPosition).getId()), editContent, "");
            }
        });
    }

    private String getEditContent() {
        return edit.getText().toString().trim();
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        sideBar.setTextView(dialog);
        sideBar.setOnTouchingLetterChangedListener(s -> {
            int position = adapter.getPositionForSection(s.charAt(0));
            if (position != -1) {
                layoutManager.scrollToPositionWithOffset(position, 0);
            }
        });
    }

    @Override
    public void initSpinner(List<GroupBaseModel.DataBean> list, String[] array) {
        jieciList = list;
        //请求完界次之后获取姓名列表
        presenter.querySimpleNameList(String.valueOf(list.get(numPosition).getId()), typeCodes[typePosition], "");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, array);
        spinnerNumber.setAdapter(adapter);
        spinnerNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (numPosition == i) return;
                numPosition = i;
                queryListBySpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        spinnerType.setAdapter(nameAdapter);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == typePosition) return;
                typePosition = i;
                queryListBySpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public void setNameListData(List<NameModel.DataBean> list) {
        nameList = list;
        showNameList(true);
        if (null == adapter) {
            adapter = new ContactAdapter(this, list);
            adapter.setOnItemClickListener((view, position) -> {
                NameModel.DataBean dataBean = nameList.get(position);
                Intent intent = new Intent(this, AddressDetailsActivity.class);
                intent.putExtra("bean", dataBean);
                intent.putExtra("isName", true);
                startActivity(intent);
            });
            recycler.setAdapter(adapter);
        } else {
            adapter.refreshData(list);
        }
    }


    @Override
    public void setGroupList(List<GroupListModel.DataBean> list) {
        this.list = list;
        showNameList(false);
        groupListAdapter.refreshData(list);
        expandableListView.setOnChildClickListener((expandableListView, view, i, i1, l) -> {
            GroupListModel.DataBean.UserListBean userListBean = list.get(i).getUserList().get(i1);
            Intent intent = new Intent(AddressBookActivity.this, AddressDetailsActivity.class);
            intent.putExtra("bean", userListBean);
            intent.putExtra("isName", false);
            startActivity(intent);
            return false;
        });
    }

    @Override
    public void showNameList(boolean show) {
        if (show) {
            refreshLayout.setVisibility(View.VISIBLE);
            expandableListView.setVisibility(View.GONE);
            sideBar.setVisibility(View.VISIBLE);
        } else {
            refreshLayout.setVisibility(View.GONE);
            expandableListView.setVisibility(View.VISIBLE);
            sideBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void queryListBySpinner() {
        if (typePosition == 0) {
            presenter.querySimpleNameList(String.valueOf(jieciList.get(numPosition).getId()), "", "");
        } else {
            presenter.queryGroupedList(String.valueOf(jieciList.get(numPosition).getId()), typeCodes[typePosition]);
        }
    }

    @Override
    public void startRefreshView() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void stopRefreshView() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        if (typePosition == 0) {
            String editContent = getEditContent();
            presenter.querySimpleNameList(String.valueOf(jieciList.get(numPosition).getId()), editContent, "");
        } else {
            presenter.queryGroupedList(String.valueOf(jieciList.get(numPosition).getId()), typeCodes[typePosition]);
        }
    }

    @Override
    public void showNoDataView() {
        tvNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNodataView() {
        tvNoData.setVisibility(View.GONE);
    }
}
