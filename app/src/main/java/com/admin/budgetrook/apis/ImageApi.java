package com.admin.budgetrook.apis;

import com.admin.budgetrook.dto.ParseDto;
import com.admin.budgetrook.dto.ResponseMessage;
import com.admin.budgetrook.entities.ImageEntity;
import com.admin.budgetrook.wrappers.ImageWrapper;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ImageApi {
    @POST("/image")
    Call<ImageWrapper> postImage(@Body ImageWrapper imageWrapper);

    @PATCH("/image")
    Call<ResponseMessage> updateImage(@Body ImageWrapper imageWrapper);

    @DELETE("/image")
    Call<ResponseMessage> deleteImage(@Body ImageWrapper imageWrapper);

    @Multipart
    @POST("/image/process/{id}")
    Call<ParseDto> postAndProcessImage(@Part MultipartBody.Part filePart, @Path("id") Long id);

}
