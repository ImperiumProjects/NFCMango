package com.imperium.power.nfcmango;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MobileArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public MobileArrayAdapter(Context context, String[] values) {
        super(context, R.layout.activity_caught_list, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.activity_caught_list, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        textView.setText(values[position]);

        // Change icon based on name
        String s = values[position];

        System.out.println(s);

        if (s.equals("Pikachu")) {
            imageView.setImageResource(R.drawable.pika);
        } else if (s.equals("Vulpix")) {
            imageView.setImageResource(R.drawable.vulp);
        } else if (s.equals("Bulbasaur")) {
            imageView.setImageResource(R.drawable.bulb);
        } else if (s.equals("Seadra")) {
            imageView.setImageResource(R.drawable.hors);
        } else if (s.equals("Dragonite")) {
            imageView.setImageResource(R.drawable.drag);
        } else {
            imageView.setImageResource(R.drawable.odd);
        }

        return rowView;
    }
}