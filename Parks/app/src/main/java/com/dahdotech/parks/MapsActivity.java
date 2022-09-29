package com.dahdotech.parks;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.dahdotech.parks.data.AsyncResponse;
import com.dahdotech.parks.data.Repository;
import com.dahdotech.parks.model.Park;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.dahdotech.parks.databinding.ActivityMapsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private static final String  TAG = "testing";
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        BottomNavigationView bottomNavigationView =
                findViewById(R.id.button_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            int id = item.getItemId();
            if(id == R.id.maps_nav_button) {
                mMap.clear();
                mapFragment.getMapAsync(this);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map, mapFragment)
                        .commit();
                return true;
            }
            else if(id == R.id.parks_nav_button){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map, ParksFragment.newInstance())
                        .commit();
                return true;
            }

            return false;
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Repository.getParks(parks -> {
            LatLng sydney = new LatLng(-34, 151);
            for(Park park : parks){

                sydney = new LatLng(Double.parseDouble(park.getLatitude()),
                        Double.parseDouble(park.getLongitude()));
                mMap.addMarker(new MarkerOptions().position(sydney).title(park.getFullName()));
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 5));
        });
    }
}