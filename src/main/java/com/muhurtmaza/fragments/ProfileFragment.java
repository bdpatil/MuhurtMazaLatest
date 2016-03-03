package com.muhurtmaza.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.muhurtmaza.R;
import com.muhurtmaza.ui.EditProfileActivity;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.widget.MMToast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bharat on 01-12-2015.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    de.hdodenhof.circleimageview.CircleImageView imgUserProfile;
    TextView txtFirstName,txtLastName,txtLanguage,txtEmail,txtMobileNo,txtAddress,txtCity;
    private android.support.v7.widget.Toolbar mToolBar;
    private Context context;
    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.inject(getActivity());
        setupUI(view);

        return view;
    }

    private void setupUI(View view) {

        mToolBar = (Toolbar) ((AppCompatActivity) getActivity()).findViewById(R.id.toolbar);
        setHasOptionsMenu(true);
        mToolBar.setTitle("Profile");


        //Ids related to the fragment_Profile xml..bharat

        imgUserProfile= (CircleImageView) view.findViewById(R.id.imgNewUserProfile_ProfileFragment);
        txtFirstName= (TextView) view.findViewById(R.id.txt_fnameProfileFragment);
        txtLastName= (TextView) view.findViewById(R.id.txt_lnameProfileFragment);
        txtLanguage= (TextView) view.findViewById(R.id.txt_languageProfileFragment);
        txtEmail= (TextView) view.findViewById(R.id.txt_emailProfileFragment);
        txtMobileNo= (TextView) view.findViewById(R.id.txt_mobileNoProfileFragment);
        txtAddress= (TextView) view.findViewById(R.id.txt_addressProfileFragment);
        txtCity= (TextView) view.findViewById(R.id.txt_cityProfileFragment);

        AppPreferences appPreferences = AppPreferences.getInstance(getActivity());
        String str_fnameProfile = appPreferences.getString(AppConstants.USER_FNAME, "");
        String str_lNameProfile = appPreferences.getString(AppConstants.USER_LNAME,"");
        String str_languageProfile = appPreferences.getString(AppConstants.USER_LANGUAGE,"");
        String str_emailProfile = appPreferences.getString(AppConstants.USER_EMAIL,"");
        String str_mobileNoProfile = appPreferences.getString(AppConstants.USER_NUMBER,"");
        String str_addressProfile = appPreferences.getString(AppConstants.USER_ADDRESS,"");
        String str_cityProfile = appPreferences.getString(AppConstants.USER_CITY,"");
        String ProfileImage=appPreferences.getString(AppConstants.USER_PROFILE,"");

        Log.d("Image", appPreferences.getString(AppConstants.USER_PROFILE, ""));

        if(str_fnameProfile!=null && !str_fnameProfile.equals("null") && !str_fnameProfile.equals(""))
            txtFirstName.setText(str_fnameProfile);

        if(str_lNameProfile!=null && !str_lNameProfile.equals("null") && !str_lNameProfile.equals(""))
            txtLastName.setText(str_lNameProfile);

        if(str_languageProfile!=null && !str_languageProfile.equals("null") && !str_languageProfile.equals(""))
            txtLanguage.setText(str_languageProfile);

        if(str_emailProfile!=null && !str_emailProfile.equals("null") && !str_emailProfile.equals(""))
            txtEmail.setText(str_emailProfile);

        if(str_mobileNoProfile!=null && !str_mobileNoProfile.equals("null") && !str_mobileNoProfile.equals(""))
            txtMobileNo.setText(str_mobileNoProfile);

        if(str_addressProfile!=null && !str_addressProfile.equals("null") && !str_addressProfile.equals(""))
            txtAddress.setText(str_addressProfile);

        if(str_cityProfile!=null && !str_cityProfile.equals("null") && !str_cityProfile.equals(""))
            txtCity.setText(str_cityProfile);

        Glide.with(getActivity())
                .load(ProfileImage)
                .centerCrop()
                .placeholder(R.drawable.img_splash)
                .crossFade()
                .into(imgUserProfile);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile_view, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent i = new Intent(getActivity(),EditProfileActivity.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    public void onClick(View v) {


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
