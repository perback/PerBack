package com.perback.perback.x_base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.perback.perback.R;
import com.perback.perback.activities.HealthStatusActivity;
import com.perback.perback.activities.LoginActivity;
import com.perback.perback.activities.MapActivity;
import com.perback.perback.activities.MyTripsActivity;
import com.perback.perback.activities.RegisterActivity;
import com.perback.perback.activities.SettingsActivity;
import com.perback.perback.activities.StartTripActivity;
import com.perback.perback.activities.TripProgressActivity;
import com.perback.perback.controllers.TripController;

public abstract class BaseActivity extends ActionBarActivity {
    protected FragmentManager fragmentManager;
    protected ViewHolder views;
    private Toolbar toolbar;
    private Activity activity;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        if (getLayoutResId() != View.NO_ID)
            setContentView(getLayoutResId());

        activity = this;
        initToolbar();
        setUpNavDrawer();
        linkUI();
        init();
        setData();
        setActions();
    }

    abstract protected int getLayoutResId();

    protected void linkUI() {
        views = new ViewHolder(findViewById(android.R.id.content));
    }

    protected void init() {
    }

    protected void setData() {
    }

    protected void setActions() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.general_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_phone:
                return true;
            case R.id.action_end_flag:
                return true;
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_menu);

            if (activity.getClass().getSimpleName().equals(LoginActivity.class.getSimpleName()))
                getSupportActionBar().setTitle(getResources().getString(R.string.title_login_activity));
            else if (activity.getClass().getSimpleName().equals(RegisterActivity.class.getSimpleName()))
                getSupportActionBar().setTitle(getResources().getString(R.string.title_signup_activity));
            else if (activity.getClass().getSimpleName().equals(SettingsActivity.class.getSimpleName()))
                getSupportActionBar().setTitle(getResources().getString(R.string.title_settings_activity));
            else if (activity.getClass().getSimpleName().equals(StartTripActivity.class.getSimpleName()))
                getSupportActionBar().setTitle(getResources().getString(R.string.title_start_trip_activity));
            else if (activity.getClass().getSimpleName().equals(MyTripsActivity.class.getSimpleName()))
                getSupportActionBar().setTitle(getResources().getString(R.string.title_my_trips_activity));
            else if (activity.getClass().getSimpleName().equals(HealthStatusActivity.class.getSimpleName()))
                getSupportActionBar().setTitle(getResources().getString(R.string.title_health_status_activity));
            else if (activity.getClass().getSimpleName().equals(TripProgressActivity.class.getSimpleName()))
                getSupportActionBar().setTitle(getResources().getString(R.string.title_trip_progress_activity));
            else
                getSupportActionBar().setTitle("Perback");
        }
    }

    private void setUpNavDrawer() {
        if (toolbar != null) {
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.navigation_view);
            if (TripController.getInstance().isMonitoring()) {
                MenuItem menuItem = navigationView.getMenu().getItem(1);
                menuItem.setTitle(getString(R.string.item_trip_progress));
            }

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);

                    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {
                            Intent intent;
                            menuItem.setChecked(true);
                            switch (menuItem.getItemId()) {
                                case R.id.item_map:
                                    intent = new Intent(BaseActivity.this, MapActivity.class);
                                    startActivity(intent);
                                    drawerLayout.closeDrawer(GravityCompat.START);
                                    return true;
                                case R.id.item_start_trip:
                                    if (TripController.getInstance().isMonitoring())
                                        intent = new Intent(BaseActivity.this, TripProgressActivity.class);
                                    else
                                        intent = new Intent(BaseActivity.this, StartTripActivity.class);
                                    startActivity(intent);
                                    drawerLayout.closeDrawer(GravityCompat.START);
                                    return true;
                                case R.id.item_my_trips:
                                    intent = new Intent(BaseActivity.this, MyTripsActivity.class);
                                    startActivity(intent);
                                    drawerLayout.closeDrawer(GravityCompat.START);
                                    return true;
                                case R.id.item_health_status:
                                    intent = new Intent(BaseActivity.this, HealthStatusActivity.class);
                                    startActivity(intent);
                                    drawerLayout.closeDrawer(GravityCompat.START);
                                    return true;
                                case R.id.item_settings:
                                    intent = new Intent(BaseActivity.this, SettingsActivity.class);
                                    startActivity(intent);
                                    drawerLayout.closeDrawer(GravityCompat.START);
                                    return true;
                                default:
                                    return true;
                            }
                        }
                    });
                }
            });
        }
    }


    public void showMessage(String message, DialogInterface.OnClickListener clickListener) {
        new AlertDialog.Builder(BaseActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", clickListener)
                .setCancelable(clickListener == null)
                .show();
    }
}