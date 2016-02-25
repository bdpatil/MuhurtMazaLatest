package com.muhurtmaza.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muhurtmaza.R;
import com.muhurtmaza.adapter.FragmentAdapter;

/**
 * Created by admin on 30-11-2015.
 */
public class MyBookingsFragment extends ParentFragment {

    private LinearLayoutManager layoutManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_bookings, container, false);

        ViewPager viewPager = (ViewPager)rootView.findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout)rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentAdapter adapter = new FragmentAdapter(getFragmentManager());
        adapter.addFragment(new AllBookedPoojaFragment(), "All");
        adapter.addFragment(new UpcomingBookedPoojaFragment(), "Upcoming");
        viewPager.setAdapter(adapter);
    }
}

