package com.itsv.FSZHZX.ui.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.api.UserApi;
import com.itsv.FSZHZX.base.ApiHelper;
import com.itsv.FSZHZX.base.BaseAppCompatActivity;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.databinding.ActivityNewQuizBinding;
import com.itsv.FSZHZX.listener.QuizAnswerListener;
import com.itsv.FSZHZX.model.QuizModel;
import com.itsv.FSZHZX.ui.fragment.QuizFragment;
import com.itsv.FSZHZX.ui.fragment.QuizListFragment;
import com.itsv.FSZHZX.utils.MyPagerHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewQuizActivity extends BaseAppCompatActivity {

    private ArrayList<Fragment> fragments;
    private QuizListFragment listFragment;
    private QuizFragment quizFragment;
    private List<QuizModel.DataBean> quizList = new ArrayList<>();
    private ActivityNewQuizBinding binding;

    @Override
    protected int getLayoutID() {
        return 0;
    }

    @Override
    protected View getLayoutView() {
        binding = ActivityNewQuizBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void initViewsAndEnvents() {
        initToolbar(binding.toolbarLayout.toolbarButton, false);
        binding.toolbarLayout.tvTitle.setText("在线答题");
        binding.toolbarLayout.tvCommit.setText("下一题");
        binding.toolbarLayout.tvCommit.setVisibility(View.GONE);


        binding.toolbarLayout.tvBackList.setOnClickListener(v -> transToListFragment());

        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> {
            int currentItem = binding.viewPager.getCurrentItem();
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


        initViewPager();
        binding.viewPager.setCurrentItem(1);
        binding.viewPager.setCurrentItem(0);

        listFragment.setTabListener((quizItemPosition, fragmentIndex) -> {
            MyPagerHelper.setCurrentItem(binding.viewPager, fragmentIndex, 300);
            quizFragment.onListItemClick(quizItemPosition, this.quizList);
        });

        quizFragment.setAnswerListener((position, show) -> {
            if (show) {
                showCommitButton();
            } else {
                hideCommitButton(position);
            }
        });

        binding.toolbarLayout.tvCommit.setOnClickListener(view -> {
            quizFragment.judgeQuiz();
        });

        getRoundQuestion();
    }

    private void initViewPager() {
        binding.viewPager.setUserInputEnabled(false);
        binding.viewPager.setAdapter(new FragmentStateAdapter(this) {

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

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    binding.toolbarLayout.tvBackList.setVisibility(View.GONE);
                } else {
                    binding.toolbarLayout.tvBackList.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    public void getRoundQuestion() {
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.questionURL)
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
                        quizList = model.getData();
                        listFragment.setListData(quizList);
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
        int currentItem = binding.viewPager.getCurrentItem();
        if (currentItem == 0) {
            finish();
        } else {
            transToListFragment();
        }
    }

    private void transToListFragment() {
        MyPagerHelper.setCurrentItem(binding.viewPager, 0, 300);
    }

    public void showCommitButton() {
        if (binding.toolbarLayout.tvCommit.getVisibility() == View.GONE) {
            binding.toolbarLayout.tvCommit.setVisibility(View.VISIBLE);
        }
    }

    public void hideCommitButton(int questionIndex) {
        binding.toolbarLayout.tvCommit.setVisibility(View.GONE);
        if (questionIndex == quizList.size() - 1) {
            binding.toolbarLayout.tvCommit.setText("确定");
        } else {
            binding.toolbarLayout.tvCommit.setText("下一题");
        }
    }
}