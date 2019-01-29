package com.admin.budgetrook.apis;

import com.admin.budgetrook.entities.CategoryEntity;
import com.admin.budgetrook.wrappers.CategoryWrapper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface CategoryApi {
    @POST("/category")
    Call<CategoryWrapper> postCategory(@Body CategoryWrapper categoryWrapper);

    @DELETE("/category")
    Call<CategoryEntity> deleteCategory(@Body CategoryEntity categoryEntity);

    @PATCH("/category")
    Call<CategoryEntity> updateCategory(@Body CategoryEntity categoryEntity);
}

