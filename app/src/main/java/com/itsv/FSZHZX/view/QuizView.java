package com.itsv.FSZHZX.view;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.itsv.FSZHZX.model.QuizModel;

import java.util.List;

public interface QuizView extends MvpView {

    void animOut();

    void animIn();

    void hideExplaination();

    void showExplaination();

    void showCommitButton();

    void hideCommitButton();

    void getAllQuiz(List<QuizModel.DataBean> list);

    void initQuiz(String questionTitle, String optionA, String optionB, String optionC, String optionD, String questionSource,
                  String explainContent, String optionTrue, String hintContent,int score,String id,String hintImportantContent);


    boolean isAnswerCorrect();

    void disableRadioGroup();

    void enableRadioGroup();


    void clearChoiceCheck();

    void allQuizFinished();

    void showTip(String hint);

    void showSummery();

    void showQuestionView();

    void clearUp();


}
