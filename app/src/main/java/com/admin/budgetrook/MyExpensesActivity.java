package com.admin.budgetrook;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.admin.budgetrook.adapters.ExpenseAdapter;
import com.admin.budgetrook.entities.CategoryEntity;
import com.admin.budgetrook.entities.ExpenseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyExpensesActivity extends Activity {

    private ListView myExpensesListView;
    private Map<String, String> categoriesMap = new HashMap<String, String>();

    @Override
    protected void onResume() {
        super.onResume();
        new PopulateListTask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_expenses);
        myExpensesListView = (ListView) findViewById(R.id.my_expenses_lv);
        myExpensesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ExpenseDetailView.class);
                ExpenseEntity expense = (ExpenseEntity)parent.getItemAtPosition(position);
                intent.putExtra("expenseUid", Long.toString(expense.getUid()));
                startActivity(intent);
            }
        });

    }

    private void setAdapter(List<ExpenseEntity> expenses) {
        ListAdapter adapter = new ExpenseAdapter(getApplicationContext(), expenses, categoriesMap);
        myExpensesListView.setAdapter(adapter);

    }

    class PopulateListTask extends AsyncTask<Void, Void, List<ExpenseEntity>> {

        @Override
        protected List<ExpenseEntity> doInBackground(Void... voids) {
            List<CategoryEntity> categories = AppDatabase.getInstance(getApplicationContext()).categoryDao().getAll();
            for(CategoryEntity category : categories){
                categoriesMap.put(Integer.toString(category.getUid()), category.getName());
            }
            return AppDatabase.getInstance(getApplicationContext()).expenseDao().getAll();
        }

        @Override
        protected void onPostExecute(List<ExpenseEntity> expenses) {
            setAdapter(expenses);
        }
    }
}
