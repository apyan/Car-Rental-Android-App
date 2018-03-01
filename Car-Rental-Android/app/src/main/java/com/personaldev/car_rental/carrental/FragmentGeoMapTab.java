package com.personaldev.car_rental.carrental;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.personaldev.car_rental.carrental.AppFunctions.AppConnect;

/**
 * Fragment displaying the map visual from Google Maps
 */

public class FragmentGeoMapTab extends Fragment implements OnMapReadyCallback {

    // Variables used for the fragment
    public AppConnect appConnect;
    public Context context;
    public static String defaultURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=37.773972,-122.431297&radius=500&type=restaurant&key=";
    public static String fixedURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=(location)&radius=500&type=restaurant&key=";

    public RequestQueue requestQueue;
    public JsonObjectRequest jsonObjectRequest;

    // Google Maps Variables
    public SupportMapFragment supportMapFragment;

    private final int[] MAP_TYPES = { GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE };
    private int curMapTypeIndex = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set up the fragment
        View v = inflater.inflate(R.layout.fragment_geo_map, container, false);

        // Set up needed classes
        context = getActivity();
        appConnect = new AppConnect(getActivity());
        requestQueue = Volley.newRequestQueue(context);

        // Set up UI elements
        //button_00 = (Button) v.findViewById(R.id.connect_button);

        // Button onClick Inputs
        //button_00.setOnClickListener(this);

        // Initializes Google Map Services
        supportMapFragment = (SupportMapFragment) this.getChildFragmentManager().
                findFragmentById(R.id.supportMapFragment);
        supportMapFragment.getMapAsync(this);


        /*FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        // Add Contents Here
                    }
                });
            }
        });*/

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in SF and move the camera
        LatLng SanFrancisco = new LatLng(37.773972,-122.431297);
        googleMap.addMarker(new
                MarkerOptions().position(SanFrancisco).title("San Francisco"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SanFrancisco));
    }

}
