package com.sharingame.entity;

public class ShargModel {

    private String id;

    public ShargModel(){
    }

    public ShargModel(String id){
        this.setId(id);
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }
}
