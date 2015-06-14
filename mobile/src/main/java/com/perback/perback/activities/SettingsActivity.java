package com.perback.perback.activities;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.perback.perback.R;
import com.perback.perback.dao.Dao;
import com.perback.perback.x_base.BaseActivity;


/**
 * Created by ralucapostelnicu on 14/06/15.
 */
public class SettingsActivity extends BaseActivity {

    private TextView tvUpdateDist;
    private TextView tvDistProximity;
    private CheckBox chkDisplayLocation;
    private TextView tvTerms;
    private TextView tvPrivacy;
    private TextView tvLogin;

    private Dao dao;

    @Override
    protected int getLayoutResId() {
        return R.layout.settings_activity;
    }

    @Override
    protected void init() {
        super.init();
        dao = Dao.getInstance();
    }

    @Override
    protected void linkUI() {
        super.linkUI();
        tvUpdateDist = (TextView) findViewById(R.id.tv_update_distance);
        tvDistProximity = (TextView) findViewById(R.id.tv_distance_proximity);
        chkDisplayLocation = (CheckBox) findViewById(R.id.chk_display_location);
        tvTerms = (TextView) findViewById(R.id.tv_terms);
        tvPrivacy = (TextView) findViewById(R.id.tv_privacy);
        tvLogin = (TextView) findViewById(R.id.tv_login);
    }

    @Override
    protected void setActions() {
        super.setActions();

        tvUpdateDist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedPosition = dao.readUpdateDistancePosition();
                new MaterialDialog.Builder(SettingsActivity.this)
                        .title(R.string.update_distance)
                        .items(R.array.update_distance_time)
                        .itemsCallbackSingleChoice(selectedPosition, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                dao.writeUpdateDistancePosition(which);
                                return true;
                            }
                        })
                        .positiveText(R.string.choose)
                        .negativeText(R.string.cancel)
                        .show();

            }
        });

        tvDistProximity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedPosition = dao.readDistanceProximityPosition();
                new MaterialDialog.Builder(SettingsActivity.this)
                        .title(R.string.distance_proximity)
                        .items(R.array.distance_proximity)
                        .itemsCallbackSingleChoice(selectedPosition, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                dao.writeDistanceProximityPosition(which);
                                return true;
                            }
                        })
                        .positiveText(R.string.choose)
                        .negativeText(R.string.cancel)
                        .show();

            }
        });

        chkDisplayLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(chkDisplayLocation.isChecked()){
                    dao.writeDisplayLocation(true);
                }else{
                    dao.writeDisplayLocation(false);
                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void setData() {
        super.setData();

        if( dao.readDisplayLocation() == true){
            chkDisplayLocation.setChecked(true);
        }
    }
}
