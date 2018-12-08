package com.example.ashleyhwang.groceryfy;

public class GroceryList {

    private String name ="";
    private boolean isChecked;
    private double price;

    public GroceryList(String name){this.name = name;}

    public GroceryList(String name, double price){
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
