package com.muhurtmaza.webservice.response;

import com.google.gson.Gson;
import com.muhurtmaza.webservice.BaseResponse;

import org.json.JSONObject;

/**
 * Created by admin on 19-12-2015.
 */
public class BookPoojaFeedbackResponse extends BaseResponse{

    String bookingId;
    String poojaName;
    String poojaImage;

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

    public String getPoojaImage() {
        return poojaImage;
    }

    public void setPoojaImage(String poojaImage) {
        this.poojaImage = poojaImage;
    }

    @Override
    public String toString() {
        return "BookPoojaFeedback [bookingId=" + bookingId + ", poojaName="
                + poojaName + ", poojaImage=" + poojaImage + "]";
    }

    public static BookPoojaFeedbackResponse fromJson(String pResult)  {
        try {
            JSONObject objJson= new JSONObject(pResult);
            BookPoojaFeedbackResponse obj=new BookPoojaFeedbackResponse();
            JSONObject user_info=objJson.getJSONObject("nameList");

            Gson gson = new Gson();
            obj= gson.fromJson(objJson.toString(), BookPoojaFeedbackResponse.class);

            return obj;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
