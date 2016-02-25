package com.muhurtmaza.webservice;

/**
 * Created by Sandeep on 11/22/15.
 */
public class ApiConstants {

    //public static final String DOMAIN_URL= "http://52.26.134.135";
    //public static final String DOMAIN_URL= "http://ec2-52-4-20-173.compute-1.amazonaws.com";
      public static final String DOMAIN_URL= "http://192.168.0.127:8045"; //Local URL from Darshana
    //public static final String DOMAIN_URL= "http://192.168.0.127:8086"; //Local URL from Darshana 15 Dec 2015
   // public static final String DOMAIN_URL= "http://muhurtmaza.com";
    //public static final String DOMAIN_URL= "http://192.168.0.127:8000";

    public static final String BASE_URL = DOMAIN_URL+"/mobile/";

    public static final String POST = "POST";

    public static final String GET = "GET";


    // API IDS

    public static final int LOGIN_ID=1;
    public static final int GET_ALL_BOOKINGS_ID=2;
    public static final int SIGN_UP_ID = 3;
    public static final int GENERATE_OTP_ID = 4;
    public static final int CHECK_OTP_ID = 5;
    public static final int REGENERATE_OTP_ID = 6;
    public static final int EDIT_PROFILE_ID = 7;
    public static final int FORGOT_PASSWORD_ID = 8;
    public static final int GET_FEEDBACK_ID = 9;
    public static final int SEND_FEEDBACK_ID = 10;
    public static final int SEARCH_POOJA_ID = 11;
    public static final int CHANGE_PASSWORD_ID = 12;
    public static final int EMAIL_ALERT_ACTIVATION_ID = 13;
    public static final int SMS_ALERT_ACTIVATION_ID = 14;
    public static final int GET_UPCOMING_BOOKINGS_ID = 15;
    public static final int GET_CITY_NAMES_ID = 16;
    public static final int GET_ALL_POOJA_LIST_ID = 17;
    public static final int GET_POOJA_DETAILS_ID = 18;
    public static final int BOOK_POOJA_ID = 19;
    public static final int NEW_SIGN_UP_ID = 20;
    public static final int POOJA_BOOKINGS_DETAILS_ID = 21;
    public static final int REPORT_ISSUES_ID=22;
    //API URLS
    public static final String LOGIN_URL = BASE_URL + "customer_login/";

    public static final String GET_ALL_BOOKINGS_URL = BASE_URL + "view_all_bookings/?user_id=";

    public static final String SIGN_UP_URL = BASE_URL + "customer_signup/";

    public static final String SIGN_IN_GOOGLE_FACEBOOK = BASE_URL + "customer_register_facebook/";

    public static final String GENERATE_OTP = BASE_URL + "generate_OTP/";

    public static final String REGENERATE_OTP = BASE_URL + "generate_OTP/";

    public static final String CHECK_OTP = BASE_URL + "check_OTP/";

    public static final String EDIT_PROFILE_URL = BASE_URL + "edit_customer_profile/";

    public static final String FORGET_PASSWORD_URL = BASE_URL + "forgot_password/";

    public static final String GET_FEEDBACK = BASE_URL + "feedback/?cust_id=";

    public static final String SEND_FEEDBACK = BASE_URL + "save_feedback/";

    public static final String SEARCH_POOJA_URL = BASE_URL +"search_pooja/?search_text=";

    public static final String CHANGE_PASSWORD_URL = BASE_URL + "edit_customer_profile/";

    public static final String SMS_ALERT_ACTIVATION = BASE_URL + "sms_alert_activation/";

    public static final String EMAIL_ALERT_ACTIVATION = BASE_URL + "email_alert_activation/";

    public static final String GET_UPCOMING_BOOKINGS_URL = BASE_URL + "view_all_bookings/?user_id=";

    public static final String GET_CITY_NAMES = BASE_URL + "city_list/";

    public static final String POOJA_BOOKINGS_DETAILS = BASE_URL + "pooja_booking_details/";

    public static final String GET_ALL_POOJA_LIST_URL = BASE_URL + "get_pooja_details/";

    public static final String GET_POOJA_DETAILS_URL = BASE_URL + "selected_pooja_details/";

    public static final String BOOK_POOJA_URL = BASE_URL + "book_pooja/";

    public static final String REPORT_ISSUES = BASE_URL + "save_booking_issue/";

}
