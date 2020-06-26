package com.itsv.FSZHZX.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.base.MyBaseMvpActivity;
import com.itsv.FSZHZX.model.MeetingDetaisModel;
import com.itsv.FSZHZX.presenter.MtDetailsPre;
import com.itsv.FSZHZX.view.MtDetailsView;

import java.text.MessageFormat;
import java.util.List;

import butterknife.BindView;

public class MeetingDetailsActivity extends MyBaseMvpActivity<MeetingDetailsActivity, MtDetailsPre> implements MtDetailsView {
    private MtDetailsPre presenter;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.item_noti_sign)
    ImageView ivSign;
    @BindView(R.id.item_noti_title)
    TextView title;
    @BindView(R.id.item_noti_time)
    TextView tvTime;
    @BindView(R.id.item_noti_count)
    TextView tvCount;
    @BindView(R.id.item_noti_paticipant)
    TextView tvPaticipant;
    @BindView(R.id.item_noti_topic)
    TextView tvTopic;
    @BindView(R.id.item_noti_att)
    TextView tvAtt;
    @BindView(R.id.item_noti_attach)
    TextView tvAttach;
    
    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        
        presenter = new MtDetailsPre();
        return presenter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_meeting_details;
    }

    @Override
    protected void initViewsAndEnvents() {
        tvTitle.setText("会议详情");
        Intent intent = getIntent();
        long id = intent.getLongExtra("id", 0);
        if (id==0) return;
        presenter.adviceList(id);

    }

    @Override
    public void loadViews(String meetingTitle, String meetingStartTime, String meetingContent, List<MeetingDetaisModel.DataBean.MeetingUserListBean> meetingUserList) {
        title.setText(meetingTitle);
        tvTime.setText(meetingStartTime);
        tvTopic.setText(meetingContent);
        if (null == meetingUserList||meetingUserList.isEmpty()) {
            tvCount.setText("参会人员：无");
            tvPaticipant.setVisibility(View.GONE);
        }else {
            tvCount.setText(MessageFormat.format("参会人员：{0}人", meetingUserList.size()));
            String members = "";
            for (int i = 0; i < meetingUserList.size(); i++) {
                String name = meetingUserList.get(i).getName();
                members= MessageFormat.format("{0}{1}", members, i == meetingUserList.size() - 1 ? name : name + "，");
            }
            tvPaticipant.setText(members);
        }
    }

}
