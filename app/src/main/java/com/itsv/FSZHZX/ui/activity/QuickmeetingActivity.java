package com.itsv.FSZHZX.ui.activity;



import android.os.Bundle;

import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.base.BaseAppCompatActivity;

import butterknife.OnClick;

public class QuickmeetingActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quickmeeting);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_quickmeeting;
    }

    @Override
    protected void initViewsAndEnvents() {

    }

    @OnClick(R.id.btn_joinMt)
    public void onClick() {
//        Intent intent = new Intent(this, RoomActivity.class);
//        intent.putExtra("record", record);
//        intent.putExtra("roomNumber", roomNumber);
//        intent.putExtra("mic", switchMic.isChecked());
//        intent.putExtra("cam", switchCam.isChecked());
//        intent.putExtra("broad", switchBroad.isChecked());
//        intent.putExtra("userId", userId);
    }
}
