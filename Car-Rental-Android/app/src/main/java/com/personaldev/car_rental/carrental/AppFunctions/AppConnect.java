package com.personaldev.car_rental.carrental.AppFunctions;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This class is used for connection purposes.
 */

public class AppConnect {

    // Variables for the class
    public Context eContext;

    // Constructor
    public AppConnect(Context context) {
        eContext = context;
    }

    // Checking for Internet Connectivity
    public boolean connectionAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                eContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return ((activeNetworkInfo != null) && (activeNetworkInfo.isConnected()));
    }

    // Checking for Location Services (GPS)
    public boolean locationAvailable(){
        LocationManager locationManager = (LocationManager)
                eContext.getSystemService(Context.LOCATION_SERVICE);
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

}
