package com.admin.budgetrook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.admin.budgetrook.entities.AccountEntity;
import com.admin.budgetrook.interfaces.LoaderActivity;
import com.admin.budgetrook.tasks.RegisterUserTask;

public class RegisterActivity extends Activity implements LoaderActivity {

    private EditText login;
    private EditText password;
    private EditText repeatPassword;
    private Button backBtn;
    private Button submitBtn;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    FrameLayout progressBarHolder;

    private int taskCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.progressBarHolder = (FrameLayout) findViewById(R.id.register_progressBarHolder);
        login = (EditText) findViewById(R.id.login_et);
        password = (EditText) findViewById(R.id.password_et);
        repeatPassword = (EditText) findViewById(R.id.repeat_pass_et);

        login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                login.setError(null);
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password.setError(null);
            }
        });

        repeatPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                repeatPassword.setError(null);
            }
        });

        backBtn = (Button) findViewById(R.id.cancel_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        submitBtn = (Button) findViewById(R.id.register_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRegisterForm();
            }
        });
    }

    private void resetErrors() {
        repeatPassword.setError(null);
        password.setError(null);
        login.setError(null);
    }

    private void submitRegisterForm() {
        if (validate()) {
            AccountEntity account = new AccountEntity();
            account.setLogin(login.getText().toString());
            account.setPassword(password.getText().toString());
            new RegisterUserTask(this).execute(account);
        }
    }

    private boolean validate() {
        boolean result = true;
        if (isValid()) {
            result = true;
        } else {
            showPassErrors();
            result = false;
        }
        if (isOriginal()) {
            result = true;
        } else {
            showOriginalityErrors();
            result = false;
        }
        if (isFilled()) {
            result = true;
        } else {
            showEmptyErrors();
            result = false;
        }
        return result;
    }

    private void showEmptyErrors() {
        showErrorIfEmpty(login);
        showErrorIfEmpty(password);
        showErrorIfEmpty(repeatPassword);

    }

    private void showErrorIfEmpty(EditText et) {
        if (TextUtils.isEmpty(et.getText().toString())) {
            et.setError("Field empty");
        }
    }

    private void showOriginalityErrors() {
        login.setError("Login not original");
    }

    private void showPassErrors() {
        password.setError("Password not match");
        repeatPassword.setError("Password not match");
    }

    private void goBack() {
        finish();
    }

    private boolean isValid() {
        if (password.getText().toString().equals(repeatPassword.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isOriginal() {
        if ("Test".equals(login.getText().toString())) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isFilled() {
        if (TextUtils.isEmpty(login.getText().toString()) ||
                TextUtils.isEmpty(password.getText().toString()) ||
                TextUtils.isEmpty(repeatPassword.getText().toString())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void loaderOn() {
        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);
        submitBtn.setEnabled(false);
        backBtn.setEnabled(false);
    }

    @Override
    public void loaderOff() {
        outAnimation = new AlphaAnimation(1f, 0f);
        outAnimation.setDuration(200);
        progressBarHolder.setAnimation(outAnimation);
        progressBarHolder.setVisibility(View.GONE);
        submitBtn.setEnabled(true);
        backBtn.setEnabled(true);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void finishTask() {
        taskCounter--;
        if(taskCounter == 0){
            finish();
        }
    }

    @Override
    public void startTask() {
        taskCounter++;
    }
}
