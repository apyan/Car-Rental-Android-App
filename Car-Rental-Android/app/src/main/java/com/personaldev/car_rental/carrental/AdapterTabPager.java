package com.personaldev.car_rental.carrental;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * The adapter for the tabs on the pager.
 */

public class AdapterTabPager extends FragmentStatePagerAdapter {

    // Variables of the adapter
    int tabAmount;

    public AdapterTabPager(FragmentManager fm, int tabNumber) {
        super(fm);
        this.tabAmount = tabNumber;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // Returns the fragment of the Search Tab
                FragmentSearchTab fragmentSearchTab = new FragmentSearchTab();
                return fragmentSearchTab;
            case 1:
                // Returns the fragment of the GeoMap Tab
                FragmentGeoMapTab fragmentGeoMapTab = new FragmentGeoMapTab();
                return fragmentGeoMapTab;
            case 2:
                // Returns the fragment of the Options Tab
                FragmentOptionsTab fragmentOptionsTab = new FragmentOptionsTab();
                return fragmentOptionsTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabAmount;
    }

}
