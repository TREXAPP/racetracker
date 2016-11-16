package com.trex.racetracker;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

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
        TextView tvTimeLast = (TextView) convertView.findViewById(R.id.tvTimeLast);
        TextView tvCPNo = (TextView) convertView.findViewById(R.id.tvCPNo);
        TextView tvCPName = (TextView) convertView.findViewById(R.id.tvCPName);

        if (activeRacerObj != null) {
            tvBIB.setText(activeRacerObj.getBIB());
            tvName.setText(activeRacerObj.getFirstName());
            tvLastName.setText(activeRacerObj.getLastName());
            tvCountry.setText(activeRacerObj.getCountry());
            tvAge.setText(activeRacerObj.getAge());
            tvGender.setText(activeRacerObj.getGender());
            tvTimeLast.setText(activeRacerObj.getTimeLast());
            tvCPNo.setText(activeRacerObj.getCPNo());
            tvCPName.setText(activeRacerObj.getCPName());
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
