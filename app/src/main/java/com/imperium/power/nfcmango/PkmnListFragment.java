package com.imperium.power.nfcmango;

import android.app.ListFragment;
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

    ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
    SimpleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        //MAP
        HashMap<String, String> map=new HashMap<String, String>();
        //FILL
        for(int i=0;i<names.length;i++)
        {
            map=new HashMap<String, String>();
            map.put("Player", names[i]);
            map.put("Image", Integer.toString(pokemon[i]));
            data.add(map);
        }
        //KEYS IN MAP
        String[] from={"Player","Image"};
        //IDS OF VIEWS
        int[] to={R.id.nameTxt,R.id.imageView1};
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
                Toast.makeText(getActivity(), data.get(pos).get("Player"), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.PKMN, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }*/

    @Override
    public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
        Toast.makeText(getActivity(), data.get(pos).get("Player"), Toast.LENGTH_SHORT).show();
    }
}