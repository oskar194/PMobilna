package com.admin.budgetrook.helpers;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CookieInterceptor implements Interceptor {

    public CookieInterceptor(Context context) {
        this.context = context;
    }

    private Context context;

    @Override
    public Response intercept(Chain chain) throws IOException {
        PrefsHelper prefsHelper = PrefsHelper.getInstance();
        final Response response = chain.proceed(chain.request());

        if (!response.headers("Set-Cookie").isEmpty()) {
            for (String cookie : response.headers("Set-Cookie")) {
                prefsHelper.setCookie(context, cookie);
            }
        }

        return response;
    }
}
