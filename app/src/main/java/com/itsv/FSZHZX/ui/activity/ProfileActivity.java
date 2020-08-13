package com.itsv.FSZHZX.ui.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.base.MyBaseMvpActivity;
import com.itsv.FSZHZX.model.Profile;
import com.itsv.FSZHZX.presenter.ProfilePre;
import com.itsv.FSZHZX.ui.adapter.ProfileAdapter;
import com.itsv.FSZHZX.view.ProfileView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;

public class ProfileActivity extends MyBaseMvpActivity<ProfileActivity, ProfilePre> implements ProfileView {

    private ProfilePre presenter;
    @BindString(R.string.profile)
    String mine;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.recycler_profile)
    RecyclerView recyclerView;
    private List<Profile> list;

    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        presenter = new ProfilePre();
        return presenter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_profile_1;
    }

    @Override
    protected void initViewsAndEnvents() {
        tvTitle.setText(mine);
        presenter.userDetailInfo();
        list = new ArrayList<>();
    }


    @Override
    public void queryProfile() {

    }

    @Override
    public void loadViews(List<Profile> profiles) {
        list = profiles;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ProfileAdapter adapter = new ProfileAdapter(list, this);
        recyclerView.setAdapter(adapter);
        adapter.setOnMyItemClickListener(position -> {
                Profile profile = list.get(position);
                String word = profile.getWord();
                if (word.equals("姓名") || word.equals("生日") || word.equals("性别")) {
                    Intent intent = new Intent(ProfileActivity.this, ModifyActivity.class);
                    String content = list.get(position).getContent();
                    intent.putExtra("content", content);
                    intent.putExtra("word", word);
                    startActivity(intent);
            }

        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        presenter.userDetailInfo();
    }
}
