package com.example.ashleyhwang.groceryfy.Groceries;

import android.content.DialogInterface;
import android.content.Intent;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.ashleyhwang.groceryfy.DataModel.GroceryList;
import com.example.ashleyhwang.groceryfy.DatabaseHelper;
import com.example.ashleyhwang.groceryfy.R;

import java.util.ArrayList;

public class GrocerylistTab extends Fragment implements OnClickListener{

    private static final String TAG = "GrocerylistTab";
    private GroceryAdapter mAdapter;
    private ArrayList<GroceryList> groceryLists;
    DatabaseHelper db;
    private RecyclerView groceryrv;
    private FloatingActionButton mRemoveButton;

    @Override
    public void onClick(View view) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_grocerylist_tab, container, false);
        setHasOptionsMenu(true);
        db = new DatabaseHelper(getContext());
        mRemoveButton = rootView.findViewById(R.id.removeList);

        //Returning all the groceryListNames from db, assigning them to ArrayList
        ArrayList<String> stringNames = db.getAllGroceryListNames();

        //Initializing groceryLists, from the returned names
        groceryLists = new ArrayList<>();
        for(String i: stringNames){
            GroceryList groceryList = new GroceryList(i, db.getGroceryListPrice(i));

            if(groceryList.getName().equals("fridge")){//Don't show fridge
                continue;
            }
            groceryLists.add(0, groceryList);
        }

        final ArrayList<String> listNames = db.getAllGroceryListNames();

        //Setting up adapter with groceryLists

        // 1. get a reference to recyclerview
        groceryrv = rootView.findViewById(R.id.recyclerview);
        // 2. set layoutManager
        groceryrv.setLayoutManager(new LinearLayoutManager(getActivity()));


        // 3. create an adapter
        mAdapter = new GroceryAdapter(groceryLists);
        groceryrv.setHasFixedSize(true);
        groceryrv.setLayoutManager(new LinearLayoutManager(getContext()));

        groceryrv.setAdapter(mAdapter);


////TODO making the delete button popup
//
//        CustomAdapter.setOnDataChangeListener(new CustomAdapter.OnCheckChangeListener() {
//            @Override
//            public void onCheckChange(boolean isChecked) {
//                if (isChecked && CustomAdapter.isAnyItemChecked(groceryLists)){
//                    mRemoveButton.setVisibility(View.VISIBLE);
//                }
//                else{
//                    mRemoveButton.setVisibility(View.GONE);
//                }
//            }
//        });

        mAdapter.notifyDataSetChanged();

        //mRemoveButton
        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (groceryLists.size() > 0){
                    for (int i = 0; i < groceryLists.size(); i++) {
                        if (i > groceryLists.size()) {
                            break;
                        }
                        if (groceryLists.get(i).isChecked()) {
                            db.destroyGrocery_List(groceryLists.get(i).getName());
                            db.closeDB();
                            groceryLists.remove(i);

                        }
                    }
                }
            }
        });

        //necessary for creating a new grocery list
        FloatingActionButton newListButton = rootView.findViewById(R.id.fab);
        newListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Create a new list");
                builder.setMessage("Give a name for your grocery list.");
                final EditText et = new EditText(getContext());
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(et);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String inputName = et.getText().toString();
                        if (inputName.equals("") || listNames.contains(inputName)) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                            builder1.setTitle("Error");
                            builder1.setMessage("Give an unique name.");
                            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            builder1.show();


                        } else {
                            final String groceryListName = et.getText().toString();
                            Intent groceryAddIntent = new Intent(getContext(), GroceryAddView.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("name", groceryListName);
                            bundle.putString("clicked_list", "");
                            groceryAddIntent.putExtras(bundle);
                            startActivity(groceryAddIntent);
                        }
                    }
                });


                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
        return rootView;
    }
}
