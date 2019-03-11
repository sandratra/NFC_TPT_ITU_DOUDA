package com.sharingame.viewmodel;

import com.sharingame.entity.User;

public class UserProfile {

    private User profile;
    private ViewGame[] games;

    public UserProfile(){

    }

    public void setProfile(User user){
        this.profile = user;
    }

    public User getProfile(){
        return profile;
    }

    public ViewGame[] getGames() {
        return games;
    }

    public void setGames(ViewGame[] games) {
        this.games = games;
    }
}
