package com.itsv.FSZHZX.ui.activity;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.api.UserApi;
import com.itsv.FSZHZX.base.ApiHelper;
import com.itsv.FSZHZX.base.BaseAppCompatActivity;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.databinding.ActivityNewQuizBinding;
import com.itsv.FSZHZX.databinding.LayoutQuizBinding;
import com.itsv.FSZHZX.model.QuizModel;
import com.itsv.FSZHZX.ui.adapter.QuizListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewQuizActivity extends BaseAppCompatActivity {

    private List<QuizModel.DataBean> quizList = new ArrayList<>();
    private ActivityNewQuizBinding binding;
    private QuizListAdapter adapter;
    //    private String[] choices = new String[]{"A", "B", "C", "D"};
    private List<String> choices = new ArrayList<>();
    private int green ;
    private int red ;

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
        binding.toolbarLayout.tvCommit.setText("确定");
        binding.toolbarLayout.tvCommit.setVisibility(View.GONE);

//        binding.toolbarLayout.tvCommit.setOnClickListener(view -> {
//
//        });
        green = ContextCompat.getColor(this, R.color.colorTextTrue);
        red = ContextCompat.getColor(this, R.color.colorTextFalse);
        choices.add("A");
        choices.add("B");
        choices.add("C");
        choices.add("D");
        intList();

        getRoundQuestion();
    }

    private void intList() {
        binding.recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new QuizListAdapter(this, quizList);
        binding.recycler.setAdapter(adapter);
        adapter.setListener((position, bean) -> {
            showQuizPop(bean, position);
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
                        adapter.update(quizList);
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

    int userCheckPosition;

    private void showQuizPop(QuizModel.DataBean bean, int index) {

//        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.layout_quiz, null);
        LayoutQuizBinding binding = LayoutQuizBinding.inflate(getLayoutInflater());
        PopupWindow popupWindow = new PopupWindow(binding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        binding.tvQuestion.setText(bean.getQuestionTitle());
        binding.tvSource.setText(bean.getQuestionSource());
        binding.tvA.setText(bean.getOptionA());
        binding.tvB.setText(bean.getOptionB());
        binding.tvC.setText(bean.getOptionC());
        binding.tvD.setText(bean.getOptionD());
        binding.tvNumber.setText(String.format("%s/", (index + 1)));
        binding.tvCount.setText(String.valueOf(quizList.size()));
        binding.tvExplaination.setText(bean.getExplainContent());
        binding.radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            binding.tvVerify.setVisibility(View.VISIBLE);
            userCheckPosition = binding.radioGroup.indexOfChild(radioGroup.findViewById(i));
            Log.e("WQ", "用户选择答案==" + userCheckPosition);
        });
        binding.tvVerify.setOnClickListener(view -> {
            if (binding.tvVerify.getText().equals("关闭")) {
                popupWindow.dismiss();
            } else {
                verifyUserAnswer(index, userCheckPosition, bean, binding, popupWindow);
            }
        });
        setBackgroundAlpha(0.5f);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(() -> setBackgroundAlpha(1.0f));
        popupWindow.showAtLocation(this.binding.getRoot(), Gravity.BOTTOM, 0, 0);
    }

    private void verifyUserAnswer(int quizIndex, int userCheckPosition, QuizModel.DataBean bean, LayoutQuizBinding binding, PopupWindow popupWindow) {
        String optionTrue = bean.getOptionTrue();
        Log.e("WQ", "正确选项" + optionTrue);
        String choice = choices.get(userCheckPosition);
        if (choice.equals(optionTrue)) {
            popupWindow.dismiss();
        } else {
            binding.answerLayout.setVisibility(View.VISIBLE);
            disableRadioGroup(binding, choices.indexOf(optionTrue));
        }
        quizList.get(quizIndex).setAnswered(true);
    }

    public void disableRadioGroup(LayoutQuizBinding binding, int indexOfTrueOption) {
        binding.tvVerify.setText("关闭");
//        int indexOfTrueOption = presenter.getIndexOfTrueOption(optionTrue);
        RadioButton radioButton = (RadioButton) binding.radioGroup.getChildAt(indexOfTrueOption);

        radioButton.setTextColor(green);
        RadioButton rb = (RadioButton) binding.radioGroup.getChildAt(userCheckPosition);
        rb.setTextColor(red);
        for (int i = 0; i < binding.radioGroup.getChildCount(); i++) {
            RadioButton button = (RadioButton) binding.radioGroup.getChildAt(i);
            button.setEnabled(false);
        }
//        Log.e("WQ", "==" + userCheckedPosition);
//        radioGroup.check(userCheckedPosition);

    }


    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }
}