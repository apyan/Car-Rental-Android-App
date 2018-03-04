package com.personaldev.car_rental.carrental;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.personaldev.car_rental.carrental.AppObjects.RentalShopObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Adapter for the list view searching of car rentals
 */

public class AdapterRentals extends BaseAdapter {

    // Variables for the adapter
    public static LayoutInflater inflater = null;
    public ArrayList<RentalShopObject> foundedRentals;
    Context eContext;

    // Constructor
    public AdapterRentals(Context context, ArrayList<RentalShopObject> results){
        eContext = context;
        foundedRentals = results;
        inflater = (LayoutInflater) eContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return foundedRentals.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    // List Row Attributes
    public class RowAttributes {
        TextView text_00, text_01, text_02, text_03;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        RowAttributes rowAttributes= new RowAttributes();
        View rowView;

        // Set up the UI elements
        rowView = inflater.inflate(R.layout.listview_layout_00, null);
        rowAttributes.text_00 = (TextView) rowView.findViewById(R.id.text_000);
        rowAttributes.text_01 = (TextView) rowView.findViewById(R.id.text_001);
        rowAttributes.text_02 = (TextView) rowView.findViewById(R.id.text_002);
        rowAttributes.text_03 = (TextView) rowView.findViewById(R.id.text_003);

        // Display the cars available at the shop
        rowAttributes.text_00.setText(foundedRentals.get(position).carsListing.size() + "");

        // Display the company name and code
        String placer_holder = foundedRentals.get(position).companyName + " (" +
                foundedRentals.get(position).companyCode + ")";
        rowAttributes.text_01.setText(placer_holder);

        // Display the address
        placer_holder = foundedRentals.get(position).addressLine1;
        rowAttributes.text_02.setText(placer_holder);

        // Display the city, state, and country
        placer_holder = foundedRentals.get(position).addressCity + ", " +
                foundedRentals.get(position).addressRegion + ", " +
                foundedRentals.get(position).addressCountry;
        rowAttributes.text_03.setText(placer_holder);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                // Head to the topic page screen
                /*Intent eIntent = new Intent(eContext, TopicPageScreen.class);
                eIntent.putExtra("urlLink",
                        "https://www.reddit.com" + foundedTopics.get(position).topicPermalink + ".json");
                v.getContext().startActivity(eIntent);*/

                // Head to the Car Rental Page screen
                Intent eIntent = new Intent(eContext, ScreenCarRentalPage.class);
                v.getContext().startActivity(eIntent);
            }
        });
        return rowView;
    }

}
