package com.sharingame.sharg.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.sharingame.entity.Game;
import com.sharingame.sharg.R;
import com.sharingame.ui.UIGame;

import java.util.Arrays;
import java.util.List;

public class UserFragmentGames extends Fragment {

    LinearLayout scrollView;

    public UserFragmentGames(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_games, container, false);
        scrollView = v.findViewById(R.id.scroll_game_list);
        refreshGameList(Arrays.asList(UserFragmentProfile.selectedUserProfil.getGames()));
        return v;
    }

    public void refreshGameList(List<Game> games){
        scrollView.removeAllViews();
        for(int i=0;i<games.size();i++){
            View v = getLayoutInflater().inflate(R.layout.sample_uigame, null);
            UIGame uig = v.findViewById(R.id.ui_game_element);
            uig.setGame(games.get(i));
            scrollView.addView(v, i, new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT));
        }
    }
}
