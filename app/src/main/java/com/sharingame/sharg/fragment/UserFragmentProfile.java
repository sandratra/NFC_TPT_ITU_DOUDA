package com.sharingame.sharg.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharingame.data.MStorage;
import com.sharingame.entity.User;
import com.sharingame.sharg.R;
import com.sharingame.utility.ShargWS;
import com.sharingame.viewmodel.ViewGame;

public class UserFragmentProfile extends Fragment {

    TextView pseudo, email, description, fullName, dateMember;
    AppCompatButton addFriendButton;
    AppCompatButton confirmFriendButton;

    public static ViewGame selectedUserProfil = null;

    public UserFragmentProfile(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);
        pseudo = v.findViewById(R.id.profile_user_pseudo);
        email = v.findViewById(R.id.profile_user_email);
        description = v.findViewById(R.id.profile_user_description);
        fullName = v.findViewById(R.id.profile_user_name);
        dateMember = v.findViewById(R.id.profile_user_signup);
        addFriendButton = v.findViewById(R.id.profile_user_btn_add_friend);
        confirmFriendButton = v.findViewById(R.id.profile_user_btn_confirm_friend);
        addFriendButton.setOnClickListener(on_add_friend);
        confirmFriendButton.setOnClickListener(on_confirm_friend);
        loadData(selectedUserProfil);
        return v;
    }

    public void loadData(ViewGame user){
        pseudo.setText(user.getUser().getUsername());
        email.setText(user.getUser().getEmail());
        description.setText(user.getUser().getDescription());
        dateMember.setText("Membre depuis " + user.getUser().getFormatedDate());
        fullName.setText(user.getUser().getName() + " " + user.getUser().getLastname());
        addFriendButton.setVisibility(isMyFriend(user)? View.GONE : View.VISIBLE);
        confirmFriendButton.setVisibility(user.getIsFriend() == User.IS_ASKED ? View.VISIBLE : View.GONE);
    }

    public boolean isMyFriend(ViewGame user){
        if(user.getUser().equals(MStorage.MySelf.getProfile()))
            return true;
        //TODO: Compare friend status
        return (user.getIsFriend() == User.IS_FRIEND);
    }

    public View.OnClickListener on_add_friend = new View.OnClickListener(){
        public void onClick(View v){
            String target_api = "friend/add";
            String[] data = new String[]{MStorage.MySelf.getProfile().getId(), selectedUserProfil.getUser().getId()};
            ShargWS ws_test = new ShargWS("GET", target_api, null, null);
            ws_test.execute();
            addFriendButton.setVisibility(View.GONE);
            confirmFriendButton.setVisibility(View.GONE);
        }
    };

    public View.OnClickListener on_confirm_friend = new View.OnClickListener(){
        public void onClick(View v){
            String target_api = "friend/confirm";
            String[] data = new String[]{MStorage.MySelf.getProfile().getId(), selectedUserProfil.getUser().getId()};
            ShargWS ws_test = new ShargWS("GET", target_api, null, null);
            ws_test.execute();
            addFriendButton.setVisibility(View.GONE);
            confirmFriendButton.setVisibility(View.GONE);
        }
    };
}
