package com.example.mealmovers_merchant.main;

import com.example.mealmovers_merchant.main.models.RestaurantModel;

public class DataHolder {
    private static RestaurantModel loggedInRestaurant;

    public static RestaurantModel getLoggedInRestaurant() {
        return loggedInRestaurant;
    }

    public static void setLoggedInRestaurant(RestaurantModel loggedInRestaurant) {
        DataHolder.loggedInRestaurant = loggedInRestaurant;
    }
}
