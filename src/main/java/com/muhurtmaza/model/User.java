package com.muhurtmaza.model;

/**
 * Created by imac on 25/02/16.
 */
public class User {

    private String userid;
    private String userFirstname;
    private String userLastname;
    private String userEmailId;
    private String userContactNo;
    private String userProfilePicURL;
    private String userAddress;
    private String userCity;
    private String userSMSAlert;
    private String userEmailAlert;
    private String userLanguage;
    private String userSignUpSource;



    private String userBookedPoojaName;
    private String userBookedPoojaDate;
    private static User ourInstance = new User();
    public static User getInstance() {
        return ourInstance;
    }

    private User() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserFirstname() {
        return userFirstname;
    }

    public void setUserFirstname(String userFirstname) {
        this.userFirstname = userFirstname;
    }

    public String getUserLastname() {
        return userLastname;
    }

    public void setUserLastname(String userLastname) {
        this.userLastname = userLastname;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public String getUserContactNo() {
        return userContactNo;
    }

    public void setUserContactNo(String userContactNo) {
        this.userContactNo = userContactNo;
    }

    public String getUserProfilePicURL() {
        return userProfilePicURL;
    }

    public void setUserProfilePicURL(String userProfilePicURL) {
        this.userProfilePicURL = userProfilePicURL;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserLanguage() {
        return userLanguage;
    }

    public void setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
    }

    public static User getOurInstance() {
        return ourInstance;
    }

    public static void setOurInstance(User ourInstance) {
        User.ourInstance = ourInstance;
    }
    public String getUserSMSAlert() {
        return userSMSAlert;
    }

    public void setUserSMSAlert(String userSMSAlert) {
        this.userSMSAlert = userSMSAlert;
    }

    public String getUserEmailAlert() {
        return userEmailAlert;
    }

    public void setUserEmailAlert(String userEmailAlert) {
        this.userEmailAlert = userEmailAlert;
    }

    public String getUserSignUpSource() {
        return userSignUpSource;
    }

    public void setUserSignUpSource(String userSignUpSource) {
        this.userSignUpSource = userSignUpSource;
    }
    public String getUserBookedPoojaName() {
        return userBookedPoojaName;
    }

    public void setUserBookedPoojaName(String userBookedPoojaName) {
        this.userBookedPoojaName = userBookedPoojaName;
    }

    public String getUserBookedPoojaDate() {
        return userBookedPoojaDate;
    }

    public void setUserBookedPoojaDate(String userBookedPoojaDate) {
        this.userBookedPoojaDate = userBookedPoojaDate;
    }
    public void clear()
    {
        userid = null;
    }

}
