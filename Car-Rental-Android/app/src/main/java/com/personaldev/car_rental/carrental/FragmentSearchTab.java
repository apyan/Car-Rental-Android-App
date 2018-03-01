package com.personaldev.car_rental.carrental;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.personaldev.car_rental.carrental.AppFunctions.AppConnect;

/**
 * The fragment that displays the user searches.
 */

public class FragmentSearchTab extends Fragment implements View.OnClickListener {

    // Variables used for the fragment
    public AppConnect appConnect;
    public Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set up the fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        // Set up needed classes
        context = getActivity();
        appConnect = new AppConnect(getActivity());

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // Location Services (GPS) button
            case R.id.button_00:
                // Opens up the location menu
                break;
            default:
                break;
        }
    }
}
