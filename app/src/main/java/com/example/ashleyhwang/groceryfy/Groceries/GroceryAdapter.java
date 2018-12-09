package com.example.ashleyhwang.groceryfy.Groceries;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.ashleyhwang.groceryfy.DataModel.GroceryList;

import java.util.ArrayList;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.ViewHolder>{

    public static ArrayList<GroceryList> item_list;

//    public GroceryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,)

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final int pos = position;

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


        }

        @Override
        public void onClick(View view) {

        }
    }
}
