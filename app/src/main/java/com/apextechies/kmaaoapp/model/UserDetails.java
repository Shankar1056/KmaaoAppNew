package com.apextechies.kmaaoapp.model;

import java.util.ArrayList;

/**
 * Created by Shankar on 2/7/2018.
 */

public class UserDetails {
    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<UserData> getData() {
        return data;
    }

    String status,msg;
    ArrayList<UserData> data;
}
