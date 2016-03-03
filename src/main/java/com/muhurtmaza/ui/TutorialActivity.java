package com.muhurtmaza.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.muhurtmaza.R;
import com.muhurtmaza.model.User;
import com.muhurtmaza.tutorial.CirclePageIndicator;
import com.muhurtmaza.tutorial.PlaceSlidesFragmentAdapter;

public class TutorialActivity extends ParentActivity {

    TextView btnSkipTutorial, txtTitlesName;
    Button btnExistingUser, btnNewUser;
    private Context context;
    ViewPager mPager;
    PlaceSlidesFragmentAdapter mAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tutorial);

        context = this;
        setUI();
        mAdapter = new PlaceSlidesFragmentAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);


        CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);

        mIndicator.setViewPager(mPager);

        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                String title = "";
                switch (position) {
                    case 0:
                        title = "Choose from 140+ pujas";
                        break;
                    case 1:
                        title = "Order complete samagree";
                        break;
                    case 2:
                        title = "Choose regional puja language";
                        break;
                    case 3:
                        title = "Get experienced pandit";
                        break;
                    case 4:
                        title = "Making your puja blissful";
                        break;
                }
                txtTitlesName.setText(title);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    private void setUI() {

        btnSkipTutorial = (TextView) findViewById(R.id.btn_skip_tutorial);
        btnNewUser = (Button) findViewById(R.id.btn_new_user);
        btnExistingUser = (Button) findViewById(R.id.btn_existing_user);
        txtTitlesName = (TextView) findViewById(R.id.txt_TitlesTutorial);

        btnSkipTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = User.getInstance();
                user.clear();
                Intent intent = new Intent(TutorialActivity.this, MainDrawerActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MMToast.getInstance().showShortToast("New user",context);
                Intent intent = new Intent(TutorialActivity.this, SignUpActivity.class); //Sign
                startActivity(intent);
                finish();
            }
        });

        btnExistingUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MMToast.getInstance().showShortToast("Existing user",context);
                Intent intent = new Intent(TutorialActivity.this, NewLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }


}