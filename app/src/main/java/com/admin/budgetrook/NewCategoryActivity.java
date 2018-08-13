package com.admin.budgetrook;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.admin.budgetrook.entities.CategoryEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class NewCategoryActivity extends Activity {

    private EditText categoryName;
    private Button addCategoryButton;
    private Button cancel;

    private List<String> categoryNames = new ArrayList<String>();

    @Override
    protected void onResume() {
        super.onResume();
        new GetAllCategoriesTask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);
        addCategoryButton = (Button) findViewById(R.id.submit_btn);
        categoryName = (EditText) findViewById(R.id.name_et);
        cancel = (Button) findViewById(R.id.cancel_btn);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNameValid(categoryName.getText().toString())) {
                    insertNewCategory(categoryName.getText().toString());
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Category exist or empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean isNameValid(String name) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        return (!categoryNames.contains(name));
    }

    private void insertNewCategory(final String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(getApplicationContext()).categoryDao().insertAll(new CategoryEntity(name));
            }
        }).start();
    }

    private class GetAllCategoriesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            List<CategoryEntity> categories = AppDatabase.getInstance(getApplicationContext()).categoryDao().getAll();
            for (CategoryEntity category : categories) {
                categoryNames.add(category.getName());
            }
            return null;
        }
    }
}
