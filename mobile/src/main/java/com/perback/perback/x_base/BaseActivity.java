package com.perback.perback.x_base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.perback.perback.R;
import com.perback.perback.activities.HealthStatusActivity;
import com.perback.perback.activities.LoginActivity;
import com.perback.perback.activities.MapActivity;
import com.perback.perback.activities.MyTripsActivity;
import com.perback.perback.activities.RegisterActivity;
import com.perback.perback.activities.SettingsActivity;
import com.perback.perback.activities.StartTripActivity;
import com.perback.perback.activities.TripProgressActivity;

public abstract class BaseActivity extends ActionBarActivity {
    protected FragmentManager fragmentManager;
    protected ViewHolder views;
    private Toolbar toolbar;
    private Activity activity;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    //Search in toolbar
    private MenuItem searchAction;
    private boolean isSearchOpened = false;
    private AutoCompleteTextView etSeach;

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
    public boolean onPrepareOptionsMenu(Menu menu) {
        searchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (activity.getClass().getSimpleName().equals(MapActivity.class.getSimpleName()))
            inflater.inflate(R.menu.map_menu, menu);
        else if (activity.getClass().getSimpleName().equals(TripProgressActivity.class.getSimpleName()))
            inflater.inflate(R.menu.trip_progress_menu, menu);
        else inflater.inflate(R.menu.general_menu, menu);
        ;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_phone:
                Toast.makeText(activity, "SOS", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_end_flag:
                Toast.makeText(activity, "END TRIP", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_search:
                handleToolbarSearch();
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
            else if (activity.getClass().getSimpleName().equals(MapActivity.class.getSimpleName()))
                getSupportActionBar().setTitle(getResources().getString(R.string.title_map_activity));
            else
                getSupportActionBar().setTitle("Perback");
        }
    }

    private void setUpNavDrawer() {
        if (toolbar != null) {
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.navigation_view);

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

    private void handleToolbarSearch() {
        ActionBar action = getSupportActionBar(); //get the actionbar

        if (isSearchOpened) { //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etSeach.getWindowToken(), 0);

            //add the search icon in the action bar
            searchAction.setIcon(getResources().getDrawable(R.drawable.ic_search));

            isSearchOpened = false;
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_toolbar_layout);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            etSeach = (AutoCompleteTextView) action.getCustomView().findViewById(R.id.et_search); //the text editor

            //this is a listener to do a search when the user clicks on search button
            etSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                       Toast.makeText(activity, etSeach.getText().toString(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                }
            });

            etSeach.requestFocus();

            //open the keyboard focused in the etSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etSeach, InputMethodManager.SHOW_IMPLICIT);

            //add the close icon
            searchAction.setIcon(getResources().getDrawable(R.drawable.ic_search));

            isSearchOpened = true;
        }
    }
}