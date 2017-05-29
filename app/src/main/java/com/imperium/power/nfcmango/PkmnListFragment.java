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

    static String[] blank_names = new String[] {
            "??????????",
            "??????????",
            "??????????",
            "??????????",
            "??????????",
            "??????????",
            "??????????",
            "??????????",
            "??????????",
            "??????????",
            "??????????",
            "??????????",
            "??????????",
            "??????????",
            "??????????",
            "??????????",
            "??????????",
            "??????????"
    };

    static String[] names = new String[] {
            "Charmander",
            "Bulbasaur",
            "Squirtle",
            "Pikachu",
            "Pidgey",
            "Onix",
            "Meowth",
            "Eevee",
            "Abra",
            "Jigglypuff",
            "Chansey",
            "Slowpoke",
            "Cubone",
            "Mr. Mime",
            "Flareon",
            "Jolteon",
            "Vaporeon",
            "Mewtwo"
    };

    static int[] pokemon = new int[]{
            R.drawable.charmander_silhouette,
            R.drawable.bulbasaur_silhouette,
            R.drawable.squirtle_silhouette,
            R.drawable.pikachu_silhouette,
            R.drawable.pidgey_silhouette,
            R.drawable.onix_silhouette,
            R.drawable.meowth_silhouette,
            R.drawable.eevee_silhouette,
            R.drawable.abra_silhouette,
            R.drawable.jigglypuff_silhouette,
            R.drawable.chansey_silhouette,
            R.drawable.slowpoke_silhouette,
            R.drawable.cubone_silhouette,
            R.drawable.mr_mime_silhouette,
            R.drawable.flareon_silhouette,
            R.drawable.jolteon_silhouette,
            R.drawable.vaporeon_silhouette,
            R.drawable.mewtwo_silhouette
    };

    static int[] clr_pokemon = new int[]{
            R.drawable.charmander,
            R.drawable.bulbasaur,
            R.drawable.squirtle,
            R.drawable.pikachu,
            R.drawable.pidgey,
            R.drawable.onix,
            R.drawable.meowth,
            R.drawable.eevee,
            R.drawable.abra,
            R.drawable.jigglypuff,
            R.drawable.chansey,
            R.drawable.slowpoke,
            R.drawable.cubone,
            R.drawable.mr_mime,
            R.drawable.flareon,
            R.drawable.jolteon,
            R.drawable.vaporeon,
            R.drawable.mewtwo
    };

    static ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
    static SimpleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (data.isEmpty()) {
            // TODO Auto-generated method stub
            //MAP
            HashMap<String, String> map = new HashMap<String, String>();
            //FILL
            for (int i = 0; i < blank_names.length; i++) {
                map = new HashMap<String, String>();
                map.put("Pkmn", blank_names[i]);
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

    public static void updateListFragment(String pkmnNumber){

        HashMap<String, String> map = new HashMap<String, String>();

        for(int i = 0; i < data.size(); i++){
            if (Integer.parseInt(pkmnNumber) == i + 1){
                map = new HashMap<String, String>();
                map.put("Pkmn", names[i]);
                map.put("Image", Integer.toString(clr_pokemon[i]));
                data.set(i, map);
            }
        }

        //KEYS IN MAP
        String[] from = {"Pkmn", "Image"};
        //IDS OF VIEWS
        int[] to = {R.id.nameTxt, R.id.imageView1};
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
                if(pkmnClicked.equals("Charmander")){
                    if(CharmanderDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), CharmanderDetails.class);
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
                else if(pkmnClicked.equals("Squirtle")){
                    if(SquirtleDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), SquirtleDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(pkmnClicked.equals("Pikachu")){
                    if(PikachuDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), PikachuDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(pkmnClicked.equals("Pidgey")){
                    if(PidgeyDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), PidgeyDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(pkmnClicked.equals("Onix")){
                    if(OnixDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), OnixDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(pkmnClicked.equals("Meowth")){
                    if(MeowthDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), MeowthDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(pkmnClicked.equals("Eevee")){
                    if(EeveeDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), EeveeDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(pkmnClicked.equals("Abra")){
                    if(AbraDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), AbraDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(pkmnClicked.equals("Jigglypuff")){
                    if(JigglypuffDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), JigglypuffDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(pkmnClicked.equals("Chansey")){
                    if(ChanseyDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), ChanseyDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(pkmnClicked.equals("Slowpoke")){
                    if(SlowpokeDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), SlowpokeDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(pkmnClicked.equals("Cubone")){
                    if(CuboneDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), CuboneDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(pkmnClicked.equals("Mr. Mime")){
                    if(MrMimeDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), MrMimeDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(pkmnClicked.equals("Flareon")){
                    if(FlareonDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), FlareonDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(pkmnClicked.equals("Jolteon")){
                    if(JolteonDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), JolteonDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(pkmnClicked.equals("Vaporeon")){
                    if(VaporeonDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), VaporeonDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(pkmnClicked.equals("Mewtwo")){
                    if(MewtwoDetails.alreadyCaught) {
                        intent = new Intent(v.getContext(), MewtwoDetails.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
        Intent intent;
        String pkmnClicked = data.get(pos).get("Pkmn");
        if(pkmnClicked.equals("Charmander")){
            if(CharmanderDetails.alreadyCaught) {
                intent = new Intent(v.getContext(), CharmanderDetails.class);
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
        else if(pkmnClicked.equals("Squirtle")){
            if(SquirtleDetails.alreadyCaught) {
                intent = new Intent(v.getContext(), SquirtleDetails.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
            }
        }
        else if(pkmnClicked.equals("Pikachu")){
            if(PikachuDetails.alreadyCaught) {
                intent = new Intent(v.getContext(), PikachuDetails.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
            }
        }
        else if(pkmnClicked.equals("Pidgey")){
            if(PidgeyDetails.alreadyCaught) {
                intent = new Intent(v.getContext(), PidgeyDetails.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
            }
        }
        else if(pkmnClicked.equals("Onix")){
            if(OnixDetails.alreadyCaught) {
                intent = new Intent(v.getContext(), OnixDetails.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
            }
        }
        else if(pkmnClicked.equals("Meowth")){
            if(MeowthDetails.alreadyCaught) {
                intent = new Intent(v.getContext(), MeowthDetails.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
            }
        }
        else if(pkmnClicked.equals("Eevee")){
            if(EeveeDetails.alreadyCaught) {
                intent = new Intent(v.getContext(), EeveeDetails.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
            }
        }
        else if(pkmnClicked.equals("Abra")){
            if(AbraDetails.alreadyCaught) {
                intent = new Intent(v.getContext(), AbraDetails.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
            }
        }
        else if(pkmnClicked.equals("Jigglypuff")){
            if(JigglypuffDetails.alreadyCaught) {
                intent = new Intent(v.getContext(), JigglypuffDetails.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
            }
        }
        else if(pkmnClicked.equals("Chansey")){
            if(ChanseyDetails.alreadyCaught) {
                intent = new Intent(v.getContext(), ChanseyDetails.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
            }
        }
        else if(pkmnClicked.equals("Slowpoke")){
            if(SlowpokeDetails.alreadyCaught) {
                intent = new Intent(v.getContext(), SlowpokeDetails.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
            }
        }
        else if(pkmnClicked.equals("Cubone")){
            if(CuboneDetails.alreadyCaught) {
                intent = new Intent(v.getContext(), CuboneDetails.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
            }
        }
        else if(pkmnClicked.equals("Mr. Mime")){
            if(MrMimeDetails.alreadyCaught) {
                intent = new Intent(v.getContext(), MrMimeDetails.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
            }
        }
        else if(pkmnClicked.equals("Flareon")){
            if(FlareonDetails.alreadyCaught) {
                intent = new Intent(v.getContext(), FlareonDetails.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
            }
        }
        else if(pkmnClicked.equals("Jolteon")){
            if(JolteonDetails.alreadyCaught) {
                intent = new Intent(v.getContext(), JolteonDetails.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
            }
        }
        else if(pkmnClicked.equals("Vaporeon")){
            if(VaporeonDetails.alreadyCaught) {
                intent = new Intent(v.getContext(), VaporeonDetails.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
            }
        }
        else if(pkmnClicked.equals("Mewtwo")){
            if(MewtwoDetails.alreadyCaught) {
                intent = new Intent(v.getContext(), MewtwoDetails.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(), "You have not caught this Pkmn!", Toast.LENGTH_LONG).show();
            }
        }
    }
}