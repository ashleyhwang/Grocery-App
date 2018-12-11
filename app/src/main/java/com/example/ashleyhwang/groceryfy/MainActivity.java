package com.example.ashleyhwang.groceryfy;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.ashleyhwang.groceryfy.DataModel.GroceryList;
import com.example.ashleyhwang.groceryfy.Groceries.GrocerylistTab;
import com.example.ashleyhwang.groceryfy.Recipe.RecipeTab;

import java.util.ArrayList;

import cz.msebera.android.httpclient.impl.execchain.MainClientExec;

public class MainActivity extends AppCompatActivity {
    private static final String STR_URL = "http://192.168.0.100:8080/";
    private static final String LOG = "MainActivity:";
    private ViewPager mViewPager;
    private ArrayList<GroceryList> groceryLists;
    DatabaseHelper db;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private android.support.v7.widget.Toolbar mToolbar;
    ArrayList<String> mListNames = new ArrayList<>();
    private static final String TAG = "MainActivity";
//    Client client;
    private boolean isLoggedIn = false;

    @Override
    protected void onStart() {
        super.onStart();
//        client = new Client();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(getApplicationContext());
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open,
                R.string.close);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        NavigationView mNav = (NavigationView) findViewById(R.id.navigation_view);
//        View v = mNav.getHeaderView(0);
//        TextView mNavHeader = (TextView) v.findViewById(R.id.nav_header1);
//        setSupportActionBar(mToolbar);

        View mMapButton = findViewById(R.id.nav_mapbtn);

        View logos= (ImageView) findViewById(R.id.nav_logo);
//        View mMapMe = findViewById(R.id.nav_map);
        View mLoginButton = findViewById(R.id.nav_login);
        View mLogoutButton = findViewById(R.id.nav_logout);
        Button mSettingsBtn = (Button) findViewById(R.id.nav_settings);
        Log.d(TAG, "onCreate: ");
        mLogoutButton.setVisibility(View.GONE);
        Bundle b = getIntent().getExtras();


        if(b != null){
            Log.d(LOG, "login_status: " + isLoggedIn);
            setLoggedIn(b.getBoolean("login_status"));
            if(isLoggedIn){
                mLogoutButton.setVisibility(View.VISIBLE);
                mLoginButton.setVisibility(View.GONE);
//                mNavHeader.setText(db.getAccount().getUserName());
            }
        }

        mMapButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent mapIntent = new Intent(MainActivity.this, MapActivity.class) ;
                startActivity(mapIntent);
            }
        });
//
//        mLoginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(loginIntent);
//            }
//        });

//        mLogoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent logout = new Intent(MainActivity.this, MainActivity.class);
//                startActivity(logout);
//            }
//        });
//        mSettingsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
//                settingsIntent.putExtra("login_status", isLoggedIn());
//                startActivity(settingsIntent);
//            }
//        });




        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.nav_icon); //nav icon!!
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });


        groceryLists = new ArrayList<>();
        mListNames = db.getAllGroceryListNames();
        for(String i: mListNames){
            GroceryList groceryList = new GroceryList(i);
            groceryLists.add(groceryList);
        }


        // controls two fragments (recipe and grocerylist)
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    protected void onStop() {
        db.closeDB();
        super.onStop();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem saveList =  menu.findItem(R.id.action_save);
        MenuItem value = menu.findItem(R.id.action_value);
        MenuItem settings = menu.findItem(R.id.action_settings);
        if(mViewPager.getCurrentItem()==1){
            saveList.setVisible(false);
            value.setVisible(false);
            settings.setVisible(true);
        }else{
            saveList.setVisible(false);
            value.setVisible(true);
            settings.setVisible(true);

        }
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_value) {
//            View parentLayout = findViewById(R.id.main_content);
//            Snackbar snackbar = Snackbar.make(parentLayout,"Fridges total value: " + String.format("%.2f", db.getGroceryListPrice("fridge")),
//                    Snackbar.LENGTH_LONG);
//            snackbar.setAction("Action", null).show();
//        }else if(id == R.id.action_settings){
//            SettingsActivity settings = new SettingsActivity();
//            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
//            settingsIntent.putExtra("login_status", isLoggedIn());
//            startActivity(settingsIntent);
//        }
//        return super.onOptionsItemSelected(item);
//    }

    /*
     * Fragment adapter that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    RecipeTab tab1 = new RecipeTab();
                    return tab1;
                case 1:
                    GrocerylistTab tab2 = new GrocerylistTab();
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Recipe";
                case 1:
                    return "Grocery Lists";
            }
            return null;
        }
    }

    //Handling backButton press
    @Override
//    public void onBackPressed() {
//
//        View v = findViewById(R.id.addItems);
//        View v1 = findViewById(R.id.clickHere);
//
//        if(mViewPager.getCurrentItem()==0 && v.getVisibility()== View.VISIBLE){
//            v.setVisibility(View.GONE);
//            v1.setVisibility(View.GONE);
//        }else {
//            Intent a = new Intent(Intent.ACTION_MAIN);
//            a.addCategory(Intent.CATEGORY_HOME);
//            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(a);
//        }
//    }

    public void setSupportActionBar(@Nullable android.support.v7.widget.Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
