package com.imperium.power.nfcmango;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class ChanseyDetails extends AppCompatActivity {

    //Boolean initialised as false as pokemon cannot be caught before view
    //is ever created
    public static boolean alreadyCaught = false;

    /**
     * Creates view and sets alreadyCaught var to true
     * @param savedInstanceState saved info
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chansey_details);
        alreadyCaught = true;

        TextView factText = (TextView) findViewById(R.id.factText);
        factText.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
