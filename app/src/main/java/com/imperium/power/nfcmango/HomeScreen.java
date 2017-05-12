package com.imperium.power.nfcmango;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HomeScreen extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.imperium.power.nfcmango";
    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_home_screen, mobileArray);
        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
    }

    /** Called when the user taps the Catch! button */
    public void tapCatch(View view) {
        Intent intent = new Intent(this, NFCScreen.class);
        startActivity(intent); //this is causing crashes
    }
}
