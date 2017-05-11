package com.imperium.power.nfcmango;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class NFCScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcscreen);
        nfcAdpt = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdpt == null) {
            Toast.makeText(this, "NFC not supported", Toast.LENGTH_LONG).show();
            finish();
        }
        if(!nfcAdpt.isEnabled()) {
            Toast.makeText(this, "Enable NFC before using the app", Toast.LENGTH_LONG).show();
        }

        Intent nfcIntent = new Intent(this, getClass());
        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent nfcPendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
        IntentFilter tagIntentFilter =
                new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            tagIntentFilter.addDataType("text/plain");
            IntentFilter[] intentFiltersArray = new IntentFilter[]{tagIntentFilter};
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcAdpt.enableForegroundDispatch(this, nfcPendingIntent, intentFiltersArray, null);
        handleIntent(getIntent());
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdpt.disableForegroundDispatch(this);
    }

    /** Called when the user taps the View Caught Cfcm button */
    public void viewCaught(View view) {
        Intent intent = new Intent(this, CaughtList.class);
        startActivity(intent);
    }
}
