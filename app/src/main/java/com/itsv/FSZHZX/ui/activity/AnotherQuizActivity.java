package com.itsv.FSZHZX.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.api.UserApi;
import com.itsv.FSZHZX.base.ApiHelper;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.model.QuizModel;
import com.itsv.FSZHZX.ui.fragment.QuizFragment;
import com.itsv.FSZHZX.ui.fragment.QuizListFragment;
import com.itsv.FSZHZX.utils.MyPagerHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnotherQuizActivity extends AppCompatActivity {

    private ArrayList<Fragment> fragments;
    private ViewPager2 viewPager;
    private QuizListFragment listFragment;
    private QuizFragment quizFragment;
    private TextView tvBackList;
    private List<QuizModel.DataBean> dataBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_quiz);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("在线答题");

        tvBackList = findViewById(R.id.tv_back_list);
        tvBackList.setOnClickListener(v -> transToListFragment());

        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem == 0) {
                finish();
            } else {
                transToListFragment();
            }
        });

        fragments = new ArrayList<>();
        listFragment = new QuizListFragment();
        fragments.add(listFragment);
        quizFragment = new QuizFragment();
        fragments.add(quizFragment);


        viewPager = findViewById(R.id.viewPager);

        initViewPager();
        viewPager.setCurrentItem(1);
        viewPager.setCurrentItem(0);

        listFragment.setTabListener((quizItemPosition, fragmentIndex) -> {
            MyPagerHelper.setCurrentItem(viewPager, fragmentIndex, 300);
            quizFragment.onListItemClick(quizItemPosition, this.dataBeanList);
        });

        getRoundQuestion();
    }

    private void initViewPager() {
        viewPager.setUserInputEnabled(false);
        viewPager.setAdapter(new FragmentStateAdapter(this) {

            @Override
            public int getItemCount() {
                return fragments.size();
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    tvBackList.setVisibility(View.GONE);
                } else {
                    tvBackList.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    public void getRoundQuestion() {
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.BASEURL)
                .createService(UserApi.class);
        Call<ResponseBody> call = api.getRoundQuestion(Constant.TOKEN);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        Gson gson = new Gson();
                        QuizModel model = gson.fromJson(params, QuizModel.class);
                        dataBeanList = model.getData();
                        listFragment.setListData(dataBeanList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (null != viewPager) {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem == 0) {
                finish();
            } else {
                transToListFragment();
            }
        }
    }

    private void transToListFragment() {
        MyPagerHelper.setCurrentItem(viewPager, 0, 300);
    }
}
