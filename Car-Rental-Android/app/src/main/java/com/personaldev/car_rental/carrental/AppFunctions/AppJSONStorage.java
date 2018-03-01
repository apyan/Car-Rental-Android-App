package com.personaldev.car_rental.carrental.AppFunctions;

import android.content.Context;

import com.personaldev.car_rental.carrental.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class is used for writing and saving data
 * of the Android application.
 */

public class AppJSONStorage {

    // Variables
    public Context context;
    public String filename;

    // File Data Variables
    public int radiusSearch = 50;
    public int currencyPrice = 0;

    // Constructor
    // Ex. "user-data.json"
    public AppJSONStorage(Context eContext, String name){
        context = eContext;
        filename = name;
    }

    // Checks for the file existence
    public boolean fileExists() {
        File file = new File(context.getFilesDir(), filename);
        if(file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    // Initializes a new file
    public void initializeFile() {
        File file = new File(context.getFilesDir(), filename);
        if(!file.exists()) {
            dataWrite();
        }
    }

    // Read data from the file
    public void dataRead() {
        // Load data
        String jsonString = loadData();

        try {
            // Obtain as JSON object
            JSONObject jsonObject = new JSONObject(jsonString);
            radiusSearch = jsonObject.getInt(context.getString(R.string.user_json_00));
            currencyPrice = jsonObject.getInt(context.getString(R.string.user_json_01));

        } catch (final JSONException e) {
            e.getMessage();
        }

    }

    // Load data from the file as a String
    public String loadData() {
        // Variables
        String line;
        String combined = "";
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(new File(context.getFilesDir(), filename)));
            // Read every line from the file
            while ((line = in.readLine()) != null) {
                // Reading data
                combined = combined + line;
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return combined;
    }

    // Write data to the file
    public void dataWrite() {
        // Variables
        String writer = "{";

        // Begin the JSON file construction
        // For the last city name sought
        writer = writer + "\r\n\"" + context.getString(R.string.user_json_00) + "\" : " + radiusSearch;

        // For the last city ID sought
        writer = writer + ",\r\n\"" + context.getString(R.string.user_json_01) + "\" : " + currencyPrice;

        // End the JSON file construction
        writer = writer + "}";

        try {
            FileWriter out = new FileWriter(new File(context.getFilesDir(), filename));
            out.write(writer);
            out.close();
        } catch (IOException e) {
        }
    }

}
