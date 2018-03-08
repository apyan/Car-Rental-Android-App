package com.personaldev.car_rental.carrental.AppObjects;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Object to hold the information of the rental shops
 * found on Amadeus.
 */

public class RentalShopObject implements Parcelable {

    // Variables of the object
    public String companyCode = "";
    public String companyName = "";
    public String branchID = "";
    public double latitude = 0.00;
    public double longitude = 0.00;
    public String addressLine1 = "";
    public String addressCity = "";
    public String addressRegion = "";
    public String addressPostalCode = "";
    public String addressCountry = "";
    public ArrayList<CarObject> carsListing;
    public Location location_00, location_01;
    public String distanceMiles = "";

    // Constructor
    public RentalShopObject(String inLatitude, String inLongitude) {
        carsListing = new ArrayList<>();
        location_00 = new Location("");
        location_01 = new Location("");
        location_00.setLatitude(Double.parseDouble(inLatitude));
        location_00.setLongitude(Double.parseDouble(inLongitude));
    }

    // Form the distance from the origin in miles
    public void distanceInMiles() {
        DecimalFormat df2 = new DecimalFormat(".##");
        location_01.setLatitude(latitude);
        location_01.setLongitude(longitude);
        double inMeters = (location_00.distanceTo(location_01)) * (0.000621371192);
        distanceMiles = df2.format(inMeters);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Storing the data to Parcel object
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(companyCode);
        dest.writeString(companyName);
        dest.writeString(branchID);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(addressLine1);
        dest.writeString(addressCity);
        dest.writeString(addressRegion);
        dest.writeString(addressPostalCode);
        dest.writeString(addressCountry);
        dest.writeString(distanceMiles);
    }

    // For Creator
    private RentalShopObject(Parcel in){
        this.companyCode = in.readString();
        this.companyName = in.readString();
        this.branchID = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.addressLine1 = in.readString();
        this.addressCity = in.readString();
        this.addressRegion = in.readString();
        this.addressPostalCode = in.readString();
        this.addressCountry = in.readString();
        this.distanceMiles = in.readString();
    }

    public static final Parcelable.Creator<RentalShopObject> CREATOR = new Parcelable.Creator<RentalShopObject>() {
        @Override
        public RentalShopObject createFromParcel(Parcel source) {
            return new RentalShopObject(source);
        }

        @Override
        public RentalShopObject[] newArray(int size) {
            return new RentalShopObject[size];
        }
    };
}
