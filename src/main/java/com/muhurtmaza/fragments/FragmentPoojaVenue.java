package com.muhurtmaza.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.muhurtmaza.R;
import com.muhurtmaza.model.PoojaBookModel;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.utility.CommonUtility;

import java.util.ArrayList;

/**
 * Created by imac on 15/12/15.
 */
public class FragmentPoojaVenue extends ParentFragment {
    EditText firstname,lastname,email,address,phone; //city;
    Spinner language,state,spnCity;
    /*private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";*/

    private Context mContext;
    AppPreferences appPreferences;

    Gson gson;
    String json="";
    PoojaBookModel poojaBookModelVenu;

    Spinner spnCityList;
    private ArrayList<String> citylist;
    private ArrayAdapter<String> dataAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pooja_details_venue, container, false);
        setupUI(v);
        return v;
    }

    public void setupUI(View v){

        firstname = (EditText)v.findViewById(R.id.firstname);
        lastname = (EditText)v.findViewById(R.id.lastname);
        language = (Spinner)v.findViewById(R.id.language);
        email = (EditText)v.findViewById(R.id.email);
        //city = (EditText)v.findViewById(R.id.city);
        state = (Spinner)v.findViewById(R.id.state);
        address = (EditText)v.findViewById(R.id.address);
        phone = (EditText)v.findViewById(R.id.phone);
        spnCity = (Spinner)v.findViewById(R.id.spn_city);

        mContext = getActivity();
        appPreferences = AppPreferences.getInstance(mContext);

        spnCityList = (Spinner)v.findViewById(R.id.spn_city);
        citylist = new ArrayList<String>();

        String selectedCity = appPreferences.getString(AppConstants.USER_SELECTED_CITY,"");
        citylist.add(selectedCity);
        spnCityList.setSelection(0);

        String strLen = appPreferences.getString(AppConstants.TOTAL_BOOK_POOJA_CITY,"");
        int len = Integer.parseInt(strLen);
        Log.d("City list len",":"+len);
        int i;
        for(i=0; i<=len;i++){
            String getCityName = appPreferences.getString(AppConstants.CITY_ID_+i,"");
            getCityName = getCityName.trim();
            Log.d("Get city",getCityName+"="+selectedCity+" Result ="+(getCityName!=selectedCity));
            if(!getCityName.equals(selectedCity) && !getCityName.equals(""))
                citylist.add(getCityName);
        }

        dataAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_list_item_1, citylist);
        spnCityList.setAdapter(dataAdapter);

        poojaBookModelVenu = new PoojaBookModel();
        gson = new Gson();

        json = gson.toJson(poojaBookModelVenu);
        appPreferences.putString(AppConstants.PREF_POOJA_BOOK_VENU,json);


        firstname.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                validateFirstName();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }});

        lastname.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                validateLastName();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }});

        email.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                validateEmail();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }});
        phone.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                validatePhone();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }});
        address.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateAddress();
            }});

        /*city.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                validateCity();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });*/

    }



    public static FragmentPoojaVenue newInstance(String text) {

        FragmentPoojaVenue f = new FragmentPoojaVenue();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
    public boolean validate(){
        if(validateFirstName()&&validateLastName()&&validateEmail()&&validatePhone()&&validateAddress()){

            poojaBookModelVenu.setmFirstName(firstname.getText().toString());
            poojaBookModelVenu.setmLastName(lastname.getText().toString());
            poojaBookModelVenu.setmLanguage(language.getSelectedItem().toString());
            poojaBookModelVenu.setmEmail(email.getText().toString());
            poojaBookModelVenu.setmContactNo(phone.getText().toString());
            poojaBookModelVenu.setmVenu(address.getText().toString());
            poojaBookModelVenu.setmCity(spnCity.getSelectedItem().toString());
            poojaBookModelVenu.setmState(state.getSelectedItem().toString());

            gson = new Gson();
            json = gson.toJson(poojaBookModelVenu);
            appPreferences.putString(AppConstants.PREF_POOJA_BOOK_VENU,json);

            return true;
        }
        else{
            return false;
        }
    }

    public boolean validateFirstName( )
    {
        if(firstname.getText().toString().length()==0){
            firstname.setError("First name is required");
            firstname.requestFocus();
            return false;
        }
        else if(firstname.getText().toString().trim().matches("[a-zA-Z]*")){
            firstname.setError(null);
            return true;
        }
        else{
            firstname.setError("Enter valid name");
            firstname.requestFocus();
            return false;
        }
    }
    // end method validateFirstName

    // validate last name
    public boolean validateLastName( )
    {
        if(lastname.getText().toString().length()==0){
            lastname.setError("Last name is required");
            lastname.requestFocus();
            return false;
        }
        else if(lastname.getText().toString().trim().matches("[a-zA-Z]*")){
            lastname.setError(null);
            return true;
        }
        else{
            lastname.setError("Enter valid name");
            lastname.requestFocus();
            return false;
        }
    } // end method validateLastName

    public boolean validateEmail(){
        if (CommonUtility.getInstance(getActivity()).isValidEmail(email.getText().toString())){ //!
            email.setError(null);
            return true;
        }
        else {
            email.setError("Enter valid email id");
            email.requestFocus();
            return false;
        }
    }

    public boolean validatePhone(){
        if (phone.getText().toString().trim().matches("\\d{10}")){
            phone.setError(null);
            return true;
        }
        else {
            phone.setError("Enter valid mobile no");
            phone.requestFocus();
            return false;
        }
    }

    public boolean validateAddress(){
        if (address.getText().toString().trim().matches("[A-Za-z0-9'\\.\\-\\s\\,\\/]{3,150}")){
            address.setError(null);
            return true;
        }
        else {
            address.setError("Enter valid address");
            address.requestFocus();
            return false;
        }
    }

   /* public boolean validateCity(){
        if(city.getText().toString().length()==0){
            city.setError("City name is required");
            city.requestFocus();
            return false;
        }
        else if(city.getText().toString().trim().matches("[a-zA-Z\\s]*")){
            city.setError(null);
            return true;
        }
        else {
            city.setError("Enter valid city");
            city.requestFocus();
            return false;
        }
    }*/


}
