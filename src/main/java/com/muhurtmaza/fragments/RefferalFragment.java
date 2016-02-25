package com.muhurtmaza.fragments;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.plus.PlusShare;
import com.muhurtmaza.R;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.utility.MuhurtMazaApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RefferalFragment extends ParentFragment {

    TextView txtSms,txtEmail,txtFacebook,txtGoogle,txtTwitter,txtWhatsApp;
    private Toolbar mToolbar;
    //private ImageView imgBack;
    private Context mContext;
    private  TextView txtTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_refferal, container, false);
        ButterKnife.inject(getActivity());
        mContext = getActivity();
        setupUI(view);
        AppPreferences appPreferences = AppPreferences.getInstance(getActivity());
        String personName = appPreferences.getString(AppConstants.USER_NAME, "");
        String personPhotoUrl = appPreferences.getString(AppConstants.USER_PROFILE, "");
        ImageLoader imgLoader = MuhurtMazaApplication.getImageLoader(getActivity());
        return view;
    }

    private void setupUI(View view) {
        mToolbar = (Toolbar) ((AppCompatActivity) getActivity()).findViewById(R.id.toolbar);
        //imgBack = (ImageView) mToolbar.findViewById(R.id.img_back);
        //imgBack.setVisibility(View.VISIBLE);
        txtTitle= (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.txt_title1);
        txtTitle.setVisibility(View.INVISIBLE);

        txtSms = (TextView)view.findViewById(R.id.txtSmsInfo_ReferralFragment);
        txtEmail = (TextView)view.findViewById(R.id.txtemailInfo_ReferralFragment);
        txtFacebook = (TextView)view.findViewById(R.id.txtfbInfo_ReferralFragment);
        txtGoogle = (TextView)view.findViewById(R.id.txtgoogleplusInfo_ReferralFragment);
        txtTwitter=(TextView)view.findViewById(R.id.txttwitterInfo_ReferralFragment);
        txtWhatsApp=(TextView)view.findViewById(R.id.txtWhatsappInfo_ReferralFragment);

        txtSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("sms_body", " 'MuhurtMaza' launches free app to help you book Pooja for all occasions in just 3 clicks!! ");
                startActivity(smsIntent);
            }
        });

        txtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_VIEW);
                emailIntent.setType("plain/text");
                emailIntent.setData(Uri.parse(""));
                emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, " 'MuhurtMaza' ");
                emailIntent.putExtra("email_body", "'MuhurtMaza' launches free app to help you Book Pooja for all occassions in only 3 clicks!! You focus on performing Pooja Rituals and leave the rest(Muhurt,Guruji & Samagree) to us.");
                startActivity(emailIntent);
            }
        });
        txtFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Example text");
                shareIntent.setPackage("com.facebook.katana");
                startActivity(shareIntent);
          /*      Intent fbIntent = new Intent(Intent.ACTION_VIEW);
                fbIntent.setType("plain/text");
                fbIntent.putExtra(Intent.ACTION_VIEW, "'MuhurtMaza' launches free app to help you Book Pooja for all occassions in only 3 clicks!! You focus on performing Pooja Rituals and leave the rest(Muhurt,Guruji & Samagree) to us.");
                String uri;
                fbIntent.putExtra(Intent.ACTION_VIEW, Uri.parse(""));
                PackageManager pm = v.getContext().getPackageManager();
                List<ResolveInfo> activityList = pm.queryIntentActivities(fbIntent, 0);
                for (final ResolveInfo app : activityList) {
                    if ((app.activityInfo.name).contains("facebook")) {
                        final ActivityInfo activity = app.activityInfo;
                        final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                        fbIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                        fbIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        fbIntent.setComponent(name);
                        v.getContext().startActivity(fbIntent);
                        break;
                    }
                }*/
            }
        });
        txtGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent googleIntent = new PlusShare.Builder(getActivity()).setType("text/plain")
                        .setText("'MuhurtMaza' launches free app to help you Book Pooja for all occassions in only 3 clicks!! You focus on performing Pooja Rituals and leave the rest(Muhurt,Guruji & Samagree) to us.").getIntent();
                startActivityForResult(googleIntent, 0);
            }
        });
        txtWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra("msg_body", "'MuhurtMaza' launches free app to help you Book Pooja for all occassions in only 3 clicks!! You focus on performing Pooja Rituals and leave the rest(Muhurt,Guruji & Samagree) to us.");
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
            }
        });
        txtTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent twitIntent = new Intent(Intent.ACTION_SEND);
                twitIntent.putExtra(Intent.EXTRA_TEXT, "Example text");
                twitIntent.setType("application/twitter");
                startActivity(twitIntent);           }
        });



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        txtTitle.setVisibility(View.INVISIBLE);
    }
}
