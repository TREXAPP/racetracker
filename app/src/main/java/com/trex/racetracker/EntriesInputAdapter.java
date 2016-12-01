package com.trex.racetracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import static com.trex.racetracker.StaticMethods.InitializeInputFragment;
import static com.trex.racetracker.StaticMethods.PopulateInputEntriesListView;

/**
 * Created by Igor on 29.11.2016.
 */

class EntriesInputAdapter extends ArrayAdapter<EntryObj> {

    public EntriesInputAdapter(Context context, EntryObj[] entryObj) {
        super(context,R.layout.row_entries_in_input, entryObj);
    }

    @NonNull
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.row_entries_in_input,parent,false);
        SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);

        EntryObj entryObj = getItem(position);
        TextView tvBIB = (TextView) customView.findViewById(R.id.tvBIB);
        TextView tvName = (TextView) customView.findViewById(R.id.tvName);
        TextView tvLastName = (TextView) customView.findViewById(R.id.tvLastName);
        TextView tvTime = (TextView) customView.findViewById(R.id.tvTime);
        TextView tvCountry = (TextView) customView.findViewById(R.id.tvCountry);
        TextView tvAge = (TextView) customView.findViewById(R.id.tvAge);
        TextView tvGender = (TextView) customView.findViewById(R.id.tvGender);
        LinearLayout layoutWrapper = (LinearLayout) customView.findViewById(R.id.layoutWrapper);
        LinearLayout layoutRacerInfo = (LinearLayout) customView.findViewById(R.id.layoutRacerInfo);
        LinearLayout layoutName = (LinearLayout) customView.findViewById(R.id.layoutName);
        LinearLayout layoutTime = (LinearLayout) customView.findViewById(R.id.layoutTime);
        ImageButton ibtEdit = (ImageButton) customView.findViewById(R.id.ibtEdit);
        ImageButton ibtDelete = (ImageButton) customView.findViewById(R.id.ibtDelete);


        if (entryObj != null) {
            tvBIB.setText(entryObj.getBIB());
            tvName.setText(entryObj.getFirstName());
            tvLastName.setText(entryObj.getLastName());
            tvTime.setText(entryObj.getTime()); //TODO: Format if needed!
            tvCountry.setText(entryObj.getCountry());
            tvAge.setText(entryObj.getAge());
            tvGender.setText(entryObj.getGender());
            tvBIB.setText(entryObj.getBIB());

            if (globals.getBoolean("islogin",false)) {
                if (entryObj.getCountry().equals("") && entryObj.getAge().equals("") && entryObj.getGender().equals("")) {
                    layoutWrapper.setBackgroundColor(Color.parseColor("#FFCC0000")); //red
                    if (entryObj.getFirstName().equals("") && entryObj.getLastName().equals("")) {
                        layoutRacerInfo.setBackgroundColor(Color.parseColor("#FFCC0000")); //red
                        tvBIB.setTextColor(Color.parseColor("#FFFFFFFF")); //white
                        tvTime.setTextColor(Color.parseColor("#FFFFFFFF")); //white
                       // layoutName.setVisibility(View.GONE);
                      //  tvTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

                    }
                } else {
                    //ok, use defaults
                }
            } else {
                layoutWrapper.setBackgroundColor(Color.parseColor("#FFBBD0BF")); //light gray
                layoutRacerInfo.setBackgroundColor(Color.parseColor("#FFBBD0BF")); //light gray

                layoutName.setVisibility(View.GONE);
                tvTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            }
        }

        //onclick events for the 2 buttons
        ibtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layoutParent = (LinearLayout) v.getParent().getParent();
                TextView tvBIB = (TextView) layoutParent.findViewById(R.id.tvBIB);
                View parentView = (View) layoutParent.getParent();
                ListView lvInputEntries = (ListView) parentView.findViewById(R.id.lvInputEntries);
                DatabaseHelper dbHelper = DatabaseHelper.getInstance(getContext());
                dbHelper.setEntryDeleted("BIB='" + tvBIB.getText() + "'");
                PopulateInputEntriesListView(getContext(),lvInputEntries);
            }
        });

        ibtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:
                //open a custom popup with textview for BIB and Time(optional)
                //on click OK, find and update the entry with the new racer (save the old bib and update with the new BIB, and all the other info - use the existing methods, modify if nessessery)
            }
        });

        return customView;

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

}
