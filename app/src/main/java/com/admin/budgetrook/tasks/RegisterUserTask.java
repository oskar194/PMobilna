package com.admin.budgetrook.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.admin.budgetrook.AppDatabase;
import com.admin.budgetrook.apis.AccountApi;
import com.admin.budgetrook.apis.CategoryApi;
import com.admin.budgetrook.dao.AccountDao;
import com.admin.budgetrook.dto.ResponseMessage;
import com.admin.budgetrook.entities.AccountEntity;
import com.admin.budgetrook.entities.CategoryEntity;
import com.admin.budgetrook.helpers.ApiHelper;
import com.admin.budgetrook.helpers.NoConnectivityException;
import com.admin.budgetrook.interfaces.LoaderActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUserTask extends AsyncTask<AccountEntity, Void, String> {

    private static final String TAG = "budgetrook";

    public RegisterUserTask(Activity context) {
        listener = (LoaderActivity) context;
    }

    private LoaderActivity listener;
    private String responseMessage;
    private Exception e;

    @Override
    protected void onPreExecute() {
        listener.startTask();
        listener.loaderOn();
    }

    @Override
    protected String doInBackground(final AccountEntity... accountEntities) {
        try {
            AccountEntity givenAccount = accountEntities[0];
            AccountApi api = ApiHelper.getClient(listener.getContext()).create(AccountApi.class);
            Call<AccountEntity> call = api.postAccount(givenAccount);
            Response<AccountEntity> response = call.execute();
            if (response.isSuccessful()) {
                givenAccount.setExternalId(response.body().getExternalId());
                responseMessage = "Account " + accountEntities[0].getLogin() + " created";
            } else {
                responseMessage = "Remote unavailable";
            }
            long accountUid = AppDatabase.getInstance(listener.getContext()).accountDao().insert(givenAccount);
            givenAccount.setUid(accountUid);
        } catch (Exception e) {
            this.e = e;
            if (e instanceof NoConnectivityException) {
                responseMessage = "Network unavailable";
            }
        }
        return responseMessage;
    }

    @Override
    protected void onPostExecute(String s) {
        if (e != null) {
            Log.e(TAG, "onPostExecute: ", e);
        }
        listener.loaderOff();
        listener.showMessage(responseMessage);
        listener.finishTask();
    }
}
