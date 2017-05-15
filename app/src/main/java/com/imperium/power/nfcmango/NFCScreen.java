package com.imperium.power.nfcmango;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class NFCScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcscreen);

        TextView tx = (TextView)findViewById(R.id.nfcscreen_header);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Pokemon Solid.ttf");
        tx.setTypeface(custom_font);
    }

    /** Called when the user taps the View Caught Nfcm button */
    public void viewCaught(View view) {
        Intent intent = new Intent(this, CaughtList.class);
        startActivity(intent);
    }
}
