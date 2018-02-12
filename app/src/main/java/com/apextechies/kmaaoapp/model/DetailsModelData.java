package com.apextechies.kmaaoapp.model;

/**
 * Created by Shankar on 2/10/2018.
 */

public class DetailsModelData {

    public String getApplication_rules_id() {
        return application_rules_id;
    }

    public void setApplication_rules_id(String application_rules_id) {
        this.application_rules_id = application_rules_id;
    }

    public String getApplication_id() {
        return application_id;
    }

    public void setApplication_id(String application_id) {
        this.application_id = application_id;
    }

    public String getApplication_rules() {
        return application_rules;
    }

    public void setApplication_rules(String application_rules) {
        this.application_rules = application_rules;
    }

    public String getStpes() {
        return stpes;
    }

    public void setStpes(String stpes) {
        this.stpes = stpes;
    }

    public String getRules_image() {
        return rules_image;
    }

    public void setRules_image(String rules_image) {
        this.rules_image = rules_image;
    }

    public String getApplication_status() {
        return application_status;
    }

    public void setApplication_status(String application_status) {
        this.application_status = application_status;
    }

    private String application_rules_id,application_id,application_rules,stpes,rules_image,application_status;
    public DetailsModelData( String application_rules_id, String application_id, String application_rules, String stpes,
                             String rules_image, String application_status){
        this.application_rules_id = application_rules_id;
        this.application_id = application_id;
        this.application_rules = application_rules;
        this.stpes = stpes;
        this.rules_image = rules_image;
        this.application_status = application_status;

    }
}
