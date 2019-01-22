package com.admin.budgetrook.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.admin.budgetrook.AppDatabase;
import com.admin.budgetrook.apis.AccountApi;
import com.admin.budgetrook.apis.CategoryApi;
import com.admin.budgetrook.dao.AccountDao;
import com.admin.budgetrook.dto.ResponseMessage;
import com.admin.budgetrook.entities.AccountEntity;
import com.admin.budgetrook.entities.CategoryEntity;
import com.admin.budgetrook.helpers.ApiHelper;
import com.admin.budgetrook.interfaces.LoaderActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUserTask extends AsyncTask<AccountEntity, Void, String> {

    public RegisterUserTask(Activity context) {
        listener = (LoaderActivity) context;
    }

    private LoaderActivity listener;
    private String responseMessage;
    private AccountEntity givenAccount;

    @Override
    protected void onPreExecute() {
        listener.loaderOn();
    }

    @Override
    protected String doInBackground(final AccountEntity... accountEntities) {
        givenAccount = accountEntities[0];
        AccountApi api = ApiHelper.getClient(listener.getContext()).create(AccountApi.class);
        Call<AccountEntity> call = api.postAccount(givenAccount);
        call.enqueue(new Callback<AccountEntity>() {
            @Override
            public void onResponse(Call<AccountEntity> call, Response<AccountEntity> response) {
                givenAccount.setExternalId(response.body().getExternalId());
                responseMessage = "Account " + accountEntities[0].getLogin() + " created";
            }

            @Override
            public void onFailure(Call<AccountEntity> call, Throwable t) {
                responseMessage = t.getMessage();
                call.cancel();
            }
        });
        AppDatabase.getInstance(listener.getContext()).accountDao().insertAll(givenAccount);
        return responseMessage;
    }

    @Override
    protected void onPostExecute(String s) {
        listener.loaderOff();
        listener.showMessage(responseMessage);
    }
}
