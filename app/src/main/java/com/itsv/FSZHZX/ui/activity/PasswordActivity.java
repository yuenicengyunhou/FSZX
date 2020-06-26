package com.itsv.FSZHZX.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.base.MyBaseMvpActivity;
import com.itsv.FSZHZX.presenter.PsdPresenter;
import com.itsv.FSZHZX.view.PsdView;

import butterknife.BindView;
import butterknife.OnClick;

public class PasswordActivity extends MyBaseMvpActivity<PasswordActivity, PsdPresenter> implements PsdView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.edit_oldPass)
    EditText editOldPass;
    @BindView(R.id.edit_newPass)
    EditText editNewPass;
    @BindView(R.id.edit_newPass2)
    EditText newPass2;
    @BindView(R.id.toolbar_button)
    Toolbar mToolbar;
    private PsdPresenter presenter;


    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        presenter = new PsdPresenter();
        return presenter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_password;
    }

    @Override
    protected void initViewsAndEnvents() {
        tvTitle.setText("修改密码");
        initToolbar(mToolbar, false);
    }

    @OnClick(R.id.tv_commit)
    public void onClick(View v) {
        if (v.getId() == R.id.tv_commit) {
            String oldPsd = getEditText(editOldPass);
            if (TextUtils.isEmpty(oldPsd)) {
                showErrorToast("请输入旧密码");
                return;
            }
            String newPsd = getEditText(editNewPass);
            if (TextUtils.isEmpty(oldPsd)) {
                showErrorToast("请输入新密码");
                return;
            }
            String pssword2 = getEditText(newPass2);
            if (newPsd.equals(pssword2)) {
                presenter.editPassword(oldPsd, newPsd);
            } else {
                showErrorToast("两次输入新密码不一致");
            }
        }
    }

    private String getEditText(EditText editText) {
        return editText.getText().toString().trim();
    }
}
