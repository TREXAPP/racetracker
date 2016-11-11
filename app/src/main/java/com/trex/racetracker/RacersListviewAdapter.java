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

class RacersListviewAdapter extends ArrayAdapter<Racer> {

    public RacersListviewAdapter(Context context, Racer[] racer) {
        super(context,R.layout.row_racers, racer);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.row_racers,parent,false);

        Racer racer = getItem(position);
        TextView tvBIB = (TextView) customView.findViewById(R.id.tvBIB);
        TextView tvName = (TextView) customView.findViewById(R.id.tvName);
        TextView tvLastName = (TextView) customView.findViewById(R.id.tvLastName);
        TextView tvCountry = (TextView) customView.findViewById(R.id.tvCountry);
        TextView tvAge = (TextView) customView.findViewById(R.id.tvAge);
        TextView tvGender = (TextView) customView.findViewById(R.id.tvGender);
        TextView tvTimeLast = (TextView) customView.findViewById(R.id.tvTimeLast);
        TextView tvCPLast = (TextView) customView.findViewById(R.id.tvCPLast);

        if (racer != null) {
            tvBIB.setText(racer.getBIB());
            tvName.setText(racer.getFirstName());
            tvLastName.setText(racer.getLastName());
            tvCountry.setText(racer.getCountry());
            tvAge.setText(racer.getAge());
            tvGender.setText(racer.getGender());
            tvTimeLast.setText(racer.getTimeLast());
            tvCPLast.setText(racer.getCPLast());
        }
        return customView;

    }
}
