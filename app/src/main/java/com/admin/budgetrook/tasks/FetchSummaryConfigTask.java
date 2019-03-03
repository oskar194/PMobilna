package com.admin.budgetrook.tasks;

import android.os.AsyncTask;

import com.admin.budgetrook.AppDatabase;
import com.admin.budgetrook.helpers.PrefsHelper;
import com.admin.budgetrook.interfaces.DataReceiver;
import com.admin.budgetrook.wrappers.SummaryWrapper;

public class FetchSummaryConfigTask extends AsyncTask<Void, Void, Object> {


    private DataReceiver listener;
    private String id;

    public FetchSummaryConfigTask(DataReceiver listener, String id) {
        this.listener = listener;
        this.id = id;
    }

    @Override
    protected Object doInBackground(Void... voids) {
        SummaryWrapper wrapper = new SummaryWrapper();
        PrefsHelper prefs = PrefsHelper.getInstance();
        AppDatabase db = AppDatabase.getInstance(listener.getContext());

        wrapper.dates = db.expenseDao().getAvailableDates(prefs.getCurrentUserId(listener.getContext()));
        wrapper.categories = db.categoryDao().getAll(prefs.getCurrentUserId(listener.getContext()));

        return wrapper;
    }

    @Override
    protected void onPostExecute(Object data) {
        listener.receiveData(data, id);
    }
}
