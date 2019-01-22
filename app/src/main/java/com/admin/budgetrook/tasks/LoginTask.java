package com.admin.budgetrook.tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.admin.budgetrook.apis.AccountApi;
import com.admin.budgetrook.entities.AccountEntity;
import com.admin.budgetrook.helpers.ApiHelper;
import com.admin.budgetrook.interfaces.LoaderActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginTask extends AsyncTask<AccountEntity, Void, String> {

    public LoginTask(Activity context) {
        listener = (LoaderActivity) context;
    }

    private LoaderActivity listener;
    private String responseMessage;

    @Override
    protected void onPreExecute() {
        listener.loaderOn();
    }

    @Override
    protected String doInBackground(AccountEntity... accountEntities) {
        AccountApi api = ApiHelper.getClient(listener.getContext()).create(AccountApi.class);
        AccountEntity accountEntity = accountEntities[0];
        Call<ResponseBody> loginCall = api.login(accountEntity.getLogin(), accountEntity.getPassword());
        loginCall.enqueue(new Callback<ResponseBody>(){

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                responseMessage = "Login success";
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                responseMessage = "Login failed";
                call.cancel();
            }
        });
        return responseMessage;
    }

    @Override
    protected void onPostExecute(String s) {
        listener.loaderOff();
        listener.showMessage(s);
    }
}
