package com.example.mealmovers_merchant.main.models;

import java.io.Serializable;
import java.util.List;

public class RestaurantModel implements Serializable {

    String _id;
    String name;
    List<String> categories;
    String deliveryTime;
    String deliveryPrice;
    String minOrder;
    List<MenuItemModel> menu_items ;
    String image_url;


    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public List<MenuItemModel> getMenuItems() {
        return menu_items;
    }

    public void setMenuItems(List<MenuItemModel> menuItems) {
        this.menu_items = menuItems;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryCost() {
        return deliveryPrice;
    }

    public void setDeliveryCost(String deliveryCost) {
        this.deliveryPrice = deliveryCost;
    }


    public String getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(String minOrder) {
        this.minOrder = minOrder;
    }


}
