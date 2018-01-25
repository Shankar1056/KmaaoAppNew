package com.apextechies.kmaaoapp.model;

/**
 * Created by Shankar on 1/26/2018.
 */

public class DetailsModel {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public DetailsModel(String id)
    {
        this.id = id;
    }
}
