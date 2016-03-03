/**
 * @author NileshM 09/02/2016
 */

package com.muhurtmaza.fragments;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.plus.PlusShare;
import com.muhurtmaza.R;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.utility.MuhurtMazaApplication;
import com.muhurtmaza.widget.MMToast;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RefferalFragment extends ParentFragment implements View.OnClickListener {

    TextView txtwhatsapp,txtFacebook,txtTwitter,txtEmail,txtSms,txtGoogle,txtUserName,txtCityName;

    CallbackManager callbackManager;
    ShareDialog shareDialog;
    de.hdodenhof.circleimageview.CircleImageView imgUserProfile;
    private static final String SharingMSG ="'MuhurtMaza' launches free app to help you Book Pooja for all occassions in only 3 clicks!! You focus on performing Pooja Rituals and leave the rest(Muhurt,Guruji & Samagree) to us.";
    private Toolbar mToolbar;
    private Context mContext;
    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_refferal, container, false);
        ButterKnife.inject(getActivity());
        FacebookSdk.sdkInitialize(getActivity());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(getActivity(),"Success field",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(),"Cancel field",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getActivity(),"Error field",Toast.LENGTH_SHORT).show();


            }
        });

        mContext = getActivity();

        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        mToolbar = (Toolbar) ((AppCompatActivity) getActivity()).findViewById(R.id.toolbar);
        mToolbar.setTitle("Referral");
        //imgBack = (ImageView) mToolbar.findViewById(R.id.img_back);
        //imgBack.setVisibility(View.VISIBLE);
        imgUserProfile= (CircleImageView) view.findViewById(R.id.imgUserProfile_ReferralFragment);
        txtUserName= (TextView) view.findViewById(R.id.txtUserName_ReferralFragment);
        txtCityName=(TextView) view.findViewById(R.id.txtCityName_ReferralFragment);
        txtwhatsapp = (TextView) view.findViewById(R.id.txtWhatsappInfo_ReferralFragment);
        txtFacebook = (TextView) view.findViewById(R.id.txtfbInfo_ReferralFragment);
        txtTwitter = (TextView) view.findViewById(R.id.txttwitterInfo_ReferralFragment);
        txtGoogle = (TextView) view.findViewById(R.id.txtgoogleplusInfo_ReferralFragment);
        txtEmail = (TextView) view.findViewById(R.id.txtemailInfo_ReferralFragment);
        txtSms = (TextView) view.findViewById(R.id.txtSmsInfo_ReferralFragment);
        txtGoogle= (TextView) view.findViewById(R.id.txtgoogleplusInfo_ReferralFragment);

/*get from shared preferences*/
        AppPreferences appPreferences = AppPreferences.getInstance(getActivity());

        String ProfileImage=appPreferences.getString(AppConstants.USER_PROFILE, "");
        String userName=appPreferences.getString(AppConstants.USER_NAME, "");
        String lName=appPreferences.getString(AppConstants.USER_LNAME,"");
        String fName=appPreferences.getString(AppConstants.USER_FNAME,"");
        String cityName=appPreferences.getString(AppConstants.USER_CITY,"");

        Log.d("City", appPreferences.getString(AppConstants.USER_CITY,""));
        /*setting profileimage ,username,city*/
        Glide.with(getActivity())
                .load(ProfileImage)
                .centerCrop()
                .placeholder(R.drawable.img_splash)
                .crossFade()
                .into(imgUserProfile);
        txtUserName.setText(userName);
        txtCityName.setText(cityName);


        txtwhatsapp.setOnClickListener(this);
        txtFacebook.setOnClickListener(this);
        txtTwitter.setOnClickListener(this);
        txtGoogle.setOnClickListener(this);
        txtEmail.setOnClickListener(this);
        txtSms.setOnClickListener(this);


    }
    private void txtwhatsapp() {

        try{
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, SharingMSG);
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
        }
        catch(Exception notfound){
            Toast.makeText(getActivity(),"App not found",Toast.LENGTH_SHORT).show();
        }
    }

    private void txtFacebook(View v) {

        try{
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle("Muhurt Maza")
                        .setContentDescription(
                                SharingMSG)
                        .setContentUrl(Uri.parse("http://muhurtmaza.com"))
                        .build();

                shareDialog.show(linkContent);
            }

        }
        catch(Exception notfound){
            Toast.makeText(getActivity(),"App not found",Toast.LENGTH_SHORT).show();
        }

    }

    private void txtTwitter() {

        try{
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, SharingMSG);
            intent.setType("image/jpeg");
            intent.setPackage("com.twitter.android");
            startActivity(intent);
        }
        catch(Exception notfound){
            Toast.makeText(getActivity(),"App not found",Toast.LENGTH_SHORT).show();
        }
    }
    private void txtGoogle() {

        try{
            // Launch the Google+ share dialog with attribution to your app.
            Intent googleIntent = new Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT, SharingMSG)
                    .setPackage("com.google.android.apps.plus");

            startActivity(googleIntent);
        }
        catch(Exception notfound){
            Toast.makeText(getActivity(),"App not found",Toast.LENGTH_SHORT).show();
        }
    }
    private void txtEmail() {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_VIEW);
            emailIntent.setType("plain/text");
            emailIntent.setData(Uri.parse(""));
            emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, " 'MuhurtMaza' ");
            emailIntent.putExtra(Intent.EXTRA_TEXT, SharingMSG);
            startActivity(emailIntent);
        }
        catch(Exception notfound){
            Toast.makeText(getActivity(),"App not found",Toast.LENGTH_SHORT).show();
        }
    }
    private void txtSms() {

        try{
            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("sms_body", SharingMSG);
            startActivity(smsIntent);
        }
        catch(Exception notfound){
            Toast.makeText(getActivity(),"App not found",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        if (v==txtwhatsapp){
            txtwhatsapp();
        } if (v==txtFacebook){
            txtFacebook(v);
        }
        if (v==txtTwitter){
            txtTwitter();
        }
        if (v==txtGoogle){
            txtGoogle();
        }
        if (v==txtEmail){
            txtEmail();
        }
        if (v==txtSms){
            txtSms();
        }
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