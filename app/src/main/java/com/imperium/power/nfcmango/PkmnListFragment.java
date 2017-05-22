package com.imperium.power.nfcmango;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class PkmnListFragment extends ListFragment implements OnItemClickListener {

    String[] names = new String[] {
            "Pikachu",
            "Bulbasaur",
            "Dragonite",
            "Seadra",
            "Oddish",
            "Vulpix"
    };

    int[] pokemon = new int[]{
            R.drawable.pika,
            R.drawable.bulb,
            R.drawable.drag,
            R.drawable.hors,
            R.drawable.odd,
            R.drawable.vulp
    };

    static ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
    SimpleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (data.isEmpty()) {
            // TODO Auto-generated method stub
            //MAP
            HashMap<String, String> map = new HashMap<String, String>();
            //FILL
            for (int i = 0; i < names.length; i++) {
                map = new HashMap<String, String>();
                map.put("Pkmn", names[i]);
                map.put("Image", Integer.toString(pokemon[i]));
                data.add(map);
            }
        }
        //KEYS IN MAP
        String[] from = {"Pkmn", "Image"};
        //IDS OF VIEWS
        int[] to = {R.id.nameTxt, R.id.imageView1};
        //ADAPTER
        adapter = new SimpleAdapter(getActivity(), data, R.layout.list_fragment, from, to);
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos,
                                    long id) {
                Intent intent;
                String pkmnClicked = data.get(pos).get("Pkmn");
                if(pkmnClicked.equals("Pikachu")){
                    if(PikachuDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), PikachuDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(pkmnClicked.equals("Bulbasaur")){
                    if(BulbasaurDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), BulbasaurDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(pkmnClicked.equals("Dragonite")){
                    if(DragoniteDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), DragoniteDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(pkmnClicked.equals("Seadra")){
                    if(SeadraDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), SeadraDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(pkmnClicked.equals("Oddish")){
                    if(OddishDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), OddishDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(pkmnClicked.equals("Vulpix")){
                    if(VulpixDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), VulpixDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
        Intent intent;
        String pkmnClicked = data.get(pos).get("Pkmn");
        if(pkmnClicked.equals("Pikachu")){
            intent = new Intent(v.getContext(), PikachuDetails.class);
            startActivity(intent);
        }
        else if(pkmnClicked.equals("Bulbasaur")){
            intent = new Intent(v.getContext(), BulbasaurDetails.class);
            startActivity(intent);
        }
        else if(pkmnClicked.equals("Dragonite")){
            intent = new Intent(v.getContext(), DragoniteDetails.class);
            startActivity(intent);
        }
        else if(pkmnClicked.equals("Seadra")){
            intent = new Intent(v.getContext(), SeadraDetails.class);
            startActivity(intent);
        }
        else if(pkmnClicked.equals("Oddish")){
            intent = new Intent(v.getContext(), OddishDetails.class);
            startActivity(intent);
        }
        else if(pkmnClicked.equals("Vulpix")){
            intent = new Intent(v.getContext(), VulpixDetails.class);
            startActivity(intent);
        }
        else{
        }
    }
}