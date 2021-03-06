package com.example.ashleyhwang.groceryfy.Groceries;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.ashleyhwang.groceryfy.DataModel.GroceryItem;
import com.example.ashleyhwang.groceryfy.ImageChooser;
import com.example.ashleyhwang.groceryfy.R;

import java.util.ArrayList;



//Adapter for Items in GroceryAddView
public class CustomAdapter extends ArrayAdapter<GroceryItem> {
    private Activity context;
    private int id;
    ArrayList<GroceryItem> list;
    private ImageChooser imgC;

    public CustomAdapter(Activity context, int resource, ArrayList<GroceryItem> objects){
        super(context, resource, objects);
        this.context = context;
        this.id = resource;
        this.list = objects;
        imgC = new ImageChooser();
    }

    public boolean isAnyItemChecked(ArrayList<GroceryItem> list){
        for (GroceryItem item: list){
            if (item.isChecked())
                    return true;
        }
        return false;
    }

    public interface OnCheckChangeListener{
        public void onCheckChange(boolean isChecked);
    }
    OnCheckChangeListener mOnCheckChangeListener;

    public void setOnDataChangeListener(OnCheckChangeListener onCheckChangeListener){
        mOnCheckChangeListener = onCheckChangeListener;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.grocery_addlist_row, null);
        }

        final GroceryItem item = list.get(position);
        TextView itemName = (TextView) convertView.findViewById(R.id.newGrocery);
        TextView amount = (TextView) convertView.findViewById(R.id.newAmount);
        TextView price = (TextView) convertView.findViewById(R.id.newPrice);
        ImageView pic  = convertView.findViewById(R.id.foodPic);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                item.setIsChecked(isChecked);
                if(mOnCheckChangeListener != null){
                    mOnCheckChangeListener.onCheckChange(isAnyItemChecked(list));
                }

            }
        });

        itemName.setText(item.getName());
        amount.setText("Amount: " + item.getAmount());
        price.setText("Price: " + item.getPrice());
        checkBox.setChecked(item.isChecked());
        pic.setImageResource(imgC.getFoodPic());

        return convertView;
    }
}
