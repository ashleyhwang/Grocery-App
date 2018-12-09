package com.example.ashleyhwang.groceryfy.DataModel;

public class Recipe {
    private int id;
    private String Dishname;

    public Recipe(int id, String Dishname){
        this.id = id;
        this.Dishname = Dishname;
    }
    public String getInstruc() {
        return instruc;
    }

    public void setInstruc(String instruc) {
        this.instruc = instruc;
    }

    private String instruc;




    public String getDishname(){
        return Dishname;
    }

    public int getId() {
        return id;
    }

}
