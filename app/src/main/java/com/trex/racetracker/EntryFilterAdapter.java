package com.trex.racetracker;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Igor on 15.5.2017.
 */

class EntryFilterAdapter extends ArrayAdapter {
    Activity activity;
    private Spinner spinner;

    public EntryFilterAdapter(Context context, int textViewResourceId, EntryFilterObj[] entryFilterObj, ArrayList<String> list, Activity act, Spinner spinner) {
        super(context, textViewResourceId, list);
        this.activity = act;
        this.spinner = spinner;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        return super.getView(position, convertView, parent);

    }
}
