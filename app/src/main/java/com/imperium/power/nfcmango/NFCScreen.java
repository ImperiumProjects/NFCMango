package com.imperium.power.nfcmango;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;

public class NFCScreen extends AppCompatActivity {

    private static final String LOG_TAG = NFCScreen.class.getSimpleName();
    private static final int BARCODE_READER_REQUEST_CODE = 1;
    private NfcAdapter mNfcAdapter;

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    int stoppedMilliseconds = 0;
    String s;

    Chronometer mChronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcscreen);

        mChronometer = (Chronometer) findViewById(R.id.chronometer2);
        TextView username = (TextView) findViewById(R.id.usernameFieldNFC);

        if(HomeScreen.username != null){
            username.setText(HomeScreen.username);
        }
        else{
            try{
                FileInputStream fileIn = openFileInput(HomeScreen.usernameFilename);
                InputStreamReader InputRead = new InputStreamReader(fileIn);

                char[] inputBuffer = new char[HomeScreen.READ_BLOCK_SIZE];
                s="";
                int charRead;

                while((charRead = InputRead.read(inputBuffer))> 0){
                    String readString = String.copyValueOf(inputBuffer, 0, charRead);
                    s += readString;
                }
                InputRead.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            username.setText(s);
            Log.d("lolwheres", s);
        }

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        String chronoText = mChronometer.getText().toString();
        String array[] = chronoText.split(":");
        if(array.length == 2){
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                    + Integer.parseInt(array[1]) * 1000;
        }
        else if(array.length == 3){
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                    + Integer.parseInt(array[1]) * 60 * 1000
                    + Integer.parseInt(array[2]) * 1000;
        }

        mChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
        mChronometer.start();

        handleIntent(getIntent());

        if (PkmnListFragment.data.isEmpty()) {
            //Create hash map to store strings + images
            HashMap<String, String> map = new HashMap<String, String>();
            //Creates new hash map for each pair
            for (int i = 0; i < PkmnListFragment.blank_names.length; i++) {
                map = new HashMap<String, String>();
                map.put("Pkmn", PkmnListFragment.blank_names[i]);
                map.put("Image", Integer.toString(PkmnListFragment.pokemon[i]));
                //Stores each hash map in ArrayList
                PkmnListFragment.data.add(map);
            }
        }
        //KEYS IN MAP
        String[] from = {"Pkmn", "Image"};
        //IDS OF VIEWS
        int[] to = {R.id.nameTxt, R.id.imageView1};

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putInt("stoppedTime", stoppedMilliseconds);
        // etc.
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        stoppedMilliseconds = savedInstanceState.getInt("stoppedTime");
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    public void tapQR(View view) {
        Intent intent = new Intent(this, BarcodeCaptureActivity.class);
        startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        stopForegroundDispatch(this, mNfcAdapter);
        super.onPause();
    }

    /**
     * This method gets called, when a new Intent gets associated with the current activity instance.
     * Instead of creating a new activity, onNewIntent will be called. For more information have a look
     * at the documentation.
     *
     * In our case this method gets called, when the user attaches a Tag to the device.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);
            }
            else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        }
        else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }

    /**
     * processes the result of a successful QR code scan
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    if(barcode.displayValue.equals("964")){
                        if(!CharmanderDetails.alreadyCaught) {
                            CaughtList.incrementNumberCaught(barcode.displayValue);
                        }
                        Intent intent = new Intent(getApplicationContext(), CharmanderDetails.class);
                        startActivity(intent);
                    }
                    if(barcode.displayValue.equals("823")){
                        if(!BulbasaurDetails.alreadyCaught) {
                            CaughtList.incrementNumberCaught(barcode.displayValue);
                        }
                        Intent intent = new Intent(getApplicationContext(), BulbasaurDetails.class);
                        startActivity(intent);
                    }
                    if(barcode.displayValue.equals("728")){
                        if(!SquirtleDetails.alreadyCaught) {
                            CaughtList.incrementNumberCaught(barcode.displayValue);
                        }
                        Intent intent = new Intent(getApplicationContext(), SquirtleDetails.class);
                        startActivity(intent);
                    }
                    if(barcode.displayValue.equals("38")){
                        if(!PikachuDetails.alreadyCaught) {
                            CaughtList.incrementNumberCaught(barcode.displayValue);
                        }
                        Intent intent = new Intent(getApplicationContext(), PikachuDetails.class);
                        startActivity(intent);
                    }
                    if(barcode.displayValue.equals("741")){
                        if(!PidgeyDetails.alreadyCaught) {
                            CaughtList.incrementNumberCaught(barcode.displayValue);
                        }
                        Intent intent = new Intent(getApplicationContext(), PidgeyDetails.class);
                        startActivity(intent);
                    }
                    if(barcode.displayValue.equals("581")){
                        if(!OnixDetails.alreadyCaught) {
                            CaughtList.incrementNumberCaught(barcode.displayValue);
                        }
                        Intent intent = new Intent(getApplicationContext(), OnixDetails.class);
                        startActivity(intent);
                    }
                    if(barcode.displayValue.equals("562")){
                        if(!MeowthDetails.alreadyCaught) {
                            CaughtList.incrementNumberCaught(barcode.displayValue);
                        }
                        Intent intent = new Intent(getApplicationContext(), MeowthDetails.class);
                        startActivity(intent);
                    }
                    if(barcode.displayValue.equals("664")){
                        if(!EeveeDetails.alreadyCaught) {
                            CaughtList.incrementNumberCaught(barcode.displayValue);
                        }
                        Intent intent = new Intent(getApplicationContext(), EeveeDetails.class);
                        startActivity(intent);
                    }
                    if(barcode.displayValue.equals("82")){
                        if(!AbraDetails.alreadyCaught) {
                            CaughtList.incrementNumberCaught(barcode.displayValue);
                        }
                        Intent intent = new Intent(getApplicationContext(), AbraDetails.class);
                        startActivity(intent);
                    }
                    if(barcode.displayValue.equals("481")){
                        if(!JigglypuffDetails.alreadyCaught) {
                            CaughtList.incrementNumberCaught(barcode.displayValue);
                        }
                        Intent intent = new Intent(getApplicationContext(), JigglypuffDetails.class);
                        startActivity(intent);
                    }
                    if(barcode.displayValue.equals("319")){
                        if(!ChanseyDetails.alreadyCaught) {
                            CaughtList.incrementNumberCaught(barcode.displayValue);
                        }
                        Intent intent = new Intent(getApplicationContext(), ChanseyDetails.class);
                        startActivity(intent);
                    }
                    if(barcode.displayValue.equals("970")){
                        if(!SlowpokeDetails.alreadyCaught) {
                            CaughtList.incrementNumberCaught(barcode.displayValue);
                        }
                        Intent intent = new Intent(getApplicationContext(), SlowpokeDetails.class);
                        startActivity(intent);
                    }
                    if(barcode.displayValue.equals("888")){
                        if(!CuboneDetails.alreadyCaught) {
                            CaughtList.incrementNumberCaught(barcode.displayValue);
                        }
                        Intent intent = new Intent(getApplicationContext(), CuboneDetails.class);
                        startActivity(intent);
                    }
                    if(barcode.displayValue.equals("161")){
                        if(!MrMimeDetails.alreadyCaught) {
                            CaughtList.incrementNumberCaught(barcode.displayValue);
                        }
                        Intent intent = new Intent(getApplicationContext(), MrMimeDetails.class);
                        startActivity(intent);
                    }
                    if(barcode.displayValue.equals("457")){
                        if(!FlareonDetails.alreadyCaught) {
                            CaughtList.incrementNumberCaught(barcode.displayValue);
                        }
                        Intent intent = new Intent(getApplicationContext(), FlareonDetails.class);
                        startActivity(intent);
                    }
                    if(barcode.displayValue.equals("905")){
                        if(!JolteonDetails.alreadyCaught) {
                            CaughtList.incrementNumberCaught(barcode.displayValue);
                        }
                        Intent intent = new Intent(getApplicationContext(), JolteonDetails.class);
                        startActivity(intent);
                    }
                    if(barcode.displayValue.equals("225")){
                        if(!VaporeonDetails.alreadyCaught) {
                            CaughtList.incrementNumberCaught(barcode.displayValue);
                        }
                        Intent intent = new Intent(getApplicationContext(), VaporeonDetails.class);
                        startActivity(intent);
                    }
                    if(barcode.displayValue.equals("961")){
                        if(!MewtwoDetails.alreadyCaught) {
                            CaughtList.incrementNumberCaught(barcode.displayValue);
                        }
                        Intent intent = new Intent(getApplicationContext(), MewtwoDetails.class);
                        startActivity(intent);
                    }
                }
            }
            else Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format), CommonStatusCodes.getStatusCodeString(resultCode)));
        }
        else super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    /**
     * @param activity The corresponding {@link Activity} requesting to stop the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    /** Called when the user taps the View Caught Nfcm button */
    public void viewCaught(View view) {
        Intent intent = new Intent(this, CaughtList.class);
        startActivity(intent);
    }

    /**
     * Background task for reading the data. Do not block the UI thread while reading.
     */
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                if(result.equals("964")){
                    if(!CharmanderDetails.alreadyCaught) {
                        CaughtList.incrementNumberCaught(result);
                    }
                    Intent intent = new Intent(getApplicationContext(), CharmanderDetails.class);
                    startActivity(intent);
                }
                else if(result.equals("823")){
                    if(!BulbasaurDetails.alreadyCaught) {
                        CaughtList.incrementNumberCaught(result);
                    }
                    Intent intent = new Intent(getApplicationContext(), BulbasaurDetails.class);
                    startActivity(intent);
                }
                else if(result.equals("728")){
                    if(!SquirtleDetails.alreadyCaught) {
                        CaughtList.incrementNumberCaught(result);
                    }
                    Intent intent = new Intent(getApplicationContext(), SquirtleDetails.class);
                    startActivity(intent);
                }
                else if(result.equals("38")){
                    if(!PikachuDetails.alreadyCaught) {
                        CaughtList.incrementNumberCaught(result);
                    }
                    Intent intent = new Intent(getApplicationContext(), PikachuDetails.class);
                    startActivity(intent);
                }
                else if(result.equals("741")){
                    if(!PidgeyDetails.alreadyCaught) {
                        CaughtList.incrementNumberCaught(result);
                    }
                    Intent intent = new Intent(getApplicationContext(), PidgeyDetails.class);
                    startActivity(intent);
                }
                else if(result.equals("581")){
                    if(!OnixDetails.alreadyCaught) {
                        CaughtList.incrementNumberCaught(result);
                    }
                    Intent intent = new Intent(getApplicationContext(), OnixDetails.class);
                    startActivity(intent);
                }
                else if(result.equals("562")){
                    if(!MeowthDetails.alreadyCaught) {
                        CaughtList.incrementNumberCaught(result);
                    }
                    Intent intent = new Intent(getApplicationContext(), MeowthDetails.class);
                    startActivity(intent);
                }
                else if(result.equals("664")){
                    if(!EeveeDetails.alreadyCaught) {
                        CaughtList.incrementNumberCaught(result);
                    }
                    Intent intent = new Intent(getApplicationContext(), EeveeDetails.class);
                    startActivity(intent);
                }
                else if(result.equals("82")){
                    if(!AbraDetails.alreadyCaught) {
                        CaughtList.incrementNumberCaught(result);
                    }
                    Intent intent = new Intent(getApplicationContext(), AbraDetails.class);
                    startActivity(intent);
                }
                else if(result.equals("481")){
                    if(!JigglypuffDetails.alreadyCaught) {
                        CaughtList.incrementNumberCaught(result);
                    }
                    Intent intent = new Intent(getApplicationContext(), JigglypuffDetails.class);
                    startActivity(intent);
                }
                else if(result.equals("319")){
                    if(!ChanseyDetails.alreadyCaught) {
                        CaughtList.incrementNumberCaught(result);
                    }
                    Intent intent = new Intent(getApplicationContext(), ChanseyDetails.class);
                    startActivity(intent);
                }
                else if(result.equals("970")){
                    if(!SlowpokeDetails.alreadyCaught) {
                        CaughtList.incrementNumberCaught(result);
                    }
                    Intent intent = new Intent(getApplicationContext(), SlowpokeDetails.class);
                    startActivity(intent);
                }
                else if(result.equals("888")){
                    if(!CuboneDetails.alreadyCaught) {
                        CaughtList.incrementNumberCaught(result);
                    }
                    Intent intent = new Intent(getApplicationContext(), CuboneDetails.class);
                    startActivity(intent);
                }
                else if(result.equals("161")){
                    if(!MrMimeDetails.alreadyCaught) {
                        CaughtList.incrementNumberCaught(result);
                    }
                    Intent intent = new Intent(getApplicationContext(), MrMimeDetails.class);
                    startActivity(intent);
                }
                else if(result.equals("457")){
                    if(!FlareonDetails.alreadyCaught) {
                        CaughtList.incrementNumberCaught(result);
                    }
                    Intent intent = new Intent(getApplicationContext(), FlareonDetails.class);
                    startActivity(intent);
                }
                else if(result.equals("905")){
                    if(!JolteonDetails.alreadyCaught) {
                        CaughtList.incrementNumberCaught(result);
                    }
                    Intent intent = new Intent(getApplicationContext(), JolteonDetails.class);
                    startActivity(intent);
                }
                else if(result.equals("225")){
                    if(!VaporeonDetails.alreadyCaught) {
                        CaughtList.incrementNumberCaught(result);
                    }
                    Intent intent = new Intent(getApplicationContext(), VaporeonDetails.class);
                    startActivity(intent);
                }
                else if(result.equals("961")){
                    if(!MewtwoDetails.alreadyCaught) {
                        CaughtList.incrementNumberCaught(result);
                    }
                    Intent intent = new Intent(getApplicationContext(), MewtwoDetails.class);
                    startActivity(intent);
                }
            }
        }
    }
}
