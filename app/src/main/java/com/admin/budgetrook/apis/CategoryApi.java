package com.admin.budgetrook.apis;

import com.admin.budgetrook.entities.CategoryEntity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CategoryApi {
    @POST("/category")
    Call<CategoryEntity> postCategory(@Body CategoryEntity categoryEntity);
}

