package com.example.ashleyhwang.groceryfy;

import java.io.Serializable;

public class GroceryStore implements Serializable {
    public double lat;
    public double lng;
    public int id;
    public String storeName;

    public GroceryStore(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }


}
