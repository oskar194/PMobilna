package com.admin.budgetrook;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.admin.budgetrook.dialogs.AmountPickerDialog;
import com.admin.budgetrook.dialogs.DatePickerFragment;
import com.admin.budgetrook.entities.CategoryEntity;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.entities.ImageEntity;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpenseDetailView extends Activity implements DatePickerFragment.OnFragmentInteractionListener,
        AmountPickerDialog.AmountPickerInterface {

    private final String TAG = "BUDGETROOK";

    private static String expenseUid;
    private TextView expenseName;
    private TextView categoryName;
    private TextView expenseAmount;
    private TextView expenseDate;
    private ImageView thumbnail;

    private Button cancelButton;
    private Button saveButton;
    private Button deleteButton;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    FrameLayout progressBarHolder;

    DialogFragment datePickerDialog;
    DialogFragment amountPickerDialog;

    ImageEntity imageEntity;

    private static final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    private void createDatePickerDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        datePickerDialog = DatePickerFragment.newInstance(expenseDate.getText().toString());
        datePickerDialog.show(ft, "dialog");
    }

    private void createAmountPickerDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        amountPickerDialog = AmountPickerDialog.newInstance(expenseAmount.getText().toString());
        amountPickerDialog.show(ft, "dialog");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("BUDGETROOK", "expenseUid in onResume: " + expenseUid);
        new SetupTask().execute(expenseUid);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail_view);
        if (getIntent().getStringExtra("expenseUid") != null) {
            expenseUid = getIntent().getStringExtra("expenseUid");
            Log.d("BUDGETROOK", "expenseUid: " + expenseUid);
        }
        expenseName = (TextView) findViewById(R.id.detail_name_tv);
        categoryName = (TextView) findViewById(R.id.detail_category_tv);
        expenseAmount = (TextView) findViewById(R.id.detail_amount_tv);
        expenseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAmountPickerDialog();
            }
        });

        expenseDate = (TextView) findViewById(R.id.detail_date_tv);

        expenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDatePickerDialog();
            }
        });
        thumbnail = (ImageView) findViewById(R.id.detail_thumbnail_iv);

        cancelButton = (Button) findViewById(R.id.detail_cancel_btn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leave();
            }
        });
        saveButton = (Button) findViewById(R.id.detail_save_btn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String raw = expenseAmount.getText().toString();
                new UpdateTask().execute(new SetupDto(
                        expenseName.getText().toString(),
                        new BigDecimal(raw),
                        convertDate(expenseDate.getText().toString()),
                        categoryName.getText().toString(),
                        imageEntity == null ? null : imageEntity.getImage()
                ));
            }
        });
        deleteButton = (Button) findViewById(R.id.detail_delete_btn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteTask().execute();
            }
        });
        progressBarHolder = (FrameLayout) findViewById(R.id.detail_progressBarHolder);
    }

    private void setViewValues(SetupDto setupDto) {
        expenseName.setText(setupDto.expenseName);
        categoryName.setText(setupDto.categoryName);
        String floating = String.format("%.2f", (setupDto.expenseAmount.floatValue()));
        if (floating.contains(",")) {
            floating = floating.replace(",", ".");
        }
        expenseAmount.setText(floating);
        expenseDate.setText(format.format(setupDto.expenseDate));
        if (imageEntity != null) {
            thumbnail.setImageBitmap(BitmapFactory.decodeByteArray(imageEntity.getImage(), 0, imageEntity.getImage().length));
        } else {
            thumbnail.setImageResource(R.drawable.ic_notification);
        }
    }

    @Override
    public void onFragmentInteraction(Date date) {
        datePickerDialog.dismiss();
        expenseDate.setText(format.format(date));
    }

    @Override
    public void finishDialog() {
        datePickerDialog.dismiss();
    }

    @Override
    public void amountSelected(String newAmount) {
        expenseAmount.setText(newAmount);
        amountPickerDialog.dismiss();
    }

    private class SetupTask extends AsyncTask<String, Void, SetupDto> {

        @Override
        protected SetupDto doInBackground(String... params) {
            ExpenseEntity expense = AppDatabase.getInstance(getApplicationContext())
                    .expenseDao().getById(Integer.valueOf(params[0]));
            CategoryEntity category = AppDatabase.getInstance(getApplicationContext())
                    .categoryDao().getById(expense.getCategoryId());

            imageEntity = AppDatabase.getInstance(getApplicationContext()).
                    imageDao().getByExpenseId(Integer.valueOf(params[0]));

            Log.d(TAG, "doInBackground: imageEntity " + imageEntity);
            Log.d(TAG, "doInBackground: uid " + Integer.valueOf(params[0]));

            return new SetupDto(
                    expense.getName(),
                    expense.getAmount(),
                    expense.getDate(),
                    category.getName(),
                    imageEntity == null ? null : imageEntity.getImage()
            );
        }

        @Override
        protected void onPostExecute(SetupDto setupDto) {
            setViewValues(setupDto);
        }
    }

    private void loaderOn() {
        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);
        cancelButton.setEnabled(false);
        saveButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void loaderOff() {
        outAnimation = new AlphaAnimation(1f, 0f);
        outAnimation.setDuration(200);
        progressBarHolder.setAnimation(outAnimation);
        progressBarHolder.setVisibility(View.GONE);
        cancelButton.setEnabled(true);
        saveButton.setEnabled(true);
        deleteButton.setEnabled(true);
    }

    private void leave() {
        this.finish();
    }

    private class UpdateTask extends AsyncTask<SetupDto, Void, Void> {
        @Override
        protected Void doInBackground(SetupDto... setupDtos) {
            SetupDto setupDto = setupDtos[0];
            CategoryEntity category = AppDatabase.getInstance(getApplicationContext())
                    .categoryDao().getByName(setupDto.categoryName);
            ExpenseEntity expenseEntity = new ExpenseEntity(
                    setupDto.expenseAmount,
                    setupDto.expenseName,
                    category.getUid(),
                    setupDto.expenseDate,
                    true,
                    true
            );
            expenseEntity.setUid(Integer.valueOf(expenseUid));
            AppDatabase.getInstance(getApplicationContext()).expenseDao().update(expenseEntity);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loaderOn();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loaderOff();
            leave();
        }
    }

    private class DeleteTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            ExpenseEntity expenseEntity = AppDatabase.getInstance(getApplicationContext())
                    .expenseDao().getById(Integer.valueOf(expenseUid));
            AppDatabase.getInstance(getApplicationContext()).expenseDao().delete(expenseEntity);

            if (imageEntity != null) {
                String imagePath = imageEntity.getPath();
                if (imagePath != null) {
                    File f = new File(imagePath);
                    f.delete();
                }
                AppDatabase.getInstance(getApplicationContext()).imageDao().delete(imageEntity);
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loaderOn();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loaderOff();
            leave();
        }
    }

    private class SetupDto {
        String expenseName;
        BigDecimal expenseAmount;
        Date expenseDate;
        String categoryName;
        byte[] imageData;

        public SetupDto(String expenseName, BigDecimal expenseAmount, Date expenseDate, String categoryName, byte[] imageData) {
            this.expenseName = expenseName;
            this.expenseAmount = expenseAmount;
            this.expenseDate = expenseDate;
            this.categoryName = categoryName;
            this.imageData = imageData;
        }
    }

    private Date convertDate(String date) {
        Date d = new Date();
        try {
            d = format.parse(expenseDate.getText().toString());
        } catch (ParseException e) {
            Log.d("Exception", e.getMessage());
        }
        return d;
    }
}
