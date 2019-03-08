package com.sharingame.sharg;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.sharingame.utility.CustomPagerAdapter;
import com.sharingame.utility.DialogHelper;
import com.sharingame.utility.Message;
import com.sharingame.utility.NFCHelper;

public class ShargActivity extends Activity implements NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {

    private ViewPager mViewPager;
    NfcAdapter nfcAdapter;
    private static final int MESSAGE_SENT = 1;

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

        //nfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            new DialogHelper(this).showDialog(R.layout.popup_layer,"NFC " + DialogHelper.DIALOG_WARNING, "NFC n'est pas disponible sur cet appareil.", null);
        }
        else {
            nfcAdapter.setNdefPushMessageCallback(this, this);
            nfcAdapter.setOnNdefPushCompleteCallback(this, this);
        }
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

    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        //mInfoText.setText(new String(msg.getRecords()[0].getPayload()));
        Message.message(getApplicationContext(),new String(msg.getRecords()[0].getPayload()));
    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
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
        mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
        //new DialogHelper(this).showDialog(R.layout.popup_layer, DialogHelper.DIALOG_INFO, "Text NFC reçu: " + event.toString(), null);
        //Message.message(getApplicationContext(),"Impossible de sauvegarder les champs!");
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MESSAGE_SENT:
                    Toast.makeText(getApplicationContext(), "Message sent!", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };


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
