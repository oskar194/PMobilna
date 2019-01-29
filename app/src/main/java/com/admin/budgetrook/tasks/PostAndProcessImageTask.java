package com.admin.budgetrook.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.admin.budgetrook.AppDatabase;
import com.admin.budgetrook.DbHelper;
import com.admin.budgetrook.apis.ImageApi;
import com.admin.budgetrook.dao.ExpenseDao;
import com.admin.budgetrook.dto.ParseDto;
import com.admin.budgetrook.dto.ResponseMessage;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.entities.ImageEntity;
import com.admin.budgetrook.helpers.ApiHelper;
import com.admin.budgetrook.helpers.NoConnectivityException;
import com.admin.budgetrook.helpers.PrefsHelper;
import com.admin.budgetrook.interfaces.LoaderActivity;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class PostAndProcessImageTask extends AsyncTask<ImageEntity, Void, String> {

    private static final String TAG = "budgetrook";

    public PostAndProcessImageTask(Context context) {
        listener = (LoaderActivity) context;
    }

    private LoaderActivity listener;
    private String message;
    private Exception e;

    @Override
    protected void onPreExecute() {
        listener.startTask();
    }

    @Override
    protected String doInBackground(ImageEntity... imageEntities) {
        try {
            String username = PrefsHelper.getInstance().getCurrentUserLogin(listener.getContext());
            ImageEntity entity = imageEntities[0];

            File file = new File(entity.getPath());
            if (!file.exists()) {
                throw new RuntimeException("Requested file does not exist");
            }

            ImageApi api = ApiHelper.getClient(listener.getContext()).create(ImageApi.class);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
            Log.d(TAG, "PostAndProcessImageTask doInBackground: ImageEntity "+ entity.toString());
            Call<ParseDto> call = api.postAndProcessImage(part, entity.getExternalId());
            Response<ParseDto> response = call.execute();
            if (response.isSuccessful()) {
                message = "Image processed successfully";
                ParseDto dto = response.body();
                Double amount = 0d;
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    amount = Double.parseDouble(dto.getTotal());
                } catch (Exception e){
                    this.e = e;
                }
                try {
                    date = sdf.parse(dto.getDate());
                } catch (Exception e){
                    this.e = e;
                }

                ExpenseDao dao = DbHelper.getDb(listener.getContext()).expenseDao();
                ExpenseEntity expenseEntity = dao.getById(entity.getExpenseId(), entity.getAccountId());
                expenseEntity.setAmount(new BigDecimal(amount));
                expenseEntity.setDate(date);
                dao.update(expenseEntity);
            } else {
                message = "Remote unavailable";
            }
        } catch (Exception e) {
            this.e = e;
            if (e instanceof NoConnectivityException) {
                message = "Network unavailable";
            }
        }
        return message;
    }

    @Override
    protected void onPostExecute(String s) {
        if (e != null) {
            Log.e(TAG, "onPostExecute: " + e.getMessage(), e);
        }
        listener.loaderOff();
        listener.showMessage(s);
        listener.finishTask();
    }
}
