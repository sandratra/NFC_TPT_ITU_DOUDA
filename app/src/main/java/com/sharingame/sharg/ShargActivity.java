package com.sharingame.sharg;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.sharingame.data.MStorage;
import com.sharingame.sharg.fragment.UserFragmentGames;
import com.sharingame.sharg.fragment.UserFragmentProfile;
import com.sharingame.utility.CustomPagerAdapter;
import com.sharingame.utility.DialogHelper;
import com.sharingame.utility.Message;
import com.sharingame.utility.NFCHelper;
import com.sharingame.utility.ObjectUtils;
import com.sharingame.utility.ShargWS;
import com.sharingame.viewmodel.ViewGame;

import java.util.concurrent.ExecutionException;

public class ShargActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {

    private static ShargActivity _instance = null;

    ViewPager mViewPager;
    NfcAdapter nfcAdapter;
    TabLayout tabLayout;

    private static final int MESSAGE_SENT = 1;

    public static ShargActivity GetInstance(){
        return _instance;
    }

    //region Overrides
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _instance = this;
        setContentView(R.layout.main_activity);
        mViewPager = findViewById(R.id.container);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        mViewPager.setAdapter(new CustomPagerAdapter(this));
        mViewPager.setOnTouchListener(mOnViewPagerTouchListener);

        if (nfcAdapter == null) {
            new DialogHelper(this).showDialog(R.layout.popup_layer, "NFC " + DialogHelper.DIALOG_WARNING, "NFC n'est pas disponible sur cet appareil.", null);
        } else {
            nfcAdapter.setNdefPushMessageCallback(this, this);
            nfcAdapter.setOnNdefPushCompleteCallback(this, this);
        }

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Intent intent = getIntent();
        Log.i("INFO", "ONRESUME: " + intent.getAction());
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            final NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            final DialogHelper dh =new DialogHelper(this);
            dh.showDialog(R.layout.popup_layer, "NFC " + DialogHelper.DIALOG_INFO, "Une requête a été reçue via la NFC("+new String(message.getRecords()[0].getPayload())+"). Touchez OK pour traiter les données.", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manageNfcData(message);
                    dh.dismiss();
                }
            });

        } else{
            Log.w("NFC_WAITING","...");
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        Log.i("INFO", "createNdefMessage");
        NdefMessage msg = NFCHelper.createMyNdefMessage(ObjectUtils.ToJsonSimple(MStorage.MySelf.getProfile().getId()), "application/org.sharg.nfc.tpt");
        return msg;
    }

    @Override
    public void onNdefPushComplete(NfcEvent event)
    {
        //Notifier l’utilisateur
        Log.i("INFO", "PUSH COMPLETE");
        mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
        //new DialogHelper(this).showDialog(R.layout.popup_layer, DialogHelper.DIALOG_INFO, "Text NFC reçu: " + event.toString(), null);
        //Message.message(getApplicationContext(),"Impossible de sauvegarder les champs!");
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                return false;
        }
        return super.onKeyUp(keyCode, event);
    }
    //endregion

    //region Listeners
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MESSAGE_SENT:
                    //Toast.makeText(getApplicationContext(), "Message envoyé!", Toast.LENGTH_LONG).show();
                    new DialogHelper(ShargActivity.this).showDialog(R.layout.popup_layer, DialogHelper.DIALOG_INFO, "Votre requête de demande d'ami a été envoyée!", null);
                    break;
            }
        }
    };

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
                    tabLayout = findViewById(R.id.fragment_user_tab_layout);
                    tabLayout.clearOnTabSelectedListeners();
                    tabLayout.addOnTabSelectedListener(onTabLayoutListener);


                    String target_api = "profil";
                    String[] data = new String[]{MStorage.MySelf.getProfile().getId()};
                    ShargWS ws = new ShargWS("GET", target_api, null, data);
                    try {
                        String res = ws.execute().get();
                        UserFragmentProfile.selectedUserProfil = ObjectUtils.FromJsonSimple(ViewGame.class, res);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    initSelectedTab(0);
                    return true;
            }
            return false;
        }
    };

    private TabLayout.OnTabSelectedListener onTabLayoutListener = new TabLayout.OnTabSelectedListener(){
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            initSelectedTab(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };
    //endregion

    void manageNfcData(NdefMessage message){
        mViewPager = findViewById(R.id.container);
        mViewPager.setCurrentItem(2,false);
        String uid = new String(message.getRecords()[0].getPayload()).replace("\"","");

        String target_api = "profil";
        String[] data = new String[]{uid};
        ShargWS ws = new ShargWS("GET", target_api, null, data);
        try {
            String res = ws.execute().get();
            Log.i("-----NFC_RES-----------", res);
            UserFragmentProfile.selectedUserProfil = ObjectUtils.FromJsonSimple(ViewGame.class, res);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tabLayout = findViewById(R.id.fragment_user_tab_layout);
        tabLayout.clearOnTabSelectedListeners();
        tabLayout.addOnTabSelectedListener(onTabLayoutListener);
        initSelectedTab(0);
    }

    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        //mInfoText.setText(new String(msg.getRecords()[0].getPayload()));
        Message.message(getApplicationContext(),new String(msg.getRecords()[0].getPayload()));
    }

    void initSelectedTab(int index){
        Fragment fragment = null;
        switch (index) {
            case 0:
                fragment = new UserFragmentProfile();
                break;
            case 1:
                fragment = new UserFragmentGames();
                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_user_layout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
}
