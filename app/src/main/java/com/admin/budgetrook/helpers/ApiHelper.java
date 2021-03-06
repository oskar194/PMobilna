package com.admin.budgetrook.helpers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHelper {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        Gson gson = new GsonBuilder()
                .setDateFormat("dd/MM/yyyy HH:mm:ss")
                .create();

        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new ConnectivityInterceptor(context))
                    .addInterceptor(new AuthHeaderInterceptor(context))
                    .addInterceptor(new CookieInterceptor(context))
                    .addInterceptor(interceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.7.70.236:8080")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
