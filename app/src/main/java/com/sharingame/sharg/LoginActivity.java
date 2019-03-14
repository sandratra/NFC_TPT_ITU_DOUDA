package com.sharingame.sharg;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.sharingame.data.MStorage;
import com.sharingame.entity.User;
import com.sharingame.utility.DialogHelper;
import com.sharingame.utility.LocalDB;
import com.sharingame.utility.NameValuePair;
import com.sharingame.utility.ObjectUtils;
import com.sharingame.utility.ShargWS;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends Activity {

    private LocalDB dbHelper;

    EditText user_name;
    EditText password;
    AppCompatButton btn_login;
    private FrameLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        progressBar = findViewById(R.id.loadingPanel);
        dbHelper = new LocalDB(this);
        initComponents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (!nfcAdapter.isNdefPushEnabled()){
            new DialogHelper(this).showDialog(R.layout.popup_layer, DialogHelper.DIALOG_INFO, "Veuillez activer NFC, l'application peut toujours fonctionner mais avec des fonctionalités réduites.", null);
        }
    }

    private void initComponents(){
        user_name = findViewById(R.id.input_login);
        password = findViewById(R.id.input_pwd);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(on_login);
    }

    public View.OnClickListener on_login = new View.OnClickListener(){
        public void onClick(View v){
            progressBar.setVisibility(View.VISIBLE);
            String user_name_text = user_name.getText().toString();
            String password_text = password.getText().toString();
            if(user_name_text.isEmpty() || password_text.isEmpty())
            {
                new DialogHelper(LoginActivity.this).showDialog(R.layout.popup_layer, DialogHelper.DIALOG_WARNING, "Veuillez vérifier que tous les champs ne sont pas vide.", null);
                progressBar.setVisibility(View.GONE);
                return;
            }
            else
            {
                String target_api = "user/auth/signin";
                ArrayList<NameValuePair> prm = new ArrayList<>();
                prm.add(new NameValuePair("name", user_name_text));
                prm.add(new NameValuePair("password", password_text));
                ShargWS ws_test = new ShargWS("POST", target_api, prm, null);
                try {
                    String res = ws_test.execute().get();
                    if (res == null){
                        new DialogHelper(LoginActivity.this).showDialog(R.layout.popup_layer, DialogHelper.DIALOG_ERROR, "Erreur d'identification ou de mot de passe. Vérifiez que tous les champs soient valides.", null);
                        progressBar.setVisibility(View.GONE);
                        return ;
                    } else {
                        User user = ObjectUtils.FromJsonSimple(User.class, res);
                        MStorage.MySelf.setProfile(user);
                        Intent myIntent = new Intent(LoginActivity.this, ShargActivity.class);
                        startActivity(myIntent);
                        progressBar.setVisibility(View.GONE);
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
            }
        }
    };
}