package com.admin.budgetrook.helpers;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthHeaderInterceptor implements Interceptor {

    public AuthHeaderInterceptor(Context context) {
        this.context = context;
    }

    private Context context;

    @Override
    public Response intercept(Chain chain) throws IOException {
        PrefsHelper prefsHelper = PrefsHelper.getInstance();
        Request.Builder builder = chain.request().newBuilder();
        if (prefsHelper.getCurrentCookie(context) != null) {
            String cookie = prefsHelper.getCurrentCookie(context);
            builder.addHeader("Cookie", cookie);
        }
        return chain.proceed(builder.build());
    }
}
