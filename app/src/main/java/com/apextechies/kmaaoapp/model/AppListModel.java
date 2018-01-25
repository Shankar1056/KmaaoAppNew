package com.apextechies.kmaaoapp.model;

/**
 * Created by Shankar on 1/25/2018.
 */

public class AppListModel{
    public String getId() {
    return id;
}

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        String id;
        String name;
        String image;

        public AppListModel(String id, String name, String image)
        {
            this.id = id;
            this.name = name;
            this.image = image;
        }
}