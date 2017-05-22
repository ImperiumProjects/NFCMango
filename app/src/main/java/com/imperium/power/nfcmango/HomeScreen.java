package com.imperium.power.nfcmango;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }

    /** Called when the user taps the Catch! button */
    public void tapCatch(View view) {
        Intent intent = new Intent(this, NFCScreen.class);
        startActivity(intent);
    }
}
