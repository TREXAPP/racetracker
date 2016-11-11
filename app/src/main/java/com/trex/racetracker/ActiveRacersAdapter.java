package com.trex.racetracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

/**
 * Created by Igor_2 on 10.11.2016.
 */

class ActiveRacersAdapter extends ArrayAdapter<ActiveRacer> {

    public ActiveRacersAdapter(Context context, ActiveRacer[] activeRacer) {
        super(context,R.layout.row_racers, activeRacer);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.row_racers,parent,false);

        ActiveRacer activeRacer = getItem(position);
        TextView tvBIB = (TextView) customView.findViewById(R.id.tvBIB);
        TextView tvName = (TextView) customView.findViewById(R.id.tvName);
        TextView tvLastName = (TextView) customView.findViewById(R.id.tvLastName);
        TextView tvCountry = (TextView) customView.findViewById(R.id.tvCountry);
        TextView tvAge = (TextView) customView.findViewById(R.id.tvAge);
        TextView tvGender = (TextView) customView.findViewById(R.id.tvGender);
        TextView tvTimeLast = (TextView) customView.findViewById(R.id.tvTimeLast);
        TextView tvCPLast = (TextView) customView.findViewById(R.id.tvCPLast);

        if (activeRacer != null) {
            tvBIB.setText(activeRacer.getBIB());
            tvName.setText(activeRacer.getFirstName());
            tvLastName.setText(activeRacer.getLastName());
            tvCountry.setText(activeRacer.getCountry());
            tvAge.setText(activeRacer.getAge());
            tvGender.setText(activeRacer.getGender());
            tvTimeLast.setText(activeRacer.getTimeLast());
            tvCPLast.setText(activeRacer.getCPLast());
        }
        return customView;

    }
}
