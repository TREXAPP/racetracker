package com.trex.racetracker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import layout.DeleteEntryDialog;
import layout.EditEntryDialog;

import static android.R.drawable.ic_menu_close_clear_cancel;
import static android.app.PendingIntent.getActivity;
import static com.trex.racetracker.DbMethods.*;
import static com.trex.racetracker.StaticMethods.*;

/**
 * Created by Igor on 29.11.2016.
 */

public class EntriesInputListAdapter extends ArrayAdapter<EntryObj> {
    public static final int DELETE = 10;
    public static final int RESTORE = 20;


    Activity activity;
    private ListView lvInputEntries;
    private int deleteBtnMode = EntriesListAdapter.DELETE;


    public EntriesInputListAdapter(Context context, EntryObj[] entryObj, Activity act, ListView lvInputEntries) {
        super(context,R.layout.row_entries_in_input, entryObj);
        this.activity = act;
        this.lvInputEntries = lvInputEntries;
    }

    @NonNull
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.row_entries_in_input,parent,false);
        SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);

        final EntryObj entryObj = getItem(position);
        final TextView tvBIB = (TextView) customView.findViewById(R.id.tvBIB);
        TextView tvName = (TextView) customView.findViewById(R.id.tvName);
        TextView tvLastName = (TextView) customView.findViewById(R.id.tvLastName);
        final TextView tvTime = (TextView) customView.findViewById(R.id.tvTime);
        TextView tvCountry = (TextView) customView.findViewById(R.id.tvCountry);
        TextView tvAge = (TextView) customView.findViewById(R.id.tvAge);
        TextView tvGender = (TextView) customView.findViewById(R.id.tvGender);
        ImageView ivSync = (ImageView) customView.findViewById(R.id.ivSync);
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
            tvTime.setText(entryObj.getTime().substring(11,19)); //TODO: Format if needed!
          //  tvTime.setText(entryObj.getTime()); //TODO: Format if needed!
            tvCountry.setText(entryObj.getCountry());
            if (entryObj.getAge() == 0) {
                tvAge.setText("");
            } else {
                tvAge.setText(String.valueOf(entryObj.getAge()));
            }
            tvGender.setText(entryObj.getGender());
            tvBIB.setText(entryObj.getBIB());

            if (!entryObj.isSynced()) {
                ivSync.setImageResource(R.drawable.error);
                //ivSync.setVisibility(View.VISIBLE);
            } else {
                ivSync.setImageResource(R.drawable.success);
                //ivSync.setVisibility(View.INVISIBLE);
            }

            if (globals.getBoolean("islogin",false)) {
                if (entryObj.getCountry().equals("") && (entryObj.getAge() == 0) && entryObj.getGender().equals("")) {
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
        /*
        //onclick events for the 2 buttons
        ibtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layoutParent = (LinearLayout) v.getParent().getParent();
                TextView tvBIB = (TextView) layoutParent.findViewById(R.id.tvBIB);
                //View parentView = (View) layoutParent.getParent();
                //ListView lvInputEntries = (ListView) parentView.findViewById(R.id.lvInputEntries);
                //DatabaseHelper dbHelper = DatabaseHelper.getInstance(getContext());
                setEntryDeleted(getContext(),"BIB='" + tvBIB.getText() + "'", true, DELETE);
                PopulateInputEntriesListView(getContext(),lvInputEntries, activity);
                Toast.makeText(getContext(), "Entry deleted!", Toast.LENGTH_SHORT).show();

            }
        });
        */
        ibtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteEntryDialog dialog = new DeleteEntryDialog(getContext(),activity, entryObj,lvInputEntries,EditEntryDialog.INPUT, deleteBtnMode);
                dialog.show(activity.getFragmentManager(),"deleteEntry");
            }
        });



        ibtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO:
                //open a custom popup with textview for BIB and Time(optional)
                //on click OK, find and update the entry with the new racer (save the old bib and update with the new BIB, and all the other info - use the existing methods, modify if nessessery)

               // Input.InputEditDismissHandler handler = new Input.InputEditDismissHandler();
                final SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);
                String BIB = tvBIB.getText().toString();
                String hours =  tvTime.getText().toString().substring(0,2);
                String minutes =  tvTime.getText().toString().substring(3,5);
                String seconds =  tvTime.getText().toString().substring(6,8);

                //InputEditDismissHandler handler = new InputEditDismissHandler();
               // final SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);

                EditEntryDialog dialog = new EditEntryDialog(getContext(),BIB,hours,minutes,seconds,entryObj,lvInputEntries, EditEntryDialog.INPUT);
               // dialog.setTargetFragment(parentFragment, 300);
               // dialog.setTargetFragment();
                dialog.show(activity.getFragmentManager(),"editEntry");
             //   dialog.show(getFragmentManager(),"editEntry");


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
