package com.personaldev.car_rental.carrental;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.personaldev.car_rental.carrental.AppFunctions.AppConnect;
import com.personaldev.car_rental.carrental.AppObjects.CarObject;
import com.personaldev.car_rental.carrental.AppObjects.PricingObject;
import com.personaldev.car_rental.carrental.AppObjects.RentalShopObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Fragment displaying the map visual from Google Maps
 */

public class FragmentGeoMapTab extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    // Variables used for the fragment
    public AppConnect appConnect;
    public Context context;
    public ArrayList<RentalShopObject> rentalListing;
    public static String url_0 = "https://api.sandbox.amadeus.com/v1.2/cars/search-circle?apikey=";
    public static String url_1 = "&latitude=";
    public static String url_2 = "&longitude=";
    public static String url_3 = "&radius=";
    public static String url_4 = "&pick_up=";
    public static String url_5 = "&drop_off=";
    public static String url_6 = "&currency=";

    // UI Elements
    public Button button_00, button_01;

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
        //appConnect.handleSSLHandshake();
        requestQueue = Volley.newRequestQueue(context);
        rentalListing = new ArrayList<>();

        // Initializes Google Map Services
        supportMapFragment = (SupportMapFragment) this.getChildFragmentManager().
                findFragmentById(R.id.supportMapFragment);
        supportMapFragment.getMapAsync(this);

        // Assign UI Elements
        button_00 = (Button) v.findViewById(R.id.searchButton);
        button_01 = (Button) v.findViewById(R.id.locateButton);

        // Button onClick Inputs
        button_00.setOnClickListener(this);
        button_01.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // Searches for the nearby car rentals
            case R.id.searchButton:
                break;
            // Locates the user on Google Maps via Location services
            case R.id.locateButton:
                break;
            default:
                break;
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // Add a marker in SF and move the camera
        LatLng SanFrancisco = new LatLng(37.773972,-122.431297);
        googleMap.addMarker(new
                MarkerOptions().position(SanFrancisco).title("San Francisco"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SanFrancisco));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(SanFrancisco,12));

        // Checks for connection
        if(appConnect.connectionAvailable()) {

            // Appropriate the screen output (Searching)
            final String jsonURL = url_0 + getString(R.string.amadeus_api_key) + url_1 + "37.773972" +
                    url_2 + "-122.431297" + url_3 + "99" + url_4 + "2018-06-07" + url_5 + "2018-06-07"
                    + url_6 + "USD";

            // Initialize JsonObjectRequest for Volley JSON retrieval
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    jsonURL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            // Appropriate the screen output (Viewing Topics)
                            // Nothing to see

                            // Process the JSON file
                            try {

                                // Get the JSON attributes
                                JSONArray dataObjectArray = response.getJSONArray("results");

                                // Loop through the children array
                                for(int index = 0; index < dataObjectArray.length(); index++) {

                                    // Create a new RentalShopObject
                                    RentalShopObject rentalShopObject = new RentalShopObject();

                                    // Get current child JSON object (provider)
                                    JSONObject rentalEntry = dataObjectArray.getJSONObject(index);
                                    JSONObject dataObject_00 = rentalEntry.getJSONObject("provider");
                                    rentalShopObject.companyCode = dataObject_00.getString("company_code");
                                    rentalShopObject.companyName = dataObject_00.getString("company_name");

                                    rentalShopObject.branchID = rentalEntry.getString("branch_id");

                                    Log.d("TESTER-000", "VIEWING " + rentalShopObject.branchID);
                                    Log.d("TESTER-000", "VIEWING " + jsonURL);

                                    // Get current child JSON object (address)
                                    JSONObject dataObject_01 = rentalEntry.getJSONObject("location");
                                    rentalShopObject.latitude = dataObject_01.getDouble("latitude");
                                    rentalShopObject.longitude = dataObject_01.getDouble("longitude");

                                    Log.d("TESTER-000", "VIEWING " + rentalShopObject.longitude);

                                    // Get current child JSON object (address)
                                    JSONObject dataObject_02 = rentalEntry.getJSONObject("address");
                                    rentalShopObject.addressLine1 = dataObject_02.getString("line1");
                                    rentalShopObject.addressCity = dataObject_02.getString("city");
                                    // Checks for Region existence
                                    if(dataObject_02.has("region")) {
                                        rentalShopObject.addressRegion = dataObject_02.getString("region");
                                    }
                                    // Checks for Postal Code existence
                                    if(dataObject_02.has("postal_code")) {
                                        rentalShopObject.addressPostalCode = dataObject_02.getString("postal_code");
                                    }

                                    rentalShopObject.addressCountry = dataObject_02.getString("country");

                                    Log.d("TESTER-000", "VIEWING " + rentalShopObject.addressCountry);

                                    // -------

                                    // Loop through the cars array
                                    JSONArray dataObjectArray_1 = rentalEntry.getJSONArray("cars");
                                    for(int index_1 = 0; index_1 < dataObjectArray_1.length(); index_1++) {

                                        // Create a new CarObject
                                        CarObject carObject = new CarObject();

                                        // Get current child JSON object (vehicle_info)
                                        JSONObject rentalEntry_1 = dataObjectArray_1.getJSONObject(index_1);
                                        JSONObject dataObject_03 = rentalEntry_1.getJSONObject("vehicle_info");
                                        carObject.acrissCode = dataObject_03.getString("acriss_code");
                                        carObject.transmission = dataObject_03.getString("transmission");
                                        carObject.fuel = dataObject_03.getString("fuel");
                                        carObject.airConditioning = dataObject_03.getBoolean("air_conditioning");
                                        carObject.vehicleCategory = dataObject_03.getString("category");
                                        carObject.vehicleType = dataObject_03.getString("type");

                                        Log.d("TESTER-001", "VIEWING " + carObject.acrissCode);

                                        // Get current child JSON object (estimated_total)
                                        JSONObject dataObject_04 = rentalEntry_1.getJSONObject("estimated_total");
                                        carObject.estimatedTotal = dataObject_04.getString("amount");
                                        carObject.estimatedCurrency = dataObject_04.getString("currency");

                                        // Loop through the rates array
                                        JSONArray dataObjectArray_2 = rentalEntry_1.getJSONArray("rates");
                                        for(int index_2 = 0; index_2 < dataObjectArray_2.length(); index_2++) {

                                            // Create a new PricingObject
                                            PricingObject pricingObject = new PricingObject();

                                            // Get current child JSON object (price)
                                            JSONObject rentalEntry_2 = dataObjectArray_2.getJSONObject(index_2);
                                            pricingObject.rateType = rentalEntry_2.getString("type");
                                            JSONObject dataObject_05 = rentalEntry_2.getJSONObject("price");
                                            pricingObject.priceAmount = dataObject_05.getString("amount");
                                            pricingObject.priceCurrency = dataObject_05.getString("currency");

                                            // Add the new topic into the Price Listing Array
                                            carObject.priceListing.add(pricingObject);
                                        }

                                        // Add the new topic into the Car Listing Array
                                        rentalShopObject.carsListing.add(carObject);
                                    }

                                    // ---

                                    // Adding Markers
                                    String mapMarkerTitle = rentalShopObject.companyName +
                                            " (" + rentalShopObject.companyCode + ")";
                                    googleMap.addMarker(new MarkerOptions().position(new LatLng(rentalShopObject.latitude,
                                            rentalShopObject.longitude))
                                            .title(mapMarkerTitle).icon(BitmapDescriptorFactory
                                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                                    // Add the new topic into the Rental Listing Array
                                    rentalListing.add(rentalShopObject);
                                }

                            } catch(JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            // Do something when error occurred

                            // Appropriate the screen output (No Connection)
                            Toast.makeText(context, getString(R.string.message_02), Toast.LENGTH_SHORT).show();
                            Log.d("TESTER-000", "VIEWING " + error.toString());
                        }
                    }
            );

            // Add JsonObjectRequest to the RequestQueue
            requestQueue.add(jsonObjectRequest);

        } else {
            Toast.makeText(context, getString(R.string.message_02), Toast.LENGTH_SHORT).show();
        }
    }

}
