package com.imperium.power.nfcmango;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import cz.msebera.android.httpclient.Header;

import static com.imperium.power.nfcmango.NFCScreen.MIME_TEXT_PLAIN;

public class HomeScreen extends AppCompatActivity {

    static String username;
    static String password;
    static String filename = "timerFile";
    private NfcAdapter mNfcAdapter;
    KeyStore keyStore;
    static String currentDateandTime;
    static String timerString;

    /**
     * Creates view and adds setOnClickListener for pkball image
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home_screen);

            final EditText passwordField = (EditText) findViewById(R.id.pwField);

            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
            Activity activity = this;
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
            } catch (IntentFilter.MalformedMimeTypeException e) {
                throw new RuntimeException("Check your mime type.");
            }
            mNfcAdapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);

            passwordField.setTransformationMethod(new PasswordTransformationMethod());

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = new BufferedInputStream(new FileInputStream("/assets/certs/privkey.pem"));
            Certificate ca;
            try{
                ca = cf.generateCertificate(caInput);
            }
            finally{
                caInput.close();
            }

            String keyStoreType = KeyStore.getDefaultType();
            keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tapPkBall(View view){

        final EditText usernameField = (EditText) findViewById(R.id.unField);
        final EditText passwordField = (EditText) findViewById(R.id.pwField);
        final TextView error = (TextView) findViewById(R.id.error_text);

        if(!(usernameField.getText().toString().equals("")) && !(passwordField.getText().toString().equals(""))){
            username = usernameField.getText().toString();
            password = passwordField.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            currentDateandTime = sdf.format(new Date());

            SimpleDateFormat timerTime = new SimpleDateFormat("HH:mm:ss");
            timerString = timerTime.format(new Date());
            final String timer = timerString;
            ///
            try {
                AsyncHttpClient client = new AsyncHttpClient();
                MyCustomSSLFactory socketFactory = new MyCustomSSLFactory(keyStore);
                client.setSSLSocketFactory(socketFactory);
                RequestParams params = new RequestParams();
                params.put("name", username);
                params.put("password", password);
                params.put("start_time", currentDateandTime);
                client.post("https://labday01.embl.de/login.php", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Intent intent = new Intent(getApplicationContext(), NFCScreen.class);

                        try{
                            FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                            outputStream.write(timer.getBytes());
                            outputStream.close();
                            String str = new String(responseBody, "UTF-8");
                            Log.d("successResponse", "Success: " + statusCode);
                            Log.d("successBody", "Body :" + str);
                            if(str.equals("Successsuccess")){
                                startActivity(intent);
                                finish();
                            }
                            else{
                                error.setText("There was an error logging in, please check your username and password");
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("codeResponse", "Error Code: " + statusCode);
                    }
                });
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        else{
            if(usernameField.getText().toString().equals("")){
                error.setText(R.string.home_screen_username_error);
            }
            else if(passwordField.getText().toString().equals("")){
                error.setText(R.string.home_screen_password_error);
            }
            else {
                error.setText(R.string.home_screen_error);
            }
        }
    }

    /**
     * This method gets called, when a new Intent gets associated with the current activity instance.
     * Instead of creating a new activity, onNewIntent will be called.
     *
     * In our case this method gets called, when the user attaches a Tag to the device.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("homescreenNfcAttach", "NFC Tag attached");
    }
}
