package com.muhurtmaza.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.muhurtmaza.R;
import com.muhurtmaza.fragments.MyBookingsFragment;
import com.muhurtmaza.fragments.NewPoojaBookingFragment;
import com.muhurtmaza.fragments.ProfileFragment;
import com.muhurtmaza.fragments.RefferalFragment;
import com.muhurtmaza.fragments.SettingFragment;
import com.muhurtmaza.model.User;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;

import javax.crypto.SecretKey;

public class MainDrawerActivity extends ParentActivity implements NavigationView.OnNavigationItemSelectedListener, NewPoojaBookingFragment.OnFragmentInteractionListener, MyBookingsFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener, RefferalFragment.OnFragmentInteractionListener, SettingFragment.OnFragmentInteractionListener {


    private Context mContext;
    Fragment fragment = null;
    Class fragmentClass = null;
    FragmentManager fragmentManager = null;
    private String entrySource = "";
    de.hdodenhof.circleimageview.CircleImageView imgBack;
    private TextView txtTitle;
    AppPreferences appPreferences;
    public String userid;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = NewPoojaBookingFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mContext = this;
        FacebookSdk.sdkInitialize(mContext);
        appPreferences = AppPreferences.getInstance(mContext);
        userid = appPreferences.getString(AppConstants.USER_ID, "");
        user = User.getInstance();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //MMToast.getInstance().showShortToast("try ",mContext);
        //setUI();

//        View drawerHeader = navigationView.inflateHeaderView(R.layout.nav_header_new);
        if (user.getUserid() == null) {
            Menu menu = navigationView.getMenu();
            MenuItem nav_logout = menu.findItem(R.id.nav_log_out);
            nav_logout.setTitle("Login");
        }
     //   imgBack = (de.hdodenhof.circleimageview.CircleImageView) drawerHeader.findViewById(R.id.img_back);


        final ActionBar ab = getSupportActionBar();
        ab.setTitle("Book Pooja");
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);


        if (navigationView != null) {
            //setupDrawerContent(navigationView);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }


    private void setUI() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View drawerHeader = navigationView.inflateHeaderView(R.layout.nav_header_new);
        if (user.getUserid() == null) {
            Menu menu = navigationView.getMenu();
            MenuItem nav_logout = menu.findItem(R.id.nav_log_out);
            nav_logout.setTitle("Login");
        }
        imgBack = (de.hdodenhof.circleimageview.CircleImageView) drawerHeader.findViewById(R.id.img_back);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setTitle("Book Pooja");
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);


        if (navigationView != null) {
            //setupDrawerContent(navigationView);
        }

        try {
            Log.d("Call fragment", "Fragments");
            fragmentClass = NewPoojaBookingFragment.class;
            fragmentManager = getSupportFragmentManager();
            fragment = (Fragment) fragmentClass.newInstance();
            fragmentManager.beginTransaction().add(R.id.flContent, fragment).commit();
        } catch (Exception e) {
            Log.d("Error", "Error data=" + e.toString());
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    /*private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {

        boolean isClose=true;
        switch (menuItem.getItemId()){

            case R.id.nav_book_pooja:
                fragmentClass = NewPoojaBookingFragment.class;
                break;

            case R.id.nav_my_bookings:
                if(user.getUserid()==null){
                    showLogin();
                    fragmentClass = NewPoojaBookingFragment.class;
                }else
                    fragmentClass = MyBookingsFragment.class;
                break;

            case R.id.nav_profile:
                if(user.getUserid()==null){
                    showLogin();
                    fragmentClass = NewPoojaBookingFragment.class;
                }else
                fragmentClass = ProfileFragment.class;
                break;

            case R.id.nav_referral:
                if(user.getUserid()==null){
                    showLogin();
                    fragmentClass = NewPoojaBookingFragment.class;
                }else
                fragmentClass = RefferalFragment.class;
                break;

            case R.id.nav_settings:
                if(user.getUserid()==null){
                    showLogin();
                    fragmentClass = NewPoojaBookingFragment.class;
                }else
                fragmentClass = SettingFragment.class;
                break;

            case R.id.nav_log_out:
                if(user.getUserid()==null){

                    Intent intent = new Intent(mContext, NewLoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }else {
                    isClose = false;
                    try {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainDrawerActivity.this);
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        final AppPreferences appPreferences = AppPreferences.getInstance(mContext);

                                        Log.d("Sign up source", "Source=" + AppConstants.SIGN_UP_SOURCE);
                                        if (userid == null || userid.equals("") || userid.equals("null")) {
                                            Intent intent = new Intent(mContext, TutorialActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();

                                        } else if (appPreferences.getString(AppConstants.SIGN_UP_SOURCE, "").equals(AppConstants.FACEBOOK_SIGN_UP)) {

                                            LoginManager.getInstance().logOut();
                                            appPreferences.deleteAllPreferences();

                                            //	Log.d(TAG, "Logged out from FACEBOOK ");
                                            Intent intent = new Intent(MainDrawerActivity.this, NewLoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();

                                        } else if (appPreferences.getString(
                                                AppConstants.SIGN_UP_SOURCE, "").equals(AppConstants.GOOGLE_SIGN_UP)) {


                                            AppPreferences.getInstance(MainDrawerActivity.this).putString(AppConstants.USER_ID, "");
                                            AppPreferences.getInstance(MainDrawerActivity.this).putBoolean("isGoogleLogin", false);
                                            Intent intent = new Intent(MainDrawerActivity.this, NewLoginActivity.class);
                                            intent.putExtra(AppConstants.LOGOUT_FROM, AppConstants.GOOGLE_SIGN_UP);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();

                                        } else if (appPreferences.getString(AppConstants.SIGN_UP_SOURCE, "").equals(AppConstants.MANUAL_LOGIN) ||
                                                appPreferences.getString(AppConstants.SIGN_UP_SOURCE, "").equals(AppConstants.NEW_USER_SIGN_UP_SOURCE)) {

                                            appPreferences.deleteAllPreferences();
                                            Intent intent = new Intent(MainDrawerActivity.this, NewLoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }

                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //no changes
                                        break;
                                }
                            }
                        };
                        builder.setTitle("Log out").setMessage("Are you sure you want to Log out?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;

            default:
                break;
        }

        if (isClose){
            try {
                fragment = (Fragment)fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            // Highlight the selected item, update the title, and close the drawer
            menuItem.setChecked(true);
            Log.d("Set title",""+menuItem.getTitle());
            setTitle(menuItem.getTitle());
            mDrawerLayout.closeDrawers();

        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // Handle navigation view item clicks here.
        boolean isClose = true;
        switch (menuItem.getItemId()) {

            case R.id.nav_book_pooja:
                fragmentClass = NewPoojaBookingFragment.class;
                break;

            case R.id.nav_my_bookings:
                if (user.getUserid() == null) {
                    showLogin();
                    fragmentClass = NewPoojaBookingFragment.class;
                } else
                    fragmentClass = MyBookingsFragment.class;
                break;

            case R.id.nav_profile:
                if (user.getUserid() == null) {
                    showLogin();
                    fragmentClass = NewPoojaBookingFragment.class;
                } else
                    fragmentClass = ProfileFragment.class;
                break;

            case R.id.nav_referral:
                if (user.getUserid() == null) {
                    showLogin();
                    fragmentClass = NewPoojaBookingFragment.class;
                } else
                    fragmentClass = RefferalFragment.class;
                break;

            case R.id.nav_settings:
                if (user.getUserid() == null) {
                    showLogin();
                    fragmentClass = NewPoojaBookingFragment.class;
                } else
                    fragmentClass = SettingFragment.class;
                break;

            case R.id.nav_log_out:
                if (user.getUserid() == null) {

                    Intent intent = new Intent(mContext, NewLoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {
                    isClose = false;
                    try {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainDrawerActivity.this);
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        final AppPreferences appPreferences = AppPreferences.getInstance(mContext);

                                        Log.d("Sign up source", "Source=" + AppConstants.SIGN_UP_SOURCE);
                                        if (userid == null || userid.equals("") || userid.equals("null")) {
                                            Intent intent = new Intent(mContext, TutorialActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();

                                        } else if (appPreferences.getString(AppConstants.SIGN_UP_SOURCE, "").equals(AppConstants.FACEBOOK_SIGN_UP)) {

                                            LoginManager.getInstance().logOut();
                                            appPreferences.deleteAllPreferences();

                                            //	Log.d(TAG, "Logged out from FACEBOOK ");
                                            Intent intent = new Intent(MainDrawerActivity.this, NewLoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();

                                        } else if (appPreferences.getString(
                                                AppConstants.SIGN_UP_SOURCE, "").equals(AppConstants.GOOGLE_SIGN_UP)) {


                                            AppPreferences.getInstance(MainDrawerActivity.this).putString(AppConstants.USER_ID, "");
                                            AppPreferences.getInstance(MainDrawerActivity.this).putBoolean("isGoogleLogin", false);
                                            Intent intent = new Intent(MainDrawerActivity.this, NewLoginActivity.class);
                                            intent.putExtra(AppConstants.LOGOUT_FROM, AppConstants.GOOGLE_SIGN_UP);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();

                                        } else if (appPreferences.getString(AppConstants.SIGN_UP_SOURCE, "").equals(AppConstants.MANUAL_LOGIN) ||
                                                appPreferences.getString(AppConstants.SIGN_UP_SOURCE, "").equals(AppConstants.NEW_USER_SIGN_UP_SOURCE)) {

                                            appPreferences.deleteAllPreferences();
                                            Intent intent = new Intent(MainDrawerActivity.this, NewLoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }

                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //no changes
                                        break;
                                }
                            }
                        };
                        builder.setTitle("Log out").setMessage("Are you sure you want to Log out?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;

            default:
                break;
        }

        if (isClose) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            // Highlight the selected item, update the title, and close the drawer
            menuItem.setChecked(true);
            Log.d("Set title", "" + menuItem.getTitle());
            setTitle(menuItem.getTitle());


        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void showLogin() {
        Toast.makeText(mContext, "You need to login to access this feature", Toast.LENGTH_SHORT).show();
    }


}
