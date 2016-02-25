package com.muhurtmaza.webservice.response;

import com.google.gson.Gson;
import com.muhurtmaza.webservice.BaseResponse;

import org.json.JSONObject;

/**
 * Created by Admin on 18-02-2016.
 */
public class ReportIssueResponse extends BaseResponse{

    private String mMessage;

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public static ReportIssueResponse fromJson(String pResult) {

      /*  JSONObject objJson= new JSONObject(pResult);
        NewUserResponse obj = new NewUserResponse();
        boolean success=objJson.getBoolean("success");

        if(!success){
            obj.setmMessage(objJson.getString("message"));
        }else{
            JSONObject user_info=objJson.getJSONObject("user_info");
            JSONObject basic_info=user_info.getJSONObject("basic");
            // obj.setmUser_id(basic_info.getString("user_id"));
            Gson gson = new Gson();
            obj= gson.fromJson(basic_info.toString(), NewUserResponse.class);
            obj.setmMessage(objJson.getString("message"));*/
        ReportIssueResponse objJson=new ReportIssueResponse();
        try {
            JSONObject obj= new JSONObject(pResult);
            objJson.setmMessage(obj.getString("message"));
            if(!objJson.getmMessage().isEmpty()){
                objJson.setmMessage(obj.getString("message"));
            }
        }catch (Exception e){

        }
        return objJson;

    }


}
