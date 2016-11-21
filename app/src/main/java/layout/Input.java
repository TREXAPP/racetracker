package layout;

/**
 * Created by Igor on 22.10.2016.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.TextClock;
import android.widget.TextView;
import static com.trex.racetracker.StaticMethods.*;


import com.trex.racetracker.DatabaseHelper;
import com.trex.racetracker.EntryObj;
import com.trex.racetracker.MainActivity;
import com.trex.racetracker.R;

import org.w3c.dom.Text;

/**
 * A placeholder fragment containing a simple view.
 */
public class Input extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    public TextView tvBIBEntry;

    public Input() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Input newInstance(int sectionNumber) {
        Input fragment = new Input();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_input, container, false);
        Button btn0 = (Button) rootView.findViewById(R.id.btn0);
        Button btn1 = (Button) rootView.findViewById(R.id.btn1);
        Button btn2 = (Button) rootView.findViewById(R.id.btn2);
        Button btn3 = (Button) rootView.findViewById(R.id.btn3);
        Button btn4 = (Button) rootView.findViewById(R.id.btn4);
        Button btn5 = (Button) rootView.findViewById(R.id.btn5);
        Button btn6 = (Button) rootView.findViewById(R.id.btn6);
        Button btn7 = (Button) rootView.findViewById(R.id.btn7);
        Button btn8 = (Button) rootView.findViewById(R.id.btn8);
        Button btn9 = (Button) rootView.findViewById(R.id.btn9);
        Button btnE = (Button) rootView.findViewById(R.id.btnE);

        InitializeInputFragment(getContext(),rootView);
        tvBIBEntry = (TextView)rootView.findViewById(R.id.tvBIBEntry);

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed("0",view);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed("1",view);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed("2",view);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed("3",view);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed("4",view);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed("5",view);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed("6",view);
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed("7",view);
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed("8",view);
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed("9",view);
            }
        });
        btnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed("E",view);
            }
        });

        return rootView;
    }

    public void DigitPressed(String digit, View view) {
        SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);
        SharedPreferences.Editor editor = globals.edit();
        boolean newEntry = false;
        if (!globals.contains("EntryNoState")) {
            editor.putString("EntryNoState","");
        }

           String BIBEntryString = globals.getString("EntryNoState","");
        if (digit.equals("E")) {
            //erase button pressed
            if (!BIBEntryString.equals("")) {
                BIBEntryString = BIBEntryString.substring(0,BIBEntryString.length() - 1);
            }
        } else {
            //digit button pressed
            BIBEntryString += digit;
            if (!globals.contains("inputdigitsno")) {
                SharedPreferences.Editor editor1 = globals.edit();
                editor1.putInt("inputdigitsno",3);
                editor1.commit();
            }
            if (BIBEntryString.length() >= globals.getInt("inputdigitsno",3)) {
                newEntry = true;
            }
        }

        tvBIBEntry.setText(BIBEntryString);
        if (newEntry) { //FF7BFDB1 lightgreen     FFFF0004 red
            tvBIBEntry.setBackgroundColor(Color.parseColor("#FFFF0004"));
            tvBIBEntry.setTextColor(Color.parseColor("#FF7BFDB1"));
            //TODO: disable keyboard

            BIBEntryString = "";
            Integer timer = globals.getInt("entryconfirmtimer",100);
            new CountDownTimer(timer, timer) {

                public void onTick(long millisUntilFinished) {
                   //nothing to do here
                }

                public void onFinish() {
                    //TODO:
                    //put entry in SQLite
                    //update listview(s)
                    //refresh fragments if neccessery

                    EntryObj entryObj = PrepareEntryObj();


                    tvBIBEntry.setBackgroundColor(Color.parseColor("#FF7BFDB1"));
                    tvBIBEntry.setTextColor(Color.parseColor("#FF000000"));
                    tvBIBEntry.setText("");
                    //TODO: reenable keyboard (if disabled)
                }
            }.start();
        }

        editor.putString("EntryNoState",BIBEntryString);
        editor.apply();
    }

    private EntryObj PrepareEntryObj() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);
        EntryObj entryObj = new EntryObj();
        if (globals.getString("islogin","0").equals("1")) {

            //TODO - fill in the EntryObj:
            /*
            EntryID		- null za moe entry, od baza za synched
            CPID		- od loginInfo baza row(0)
            CPName		- od loginInfo baza row(0)
            UserID		- globals.("username")
            ActiveRacerID	- sqlite query Racers by BIB
            BIBCODE		- za entrytype=bib: bibcode; za site drugi null
            Time		- from time.now() function
            EntryTypeID	- za 1,2,3,9 ( direct, indirect, bib, nfc, other...)
            Comment		- default ""
                Synced		- false. koga ke se prati servisot ke go napravi true i ke vrati EntryID
                myEntry		- true
                ___________
                operator!!!	- add!	globals
*/

                //  entryObj.setCPID(globals.getString("cpid"));

        } else {
            entryObj.setCPID(null);
        }
        return entryObj;
    }


}
