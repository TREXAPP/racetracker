package com.trex.racetracker.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.trex.racetracker.Activities.MainActivity;
import com.trex.racetracker.Models.EntryObj;
import com.trex.racetracker.R;

import layout.DeleteEntryDialog;
import layout.EditEntryDialog;

import static android.app.PendingIntent.getActivity;

/**
 * Created by Igor on 29.11.2016.
 */

public class EntriesListAdapter extends ArrayAdapter<EntryObj> {
    public static final int DELETE = 10;
    public static final int RESTORE = 20;


    Activity activity;
    private ListView lvEntries;
    private int deleteBtnMode;


    public EntriesListAdapter(Context context, EntryObj[] entryObj, Activity act, ListView lvEntries) {
        super(context, R.layout.row_entries, entryObj);
        this.activity = act;
        this.lvEntries = lvEntries;
    }

    @NonNull
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.row_entries,parent,false);
        final SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);
        deleteBtnMode = globals.getInt("EntriesDeleteBtnMode",DELETE);
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
//        ImageButton ibtEdit = (ImageButton) customView.findViewById(R.id.ibtEdit);
        ImageButton ibtDelete = (ImageButton) customView.findViewById(R.id.ibtDelete);
        LinearLayout ibtEditWrapper = (LinearLayout) customView.findViewById(R.id.ibtEditWrapper);
        LinearLayout ibtDeleteWrapper = (LinearLayout) customView.findViewById(R.id.ibtDeleteWrapper);
        LinearLayout myEntryIndicator = (LinearLayout) customView.findViewById(R.id.myEntryIndicator);


        if (deleteBtnMode == RESTORE) {
            ibtDelete.setImageResource(android.R.drawable.ic_menu_revert);
        }

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
                //ivSync.setImageResource(R.drawable.error);
                //ivSync.setVisibility(View.VISIBLE);
            } else {
                ivSync.setImageResource(R.drawable.success);
                //ivSync.setVisibility(View.INVISIBLE);
            }

            if (entryObj.isMyEntry()) {
                myEntryIndicator.setBackgroundColor(Color.parseColor("#FF00B7FF")); //blue
            } else {
                myEntryIndicator.setBackgroundColor(Color.parseColor("#FFFFBA00")); //orange
                ibtDelete.setEnabled(false);
//                ibtEdit.setEnabled(false);
                ibtEditWrapper.setBackgroundColor(Color.parseColor("#FFBBD0BF")); //light gray
                ibtDeleteWrapper.setBackgroundColor(Color.parseColor("#FFBBD0BF")); //light gray
            }

            if (globals.getBoolean("loggedIn",false)) {
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
                setEntryDeleted(getContext(),"BIB='" + tvBIB.getText() + "'", true, deleteBtnMode);
                String selection = Entries.getSelectionString(globals);
                Boolean leftJoin = globals.getBoolean("EntriesSelectionLeftJoin",true);
                InitializeEntriesListView(getContext(),lvEntries, selection, leftJoin, activity);
               // PopulateInputEntriesListView(getContext(),lvInputEntries, activity);
                if (deleteBtnMode == RESTORE) {
                    Toast.makeText(getContext(), "Entry restored!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Entry deleted!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        */
        ibtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteEntryDialog dialog = new DeleteEntryDialog(getContext(),activity, entryObj,lvEntries,EditEntryDialog.ENTRIES, deleteBtnMode);
                dialog.show(activity.getFragmentManager(),"deleteEntry");
            }
        });
//
//        ibtEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //TODO:
//                //open a custom popup with textview for BIB and Time(optional)
//                //on click OK, find and update the entry with the new racer (save the old bib and update with the new BIB, and all the other info - use the existing methods, modify if nessessery)
//
//                // Input.InputEditDismissHandler handler = new Input.InputEditDismissHandler();
//                final SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);
//                String BIB = tvBIB.getText().toString();
//                String hours =  tvTime.getText().toString().substring(0,2);
//                String minutes =  tvTime.getText().toString().substring(3,5);
//                String seconds =  tvTime.getText().toString().substring(6,8);
//
//                //InputEditDismissHandler handler = new InputEditDismissHandler();
//                // final SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);
//
//                EditEntryDialog dialog = new EditEntryDialog(getContext(),BIB,hours,minutes,seconds,entryObj,lvEntries, EditEntryDialog.ENTRIES);
//                // dialog.setTargetFragment(parentFragment, 300);
//                // dialog.setTargetFragment();
//                dialog.show(activity.getFragmentManager(),"editEntry");
//                //   dialog.show(getFragmentManager(),"editEntry");
//
//
//            }
//        });

        return customView;

    }



}
