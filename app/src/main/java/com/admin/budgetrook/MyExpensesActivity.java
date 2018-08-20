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
import com.admin.budgetrook.entities.ExpenseEntity;

import java.util.ArrayList;
import java.util.List;

public class MyExpensesActivity extends Activity {

    private ListView myExpensesListView;

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
                Log.d("BUDGETROOK", "expensUid in Parent: " + expense.getUid());
                intent.putExtra("expenseUid", Long.toString(expense.getUid()));
                startActivity(intent);
            }
        });

    }

    private void setAdapter(List<ExpenseEntity> expenses) {
        ListAdapter adapter = new ExpenseAdapter(getApplicationContext(), expenses);
        myExpensesListView.setAdapter(adapter);

    }

    class PopulateListTask extends AsyncTask<Void, Void, List<ExpenseEntity>> {

        @Override
        protected List<ExpenseEntity> doInBackground(Void... voids) {
            return AppDatabase.getInstance(getApplicationContext()).expenseDao().getAll();
        }

        @Override
        protected void onPostExecute(List<ExpenseEntity> expenses) {
            setAdapter(expenses);
        }
    }
}
