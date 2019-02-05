package com.admin.budgetrook.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.admin.budgetrook.DbHelper;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.helpers.PrefsHelper;
import com.admin.budgetrook.interfaces.DataReceiver;

import java.util.Date;
import java.util.List;

public class GetExpensesWithinDatesTask extends AsyncTask<Void, Void, List<ExpenseEntity>> {
    private Date from;
    private Date to;
    private DataReceiver listener;
    private String id;

    public GetExpensesWithinDatesTask(Date from, Date to, Context context, String id){
        this.from = from;
        this.to = to;
        listener = (DataReceiver) context;
        this.id = id;
    }

    @Override
    protected void onPostExecute(List<ExpenseEntity> expenseEntities) {
        listener.receiveData(expenseEntities, id);
    }

    @Override
    protected List<ExpenseEntity> doInBackground(Void... voids) {
        return DbHelper.getDb(listener.getContext()).expenseDao().getBetween(from, to,
                PrefsHelper.getInstance().getCurrentUserId(listener.getContext()));
    }
}
