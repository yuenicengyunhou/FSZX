package com.itsv.FSZHZX.api;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface QuestionApi {
    @POST("questionType/getAllQuestionType")
    Call<ResponseBody> getQuestionTypes(@QueryMap HashMap<String, Object> map);

    @POST("questionBank/getListQuestion")
    Call<ResponseBody> getQuestionList(@QueryMap HashMap<String, Object> map);
}
