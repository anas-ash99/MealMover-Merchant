package com.example.mealmovers_merchant.main.retrofit;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit restaurantRetrofit, addressRetrofit, orderRetrofit = null;
    private static String baseUrl = "https://mealmovers-customer.herokuapp.com/";
    private static String addressUrl = "https://api.geoapify.com";

//http://10.0.2.2:5000/

//https://mealmovers-customer.herokuapp.com/

//    public static RestaurantApi restaurantApi (){
//        if (restaurantRetrofit == null){
//            restaurantRetrofit = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        }
//        return restaurantRetrofit.create(RestaurantApi.class);
//    }


    public static OrdersApi ordersApi (){
        if (orderRetrofit == null){
            orderRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return orderRetrofit.create(OrdersApi.class);
    }






}


