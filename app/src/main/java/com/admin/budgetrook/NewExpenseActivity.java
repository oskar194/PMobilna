package com.admin.budgetrook;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.budgetrook.entities.AccountEntity;
import com.admin.budgetrook.entities.CategoryEntity;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.entities.ImageEntity;
import com.admin.budgetrook.helpers.PrefsHelper;
import com.admin.budgetrook.interfaces.LoaderActivity;
import com.admin.budgetrook.tasks.PostAndProcessImageTask;
import com.admin.budgetrook.tasks.PostExpenseTask;
import com.admin.budgetrook.tasks.PostImageTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewExpenseActivity extends Activity implements LoaderActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final String TAG = "BUDGETROOK";

    private Button submitButton;
    private ImageView photoView;
    private Spinner categorySelector;
    private EditText expenseName;
    private TextView addPhotoText;
    private Button addCategoryButton;

    private String imagePath;
    private byte[] imageData;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    FrameLayout progressBarHolder;

    private int taskCounter = 0;

    private List<CategoryEntity> categories = new ArrayList<CategoryEntity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);
        this.progressBarHolder = (FrameLayout) findViewById(R.id.new_expense_progressBarHolder);
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

    private void startCameraActivity(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (Exception ex) {
            Log.e(TAG, "startCameraActivity: ", ex);
        }
        if (photoFile != null) {
            imagePath = photoFile.getAbsolutePath();
            Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                    "com.admin.budgetrook.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            takePictureIntent.setClipData(ClipData.newRawUri("", photoURI));
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        imagePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                setPic();
                addPhotoText.setVisibility(View.INVISIBLE);
                photoView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = photoView.getWidth();
        int targetH = photoView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        bitmap = CameraHelper.rotateBitmap(bitmap, 90);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        imageData = stream.toByteArray();
        photoView.setImageBitmap(bitmap);
    }

    private class UpdateSpinnerTask extends AsyncTask<String, Integer, List<String>> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<String> doInBackground(String... strings) {
            Long accountId = PrefsHelper.getInstance().getCurrentUserId(getApplicationContext());
            List<String> result = new ArrayList<String>();
            List<CategoryEntity> categories = AppDatabase.getInstance(getApplicationContext()).categoryDao().getAll(accountId);
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
        private ImageEntity imageEntity;
        private ExpenseEntity expenseEntity;

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean success = false;
            try {
                Long accountId = PrefsHelper.getInstance().getCurrentUserId(getApplicationContext());

                CategoryEntity category = AppDatabase.getInstance(getApplicationContext()).categoryDao().getByName(
                        categorySelector.getSelectedItem().toString(), accountId
                );
                String name = expenseName.getText().toString();
                expenseEntity = new ExpenseEntity(
                        new BigDecimal(0), name, category.getUid(), new Date(), false,
                        true, accountId
                );
                long expenseUid = AppDatabase.getInstance(getApplicationContext()).expenseDao().insert(expenseEntity);
                expenseEntity.setUid(expenseUid);

                if (imagePath != null && imageData != null) {
                    Log.d(TAG, "doInBackground: imagePath " + imagePath);
                    Log.d(TAG, "doInBackground: imageData " + imageData.length);
                    imageEntity = new ImageEntity((int) expenseUid, imageData, imagePath, accountId);
                    Log.d(TAG, "doInBackground: imageEntity " + imageEntity.toString());
                    long imageUid = AppDatabase.getInstance(getApplicationContext()).imageDao().insert(imageEntity);
                    imageEntity.setUid(imageUid);
                }

                success = true;
            } catch (Exception e) {
                e.printStackTrace();
                success = false;
            }
            return success;
        }

        @Override
        protected void onPreExecute() {
            startTask();
            loaderOn();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            showMessage(result);
            proceedWithUpload(imageEntity, expenseEntity);
            finishTask();
        }
    }

    private void proceedWithUpload(ImageEntity imageEntity, ExpenseEntity expenseEntity) {
        new PostExpenseTask(getContext(), imageEntity).execute(expenseEntity);
    }

    @Override
    public void loaderOn() {
        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);
        submitButton.setEnabled(false);
        addCategoryButton.setEnabled(false);
    }

    @Override
    public void loaderOff() {
        outAnimation = new AlphaAnimation(1f, 0f);
        outAnimation.setDuration(200);
        progressBarHolder.setAnimation(outAnimation);
        progressBarHolder.setVisibility(View.GONE);
        submitButton.setEnabled(true);
        addCategoryButton.setEnabled(true);
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
    public synchronized void finishTask() {
        taskCounter--;
        if (taskCounter == 0) {
            finish();
        }
    }

    @Override
    public synchronized void startTask() {
        taskCounter++;
    }

    private void showMessage(Boolean result) {
        if (result) {
            Toast.makeText(getApplicationContext(), "New expense created", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Expense creation failed", Toast.LENGTH_SHORT).show();
        }
    }

}

