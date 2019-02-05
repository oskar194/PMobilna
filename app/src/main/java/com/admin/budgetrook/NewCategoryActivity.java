package com.admin.budgetrook;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.admin.budgetrook.helpers.NoConnectivityException;
import com.admin.budgetrook.helpers.PrefsHelper;
import com.admin.budgetrook.interfaces.LoaderActivity;
import com.admin.budgetrook.tasks.PostCategoryTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewCategoryActivity extends AppCompatActivity implements LoaderActivity {

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
        final PostCategoryTask postTask = new PostCategoryTask(this);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loaderOn();
                if (isNameValid(categoryName.getText().toString())) {
                    CategoryEntity category = new CategoryEntity(categoryName.getText().toString());
                    postTask.execute(category);
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

    @Override
    public void loaderOn() {
        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);
        addCategoryButton.setEnabled(false);
        cancel.setEnabled(false);
    }

    @Override
    public void loaderOff() {
        outAnimation = new AlphaAnimation(1f, 0f);
        outAnimation.setDuration(200);
        progressBarHolder.setAnimation(outAnimation);
        progressBarHolder.setVisibility(View.GONE);
        addCategoryButton.setEnabled(true);
        cancel.setEnabled(true);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void finishTask() {
        finish();
    }

    @Override
    public void startTask() {

    }

    private class GetAllCategoriesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Long accountId = PrefsHelper.getInstance().getCurrentUserId(getApplicationContext());

            List<CategoryEntity> categories = AppDatabase.getInstance(getApplicationContext()).categoryDao().getAll(accountId);
            for (CategoryEntity category : categories) {
                categoryNames.add(category.getName());
            }
            return null;
        }
    }
}
