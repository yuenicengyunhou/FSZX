package com.itsv.FSZHZX.ui.activity;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.base.BaseAppCompatActivity;
import com.itsv.FSZHZX.model.GroupListModel;
import com.itsv.FSZHZX.model.NameModel;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

public class AddressDetailsActivity extends BaseAppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.detail_tv_name)
    TextView tvName;
//    @BindView(R.id.detail_tv_duty)
//    TextView tvDuty;
    @BindView(R.id.detail_tv_cell)
    TextView tvCell;
    @BindView(R.id.detail_tv_phone)
    TextView tvPhone;
    @BindView(R.id.detail_tv_mail)
    TextView tvMail;
    @BindView(R.id.detail_iv_head)
    RoundedImageView ivHead;
//    @BindView(R.id.detail_tv_address)
//    TextView tvAddress;
    @BindDrawable(R.mipmap.head_grey)
    Drawable headGrey;
    @BindString(R.string.address_details)
    String details;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_address_details;
    }

    @Override
    protected void initViewsAndEnvents() {
        tvTitle.setText(details);
        Intent intent = getIntent();
        boolean isName = intent.getBooleanExtra("isName", false);
        if (isName) {
            NameModel.DataBean bean = (NameModel.DataBean) intent.getSerializableExtra("bean");
            if (bean != null) {
                String name = bean.getName();
                String placeWork = bean.getPlaceWork();
                String phone = bean.getPhone();
                String address = bean.getAddress();
                String avatar = bean.getAvatar();
                String positionName = bean.getJobName();
                String officeTel = bean.getOfficeTel();
                String party = bean.getParty();
                Glide.with(this).load(avatar).placeholder(headGrey).into(ivHead);
                tvName.setText(name);
//                tvDuty.setText(positionName);
                tvCell.setText(String.format("工作单位\n%s", dealNull(placeWork)));
                tvMail.setText(String.format("党派\n%s", dealNull(party)));
//            tvAddress.setText(String.format("地址\n%s", dealNull(address)));
                tvPhone.setText(String.format("职务\n%s", dealNull(positionName)));
            }
        } else {
            GroupListModel.DataBean.UserListBean bean = (GroupListModel.DataBean.UserListBean) intent.getSerializableExtra("bean");
            if (bean==null) return;
            String name = bean.getName();
            String placeWork = bean.getPlaceWork();
            String phone = bean.getPhone();
            String address = bean.getAddress();
            String positionName = bean.getJobName();
            String avatar = bean.getAvatar();
            String officeTel = bean.getOfficeTel();
            String party = bean.getParty();
            Glide.with(this).load(avatar).placeholder(headGrey).into(ivHead);
            tvName.setText(name);
//            tvDuty.setText(positionName);
            tvCell.setText(String.format("工作单位\n%s", dealNull(placeWork)));
            tvMail.setText(String.format("党派\n%s", dealNull(party)));
//            tvAddress.setText(String.format("地址\n%s", dealNull(address)));
            tvPhone.setText(String.format("职务\n%s", dealNull(positionName)));
        }

    }

    private String dealNull(String string) {
        if (null == string || TextUtils.isEmpty(string)||string.equals("null")) {
            return "未填写";
        } else {
            return string;
        }
    }

    @OnClick(R.id.iv_back)
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        }
    }


}
