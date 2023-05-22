package com.example.mealmovers_merchant.main.use_cases.firebase_case;

public interface ObserveCallBack<T> {
    void onNewOrders(T results);
}
