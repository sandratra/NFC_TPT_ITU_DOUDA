package com.sharingame.sharg.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharingame.entity.Game;
import com.sharingame.sharg.R;
import com.sharingame.ui.UIGame;
import com.sharingame.utility.ObjectUtils;

public class UserFragmentGameDetails extends Fragment {

    private View layout;
    private Game game;

    private TextView title;
    private TextView description;
    private TextView tags;

    public UserFragmentGameDetails(){
        this.game = UIGame.SELECTED_GAME;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_user_game_details, container, false);
        title = layout.findViewById(R.id.profil_user_game_detail_title);
        description = layout.findViewById(R.id.profil_user_game_detail_description);
        tags = layout.findViewById(R.id.profil_user_game_detail_tags);
        loadDetails();
        return layout;
    }

    public void loadDetails(){
        title.setText(game.getTitle());
        description.setText(game.getDescription());
        String[] t = new String[game.getTags().length];
        for(int i=0;i<t.length;i++){
            t[i] = game.getTags()[i].getLabel();
        }
        tags.setText(ObjectUtils.joinString(t," | "));
    }
}
