package com.example.app.material;

import android.service.quicksettings.Tile;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private static int TAB_COUNT = 3;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return ListContentFragment.newInstance();
            case 1:
                return TileContentFragment.newInstance();
            case 2:
                return CardContentFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return ListContentFragment.TITLE;
            case 1:
                return TileContentFragment.TITLE;
            case 2:
                return CardContentFragment.TITLE;
        }
        return super.getPageTitle(position);
    }
}