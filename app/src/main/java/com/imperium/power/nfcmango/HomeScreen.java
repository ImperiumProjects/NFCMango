package com.imperium.power.nfcmango;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        ImageView pkball = (ImageView) findViewById(R.id.pokeballClick);

        pkball.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NFCScreen.class);
                startActivity(intent);
            }
        });
    }
    /*
    public void tapCatch(View view) {
        Intent intent = new Intent(this, NFCScreen.class);
        startActivity(intent);
    }
    */
}
