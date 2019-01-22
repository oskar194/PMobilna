package com.admin.budgetrook.apis;

import com.admin.budgetrook.dto.ResponseMessage;
import com.admin.budgetrook.entities.AccountEntity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface AccountApi {
    @POST("/register")
    Call<AccountEntity> postAccount(@Body AccountEntity accountEntity);

    @FormUrlEncoded
    @POST("/login")
    Call<ResponseBody> login(@Field("username") String name, @Field("password") String password);

    @PATCH("/account")
    Call<AccountEntity> updateAccount(@Body AccountEntity accountEntity);

    @DELETE("/account")
    Call<AccountEntity> deleteAccount(@Body AccountEntity accountEntity);
}
