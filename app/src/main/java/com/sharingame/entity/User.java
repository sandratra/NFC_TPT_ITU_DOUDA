package com.sharingame.entity;

import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User extends ShargModel {
    private String name,lastname,username,email,description;
    private Date datesignup;
    private String role;
    private String region;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDatesignup() {
        return datesignup;
    }

    public void setDatesignup(Date datesignup) {
        this.datesignup = datesignup;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getFormatedDate(){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(datesignup);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return getId().equals(((User)obj).getId());
    }
}
