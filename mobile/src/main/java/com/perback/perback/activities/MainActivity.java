package com.perback.perback.activities;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.perback.perback.R;
import com.perback.perback.x_base.BaseActivity;

public class MainActivity extends BaseActivity {

    private ImageView ivFacebook;
    private ImageView ivTwitter;
    private ImageView ivPerback;
    private Button btnSkip;


    @Override
    protected int getLayoutResId() {
        return R.layout.backup_data_activity;
    }

    @Override
    protected void setData() {
        super.setData();
       /* TripTestActivity.launch(this);
        finish();*/
    }

    @Override
    protected void linkUI() {
        super.linkUI();
        ivFacebook = (ImageView) findViewById(R.id.login_iv_facebook);
        ivTwitter = (ImageView) findViewById(R.id.login_iv_twitter);
        ivPerback = (ImageView) findViewById(R.id.login_iv_perback);
        btnSkip = (Button) findViewById(R.id.login_btn_skip);
    }

    @Override
    protected void setActions() {
        super.setActions();

        ivFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Facebook pressed", Toast.LENGTH_SHORT).show();
            }
        });

        ivTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Twitter pressed", Toast.LENGTH_SHORT).show();
            }
        });

        ivPerback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MapActivity.class);
                startActivity(i);
            }
        });

    }
}
