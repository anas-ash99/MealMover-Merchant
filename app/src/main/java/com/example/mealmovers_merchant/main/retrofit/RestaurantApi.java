package com.example.mealmovers_merchant.main.retrofit;

import com.example.mealmovers_merchant.main.models.OrderModel;
import com.example.mealmovers_merchant.main.models.RestaurantModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestaurantApi {



    @GET("/restaurants/get_restaurant_by_id/{resId}")
    Call<RestaurantModel> getRestaurant(@Path("resId") String resId );


}
