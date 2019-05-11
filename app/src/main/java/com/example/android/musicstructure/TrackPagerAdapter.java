package com.example.android.musicstructure;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TrackPagerAdapter extends FragmentStatePagerAdapter {

    public TrackPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    /**
     * Get the fragment for the current page position
     */
    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new TrackListFragment();
            case 1:
                return new TrackLikeFragment();
        }
        return null;
    }


    /**
     * Get the number of pages
     */
    @Override
    public int getCount() {
        return 2;
    }


    /**
     * Get the page title
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "TRACKS";
            case 1:
                return "LIKES";
            default:
                return null;
        }
    }
}
