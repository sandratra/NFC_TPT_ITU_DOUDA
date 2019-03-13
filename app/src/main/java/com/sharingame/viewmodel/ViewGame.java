package com.sharingame.viewmodel;

import com.sharingame.entity.Game;
import com.sharingame.entity.Image;
import com.sharingame.entity.Platform;
import com.sharingame.entity.Tag;
import com.sharingame.entity.Technologies;
import com.sharingame.entity.User;

public class ViewGame {
    private User user;
    private Game[] games;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Game[] getGames() {
        return games;
    }

    public void setGames(Game[] games) {
        this.games = games;
    }
}
