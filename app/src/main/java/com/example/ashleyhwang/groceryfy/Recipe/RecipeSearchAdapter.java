package com.example.ashleyhwang.groceryfy.Recipe;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashleyhwang.groceryfy.Groceries.GroceryAddView;
import com.example.ashleyhwang.groceryfy.MainActivity;
import com.example.ashleyhwang.groceryfy.R;
import com.example.ashleyhwang.groceryfy.DataModel.Recipe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecipeSearchAdapter extends RecyclerView.Adapter<RecipeSearchAdapter.ViewHolder> {
    Context mContext;
    List<Recipe> items;
    //AppDatabase db;
    Recipe rec;

    private static final String TAG = "RecipeSearchAdapter";
    private static final String BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/";
    private static final String INFO = "/information";

    public RecipeSearchAdapter(List<Recipe> items){this.items = items;}

    @NonNull
    @Override
    public RecipeSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_search_result_row, parent, false);
        mContext = parent.getContext();
        ViewHolder viewHolder = new ViewHolder(view);
//        db = Room.databaseBuilder(mContext, AppDatabase.class,AppDatabase.NAME).build();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeSearchAdapter.ViewHolder holder, int position) {
        Recipe recipe = items.get(position);
        String recipeText = (recipe.getDishname());
        holder.dishname.setText(recipeText);

        final int index = position;
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                rec = items.get(index);
                new IngredientsTask().execute(rec.getId());
            }
        });

    }

    private class IngredientsTask extends AsyncTask<Integer, Void, String> {
        int recID;
        @Override
        protected String doInBackground(Integer... params) {
            recID= params[0];
            String queryString = BASE_URL + params[0]+INFO;
            // Set up variables for the try block that need to be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String recipeJSON = null;
            try {

                URL requestURL = new URL(queryString);

                // Open the network connection.
                urlConnection = (HttpURLConnection) requestURL.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.addRequestProperty("X-Mashape-Key","aU7wK5hcDomsh6uME0hMyoJThpJap1zeC7TjsnbayUFKEnJMY8");
                urlConnection.connect();

                // Get the InputStream.
                InputStream inputStream = urlConnection.getInputStream();

                // Read the response string into a StringBuilder.
                StringBuilder builder = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // but it does make debugging a *lot* easier if you print out the completed buffer for debugging.
                    builder.append(line + "\n");
                    publishProgress();
                }

                if (builder.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    // return null;
                    return null;
                }
                recipeJSON = builder.toString();

                // Catch errors.
            } catch (IOException e) {
                e.printStackTrace();

                // Close the connections.
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            // Return the raw response.
            Log.d(TAG, "doInBackground: search "+recipeJSON);
            return recipeJSON;
        }

        @Override
        protected void onPreExecute(){super.onPreExecute();}

        @Override
        protected void onPostExecute(String s){
            items = new ArrayList<>();
            try{
                //1.
//                JSONArray jsonArray = new JSONArray(s);
//                JSONObject jsonObject = jsonArray.getJSONObject(0);
//                Log.d(TAG, "onPostExecute: BLAHBLAH" +jsonObject);
                // Convert the response into a JSON object.
                JSONObject jsonObject = new JSONObject(s); //get top level object
                Log.d(TAG, "onPostExecute: BLAHBLAH" +jsonObject);
                // Get the JSONArray of book items.
//                JSONArray itemsArray = jsonObject.getJSONArray("results"); //array of earthquakes objects
//                JSONArray jsonArray = jsonObject.toJSONArray();


                Recipe rec1 = new Recipe();
                rec1.setId(jsonObject.getInt("id"));
                rec1.setReadyTime(jsonObject.getInt("readyInMinutes"));
                rec1.setServings(jsonObject.getInt("servings"));
                rec1.setDishname(jsonObject.getString("title"));
                rec1.setInstruc(jsonObject.getString("instructions"));
                rec1.setSourceUrl(jsonObject.getString("sourceUrl"));
                showDialog(rec1);
                Log.d(TAG, "onPostExecute: "+ArrayToString(rec));
//                new LoadDB( db, rv,  adapter, context).execute(items); //now enter all the data in db
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    public void showDialog(Recipe rec){
        LayoutInflater layoutInflater = LayoutInflater.from(mContext); //from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.recipe_instruction, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(mContext);
        alertDialogBuilderUserInput.setView(view);

        final TextView et_title = (TextView) view.findViewById(R.id.tv_title);
        final TextView et_intr = (TextView) view.findViewById(R.id.tv_instr);
        final TextView et_servings = (TextView) view.findViewById(R.id.tv_serving);
        final TextView et_ready = (TextView) view.findViewById(R.id.tv_time);

        et_title.setText(rec.getDishname());
        et_intr.setText(rec.getInstruc());
        et_ready.setText("Will be ready in just "+rec.getReadyTime()+" minutes");
        et_servings.setText("Serving "+rec.getServings()+" people!");

        alertDialogBuilderUserInput.setCancelable(false)
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO: Saving the result as a row item;
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        alertDialog.show();

    }


    @Override
    public int getItemCount() {
        if (items ==null){
            return 0; }
        else
            return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView dishname;

        public ViewHolder(View itemView){
            super(itemView);
            dishname = itemView.findViewById(R.id.dishname);
        }
    }

    private String ArrayToString(Recipe rec){
        int j=0;
        return "["+rec.getDishname()+","+rec.getId()+" REady in "+rec.getReadyTime()+" minutes,"+rec.getServings()+"servings"+"]";
    }


}
