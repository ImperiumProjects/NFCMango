package com.imperium.power.nfcmango;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        TextView tx = (TextView)findViewById(R.id.screen_header);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Pokemon Solid.ttf");
        tx.setTypeface(custom_font);
    }

    /** Called when the user taps the Catch! button */
    public void tapCatch(View view) {
        Intent intent = new Intent(this, NFCScreen.class);
        startActivity(intent);
    }
}
