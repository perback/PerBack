package com.perback.perback.activities;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.perback.perback.R;
import com.perback.perback.x_base.BaseActivity;

/**
 * Created by ralucapostelnicu on 14/06/15.
 */
public class RegisterActivity extends BaseActivity {

    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnSignUp;

    @Override
    protected int getLayoutResId() {
        return R.layout.register_activity;
    }

    @Override
    protected void linkUI() {
        super.linkUI();
        etUsername = (EditText) findViewById(R.id.et_username);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
    }

    @Override
    protected void setActions() {
        super.setActions();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterActivity.this, "Sign up", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
