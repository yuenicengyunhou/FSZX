package com.itsv.FSZHZX.ui.activity;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.api.UserApi;
import com.itsv.FSZHZX.base.ApiHelper;
import com.itsv.FSZHZX.base.BaseAppCompatActivity;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.databinding.ActivityNewQuizBinding;
import com.itsv.FSZHZX.databinding.LayoutQuizBinding;
import com.itsv.FSZHZX.model.QuizModel;
import com.itsv.FSZHZX.model.QuizResult;
import com.itsv.FSZHZX.ui.adapter.QuizListAdapter;
import com.itsv.FSZHZX.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private final List<String> choices = new ArrayList<>();
    private int green;
    private int red;
    private int userCheckPosition;//用户选中位置

    private String startTime;
    private long startLong;
    private int totalScore;
    private int correctTimes;
    /*记录的用户数据*/
    private List<String> questionIds = new ArrayList<>();
    private List<String> answers = new ArrayList<>();
    private List<String> scores = new ArrayList<>();
    private List<QuizResult> resultList;

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
        binding.toolbarLayout.tvCommit.setVisibility(View.GONE);
        binding.toolbarLayout.ivBack.setOnClickListener(v -> finish());
        getStartTime();
        resultList = new ArrayList<>();
        green = ContextCompat.getColor(this, R.color.colorTextTrue);
        red = ContextCompat.getColor(this, R.color.colorTextFalse);
        choices.add("A");
        choices.add("B");
        choices.add("C");
        choices.add("D");
        intList();

        binding.btnOneMore.setOnClickListener(view -> {
            clearUp();
        });

        binding.btnBack.setOnClickListener(view -> finish());

        getRoundQuestion();
    }

    private void getStartTime() {
        startLong = System.currentTimeMillis();
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startTime = dateFormat.format(date);
    }

    private int getDuration() {
        return (int) (System.currentTimeMillis() - startLong);
    }

    private void commitAnswers() {
        for (int i = 0; i < resultList.size(); i++) {
            QuizResult quizResult = resultList.get(i);
            String questionId = quizResult.getQuestionId();
            String score = quizResult.getScore();
            String userAnswer = quizResult.getUserAnswer();
            answers.add(userAnswer);
            questionIds.add(questionId);
            scores.add(score);
        }
        answerOnline(Constant.TOKEN, startTime, questionIds, answers, scores, String.valueOf(getDuration()), String.valueOf(totalScore));
    }
    //a d a a c b a b d c

    public void answerOnline(String token, String startTime, List<String> questionIds, List<String> answers, List<String> scores, String duration, String totalScore) {
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.BASEURL).createService(UserApi.class);
        Call<ResponseBody> call = api.answerOnline(token, startTime, listToString(questionIds), listToString(answers), listToString(scores), duration, totalScore);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();

                        JSONObject object = new JSONObject(params);
                        boolean success = object.getBoolean("success");
                        if (!success) {
                            ToastUtils.showSingleToast("答题结果提交服务器失败");
                        } else {
                            ToastUtils.showSingleToast("答案已自动提交");
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ToastUtils.showSingleToast("答题结果提交服务器失败");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                ToastUtils.showSingleToast("答题结果提交服务器失败");
            }
        });
    }

    private String listToString(List<String> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(";"); // 分隔符
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

    public String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return formatt(hours) + ":" + formatt(minutes) + ":" + formatt(seconds);
    }

    private String formatt(int i) {
        String t = "";
        if (i < 10) {
            return "0" + i;
        } else {
            return String.valueOf(i);
        }
    }

    private void intList() {
        binding.recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new QuizListAdapter(this, quizList);
        binding.recycler.setAdapter(adapter);
        adapter.setListener((position, bean) -> showQuizPop(bean, position));
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
                        quizList = model.getData();
                        setList();
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

    private void setList() {
        resultList.clear();
        for (int i = 0; i < quizList.size(); i++) {
            resultList.add(new QuizResult());
        }
    }


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
        binding.tvViewTips.setOnClickListener(view -> {
//          binding.tvExplaination.setVisibility(View.VISIBLE);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(bean.getHintContent());
            builder.setPositiveButton("确定", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            builder.show();
        });
        binding.radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            binding.tvVerify.setVisibility(View.VISIBLE);
            userCheckPosition = binding.radioGroup.indexOfChild(radioGroup.findViewById(i));
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
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.setOnDismissListener(() -> {
            setBackgroundAlpha(1.0f);
            Log.e("WQ", "listen==" + showCommitButton());
            if (showCommitButton()) {
                quizDone();
            }

//            this.binding.toolbarLayout.tvCommit.setVisibility(questionIds.size() == quizList.size() ? View.VISIBLE : View.GONE);
        });
        popupWindow.showAtLocation(this.binding.getRoot(), Gravity.BOTTOM, 0, 0);
    }

    private void quizDone() {
        this.binding.recycler.setVisibility(View.GONE);
        this.binding.doneView.setVisibility(View.VISIBLE);
        this.binding.tvCorrectNum.setText(String.valueOf(correctTimes));
        this.binding.tvRate.setText(MessageFormat.format("正确率：{0}%", correctTimes * 100 / quizList.size()));
        this.binding.tvDuration.setText(MessageFormat.format("用时：{0}", generateTime(getDuration())));
        this.binding.tvWrongCount.setText(MessageFormat.format("错题数：{0}", quizList.size() - correctTimes));
        this.binding.tvScore.setText(MessageFormat.format("积分：+{0}", totalScore));
        commitAnswers();
//        answerOnline(Constant.TOKEN, startTime, questionIds, answers, scores, String.valueOf(getDuration()), String.valueOf(totalScore));
    }

    //a b c b b ca a b
    private boolean showCommitButton() {
        int temp = 0;
        for (int i = 0; i < quizList.size(); i++) {
            QuizModel.DataBean dataBean = quizList.get(i);
            if (dataBean.isAnswered()) {
                temp = temp + 1;
            }
        }
        return temp == quizList.size();
    }

    private void verifyUserAnswer(int quizIndex, int userCheckPosition, QuizModel.DataBean bean, LayoutQuizBinding binding, PopupWindow popupWindow) {
        String optionTrue = bean.getOptionTrue();
        String choice = choices.get(userCheckPosition);
        int score = bean.getScore();
        String id = bean.getId();
        String userScore;
        quizList.get(quizIndex).setAnswered(true);
        if (choice.equals(optionTrue)) {
            correctTimes = correctTimes + 1;
            userScore = "1";
            totalScore = totalScore + score;
            quizList.get(quizIndex).setCorrect(true);
            popupWindow.dismiss();
        } else {
            userScore = "0";
            quizList.get(quizIndex).setCorrect(false);
            binding.answerLayout.setVisibility(View.VISIBLE);
            disableRadioGroup(binding, choices.indexOf(optionTrue));
        }
        saveUserData(quizIndex, choice, id, userScore);
        adapter.update(quizList);
    }
    //a c d d b  b b  b c a

    private void saveUserData(int quizIndex, String userAnswer, String quizId, String score) {
        Log.e("WQ", "position===" + quizIndex + "   userAnswer---" + userAnswer);
        QuizResult quizResult = resultList.get(quizIndex);
        quizResult.setQuestionId(quizId);
        quizResult.setScore(score);
        quizResult.setUserAnswer(userAnswer);
    }

    public void disableRadioGroup(LayoutQuizBinding binding, int indexOfTrueOption) {
        binding.tvVerify.setText("关闭");
        RadioButton radioButton = (RadioButton) binding.radioGroup.getChildAt(indexOfTrueOption);

        radioButton.setTextColor(green);
        RadioButton rb = (RadioButton) binding.radioGroup.getChildAt(userCheckPosition);
        rb.setTextColor(red);
        for (int i = 0; i < binding.radioGroup.getChildCount(); i++) {
            RadioButton button = (RadioButton) binding.radioGroup.getChildAt(i);
            button.setEnabled(false);
        }
    }


    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    public void clearUp() {
        binding.doneView.setVisibility(View.GONE);
        binding.recycler.setVisibility(View.VISIBLE);
        quizList.clear();
        getStartTime();
        startLong = System.currentTimeMillis();
        totalScore = 0;
        correctTimes = 0;
        questionIds.clear();
        answers.clear();
        scores.clear();
        getRoundQuestion();
    }
}