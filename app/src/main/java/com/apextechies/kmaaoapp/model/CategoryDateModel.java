package com.apextechies.kmaaoapp.model;

/**
 * Created by Shankar on 2/8/2018.
 */

public class CategoryDateModel {

    public String getApplication_id() {
        return application_id;
    }

    public void setApplication_id(String application_id) {
        this.application_id = application_id;
    }

    public String getApplication_name() {
        return application_name;
    }

    public void setApplication_name(String application_name) {
        this.application_name = application_name;
    }

    public String getApplication_play_store_link() {
        return application_play_store_link;
    }

    public void setApplication_play_store_link(String application_play_store_link) {
        this.application_play_store_link = application_play_store_link;
    }

    public String getApplication_price() {
        return application_price;
    }

    public void setApplication_price(String application_price) {
        this.application_price = application_price;
    }

    public String getApplication_status() {
        return application_status;
    }

    public void setApplication_status(String application_status) {
        this.application_status = application_status;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getExpiry_time() {
        return expiry_time;
    }

    public void setExpiry_time(String expiry_time) {
        this.expiry_time = expiry_time;
    }

    String application_id,application_name,application_play_store_link,application_price,application_status,created_date,created_time,expiry_date,expiry_time;
    public CategoryDateModel(String application_id, String application_name, String application_play_store_link, String application_price,
                             String application_status, String created_date, String created_time, String expiry_date, String expiry_time){
        this.application_id = application_id;
        this.application_name = application_name;
        this.application_play_store_link = application_play_store_link;
        this.application_price = application_price;
        this.application_status = application_status;
        this.created_date = created_date;
        this.created_time = created_time;
        this.expiry_date = expiry_date;
        this.expiry_time = expiry_time;

    }
}
