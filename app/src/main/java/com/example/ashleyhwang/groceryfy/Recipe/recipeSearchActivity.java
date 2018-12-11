package com.example.ashleyhwang.groceryfy.Recipe;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.ashleyhwang.groceryfy.DataModel.Recipe;
import com.example.ashleyhwang.groceryfy.R;

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

public class recipeSearchActivity extends AppCompatActivity {
    private static final String TAG = "recipeSearchActivity";
    private String dishname;
    static ArrayList<Recipe> items = new ArrayList<>();

    RecyclerView rv;
    RecipeSearchAdapter adapter;
    private final String BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/search?number=10&query=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_search);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        dishname = (String) bundle.getString("dish");
        new RecipeAsync().execute(dishname);
    }

    private class RecipeAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String queryString = BASE_URL + params[0];
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
            Log.d(TAG, "doInBackground: "+recipeJSON);
            return recipeJSON;


        }

        @Override
        protected void onPreExecute(){super.onPreExecute();}

        @Override
        protected void onPostExecute(String s){
            items = new ArrayList<>();
            try{
                // Convert the response into a JSON object.
                JSONObject jsonObject = new JSONObject(s); //get top level object
                // Get the JSONArray of book items.
                JSONArray itemsArray = jsonObject.getJSONArray("results"); //array of earthquakes objects

                Log.d(TAG, "onPostExecute: "+jsonObject);
                // Initialize iterator and results fields.
                int i = 0;
                // Look for results in the items array
                while (i < itemsArray.length()) {
                    // Get the current item information.
                    JSONObject rObject = itemsArray.getJSONObject(i);
                    Recipe r = new Recipe();
                    r.setId(rObject.getInt("id"));
                    r.setDishname(rObject.getString("title"));
                    items.add(r);
                    i++;
                }
                initRecyclerView();
                Log.d(TAG, "onPostExecute: "+ArrayToString(items));
//                new LoadDB( db, rv,  adapter, context).execute(items); //now enter all the data in db
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initRecyclerView() {
        rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getApplication()));
        adapter = new RecipeSearchAdapter(items);
        rv.setAdapter(adapter);
    }

    private String ArrayToString(List<Recipe> items ){
        int j=0;
        String string= "";
        while (j<items.size()){
            string += "["+items.get(j).getDishname()+","+items.get(j).getId()+"]";
            j++;
        }
        return string;
    }

}
