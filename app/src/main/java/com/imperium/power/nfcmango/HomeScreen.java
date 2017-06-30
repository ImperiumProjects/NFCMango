package com.imperium.power.nfcmango;

import android.content.Context;
import android.content.Intent;
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

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;

public class HomeScreen extends AppCompatActivity {

    static String username;
    static String password;
    private String filename = "timerFile";

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
            passwordField.setTransformationMethod(new PasswordTransformationMethod());
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
            sdf.setTimeZone(TimeZone.getDefault());
            String currentDateandTime = sdf.format(new Date());

            SimpleDateFormat timerTime = new SimpleDateFormat("HH:mm:ss");
            String timerString = timerTime.format(new Date());
            final String timer = timerString;
            ///
            try {
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("name", username);
                params.put("password", password);
                params.put("start_time", currentDateandTime);
                client.post("http://labday01.embl.de/login.php", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Intent intent = new Intent(getApplicationContext(), NFCScreen.class);

                        try{
                            FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                            outputStream.write(timer.getBytes());
                            outputStream.close();
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }

                        startActivity(intent);
                        finish();
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
            //Intent intent = new Intent(getApplicationContext(), NFCScreen.class);
            //startActivity(intent);
            //finish();
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
}
