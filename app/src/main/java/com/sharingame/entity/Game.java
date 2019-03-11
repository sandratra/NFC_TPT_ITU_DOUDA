package com.sharingame.entity;

import java.lang.reflect.GenericArrayType;
import java.util.Date;

public class Game extends ShargModel {
    private String title;
    private String description;
    private Date releaseDate;
    private boolean validAdmin;
    private String link;
    private int usersId;

    public Game(String title, String description){
        this.setTitle(title);
        this.setDescription(description);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isValidAdmin() {
        return validAdmin;
    }

    public void setValidAdmin(boolean validAdmin) {
        this.validAdmin = validAdmin;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getUsersId() {
        return usersId;
    }

    public void setUsersId(int usersId) {
        this.usersId = usersId;
    }
}
