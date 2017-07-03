package com.imperium.power.nfcmango;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class CaughtList extends AppCompatActivity {

    static int numberCaught = 0;
    TextView mTextView;
    static int[] pkmnArray = {964,
            823,
            728,
            38,
            741,
            581,
            562,
            664,
            82,
            481,
            319,
            970,
            888,
            161,
            457,
            905,
            225,
            961};
    static String caughtPkmn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caught_list);
        mTextView = (TextView)findViewById(R.id.caught_list_banner);
        mTextView.setText("You have caught " + numberCaught + " out of 18 EMBLmon");
    }

    /**
     * increments numberCaught array when NFC or QR scanned
     * sends update http request to leaderboard
     * @param pkmn string name of pkmn
     */
    public static void incrementNumberCaught(String pkmn) {
        numberCaught += 1;
        for(int i = 0; i < pkmnArray.length; i++){
            if(Integer.parseInt(pkmn) == pkmnArray[i]){
                caughtPkmn = Integer.toString(i + 1);
                break;
            }
        }
        PkmnListFragment.updateListFragment(caughtPkmn);
        //
        // Update number caught HTTPS
        //
        if (numberCaught == 18){
            //
            // Add HTTP POST code to finish timer
            //
        }
    }
}
