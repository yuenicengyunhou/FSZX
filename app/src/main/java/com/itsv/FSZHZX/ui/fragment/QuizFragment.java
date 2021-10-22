package com.itsv.FSZHZX.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.model.QuizModel;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class QuizFragment extends Fragment {
    TextView tvTitle;
    TextView tvCommit;
    TextView tvNumber;
    TextView tvCount;
    TextView tvQuestion;
    RadioButton tvA;
    RadioButton tvB;
    RadioButton tvC;
    RadioButton tvD;
    RadioGroup radioGroup;
    TextView tvSource;
    TextView tvExplaination;
    LinearLayout answerLayout;
    ScrollView fullLayout;
    TextView tvCorrectTimes;
    TextView tvRate;
    TextView tvDuration;
    TextView tvWrongCount;
    TextView tvScore;
    RelativeLayout doneView;
//    @BindString(R.string.quiz)
//    String quiz;
//    @BindColor(R.color.colorTextTrue)
//    int green;
//    @BindColor(R.color.colorTextFalse)
//    int red;
//    @BindColor(R.color.colorTextB)
//    int black;
//    @BindColor(R.color.white)
//    int white;
//    @BindDrawable(R.drawable.selector_wise)
//    Drawable wise;
//    @BindDrawable(R.drawable.selector_fool)
//    Drawable fool;
//    @BindDrawable(R.drawable.selector_quize)
//    Drawable raw;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        quizList = new ArrayList<>();
        initViews(view);
    }

    private void initViews(View view) {
        tvTitle = view.findViewById(R.id.tv_title);
        tvCommit = view.findViewById(R.id.tv_commit);
        tvNumber = view.findViewById(R.id.tv_number);

        tvCount = view.findViewById(R.id.tv_count);
        tvQuestion = view.findViewById(R.id.tv_question);
        tvA = view.findViewById(R.id.tv_A);
        tvB = view.findViewById(R.id.tv_B);
        tvC = view.findViewById(R.id.tv_C);
        tvD = view.findViewById(R.id.tv_D);

        radioGroup = view.findViewById(R.id.radioGroup);
        tvSource = view.findViewById(R.id.tv_source);
        tvExplaination = view.findViewById(R.id.tv_explaination);
        answerLayout = view.findViewById(R.id.answerLayout);
        fullLayout = view.findViewById(R.id.fullLayout);

        tvCorrectTimes = view.findViewById(R.id.tv_correctNum);
        tvRate = view.findViewById(R.id.tv_rate);
        tvDuration = view.findViewById(R.id.tv_duration);
        tvWrongCount = view.findViewById(R.id.tv_wrongCount);
        tvScore = view.findViewById(R.id.tv_score);
        doneView = view.findViewById(R.id.doneView);

    }

    public void onListItemClick(int position,List<QuizModel.DataBean> list) {
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
            initQuiz(questionTitle, optionA, optionB, optionC, optionD, questionSource, explainContent, optionTrue, hintContent, score, id);
        }
    }

    private void initQuiz(String questionTitle, String optionA, String optionB, String optionC, String optionD, String questionSource, String explainContent, String optionTrue, String hintContent, int score, String id) {
        this.score = score;
        this.questionId = id;
        if (questionIndex != 0) {
            animOut();
            enableRadioGroup();
            clearChoiceCheck();
        }
//        if (null == tvNumber) {
//            Log.e("WQ", "hahhha");
//        }
//        hideCommitButton();
//        hideExplaination();
        tvNumber.setText(String.valueOf(questionIndex + 1));
        tvQuestion.setText(questionTitle);
        tvA.setText(MessageFormat.format("A.  {0}", optionA));
        tvB.setText(MessageFormat.format("B.  {0}", optionB));
        tvC.setText(MessageFormat.format("C.  {0}", optionC));
        tvD.setText(MessageFormat.format("D.  {0}", optionD));
        tvSource.setText(questionSource);
        tvExplaination.setText(explainContent);
        hint = hintContent;
        this.optionTrue = optionTrue;
    }

    private void animOut() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide);
        fullLayout.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fullLayout.setVisibility(View.GONE);
                animIn();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    public void animIn() {
        Animation slidein = AnimationUtils.loadAnimation(getContext(), R.anim.quiz_slide_in);
        fullLayout.startAnimation(slidein);
        slidein.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                fullLayout.setVisibility(View.VISIBLE);
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
        answerLayout.setVisibility(View.VISIBLE);
    }

    public void hideExplaination() {
        answerLayout.setVisibility(View.GONE);
    }

    public void showCommitButton() {
        if (tvCommit.getVisibility() == View.GONE) {
            tvCommit.setVisibility(View.VISIBLE);
        }
    }

    public void hideCommitButton() {
        tvCommit.setVisibility(View.GONE);
        if (questionIndex == quizList.size() - 1) {
            tvCommit.setText("确定");
        } else {
            tvCommit.setText("下一题");
        }
    }
    public void enableRadioGroup() {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            radioButton.setEnabled(true);
            radioButton.setTextColor(getResources().getColor(R.color.black));
        }
    }

    public void clearChoiceCheck() {
        radioGroup.clearCheck();
    }

}
