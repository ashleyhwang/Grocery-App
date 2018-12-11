package com.example.ashleyhwang.groceryfy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.ashleyhwang.groceryfy.DataModel.Recipe;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private static final String TAG = "MapActivity";
    private ArrayList<GroceryStore> groceryStores;
    private GoogleMap mMap;
    private LocationManager locManager;
    private double latitude;
    private double longitude;

    private GoogleMap map;
    private LatLng latLng;
    private double lat1, lon1;
    private Location mCurrentLocation; //from stackoverflow "https://stackoverflow.com/questions/18546234/mark-current-location-on-google-map"
    Geocoder geocoder = null;
    List<String> locations; // contains all locations
    String location;
    TextView cityLabel;
    FusedLocationProviderClient mFusedLocatioClient;
    private static final String BASE_URL= "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    private static final String API_KEY= "AIzaSyD_9cSTWMeud_KNytVU5JUjXdmaxlfae6g\n";
    private static final String Grocery_info ="&radius=1500&type=supermarket&keyword=grocery&key=AIzaSyD_9cSTWMeud_KNytVU5JUjXdmaxlfae6g";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_map);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("Mapper");
            actionBar.setDisplayHomeAsUpEnabled(true);
            cityLabel = (TextView) findViewById(R.id.cityLabel);


            geocoder = new Geocoder(this);
            locations = new ArrayList<String>();
            locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

//            mCurrentLocation = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            lat1 = mCurrentLocation.getLatitude();
//            lon1 = mCurrentLocation.getLongitude();
            //TODO: Actually implementing the current location feature;
            lat1 =  42.334515;
            lon1 = -71.168648;
            location = lat1+","+lon1;
            new FindStoresTask().execute(BASE_URL+lat1+","+lon1+Grocery_info);
            //2차시도
//            mFusedLocatioClient = (FusedLocationProviderClient) LocationServices.getFusedLocationProviderClient(this);
            SupportMapFragment mf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.the_map);
            mf.getMapAsync(this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private class FindStoresTask extends AsyncTask<String, Integer, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String queryString = params[0];
            // Set up variables for the try block that need to be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String citiesJSON = null;
            try {

                URL requestURL = new URL(queryString);

                // Open the network connection.
                urlConnection = (HttpURLConnection) requestURL.openConnection();
                urlConnection.setRequestMethod("GET");
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
                citiesJSON = builder.toString();

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
            return citiesJSON;
        }
        @Override
        protected void onPostExecute(String s) {
            //items = new ArrayList<Earthquakes>();
            groceryStores = new ArrayList<>();

            try {
                // Convert the response into a JSON object.
                JSONObject jsonObject = new JSONObject(s); //get top level object
                // Get the JSONArray of book items.
                JSONArray itemsArray = jsonObject.getJSONArray("results"); //array of earthquakes objects

                // Initialize iterator and results fields.
                int i = 0;
                // Look for results in the items array
                while (i < itemsArray.length()) {
                    // Get the current item information.
                    JSONObject cityObject = itemsArray.getJSONObject(i);
                    GroceryStore city = new GroceryStore();
                    city.setStoreName(cityObject.getString("name"));
                    city.setLat(cityObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                    city.setLng(cityObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
                    String storeltLg = city.getLat()+","+city.getLng();
                    groceryStores.add(city);


                    if(!groceryStores.isEmpty()){
                        LatLng ltlg = new LatLng(city.getLat(), city.getLng());
                        map.addMarker(new MarkerOptions()
                                .position(ltlg)
                                .title(city.getStoreName())
                                .snippet(storeltLg)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    }
                    i++;
                }

                Log.d(TAG, "onPostExecute: "+ArrayToString(groceryStores));
//                new LoadDB( db, rv,  adapter, context).execute(items); //now enter all the data in db
            } catch (Exception e){
                // If onPostExecute does not receive a proper JSON string,
                // update the UI to show failed results.
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMapLoaded() {
        getMoreInfo(); // call this --> use a geoCoder to find the location of the city
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String url = ("https://www.google.com/maps/search/"+ marker.getSnippet());
                Uri uri = Uri.parse(url);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });


    }
;
    @Override
    public void onMapReady(GoogleMap googleMap) { // map is loaded but not laid out yet
        this.map = googleMap;
        map.setOnMapLoadedCallback(this);      // calls onMapLoaded when layout done
        UiSettings mapSettings;
        mapSettings = map.getUiSettings();
        mapSettings.setZoomControlsEnabled(true);
    }

    public void getMoreInfo() {
        latLng = new LatLng(lat1, lon1);  //used in addMarker below for placing a marker at the Longitude/Latitude spot
        Geocoder gcd = new Geocoder(this);
        // puts marker icon at location
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        map.setMyLocationEnabled(true);
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Current Location")
                .snippet(location)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14), 3000, null);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //this will catch the <- arrow
        //and return to MainActivity
        //needed since we use fragments to map sites
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String ArrayToString(List<GroceryStore> items ){
        int j=0;
        String string= "";
        while (j<items.size()){
            string += "["+items.get(j).getStoreName()+","+items.get(j).getLat()+","+items.get(j).getLng()+"]";
            j++;
        }
        return string;
    }
}
