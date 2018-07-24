package com.example.ducvu212.gallary;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private static final int LINEAR_MANAGER = 0;
    private static final int STAGGED_MANAGER = 1;
    private static final int GRID_MANAGER = 2;
    private static final int TOTAL_MANAGER = 3;
    // tab titles
    private String[] tabTitles = new String[]{"Linear", "Stagged", "Grid"};

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int i) {

        Fragment fragment = null;
        switch (i) {
            case LINEAR_MANAGER:
                fragment = LinearFragment.newInstance();
                break;

            case STAGGED_MANAGER:
                fragment = StaggedFragment.newInstance();
                break;

            case GRID_MANAGER:
                fragment = GridFragment.newInstance();
                break;

            default:
                return null;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return TOTAL_MANAGER;
    }
}
