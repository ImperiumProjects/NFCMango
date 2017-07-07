package com.imperium.power.nfcmango;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import cz.msebera.android.httpclient.Header;

public class CaughtList extends AppCompatActivity {

    static int numberCaught = 0;
    TextView mTextView;
    static int[] pkmnArray = {964,
            823,
            728,
            38,
            741,
            581,
            562,
            664,
            82,
            481,
            319,
            970,
            888,
            161,
            457,
            905,
            225,
            961};
    static String caughtPkmn;
    static KeyStore keyStore;
    String s;
    static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caught_list);
        mTextView = (TextView)findViewById(R.id.caught_list_banner);
        mTextView.setText("You have caught " + numberCaught + " out of 18 EMBLmon");

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

        username = s;

        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = new BufferedInputStream(new FileInputStream("/assets/certs/privkey.pem"));
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
            } finally {
                caInput.close();
            }
            String keyStoreType = KeyStore.getDefaultType();
            keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * increments numberCaught array when NFC or QR scanned
     * sends update http request to leaderboard
     * @param pkmn string name of pkmn
     */
    public static void incrementNumberCaught(String pkmn) {
        numberCaught += 1;
        for (int i = 0; i < pkmnArray.length; i++) {
            if (Integer.parseInt(pkmn) == pkmnArray[i]) {
                caughtPkmn = Integer.toString(i + 1);
                break;
            }
        }
        PkmnListFragment.updateListFragment(caughtPkmn);

        try {
            AsyncHttpClient client = new AsyncHttpClient();
            MyCustomSSLFactory socketFactory = new MyCustomSSLFactory(keyStore);
            client.setSSLSocketFactory(socketFactory);
            RequestParams params = new RequestParams();
            params.put("name", NFCScreen.username);
            params.put("caught", numberCaught);
            client.post("https://labday01.embl.de/updateCaught.php", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String str = new String(responseBody, "UTF-8");
                        Log.d("successResponse", str);
                        if (str.equals("success")) {
                            try {
                                Log.d("successfulUpdate", "Successfully updated EMBLmon");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (str.matches(".*\\SuccessDuplicate\\b.*")) {
                            Log.d("unsuccessfulUpdate", "Something went wrong (DUPLICATE)");
                        } else {
                            Log.d("veryUnsuccessfulUpdate", "SOMETHING REALLYWENT WRONG");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("codeResponse", "Error Code: " + statusCode);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(numberCaught == 18){
            //
            // Add HTTP POST code to finish timer
            //
        }
    }
}