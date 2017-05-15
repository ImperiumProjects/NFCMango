package com.imperium.power.nfcmango;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class CaughtList extends ListActivity {

    static final String[] PKMN =
            new String[] { "Pikachu", "Vulpix", "Dragonite",
                    "Bulbasaur", "Oddish", "Seadra"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_caught_list);
        setListAdapter(new MobileArrayAdapter(this, PKMN));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //get selected items
        String selectedValue = (String) getListAdapter().getItem(position);
        Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();

    }
}
