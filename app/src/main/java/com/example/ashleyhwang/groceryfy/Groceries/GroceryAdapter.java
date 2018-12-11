package com.example.ashleyhwang.groceryfy.Groceries;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.ashleyhwang.groceryfy.DataModel.GroceryList;
import com.example.ashleyhwang.groceryfy.MainActivity;
import com.example.ashleyhwang.groceryfy.R;

import java.util.ArrayList;
import java.util.List;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.ViewHolder> implements CustomAdapter.OnCheckChangeListener{
    //list of grocery list names
    public static List<GroceryList> items;
    private static ItemClickListener mClickListener;
    Button mRemoveButton;
    Context mContext; //get the parent context for use in startActivity and to pass the deleteEQUID and Room.databasBuilders


    public GroceryAdapter(List<GroceryList> items){this.items = items;}


    @NonNull
    @Override
    public GroceryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        //create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grocery_list_row, null);

        //create ViewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int pos = position;

        holder.newGrocery.setText(items.get(pos).getName());
        holder.chkSelected.setChecked(items.get(pos).isChecked());
        holder.chkSelected.setTag(items.get(pos));

        holder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                GroceryList model = (GroceryList) cb.getTag();

                model.setChecked(cb.isChecked());
                items.get(pos).setChecked(cb.isChecked());
            }
        });

        holder.btn_edt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String clickedList = items.get(pos).getName();
                Bundle bundle = new Bundle();
                bundle.putString("clicked_list", clickedList);
                bundle.putString("name","");
                Intent addViewIntent = new Intent(mContext, GroceryAddView.class);
                addViewIntent.putExtras(bundle);
                mContext.startActivity(addViewIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onCheckChange(boolean isChecked) {
        if(isChecked){
            mRemoveButton.setVisibility(View.VISIBLE);
        } else { mRemoveButton.setVisibility(View.GONE);}
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView newGrocery;
        public Button btn_edt;
        public CheckBox chkSelected;
        public Button mRemoveButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            newGrocery = (TextView) itemView.findViewById(R.id.newGrocery);
            btn_edt = (Button) itemView.findViewById(R.id.editListBtn);
            chkSelected = (CheckBox) itemView.findViewById(R.id.checkbox);
            mRemoveButton = (Button) itemView.findViewById(R.id.removeList);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if(mClickListener != null ) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    void setClickListener(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }
    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }
}
