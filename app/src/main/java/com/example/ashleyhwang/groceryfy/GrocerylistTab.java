package com.example.ashleyhwang.groceryfy;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.ArrayList;

public class GrocerylistTab extends Fragment implements OnClickListener{

    private static final String TAG = "GrocerylistTab";
    GroceryAdapter listsAdapter;
    private ArrayList<GroceryList> groceryLists;
    private RecyclerView groceryListsView;


    @Override
    public void onClick(View view) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
