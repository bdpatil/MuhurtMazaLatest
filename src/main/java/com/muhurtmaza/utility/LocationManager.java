
package com.muhurtmaza.utility;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender.SendIntentException;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * Created by Sandeep on 11/22/15.
 */

public class LocationManager implements LocationListener, ConnectionCallbacks,
        OnConnectionFailedListener {

    private static final int REQUEST_CHECK_SETTINGS = 101;

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

    // App context
    private Context mContext;

    // Stores location received from location client
    private Location mLocation;

    /*
     * Flag that indicates whether an add or remove request is underway. Check this flag before
     * attempting to start a new request.
     */
    private boolean mInProgress;

    private LocationCallbackListener locationCallbackListener;

    public LocationManager(Context context, LocationCallbackListener listener) {

        this.mContext = context;
        this.locationCallbackListener = listener;
        this.mInProgress = false;
    }

    public void fetchLocation() throws UnsupportedOperationException {
        // If a request is not already in progress
        if (!mInProgress) {

            // Toggle the flag and continue
            mInProgress = true;

            // Request a connection to Location Services
            getLocation();

            // If a request is in progress
        } else {

            // Throw an exception and stop the request
            throw new UnsupportedOperationException();
        }
    }

    private void getLocation() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        // mLocationRequest = LocationRequest.create()/*
        // * .setInterval(10000) // 10 seconds
        // * .setFastestInterval(5000) // 5 seconds
        // */.setPriority(
        // LocationRequest.PRIORITY_HIGH_ACCURACY);
        try {

            int lGooglePlayServiceStatus = GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(mContext);

            if (lGooglePlayServiceStatus == ConnectionResult.SUCCESS) {

                LocationSettingsRequest.Builder lLocationBuilder = new LocationSettingsRequest.Builder()
                        .addLocationRequest(/* mLocationRequest */LocationRequest.create())
                        .setAlwaysShow(true);

                PendingResult<LocationSettingsResult> lLocationResult = LocationServices.SettingsApi
                        .checkLocationSettings(mGoogleApiClient, lLocationBuilder.build());

                lLocationResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(LocationSettingsResult result) {
                        final Status lStatus = result.getStatus();

                        switch (lStatus.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                // All location settings are satisfied. The client
                                // can initialize location
                                // requests here.

                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be
                                // fixed by showing the user
                                // a dialog.
                                try {
                                    // Show the dialog by calling
                                    // startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    lStatus.startResolutionForResult(
                                            ((Activity) mContext),
                                            REQUEST_CHECK_SETTINGS);
                                } catch (SendIntentException e) {
                                    // Ignore the error.
                                } catch (ClassCastException e) {
                                    // Ignore the error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we
                                // have no way to fix the
                                // settings so we won't show the dialog.

                                break;
                        }
                    }

                });
            } else {

                if (GooglePlayServicesUtil
                        .isUserRecoverableError(lGooglePlayServiceStatus)) {
                    GooglePlayServicesUtil.getErrorDialog(
                            lGooglePlayServiceStatus, ((Activity) mContext), 10).show();
                } else {
//                    Toast.makeText(
//                            mContext,
//                            mContext.getResources()
//                                    .getString(
//                                            R.string.this_device_does_not_support_location_service),
//                            Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            mLocation = location;

            if (locationCallbackListener != null) {
                locationCallbackListener.onLocationReceived(mLocation);
            }
        }

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

        mInProgress = false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {

    }

    @Override
    public void onConnectionSuspended(int arg0) {
            mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle arg0) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                getCurrentLocation();
            }
        }, 2000);
    }

    private void getCurrentLocation() {
        // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
        // mLocationRequest, this);
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation != null) {
            locationCallbackListener.onLocationReceived(mLocation);
        }

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

        mInProgress = false;
    }

    public interface LocationCallbackListener {
        public abstract void onLocationReceived(Location location);
    }
}
