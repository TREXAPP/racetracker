package com.trex.racetracker;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Igor_2 on 10.11.2016.
 */

class ActiveRacersAdapter extends ArrayAdapter<ActiveRacerObj> {

    public ActiveRacersAdapter(Context context, ActiveRacerObj[] activeRacerObj) {
        super(context,R.layout.row_racers, activeRacerObj);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.row_racers,parent,false);

        ActiveRacerObj activeRacerObj = getItem(position);
        TextView tvBIB = (TextView) customView.findViewById(R.id.tvBIB);
        TextView tvName = (TextView) customView.findViewById(R.id.tvName);
        TextView tvLastName = (TextView) customView.findViewById(R.id.tvLastName);
        TextView tvCountry = (TextView) customView.findViewById(R.id.tvCountry);
        TextView tvAge = (TextView) customView.findViewById(R.id.tvAge);
        TextView tvGender = (TextView) customView.findViewById(R.id.tvGender);
        TextView tvCPNo = (TextView) customView.findViewById(R.id.tvCPNo);
        LinearLayout llName = (LinearLayout) customView.findViewById(R.id.llName);

        if (activeRacerObj != null) {
            tvBIB.setText(activeRacerObj.getBIB());
            tvName.setText(activeRacerObj.getFirstName());
            tvLastName.setText(activeRacerObj.getLastName());
            tvCountry.setText(activeRacerObj.getCountry());
            if (activeRacerObj.getAge() == 0) {
                tvAge.setText("");
            } else {
                tvAge.setText(activeRacerObj.getAge());
            }

            tvGender.setText(activeRacerObj.getGender());
            tvCPNo.setText(activeRacerObj.getCPNo());
            if (activeRacerObj.getTimeLast().equals("") || activeRacerObj.getTimeLast() != null) {
                tvBIB.setBackgroundColor(Color.parseColor("#007ACC"));
                llName.setBackgroundColor(Color.parseColor("#007ACC"));
            }
        }
        return customView;

    }
}
