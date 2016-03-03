package com.muhurtmaza.model;

/**
 * Created by imac on 29/02/16.
 */
public class BookingDetails {

    private String bookingId;
    private String poojaName;
    private String refferanceNo;
    private String user_email_id;
    private String user_id;
    private String samagree_chossen;
    private String dakshina;
    private String muhurt_date;

    private static BookingDetails ourInstance = new BookingDetails();

    public static BookingDetails getInstance() {
        return ourInstance;
    }

    private BookingDetails() {
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getPoojaName() {
        return poojaName;
    }

    public void setPoojaName(String poojaName) {
        this.poojaName = poojaName;
    }

    public String getRefferanceNo() {
        return refferanceNo;
    }

    public void setRefferanceNo(String refferanceNo) {
        this.refferanceNo = refferanceNo;
    }

    public String getUser_email_id() {
        return user_email_id;
    }

    public void setUser_email_id(String user_email_id) {
        this.user_email_id = user_email_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSamagree_chossen() {
        return samagree_chossen;
    }

    public void setSamagree_chossen(String samagree_chossen) {
        this.samagree_chossen = samagree_chossen;
    }

    public String getDakshina() {
        return dakshina;
    }

    public void setDakshina(String dakshina) {
        this.dakshina = dakshina;
    }

    public String getMuhurt_date() {
        return muhurt_date;
    }

    public void setMuhurt_date(String muhurt_date) {
        this.muhurt_date = muhurt_date;
    }

    public static BookingDetails getOurInstance() {
        return ourInstance;
    }

    public static void setOurInstance(BookingDetails ourInstance) {
        BookingDetails.ourInstance = ourInstance;
    }


}
