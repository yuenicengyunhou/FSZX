package com.itsv.FSZHZX.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.base.MyBaseMvpActivity;
import com.itsv.FSZHZX.listener.EndlessRecyclerOnScrollListener;
import com.itsv.FSZHZX.model.MyAttachModel;
import com.itsv.FSZHZX.model.MyMeetingModel;
import com.itsv.FSZHZX.model.NotiModel;
import com.itsv.FSZHZX.presenter.MtNotifyPre;
import com.itsv.FSZHZX.ui.adapter.MeetingAdapter;
import com.itsv.FSZHZX.ui.adapter.NotiAdapter;
import com.itsv.FSZHZX.utils.DesignUtils;
import com.itsv.FSZHZX.utils.ToastUtils;
import com.itsv.FSZHZX.view.MtNofityView;
import com.manis.core.interfaces.ManisApiInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.OnClick;

public class MtNotifyActivity extends MyBaseMvpActivity<MtNotifyActivity, MtNotifyPre> implements MtNofityView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tv_title)
   public TextView tvTitle;
    @BindView(R.id.toolbar_add)
    Toolbar toolbar_add;
    //    @BindView(R.id.tv_commit)
//    TextView tvCommit;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.iv_create)
    ImageView ivCreate;
    @BindView(R.id.noti_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.noti_swipe)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.mt_search)
    LinearLayout searchLayout;
    @BindView(R.id.meetingEdit)
    EditText meetingEdit;
    @BindView(R.id.button_date)
    TextView buttonDate;
    @BindView(R.id.tv_nodata)
    TextView tvNoData;
    @BindDrawable(R.mipmap.ic_add)
    Drawable icAdd;
    @BindColor(R.color.colorPrimary)
    int colorPrimary;
    @BindView(R.id.loadingProgress)
    CardView loadingProgress;
    //    @BindString(R.string.meetingNotifi)
//    String meetingNotifi;
//    @BindDrawable(R.drawable.rec_grey)
//    Drawable bgEdit;
    private MtNotifyPre presenter;
    private List<MyMeetingModel.DataBean> beans;
    private List<NotiModel.DataBean> notiBeans;
    private boolean isMtNotify;//true 为会议通知页面， false为视频会议页面
    private MeetingAdapter meetingAdapter;
    private int currentPage = 1;
    private NotiAdapter notiAdapter;
    private int pdfPickPosition;
    private String meetingTitle = "";
    private String meetingTime = "";
    private String userName;

    private SimpleDateFormat format;
    private String mRoomNum;
    private long mUserId;

    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        presenter = new MtNotifyPre();
        return presenter;
    }

    public void setmUserId(long userid){
        mUserId = userid;
        queryList(currentPage);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_mt_notify;
    }

    @Override
    protected void initViewsAndEnvents() {
        initToolbar(toolbar_add, false);
//        tvCommit.setText("加入会议");
        EventBus.getDefault().register(this);
        initLoading();
        initMeetingTime();
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        userName = intent.getStringExtra("userName");
        initRecycler(recyclerView, refreshLayout, colorPrimary);
        refreshLayout.setOnRefreshListener(this);
        if (title != null) {
            isMtNotify = title.equals("会议通知");
            tvTitle.setText(title);
            if (isMtNotify) {
                searchLayout.setVisibility(View.GONE);
                ivAdd.setVisibility(View.GONE);
                ivCreate.setVisibility(View.GONE);
                notiBeans = new ArrayList<>();
                notiAdapter = new NotiAdapter(notiBeans, this);
                recyclerView.setAdapter(notiAdapter);
                initNotiOnItemClick(notiAdapter);
            } else {
                ManisApiInterface.init(getApplication(), "https://bj189.scmeeting.com/");
                initEdit();
                beans = new ArrayList<>();
                meetingAdapter = new MeetingAdapter(beans, this);
                recyclerView.setAdapter(meetingAdapter);
                meetingAdapter.setItemClickListener(new MeetingAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(int position, String sdkCid, String sdkConferenceUrl, String sdkRoomNum,long id,long moderatorId,String moderatorPsw) {
                        presenter.validEnableInto(sdkRoomNum,userName,sdkCid,mUserId==moderatorId,moderatorPsw,String.valueOf(mUserId));
                    }

                    @Override
                    public void onViewPDF(List<MyAttachModel> list, String[] strings) {
                        checkAttachCount(list, strings);
                    }

                    @Override
                    public void toDetails(int position, String content) {
                        showMeetingContentDialog(content);
                    }
                });
            }
        }

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                currentPage = currentPage + 1;
                if (!isMtNotify) {
                    queryList(currentPage);
                }

            }
        });
        presenter.userDetailInfo();
    }

    private void initLoading() {
        int screenWidth = DesignUtils.getScreenWidth(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenWidth / 3, screenWidth / 3);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        loadingProgress.setLayoutParams(layoutParams);
    }

    @SuppressLint("SimpleDateFormat")
    private void initMeetingTime() {
        Date date = new Date();
        format = new SimpleDateFormat("yyyy-MM-dd");
        meetingTime = format.format(date);
        setDateButton();
    }

    @Override
    public void setDateButton() {
        buttonDate.setText(meetingTime);
    }

    private void initEdit() {
        meetingEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                meetingTitle = meetingEdit.getText().toString().trim();
                selectByTypes(meetingTitle);
            }
        });
    }

    private void selectByTypes(String types) {
        currentPage = 1;
        List<MyMeetingModel.DataBean> tempList = new ArrayList<>();
        for (int i = 0; i < beans.size(); i++) {
            MyMeetingModel.DataBean dataBean = beans.get(i);
            String meetingTypeName = dataBean.getMeetingTypeName();
            if (meetingTypeName.contains(types)) {
                tempList.add(dataBean);
            }
        }
        meetingAdapter.refreshData(tempList);
//        presenter.userIndexList(Constant.TOKEN, currentPage, 30, meetingTitle, "", meetingTime);
    }

    private void initNotiOnItemClick(NotiAdapter notiAdapter) {
        notiAdapter.setItemClickListener(new NotiAdapter.NotiItemClickListener() {
            @Override
            public void onAttend(long id, int position) {
                presenter.editItem(id, "1", "", position);
            }

            @Override
            public void onRefuse(long id, int position, String reason) {
                showReasonDialog(id, position, reason, true);
            }

            @Override
            public void onViewPDF(List<MyAttachModel> list, String[] strings) {
                checkAttachCount(list, strings);
            }

            @Override
            public void toDetails(int position, String content) {
                showMeetingContentDialog(content);
            }
        });
    }

    private void queryList(int currentPage) {
        if (isMtNotify) {
            presenter.adviceList(Constant.TOKEN, currentPage, 30);
        } else {
            presenter.userIndexList(Constant.TOKEN, currentPage, 30, meetingTitle, "", meetingTime);
        }
    }


    @Override
    public void loading() {
        if (isMtNotify) {
            notiAdapter.setLoadState(notiAdapter.LOADING);
        } else {
            meetingAdapter.setLoadState(meetingAdapter.LOADING);
        }
    }

    @Override
    public void loadingComplete() {
        if (isMtNotify) {
            notiAdapter.setLoadState(notiAdapter.LOADING_COMPLETE);
        } else {
            meetingAdapter.setLoadState(meetingAdapter.LOADING_COMPLETE);
        }
    }

    @Override
    public void loadingEnd() {
        if (isMtNotify) {
            notiAdapter.setLoadState(notiAdapter.LOADING_END);
        } else {
            meetingAdapter.setLoadState(meetingAdapter.LOADING_END);
        }
    }

    @Override
    public void starRefresh() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void stopRefresh() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void loadMeetingList(List<MyMeetingModel.DataBean> list) {
        if (currentPage == 1) {
            beans = list;
        } else {
            beans.addAll(list);
        }
        if (beans.isEmpty()) {
            showNoDataView();
        } else {
            hideNoDataView();
        }
        meetingAdapter.refreshData(beans);
    }

    @Override
    public void loadNotiList(List<NotiModel.DataBean> list) {
        if (currentPage == 1) {
            notiBeans = list;
        } else {
            notiBeans.addAll(list);
        }
        if (notiBeans.isEmpty()) {
            showNoDataView();
        } else {
            hideNoDataView();
        }
        notiAdapter.refreshData(notiBeans);
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        queryList(currentPage);
    }

    @Override
    public void showMeetingContentDialog(String content) {
        showReasonDialog(0, 0, content, false);
    }

    @Override
    public void showReasonDialog(long id, int position, String reason, boolean isReason) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        @SuppressLint("InflateParams") View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_reason, null);
        builder.setView(contentView);
        AlertDialog alertDialog = builder.create();
        TextView tvClose = contentView.findViewById(R.id.dialog_title);
        EditText editText = contentView.findViewById(R.id.dialog_edit);
        editText.setText(reason);
        if (!isReason) {
            editText.setEnabled(false);
            tvClose.setText("主题内容");
        } else {
            editText.setEnabled(true);
            tvClose.setText("不与会原因");
        }
        TextView tvConfirm = contentView.findViewById(R.id.dialog_confirm);
        tvConfirm.setOnClickListener(view -> {
            if (isReason) {
                String trim = editText.getText().toString().trim();
                commitReason(position, id, trim, alertDialog);
            } else {
                alertDialog.dismiss();
            }
        });
        tvClose.setOnClickListener(view -> alertDialog.dismiss());
        alertDialog.show();
    }

    //提交不参加原因
    private void commitReason(int position, long id, String mReason, AlertDialog alertDialog) {
        if (TextUtils.isEmpty(mReason)) {
            Toast.makeText(this, "您未填写原因", Toast.LENGTH_SHORT).show();
        } else {
            presenter.editItem(id, "2", mReason, position);
            alertDialog.dismiss();
        }
    }

    @Override
    public void notifyList(int position, String isCanhui) {
        NotiModel.DataBean dataBean = notiBeans.get(position);
        dataBean.setIsCanhui(isCanhui);
        notiAdapter.refreshData(position, isCanhui);
    }

    @Override
    public void checkAttachCount(List<MyAttachModel> list, String[] strings) {
        if (list.size() < 2) {
            MyAttachModel model = list.get(0);
            if (null != model) {
                openPDF(model.fileId, model.fileName);
            } else {
                ToastUtils.showSingleToast("此附件参数错误");
            }
        } else {
            showAttachDialog(list, strings);
        }
    }

    @Override
    public void showAttachDialog(List<MyAttachModel> fileListBeans, String[] strings) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialogStyle);
        @SuppressLint("InflateParams") View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_attach, null);
        builder.setView(contentView);
        AlertDialog alertDialog = builder.create();
        ListView listView = contentView.findViewById(R.id.dialog_list);
        TextView title = contentView.findViewById(R.id.dialog_title);
        TextView confirm = contentView.findViewById(R.id.dialog_confirm);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.item_single_choice, strings);
        listView.setAdapter(arrayAdapter);
        listView.setItemChecked(0, true);
        listView.setOnItemClickListener((parent, view, position, id) -> pdfPickPosition = position);
        title.setOnClickListener(v -> alertDialog.dismiss());
        confirm.setOnClickListener(v -> {
            if (!strings[pdfPickPosition].equals("此附件参数错误")) {
                MyAttachModel model = fileListBeans.get(pdfPickPosition);
                String fileName = model.fileName;
                String fileId = model.fileId;
                openPDF(fileId, fileName);
                alertDialog.dismiss();
            } else {
                Toast.makeText(this, "该附件错误，不能打开", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        Objects.requireNonNull(alertDialog.getWindow()).setLayout(4 * DesignUtils.getScreenWidth(this) / 5, 2 * DesignUtils.getScreenHeight(this) / 5);
    }

    private void openPDF(String fileId, String fileName) {
        String url;
        try {
            url = "https://fs.itsv.com.cn:9528/api/open/file/previewPDF?fileId=" + fileId + "&fileName=" + URLEncoder.encode(fileName, "utf-8");
            Intent intent = new Intent(this, PdfActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("fileId", fileId);
            startActivity(intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.iv_add)
    public void onClick() {
        Intent roomIntent = new Intent(this, AddActivity.class);
        roomIntent.putExtra("sdkCid", "");
        roomIntent.putExtra("sdkConferenceUrl", "https://bj189.scmeeting.com/");
        roomIntent.putExtra("sdkRoomNum", "");
        roomIntent.putExtra("userId", mUserId);
        startActivity(roomIntent);
    }

    @OnClick(R.id.iv_create)
    public void create() {
        presenter.createTempConference();
    }

    @Override
    public void tempMeeting(String roomNum) {
//        joinMeetingRoom(roomNum, userName, "");
        presenter.validEnableInto(roomNum,userName,"",true,"Aa123456",String.valueOf(mUserId));
    }

    @OnClick(R.id.button_date)
    public void chooseDate() {
        try {
            Date date = format.parse(meetingTime);
            Calendar calendar = Calendar.getInstance();
            if (null != date) {
                calendar.setTime(date);
            }
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
                int trueMonth = monthOfYear + 1;
                String month = trueMonth > 9 ? String.valueOf(trueMonth) : "0" + trueMonth;
                String day = dayOfMonth > 9 ? String.valueOf(dayOfMonth) : "0" + dayOfMonth;
                meetingTime = year + "-" + month + "-" + day;
                setDateButton();
                presenter.userIndexList(Constant.TOKEN, currentPage, 30, meetingTitle, "", meetingTime);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showNoDataView() {
        tvNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoDataView() {
        tvNoData.setVisibility(View.GONE);
    }

    public void joinMeetingRoom(String roomnumber, String username, String sdkCid,boolean isModerator,String psw,String mId) {
        showLoading();
        //ManisApiInterface.app.guestLogin(String roomnumber, String username, Context context,  String conferencePass, ManisApiInterface.OnGuestLoginToRoomEvents2 onGuestLoginToRoomEvents2);
        //密码固定传Aa123456
        ManisApiInterface.app.guestLogin(roomnumber, username, this, "Aa123456", (b, s, conferenceInfo, userInfo) -> {
            if (b) {
                hideLoading();
                Intent intent = new Intent(MtNotifyActivity.this, RoomActivity.class);
                String record = conferenceInfo.getRecord();
//                String userId = userInfo.getmUserId();
                String jid = userInfo.getJid();
                String roomNumber = conferenceInfo.getRoomNumber();
                String userName = userInfo.getmUserName();
                intent.putExtra("record", record);
                intent.putExtra("jid", jid);
                intent.putExtra("isController", isModerator);
                intent.putExtra("moderatorPsw", psw);
                intent.putExtra("userName", userName);
                intent.putExtra("roomNumber", roomNumber);
                mRoomNum = roomNumber;
                intent.putExtra("sdkCid", sdkCid);
                intent.putExtra("mic", false);
                intent.putExtra("video", true);
                intent.putExtra("broad", false);
                intent.putExtra("userId", mId);
                startActivity(intent);
            } else {
                hideLoading();
                ToastUtils.showSingleToast(s);
            }
        });
    }

    @Override
    public void showLoading() {
        loadingProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void hideLoading() {
        loadingProgress.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!isMtNotify) {
            currentPage = 1;
            queryList(currentPage);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeMeeting(String s) {
        if (s.equals("close")) {
            presenter.closeMeeting(mRoomNum);
        }
    }

    @Override
    public void finish() {
        super.finish();
        EventBus.getDefault().unregister(this);
    }
}
