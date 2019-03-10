package com.sharingame.sharg.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharingame.data.MStorage;
import com.sharingame.sharg.R;

public class UserFragmentProfile extends Fragment {

    TextView title;

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
        title = v.findViewById(R.id.lbl_user_name);
        title.setText(MStorage.MySelf.getProfile().getUsername());
        return v;
    }
}
