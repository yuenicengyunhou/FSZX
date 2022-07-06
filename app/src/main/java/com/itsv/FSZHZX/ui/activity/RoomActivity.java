package com.itsv.FSZHZX.ui.activity;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.api.UserApi;
import com.itsv.FSZHZX.base.ApiHelper;
import com.itsv.FSZHZX.base.BaseAppCompatActivity;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.model.Chat;
import com.itsv.FSZHZX.model.ProfileDetailsM;
import com.itsv.FSZHZX.model.SimpleModel;
import com.itsv.FSZHZX.ui.adapter.ChatAdapter;
import com.itsv.FSZHZX.ui.adapter.PaticipantAdapter;
import com.itsv.FSZHZX.utils.DesignUtils;
import com.itsv.FSZHZX.utils.ToastUtils;
import com.manis.core.callback.MyCallback;
import com.manis.core.entity.Participant;
import com.manis.core.entity.Stats;
import com.manis.core.entity.SurfaceViewHolder;
import com.manis.core.entity.VideoStats;
import com.manis.core.interfaces.ManisApiInterface;

import org.greenrobot.eventbus.EventBus;
import org.webrtc.TextureViewRenderer;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomActivity extends BaseAppCompatActivity implements ManisApiInterface.UpdateUIEvents {

    @BindView(R.id.wholeView)
    RelativeLayout wholeView;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.roomBg)
    TextView roomBg;
    @BindView(R.id.textureViewBoss)
    TextureViewRenderer textureViewBoss;
    @BindView(R.id.screenRelative)
    RelativeLayout screenRelative;
    @BindView(R.id.textureViewMe)
    TextureViewRenderer textureViewMe;
    @BindView(R.id.iv_reverse)
    ImageView btnReverse;
    @BindView(R.id.cb_broad)
    CheckBox cbBroad;
    @BindView(R.id.cb_mic)
    CheckBox check_mic;
    @BindView(R.id.memberLayout)
    LinearLayout memberLayout;
    @BindView(R.id.chatLayout)
    LinearLayout chatLayout;
    @BindView(R.id.recycler_member)
    RecyclerView memberRecycler;
    @BindView(R.id.recycler_chat)
    RecyclerView chatRecycler;
    @BindView(R.id.chat_edit)
    EditText chatEdit;
    @BindView(R.id.edit_member)
    EditText editMember;
    @BindView(R.id.member_mute)
    Button memberMute;
    @BindView(R.id.tv_time)
    TextView tvTime;
    private PaticipantAdapter paticipantAdapter;
    private String mJid = "";
    private boolean isModerator;
    private boolean operate;
    private int operateType;
    private String operateNickName;
    private String raiseHandName = "";
    private String raiseJid;
    private int seconds;
    private TimerTask timerTask;
    private Timer timer;
    private String mRaiseHandType;
    private String record = "";
    private String roomNumber = "";
    private String userName = "";
    private SurfaceViewHolder localView;
    private Handler handler = new UIHandler(this);
    private boolean initFlag = false;        // 会议初始化标识
    private boolean mic = true; //麦克风状态，true静音，false非静音
    private boolean video = true; //视频状态，true打开，false关闭
    private boolean broad;
    //    private Map<String, SurfaceViewHolder> mVideoList = new HashMap<>();
    private List<Participant> mAttendeeList = new ArrayList<>();
    private List<Chat> chats = new ArrayList<>();
    private ChatAdapter chatAdapter;
    private boolean showChats;
    private boolean showMember;
    private String AGREE_TAG = "E664FA13A08B871E586CBD5BA107E217_ON_HANDS_UP_AGREE";
    private String REFUSE_TAG = "E664FA13A08B871E586CBD5BA107E217_ON_HANDS_UP_REJECT";
    private String MUTE_TAG = "E664FA13A08B871E586CBD5BA107E217_AUDIO_MUTED_TRUE_";
    private String DISMUTE_TAG = "E664FA13A08B871E586CBD5BA107E217_AUDIO_MUTED_FALSE_";
    private boolean toBeModerator;
    private String moderatorPsw;
    private boolean isShareScreen;
    private String sdkRoomNum;

    @SuppressLint("HandlerLeak")
    private class UIHandler extends Handler {
        private WeakReference<RoomActivity> mActivity;

        public UIHandler(RoomActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            RoomActivity roomActivity = mActivity.get();
            if (roomActivity == null) return;
            switch (msg.what) {
                case 0://
                    initFlag = true;
                    if (toBeModerator) {
                        getModerator(moderatorPsw);
                    }
                    initTextureOnMyside();
                    break;
                case 3:
                    if (showChats) {
                        if (null != chatAdapter) {
                            chatAdapter.refreshData(chats);
                        }
                    }
                    break;
                case 5:                    // 退出会议室
                    leaveRoom(false);
                    break;
                case 6:
                    if (showMember) {
                        if (null != paticipantAdapter) {
                            paticipantAdapter.setModerator(isModerator);
                            paticipantAdapter.setParticipantList(mAttendeeList);
                        }
                    }
                    break;
                case 8://被管理员实施的操作
                    switch (operateType) {
                        case 1:
                            if (operate) {
                                ToastUtils.showSingleToast("你已被管理员静音");
                                check_mic.setText("禁麦中");
                            } else {
                                check_mic.setText("开麦中");
                            }
                            ManisApiInterface.app.setMic(!operate);
                            check_mic.setChecked(operate);
                            break;
                        case 2:
                            if (operate) {
                                ToastUtils.showSingleToast("你已被管理员禁视频");
                            }
                            ManisApiInterface.app.setVideo(operate);
                            break;
                        case 3:
                            if (!initFlag) {
                                initHint();
                                return;
                            }
                            leaveRoom(true);
                            break;
                        case 4:
                            isModerator = true;
                            ToastUtils.showSingleToast("你已被移交管理员");
                            break;
                        case 5:
                            ToastUtils.showSingleToast("你已被管理员改名为" + operateNickName);
                            break;
                    }
                    break;
                case 9:
                    if (mRaiseHandType.equals("REFUSE")) {
                        ToastUtils.showSingleToast("管理员拒绝了你的举手");
                    } else if (mRaiseHandType.equals("AGREE")) {
                        check_mic.setChecked(false);
                        ToastUtils.showSingleToast("管理员同意了你的举手");
                    } else {
                        showMicOrCamPermissionDialog(raiseHandName + "发来举手申请", raiseJid);
                    }
                    break;
                case 10://计时器专用
                    startTikTok();
                    break;
                case 11:
                    //设置屏幕共享
                    if (isShareScreen) {
                        initTextureScreen();
                    } else {
                        initTextureBoss();
//                        shareScreenHolder.unbundleTextureView(textureViewBossScreen);
//                        shareScreenHolder.releaseTexture();
                        screenRelative.removeAllViews();
                        shareScreenHolder = null;
                    }
                    break;
                case 12://主屏画面
                    TextureView textureView = combinedHolder.initTextureView(textureViewBoss, false);
                    textureSizeAutoChange(combinedHolder, textureView);
                    break;
                case 13:
                    ToastUtils.showSingleToast("初始化流失败");
                    break;
            }
        }

    }

    private void startTikTok() {
        seconds++;
        String time;
        int minute = seconds / 60;
        int hour = seconds / 60 / 60;
        int second = seconds % 60;
        if (hour < 10) {
            time = "0" + hour;
        } else {
            time = String.valueOf(hour);
        }
        if (minute < 10) {
            time = time + ":0" + minute;
        } else {
            time = time + ":" + minute;
        }
        if (second < 10) {
            time = time + ":0" + second;
        } else {
            time = time + ":" + second;
        }
        if (null != tvTime) {
            tvTime.setText(time);
        }
    }

    /*设置textureview适应画面比例*/
    private void textureSizeAutoChange(SurfaceViewHolder viewHolder, TextureView textureView) {
        viewHolder.setOnVideoSizeChangeListener((s, i, i1, i2) -> {
            if (i != 0) {
                if (i > i1) {
                    int screenWidth = DesignUtils.getScreenWidth(RoomActivity.this);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenWidth, screenWidth * i1 / i);
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    textureView.setLayoutParams(layoutParams);
                } else {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    textureView.setLayoutParams(layoutParams);
                }
            }
        });
    }

    private void initTextureOnMyside() {
        if (localView != null) {
            localView.initTextureView(textureViewMe, false);
        } else {
            handler.sendEmptyMessageDelayed(0, 1000);
        }
    }

    boolean hasInitBoss = false;

    private void initTextureBoss() {
        textureViewBoss.setVisibility(View.VISIBLE);
        screenRelative.setVisibility(View.GONE);
        if (combinedHolder != null) {
            if (!hasInitBoss) {
                combinedHolder.initTextureView(textureViewBoss, false);
                textureSizeAutoChange(combinedHolder, textureViewBoss);
                hasInitBoss = true;
            }
        } else {
            handler.sendEmptyMessageDelayed(11, 1000);
        }
    }

    private void initTextureScreen() {
        screenRelative.setVisibility(View.VISIBLE);
        textureViewBoss.setVisibility(View.GONE);
        if (shareScreenHolder != null) {
            TextureView textureView = shareScreenHolder.createTextureView(this);
            textureSizeAutoChange(shareScreenHolder, textureView);
            screenRelative.addView(textureView);
        } else {
            handler.sendEmptyMessageDelayed(11, 1000);
        }
    }


    @Override
    protected int getLayoutID() {
        return R.layout.activity_room;
    }

    @Override
    protected void initViewsAndEnvents() {
        keepScreenOn();
        initData();
        initViews();
        connectToRoom();
        startClock();
        checkin();
    }

    private void startClock() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 10;
                handler.sendMessage(msg);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }

    private void keepScreenOn() {
        setDarkStatusBar();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void initData() {
        Intent intent = getIntent();
        record = intent.getStringExtra("record");
        sdkRoomNum = intent.getStringExtra("sdkRoomNum");
        roomNumber = intent.getStringExtra("roomNumber");
        mJid = intent.getStringExtra("jid");
        toBeModerator = intent.getBooleanExtra("isController", false);
        moderatorPsw = intent.getStringExtra("moderatorPsw");
        userName = intent.getStringExtra("userName");
        mic = intent.getBooleanExtra("mic", true);
        video = intent.getBooleanExtra("video", true);
        broad = intent.getBooleanExtra("broad", false);
    }

    private void initViews() {
        tvId.setText(roomNumber);
        initMemberLayout();
        initChatLayout();
        cbBroad.setChecked(broad);
        cbBroad.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!initFlag) {
                initHint();
            } else {
                ManisApiInterface.app.speakerSwitch();
            }
        });
        TextView tvLeave = findViewById(R.id.tv_leave);
        tvLeave.setOnClickListener(view -> showLeaveDialog());
    }

    private void initMemberLayout() {
        memberRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        paticipantAdapter = new PaticipantAdapter(this, mAttendeeList);
        memberRecycler.setAdapter(paticipantAdapter);
        editMember.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = editMember.getText().toString().trim();
                List<Participant> tempList = new ArrayList<>();
                for (int i = 0; i < mAttendeeList.size(); i++) {
                    Participant participant = mAttendeeList.get(i);
                    String nickname = participant.getNickname();
                    if (nickname.contains(temp)) {
                        tempList.add(participant);
                    }
                }
                paticipantAdapter.setModerator(isModerator);
                paticipantAdapter.setParticipantList(tempList);

            }
        });
        paticipantAdapter.setItemClickListener(new PaticipantAdapter.ItemClickListener() {
            @Override
            public void onCam(String jid, boolean isOn) {
                ManisApiInterface.app.hostVideoSet(jid, isOn);
            }

            @Override
            public void onMic(String jid, boolean isOn) {
                ManisApiInterface.app.hostMute(jid, isOn);
                if (isOn) {
                    ManisApiInterface.app.sendImMessage(MUTE_TAG + jid, userName, "");
                } else {
                    ManisApiInterface.app.sendImMessage(DISMUTE_TAG + jid, userName, "");
                }
            }
        });
    }

    private void initChatLayout() {
        chatRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        chatAdapter = new ChatAdapter(this, chats);
        chatRecycler.setAdapter(chatAdapter);
    }

    private void connectToRoom() {
//        ToastUtils.showSingleToast("连接会议");
        ManisApiInterface.app.connectToRoom(true, //启用音频设备
                true,            //启用视频设备
                mic,        //音频开关
                video,        //视频开关
                !broad,        //听筒模式开关
                true,    //视频流开关
                this,        //当前的activity
                record,        //录播资源id，通过创建或加入会议接口回调参数ConferenceInfo的getRecord()方法获取
                this,    //回调监听
                ManisApiInterface.VideoQuality.HD720,
                true,
                () -> {
                    //回调为异步线程，通知主线程弹出提示
                });
    }

    @OnClick({R.id.tv_member, R.id.tv_more, R.id.iv_reverse, R.id.member_title, R.id.chat_title, R.id.chat_send, R.id.member_mute, R.id.tv_chat})
    public void onClick(View v) {
        if (!initFlag) {
            initHint();
            return;
        }
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_member:
                showPopLayout(true, memberLayout);
                break;
            case R.id.member_title:
                hidePopLayout(memberLayout);
                break;
            case R.id.tv_more:
                showMorePopWindow();
                break;
            case R.id.chat_title:
                hidePopLayout(chatLayout);
                break;
//            case R.id.tv_leave:
//                showLeaveDialog();
//                break;
            case R.id.iv_reverse:
                ManisApiInterface.app.onCameraSwitch();//
                break;
            case R.id.chat_send:
                String trim = chatEdit.getText().toString().trim();
                if (!trim.isEmpty()) {
                    ManisApiInterface.app.sendImMessage(trim, userName, "");
                    chatEdit.setText("");
                    chats.add(new Chat(trim, userName, "", "", 1));
                    hideInput();
                    chatAdapter.refreshData(chats);
                }
                break;
            case R.id.member_mute:
                if (isModerator) {
                    turnOffAllMic(memberMute.getText().toString().trim().equals("全员静音"));
                } else {
                    ToastUtils.showSingleToast("请先获取管理员资格");
                }
                break;
            case R.id.tv_chat:
                toChat();
                break;
        }
    }

    private void showLeaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定离开会议吗？");
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("确定", (dialog, which) -> {
            handler.sendEmptyMessage(5);
            dialog.dismiss();
        });
        builder.show();
    }

    private void turnOffAllMic(boolean isMute) {
        ManisApiInterface.app.setConferenceCtrl(roomNumber, mJid, ManisApiInterface.ConferencesCtrl.ALLAUDIO, isMute, new MyCallback() {
            @Override
            public void onSucess(String message) {
                ToastUtils.showSingleToast(message);
                String text = isMute ? "关闭全员静音" : "全员静音";
                memberMute.setText(text);
            }

            @Override
            public void onFailed(String message) {
                ToastUtils.showSingleToast(message);
            }
        });
    }

    private void hidePopLayout(LinearLayout layout) {
        showChats = false;
        showMember = false;
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.pophidden_anim);
        layout.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private void showPopLayout(boolean isMember, LinearLayout layout) {
        if (isMember) {
            showMember = true;
            if (isModerator) {
                memberMute.setVisibility(View.VISIBLE);
            } else {
                memberMute.setVisibility(View.GONE);
            }
            if (null != paticipantAdapter) {
                paticipantAdapter.setModerator(isModerator);
                paticipantAdapter.setParticipantList(mAttendeeList);
            }
        } else {
            showChats = true;
            if (null != chatAdapter) {
                chatAdapter.refreshData(chats);
            }
        }
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.popshow_anim);
        layout.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void refreshAttendList(Map<String, Participant> map) {
        mAttendeeList.clear();
        for (String key : map.keySet()) {
            Participant participant = map.get(key);
            mAttendeeList.add(participant);
        }
    }

    private void initHint() {
        ToastUtils.showSingleToast("会议初始化中，请稍后尝试操作。");
    }

    //下面是会议的监听
    @Override
    public void onIceFailed() {

    }

    @Override
    public void onVideoDrawingBoard(String s, String s1) {

    }

    //message：消息内容 name:昵称 toJid：发言指向用户jid fromJid：发言用户Jid
    @Override//接收消息
    public void iMMessageRecever(String message, String name, String toJid, String fromJid) {
        String NEW_MEMBER_TAG = "E664FA13A08B871E586CBD5BA107E217_NOTIFY_";
        String ROLLBACK_TAG = "E664FA13A08B871E586CBD5BA107E217_ROLLBACK_";
        String HANDSUP_TAG = "E664FA13A08B871E586CBD5BA107E217_HANDS_UP_";
        if (message.contains(AGREE_TAG)) {
            mRaiseHandType = "AGREE";
            raiseHandName = name;
            raiseJid = message.substring(50);
            handler.sendEmptyMessage(9);
        } else if (message.contains(REFUSE_TAG)) {
            mRaiseHandType = "REFUSE";
            raiseHandName = name;
            raiseJid = message.substring(51);
            handler.sendEmptyMessage(9);
        } else if (message.contains(HANDSUP_TAG)) {
            mRaiseHandType = "OTHER";
            raiseHandName = name;
            raiseJid = message.substring(42);
            handler.sendEmptyMessage(9);
        } else if (message.contains(MUTE_TAG) || message.contains(DISMUTE_TAG)) {
        } else if (message.contains(ROLLBACK_TAG)) {
        } else {
            if (!message.contains(NEW_MEMBER_TAG)) {
                int temp = name.equals(userName) ? 1 : 0;
                Chat chat = new Chat(message, name, fromJid, toJid, temp);
                chats.add(chat);
                handler.sendEmptyMessage(3);
            }
        }
    }

    private SurfaceViewHolder combinedHolder;
    private SurfaceViewHolder shareScreenHolder;

    @Override
    public void onAddStream(SurfaceViewHolder surfaceViewHolder) {
//        String jid = surfaceViewHolder.getJid();
        if (surfaceViewHolder.isScreen()) {
            isShareScreen = true;
            shareScreenHolder = surfaceViewHolder;
            handler.sendEmptyMessage(11);
        } else {
            isShareScreen = false;
            combinedHolder = surfaceViewHolder;
//            mVideoList.put(jid, surfaceViewHolder);
            handler.sendEmptyMessageDelayed(12, 1000);
        }
    }

    @Override
    public void onRemoveStream(SurfaceViewHolder surfaceViewHolder) {
        if (surfaceViewHolder.isScreen()) {//如果是共享屏幕流，则移除这个流，并切回原流
            isShareScreen = false;
            handler.sendEmptyMessage(11);
        }
    }

    @Override
    public void onAddLocalStream(SurfaceViewHolder surfaceViewHolder) {
        localView = surfaceViewHolder;
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    /**
     * 视频排序
     * <p>
     * /* * @ param /jids          用于根据 jid 序列对视频进行设置排序
     * para m /isSplitScreen 用于设置分屏和屏幕模式，true为分屏模式，false为屏幕模式
     */
    @Override
    public void onVideoPosition(boolean b, List<String> list) {
    }

    @Override//全员静音  接触全员静音
    public void beHostToOperate(boolean b, int i, String s) {
        operate = b;
        operateType = i;
        operateNickName = s;
        handler.sendEmptyMessage(8);
    }

    @Override//被静音状态下，举手被同意
    public void beMeetingSuperAdministratorControl(int i, boolean b, String s) {
        if (i == 7) {
            isModerator = b;
        }
    }

    @Override
    public void updatePartcipantList(List<Participant> list) {
    }

    @Override
    public void updateAttendeeList(Map<String, Participant> map) {
        refreshAttendList(map);
        handler.sendEmptyMessage(6);
    }

    @Override//无论是否被静音 被拒绝举手  被同意举手
    public void raiseHand(ManisApiInterface.RaiseHandType raiseHandType, String s, String s1, String s2) {
        mRaiseHandType = raiseHandType.toString();
        raiseHandName = s;
        raiseJid = s1;
        handler.sendEmptyMessage(9);

    }

    @Override
    public void onVideoStatsInfo(VideoStats videoStats) {
//        Log.e("WQ", "videoStats");
    }

    @Override
    public void onStatsInformation(Stats stats) {
//        Log.e("WQ", "onStatsInformation");
    }

    @Override
    public void onWhiteBoard(String s, String s1) {
//        Log.e("WQ", "onWhiteBoard");
    }

    @Override
    public void memberLeave(String s) {
    }

    @Override
    public void onRecordStart(String s) {
    }

    @Override
    public void onRecordFinish() {
    }

    @Override
    public void memberJoin(String s) {
    }

    @Override
    public void onLocalVideoPreview(TextureViewRenderer textureViewRenderer) {
//        textureViewMe = textureViewRenderer;
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        textureViewMe.setLayoutParams(layoutParams);
//        frameLayout.addView(textureViewMe);
    }

    @Override
    public void onBackPressed() {
        if (showMember || showChats) {
            if (showMember) {
                hidePopLayout(memberLayout);
            } else {
                hidePopLayout(chatLayout);
            }
        } else {
            showLeaveDialog();
        }
    }

    private void showMorePopWindow() {
        @SuppressLint("InflateParams") View contentView = LayoutInflater.from(this).inflate(R.layout.pop_more, null);
        PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null, (Bitmap) null));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        setMoreView(contentView, popupWindow);
        popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
    }

    private void setMoreView(View contentView, PopupWindow popupWindow) {
        TextView dismiss = contentView.findViewById(R.id.pop_dismiss);
        dismiss.setOnClickListener(view -> popupWindow.dismiss());
        TextView invite = contentView.findViewById(R.id.pop_invite);
        invite.setOnClickListener(view -> inviteNewMembers());
        TextView tvKing = contentView.findViewById(R.id.pop_king);
        tvKing.setOnClickListener(view -> {
            showDialog(true);
            popupWindow.dismiss();
        });
        TextView pushOff = contentView.findViewById(R.id.pop_pushOff);
        if (isModerator) {
            pushOff.setVisibility(View.VISIBLE);
        } else {
            pushOff.setVisibility(View.GONE);
        }
        pushOff.setOnClickListener(v -> {
            if (isModerator) {
                showDialog(false);
                popupWindow.dismiss();
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showDialog(boolean isGetModerator) {
        String title = isGetModerator ? "获的管理员资格" : "会议延长时间";
        String hint = isGetModerator ? "输入管理员密码" : "输入延长分钟数";
        String noti = isGetModerator ? "请输入密码" : "请输入分钟数";
        final EditText inputServer = new EditText(this);
        inputServer.setPadding(5 * Constant.SCREEN_DENSITY, 0, 5 * Constant.SCREEN_DENSITY, 0);
        if (isGetModerator) {
            inputServer.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        inputServer.setHint(hint);
        inputServer.setBackground(getResources().getDrawable(R.drawable.rec_grey));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", (dialog, which) -> {
            String string = inputServer.getText().toString().trim();
            if (string.isEmpty()) {
                Toast.makeText(RoomActivity.this, noti, Toast.LENGTH_SHORT).show();
            } else {
                if (isGetModerator) {
                    getModerator(string);
                } else {
                    pushoffMeeting(string);
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void pushoffMeeting(String string) {
        ManisApiInterface.app.recordLengthen(roomNumber, mJid, Integer.decode(string), new MyCallback() {
            @Override
            public void onSucess(String message) {
                //调用成功
                ToastUtils.showSingleToast(message);
            }

            @Override
            public void onFailed(String message) {
                //调用失败
                ToastUtils.showSingleToast(message);
            }
        });
    }

    private void getModerator(String psd) {
        ManisApiInterface.app.getModerator(psd, (b, s, integer) -> {
            isModerator = b;
            ToastUtils.showSingleToast(s);
        });

    }

    private void inviteNewMembers() {
        boolean weixinAvilible = isWeixinAvilible(this);
        if (weixinAvilible) {
            shareToWechat();
        } else {
            ToastUtils.showSingleToast("您未安装微信");
        }
    }

    private void toChat() {
        showPopLayout(false, chatLayout);
    }

    private boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        for (int i = 0; i < pinfo.size(); i++) {
            String pn = pinfo.get(i).packageName;
            if (pn.equals("com.tencent.mm")) {
                return true;
            }
        }
        return false;
    }

    private void shareToWechat() {
        Intent wechatIntent = new Intent(Intent.ACTION_SEND);
        wechatIntent.setPackage("com.tencent.mm");
        wechatIntent.setType("text/plain");
        wechatIntent.putExtra(Intent.EXTRA_TEXT, "您可凭会议室房间号进入会议室：" + roomNumber);
        startActivity(wechatIntent);
    }

    //当被管理员再次启用麦克风或视频是的提示弹窗                 //0麦克风  1视频   2举手
    private void showMicOrCamPermissionDialog(String message, String raiseJid) {
        String positive = "同意";
        String negative = "拒绝";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(positive, (dialog, which) -> {
            hostAgreeRaise(raiseJid);
            dialog.dismiss();
        });
        builder.setNegativeButton(negative, (dialog, which) -> {
            hostRefuseRaise(raiseJid);
            dialog.dismiss();
        });
        builder.show();
    }

    private void hostAgreeRaise(String raiseJid) {
        ManisApiInterface.app.sendImMessage(AGREE_TAG, userName, raiseJid);
    }

    private void hostRefuseRaise(String raiseJid) {
        ManisApiInterface.app.sendImMessage(REFUSE_TAG, userName, raiseJid);
    }

    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    private void leaveRoom(boolean kikOut) {
        //退出后，如果会议室只剩下自己，通知关闭会议
        if (mAttendeeList.size() == 1) {
            EventBus.getDefault().post("close");
        }
        if (initFlag) {
            ManisApiInterface.app.onCallHangUp((b, s) -> {
                if (b) {
                    if (null != timer) {
                        timer.cancel();
                        timer = null;
                    }
                    if (null != timerTask) {
                        timerTask.cancel();
                        timerTask = null;
                    }
                    if (null != handler) {
                        handler.removeMessages(12);
                        handler.removeMessages(0);
                        handler = null;
                    }
                    // 断开链接
                    ManisApiInterface.app.disConnect();

                }
            });
        }

//        ManisApiInterface.app.onCallHangUp((b, s) -> {
//            if (b) {
//                if (null != timer) {
//                    timer.cancel();
//                    timer = null;
//                }
//                if (null != timerTask) {
//                    timerTask.cancel();
//                    timerTask = null;
//                }
//                if (null != handler) {
//                    handler.removeMessages(12);
//                    handler.removeMessages(0);
//                    handler = null;
//                }
//                // 断开链接
//                ManisApiInterface.app.disConnect();
//                finish();
//            }
//        });
        if (kikOut) {
            ToastUtils.showSingleToast("你已被管理员踢出");
        }
        finish();
    }


    private void checkin() {
        String userId = readUserInfoCache();
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.BASEURL)
                .createService(UserApi.class);
        Call<ResponseBody> call = api.autoCheckin(Constant.TOKEN, sdkRoomNum, userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }

    private String readUserInfoCache() {
        SharedPreferences preferences = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        long userId = preferences.getLong("userId", 0);
        return String.valueOf(userId);
    }
}