package com.imperium.power.nfcmango;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeScreen extends AppCompatActivity {

    static String username;
    static String password;

    /**
     * Creates view and adds setOnClickListener for pkball image
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home_screen);

            ImageView pkball = (ImageView) findViewById(R.id.pokeballClick);
            final EditText usernameField = (EditText) findViewById(R.id.unField);
            final EditText passwordField = (EditText) findViewById(R.id.pwField);
            final TextView error = (TextView) findViewById(R.id.error_text);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getDefault());
            final String currentDateAndTime = sdf.format(new Date());
            final OkHttpClient client = new OkHttpClient();

            passwordField.setTransformationMethod(new PasswordTransformationMethod());

            pkball.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(!(usernameField.getText().toString().equals("")) && !(passwordField.getText().toString().equals(""))){
                        username = usernameField.getText().toString();
                        password = passwordField.getText().toString();
                        ///
                        try {
                            doGetRequest("http://labday01.embl.de/login.php");
                        }
                        catch(IOException e){
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(getApplicationContext(), NFCScreen.class);
                        startActivity(intent);
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
                void doGetRequest(String url) throws IOException{
                    RequestBody body = new FormBody.Builder()
                            .add("name", username)
                            .add("password", password)
                            .add("start_time", currentDateAndTime)
                            .build();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();
                    client.newCall(request)
                            .enqueue(new Callback() {
                                @Override
                                public void onFailure(final Call call, IOException e){
                                    //Error
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run(){
                                            //show error or whatever
                                        }
                                    });
                                }
                                @Override
                                public void onResponse(Call call, final Response response) throws IOException{
                                    String res = response.body().string();
                                    Log.d("responsecode", res);
                                }
                            });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
