package com.muhurtmaza.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.muhurtmaza.R;


/**
 * Created by ishank on 10/30/2015.
 */

public final class PlaceSlideFragment extends Fragment {


    private static PlaceSlideFragment mFragment = null;
    private int imageResourceId = 0;

    public PlaceSlideFragment() {

    }

    public static PlaceSlideFragment getInstance(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("ImageRes", i);
        mFragment = new PlaceSlideFragment();
        mFragment.setArguments(bundle);

        return mFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            imageResourceId = getArguments().getInt("ImageRes");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.usp_detailing_content, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageView uspImageView = (ImageView) getView().findViewById(R.id.usp_image);
        uspImageView.setImageResource(imageResourceId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}