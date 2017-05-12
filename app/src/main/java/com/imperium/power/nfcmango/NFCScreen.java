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

    PendingIntent nfcPendingIntent;
    NfcAdapter nfcAdpt;
    IntentFilter[] intentFiltersArray;
    Intent nfcIntent;

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

        nfcIntent = new Intent(this, getClass());
        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        nfcPendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
        IntentFilter tagIntentFilter =
                new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            tagIntentFilter.addDataType("text/plain");
            intentFiltersArray = new IntentFilter[]{tagIntentFilter};
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcAdpt.enableForegroundDispatch(this, nfcPendingIntent, intentFiltersArray, null);
        //Intent intent = new Intent(this, NFCScreen.class);
        startActivity(nfcIntent); //this is causing crashes
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdpt.disableForegroundDispatch(this);
    }

    /*@Override
    public void onNewIntent(Intent intent) {
        Log.d("Nfc", "New intent");
        getTag(intent);
    }*/

    /*private void getTag(Intent i) {
        if (i == null)
            return ;

        String type = i.getType();
        String action = i.getAction();
        List<ndefdata>dataList = new ArrayList<ndefdata>();

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Log.d("Nfc", "Action NDEF Found");
            Parcelable[] parcs = i.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            for (Parcelable p : parcs) {
                recNumberTxt.setText(String.valueOf(numRec));
                NdefRecord[] records = msg.getRecords();
                for (NdefRecord record: records) {
                    short tnf = record.getTnf();
                    // Here we handle the payload
                }
            }
        }
    }*/

    /** Called when the user taps the View Caught Nfcm button */
    public void viewCaught(View view) {
        Intent intent = new Intent(this, CaughtList.class);
        startActivity(intent);
    }
}
