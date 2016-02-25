package com.muhurtmaza.tutorial;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.muhurtmaza.R;

/**
 * Created by bharat on 10/30/2015.
 */
public class PlaceSlidesFragmentAdapter extends FragmentPagerAdapter implements
        IconPagerAdapter {


    protected static final int[] ICONS = new int[]{R.drawable.tutorial_one,
            R.drawable.tutorial_two, R.drawable.tutorial_three, R.drawable.tutorial_four,R.drawable.tutorial_five};


    private int mCount = ICONS.length;

    public PlaceSlidesFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return PlaceSlideFragment.getInstance(ICONS[position]);
    }


    @Override
    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getIconResId(int index) {
        return ICONS[index % ICONS.length];
    }

}