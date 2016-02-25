package com.muhurtmaza.adapter;

import android.support.v4.app.FragmentPagerAdapter;

import com.muhurtmaza.fragments.ParentFragment;

import java.util.List;

/**
 * Created by admin on 04-02-2016.
 */
public class PoojaTypesAdapter extends FragmentPagerAdapter {

    private int COUNT = 3;
    private List<ParentFragment> mList;
    private String [] lProfile = {"All","Seasonal","Popular"};

    public PoojaTypesAdapter(android.support.v4.app.FragmentManager fragmentManager,List<ParentFragment> lFragments) {
        super(fragmentManager);
        this.mList = lFragments;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int pPosition) {
        return mList.get(pPosition);
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return lProfile[position];
    }
}
