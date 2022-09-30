package com.dahdotech.parks;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.dahdotech.parks.adapter.CustomInfoWindow;
import com.dahdotech.parks.data.AsyncResponse;
import com.dahdotech.parks.data.Repository;
import com.dahdotech.parks.model.Park;
import com.dahdotech.parks.model.ParkViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.dahdotech.parks.databinding.ActivityMapsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
GoogleMap.OnInfoWindowClickListener{


    private static final String  TAG = "testing";
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ParkViewModel parkViewModel;
    private List<Park> parkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parkList = new ArrayList<>();
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        parkViewModel = new ViewModelProvider(this).
                get(ParkViewModel.class);

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
        mMap.setInfoWindowAdapter(new CustomInfoWindow(getApplicationContext()));
        mMap.setOnInfoWindowClickListener(this);

        parkList.clear();

        Repository.getParks(parks -> {
            parkList = parks;
            LatLng location = new LatLng(-34, 151);
            for(Park park : parks){

                location = new LatLng(Double.parseDouble(park.getLatitude()),
                        Double.parseDouble(park.getLongitude()));

                MarkerOptions markerOptions =
                        new MarkerOptions().position(location)
                                        .title(park.getFullName())
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                                        .snippet(park.getStates());

                Marker marker = mMap.addMarker(markerOptions);
                marker.setTag(park);

            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 5));
            parkViewModel.setSelectedParks(parkList);
        });
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        //go to detailsFragment
        goToDetailsFragment(marker);
    }

    private void goToDetailsFragment(@NonNull Marker marker) {
        parkViewModel.setSelectedPark((Park) marker.getTag());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map, DetailsFragment.newInstance())
                .commit();
    }
}