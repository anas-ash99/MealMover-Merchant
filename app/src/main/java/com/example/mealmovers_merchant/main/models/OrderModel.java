package com.example.mealmovers_merchant.main.models;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class OrderModel {
    String _id;
    String restaurant_id;
    String customer_name;
    String created_at;
    Double orderPrice;
    AddressModel address;
    String type;
    String status;
    List<MenuItemModel> items;
    String paymentStatus;
    String deliveryTime;
    LocalDateTime timeDelivery;
    Boolean isScheduled;

    public Boolean getScheduled() {
        return isScheduled;
    }

    public void setScheduled(Boolean scheduled) {
        isScheduled = scheduled;
    }

    public LocalDateTime getTimeDelivery() {
        return timeDelivery;
    }

    public void setTimeDelivery(LocalDateTime timeDelivery) {
        this.timeDelivery = timeDelivery;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public OrderModel(String _id, String restaurant_id, String customer_name, Double orderPrice, AddressModel address, String type, String status, List<MenuItemModel> items) {
        this._id = _id;
        this.restaurant_id = restaurant_id;
        this.customer_name = customer_name;
        this.orderPrice = orderPrice;
        this.address = address;
        this.type = type;
        this.status = status;
        this.items = items;
    }

    public OrderModel() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }


    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


    public Double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MenuItemModel> getItems() {
        return items;
    }

    public void setItems(List<MenuItemModel> items) {
        this.items = items;
    }
}
