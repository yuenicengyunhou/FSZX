package com.itsv.FSZHZX.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.databinding.FragmentQuizBinding;
import com.itsv.FSZHZX.listener.QuizAnswerListener;
import com.itsv.FSZHZX.model.QuizModel;
import com.itsv.FSZHZX.utils.DesignUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class QuizFragment extends Fragment {

    private List<QuizModel.DataBean> quizList;

    //记录用户数据
    private int questionIndex;
    private int score;
    private String questionId = "";
    private String hint;
    private String optionTrue;

    private int userCheckedPosition;
    private String startTime;
    private long startLong;
    private int totalScore;
    private int correctTimes;

    /*记录的用户数据*/
    private List<String> questionIds = new ArrayList<>();
    private List<String> answers = new ArrayList<>();
    private List<String> scores = new ArrayList<>();
    private FragmentQuizBinding binding;
    private QuizAnswerListener answerListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        quizList = new ArrayList<>();
        init();
    }

    private void init() {
        choices = new ArrayList<>();
        choices.add("A");
        choices.add("B");
        choices.add("C");
        choices.add("D");

        binding.radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            userCheckedPosition = radioGroup.indexOfChild(radioGroup.findViewById(i));
            if (null != answerListener) {
                answerListener.answer(questionIndex, true);
            }
        });

        binding.tvViewTips.setOnClickListener(view -> {
            showTip(hint);
        });
    }

    public void onListItemClick(int position, List<QuizModel.DataBean> list) {
        questionIndex = position;
        quizList = list;
        if (null != quizList && !quizList.isEmpty() && position < quizList.size()) {
            QuizModel.DataBean dataBean = quizList.get(position);
            String questionTitle = dataBean.getQuestionTitle();
            String optionA = dataBean.getOptionA();
            String optionB = dataBean.getOptionB();
            String optionC = dataBean.getOptionC();
            String optionD = dataBean.getOptionD();
            String questionSource = dataBean.getQuestionSource();
            String explainContent = dataBean.getExplainContent();
            String optionTrue = dataBean.getOptionTrue();
            String hintContent = dataBean.getHintContent();
            String id = dataBean.getId();
            int score = dataBean.getScore();
            initQuiz(questionTitle, optionA, optionB, optionC, optionD, questionSource, explainContent, optionTrue, hintContent, score, id, false);
        }
    }

    private void initQuiz(String questionTitle, String optionA, String optionB, String optionC, String optionD, String questionSource, String explainContent, String optionTrue, String hintContent, int score, String id, boolean anime) {
        this.score = score;
        this.questionId = id;
        if (questionIndex != 0 && anime) {
            animOut();
            enableRadioGroup();
            clearChoiceCheck();
        }
        if (null != answerListener) {
            answerListener.answer(questionIndex, false);
        }
//        hideCommitButton();
        hideExplaination();
        binding.tvNumber.setText(String.valueOf(questionIndex + 1));
        binding.tvQuestion.setText(questionTitle);
        binding.tvA.setText(MessageFormat.format("A.  {0}", optionA));
        binding.tvB.setText(MessageFormat.format("B.  {0}", optionB));
        binding.tvC.setText(MessageFormat.format("C.  {0}", optionC));
        binding.tvD.setText(MessageFormat.format("D.  {0}", optionD));
        binding.tvSource.setText(questionSource);
        binding.tvExplaination.setText(explainContent);
        binding.tvCount.setText(MessageFormat.format("/{0}", quizList.size()));
        hint = hintContent;
        this.optionTrue = optionTrue;
    }

    private void animOut() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide);
        binding.fullLayout.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.fullLayout.setVisibility(View.GONE);
                animIn();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void animIn() {
        Animation slidein = AnimationUtils.loadAnimation(getContext(), R.anim.quiz_slide_in);
        binding.fullLayout.startAnimation(slidein);
        slidein.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.fullLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void showExplaination() {
        binding.answerLayout.setVisibility(View.VISIBLE);
    }

    public void hideExplaination() {
        binding.answerLayout.setVisibility(View.GONE);
    }


    public void enableRadioGroup() {
        for (int i = 0; i < binding.radioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) binding.radioGroup.getChildAt(i);
            radioButton.setEnabled(true);
            radioButton.setTextColor(getResources().getColor(R.color.black));
        }
    }

    public void clearChoiceCheck() {
        binding.radioGroup.clearCheck();
    }

    public void setAnswerListener(QuizAnswerListener answerListener) {
        this.answerListener = answerListener;
    }

    protected void showPopWindow(View contentView) {
        PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                DesignUtils.getScreenHeight(getContext()) / 2);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        initPopEvents(contentView, popupWindow);
        popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
    }


    public void showTip(String hint) {
        @SuppressLint("InflateParams") View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_quiz_tips, null);
        showPopWindow(contentView);
    }

    protected void initPopEvents(View contentView, PopupWindow popupWindow) {
        TextView tvDiss = contentView.findViewById(R.id.tv_dismiss);
        tvDiss.setOnClickListener(view -> popupWindow.dismiss());
        TextView tvTips = contentView.findViewById(R.id.tv_tips);
        tvTips.setText(hint);
    }

    public void judgeQuiz() {
        if (questionIndex < quizList.size()) {
            if (isAnswerCorrect() || (binding.answerLayout.getVisibility() == View.VISIBLE)) {
                initUserData();
                if (questionIndex == quizList.size() - 1) {
                    allQuizFinished();
                } else {
                    questionIndex = questionIndex + 1;
                    getQuizInfor(quizList, questionIndex);
                }
            } else {
                disableRadioGroup();
                showExplaination();
            }
        }
    }
    public void getQuizInfor(List<QuizModel.DataBean> list, int position) {
        if (list == null || list.isEmpty()) return;
        QuizModel.DataBean dataBean = list.get(position);
        String questionTitle = dataBean.getQuestionTitle();
        String optionA = dataBean.getOptionA();
        String optionB = dataBean.getOptionB();
        String optionC = dataBean.getOptionC();
        String optionD = dataBean.getOptionD();
        String questionSource = dataBean.getQuestionSource();
        String explainContent = dataBean.getExplainContent();
        String optionTrue = dataBean.getOptionTrue();
        String hintContent = dataBean.getHintContent();
        String id = dataBean.getId();
        int score = dataBean.getScore();

        initQuiz(questionTitle, optionA, optionB, optionC, optionD, questionSource, explainContent, optionTrue, hintContent, score, id,true);
    }
    public void disableRadioGroup() {
        int indexOfTrueOption = getIndexOfTrueOption(optionTrue);
        RadioButton radioButton = (RadioButton) binding.radioGroup.getChildAt(indexOfTrueOption);
        radioButton.setTextColor(getResources().getColor(R.color.colorTextTrue));
        RadioButton rb = (RadioButton) binding.radioGroup.getChildAt(userCheckedPosition);
        rb.setTextColor(getResources().getColor(R.color.colorTextFalse));
        for (int i = 0; i < binding.radioGroup.getChildCount(); i++) {
            RadioButton button = (RadioButton) binding.radioGroup.getChildAt(i);
            button.setEnabled(false);
        }
        Log.e("WQ", "==" + userCheckedPosition);
//        radioGroup.check(userCheckedPosition);

    }

    private List<String> choices;

    public int getIndexOfTrueOption(String optionTrue) {
        return choices.indexOf(optionTrue);
    }

    public boolean isAnswerCorrect() {
        int i = getIndexOfTrueOption(optionTrue);
        return i == userCheckedPosition;
    }

    private void initUserData() {
        questionIds.add(questionId);
        answers.add(getOption(userCheckedPosition));
        if (isAnswerCorrect()) {
            correctTimes = correctTimes + 1;
            totalScore = totalScore + score;
            scores.add(String.valueOf(score));
        } else {
            scores.add("0");
        }
    }

    public String getOption(int i) {
        return choices.get(i);
    }

    public void allQuizFinished() {
        if (null != answerListener) {
            answerListener.answer(questionIndex,false);
        }
        showSummery();
        binding.doneView.tvCorrectNum.setText(String.valueOf(correctTimes));
        binding.doneView.tvRate.setText(MessageFormat.format("正确率：{0}%", correctTimes * 100 / quizList.size()));
//        binding.doneView.tvDuration.setText(MessageFormat.format("用时：{0}", presenter.generateTime(getDuration())));
        binding.doneView.tvWrongCount.setText(MessageFormat.format("错题数：{0}", quizList.size() - correctTimes));
        binding.doneView.tvScore.setText(MessageFormat.format("积分：+{0}", totalScore));
//        presenter.answerOnline(Constant.TOKEN, startTime, questionIds, answers, scores, String.valueOf(getDuration()), String.valueOf(totalScore));
    }
    public void showSummery() {
        binding.doneView.getRoot().setVisibility(View.VISIBLE);
        binding.fullLayout.setVisibility(View.GONE);
    }

}
