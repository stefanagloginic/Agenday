package com.example.stefana.agenday;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    final private int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_lOCATION = 97;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        if(isCoarseLocationAllowed()) {
            //if user allows location access then apply the homeFragment
            viewPagerAdapter.addFragments(new HomeFragment(), "Home");
            addExtraFragments();
        }else{
            //otherwise request the permission and see if the user allows us to use it
            requestCoarseLocationPermission();
        }

    }

    private void requestCoarseLocationPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //if statement checks if the user has previously denied the permission
                //add asychronously why you need the permission
                //show one then try asking again

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_lOCATION);
        } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_lOCATION);
        }

    }

    private boolean isCoarseLocationAllowed(){
        boolean coarseLocationAllowed = false;

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            coarseLocationAllowed = true;
        }

        return coarseLocationAllowed;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /*  switch uses the requestCode provided by the requestPermission functions which is the private field
            in this case it is the MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION
        */
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_lOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // once location is allowed add the homefragment
                    viewPagerAdapter.addFragments(new HomeFragment(), "Home");
                } else {
                    //if permission is denied apply a new fragment which shows a no location
                    viewPagerAdapter.addFragments(new LocationPermissionDeniedFragment(), "Home");

                }

                addExtraFragments();
            }
        }
    }

    private void addExtraFragments(){
        viewPagerAdapter.addFragments(new ProfileFragment(), "Profile");
        viewPagerAdapter.addFragments(new SearchFragment(), "Search");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}
