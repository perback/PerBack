package com.perback.perback.x_base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.perback.perback.R;
import com.perback.perback.activities.HealthStatusActivity;
import com.perback.perback.activities.LoginActivity;
import com.perback.perback.activities.MapActivity;
import com.perback.perback.activities.MyTripsActivity;
import com.perback.perback.activities.RegisterActivity;
import com.perback.perback.activities.SettingsActivity;
import com.perback.perback.activities.StartTripActivity;
import com.perback.perback.activities.TripProgressActivity;
import com.perback.perback.adapters.PlaceAutocompleteAdapter;
import com.perback.perback.controllers.TripController;
import com.perback.perback.dao.Dao;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public abstract class BaseActivity extends ActionBarActivity implements GoogleApiClient.OnConnectionFailedListener {
    protected FragmentManager fragmentManager;
    protected ViewHolder views;
    private Toolbar toolbar;
    private Activity activity;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    //Search in toolbar
    private MenuItem searchAction;
    private boolean isSearchOpened = false;
    private AutoCompleteTextView etAutocompleteSeach;
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private static int i = 2;

    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(43.996578, 28.295629), new LatLng(47.682693, 26.300057));

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        if (getLayoutResId() != View.NO_ID)
            setContentView(getLayoutResId());

        activity = this;
        linkUI();
        if (views.get(R.id.collapsing_toolbar) == null) {
            initToolbar();
            setUpNavDrawer();
        }
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

    public Menu menu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (activity.getClass().getSimpleName().equals(MapActivity.class.getSimpleName()))
            inflater.inflate(R.menu.map_menu, menu);
        else if (activity.getClass().getSimpleName().equals(TripProgressActivity.class.getSimpleName()))
            inflater.inflate(R.menu.trip_progress_menu, menu);
        else inflater.inflate(R.menu.general_menu, menu);

        this.menu = menu;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_phone:
                Toast.makeText(activity, "SOS", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_end_flag:

                new MaterialDialog.Builder(this)
                        .title("End Trip")
                        .content("Congratulations! You have achieved "+ new DecimalFormat("#.##").format(TripController.getInstance().getProgress())+"%" +" of your trip")
                        .positiveText("Finish")
                        .negativeText("Cancel")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                Dao.getInstance().writeIsTripStarted(false);
                                TripController.getInstance().endTrip();
//                                activity.finish();
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                            }
                        }).show();

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

            if (TripController.getInstance().isMonitoring()) {
                MenuItem menuItem = navigationView.getMenu().getItem(1);
                menuItem.setTitle(getString(R.string.item_trip_progress));
                menuItem.setIcon(R.drawable.ic_terrain_grey600_48dp);
            }

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }


            }; // Drawer Toggle Object Made
            drawerLayout.setDrawerListener(actionBarDrawerToggle); // Drawer Listener set to the Drawer toggle
            actionBarDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

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
                                    if (TripController.getInstance().isMonitoring()) {
                                        intent = new Intent(BaseActivity.this, TripProgressActivity.class);
                                        startActivity(intent);
                                    } else {
                                        intent = new Intent(BaseActivity.this, StartTripActivity.class);
                                        startActivity(intent);
                                    }
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
        final ActionBar action = getSupportActionBar(); //get the actionbar

        if (isSearchOpened) { //test if the search is open

            /*action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar*/

            //hides the keyboard
            /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etAutocompleteSeach.getWindowToken(), 0);*/

            //add the search icon in the action bar
//            searchAction.setIcon(getResources().getDrawable(R.drawable.ic_magnify_white_24dp));

            etAutocompleteSeach.setText("");

        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_toolbar_layout);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            etAutocompleteSeach = (AutoCompleteTextView) action.getCustomView().findViewById(R.id.et_search); //the text editor

            etAutocompleteSeach.setDropDownBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(R.color.white_with_opacity)));
            //this is a listener to do a search when the user clicks on search button
            etAutocompleteSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(etAutocompleteSeach.getWindowToken(), 0);

                        Geocoder geocoder = new Geocoder(BaseActivity.this);
                        final String searchedLocation = etAutocompleteSeach.getText().toString();
                        if (!TextUtils.isEmpty(searchedLocation)) {
                            List<Address> search_addrs = null;
                            try {
                                search_addrs = geocoder.getFromLocationName(searchedLocation, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (search_addrs != null && search_addrs.size() > 0) {
                                Address address = search_addrs.get(0);
                                ((MapActivity) activity).addMarkerOnMap(address.getLatitude(), address.getLongitude(), searchedLocation);
                            }
                        }

                        action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
                        action.setDisplayShowTitleEnabled(true); //show the title in the action bar

                        searchAction.setIcon(getResources().getDrawable(R.drawable.ic_magnify_white_24dp));
                        isSearchOpened = false;

                        return true;
                    }
                    return false;
                }
            });

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, i++ /* clientId */, this)
                    .addApi(Places.GEO_DATA_API)
                    .build();
            etAutocompleteSeach.setOnItemClickListener(mAutocompleteClickListener);
            mAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
                    mGoogleApiClient, LAT_LNG_BOUNDS, null);
            etAutocompleteSeach.setAdapter(mAdapter);

            etAutocompleteSeach.requestFocus();

            //open the keyboard focused in the etSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etAutocompleteSeach, InputMethodManager.SHOW_IMPLICIT);

            //add the close icon
            searchAction.setIcon(getResources().getDrawable(R.drawable.ic_window_close_white_24dp));

            isSearchOpened = true;
        }
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a PlaceAutocomplete object from which we
             read the place ID.
              */
            final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            Toast.makeText(getApplicationContext(), "Clicked: " + item.description,
                    Toast.LENGTH_SHORT).show();
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            places.release();
        }
    };

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}