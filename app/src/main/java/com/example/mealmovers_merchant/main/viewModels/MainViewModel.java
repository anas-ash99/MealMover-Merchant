package com.example.mealmovers_merchant.main.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mealmovers_merchant.main.models.OrderModel;
import com.example.mealmovers_merchant.main.repositories.OrdersRepo;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

    List<OrderModel> todaysOrder = new ArrayList<>();
    List<OrderModel> newOrders = new ArrayList<>();
    List<OrderModel> kitchenOrders = new ArrayList<>();
    List<OrderModel> beingDeliveredOrders = new ArrayList<>();
    List<OrderModel> deliveredOrders = new ArrayList<>();
    List<OrderModel> notConfirmedOrders = new ArrayList<>();



    public List<OrderModel> getTodaysOrder() {
        return todaysOrder;
    }


    public void setTodaysOrder(List<OrderModel> todaysOrder) {

        List<OrderModel> emptyList1 = new ArrayList<>();
        List<OrderModel> emptyList2 = new ArrayList<>();
        List<OrderModel> emptyList3 = new ArrayList<>();
        List<OrderModel> emptyList4 = new ArrayList<>();
        List<OrderModel> emptyList5 = new ArrayList<>();
            this.deliveredOrders = emptyList1;
            this.newOrders = emptyList2;
            this.kitchenOrders = emptyList3;
            this.beingDeliveredOrders = emptyList4;
            this.notConfirmedOrders = emptyList5;

            for (OrderModel order : todaysOrder) {

                if (order.getStatus().equals("confirmed") ||order.getStatus().equals("new") ) {
                    newOrders.add(order);
                } else if (order.getStatus().equals("kitchen")) {
                    kitchenOrders.add(order);
                } else if (order.getStatus().equals("being delivered")) {
                    beingDeliveredOrders.add(order);
                } else if (order.getStatus().equals("delivered")) {
                    deliveredOrders.add(order);
                } else if (order.getStatus().equals("received")) {
                    notConfirmedOrders.add(order);
                }
            }
//        }




    }

    public void handleOrderClick(OrderModel order){
       switch (order.getStatus()){
           case "received":
               this.newOrders.remove(order);
               this.kitchenOrders.add(order);
               break;
           case "kitchen":
               this.kitchenOrders.remove(order);
               this.beingDeliveredOrders.add(order);
               break;
       }


    }

    public void addOrder(OrderModel order) {

        newOrders.add(0, order);
    }
    public List<OrderModel> getNotConfirmedOrders() {
        return notConfirmedOrders;
    }
    public List<OrderModel> getNewOrders() {
        return newOrders;
    }

    public List<OrderModel> getKitchenOrders() {
        return kitchenOrders;
    }

    public List<OrderModel> getBeingDeliveredOrders() {
        return beingDeliveredOrders;
    }

    public List<OrderModel> getDeliveredOrders() {
        return deliveredOrders;
    }

    public void setTodaysOrderToNull() {
        List<OrderModel> emptyList = new ArrayList<>();
        this.todaysOrder = emptyList;
    }

    public void setNotConfirmedOrders(List<OrderModel> notConfirmedOrders) {
        this.notConfirmedOrders = notConfirmedOrders;
    }

    public void setNewOrders(List<OrderModel> newOrders) {
        this.newOrders = newOrders;
    }

    public void setKitchenOrders(List<OrderModel> kitchenOrders) {
        this.kitchenOrders = kitchenOrders;
    }

    public void setBeingDeliveredOrders(List<OrderModel> beingDeliveredOrders) {
        this.beingDeliveredOrders = beingDeliveredOrders;
    }

    public void setDeliveredOrders(List<OrderModel> deliveredOrders) {
        this.deliveredOrders = deliveredOrders;
    }
}
