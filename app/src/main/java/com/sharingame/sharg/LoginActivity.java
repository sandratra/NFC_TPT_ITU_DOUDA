package com.sharingame.sharg;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        dbHelper = new LocalDB(this);
        initComponents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (!nfcAdapter.isNdefPushEnabled()){
            new DialogHelper(this).showDialog(R.layout.popup_layer, DialogHelper.DIALOG_INFO, "Veuillez activer l'option Beam de votre smartphone pour pouvoir utiliser les fonctionnalités NFC", null);
        }

        //TODO: remove these lines before production
        User test_user = new User();
        test_user.setId("47");
        test_user.setName("RAKOTO");
        test_user.setLastname("Rabe");
        test_user.setUsername("_rakotobe_");
        test_user.setEmail("koto@test.com");
        test_user.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        MStorage.MySelf.setProfile(test_user);
    }

    private void initComponents(){
        user_name = findViewById(R.id.input_login);
        password = findViewById(R.id.input_pwd);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(on_login);
    }

    public View.OnClickListener on_login = new View.OnClickListener(){
        public void onClick(View v){
            String user_name_text = user_name.getText().toString();
            String password_text = password.getText().toString();
            if(user_name_text.isEmpty() || password_text.isEmpty())
            {
                new DialogHelper(LoginActivity.this).showDialog(R.layout.popup_layer, DialogHelper.DIALOG_WARNING, "Veuillez vérifier que tous les champs ne sont pas vide.", null);
                return;
            }
            else
            {
                String target_api = "user/auth/signin";
                String[] data = new String[]{};
                ArrayList<NameValuePair> params = new ArrayList<>();
                params.add(new NameValuePair("name", user_name_text));
                params.add(new NameValuePair("password", password_text));
                ShargWS ws_test = new ShargWS("POST", target_api, params, data);
                try {
                    String res = ws_test.execute().get();
                    if (res == null){
                        new DialogHelper(LoginActivity.this).showDialog(R.layout.popup_layer, DialogHelper.DIALOG_ERROR, "Erreur d'identification ou de mot de passe.", null);
                        return;
                    } else {
                        User user = ObjectUtils.FromJsonSimple(User.class, res);
                        MStorage.MySelf.setProfile(user);
                        Intent myIntent = new Intent(LoginActivity.this, ShargActivity.class);
                        startActivity(myIntent);
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}