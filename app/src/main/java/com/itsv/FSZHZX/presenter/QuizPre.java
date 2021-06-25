package com.itsv.FSZHZX.presenter;


import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.itsv.FSZHZX.api.QuestionApi;
import com.itsv.FSZHZX.api.UserApi;
import com.itsv.FSZHZX.base.ApiHelper;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.model.QuizModel;
import com.itsv.FSZHZX.ui.activity.QuizActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizPre implements MvpPresenter<QuizActivity> {
    private QuizActivity mvpView;
    private List<String> choices;

    @Override
    public void attachView(QuizActivity view) {
        this.mvpView = view;
        choices = new ArrayList<>();
        choices.add("A");
        choices.add("B");
        choices.add("C");
        choices.add("D");
    }

    @Override
    public void detachView(boolean retainInstance) {

    }

    public void getRoundQuestion() {
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.BASEURL)
                .createService(UserApi.class);
        Call<ResponseBody> call = api.getRoundQuestion(Constant.TOKEN);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (null == mvpView.tvTitle) return;
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        Gson gson = new Gson();
                        QuizModel model = gson.fromJson(params, QuizModel.class);
                        if (model != null) {
                            String msg = model.getMsg();
                            int code = model.getCode();
                            if (code != 200) {
                                mvpView.showErrorToast(msg);
                                return;
                            }
                            List<QuizModel.DataBean> data = model.getData();
                            if (data.isEmpty()) return;
                            mvpView.getAllQuiz(data);
                            getQuizInfor(data, 0);
                        }
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

    public void getQuestionList(String id) {
        QuestionApi api = ApiHelper.getInstance().buildRetrofit(Constant.BASEURL)
                .createService(QuestionApi.class);
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", Constant.TOKEN);
        map.put("questionType", id);
        Call<ResponseBody> call = api.getQuestionList(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (null == mvpView.tvTitle) return;
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();
                        Gson gson = new Gson();
                        QuizModel model = gson.fromJson(params, QuizModel.class);
                        if (model != null) {
                            String msg = model.getMsg();
                            int code = model.getCode();
                            if (code != 200) {
                                mvpView.showErrorToast(msg);
                                return;
                            }
                            List<QuizModel.DataBean> data = model.getData();
                            if (data.isEmpty()) return;
                            mvpView.getAllQuiz(data);
                            getQuizInfor(data, 0);
                        }
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
        String hintImportantContent = dataBean.getHintImportantContent();
        int score = dataBean.getScore();

        mvpView.initQuiz(questionTitle, optionA, optionB, optionC, optionD, questionSource, explainContent, optionTrue, hintContent, score, id,hintImportantContent);
    }

    public int getIndexOfTrueOption(String optionTrue) {
        return choices.indexOf(optionTrue);
    }

    public String getOption(int i) {
        return choices.get(i);
    }


    public void answerOnline(String token, String startTime, List<String> questionIds, List<String> answers, List<String> scores, String duration, String totalScore) {
        UserApi api = ApiHelper.getInstance().buildRetrofit(Constant.BASEURL).createService(UserApi.class);
        Call<ResponseBody> call = api.answerOnline(token, startTime, listToString(questionIds), listToString(answers), listToString(scores), duration, totalScore);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (null == mvpView.tvTitle) return;
                if (response.isSuccessful()) {
                    try {
                        String params = response.body().string();

                        JSONObject object = new JSONObject(params);
                        boolean success = object.getBoolean("success");
                        if (!success) {
                            mvpView.showErrorToast("答题结果提交服务器失败");
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    mvpView.showErrorToast("答题结果提交服务器失败");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                mvpView.showErrorToast("答题结果提交服务器失败");
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

}
