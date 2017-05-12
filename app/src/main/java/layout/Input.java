package layout;

/**
 * Created by Igor on 22.10.2016.
 */

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static com.trex.racetracker.StaticMethods.*;
import static com.trex.racetracker.DbMethods.*;


import com.trex.racetracker.DatabaseHelper;
import com.trex.racetracker.EntryObj;
import com.trex.racetracker.MainActivity;
import com.trex.racetracker.R;
import com.trex.racetracker.Provider;

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
    public ViewGroup layoutInput;
    public ListView lvInputEntries;
    private boolean viewInflated;
    private BroadcastReceiver mReceiver;



/*
    private BroadcastReceiver myReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {

            String sAction = intent.getAction();
            if ("com.trex.racetracker.REFRESH_LIST".equals(sAction) )
            {
                PopulateInputEntriesListView(context,lvInputEntries,getActivity());
            }
        }
    };
*/
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

        //debug
        //insert sample
        /*
        ContentValues initialValues = new ContentValues();
        initialValues.put("Column", "value");
        Uri contentUri = Uri.withAppendedPath(Provider.CONTENT_URI, DatabaseHelper.TABLE_1_NAME);
        Uri resultUri = getContext().getContentResolver().insert(contentUri, initialValues);
        */

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

        if (!viewInflated) {
            InitializeInputFragment(getContext(),rootView, getActivity());
        }
        viewInflated = true;

        tvBIBEntry = (TextView)rootView.findViewById(R.id.tvBIBEntry);
        lvInputEntries = (ListView)rootView.findViewById(R.id.lvInputEntries);

        layoutInput = container;

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed(getContext(),"0",view);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed(getContext(),"1",view);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed(getContext(),"2",view);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed(getContext(),"3",view);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed(getContext(),"4",view);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed(getContext(),"5",view);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed(getContext(),"6",view);
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed(getContext(),"7",view);
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed(getContext(),"8",view);
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed(getContext(),"9",view);
            }
        });
        btnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DigitPressed(getContext(),"E",view);
            }
        });

        SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);
        if (globals.getBoolean("islogin",false)) {
            disableEnableControls(true,layoutInput);
        } else {
            disableEnableControls(false,layoutInput);

        }

        if (mReceiver == null) mReceiver = InitializeBroadcastReceiver(getContext(),rootView,getActivity());

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        IntentFilter myFilter = new IntentFilter();
        myFilter.addAction("com.trex.racetracker.REFRESH_LIST");
        if (mReceiver == null) mReceiver = InitializeBroadcastReceiver(getContext(),view,getActivity());
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver,myFilter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
        //  LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(myReceiver);
    }

    private BroadcastReceiver InitializeBroadcastReceiver(final Context ctx, final View view, final Activity activity) {

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String sAction = intent.getAction();
                if ("com.trex.racetracker.REFRESH_LIST".equals(sAction) )
                {
                    InitializeInputFragment(ctx, view, activity);

                    //InitializeInputFragment(context,getView(), getActivity());
                    //PopulateInputEntriesListView(getContext(),lvInputEntries,getActivity());
                }
            }
        };
        return receiver;
    }



    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);
        if (globals.getBoolean("islogin",false)) {
            disableEnableControls(true,layoutInput);
        } else {
            disableEnableControls(false,layoutInput);
        }
    }

    public void DigitPressed(Context context, String digit, View view) {
        SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
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
        if (newEntry) {
            disableEnableControls(false,layoutInput);
            BIBEntryString = "";
            Integer timer = globals.getInt("entryconfirmtimer",100);




            new CountDownTimer(timer, timer) {

                public void onTick(long millisUntilFinished) {
                   //nothing to do here
                }

                public void onFinish() {

                    tvBIBEntry.setBackgroundColor(Color.parseColor("#FF7BFDB1"));
                    tvBIBEntry.setTextColor(Color.parseColor("#FF000000"));
                    tvBIBEntry.setText("");
                    layoutInput.setBackgroundColor(Color.TRANSPARENT); //TRANSPARENT
                    disableEnableControls(true,layoutInput);
                }
            }.start();

            DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
            //for direct entry EntryTypeID=1, Barcode = null
            EntryObj entryObj = PrepareEntryObj(context, inputedBIB,1,null,dbHelper,tvBIBEntry);

            if (!(insertIntoCPEntries(context,entryObj,true))) {
                Toast.makeText(context, "Error writing Entry into local database! Contact the administrator.", Toast.LENGTH_SHORT).show();
            }

            //update listview
         //   ListView lvInputEntries = (ListView) view.findViewById(R.id.lvInputEntries);
            PopulateInputEntriesListView(context,lvInputEntries, getActivity());

            // FF7BFDB1 lightgreen
            // FFFF0004 red
          //  tvBIBEntry.setBackgroundColor(Color.parseColor("#FFFF0004"));
          //  tvBIBEntry.setTextColor(Color.parseColor("#FF7BFDB1"));

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

    private EntryObj PrepareEntryObj(Context context, String inputedBIB, Integer EntryTypeID, String Barcode, DatabaseHelper dbHelper, TextView tvBIBEntry) {

        SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);

        EntryObj entryObj = new EntryObj();
        entryObj.setEntryID(null);
        entryObj.setBIB(inputedBIB);
        Cursor cursorRacers = getActiveRacerIDFromRacers(context, inputedBIB);
        boolean racerFound = false;
        boolean cpnoSet = false;
        String status = "success";
        String raceID = "";

        if (cursorRacers.getCount() == 1) {
            racerFound = true;
            cursorRacers.moveToFirst();
            // ActiveRacerID, FirstName, LastName, Country, Gender, Age
            entryObj.setActiveRacerID(Integer.parseInt(cursorRacers.getString(0)));
            entryObj.setFirstName(cursorRacers.getString(1));
            entryObj.setLastName(cursorRacers.getString(2));
            entryObj.setCountry(cursorRacers.getString(3));
            entryObj.setGender(cursorRacers.getString(4));
            entryObj.setAge(cursorRacers.getString(5));
            raceID = cursorRacers.getString(6);
            entryObj.setRaceID(Integer.parseInt(raceID));

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
        entryObj.setTimeStamp(System.currentTimeMillis());

        //check if there is a previous entry close to this, whether to flag this entry valid true or false
        int timeBetweenEntries = globals.getInt("timebetweenentries",1);
        Date lastEntryDate = getDateForLastEntry(context, inputedBIB);
        if (lastEntryDate != null) {
            if (addMinutesToDate(timeBetweenEntries,lastEntryDate).before(timeNow)) {
                entryObj.setValid(true);
            } else {
                entryObj.setValid(false);
                entryObj.setReasonInvalid("Code 01: There was a valid entry for this runner in the last " + timeBetweenEntries + " minutes");
                Toast.makeText(getContext(), "There was a valid entry for this runner in the last " + timeBetweenEntries + " minutes", Toast.LENGTH_SHORT).show();
                status = "too soon";
            }
        } else {
            entryObj.setValid(true);
        }



        if (globals.getBoolean("islogin",false)) {
            entryObj.setUserID(globals.getString("username",""));
            entryObj.setOperator(globals.getString("operator",""));
            String loginInfoWhere;
            String loginInfoOrder;
            if (racerFound) {
                loginInfoWhere = "RaceID='" + raceID + "'";
                loginInfoOrder = "CPNo DESC";
            } else {
                loginInfoWhere = "1";
                loginInfoOrder = null;
            }
            Cursor cursorCP = getEntryDataFromLoginInfo(context, loginInfoWhere,loginInfoOrder);
            //set CPNo:
            // 1. check from login info how many rows are there for this RaceID
            //2. If this CPNo exists check the next ... and so on... Raise alarm if all are used



            if (cursorCP.getCount() > 0) {
                cursorCP.moveToFirst();
                entryObj.setCPID(Integer.parseInt(cursorCP.getString(0)));
                entryObj.setCPName(cursorCP.getString(1));

                while (!cursorCP.isAfterLast()) {
                    String CPNo = cursorCP.getString(2);

                    if (!hasRunnerPassedThroughCP(context, inputedBIB,CPNo)) {
                        entryObj.setCPNo(cursorCP.getString(2));
                        cpnoSet = true;
                    }
                    cursorCP.moveToNext();
                }
                if (!cpnoSet) {

                    Toast.makeText(getContext(), "The runner has already passed through this checkpoint " + cursorCP.getCount() + " times", Toast.LENGTH_SHORT).show();
                    entryObj.setValid(false);
                    entryObj.setReasonInvalid("Code 02: The runner has already passed through this checkpoint " + cursorCP.getCount() + " times");
                    status = "already passed";
                }
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

        if (!globals.getBoolean("islogin",false)) {
            if (!status.equals("too soon")) {
                status = "not logged in";
            }
        }

        MediaPlayer mpSuccess = MediaPlayer.create(getContext(), R.raw.beep_short);
        MediaPlayer mpError = MediaPlayer.create(getContext(), R.raw.beep_long);


        //set colors depending on status:
        switch (status) {
            case "not logged in":
              //  tvBIBEntry.setBackgroundColor(Color.parseColor("#FFFF0004"));
              //  tvBIBEntry.setTextColor(Color.parseColor("#FF7BFDB1"));
                layoutInput.setBackgroundColor(Color.parseColor("#FFBBD0BF"));  //light gray
                mpSuccess.start();
                break;
            case "success":
              //  tvBIBEntry.setBackgroundColor(Color.parseColor("#FFD9DDFF"));
               // tvBIBEntry.setTextColor(Color.parseColor("#FF004F0D"));
                layoutInput.setBackgroundColor(Color.parseColor("#FF7BFDB1"));  //lightest green
                mpSuccess.start();
                break;
            case "too soon":
                tvBIBEntry.setBackgroundColor(Color.parseColor("#FFFF0004"));   //red
                tvBIBEntry.setTextColor(Color.parseColor("#FFFFFFFF"));         //white
                layoutInput.setBackgroundColor(Color.parseColor("#FFFF0004"));  //red
                mpError.start();
                break;
            case "already passed":
                tvBIBEntry.setBackgroundColor(Color.parseColor("#FF3E50FF"));   //blue
                tvBIBEntry.setTextColor(Color.parseColor("#FF7BFDB1"));         //lightest green
                layoutInput.setBackgroundColor(Color.parseColor("#FF3E50FF"));  //blue
                mpError.start();
                break;
            default:
                tvBIBEntry.setBackgroundColor(Color.parseColor("#FFFF0004"));
                tvBIBEntry.setTextColor(Color.parseColor("#FF7BFDB1"));
                mpSuccess.start();
                break;
        }

        // FF7BFDB1 lightgreen
        // FFFF0004 red
        //  tvBIBEntry.setBackgroundColor(Color.parseColor("#FFFF0004"));
        //  tvBIBEntry.setTextColor(Color.parseColor("#FF7BFDB1"));

        return entryObj;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() == null) viewInflated = false;
        if (isVisibleToUser && viewInflated) {
           // final SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);
            InitializeInputFragment(getContext(),getView(), getActivity());
            SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);
            if (globals.getBoolean("islogin",false)) {
                disableEnableControls(true,layoutInput);
            } else {
                disableEnableControls(false,layoutInput);

            }
        }
    }



}
