package com.personaldev.car_rental.carrental.AppObjects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Object to hold the information of the rental shops
 * found on Amadeus.
 */

public class RentalShopObject implements Parcelable {

    // Variables of the object
    public String companyCode;
    public String companyName;
    public String branchID;
    public double latitude;
    public double longitude;
    public String addressLine1;
    public String addressCity;
    public String addressRegion;
    public String addressCountry;
    public ArrayList<CarObject> carsListing;

    // Constructor
    public RentalShopObject() {
        carsListing = new ArrayList<>();
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
        dest.writeString(addressCountry);
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
        this.addressCountry = in.readString();
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
