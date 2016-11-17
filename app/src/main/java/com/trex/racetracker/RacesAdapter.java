package com.trex.racetracker;

import android.content.Context;
import android.content.SharedPreferences;
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

        final RaceObj raceObj = getItem(position);
        TextView tvRaceLong = (TextView) customView.findViewById(R.id.tvRaceLong);
        TextView tvCPNo = (TextView) customView.findViewById(R.id.tvCPNo);


        if (raceObj != null) {
           tvRaceLong.setText(raceObj.getRaceDescription());
            tvCPNo.setText(raceObj.getCPNo());
        }

        /*
        cbShowRace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);
                 SharedPreferences.Editor editor = globals.edit();
                if (cbShowRace.isChecked()) {
                    editor.putString("showRacers" + raceObj.getRaceID(),"1");
                    //put true in globals
                } else {
                    //put false in globals
                    editor.putString("showRacers" + raceObj.getRaceID(),"0");
                }
                editor.commit();
            }
        });
        */
        return customView;

    }
}
