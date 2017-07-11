package com.imperium.power.nfcmango;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.ServiceConnection;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;

public class NFCScreen extends AppCompatActivity {

    private static final String LOG_TAG = NFCScreen.class.getSimpleName();
    private static final int BARCODE_READER_REQUEST_CODE = 1;
    private NfcAdapter mNfcAdapter;
    private TimerService timerService;
    public static boolean serviceBound;
    private TextView timerTextView;

    private final Handler mUpdateTimeHandler = new UIUpdateHandler(this);

    private final static int MSG_UPDATE_TIME = 0;

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    public static boolean timerStarted;
    String s;
    static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcscreen);

        TextView usernameField = (TextView) findViewById(R.id.usernameFieldNFC);
        timerTextView = (TextView) findViewById(R.id.timerTextView);

        if(HomeScreen.username != null){
            usernameField.setText(HomeScreen.username);
            username = HomeScreen.username;
        }
        else{
            timerStarted = true;
            try {
                FileInputStream fileIn = openFileInput(HomeScreen.usernameFilename);
                InputStreamReader InputRead = new InputStreamReader(fileIn);

                char[] inputBuffer = new char[HomeScreen.READ_BLOCK_SIZE];
                s = "";
                int charRead;

                while ((charRead = InputRead.read(inputBuffer)) > 0) {
                    String readString = String.copyValueOf(inputBuffer, 0, charRead);
                    s += readString;
                }
                InputRead.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
            usernameField.setText(s);
            username = s;
        }

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if(serviceBound && !timerService.isTimerRunning()){
            if(Log.isLoggable(LOG_TAG, Log.VERBOSE)){
                Log.v(LOG_TAG, "Starting Timer");
            }
        }
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
    public void onWindowFocusChanged(boolean hasFocus){
        if(hasFocus){
            timerService.startTimer();
            updateUIStartRun();
        }
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("memelol", "received something");
        }
    };

    @Override
    protected void onStart(){
        super.onStart();
        if(Log.isLoggable(LOG_TAG, Log.VERBOSE)){
            Log.v(LOG_TAG, "Starting and binding service");
        }
        Intent i = new Intent(this, TimerService.class);
        startService(i);
        bindService(i, mConnection, 0);
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

    @Override
    public void onStop(){
        super.onStop();
        updateUIStopRun();
        if(serviceBound){
            //If a timer is active, foreground the service, otherwise kill the service
            if(timerService.isTimerRunning()){
                timerService.foreground();
            }
            else{
                stopService(new Intent(this, TimerService.class));
            }
            //Unbind the service
            unbindService(mConnection);
            serviceBound = false;
        }
    }

    /**
     * Updates the UI when a run starts
     */
    private void updateUIStartRun(){
        mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
    }

    /**
     * Updates the UI when a run stops
     */
    private void updateUIStopRun(){
        mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
    }

    private void updateUITimer(){
        if(serviceBound){
            long ms = timerService.elapsedTime() * 1000;
            int seconds = (int) (ms / 1000) % 60;
            int minutes = (int) ((ms / (1000*60)) % 60);
            int hours = (int) ((ms / (1000*60*60)) % 24);
            timerTextView.setText("" + hours + ":" + minutes + ":" + seconds);
            TextView finished = (TextView) findViewById(R.id.finishedTextView);
            if(CaughtList.numberCaught == 18){
                timerTextView.setVisibility(View.GONE);
                finished.setText("Return to the ITS Booth!");
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName className, IBinder service){
            if(Log.isLoggable(LOG_TAG, Log.VERBOSE)){
                Log.v(LOG_TAG, "Service Bound");
            }
            TimerService.RunServiceBinder binder = (TimerService.RunServiceBinder) service;
            timerService = binder.getService();
            serviceBound = true;
            // Ensure the service is not in the foreground when bound
            timerService.background();
            // Update the UI if the service is already running the timer
            if(timerService.isTimerRunning()){
                updateUIStartRun();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name){
            if (Log.isLoggable(LOG_TAG, Log.VERBOSE)){
                Log.v(LOG_TAG, "Service disconnect");
            }
            serviceBound = false;
        }
    };

    static class UIUpdateHandler extends Handler{

        private final static int UPDATE_RATE_MS = 1000;
        private final WeakReference<NFCScreen> activity;

        UIUpdateHandler(NFCScreen activity){
            this.activity = new WeakReference<NFCScreen>(activity);
        }

        @Override
        public void handleMessage(Message message){
            if(MSG_UPDATE_TIME == message.what){
                if(Log.isLoggable(LOG_TAG, Log.VERBOSE)){
                    Log.v(LOG_TAG, "Update time");
                }
                activity.get().updateUITimer();
                sendEmptyMessageDelayed(MSG_UPDATE_TIME, UPDATE_RATE_MS);
            }
        }
    }

    public static class TimerService extends Service{

        private static final String TAG = TimerService.class.getSimpleName();

        private long startTime, endTime;

        private boolean isTimerRunning;

        static final int NOTIFICATION_ID = 1;

        private final IBinder serviceBinder = new RunServiceBinder();

        public class RunServiceBinder extends Binder{
            TimerService getService(){
                return TimerService.this;
            }
        }

        @Override
        public void onCreate(){
            if(Log.isLoggable(TAG, Log.VERBOSE)){
                Log.v(TAG, "Creating service");
            }
            startTime = 0;
            endTime = 0;
            isTimerRunning = false;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId){
            if(Log.isLoggable(TAG, Log.VERBOSE)){
                Log.v(TAG, "Starting service");
            }
            return Service.START_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent){
            if(Log.isLoggable(TAG, Log.VERBOSE)){
                Log.v(TAG, "Binding service");
            }
            return serviceBinder;
        }

        @Override
        public void onDestroy(){
            super.onDestroy();
            if(Log.isLoggable(TAG, Log.VERBOSE)){
                Log.v(TAG, "Destroying service");
            }
        }

        public void startTimer(){
            if(!isTimerRunning){
                startTime = System.currentTimeMillis();
                isTimerRunning = true;
            }
            else{
                Log.e(TAG, "startTimer request for an already running timer");
            }
        }

        public void stopTimer(){
            if(isTimerRunning){
                endTime = System.currentTimeMillis();
                isTimerRunning = false;
            }
            else{
                Log.e(TAG, "stopTimer request for a timer that isn't running");
            }
        }

        public boolean isTimerRunning(){
            return isTimerRunning;
        }

        public long elapsedTime(){
            //If timer is running, end time will be zero
            return endTime > startTime ?
                    (endTime - startTime) / 1000 :
                    (System.currentTimeMillis() - startTime) / 1000;
        }

        public void foreground(){
            if(CaughtList.numberCaught != 18) {
                startForeground(NOTIFICATION_ID, createNotification());
            }
        }

        public void background(){
            stopForeground(true);
        }

        private Notification createNotification(){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle("EMBLmon Go")
                    .setContentText("Still more to catch! Tap to return!")
                    .setSmallIcon(R.mipmap.pokeball_all_green);

            Intent resultIntent = new Intent(this, NFCScreen.class);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(this, 0, resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);

            return builder.build();
        }
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
