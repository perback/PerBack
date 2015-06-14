package com.perback.perback.apis.ean;

import java.util.ArrayList;

public class HotelListOptions {

    public enum HotelSortOption {

        PRICE_ASC("PRICE"),
        PRICE_DESC("PRICE_REVERSE"),
        RATING_ASC("QUALITY"),
        RATING_DESC("QUALITY_REVERSE"),
        ALPHABETICAL("ALPHA"),
        PROXIMITY("PROXIMITY");

        private final String value;

        private HotelSortOption(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }

    public enum HotelCategory {

        HOTEL(1),
        SUITE(2),
        RESORT(3),
        VACATION_RENTAL(4),
        BED_AND_BREAKFAST(5),
        ALL_INCLUSIVE(6);

        private final int value;

        private HotelCategory(final int newValue) {
            value = newValue;
        }

        public int getValue() {
            return value;
        }
    }

    public enum HotelAmenity {

        BUSINESS_CENTER(1),
        FITNESS_CENTER(2),
        HOT_TUB_ON_SITE(4),
        INTERNET_ACCESS_AVAILABLE(8),
        KIDS_ACTIVITIES(16),
        KITCHEN_OR_KITCHENETTE(32),
        PETS_ALLOWED(64),
        POOL(128),
        RESTAURANT_ON_SITE(256),
        SPA_ON_SITE(512),
        WHIRLPOOL_BATH(1024),
        BREAKFAST(2048),
        BABYSITTING(4096),
        JACUZZI(8192),
        PARKING(16384),
        ROOM_SERVICE(32768),
        ACCESSIBLE_PATH_OF_TRAVEL(65536),
        ACCESSIBLE_BATHROOM(131072),
        ROLL_IN_SHOWER(262144),
        HANDICAPPED_PARKING(524288),
        IN_ROOM_ACCESSIBILITY(1048576),
        ACCESSIBILITY_EQUIPMENT_FOR_DEAF(2097152),
        BRAILLE_OR_RAISED_SIGNAGE(4194304),
        FREE_AIRPORT_SHUTTLE(8388608),
        INDOOR_POOL(16777216),
        OUTDOOR_POOL(33554432),
        EXTENDED_PARKING(67108864),
        FREE_PARKING(134217728);

        private final long value;

        private HotelAmenity(final long newValue) {
            value = newValue;
        }

        public long getValue() {
            return value;
        }
    }

    public static ArrayList<HotelSummary> filterByAmenity(ArrayList<HotelSummary> hotels, HotelAmenity[] amenities) {
        ArrayList<HotelSummary> result = new ArrayList<>();
        for(HotelSummary hotelSummary : hotels) {
            long amenityMask = hotelSummary.getAmenityMask();
            boolean hasAllAmenities = true;
            if(amenities.length>0) {
                long filterMask = amenities[0].getValue();
                for (HotelAmenity amenity : amenities) {
                    filterMask = filterMask | amenity.getValue();
                }
                hasAllAmenities = (amenityMask & filterMask) == filterMask;
            }

            if(hasAllAmenities)
                result.add(hotelSummary);
        }
        return result;
    }

    public static boolean hasAmenity(long amenityMask, HotelAmenity amenity) {
        return ((amenityMask & amenity.getValue()) == amenity.getValue());
    }

}
