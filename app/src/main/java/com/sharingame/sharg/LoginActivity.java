package com.sharingame.sharg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sharingame.utility.LocalDB;
import com.sharingame.utility.Message;
import com.sharingame.utility.ShargWS;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends Activity {

    private LocalDB dbHelper;

    EditText user_name;
    EditText password;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        dbHelper = new LocalDB(this);
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
            if(user_name_text.isEmpty() || password_text.isEmpty())
            {
                Message.message(getApplicationContext(),"Vérifiez les champs vides!");
            }
            else
            {
                long id = dbHelper.insertData(user_name_text,user_name_text);
                if(id<=0)
                {
                    Message.message(getApplicationContext(),"Impossible de sauvegarder les champs!");
                } else
                {
                    Message.message(getApplicationContext(),"La sauvegarde a été OK!");
                    user_name.setText("");
                    password.setText("");
                    //TODO: Login WebService
                }
            }

            //TODO: remove ws_test

            String URI = "https://jsonplaceholder.typicode.com/todos/1";
            ShargWS ws_test = new ShargWS(URI);
            try {
                String res = ws_test.execute().get();
                Log.i("RES_WS",res);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Intent myIntent = new Intent(LoginActivity.this, ShargActivity.class);
            //startActivity(myIntent);
        }
    };
}