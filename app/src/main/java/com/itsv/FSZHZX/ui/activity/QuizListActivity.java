package com.itsv.FSZHZX.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.gson.Gson;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.api.UserApi;
import com.itsv.FSZHZX.base.ApiHelper;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.model.QuizModel;
import com.itsv.FSZHZX.utils.ToastUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);
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
                        if (model != null) {
                            String msg = model.getMsg();
                            int code = model.getCode();
                            if (code != 200) {
                                ToastUtils.showSingleToast("错误");
                                return;
                            }
                            List<QuizModel.DataBean> data = model.getData();
                            if (data.isEmpty()) return;
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
}