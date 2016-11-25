package layout;

/**
 * Created by Igor on 22.10.2016.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
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
import android.widget.Toast;

import static com.trex.racetracker.StaticMethods.*;


import com.trex.racetracker.DatabaseHelper;
import com.trex.racetracker.EntryObj;
import com.trex.racetracker.MainActivity;
import com.trex.racetracker.R;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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
        final String inputedBIB = BIBEntryString;
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
                    DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                    //TODO:
                    //put entry in SQLite
                    //update listview(s)
                    //refresh fragments if neccessery
                    
                    //for direct entry EntryTypeID=1, Barcode = null
                    EntryObj entryObj = PrepareEntryObj(inputedBIB,1,null,dbHelper);

                    if (!(dbHelper.insertIntoCPEntries(entryObj))) {
                        Toast.makeText(getContext(), "Error writing Entry into local database! Contact the administrator.", Toast.LENGTH_SHORT).show();
                    };

                    tvBIBEntry.setBackgroundColor(Color.parseColor("#FF7BFDB1"));
                    tvBIBEntry.setTextColor(Color.parseColor("#FF000000"));
                    tvBIBEntry.setText("");
                    //TODO: reenable keyboard (if disabled)
                }
            }.start();



/*
            //TODO - for testing removed out of timer
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            EntryObj entryObj = PrepareEntryObj(inputedBIB,1,null,dbHelper);
            if (!(dbHelper.insertIntoCPEntries(entryObj))) {
                Toast.makeText(getContext(), "Error writing Entry into local database! Contact the administrator.", Toast.LENGTH_SHORT).show();
            };
            tvBIBEntry.setBackgroundColor(Color.parseColor("#FF7BFDB1"));
            tvBIBEntry.setTextColor(Color.parseColor("#FF000000"));
            tvBIBEntry.setText("");

*/
        }

        editor.putString("EntryNoState",BIBEntryString);
        editor.apply();
    }

    private EntryObj PrepareEntryObj(String inputedBIB, Integer EntryTypeID, String Barcode, DatabaseHelper dbHelper) {

        SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);

        EntryObj entryObj = new EntryObj();
        entryObj.setEntryID(null);
        entryObj.setBIB(inputedBIB);
        Cursor cursorRacers = dbHelper.getActiveRacerIDFromRacers(inputedBIB);

        if (cursorRacers.getCount() == 1) {
            cursorRacers.moveToFirst();
            // ActiveRacerID, FirstName, LastName, Country, Gender, Age
            entryObj.setActiveRacerID(Integer.parseInt(cursorRacers.getString(0)));
            entryObj.setFirstName(cursorRacers.getString(1));
            entryObj.setLastName(cursorRacers.getString(2));
            entryObj.setCountry(cursorRacers.getString(3));
            entryObj.setGender(cursorRacers.getString(4));
            entryObj.setAge(cursorRacers.getString(5));
            entryObj.setRaceID(Integer.parseInt(cursorRacers.getString(6)));

        } else {
            entryObj.setActiveRacerID(null);
            entryObj.setFirstName(null);
            entryObj.setLastName(null);
            entryObj.setCountry(null);
            entryObj.setGender(null);
            entryObj.setAge(null);
            entryObj.setRaceID(null);
        }

        cursorRacers.close();

        entryObj.setEntryTypeID(EntryTypeID);
        entryObj.setBarcode(Barcode);
        entryObj.setComment("");
        entryObj.setSynced(false);
        entryObj.setMyEntry(true);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date timeNow = calendar.getTime();
        String formattedDate = df.format(timeNow);
        entryObj.setTime(formattedDate); //example format: 2016-11-14 07:13:28

        //check if there is a previous entry close to this, whether to flag this entry valid true or false
        int timeBetweenEntries = globals.getInt("timebetweenentries",1);
        Date lastEntryDate = dbHelper.getDateForLastEntry();
        if (lastEntryDate != null) {
            if (addMinutesToDate(timeBetweenEntries,lastEntryDate).before(timeNow)) {
                entryObj.setValid(true);
            } else {
                entryObj.setValid(false);
            }
        } else {
            entryObj.setValid(true);
        }



        if (globals.getBoolean("islogin",false)) {
            entryObj.setUserID(globals.getString("username",""));
            entryObj.setOperator(globals.getString("operator",""));
            Cursor cursorCP = dbHelper.getEntryDataFromLoginInfo("1");
            //set CPNo:
            // 1. check from login info how many rows are for there for this RaceID
            //2. If this CPNo exists check the next ... and so on... Raise alarm if all are used
//TODO


            if (cursorCP.getCount() > 0) {
                cursorCP.moveToFirst();
                entryObj.setCPID(Integer.parseInt(cursorCP.getString(0)));
                entryObj.setCPName(cursorCP.getString(1));
            } else {
                entryObj.setCPID(null);
                entryObj.setCPName(null);
            }
            cursorCP.close();

        } else {
            entryObj.setCPID(null);
            entryObj.setCPName(null);
            entryObj.setUserID(null);
            entryObj.setOperator(null);
            entryObj.setCPNo(null);
        }
        
        return entryObj;
    }


}
