package com.sharingame.sharg;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.sharingame.utility.CustomPagerAdapter;
import com.sharingame.utility.DialogHelper;
import com.sharingame.utility.Message;
import com.sharingame.utility.NFCHelper;

public class ShargActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(new CustomPagerAdapter(this));

        mViewPager.setOnTouchListener(mOnViewPagerTouchListener);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
        nfcAdapter.setNdefPushMessageCallback(this, this);
        nfcAdapter.setOnNdefPushCompleteCallback(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Log.i("INFO", "ONRESUME: " + intent.getAction());
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            //new DialogHelper(this).showDialog(R.layout.popup_layer,"NFC " + DialogHelper.DIALOG_INFO, new String(message.getRecords()[0].getPayload()), null);
            Message.message(getApplicationContext(),"Onresume OK action intent!");
        } else{
            Log.i("NFC_WAITING","...");
        }
    }

    @Override
    //Cette méthode est appelée quand le périphérique NFC P2P est détecté
    public NdefMessage createNdefMessage(NfcEvent event) {
        Log.i("INFO", "createNdefMessage");
        NdefMessage msg = NFCHelper.createMyNdefMessage("TEST NFC!", "application/org.sharg.nfc.tpt");
        return msg;
    }

    @Override
    //Cette méthode est appelée lorsque le message a été réceptionné
    public void onNdefPushComplete(NfcEvent event)
    {
        //Notifier l’utilisateur
        Log.i("INFO", "PUSH COMPLETE");
        //new DialogHelper(this).showDialog(R.layout.popup_layer, DialogHelper.DIALOG_INFO, "Text NFC reçu: " + event.toString(), null);
        Message.message(getApplicationContext(),"Impossible de sauvegarder les champs!");
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                return false;
        }
        return super.onKeyUp(keyCode, event);
    }

    private View.OnTouchListener mOnViewPagerTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem());
            return true;
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0,false);
                    return true;
                case R.id.navigation_messaging:
                    //mViewPager.setCurrentItem(1,true);
                    return true;
                case R.id.navigation_news:
                    mViewPager.setCurrentItem(1,false);
                    return true;
                case R.id.navigation_notification:
                    //mViewPager.setCurrentItem(3,true);
                    return true;
                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(2,false);
                    return true;
            }
            return false;
        }
    };
}
