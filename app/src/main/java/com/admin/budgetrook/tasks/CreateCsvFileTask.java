package com.admin.budgetrook.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.admin.budgetrook.DbHelper;
import com.admin.budgetrook.entities.CategoriesAndExpenses;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.helpers.PrefsHelper;
import com.admin.budgetrook.interfaces.LoaderActivity;

import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CreateCsvFileTask extends AsyncTask<Void, Void, String> {

    private static final String TAG = "budgetrook";
    private LoaderActivity listener;
    private File file;


    private static final String formatStringTitle =
            "\"%s\",\"%s\",\"%s\",\"%s\"";
    private static final String formatString =
            "\"%s\",\"%.2f\",\"%s\",\"%s\"";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public CreateCsvFileTask(Context context, File file) {
        listener = (LoaderActivity) context;
        this.file = file;
    }

    @Override
    protected void onPreExecute() {
        listener.loaderOn();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String message = "File created!";
        List<CategoriesAndExpenses> categoriesAndExpenses = DbHelper.getDb(listener.getContext()).categoriesAndExpensesDao()
                .getAll(PrefsHelper.getInstance().getCurrentUserId(listener.getContext()));

        if (categoriesAndExpenses != null && categoriesAndExpenses.size() > 0) {
            try {
                PrintStream fileStream = new PrintStream(file);
                fileStream.println(String.format(Locale.US, formatStringTitle, "EXPENSE_NAME", "EXPENSE_AMOUNT", "DATE_ADDED", "CATEGORY_NAME"));
                for (CategoriesAndExpenses item : categoriesAndExpenses) {
                    String categoryName = item.getCategory().getName();
                    for (ExpenseEntity expenseEntity : item.getExpenses()) {
                        fileStream.println(String.format(Locale.US, formatString,
                                expenseEntity.getName(),
                                expenseEntity.getAmount().floatValue(),
                                dateFormat.format(expenseEntity.getDate()),
                                categoryName
                        ));

                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: ", e);
                message = "Write failed";
            }
        }
        return message;
    }

    @Override
    protected void onPostExecute(String message) {
        listener.showMessage(message);
        listener.loaderOff();
    }
}
