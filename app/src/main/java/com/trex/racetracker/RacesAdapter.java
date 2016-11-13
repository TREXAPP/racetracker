package com.trex.racetracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by Igor_2 on 13.11.2016.
 */

class RacesAdapter extends ArrayAdapter<RaceObj> {

    public RacesAdapter(Context context, RaceObj[] raceObj) {
        super(context,R.layout.row_racers, raceObj);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.row_races_login,parent,false);

        RaceObj raceObj = getItem(position);
        TextView tvRaceLong = (TextView) customView.findViewById(R.id.tvRaceLong);
        CheckBox cbShowRace = (CheckBox) customView.findViewById(R.id.cbShowRace);


        if (raceObj != null) {
           tvRaceLong.setText(raceObj.getRaceDescription());
           cbShowRace.setEnabled(raceObj.getShowRacers());
        }
        return customView;

    }
}
