package com.personaldev.car_rental.carrental.AppObjects;

import java.util.ArrayList;

/**
 * Object to hold the information of the rental shops' cars
 * found on Amadeus.
 */

public class CarObject {

    // Variables of the object
    public String acrissCode;
    public String transmission;
    public String fuel;
    public boolean airConditioning;
    public String vehicleCategory;
    public String vehicleType;
    public String estimatedTotal;
    public String estimatedCurrency;
    public ArrayList<PricingObject> priceListing;

    // Constructor
    public CarObject() {
        priceListing = new ArrayList<>();
    }

}
