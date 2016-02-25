package com.muhurtmaza.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.gson.Gson;
import com.muhurtmaza.R;
import com.muhurtmaza.model.PoojaBookModel;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;

/**
 * Created by imac on 15/12/15.
 */
public class FragmentPoojaPayment extends ParentFragment {

    SwitchCompat payNowSwitch,payToGurujiSwitch;

    PoojaBookModel poojaBookModelPayment;
    private Context mContext;
    AppPreferences appPreferences;

    Gson gson;
    String json="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pooja_details_payment, container, false);

        payNowSwitch = (SwitchCompat) v.findViewById(R.id.paynowswitch);
        payToGurujiSwitch = (SwitchCompat) v.findViewById(R.id.paytogurujiswitch);

        mContext = getActivity();
        appPreferences = AppPreferences.getInstance(mContext);

        poojaBookModelPayment = new PoojaBookModel();

        gson = new Gson();

      /*  String json = appPreferences.getString(AppConstants.PREF_POOJA_BOOK_PAYMENT, "");
        poojaBookModelPayment = gson.fromJson(json, PoojaBookModel.class);

        MMToast.getInstance().showLongToast("Pooja Payment model="+poojaBookModelPayment.toString(),mContext);*/

        poojaBookModelPayment.setmMethod(AppConstants.POOJA_BOOKING_MODE_GATEWAY);

        json = gson.toJson(poojaBookModelPayment);
        appPreferences.putString(AppConstants.PREF_POOJA_BOOK_PAYMENT,json);

        setupUI();
        return v;
    }

    private void setupUI(){
        payNowSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    payToGurujiSwitch.setChecked(false);
                    poojaBookModelPayment.setmMethod(AppConstants.POOJA_BOOKING_MODE_COD);

                    json = gson.toJson(poojaBookModelPayment);
                    appPreferences.putString(AppConstants.PREF_POOJA_BOOK_PAYMENT,json);

                } else {
                    payToGurujiSwitch.setChecked(true);
                }
            }
        });

        payToGurujiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    payNowSwitch.setChecked(false);
                    poojaBookModelPayment.setmMethod(AppConstants.POOJA_BOOKING_MODE_GATEWAY);

                    json = gson.toJson(poojaBookModelPayment);
                    appPreferences.putString(AppConstants.PREF_POOJA_BOOK_PAYMENT,json);

                } else {
                    payNowSwitch.setChecked(true);
                }
            }
        });
    }


    public static FragmentPoojaPayment newInstance(String text) {

        FragmentPoojaPayment f = new FragmentPoojaPayment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}
