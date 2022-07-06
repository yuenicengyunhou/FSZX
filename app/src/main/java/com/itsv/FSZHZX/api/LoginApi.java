package com.itsv.FSZHZX.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginApi {


    @POST("login")
    Call<ResponseBody> login(@Query("username") String username, @Query("password") String password,@Query("imei")String imei);

    @POST("login/logout")
    Call<ResponseBody> logout(@Query("token") String token);


}
