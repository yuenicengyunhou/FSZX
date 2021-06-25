package com.itsv.FSZHZX.ui.activity;


import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.api.QuestionApi;
import com.itsv.FSZHZX.base.ApiHelper;
import com.itsv.FSZHZX.base.BaseAppCompatActivity;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.databinding.ActivitySortQuizBinding;
import com.itsv.FSZHZX.model.HttpResult;
import com.itsv.FSZHZX.model.QuestionSort;
import com.itsv.FSZHZX.ui.adapter.QuizSortAdapter;
import com.itsv.FSZHZX.utils.ToastUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SortQuizActivity extends BaseAppCompatActivity {
    private ActivitySortQuizBinding binding;
    private ArrayList<QuestionSort> beans;
    private QuizSortAdapter adapter;


    @Override
    protected View getLayoutView() {
        binding = ActivitySortQuizBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected int getLayoutID() {
        return 0;
    }

    @Override
    protected void initViewsAndEnvents() {
        beans = new ArrayList<>();
        initToolbar(binding.toolbarLayout.toolbar, false);
        binding.toolbarLayout.tvTitle.setText("在线答题");
        binding.toolbarLayout.ivBack.setOnClickListener(v -> finish());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recycler.setLayoutManager(layoutManager);
        adapter = new QuizSortAdapter(beans, this);
        adapter.setOnItemClickListener(this::toQuizActivity);
        binding.recycler.setAdapter(adapter);
        binding.swipe.setOnRefreshListener(this::getQuestionTypes);
        binding.swipe.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        getQuestionTypes();
    }

    private void toQuizActivity(String id) {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }


    private void getQuestionTypes() {
        binding.swipe.setRefreshing(true);
        QuestionApi api = ApiHelper.getInstance().buildRetrofit(Constant.BASEURL).createService(QuestionApi.class);
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", Constant.TOKEN);
        Call<ResponseBody> call = api.getQuestionTypes(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.swipe.setRefreshing(false);
                try {
                    String string = response.body().string();
                    Gson gson = new Gson();
                    Type type = new TypeToken<HttpResult<ArrayList<QuestionSort>>>() {
                    }.getType();
                    HttpResult<ArrayList<QuestionSort>> result = gson.fromJson(string, type);
                    ArrayList<QuestionSort> list = result.getData();
                    adapter.update(list);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                binding.swipe.setRefreshing(false);
                ToastUtils.showSingleToast("网络请求错误");
            }
        });
    }

}