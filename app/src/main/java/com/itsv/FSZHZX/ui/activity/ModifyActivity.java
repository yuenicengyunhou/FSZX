package com.itsv.FSZHZX.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.base.MyBaseMvpActivity;
import com.itsv.FSZHZX.presenter.ModifyPre;
import com.itsv.FSZHZX.view.ModifyView;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.OnClick;

public class ModifyActivity extends MyBaseMvpActivity<ModifyActivity, ModifyPre> implements ModifyView {

    private ModifyPre presenter;
    @BindView(R.id.edit_modify)
    EditText editText;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindDrawable(R.drawable.button_invalid)
    Drawable buttonInvalid;
    @BindDrawable(R.drawable.selector_button)
    Drawable selectorButton;
    @BindView(R.id.toolbar_button)
    Toolbar mToolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindColor(R.color.colorInvalid)
    int colorInvalid;
    @BindColor(R.color.white)
    int white;
    @BindColor(R.color.colorTextInvalid)
    int colorTextInvalid;
    private String type;

    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        presenter = new ModifyPre();
        return presenter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_modify;
    }

    @Override
    protected void initViewsAndEnvents() {
        tvTitle.setText("修改信息");
        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        type = intent.getStringExtra("word");
        editText.setText(content);
        tvCommit.setBackground(buttonInvalid);
        initToolbar(mToolbar, false);
        tvCommit.setTextColor(colorTextInvalid);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String editText = getEditText();
                if ((editText.equals(content) || editText.isEmpty())) {
                    tvCommit.setTextColor(colorTextInvalid);
                    tvCommit.setBackground(buttonInvalid);
                } else {
                    tvCommit.setTextColor(white);
                    tvCommit.setBackground(selectorButton);
                }
            }
        });
    }

    private String getEditText() {
        return editText.getText().toString().trim();
    }

    @OnClick(R.id.tv_commit)
    public void click() {
        commit();
    }

    @Override
    public void commit() {
        String trim = editText.getText().toString().trim();
        if (type.equals("姓名")) {
            presenter.modifyName(trim);
        } else if (type.equals("性别")) {
            if (trim.equals("男") || trim.equals("女")) {
                presenter.modifyGerder(trim);
            } else {
                showErrorToast("请填写男/女");
            }
        } else {
            presenter.modifyBirthday(trim);
        }
    }
}
