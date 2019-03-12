package com.sharingame.sharg.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharingame.data.MStorage;
import com.sharingame.entity.User;
import com.sharingame.sharg.R;

public class UserFragmentProfile extends Fragment {

    TextView pseudo, email, description, fullName;
    AppCompatButton addFriendButton;

    public static User selectedUserProfil = null;

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
        addFriendButton = v.findViewById(R.id.profile_user_btn_add_friend);
        loadData(selectedUserProfil);
        return v;
    }

    public void loadData(User user){
        pseudo.setText(user.getUsername());
        email.setText(user.getEmail());
        description.setText(user.getDescription());
        fullName.setText(user.getName() + " " + user.getLastname());
        addFriendButton.setEnabled(!isMyFriend(user));
    }

    public boolean isMyFriend(User user){
        if(user.equals(MStorage.MySelf.getProfile()))
            return true;
        //TODO: Compare friend status
        Log.w("-------", "Must check if this user is my friend or not");
        return false;
    }

    public View.OnClickListener on_add_friend = new View.OnClickListener(){
        public void onClick(View v){
            //TODO: Send add friend request
            /*String target_api = "user";
            String[] data = new String[]{"2"};
            ShargWS ws_test = new ShargWS(target_api, data);
            try {
                String res = ws_test.execute().get();
                ShargModel obj = ws_test.FromJsonDataMapping(ShargModel.class, res);
                Log.i("ID_MODEL", obj.getId());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }/**/
        }
    };
}
