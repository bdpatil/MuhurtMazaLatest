package com.muhurtmaza.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.muhurtmaza.R;
import com.muhurtmaza.adapter.PoojaTypesAdapter;
import com.muhurtmaza.adapter.ViewPagerAdapter;
import com.muhurtmaza.ui.NewSearchCityActivity;
import com.muhurtmaza.ui.NotificationActivity;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.utility.BadgeUtils;
import com.muhurtmaza.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 22-01-2016.
 */
public class NewPoojaBookingFragment extends ParentFragment{

    private LinearLayoutManager layoutManager;
    private MenuItem cityMenuItem,cityNameItem;
    private Context mContext;
    private SearchView mSearchView;
    AppPreferences appPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_new_pooja_bookings, container, false);
        mContext = getActivity();
        setHasOptionsMenu(true);
        setUI(rootView);
        return rootView;
    }

    private void setUI(View rview){

        final ViewPager viewPager = (ViewPager)rview.findViewById(R.id.new_pooja_bookings_viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) rview.findViewById(R.id.tabanim_tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        showToast("All Pooja");
                        break;
                    case 1:
                        showToast("Seasonal");

                        break;
                    case 2:
                        showToast("Popular");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
    void showToast(String msg) {

        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(new FragmentAllPooja(), "All");
        adapter.addFrag(new FragmentSeasonalPooja(), "Seasonal");
        adapter.addFrag(new FragmentPopularPooja(), "Popular");
        viewPager.setAdapter(adapter);
    }
    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        appPreferences = AppPreferences.getInstance(mContext);
        inflater.inflate(R.menu.menu_book_pooja, menu);
        cityMenuItem = menu.findItem(R.id.action_city);
        cityMenuItem.setVisible(true);
        cityNameItem = menu.findItem(R.id.city_name);
        cityNameItem.setVisible(true);
        String cityname =appPreferences.getString(AppConstants.CITY_NAME_ADAPTER, "");
        if(cityname==""){
            cityname="Pune";
        }
        cityNameItem.setTitle(cityname);

//        notifyMenuItem = menu.findItem(R.id.action_notify);
//        notifyMenuItem.setVisible(false);

//        LayerDrawable icon = (LayerDrawable)notifyMenuItem.getIcon();
//        BadgeUtils.setBadgeCount(mContext, icon, 5);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        if(item.getItemId() == R.id.action_city){
            Intent intent = new Intent(mContext, NewSearchCityActivity.class);
            getActivity().startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}
