package com.perback.perback.activities;

import android.animation.Animator;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionValues;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.perback.perback.R;
import com.perback.perback.adapters.ExpandableListAdapter;
import com.perback.perback.apis.places.BaseResponse;
import com.perback.perback.apis.places.OpeningHours;
import com.perback.perback.apis.places.PlacesResponse;
import com.perback.perback.utils.AnimationEndListener;
import com.perback.perback.utils.RetrofitUtils;
import com.perback.perback.x_base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MapActivity extends BaseActivity implements OnMapReadyCallback {

    private FloatingActionButton fabCheckIn;
    private DrawerLayout drawerLayout;
    private DrawerLayout drawerLayoutRight;
    private String[] navigationDrawerItemTitles;
    private ListView drawerList;
    private boolean slideState = false;

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    protected ArrayList<String> listDataHeader;
    protected HashMap<String, List<String>> listDataChild;
    protected GoogleMap map;
    protected boolean cameraSet = false;
    protected RelativeLayout rl_scene;
    protected CardView cv_marker_details;
    protected int markerDetailsHeight;

    @Override
    protected int getLayoutResId() {
        return R.layout.map_activity;
    }

    public static void launch(BaseActivity activity) {
        activity.startActivity(new Intent(activity, MapActivity.class));
    }

    @Override
    protected void linkUI() {
        super.linkUI();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayoutRight = (DrawerLayout) findViewById(R.id.drawer_layout_right);
        fabCheckIn = (FloatingActionButton) findViewById(R.id.fab_check_in);
        rl_scene = (RelativeLayout) views.get(R.id.rl_scene);
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        cv_marker_details = (CardView) views.get(R.id.cv_marker_details);
    }

    @Override
    protected void setData() {
        super.setData();

        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        MapFragment mapFragment = MapFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.map, mapFragment, "Map Activity").commit();
        mapFragment.getMapAsync(this);

        markerDetailsHeight = (int) MapActivity.this.getResources().getDimension(R.dimen.marker_details_height);
        views.get(R.id.layout_marker_details).animate().translationYBy(markerDetailsHeight).setListener(new AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                views.get(R.id.cv_marker_details).setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (!cameraSet) {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15);
                    map.animateCamera(cameraUpdate);
                    cameraSet = true;
                }
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                views.get(R.id.fab_check_in).animate().translationYBy(-markerDetailsHeight);
                views.get(R.id.layout_marker_details).animate().translationYBy(-markerDetailsHeight);
                return true;
            }
        });
    }

    @Override
    protected void setActions() {
        super.setActions();
        fabCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MapActivity.this, "Fab button", Toast.LENGTH_SHORT).show();
                if (slideState) {
                    drawerLayoutRight.closeDrawer(Gravity.RIGHT);
                    slideState = false;
                } else {
                    drawerLayoutRight.openDrawer(Gravity.RIGHT);
                    slideState = true;
                }
                searchByFilters(); // todo remove this
            }
        });
    }

    private void searchByFilters() {
        // accomodation -> ean api, otherwise -> places api
        map.clear();
        ArrayList<String> types = new ArrayList<>();
        HashMap<Integer, boolean[]> childCheckStates = listAdapter.getmChildCheckStates();
        for (int i = 1; i < listAdapter.getGroupCount(); i++) {
            boolean[] checkedArray = childCheckStates.get(i);
            if (checkedArray != null) {
                for (int j = 0; j < checkedArray.length; j++) {
                    if (checkedArray[j])
                        types.add((String) listAdapter.getChild(i, j));
                }
            }
        }
        Log.d("Debug", "Types count: " + types.size());
        RetrofitUtils.getPlacesApi().nearbySearch(null, types, new Callback<BaseResponse<PlacesResponse>>() {
            @Override
            public void success(BaseResponse<PlacesResponse> placesResponseBaseResponse, Response response) {
                ArrayList<PlacesResponse> results = placesResponseBaseResponse.getResults();
                MarkerOptions markerOptions;
                for (PlacesResponse result : results) {
                    markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng()));
                    markerOptions.title(result.getName());
                    String weekdayText = "Opening hours:";
                    OpeningHours openingHours = result.getOpening_hours();
                    if (openingHours != null && openingHours.getWeekday_text() != null) {
                        for (String s : openingHours.getWeekday_text()) {
                            weekdayText += "\n" + s;
                        }
//                        markerOptions.snippet(weekdayText);
                    }
                    map.addMarker(markerOptions);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Debug", "Filter request failire: " + error.getMessage());
            }
        });

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Accommodation");
        listDataHeader.add("Attractions & Culture");
        listDataHeader.add("Camping & RV");
        listDataHeader.add("Entertainment & Nightlife");
        listDataHeader.add("Food & Drink");
        listDataHeader.add("Outdoors & Recreation");
        listDataHeader.add("Points of Interest");
        listDataHeader.add("Services");
        listDataHeader.add("Shopping");
        listDataHeader.add("Sports");
        listDataHeader.add("Transport");

        // Adding child data
        List<String> accommodation = new ArrayList<String>();
        accommodation.add("All Inclusive");
        accommodation.add("Bed and Breakfast");
        accommodation.add("Hotel");
        accommodation.add("Resort");
        accommodation.add("Suite");
        accommodation.add("Vacation Rental");

        List<String> attractionsAndCulture = new ArrayList<String>();
        attractionsAndCulture.add("Amusement Parks");
        attractionsAndCulture.add("Aquariums");
        attractionsAndCulture.add("Art Gallery");
        attractionsAndCulture.add("Museums");
        attractionsAndCulture.add("Zoos");

        List<String> camping = new ArrayList<String>();
        camping.add("Campground");
        camping.add("RV Parks");

        List<String> entertainmentAndNightlife = new ArrayList<String>();
        entertainmentAndNightlife.add("Casinos");
        entertainmentAndNightlife.add("Night Clubs");
        entertainmentAndNightlife.add("Theaters");

        List<String> foodAndDrinks = new ArrayList<String>();
        foodAndDrinks.add("Bakery");
        foodAndDrinks.add("Bar");
        foodAndDrinks.add("Cafe");
        foodAndDrinks.add("Restaurants");
        foodAndDrinks.add("Meal Delivery");
        foodAndDrinks.add("Meal Takeaway");

        List<String> outdoors = new ArrayList<String>();
        outdoors.add("Parks");

        List<String> pointsOfInterest = new ArrayList<String>();
        pointsOfInterest.add("Cemetery");
        pointsOfInterest.add("Churches");
        pointsOfInterest.add("City Halls");
        pointsOfInterest.add("Mosques");
        pointsOfInterest.add("Synagogues");
        pointsOfInterest.add("Universities");
        pointsOfInterest.add("Schools");
        pointsOfInterest.add("Libraries");

        List<String> services = new ArrayList<String>();
        services.add("Airports");
        services.add("ATMs");
        services.add("Banks");
        services.add("Car Dealers");
        services.add("Car Rental");
        services.add("Car Repair");
        services.add("Car Wash");
        services.add("Dentist");
        services.add("Doctor");
        services.add("Fire Station");
        services.add("Gas Stations");
        services.add("Hospitals");
        services.add("Insurance Agency");
        services.add("Parking");
        services.add("Pharmacy");
        services.add("Police");
        services.add("Post Office");
        services.add("Travel Agency");

        List<String> shopping = new ArrayList<String>();
        shopping.add("Book Stores");
        shopping.add("Bicycle Stores");
        shopping.add("Clothing Stores");
        shopping.add("Electronics Stores");
        shopping.add("Furniture Stores");
        shopping.add("Grocery");
        shopping.add("Jewelry Stores");
        shopping.add("Lawyer");
        shopping.add("Liquor Stores");
        shopping.add("Shoe Stores");
        shopping.add("Shopping Malls");
        shopping.add("Supermarket");

        List<String> sports = new ArrayList<String>();
        sports.add("Bowling Alley");
        sports.add("Stadiums");

        List<String> transport = new ArrayList<String>();
        transport.add("Bus Station");
        transport.add("Subway Station");
        transport.add("Taxi Stand");
        transport.add("Train Station");

        listDataChild.put(listDataHeader.get(0), accommodation); // Header, Child data
        listDataChild.put(listDataHeader.get(1), attractionsAndCulture);
        listDataChild.put(listDataHeader.get(2), camping);
        listDataChild.put(listDataHeader.get(3), entertainmentAndNightlife);
        listDataChild.put(listDataHeader.get(4), foodAndDrinks);
        listDataChild.put(listDataHeader.get(5), outdoors);
        listDataChild.put(listDataHeader.get(6), pointsOfInterest);
        listDataChild.put(listDataHeader.get(7), services);
        listDataChild.put(listDataHeader.get(8), shopping);
        listDataChild.put(listDataHeader.get(9), sports);
        listDataChild.put(listDataHeader.get(10), transport);
    }


}
