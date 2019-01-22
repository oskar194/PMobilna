package com.admin.budgetrook.apis;

import com.admin.budgetrook.entities.CategoryEntity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface CategoryApi {
    @POST("/category")
    Call<CategoryEntity> postCategory(@Body CategoryEntity categoryEntity);

    @DELETE("/category")
    Call<CategoryEntity> deleteCategory(@Body CategoryEntity categoryEntity);

    @PATCH("/category")
    Call<CategoryEntity> updateCategory(@Body CategoryEntity categoryEntity);
}

