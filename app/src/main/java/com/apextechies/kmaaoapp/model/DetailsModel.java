package com.apextechies.kmaaoapp.model;

import java.util.ArrayList;

/**
 * Created by Shankar on 1/26/2018.
 */

public class DetailsModel {
    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<DetailsModelData> getData() {
        return data;
    }

    private String status,msg;
    ArrayList<DetailsModelData> data;
}
