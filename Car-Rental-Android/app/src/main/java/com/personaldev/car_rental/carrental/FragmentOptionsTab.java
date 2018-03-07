package com.personaldev.car_rental.carrental;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.personaldev.car_rental.carrental.AppFunctions.AppConnect;
import com.personaldev.car_rental.carrental.AppFunctions.AppJSONStorage;

/**
 * The fragment that displays options for the user.
 */

public class FragmentOptionsTab extends Fragment implements View.OnClickListener {

    // Variables used for the fragment
    public AppConnect appConnect;
    public AppJSONStorage appJSONStorage;
    public Context context;
    public Intent locationIntent;

    // UI Elements
    public Button button_00;
    public EditText editText_00;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set up the fragment
        View v = inflater.inflate(R.layout.fragment_options, container, false);

        // Set up needed classes
        context = getActivity();
        appConnect = new AppConnect(getActivity());
        appJSONStorage = new AppJSONStorage(context, getString(R.string.user_file));
        appJSONStorage.dataRead();

        // Set up UI elements
        button_00 = (Button) v.findViewById(R.id.button_00);
        editText_00 = (EditText) v.findViewById(R.id.editText_00);

        // Button onClick Inputs
        button_00.setOnClickListener(this);

        // Initialize the UI setup
        editText_00.setText(appJSONStorage.radiusSearch + "");
        editText_00.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // EditText lost focus
                if(!hasFocus) {
                    Editable input = editText_00.getText();
                    // Ensures that the minimum radius is 5 km
                    if((input.length() == 0) || (Integer.parseInt(input.toString()) < 5)) {
                        editText_00.setText("5");
                        appJSONStorage.radiusSearch = 5;

                        // Ensures that the maximum radius is 99 km
                    } else if(input.length() > 2) {
                        editText_00.setText("99");
                        appJSONStorage.radiusSearch = 99;
                    } else {
                        appJSONStorage.radiusSearch = Integer.parseInt(input.toString());
                    }
                    appJSONStorage.dataWrite();
                    appJSONStorage.dataRead();
                }
            }
        });

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // Location Services (GPS) button
            case R.id.button_00:
                // Opens up the location menu
                locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(locationIntent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        editText_00.setText(appJSONStorage.radiusSearch + "");
    }
}
