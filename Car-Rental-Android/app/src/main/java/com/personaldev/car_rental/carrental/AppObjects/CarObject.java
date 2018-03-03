package com.personaldev.car_rental.carrental.AppObjects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Object to hold the information of the rental shops' cars
 * found on Amadeus.
 */

public class CarObject implements Parcelable {

    // Variables of the object
    public String acrissCode = "";
    public String transmission = "";
    public String fuel = "";
    public boolean airConditioning = false;
    public String vehicleCategory = "";
    public String vehicleType = "";
    public String estimatedTotal = "";
    public String estimatedCurrency = "";
    public ArrayList<PricingObject> priceListing;

    // Constructor
    public CarObject() {
        priceListing = new ArrayList<>();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Storing the data to Parcel object
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(acrissCode);
        dest.writeString(transmission);
        dest.writeString(fuel);
        dest.writeString(vehicleCategory);
        dest.writeString(vehicleType);
        dest.writeString(estimatedTotal);
        dest.writeString(estimatedCurrency);
    }

    // For Creator
    private CarObject(Parcel in){
        this.acrissCode = in.readString();
        this.transmission = in.readString();
        this.fuel = in.readString();
        this.vehicleCategory = in.readString();
        this.vehicleType = in.readString();
        this.estimatedTotal = in.readString();
        this.estimatedCurrency = in.readString();
    }

    public static final Parcelable.Creator<CarObject> CREATOR = new Parcelable.Creator<CarObject>() {
        @Override
        public CarObject createFromParcel(Parcel source) {
            return new CarObject(source);
        }

        @Override
        public CarObject[] newArray(int size) {
            return new CarObject[size];
        }
    };

}
