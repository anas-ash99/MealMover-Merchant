package com.example.mealmovers_merchant.main;

import com.example.mealmovers_merchant.main.models.OrderModel;
import com.google.protobuf.Any;

public interface CallbackMethod<T> {
    void onDone(T results, Exception error);

}
