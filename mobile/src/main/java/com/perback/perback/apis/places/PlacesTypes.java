package com.perback.perback.apis.places;

import java.util.HashMap;

public class PlacesTypes {

    private static HashMap<String, String> placesTypes;

    private static void init() {
        if(placesTypes==null) {
            placesTypes = new HashMap<>();

            placesTypes.put("Amusement Parks", "amusement_park");
            placesTypes.put("Aquariums","aquarium");
            placesTypes.put("Art Gallery","art_gallery");
            placesTypes.put("Museums","museum");
            placesTypes.put("Zoos","zoo");

            placesTypes.put("Campground", "campground");
            placesTypes.put("RV Parks","rv_park");

            placesTypes.put("Casinos", "casino");
            placesTypes.put("Night Clubs","night_club");
            placesTypes.put("Theaters","movie_theater");

            placesTypes.put("Bakery", "bakery");
            placesTypes.put("Bar","bar");
            placesTypes.put("Cafe","cafe");
            placesTypes.put("Restaurants","restaurant");
            placesTypes.put("Meal Delivery","meal_delivery");
            placesTypes.put("Meal Takeaway","meal_takeaway");

            placesTypes.put("Parks", "park");

            placesTypes.put("Cemetery", "cemetery");
            placesTypes.put("Churches", "church");
            placesTypes.put("City Halls", "city_hall");
            placesTypes.put("Mosques", "mosque");
            placesTypes.put("Synagogues", "synagogue");
            placesTypes.put("Universities", "university");
            placesTypes.put("Schools", "school");
            placesTypes.put("Libraries","library");

            placesTypes.put("Airports", "airport");
            placesTypes.put("ATMs","atm");
            placesTypes.put("Banks","bank");
            placesTypes.put("Car Dealers","car_dealer");
            placesTypes.put("Car Rental","car_rental");
            placesTypes.put("Car Repair","car_repair");
            placesTypes.put("Car Wash","car_wash");
            placesTypes.put("Dentist","dentist");
            placesTypes.put("Doctor","doctor");
            placesTypes.put("Fire Station","fire_station");
            placesTypes.put("Gas Stations","gas_station");
            placesTypes.put("Hospitals","hospital");
            placesTypes.put("Insurance Agency","insurance_agency");
            placesTypes.put("Parking","parking");
            placesTypes.put("Pharmacy","pharmacy");
            placesTypes.put("Police","police");
            placesTypes.put("Post Office","post_office");
            placesTypes.put("Travel Agency","travel_agency");

            placesTypes.put("Book Stores", "book_store");
            placesTypes.put("Bicycle Stores","bicycle_store");
            placesTypes.put("Clothing Stores","clothing_store");
            placesTypes.put("Electronics Stores","electronics_store");
            placesTypes.put("Furniture Stores","furniture_store");
            placesTypes.put("Grocery","grocery_or_supermarket");
            placesTypes.put("Jewelry Stores","jewelry_store");
            placesTypes.put("Lawyer","lawyer");
            placesTypes.put("Liquor Stores", "liquor_store");
            placesTypes.put("Shoe Stores", "shoe_store");
            placesTypes.put("Shopping Malls","shopping_mall");
            placesTypes.put("Supermarket","grocery_or_supermarket");

            placesTypes.put("Bowling Alley","bowling_alley");
            placesTypes.put("Stadiums","stadium");

            placesTypes.put("Bus Station","bus_station");
            placesTypes.put("Subway Station","subway_station");
            placesTypes.put("Taxi Stand","taxi_stand");
            placesTypes.put("Train Station","train_station");


/*    accounting beauty_salon
     convenience_store courthouse department_store
    electrician  embassy establishment
    finance  florist food funeral_home
     general_contractor  gym
    hair_care hardware_store health hindu_temple home_goods_store
      laundry
    local_government_office locksmith lodging  movie_rental moving_company
    painter   pet_store  physiotherapist place_of_worship
    plumber   real_estate_agency roofing_contractor
    spa  storage store veterinary_care */


        }

    }

    public static String getId(String text) {
        init();
        return placesTypes.get(text);
    }





}
