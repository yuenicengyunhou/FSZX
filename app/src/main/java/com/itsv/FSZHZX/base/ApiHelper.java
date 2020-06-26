package com.itsv.FSZHZX.base;


import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * ApiHelper 网络请求工具类
 * Created by kang on 2018/3/9.
 */
public class ApiHelper {

//    private static final String TAG = "ApiHelper";
//    public static final String BASE_URL = "http://118.26.64.27:8883/api/";

    private static ApiHelper mInstance;
    private Retrofit mRetrofit;
    private final OkHttpClient mHttpClient;

    private ApiHelper() {
        this(30, 30, 30);
    }

    public ApiHelper(int connTimeout, int readTimeout, int writeTimeout) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(connTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS);

        mHttpClient = builder.build();
    }

    public static ApiHelper getInstance() {
        if (mInstance == null) {
            mInstance = new ApiHelper();
        }

        return mInstance;
    }

    public ApiHelper buildRetrofit(String baseUrl) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mHttpClient)
//                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
        return this;
    }

    public <T> T createService(Class<T> serviceClass) {
        return mRetrofit.create(serviceClass);
    }

}
