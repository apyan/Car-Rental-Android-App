package com.personaldev.car_rental.carrental.AppObjects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Object to hold the information of the rental shops' cars' prices
 * found on Amadeus.
 */

public class PricingObject implements Parcelable {

    // Variables of the object
    public String rateType = "";
    public String priceAmount = "";
    public String priceCurrency = "";

    // Constructor
    public PricingObject() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Storing the data to Parcel object
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rateType);
        dest.writeString(priceAmount);
        dest.writeString(priceCurrency);
    }

    // For Creator
    private PricingObject(Parcel in){
        this.rateType = in.readString();
        this.priceAmount = in.readString();
        this.priceCurrency = in.readString();
    }

    public static final Parcelable.Creator<PricingObject> CREATOR = new Parcelable.Creator<PricingObject>() {
        @Override
        public PricingObject createFromParcel(Parcel source) {
            return new PricingObject(source);
        }

        @Override
        public PricingObject[] newArray(int size) {
            return new PricingObject[size];
        }
    };

}
