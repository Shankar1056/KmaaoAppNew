package com.apextechies.kmaaoapp.model;

/**
 * Created by Shankar on 2/7/2018.
 */

public class UserData {
    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getDevice_unique_id() {
        return device_unique_id;
    }

    public String getDevice_token() {
        return device_token;
    }

    public String getUser_created_date() {
        return user_created_date;
    }

    public String getUser_status() {
        return user_status;
    }

    private  String user_id;
    private String user_name;
    private String user_email;
    private String user_phone;
    private String device_unique_id;
    private String device_token;
    private String user_created_date;
    private String user_status;

    public String getTotal_amount() {
        return total_amount;
    }

    private String total_amount;
}
