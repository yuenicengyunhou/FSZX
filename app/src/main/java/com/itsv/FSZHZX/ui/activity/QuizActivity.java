package com.itsv.FSZHZX.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.base.MyBaseMvpActivity;
import com.itsv.FSZHZX.model.QuizModel;
import com.itsv.FSZHZX.presenter.QuizPre;
import com.itsv.FSZHZX.utils.ToastUtils;
import com.itsv.FSZHZX.view.QuizView;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

public class QuizActivity extends MyBaseMvpActivity<QuizActivity, QuizPre> implements QuizView {

    @BindView(R.id.tv_title)
    public TextView tvTitle;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.tv_A)
    RadioButton tvA;
    @BindView(R.id.tv_B)
    RadioButton tvB;
    @BindView(R.id.tv_C)
    RadioButton tvC;
    @BindView(R.id.tv_D)
    RadioButton tvD;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.tv_source)
    TextView tvSource;
    @BindView(R.id.tv_explaination)
    TextView tvExplaination;
    @BindView(R.id.answerLayout)
    LinearLayout answerLayout;
    @BindView(R.id.fullLayout)
    ScrollView fullLayout;
    @BindView(R.id.tv_correctNum)
    TextView tvCorrectTimes;
    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.tv_wrongCount)
    TextView tvWrongCount;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.doneView)
    RelativeLayout doneView;
    @BindString(R.string.quiz)
    String quiz;
    @BindColor(R.color.colorTextTrue)
    int green;
    @BindColor(R.color.colorTextFalse)
    int red;
    @BindColor(R.color.colorTextB)
    int black;
    @BindColor(R.color.white)
    int white;
    @BindDrawable(R.drawable.selector_wise)
    Drawable wise;
    @BindDrawable(R.drawable.selector_fool)
    Drawable fool;
    @BindDrawable(R.drawable.selector_quize)
    Drawable raw;
    private QuizPre presenter;

    private List<QuizModel.DataBean> quizList;
    private int questionIndex;
    private int score;
    private String questionId = "";
    private String hint;//提示
    private String hintImportantContent;//提示中需要标红的字段
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
    private String questionTypeId;


    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        presenter = new QuizPre();
        return presenter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_quiz;
    }

    @Override
    protected void initViewsAndEnvents() {
        Intent intent = getIntent();
        questionTypeId = intent.getStringExtra("id");
        initToolbar(toolbar, false);
        tvTitle.setText(quiz);
        tvCommit.setText("下一题");
        if (questionTypeId == null) {
            ToastUtils.showSingleToast("答题专项为空");
            finish();
            return;
        }
        presenter.getQuestionList(questionTypeId);
        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            userCheckedPosition = radioGroup.indexOfChild(radioGroup.findViewById(i));
            showCommitButton();
        });
        getStartTime();
    }


    @OnClick({R.id.iv_back, R.id.tv_commit, R.id.tv_viewTips, R.id.btn_back, R.id.btn_oneMore})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_commit:
                if (questionIndex < quizList.size()) {
                    if (isAnswerCorrect() || (answerLayout.getVisibility() == View.VISIBLE)) {
                        initUserData();
                        if (questionIndex == quizList.size() - 1) {
                            allQuizFinished();
                        } else {
                            questionIndex = questionIndex + 1;
                            presenter.getQuizInfor(quizList, questionIndex);
                        }
                    } else {
                        disableRadioGroup();
                        showExplaination();
                    }
                }
                break;

            case R.id.tv_viewTips:
                showTip(hint);
                break;
            case R.id.btn_oneMore:
                clearUp();
                break;
        }
    }

    private void initUserData() {
        questionIds.add(questionId);
        answers.add(presenter.getOption(userCheckedPosition));
        if (isAnswerCorrect()) {
            correctTimes = correctTimes + 1;
            totalScore = totalScore + score;
            scores.add(String.valueOf(score));
        } else {
            scores.add("0");
        }
    }

    @Override
    public boolean isAnswerCorrect() {
        int i = presenter.getIndexOfTrueOption(optionTrue);
        return i == userCheckedPosition;
    }

    @Override
    public void animOut() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide);
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

    @Override
    public void animIn() {
        Animation slidein = AnimationUtils.loadAnimation(QuizActivity.this, R.anim.quiz_slide_in);
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

    @Override
    public void showExplaination() {
        answerLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideExplaination() {
        answerLayout.setVisibility(View.GONE);
    }

    @Override
    public void showCommitButton() {
        if (tvCommit.getVisibility() == View.GONE) {
            tvCommit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideCommitButton() {
        tvCommit.setVisibility(View.GONE);
        if (questionIndex == quizList.size() - 1) {
            tvCommit.setText("确定");
        } else {
            tvCommit.setText("下一题");
        }
    }

    @Override
    public void clearChoiceCheck() {
        radioGroup.clearCheck();
    }

    @Override
    public void getAllQuiz(List<QuizModel.DataBean> list) {
        quizList = list;
        tvCount.setText(MessageFormat.format("/{0}", list.size()));
    }

    @Override
    public void initQuiz(String questionTitle, String optionA, String optionB, String optionC, String optionD, String questionSource, String explainContent, String optionTrue, String hintContent, int score, String id, String hintImportantContent) {
        this.score = score;
        this.questionId = id;
        if (questionIndex != 0) {
            animOut();
            enableRadioGroup();
            clearChoiceCheck();
        }
        hideCommitButton();
        hideExplaination();
        tvNumber.setText(String.valueOf(questionIndex + 1));
        tvQuestion.setText(questionTitle);
        tvA.setText(MessageFormat.format("A.  {0}", optionA));
        tvB.setText(MessageFormat.format("B.  {0}", optionB));
        tvC.setText(MessageFormat.format("C.  {0}", optionC));
        tvD.setText(MessageFormat.format("D.  {0}", optionD));
        tvSource.setText(questionSource);
        tvExplaination.setText(explainContent);
//        hint = hintContent;
        this.hintImportantContent = hintImportantContent;
        hint = makeRedHint(hintContent, hintImportantContent);
        this.optionTrue = optionTrue;
    }

    /**
     * 生成标红格式的hint
     */
    private String makeRedHint(String hintContent, String hintImportantContent) {
        if (hintContent.contains(hintImportantContent)) {
            String replaceWord = "<font color=\"#b21636\">" + hintImportantContent + "</font>";
            return hintContent.replace(hintImportantContent, replaceWord);
        }
        return hintContent;
    }

    //    private int indexOfTrueOption;
    @Override
    public void disableRadioGroup() {
        int indexOfTrueOption = presenter.getIndexOfTrueOption(optionTrue);
        RadioButton radioButton = (RadioButton) radioGroup.getChildAt(indexOfTrueOption);
        radioButton.setTextColor(green);
        RadioButton rb = (RadioButton) radioGroup.getChildAt(userCheckedPosition);
        rb.setTextColor(red);
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton button = (RadioButton) radioGroup.getChildAt(i);
            button.setEnabled(false);
        }
//        radioGroup.check(userCheckedPosition);

    }

    @Override
    public void enableRadioGroup() {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            radioButton.setEnabled(true);
            radioButton.setTextColor(black);
        }
    }

    @Override
    public void allQuizFinished() {
        hideCommitButton();
        showSummery();
        tvCorrectTimes.setText(String.valueOf(correctTimes));
        tvRate.setText(MessageFormat.format("正确率：{0}%", correctTimes * 100 / quizList.size()));
        tvDuration.setText(MessageFormat.format("用时：{0}", presenter.generateTime(getDuration())));
        tvWrongCount.setText(MessageFormat.format("错题数：{0}", quizList.size() - correctTimes));
        tvScore.setText(MessageFormat.format("积分：+{0}", totalScore));
        presenter.answerOnline(Constant.TOKEN, startTime, questionIds, answers, scores, String.valueOf(getDuration()), String.valueOf(totalScore));
    }

    @Override
    public void showSummery() {
        doneView.setVisibility(View.VISIBLE);
        fullLayout.setVisibility(View.GONE);
    }

    @Override
    public void showQuestionView() {
        doneView.setVisibility(View.GONE);
        fullLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTip(String hint) {
        @SuppressLint("InflateParams") View contentView = LayoutInflater.from(this).inflate(R.layout.pop_quiz_tips, null);
        showPopWindow(contentView);
    }

    @Override
    protected void initPopEvents(View contentView, PopupWindow popupWindow) {
        TextView tvDiss = contentView.findViewById(R.id.tv_dismiss);
        tvDiss.setOnClickListener(view -> popupWindow.dismiss());
        TextView tvTips = contentView.findViewById(R.id.tv_tips);
        tvTips.setText(Html.fromHtml(hint));
    }

    @Override
    public void showErrorToast(String s) {
        super.showErrorToast(s);
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

    @Override
    public void clearUp() {
        quizList.clear();
        questionIndex = 0;
        score = 0;
        questionId = "";
        hint = "";
        optionTrue = "";
        userCheckedPosition = -1;
        getStartTime();
        startLong = System.currentTimeMillis();
        totalScore = 0;
        correctTimes = 0;
        questionIds.clear();
        answers.clear();
        scores.clear();
        showQuestionView();
        enableRadioGroup();
        hideExplaination();
        clearChoiceCheck();
        presenter.getQuestionList(questionTypeId);
    }

}
