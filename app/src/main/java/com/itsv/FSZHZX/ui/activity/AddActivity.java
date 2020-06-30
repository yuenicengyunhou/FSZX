package com.itsv.FSZHZX.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.api.UserApi;
import com.itsv.FSZHZX.base.ApiHelper;
import com.itsv.FSZHZX.base.BaseAppCompatActivity;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.utils.ToastUtils;
import com.manis.core.interfaces.ManisApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends BaseAppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_roomNum)
    EditText etRoomNum;
    @BindView(R.id.et_urName)
    EditText etUrName;
    private long userId;
    private boolean isStage;
    private boolean initMeeting;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_add;
    }

    @Override
    protected void initViewsAndEnvents() {
//        personalData();
        tvTitle.setText("加入会议室");
        if (TextUtils.isEmpty(Constant.USER_NAME)) {
            etUrName.setVisibility(View.VISIBLE);
        }
        initSdk();

    }

    private void initSdk() {
        Intent intent = getIntent();
        userId = intent.getLongExtra("userId", 0);
    }

    @OnClick({R.id.btn_joinMt, R.id.iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_joinMt:
                String roomNum = getEditContent(etRoomNum);
                if (roomNum.isEmpty()) {
                    ToastUtils.showSingleToast("请填写会议号");
                    return;
                }
                if (etUrName.getVisibility() == View.VISIBLE) {
                    String editContent = getEditContent(etUrName);
                    if (TextUtils.isEmpty(editContent)) {
                        ToastUtils.showSingleToast("请填写名称");
                        return;
                    } else {
                        Constant.USER_NAME = editContent;
                    }
                }
                if (!TextUtils.isEmpty(Constant.USER_NAME)) {
                    if (initMeeting) {
                        ToastUtils.showSingleToast("正在进入会议");
                    } else {
                        validEnableInto(roomNum, Constant.USER_NAME);
                    }
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }

    }

    private String getEditContent(EditText editText) {
        return editText.getText().toString().trim();
    }

    private void joinMeetingRoom(String roomnumber, String username) {
        ManisApiInterface.app.guestLogin(roomnumber, username, this, "", (b, s, conferenceInfo, userInfo) -> {
            initMeeting = false;
            if (b) {
                Intent intent = new Intent(AddActivity.this, RoomActivity.class);
                String record = conferenceInfo.getRecord();
//                String userId = userInfo.getmUserId();
                String jid = userInfo.getJid();
                String roomNumber = conferenceInfo.getRoomNumber();
                String userName = userInfo.getmUserName();
                intent.putExtra("record", record);
                intent.putExtra("jid", jid);
                intent.putExtra("userName", userName);
                intent.putExtra("roomNumber", roomNumber);
                intent.putExtra("sdkCid", "");
                intent.putExtra("mic", false);
                intent.putExtra("cam", true);
                intent.putExtra("broad", false);
                intent.putExtra("userId", this.userId);
                intent.putExtra("isController", false);
                intent.putExtra("moderatorPsw", "");
                intent.putExtra("isStage", isStage);
                startActivity(intent);
            } else {
                ToastUtils.showSingleToast(s);
            }
        });
    }

    public void validEnableInto(String roomNum, String userName) {
        initMeeting = true;
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.meetingURL)
                .createService(UserApi.class);
        Call<ResponseBody> call = api.validEnableInto(Constant.TOKEN, roomNum);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        JSONObject object = new JSONObject(params);
                        boolean success = object.getBoolean("success");
                        if (success) {
//                            String data = object.getString("data");
                            questStage(roomNum, userName);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                initMeeting = false;
            }
        });
    }

    private void questStage(String roomNum, String userName) {
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.meetingURL).createService(UserApi.class);
        Call<ResponseBody> call = api.getStageByRoomNumber(roomNum);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        JSONObject object = new JSONObject(params);
                        boolean success = object.getBoolean("success");
                        String data = object.getString("data");
                        if (success) {
                            isStage = data.equals(String.valueOf(userId));
                            joinMeetingRoom(roomNum, userName);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                ToastUtils.showSingleToast("获取主屏失败");
                initMeeting = false;
            }
        });
    }
}
