package com.perback.perback.activities;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.perback.perback.R;
import com.perback.perback.x_base.BaseActivity;

/**
 * Created by ralucapostelnicu on 13/06/15.
 */
public class LoginActivity extends BaseActivity {

    private TextView tvForgotPass;
    private TextView tvSignUp;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogIn;
    private ImageView ivFacebook;
    private ImageView ivTwitter;

    @Override
    protected int getLayoutResId() {
        return R.layout.login_activity;
    }

    @Override
    protected void setData() {
        super.setData();

    }

    @Override
    protected void linkUI() {
        super.linkUI();
        tvForgotPass = (TextView) findViewById(R.id.tv_forgot_pass);
        tvSignUp = (TextView) findViewById(R.id.tv_sign_up);
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogIn = (Button) findViewById(R.id.btn_login);
        ivFacebook = (ImageView) findViewById(R.id.login_iv_facebook);
        ivTwitter = (ImageView) findViewById(R.id.login_iv_twitter);
    }

    @Override
    protected void setActions() {
        super.setActions();

        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Forgot Password ?", Toast.LENGTH_SHORT).show();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Sign up", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Login", Toast.LENGTH_SHORT).show();

                if (etUsername.getText().toString().trim().equals("")){
                    etUsername.setError(getResources().getString(R.string.empty_username_error_message));
                }

                if (etPassword.getText().toString().trim().equals("")){
                    etPassword.setError(getResources().getString(R.string.empty_password_error_message));
                }
            }
        });
    }
}
