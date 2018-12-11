package com.example.ashleyhwang.groceryfy.DataModel;

import java.io.Serializable;

public class Recipe implements Serializable {
    private int id;
    private String Dishname;
    private String instruc;

    public int getReadyTime() {
        return readyTime;
    }

    public void setReadyTime(int readyTime) {
        this.readyTime = readyTime;
    }

    private int readyTime;

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    private int servings;

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    private String sourceUrl;


//    public Recipe(int id, String Dishname){
//        this.id = id;
//        this.Dishname = Dishname;
//    }

    public String getInstruc() {
        return instruc;
    }

    public void setInstruc(String instruc) {
        this.instruc = instruc;
    }

    public String getDishname(){
        return Dishname;
    }

    public void setDishname(String dishname){this.Dishname= dishname;}

    public int getId() {
        return id;
    }

    public void setId(int id ){this.id = id;}

}
