package com.itsv.FSZHZX.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.RelativeLayout;

import com.itsv.FSZHZX.R;
import com.manis.core.entity.Participant;
import com.manis.core.entity.Stats;
import com.manis.core.entity.SurfaceViewHolder;
import com.manis.core.entity.VideoStats;
import com.manis.core.interfaces.ManisApiInterface;

import org.webrtc.TextureViewRenderer;

import java.util.List;
import java.util.Map;

public class TestyActivity extends AppCompatActivity implements ManisApiInterface.UpdateUIEvents {

    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testy);
        relativeLayout = findViewById(R.id.relative);
        initData();
        connectToRoom();
    }
    private String record = "";
    private String roomNumber = "";
    private String userName = "";
    private boolean initFlag = false;        // 会议初始化标识
    private boolean mic = true; //麦克风状态，true静音，false非静音
    private boolean video = true; //视频状态，true打开，false关闭
    private boolean broad;
    private boolean toBeModerator;
    private String moderatorPsw;
    private boolean isShareScreen;
    private String sdkRoomNum;
    private String mJid = "";
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

    private void connectToRoom() {
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

    @Override
    public void onIceFailed() {

    }

    @Override
    public void onVideoDrawingBoard(String s, String s1) {

    }

    @Override
    public void iMMessageRecever(String s, String s1, String s2, String s3) {

    }

    @Override
    public void onAddStream(SurfaceViewHolder surfaceViewHolder) {

    }

    @Override
    public void onRemoveStream(SurfaceViewHolder surfaceViewHolder) {

    }

    @Override
    public void onAddLocalStream(SurfaceViewHolder surfaceViewHolder) {

    }

    @Override
    public void onVideoPosition(boolean b, List<String> list) {

    }

    @Override
    public void beHostToOperate(boolean b, int i, String s) {

    }

    @Override
    public void beMeetingSuperAdministratorControl(int i, boolean b, String s) {

    }

    @Override
    public void updatePartcipantList(List<Participant> list) {

    }

    @Override
    public void updateAttendeeList(Map<String, Participant> map) {

    }

    @Override
    public void raiseHand(ManisApiInterface.RaiseHandType raiseHandType, String s, String s1, String s2) {

    }

    @Override
    public void onVideoStatsInfo(VideoStats videoStats) {

    }

    @Override
    public void onStatsInformation(Stats stats) {

    }

    @Override
    public void onWhiteBoard(String s, String s1) {

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

    }
}