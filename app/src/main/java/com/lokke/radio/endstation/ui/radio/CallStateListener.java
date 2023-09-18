package com.lokke.radio.endstation.ui.radio;

import android.os.Build;
import android.telephony.TelephonyCallback;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.S)
abstract class CallStateListener extends TelephonyCallback implements TelephonyCallback.CallStateListener {
    @Override
    abstract public void onCallStateChanged(int state);
}