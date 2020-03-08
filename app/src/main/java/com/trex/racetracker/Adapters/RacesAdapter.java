package com.trex.racetracker.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.trex.racetracker.Models.RaceObj;
import com.trex.racetracker.R;

/**
 * Created by Igor_2 on 13.11.2016.
 */

public class RacesAdapter extends ArrayAdapter<RaceObj> {

    public RacesAdapter(Context context, RaceObj[] raceObj) {
        super(context, R.layout.row_racers, raceObj);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.row_races_login,parent,false);

        final RaceObj raceObj = getItem(position);
        TextView tvRaceLong = (TextView) customView.findViewById(R.id.tvRaceLong);
        TextView tvCPNo = (TextView) customView.findViewById(R.id.tvCPNo);

        if (raceObj != null) {
           tvRaceLong.setText(raceObj.getRaceDescription());
            tvCPNo.setText(raceObj.getCPNo());
        }

        return customView;

    }
}
