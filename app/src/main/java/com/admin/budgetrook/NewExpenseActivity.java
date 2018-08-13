package com.admin.budgetrook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.budgetrook.dao.CategoryDao;
import com.admin.budgetrook.entities.CategoryEntity;
import com.admin.budgetrook.entities.ExpenseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class NewExpenseActivity extends Activity {

    public static final int RESULT_CODE = 100;

    private Button submitButton;
    private ImageView photoView;
    private Spinner categorySelector;
    private EditText expenseName;
    private TextView addPhotoText;
    private Button addCategoryButton;

    private List<CategoryEntity> categories = new ArrayList<CategoryEntity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);
        setup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new UpdateSpinnerTask().execute();
    }

    private void setup() {
        this.submitButton = (Button) findViewById(R.id.submit_button);
        this.photoView = (ImageView) findViewById(R.id.photo_view);
        this.categorySelector = (Spinner) findViewById(R.id.category_spinner);
        this.expenseName = (EditText) findViewById(R.id.name_edit_text);
        this.addPhotoText = (TextView) findViewById(R.id.add_photo_text);
        addPhotoText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startCameraActivity(v);
                    }
                }
        );
        this.addCategoryButton = (Button) findViewById(R.id.add_category_btn);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewCategoryActivity.class));
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InsertNewExpenseTask().execute();
            }
        });
    }

    private void setupSpinner(List<String> labels) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.custom_spinner_item, labels);
        categorySelector.setAdapter(dataAdapter);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void setPhotoFromCamera(Intent data) {
        String path = data.getStringExtra(CameraActivity.IMAGE_PATH);
        Bitmap photo = BitmapFactory.decodeFile(path);
        Bitmap scaled = Bitmap.createScaledBitmap(photo, photo.getWidth() / 10,
                photo.getHeight() / 10, true);
        photoView.setImageBitmap(scaled);
        addPhotoText.setVisibility(View.INVISIBLE);
        photoView.setVisibility(View.VISIBLE);
    }

    private void startCameraActivity(View v) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CODE) {
            setPhotoFromCamera(data);
        }
    }

    private class UpdateSpinnerTask extends AsyncTask<String, Integer, List<String>> {
        @Override
        protected List<String> doInBackground(String... strings) {
            List<String> result = new ArrayList<String>();
            List<CategoryEntity> categories = AppDatabase.getInstance(getApplicationContext()).categoryDao().getAll();
            for (CategoryEntity category : categories) {
                result.add(category.getName());
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<String> labels) {
            setupSpinner(labels);
        }
    }

    private class InsertNewExpenseTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean success = false;
            try {

                CategoryEntity category = AppDatabase.getInstance(getApplicationContext()).categoryDao().getByName(
                        categorySelector.getSelectedItem().toString()
                );
                String name = expenseName.getText().toString();
                ExpenseEntity expense = new ExpenseEntity(
                        10000L, name, category.getUid(), new Date()
                );
                AppDatabase.getInstance(getApplicationContext()).expenseDao().insertAll(expense);
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
                success = false;
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            showMessage(result);
        }
    }

    private void showMessage(Boolean result) {
        if(result){
            Toast.makeText(getApplicationContext(), "New expense created", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Expense creation failed", Toast.LENGTH_SHORT).show();
        }
    }

}

