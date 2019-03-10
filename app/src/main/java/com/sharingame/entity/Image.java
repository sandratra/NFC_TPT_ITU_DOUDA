package com.sharingame.entity;

import java.sql.Clob;

public class Image extends ShargModel{
    private Clob image;
    private int gamesId;

    public Clob getImage() {
        return image;
    }

    public void setImage(Clob image) {
        this.image = image;
    }

    public int getGamesId() {
        return gamesId;
    }

    public void setGamesId(int gamesId) {
        this.gamesId = gamesId;
    }
}
