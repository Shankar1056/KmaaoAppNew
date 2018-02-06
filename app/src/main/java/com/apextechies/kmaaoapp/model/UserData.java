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

    private  String user_id,user_name,user_email,user_phone,device_unique_id,device_token,user_created_date,user_status;
}
