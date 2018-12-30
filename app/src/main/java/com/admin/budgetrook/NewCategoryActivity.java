package com.admin.budgetrook;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.admin.budgetrook.apis.CategoryApi;
import com.admin.budgetrook.entities.CategoryEntity;
import com.admin.budgetrook.helpers.ApiHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewCategoryActivity extends Activity {

    private EditText categoryName;
    private Button addCategoryButton;
    private Button cancel;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    FrameLayout progressBarHolder;

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
        this.progressBarHolder = (FrameLayout) findViewById(R.id.new_category_progressBarHolder);
        addCategoryButton = (Button) findViewById(R.id.submit_btn);
        categoryName = (EditText) findViewById(R.id.name_et);
        cancel = (Button) findViewById(R.id.cancel_btn);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loaderOn();
                if (isNameValid(categoryName.getText().toString())) {
                    CategoryEntity category = new CategoryEntity(categoryName.getText().toString());
                    insertNewCategory(category);
                    CategoryApi api = ApiHelper.getClient().create(CategoryApi.class);
                    Call<CategoryEntity> call = api.postCategory(category);
                    call.enqueue(new Callback<CategoryEntity>() {
                        @Override
                        public void onResponse(Call<CategoryEntity> call, Response<CategoryEntity> response) {
                            Toast.makeText(getApplicationContext(), "Request to remote successful", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<CategoryEntity> call, Throwable t) {
                            call.cancel();
                            Toast.makeText(getApplicationContext(), "Request to remote failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Category exist or empty", Toast.LENGTH_SHORT).show();
                }
                loaderOff();
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

    private void loaderOn() {
        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);
        addCategoryButton.setEnabled(false);
        cancel.setEnabled(false);
    }

    private void loaderOff() {
        outAnimation = new AlphaAnimation(1f, 0f);
        outAnimation.setDuration(200);
        progressBarHolder.setAnimation(outAnimation);
        progressBarHolder.setVisibility(View.GONE);
        addCategoryButton.setEnabled(true);
        cancel.setEnabled(true);
    }

    private void insertNewCategory(final CategoryEntity category) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(getApplicationContext()).categoryDao().insertAll(category);
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
