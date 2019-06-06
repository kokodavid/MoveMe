package com.david.moveme;


import android.annotation.SuppressLint;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
        import android.support.design.widget.NavigationView;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
        import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.MapFragment;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.CameraPosition;
        import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback , View.OnClickListener {


    private double radius = 2000;
    private Button ride;
    private ImageView logout;
    private customfonts.EditText__SF_Pro_Display_Regular mSearch;


    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;// declare this globally
    private  static final String  TAG = "HomeActivity";



    private GoogleMap mMap;


    private ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    private DrawerLayout drawer;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ride = findViewById(R.id.ride);
        ride.setOnClickListener(this);

        mSearch = (customfonts.EditText__SF_Pro_Display_Regular) findViewById(R.id.search);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.googleMap);

        mapFragment.getMapAsync(this);

        init();

    }

    private void  init() {
        Log.d(TAG,"init: initializing");

        mSearch.setOnEditorActionListener(new  customfonts.EditText__SF_Pro_Display_Regular.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH||
               actionId == EditorInfo.IME_ACTION_DONE ||

                keyEvent.getAction() == KeyEvent.ACTION_DOWN

                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    geoLocate();

                }
                return false;
            }
        });
    }

    private void geoLocate(){
        Log.d(TAG,"geoLocate: geoLocating");
        String searchString = mSearch.getText().toString();

        Geocoder geocoder = new Geocoder(HomeActivity.this);
        List<Address> list = new ArrayList<>();
        try {

            list = geocoder.getFromLocationName(searchString,1);
        }catch (IOException e){
            Log.e(TAG,"geoLocate:IOException:" + e.getMessage());
        }

        if (list.size() > 0){
            Address address =  list.get(0);
            Log.d(TAG, "geoLocate: found a location:" + address.toString());
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

//      googleMap.setMyLocationEnabled(true);

        init();

        mMap = googleMap;
        LatLng origin = new LatLng(-1.297278, 36.779139);
        LatLng destination = new LatLng(-1.297278, 36.779139);
        /*DrawRouteMaps.getInstance(this)
                .draw(origin, destination, mMap);
        DrawMarker.getInstance(this).draw(mMap, origin, R.drawable.currentlocation, "Pickup");
        DrawMarker.getInstance(this).draw(mMap, destination, R.drawable.droplocation, "Destination");*/

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origin)
                .include(destination).build();
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));


        LatLng latLng = new LatLng(-1.297278,  36.779139);

        double lat = -1.297278;
        double lng = 36.779139;


//        googleMap.addCircle(new CircleOptions()
//                .center(new LatLng(lat, lng))
//                .radius(radius)
//                .strokeColor(Color.BLUE)
//                .strokeWidth(0f)
//                .fillColor(Color.BLACK)
//
//                .fill        int id = item.getItemId();
//Color(Color.parseColor("#26006ef1")));
//
//        CircleOptions circle=new CircleOptions();
//        circle.center(googleMap).fillColor(Color.LTGRAY).radius(1000);
//        googleMap.addCircle(circle);


        // create marker
        MarkerOptions marker = new MarkerOptions().position(latLng).title("Set Pickup Point");
//        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_caronmap));
        // adding marker
        googleMap.addMarker(marker);


        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                latLng).zoom(14).build();


        googleMap.animateCamera(


                CameraUpdateFactory.newCameraPosition(cameraPosition));






        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        setToolbar();


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {


            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);

        invalidateOptionsMenu();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title


        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;

        }


        return super.onOptionsItemSelected(item);
    }


    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
//        actionBarDrawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(Gravity.LEFT); //OPEN Nav Drawer!
        } else {
            finish();
        }
    }


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.





        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("");



        toolbar.findViewById(R.id.action_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout();
            }
        });


        toolbar.findViewById(R.id.navigation_menu).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Log.e("Click", "keryu");

                if (drawer.isDrawerOpen(navigationView)) {
                    drawer.closeDrawer(navigationView);
                } else {
                    drawer.openDrawer(navigationView);

                    Log.e("abc", "abc");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == ride) {
            Intent intent = new Intent(HomeActivity.this, BookARIde.class);
            startActivity(intent);
        }
    }
}
