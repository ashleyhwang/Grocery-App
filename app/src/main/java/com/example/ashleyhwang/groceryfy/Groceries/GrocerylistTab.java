package com.example.ashleyhwang.groceryfy.Groceries;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.ashleyhwang.groceryfy.DataModel.GroceryList;
import com.example.ashleyhwang.groceryfy.Groceries.GroceryAdapter;
import com.example.ashleyhwang.groceryfy.R;

import java.util.ArrayList;

public class GrocerylistTab extends Fragment implements OnClickListener{

    private static final String TAG = "GrocerylistTab";
    private GroceryAdapter listsAdapter;
    private ArrayList<GroceryList> groceryLists;

    private RecyclerView groceryListsView;
    private FloatingActionButton mRemoveButton;


    @Override
    public void onClick(View view) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.activity_grocerylist_tab, container, false);
        Resources res = getResources();

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
