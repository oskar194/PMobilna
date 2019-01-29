package com.admin.budgetrook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.admin.budgetrook.entities.AccountEntity;
import com.admin.budgetrook.helpers.PrefsHelper;
import com.admin.budgetrook.interfaces.LoaderActivity;
import com.admin.budgetrook.tasks.LoginTask;

public class LoginActivity extends Activity implements LoaderActivity {

    private EditText login;
    private EditText password;
    private Button loginBtn;
    private Button registerBtn;
    private Button debugBtn;

    private AlphaAnimation inAnimation;
    private AlphaAnimation outAnimation;

    private FrameLayout progressBarHolder;
    private int tasksCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBarHolder = (FrameLayout) findViewById(R.id.new_login_progressBarHolder);
        login = (EditText) findViewById(R.id.login_te);
        password = (EditText) findViewById(R.id.pass_te);
        loginBtn = (Button) findViewById(R.id.submit_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitLoginForm();
            }
        });
        registerBtn = (Button) findViewById(R.id.register_btn);
        debugBtn = (Button) findViewById(R.id.debug_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });
        debugBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debugLogin();
            }
        });
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
    }

    private void goToRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void submitLoginForm() {
        new FetchAccountTask().execute(login.getText().toString(), password.getText().toString());
    }

    private void debugLogin() {
        new FetchAccountTask().execute("Admin", "adm123");
    }

    private void proceedWithValidation(AccountEntity accountEntity) {
        if (accountEntity != null) {
            new LoginTask(this).execute(accountEntity);
        } else {
            showError();
        }
    }

    private void goToMainMenu() {
        startActivity(new Intent(this, MenuActivity.class));
    }

    private void showError() {
        login.getText().clear();
        login.setError("Entered credentials are not valid");
        password.getText().clear();
        password.setError("Entered credentials are not valid");
    }

    @Override
    public void loaderOn() {
        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);
        loginBtn.setEnabled(false);
        registerBtn.setEnabled(false);
        debugBtn.setEnabled(false);
    }

    @Override
    public void loaderOff() {
        outAnimation = new AlphaAnimation(1f, 0f);
        outAnimation.setDuration(200);
        progressBarHolder.setAnimation(outAnimation);
        progressBarHolder.setVisibility(View.GONE);
        loginBtn.setEnabled(true);
        registerBtn.setEnabled(true);
        debugBtn.setEnabled(true);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public synchronized void finishTask() {
        tasksCounter--;
        if (tasksCounter == 0) {
            goToMainMenu();
        }
    }

    @Override
    public synchronized void startTask() {
        tasksCounter++;
    }

    private class FetchAccountTask extends AsyncTask<String, Void, AccountEntity> {

        @Override
        protected void onPreExecute() {
            loaderOn();
        }

        @Override
        protected AccountEntity doInBackground(String... credentials) {
            String login = credentials[0];
            String password = credentials[1];

            AccountEntity accountEntity = AppDatabase.getInstance(getApplicationContext()).accountDao().getByLogin(login);
            if (accountEntity != null) {
                if (password.equals(accountEntity.getPassword())) {
                    return accountEntity;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(AccountEntity accountEntity) {
            loaderOff();
            proceedWithValidation(accountEntity);
        }
    }
}
