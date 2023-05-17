package com.example.mealmovers_merchant.main.retrofit;



import com.example.mealmovers_merchant.main.models.OrderModel;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrdersApi {


    @GET("/order/get_orders_for_restaurant/{resId}")
    Call<List<OrderModel>> getNewOrders(@Path("resId") String id );
    @POST("/order/update_order_status/{orderId}/{status}")
    Call<OrderModel> updateOrderStatus(@Path("orderId") String orderId, @Path("status") String status );
}
