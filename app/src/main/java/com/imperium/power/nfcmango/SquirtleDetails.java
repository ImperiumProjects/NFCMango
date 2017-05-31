package com.imperium.power.nfcmango;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SquirtleDetails extends AppCompatActivity {

    //Boolean initialised as false as pokemon cannot be caught before view
    //is ever created
    public static boolean alreadyCaught = false;

    /**
     * Creates view and sets alreadyCaught var to true
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squirtle_details);
        alreadyCaught = true;
    }
}
