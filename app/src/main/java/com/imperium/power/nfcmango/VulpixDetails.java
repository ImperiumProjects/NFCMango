package com.imperium.power.nfcmango;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class VulpixDetails extends AppCompatActivity {

    public static boolean alreadyCaught = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vulpix_details);
        alreadyCaught = true;
    }
}
