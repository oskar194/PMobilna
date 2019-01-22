package com.admin.budgetrook.apis;

import com.admin.budgetrook.entities.ImageEntity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface ImageApi {
    @POST("/image")
    Call<ImageEntity> postImage(@Body ImageEntity imageEntity);

    @PATCH("/image")
    Call<ImageEntity> updateImage(@Body ImageEntity imageEntity);

    @DELETE("/image")
    Call<ImageEntity> deleteImage(@Body ImageEntity imageEntity);

}
