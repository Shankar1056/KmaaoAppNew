package com.apextechies.kmaaoapp.model;

import java.util.ArrayList;

/**
 * Created by Shankar on 2/8/2018.
 */

public class CategoryModel {
    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<CategoryDateModel> getData() {
        return data;
    }

    private String status,msg;
    private ArrayList<CategoryDateModel> data;
}
