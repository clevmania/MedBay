package com.clevmania.medbay.model;

/**
 * Created by grandilo-lawrence on 4/17/18.
 */

public class ProfileModel {
    String fullname;
    String email;
    String photo;
    String mobile;

    public ProfileModel() {
    }

    public ProfileModel(String fullname, String email, String photo, String mobile) {
        this.fullname = fullname;
        this.email = email;
        this.photo = photo;
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
