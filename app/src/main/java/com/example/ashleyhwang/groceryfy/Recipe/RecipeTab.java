package com.example.ashleyhwang.groceryfy.Recipe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashleyhwang.groceryfy.DataModel.*;
import com.example.ashleyhwang.groceryfy.*;

import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;

public class RecipeTab extends Fragment {

    JSONObject jsonOjbect;
    JSONArray jsonArray;
    ArrayList<Recipe> recipes;
    private RecyclerView mRecyclerView;
    private RecipeAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView loading;
    private ProgressBar progressBar;
    private FloatingActionButton fab;
    private String dishname;
    View rootView;
    private Dialog mDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.activity_recipe_tab, container, false);
        setRetainInstance(true); //Retain this fragment on configuration changes;

        progressBar = rootView.findViewById(R.id.progressBar);

        loading = (TextView) rootView.findViewById(R.id.textView2);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view); //note this need

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); //note this need

        // Download JSON file AsyncTask, note how i pass parameter for the progressBar to AsyncTask constructor
        //new DownloadJSON(progressBar).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        //searching for a recipe for your dish / creating a recyclerview from there.
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                makeDialog();
            }
        });
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        //if (visible && isResumed())
        if (visible)
        {
            //Only manually call onVisible if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            //onVisible();
        }
    }

//    @Override
//    public void onResume()
//    {
//        super.onResume();
//        //if (!getUserVisibleHint())
//        //{
//        //    return;
//        //}
//        new DownloadJSON(progressBar).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//    }


    private void makeDialog(){
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    final EditText edtxt = new EditText(getContext());
    builder.setMessage("Type the name of dish you want to look for!").setTitle("Search Recipe").setView(edtxt);

    // searching for a recipe
    builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dishname = edtxt.getText().toString();

        }
    });

    //cancel searching for a dish
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            mDialog.cancel();
        }
    });
    AlertDialog dialog = builder.create();
    mDialog = dialog;
    dialog.show();
    }

    public class DownloadJSON extends AsyncTask<String, Integer, Void>{
        ProgressBar bar;

        public DownloadJSON(ProgressBar bar){this.bar = bar;}

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            bar.setMax(100);

        }
        @Override
        protected Void doInBackground(String... strings) {
            return null;
        }
    }

}


