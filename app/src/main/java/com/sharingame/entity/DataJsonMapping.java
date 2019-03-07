package com.sharingame.entity;

public class DataJsonMapping<T> {
    private T data;

    public T getData(){
        return data;
    }

    public void setData(T data){
        this.data = data;
    }
}
