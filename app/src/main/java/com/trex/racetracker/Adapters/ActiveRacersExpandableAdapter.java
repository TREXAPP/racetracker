package com.trex.racetracker.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trex.racetracker.Activities.MainActivity;
import com.trex.racetracker.Models.ActiveRacerObj;
import com.trex.racetracker.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Igor_2 on 15.11.2016.
 * more info about the events here: http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
 */

public class ActiveRacersExpandableAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<ActiveRacerObj>> _listDataChild;

    public ActiveRacersExpandableAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<ActiveRacerObj>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public ActiveRacerObj getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_parent_racers, parent,false);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.tvRacersHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        ExpandableListView eLV = (ExpandableListView) parent;
        SharedPreferences globals = _context.getSharedPreferences(MainActivity.GLOBALS,0);
        if (globals.contains("elvRacersState" + groupPosition)) {
            if (globals.getString("elvRacersState" + groupPosition,"1").equals("1")) {
                eLV.expandGroup(groupPosition);
            } else {
                eLV.collapseGroup(groupPosition);
            }
        } else {
            eLV.collapseGroup(groupPosition);
            SharedPreferences.Editor editor = globals.edit();
            editor.putString("elvRacersState" + groupPosition,"0");
            editor.commit();
        }




        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ActiveRacerObj activeRacerObj = (ActiveRacerObj) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_racers,parent,false);
        }

        TextView tvBIB = (TextView) convertView.findViewById(R.id.tvBIB);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvLastName = (TextView) convertView.findViewById(R.id.tvLastName);
        TextView tvCountry = (TextView) convertView.findViewById(R.id.tvCountry);
        TextView tvAge = (TextView) convertView.findViewById(R.id.tvAge);
        TextView tvGender = (TextView) convertView.findViewById(R.id.tvGender);
        TextView tvCPNo = (TextView) convertView.findViewById(R.id.tvCPNo);
        LinearLayout llName = (LinearLayout) convertView.findViewById(R.id.llName);

        if (activeRacerObj != null) {
            tvBIB.setText(activeRacerObj.getBIB());
            tvName.setText(activeRacerObj.getFirstName());
            tvLastName.setText(activeRacerObj.getLastName());
            tvCountry.setText(activeRacerObj.getCountry());
            if (activeRacerObj.getAge() == 0) {
                tvAge.setText("");
            } else {
                tvAge.setText(String.valueOf(activeRacerObj.getAge()));
            }
            tvGender.setText(activeRacerObj.getGender());
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
