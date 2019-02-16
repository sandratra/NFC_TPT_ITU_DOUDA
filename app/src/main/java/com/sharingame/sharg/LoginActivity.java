package com.sharingame.sharg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

    EditText user_name;
    EditText password;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_init);

        initComponents();
    }

    private void initComponents(){
        user_name = (EditText)findViewById(R.id.input_login);
        password = (EditText)findViewById(R.id.input_pwd);
        btn_login = (Button)findViewById(R.id.btn_login);

        btn_login.setOnClickListener(on_login);
    }

    public View.OnClickListener on_login = new View.OnClickListener(){
        public void onClick(View v){
            String user_name_text = user_name.getText().toString();
            String password_text = password.getText().toString();
            Intent myIntent = new Intent(LoginActivity.this, ShargActivity.class);
            startActivity(myIntent);
        }
    };
}
