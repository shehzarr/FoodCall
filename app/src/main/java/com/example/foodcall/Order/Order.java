package com.example.foodcall.Order;

import com.example.foodcall.Order.Item;

import java.util.ArrayList;
import java.util.List;

public class Order {
    String customer;
    String customer_name;
    String restaurant;
    String restaurant_name;
    String orderDate;
    String order_total;
    String order_address;
    List<Item> items = new ArrayList<>();

    public Order() {

    }

    public Order(String customer, String restaurantName, String orderDate, String order_total, List<Item> items) {
        this.customer = customer;
        this.restaurant = restaurantName;
        this.orderDate = orderDate;
        this.order_total = order_total;
        this.items = items;
    }

    public String getOrder_address() {
        return order_address;
    }

    public void setOrder_address(String order_address) {
        this.order_address = order_address;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    @Override
    public String toString() {
        return "Order{" +
                "customer='" + customer + '\'' +
                ", restaurant='" + restaurant + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", order_total='" + order_total + '\'' +
                ", items=" + items +
                '}';
    }

    public String getOrder_total() {
        return order_total;
    }

    public void setOrder_total(String order_total) {
        this.order_total = order_total;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurantName) {
        this.restaurant = restaurantName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
