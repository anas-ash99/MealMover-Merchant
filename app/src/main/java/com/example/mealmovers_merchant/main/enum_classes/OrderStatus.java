package com.example.mealmovers_merchant.main.enum_classes;

public enum OrderStatus {

    NEW("new"),
    KITCHEN("kitchen"),
    BEING_DELIVERED("being_delivered"),
    DELIVERED("delivered"),
    NOT_CONFIRMED("not_confirmed");

    final String value;
    OrderStatus(String value) {
        this.value = value;
    }
}
