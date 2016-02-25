package com.muhurtmaza.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.muhurtmaza.R;
import com.muhurtmaza.model.PoojaBookModel;
import com.muhurtmaza.model.PoojaDetailModel;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.webservice.response.PoojaDetailsResponse;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by imac on 15/12/15.
 */
public class FragmentPoojaPrices extends ParentFragment implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    SwitchCompat withoutSamagriSwitch, withSamagriSwitch;
    TextView price, withSamagriPrice, withoutSamagriPrice;
    String withSamagriP, withoutSamagriP;
    public static final String RUPEE = "\u20B9 ";
    AppPreferences appPreferences;
    EditText muhurDate;
    private Context mContext;
    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;

    Gson gson;
    String json="";
    PoojaBookModel poojaBookModelPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pooja_details_price, container, false);
        withoutSamagriP = "";
        withSamagriP = "";

        mContext = getActivity();
        appPreferences = AppPreferences.getInstance(mContext);

        withoutSamagriSwitch = (SwitchCompat) v.findViewById(R.id.withoutsamagriswitch);
        withSamagriSwitch = (SwitchCompat) v.findViewById(R.id.withsamagriswitch);
        withoutSamagriPrice = (TextView) v.findViewById(R.id.withoutsamagriprice);
        withSamagriPrice = (TextView) v.findViewById(R.id.withsamagriprice);
        price = (TextView) getActivity().findViewById(R.id.pooja_price);

        muhurDate = (EditText) v.findViewById(R.id.muhurtdate);
        poojaBookModelPrice = new PoojaBookModel();

       /* String json = appPreferences.getString(AppConstants.PREF_POOJA_BOOK_PRICE, "");
        poojaBookModelPrice = gson.fromJson(json, PoojaBookModel.class);

        MMToast.getInstance().showLongToast("Pooja Price model="+poojaBookModelPrice.toString(),mContext);*/

        poojaBookModelPrice.setmWithSamagri(AppConstants.POOJA_WITHOUT_SAMAGREE);
        poojaBookModelPrice.setmCost("");

        gson = new Gson();
        json = gson.toJson(poojaBookModelPrice);
        appPreferences.putString(AppConstants.PREF_POOJA_BOOK_PRICE,json);

        setupUI();

        return v;
    }


    private void setupUI() {
        //setup calendar
        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        withSamagriSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    withoutSamagriSwitch.setChecked(false);
                    price.setText(RUPEE + "" + withSamagriP);
                    poojaBookModelPrice.setmCost(withSamagriP);
                    poojaBookModelPrice.setmWithSamagri(AppConstants.POOJA_WITH_SAMAGREE);

                    json = gson.toJson(poojaBookModelPrice);
                    appPreferences.putString(AppConstants.PREF_POOJA_BOOK_PRICE,json);
                } else {
                    withoutSamagriSwitch.setChecked(true);

                }
            }
        });

        withoutSamagriSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    withSamagriSwitch.setChecked(false);
                    price.setText(RUPEE + "" + withoutSamagriP);
                    poojaBookModelPrice.setmCost(withoutSamagriP);
                    poojaBookModelPrice.setmWithSamagri(AppConstants.POOJA_WITHOUT_SAMAGREE);

                    json = gson.toJson(poojaBookModelPrice);
                    appPreferences.putString(AppConstants.PREF_POOJA_BOOK_PRICE,json);
                } else {
                    withSamagriSwitch.setChecked(true);

                }
            }
        });
        muhurDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                Date date = new Date();
                now.setTime(date);
                now.add(Calendar.DAY_OF_YEAR, 1);
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        FragmentPoojaPrices.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );


                dpd.setMinDate(now);
                dpd.setThemeDark(false);
                dpd.vibrate(true);
                dpd.dismissOnPause(false);
                dpd.showYearPickerFirst(false);
                dpd.setAccentColor(Color.parseColor("#d0631f"));
                dpd.setTitle("Select Muhurt");

                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        DatePickerDialog dpd = (DatePickerDialog) getActivity().getFragmentManager().findFragmentByTag("Datepickerdialog");
        if (dpd != null) dpd.setOnDateSetListener(this);
    }


    public static FragmentPoojaPrices newInstance(String text) {

        FragmentPoojaPrices f = new FragmentPoojaPrices();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    public void RecievedResponse(PoojaDetailsResponse response) {

        PoojaDetailModel rowListItem = response.getmList();
        withSamagriP = rowListItem.getmPoojaWithSamagriCost();
        withoutSamagriP = rowListItem.getmPoojaWithoutSamagriCost();
        withoutSamagriPrice.setText("Rs " + withoutSamagriP + " ");
        withSamagriPrice.setText("Rs " + withSamagriP + " ");

    }


    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = ++monthOfYear;
        String date = dayOfMonth + "/" + (month) + "/" + year;
        muhurDate.setText(date);
        String dateWithFormat = year + "-" + month + "-" + dayOfMonth;
        poojaBookModelPrice.setmDate(dateWithFormat);

        //gson = new Gson();
        json = gson.toJson(poojaBookModelPrice);
        appPreferences.putString(AppConstants.PREF_POOJA_BOOK_PRICE,json);

        validate();
    }

    public boolean validate() {
        if (muhurDate.getText().toString().length() == 0) {
            muhurDate.setError("Please select muhurt date");
            return false;
        } else {
            muhurDate.setError(null);
            return true;
        }

    }
}
