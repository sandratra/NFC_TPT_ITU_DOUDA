package com.sharingame.utility;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

public abstract class NFCHelper {

    public static NdefMessage createMyNdefMessage(String text, String mimeType)
    {
        NdefMessage msgMIME = new NdefMessage(NdefRecord.createMime(mimeType, text.getBytes()));
        return msgMIME;
    }
}
