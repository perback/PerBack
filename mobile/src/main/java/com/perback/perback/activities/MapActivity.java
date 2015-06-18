package com.perback.perback.activities;

import android.animation.Animator;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionValues;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.perback.perback.R;
import com.perback.perback.adapters.ExpandableListAdapter;
import com.perback.perback.apis.places.BaseResponse;
import com.perback.perback.apis.places.OpeningHours;
import com.perback.perback.apis.places.PlaceDetailsResponse;
import com.perback.perback.apis.places.PlacesResponse;
import com.perback.perback.dao.Dao;
import com.perback.perback.holders.TripPoint;
import com.perback.perback.utils.AnimationEndListener;
import com.perback.perback.utils.PicassoUtils;
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
    protected HashMap<Marker, Object> markerTags;
    protected boolean markerDetailsVisible = false;

    private RelativeLayout rlOpenFilterMenu;
    private RelativeLayout rlRemoveMarkers;


    @Override
    protected int getLayoutResId() {
        return R.layout.map_activity;
    }

    public static void launch(BaseActivity activity) {
        activity.startActivity(new Intent(activity, MapActivity.class));
    }

    @Override
    protected void init() {
        super.init();
        markerTags = new HashMap<>();
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
        rlOpenFilterMenu = (RelativeLayout) views.get(R.id.rl_open_filter_menu);
        rlRemoveMarkers = (RelativeLayout) views.get(R.id.rl_remove_markers);
    }

    @Override
    protected void setData() {
        super.setData();

//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayoutRight, (Toolbar) views.get(R.id.toolbar), R.string.open_drawer, R.string.close_drawer) {
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//            }
//
//
//        }; // Drawer Toggle Object Made
//        drawerLayoutRight.setDrawerListener(actionBarDrawerToggle); // Drawer Listener set to the Drawer toggle
//        actionBarDrawerToggle.syncState();

        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        MapFragment mapFragment = MapFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.map, mapFragment, "Map Activity").commit();
        mapFragment.getMapAsync(this);

        markerDetailsHeight = (int) MapActivity.this.getResources().getDimension(R.dimen.marker_details_height);
        markerDetailsHeight += MapActivity.this.getResources().getDimension(R.dimen.activity_margin) + 2;
        views.get(R.id.layout_marker_details).animate().translationYBy(markerDetailsHeight).setListener(new AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                views.get(R.id.layout_marker_details).setVisibility(View.VISIBLE);
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

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (markerDetailsVisible)
                    hideDetails();
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final PlacesResponse placeData = (PlacesResponse) markerTags.get(marker);
                if (placeData != null) {
                    if (markerDetailsVisible)
                        hideDetails(new AnimationEndListener() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                setMarkerData(placeData);
                            }
                        });
                    else {
                        setMarkerData(placeData);
                    }
                    return true;
                } else {
                    return false;
                }

            }
        });

        map.getUiSettings().setMapToolbarEnabled(false);

    }

    public void hideDetails() {
        hideDetails(null);
    }

    public void hideDetails(AnimationEndListener listener) {
        views.get(R.id.fab_check_in).animate().translationYBy(markerDetailsHeight);
        ViewPropertyAnimator vpa = views.get(R.id.layout_marker_details).animate().translationYBy(markerDetailsHeight);
//        if(listener!=null) {
//            vpa.setListener(listener);
//        }
        markerDetailsVisible = false;

    }

    public void showDetails() {
        showDetails(null);
    }

    public void showDetails(AnimationEndListener listener) {
        views.get(R.id.fab_check_in).animate().translationYBy(-markerDetailsHeight);
        ViewPropertyAnimator vpa = views.get(R.id.layout_marker_details).animate().translationYBy(-markerDetailsHeight);
//        if(listener!=null)
//            vpa.setListener(listener);
        markerDetailsVisible = true;
    }

    protected void setMarkerData(final PlacesResponse markerData) {

        RetrofitUtils.getPlacesApi().getPlaceDetails(markerData.getPlace_id(), new Callback<BaseResponse<PlaceDetailsResponse>>() {
            @Override
            public void success(BaseResponse<PlaceDetailsResponse> placeDetailsResponseBaseResponse, Response response) {
                if (placeDetailsResponseBaseResponse.isSucces()) {
                    PlaceDetailsResponse placeDetailsResponse = placeDetailsResponseBaseResponse.getResult();
                    views.getTextView(R.id.tv_place_title).setText(placeDetailsResponse.getName());
                    views.getTextView(R.id.tv_address).setText(placeDetailsResponse.getFormatted_address());
                    ((RatingBar) views.get(R.id.rb_rating)).setNumStars(5);
                    ((RatingBar) views.get(R.id.rb_rating)).setRating((float) placeDetailsResponse.getRating());
                    if (markerData.getPhotos() != null && markerData.getPhotos().size() > 0)
                        PicassoUtils.loadPlacePhoto(markerData.getPhotos().get(0).getPhoto_reference(), views.getImageView(R.id.iv_place_photo));
                    else {
                        PicassoUtils.loadPlacePhoto(null, views.getImageView(R.id.iv_place_photo));
                    }
                    views.get(R.id.layout_marker_details).setTag(placeDetailsResponse);
                    views.get(R.id.layout_marker_details).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PlaceDetailsActivity.launch(MapActivity.this, (PlaceDetailsResponse) v.getTag());
                        }
                    });
                    views.get(R.id.iv_directions).setTag(markerData);
                    views.get(R.id.iv_directions).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PlacesResponse placesResponse = (PlacesResponse) v.getTag();
                            TripPoint currentLocation = Dao.getInstance().readLocation();
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(
                                    "http://maps.google.com/maps?saddr=" + currentLocation.getLat() + ", " + currentLocation.getLng()
                                            + "&daddr=" + placesResponse.getGeometry().getLocation().getLat() + ", "
                                            + placesResponse.getGeometry().getLocation().getLng()));
                            startActivity(intent);
                        }
                    });
                    showDetails();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Debug", "Places details failure");
            }
        });
    }

    @Override
    protected void setActions() {
        super.setActions();
        fabCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchByFilters(); // todo remove this
            }
        });

        rlOpenFilterMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (slideState) {
                    drawerLayoutRight.closeDrawer(Gravity.RIGHT);
                    slideState = false;
                } else {
                    drawerLayoutRight.openDrawer(Gravity.RIGHT);
                    slideState = true;
                }
            }
        });

        drawerLayoutRight.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                slideState = true;
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                slideState = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        rlRemoveMarkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(MapActivity.this)
                        .title("Remove markers")
                        .content("Are you sure that you want to remove all markers from map?")
                        .positiveText("Yes")
                        .negativeText("Cancel")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                map.clear();
                                rlRemoveMarkers.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                            }
                        })
                        .show();
            }
        });
    }

    private void searchByFilters() {
        // accomodation -> ean api, otherwise -> places api
        map.clear();
        ArrayList<String> types = new ArrayList<>();
        HashMap<Integer, boolean[]> childCheckStates = listAdapter.getmChildCheckStates();
        for (int i = 1; i < listAdapter.getGroupCount()-1; i++) {
            boolean[] checkedArray = childCheckStates.get(i);
            if (checkedArray != null) {
                for (int j = 0; j < checkedArray.length; j++) {
                    if (checkedArray[j]) {
                        try {
                            types.add((String) listAdapter.getChild(i, j));
                        } catch (Exception e) {
                            Log.e("Debug", "Exception: "+e.getMessage());
                        }

                    }
                }
            }
        }
        Log.d("Debug", "Types count: " + types.size());
        RetrofitUtils.getPlacesApi().nearbySearch(null, types, new Callback<BaseResponse<PlacesResponse>>() {
            @Override
            public void success(BaseResponse<PlacesResponse> placesResponseBaseResponse, Response response) {
                ArrayList<PlacesResponse> results = placesResponseBaseResponse.getResults();
                MarkerOptions markerOptions;

                if (results.size() > 0) {
                    rlRemoveMarkers.setVisibility(View.VISIBLE);
                }

                for (PlacesResponse result : results) {
                    markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng()));
                    markerOptions.title(result.getName());

                    for (int i = 0; i < result.getTypes().size(); i++) {
                        if (result.getTypes().get(i).equals("amusement_park") || result.getTypes().get(i).equals("aquarium") || result.getTypes().get(i).equals("art_gallery") || result.getTypes().get(i).equals("museum") || result.getTypes().get(i).equals("zoo")) {
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        }
                        if (result.getTypes().get(i).equals("airport") || result.getTypes().get(i).equals("atm") || result.getTypes().get(i).equals("bank") || result.getTypes().get(i).equals("car_dealer") || result.getTypes().get(i).equals("car_rental") || result.getTypes().get(i).equals("car_repair") || result.getTypes().get(i).equals("car_wash") || result.getTypes().get(i).equals("dentist") || result.getTypes().get(i).equals("doctor") || result.getTypes().get(i).equals("fire_station") || result.getTypes().get(i).equals("gas_station") || result.getTypes().get(i).equals("hospital") || result.getTypes().get(i).equals("insurance_agency") || result.getTypes().get(i).equals("parking") || result.getTypes().get(i).equals("pharmacy") || result.getTypes().get(i).equals("police") || result.getTypes().get(i).equals("post_office") || result.getTypes().get(i).equals("travel_agency")) {
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        }
                        if (result.getTypes().get(i).equals("campground") || result.getTypes().get(i).equals("rv_park")) {
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        }
                        if (result.getTypes().get(i).equals("casino") || result.getTypes().get(i).equals("night_club") || result.getTypes().get(i).equals("movie_theater")) {
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        }
                        if (result.getTypes().get(i).equals("bakery") || result.getTypes().get(i).equals("bar") || result.getTypes().get(i).equals("cafe") ||
                                result.getTypes().get(i).equals("restaurant") || result.getTypes().get(i).equals("meal_delivery") || result.getTypes().get(i).equals("meal_takeaway")) {
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                        }
                        if (result.getTypes().get(i).equals("park")) {
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                        }
                        if (result.getTypes().get(i).equals("cemetery") || result.getTypes().get(i).equals("church") || result.getTypes().get(i).equals("city_hall") ||
                                result.getTypes().get(i).equals("mosque") || result.getTypes().get(i).equals("synagogue") || result.getTypes().get(i).equals("university")
                                || result.getTypes().get(i).equals("school") || result.getTypes().get(i).equals("library")) {
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                        }
                        if (result.getTypes().get(i).equals("book_store") || result.getTypes().get(i).equals("bicycle_store") || result.getTypes().get(i).equals("clothing_store") ||
                                result.getTypes().get(i).equals("electronics_store") || result.getTypes().get(i).equals("grocery_or_supermarket") || result.getTypes().get(i).equals("jewelry_store")
                                || result.getTypes().get(i).equals("furniture_store") || result.getTypes().get(i).equals("lawyer")
                                || result.getTypes().get(i).equals("liquor_store") || result.getTypes().get(i).equals("shoe_store")
                                || result.getTypes().get(i).equals("shopping_mall")) {
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                        }

                        if (result.getTypes().get(i).equals("bowling_alley") || result.getTypes().get(i).equals("stadium")) {
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                        }

                        if (result.getTypes().get(i).equals("bus_station") || result.getTypes().get(i).equals("taxi_stand") ||
                                result.getTypes().get(i).equals("subway_station") || result.getTypes().get(i).equals("train_station")) {
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        }
                    }

                    String weekdayText = "Opening hours:";
                    OpeningHours openingHours = result.getOpening_hours();
                    if (openingHours != null && openingHours.getWeekday_text() != null) {
                        for (String s : openingHours.getWeekday_text()) {
                            weekdayText += "\n" + s;
                        }
//                        markerOptions.snippet(weekdayText);
                    }
                    markerTags.put(map.addMarker(markerOptions), result);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Debug", "Filter request failire: " + error.getMessage());
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (markerDetailsVisible) {
            hideDetails();
        } else
            super.onBackPressed();
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

    public void addMarkerOnMap(double latitude, double longitude, String title) {
        MarkerOptions markerOptions;
        markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(latitude, longitude));
        markerOptions.title(title);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        map.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
        map.animateCamera(cameraUpdate);

    }


}
