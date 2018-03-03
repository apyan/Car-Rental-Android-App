package com.personaldev.car_rental.carrental;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.personaldev.car_rental.carrental.AppFunctions.AppConnect;
import com.personaldev.car_rental.carrental.AppFunctions.AppJSONStorage;
import com.personaldev.car_rental.carrental.AppObjects.CarObject;
import com.personaldev.car_rental.carrental.AppObjects.PricingObject;
import com.personaldev.car_rental.carrental.AppObjects.RentalShopObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The fragment that displays the user searches.
 */

public class FragmentSearchTab extends Fragment implements View.OnClickListener {

    // Variables used for the fragment
    public AppConnect appConnect;
    public AppJSONStorage appJSONStorage;
    public Context context;
    public static String url_0 = "https://api.sandbox.amadeus.com/v1.2/cars/search-circle?apikey=";
    public static String url_1 = "&latitude=";
    public static String url_2 = "&longitude=";
    public static String url_3 = "&radius=";
    public static String url_4 = "&pick_up=";
    public static String url_5 = "&drop_off=";
    public static String url_6 = "&currency=";
    public String jsonURL;
    public ArrayList<RentalShopObject> rentalListing;
    public String datePickUp = "";
    public String dateDropOff = "";
    public SimpleDateFormat dateFormat_00, dateFormat_01;

    public RequestQueue requestQueue;
    public JsonObjectRequest jsonObjectRequest;

    // UI Elements
    public ListView listView_00;
    public EditText editText_00, editText_01, editText_02;
    public Spinner spinner_00;
    public Button button_00, button_01, button_02, button_03;
    public LinearLayout linearLayout_00, linearLayout_01, linearLayout_02, linearLayout_03;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set up the fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        // Set up needed classes
        context = getActivity();
        appConnect = new AppConnect(getActivity());
        appConnect.handleSSLHandshake();
        appJSONStorage = new AppJSONStorage(context, getString(R.string.user_file));
        appJSONStorage.dataRead();
        rentalListing = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(context);

        // Set up date formats, initialize it as today
        dateFormat_00 = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat_01 = new SimpleDateFormat("yyyy-MM-dd");
        datePickUp = dateFormat_01.format(new Date());
        dateDropOff = dateFormat_01.format(new Date());

        // Assign UI Elements
        listView_00 = (ListView) v.findViewById(R.id.listView_00);
        editText_00 = (EditText) v.findViewById(R.id.editAddress);
        editText_01 = (EditText) v.findViewById(R.id.editCity);
        editText_02 = (EditText) v.findViewById(R.id.editZip);
        spinner_00 = (Spinner) v.findViewById(R.id.spinnerState);
        button_00 = (Button) v.findViewById(R.id.pickUpButton);
        button_01 = (Button) v.findViewById(R.id.dropOffButton);
        button_02 = (Button) v.findViewById(R.id.searchButton);
        linearLayout_00 = (LinearLayout) v.findViewById(R.id.greeting_linear);
        linearLayout_01 = (LinearLayout) v.findViewById(R.id.connection_linear);
        linearLayout_02 = (LinearLayout) v.findViewById(R.id.loading_linear);
        linearLayout_03 = (LinearLayout) v.findViewById(R.id.results_linear);

        // Button onClick Inputs
        button_00.setOnClickListener(this);
        button_01.setOnClickListener(this);
        button_02.setOnClickListener(this);

        // Set-up the Initial View
        // Set up the time labels
        button_00.setText(getString(R.string.search_tab_04) + " " + dateFormat_00.format(new Date()));
        button_01.setText(getString(R.string.search_tab_05) + " " + dateFormat_00.format(new Date()));

        // For first-time view, than the last searched latitude and (assuming longitude) is empty
        if(appJSONStorage.lastSearchLatitude.equals("")) {
            linearLayout_00.setVisibility(View.VISIBLE);
            linearLayout_01.setVisibility(View.GONE);
            linearLayout_02.setVisibility(View.GONE);
            linearLayout_03.setVisibility(View.GONE);
            listView_00.setVisibility(View.GONE);
        } else {
            linearLayout_00.setVisibility(View.GONE);

            // There is last results found from the file
            // Checks for connection
            if(appConnect.connectionAvailable()) {

                // Appropriate the screen output (Searching)
                linearLayout_01.setVisibility(View.GONE);
                linearLayout_02.setVisibility(View.VISIBLE);
                linearLayout_03.setVisibility(View.GONE);
                listView_00.setVisibility(View.GONE);

                // Create the URL
                jsonURL = url_0 + getString(R.string.amadeus_api_key) + url_1 + appJSONStorage.lastSearchLatitude +
                        url_2 + appJSONStorage.lastSearchLongitude + url_3 + appJSONStorage.radiusSearch + url_4 +
                        datePickUp + url_5 + dateDropOff + url_6 + "USD";

                // Initialize JsonObjectRequest for Volley JSON retrieval
                jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                        jsonURL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                // Appropriate the screen output (Viewing Topics)
                                linearLayout_01.setVisibility(View.GONE);
                                linearLayout_02.setVisibility(View.GONE);
                                linearLayout_03.setVisibility(View.GONE);
                                listView_00.setVisibility(View.VISIBLE);

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

                                        // Get current child JSON object (address)
                                        JSONObject dataObject_01 = rentalEntry.getJSONObject("location");
                                        rentalShopObject.latitude = dataObject_01.getDouble("latitude");
                                        rentalShopObject.longitude = dataObject_01.getDouble("longitude");

                                        // Get current child JSON object (address)
                                        JSONObject dataObject_02 = rentalEntry.getJSONObject("address");
                                        rentalShopObject.addressLine1 = dataObject_02.getString("line1");
                                        rentalShopObject.addressCity = dataObject_02.getString("city");
                                        rentalShopObject.addressRegion = dataObject_02.getString("region");
                                        rentalShopObject.addressCountry = dataObject_02.getString("country");

                                        // Loop through the cars array
                                        JSONArray dataObjectArray_1 = rentalEntry.getJSONArray("cars");
                                        for(int index_1 = 0; index_1 < dataObjectArray_1.length(); index_1++) {

                                            // Create a new CarObject
                                            CarObject carObject = new CarObject();

                                            // Get current child JSON object (vehicle_info)
                                            JSONObject rentalEntry_1 = dataObjectArray.getJSONObject(index_1);
                                            JSONObject dataObject_03 = rentalEntry_1.getJSONObject("vehicle_info");
                                            carObject.acrissCode = dataObject_03.getString("acriss_code");
                                            carObject.transmission = dataObject_03.getString("transmission");
                                            carObject.fuel = dataObject_03.getString("fuel");
                                            carObject.airConditioning = dataObject_03.getBoolean("air_conditioning");
                                            carObject.vehicleCategory = dataObject_03.getString("category");
                                            carObject.vehicleType = dataObject_03.getString("type");

                                            // Get current child JSON object (estimated_total)
                                            JSONObject dataObject_04 = rentalEntry_1.getJSONObject("estimated_total");
                                            carObject.estimatedTotal = dataObject_04.getString("amount");
                                            carObject.estimatedCurrency = dataObject_04.getString("currency");

                                            // Loop through the rates array
                                            JSONArray dataObjectArray_2 = rentalEntry.getJSONArray("rates");
                                            for(int index_2 = 0; index_2 < dataObjectArray_2.length(); index_2++) {

                                                // Create a new PricingObject
                                                PricingObject pricingObject = new PricingObject();

                                                // Get current child JSON object (price)
                                                JSONObject rentalEntry_2 = dataObjectArray.getJSONObject(index_2);
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

                                        // Add the new topic into the Rental Listing Array
                                        rentalListing.add(rentalShopObject);
                                    }

                                } catch(JSONException e) {
                                    e.printStackTrace();
                                }

                                // Populate ListView
                                listView_00.setAdapter(new AdapterRentals(getActivity(), rentalListing));
                            }
                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error){
                                // Do something when error occurred

                                // Appropriate the screen output (No Connection)
                                linearLayout_01.setVisibility(View.VISIBLE);
                                linearLayout_02.setVisibility(View.GONE);
                                linearLayout_03.setVisibility(View.GONE);
                                listView_00.setVisibility(View.GONE);
                            }
                        }
                );

                // Add JsonObjectRequest to the RequestQueue
                requestQueue.add(jsonObjectRequest);

            } else {
                // Connection not found
                linearLayout_01.setVisibility(View.VISIBLE);
                linearLayout_02.setVisibility(View.GONE);
                linearLayout_03.setVisibility(View.GONE);
                listView_00.setVisibility(View.GONE);
            }
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // Opens the calendar for pick-up
            case R.id.pickUpButton:
                //
                break;
            // Opens the calendar for drop-off
            case R.id.dropOffButton:
                //
                break;

            // Searches for the rental shops based on address given
            case R.id.searchButton:
                //
                linearLayout_00.setVisibility(View.GONE);
                // There is last results found from the file
                // Checks for connection
                if(appConnect.connectionAvailable()) {

                    // Appropriate the screen output (Searching)
                    linearLayout_01.setVisibility(View.GONE);
                    linearLayout_02.setVisibility(View.VISIBLE);
                    linearLayout_03.setVisibility(View.GONE);
                    listView_00.setVisibility(View.GONE);

                    // Create the URL
                    jsonURL = url_0 + getString(R.string.amadeus_api_key) + url_1 + "37.773972" +
                            url_2 + "-122.431297" + url_3 + appJSONStorage.radiusSearch + url_4 +
                            datePickUp + url_5 + dateDropOff + url_6 + "USD";

                    // Initialize JsonObjectRequest for Volley JSON retrieval
                    jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                            jsonURL, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    // Appropriate the screen output (Viewing Topics)
                                    linearLayout_01.setVisibility(View.GONE);
                                    linearLayout_02.setVisibility(View.GONE);
                                    linearLayout_03.setVisibility(View.GONE);
                                    listView_00.setVisibility(View.VISIBLE);

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

                                            // Get current child JSON object (address)
                                            JSONObject dataObject_01 = rentalEntry.getJSONObject("location");
                                            rentalShopObject.latitude = dataObject_01.getDouble("latitude");
                                            rentalShopObject.longitude = dataObject_01.getDouble("longitude");

                                            // Get current child JSON object (address)
                                            JSONObject dataObject_02 = rentalEntry.getJSONObject("address");
                                            rentalShopObject.addressLine1 = dataObject_02.getString("line1");
                                            rentalShopObject.addressCity = dataObject_02.getString("city");
                                            rentalShopObject.addressRegion = dataObject_02.getString("region");
                                            rentalShopObject.addressCountry = dataObject_02.getString("country");

                                            // Loop through the cars array
                                            JSONArray dataObjectArray_1 = rentalEntry.getJSONArray("cars");
                                            for(int index_1 = 0; index_1 < dataObjectArray_1.length(); index_1++) {

                                                // Create a new CarObject
                                                CarObject carObject = new CarObject();

                                                // Get current child JSON object (vehicle_info)
                                                JSONObject rentalEntry_1 = dataObjectArray.getJSONObject(index_1);
                                                JSONObject dataObject_03 = rentalEntry_1.getJSONObject("vehicle_info");
                                                carObject.acrissCode = dataObject_03.getString("acriss_code");
                                                carObject.transmission = dataObject_03.getString("transmission");
                                                carObject.fuel = dataObject_03.getString("fuel");
                                                carObject.airConditioning = dataObject_03.getBoolean("air_conditioning");
                                                carObject.vehicleCategory = dataObject_03.getString("category");
                                                carObject.vehicleType = dataObject_03.getString("type");

                                                // Get current child JSON object (estimated_total)
                                                JSONObject dataObject_04 = rentalEntry_1.getJSONObject("estimated_total");
                                                carObject.estimatedTotal = dataObject_04.getString("amount");
                                                carObject.estimatedCurrency = dataObject_04.getString("currency");

                                                // Loop through the rates array
                                                JSONArray dataObjectArray_2 = rentalEntry.getJSONArray("rates");
                                                for(int index_2 = 0; index_2 < dataObjectArray_2.length(); index_2++) {

                                                    // Create a new PricingObject
                                                    PricingObject pricingObject = new PricingObject();

                                                    // Get current child JSON object (price)
                                                    JSONObject rentalEntry_2 = dataObjectArray.getJSONObject(index_2);
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

                                            // Add the new topic into the Rental Listing Array
                                            rentalListing.add(rentalShopObject);
                                        }

                                    } catch(JSONException e) {
                                        e.printStackTrace();
                                    }

                                    // Populate ListView
                                    listView_00.setAdapter(new AdapterRentals(getActivity(), rentalListing));
                                }
                            },
                            new Response.ErrorListener(){
                                @Override
                                public void onErrorResponse(VolleyError error){
                                    // Do something when error occurred

                                    // Appropriate the screen output (No Connection)
                                    linearLayout_01.setVisibility(View.VISIBLE);
                                    linearLayout_02.setVisibility(View.GONE);
                                    linearLayout_03.setVisibility(View.GONE);
                                    listView_00.setVisibility(View.GONE);

                                    Log.d("TESTER-000", "VIEWING " + error.toString());
                                }
                            }
                    );

                    // Add JsonObjectRequest to the RequestQueue
                    requestQueue.add(jsonObjectRequest);

                } else {
                    // Connection not found
                    linearLayout_01.setVisibility(View.VISIBLE);
                    linearLayout_02.setVisibility(View.GONE);
                    linearLayout_03.setVisibility(View.GONE);
                    listView_00.setVisibility(View.GONE);
                }

                break;
            default:
                break;
        }
    }
}
