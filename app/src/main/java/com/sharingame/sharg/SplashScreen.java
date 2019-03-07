package com.sharingame.sharg;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_activity);

        PackageManager manager = this.getPackageManager();
        if (!manager.hasSystemFeature(PackageManager.FEATURE_NFC)) {
            com.sharingame.utility.Message.message(getApplicationContext(),"NFC indisponible!");
        }
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
