package com.imperium.power.nfcmango;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class CaughtList extends AppCompatActivity {

    static int numberCaught = 0;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caught_list);
        mTextView = (TextView)findViewById(R.id.caught_list_banner);
        mTextView.setText("You have caught " + numberCaught + " out of 18 Pkmn");
    }

    /**
     * increments numberCaught array when NFC or QR scanned
     * sends update http request to leaderboard
     * @param pkmn string name of pkmn
     */
    public static void incrementNumberCaught(String pkmn) {
        numberCaught += 1;
        PkmnListFragment.updateListFragment(pkmn);
    }
}
