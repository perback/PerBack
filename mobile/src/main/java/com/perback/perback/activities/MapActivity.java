package com.perback.perback.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.perback.perback.R;
import com.perback.perback.adapters.ExpandableListAdapter;
import com.perback.perback.x_base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends BaseActivity {

    private FloatingActionButton fabCheckIn;
    private DrawerLayout drawerLayout;
    private DrawerLayout drawerLayoutRight;
    private String[] navigationDrawerItemTitles;
    private ListView drawerList;
    private boolean slideState = false;

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

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

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
    }

    @Override
    protected void setData() {
        super.setData();
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }

    @Override
    protected void setActions() {
        super.setActions();
        fabCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MapActivity.this, "Fab button", Toast.LENGTH_SHORT).show();
                if(slideState){
                    drawerLayoutRight.closeDrawer(Gravity.RIGHT);
                    slideState = false;
                }else{
                    drawerLayoutRight.openDrawer(Gravity.RIGHT);
                    slideState = true;
                }
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
        pointsOfInterest.add("City Hall");
        pointsOfInterest.add("Mosque");
        pointsOfInterest.add("Synagogue");
        pointsOfInterest.add("University");

        List<String> services = new ArrayList<String>();
        services.add("Airports");
        services.add("ATMs");
        services.add("Banks");
        services.add("Car Dalers");
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
