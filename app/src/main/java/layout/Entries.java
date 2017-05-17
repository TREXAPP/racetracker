package layout;

/**
 * Created by Igor on 22.10.2016.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.trex.racetracker.MainActivity;
import com.trex.racetracker.R;
import com.trex.racetracker.StaticMethods;

import static com.trex.racetracker.StaticMethods.*;

/**
 * A placeholder fragment containing a simple view.
 */
public class Entries extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView lvEntries;
    private SearchView svEntries;
    private Spinner spEntries;

    public Entries() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Entries newInstance(int sectionNumber) {
        Entries fragment = new Entries();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_entries, container, false);
      //  TextView textView = (TextView) rootView.findViewById(R.id.section_label);
    //    textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
       // textView.setText("Vamu ke gi listame site vneseni do sega, so moznost za edit ili delete");

        svEntries = (SearchView) rootView.findViewById(R.id.svEntries);
        spEntries = (Spinner) rootView.findViewById(R.id.spEntries);
        lvEntries = (ListView) rootView.findViewById(R.id.lvEntries);



        /*

        Fill the spinner with:

        Default: Filter: All
        My Entries
        Synchronized
        Not Synchronized
        Race: <race1>
        Race: <race2>
        ...
        CP: <Treskavec>
        CP: <Prisad>
        ...

        */
        SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);

        String selectionFilter = globals.getString("EntriesSelectionFilter","1");
        String selectionSearch = globals.getString("EntriesSelectionSearch","1");
        Boolean leftJoin = globals.getBoolean("EntriesSelectionLeftJoin", true);
        String selection = selectionFilter + " AND " + selectionSearch;

        //TODO - remove debug
        selection = "1";

        InitializeEntriesFragment(getContext(), getActivity(), rootView, selection, leftJoin);

        spEntries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);

                String selectionFilter = "Valid=1";
                String selectionSearch = globals.getString("EntriesSelectionSearch","1");
                SharedPreferences.Editor editor = globals.edit();
                String selectedItem = spEntries.getSelectedItem().toString();
                Boolean leftJoin = true;
                Boolean filterFound = false;
                if (selectedItem.startsWith("Race")) {
                    leftJoin = false;
                    String[] separated = selectedItem.split("\\|");
                    if (separated.length > 1) {
                        selectionFilter = "LoginInfo.RaceName = '" + separated[1].trim() + "'";
                    } else {
                        selectionFilter = "Valid=1";
                    }
                    filterFound = true;
                }

                if (selectedItem.startsWith("CP")) {
                    String[] separated = selectedItem.split("\\|");
                    if (separated.length > 1) {
                        selectionFilter = "CPEntries.CPNo = " + separated[1].trim();
                    } else {
                        selectionFilter = "Valid=1";
                    }
                    filterFound = true;
                }

                if (!filterFound) {
//TODO - site po red opcii

                    switch(selectedItem) {
                        case "All entries":
                            selectionFilter = "Valid=1";
                            break;
                        case "Deleted":
                            selectionFilter = "Valid=0 AND ReasonInvalid LIKE 'Code 03%'";
                            break;
                        case "Synchronized":
                            selectionFilter = "Valid=1 AND Synced=1";
                            break;
                        case "Not Synchronized":
                            selectionFilter = "Valid=1 AND Synced=0";
                            break;
                    }
                }

                editor.putString("EntriesSelectionFilter",selectionFilter);
                editor.putBoolean("EntriesSelectionLeftJoin",leftJoin);
                editor.commit();
                String selection = selectionFilter + " AND " + selectionSearch;
                InitializeEntriesListView(getContext(),lvEntries,selection, leftJoin, getActivity());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return rootView;

    }


}
