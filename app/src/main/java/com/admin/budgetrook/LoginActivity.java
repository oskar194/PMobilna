package com.admin.budgetrook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

    private EditText login;
    private EditText password;
    private Button loginBtn;
    private Button registerBtn;
    private Button debugBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                goToMainMenu();
            }
        });
    }

    private void goToRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void submitLoginForm() {
        if ("test123".equals(password.getText().toString()) && "Test".equals(login.getText().toString())) {
            HttpClient.login();
            goToMainMenu();
        } else {
            showError();
        }
    }

    private void showError() {
        login.getText().clear();
        password.getText().clear();
        Toast.makeText(this, "Entered credentials are not valid", Toast.LENGTH_SHORT).show();
    }

    private void goToMainMenu() {
        startActivity(new Intent(this, MenuActivity.class));
    }
}
