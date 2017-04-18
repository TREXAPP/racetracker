package layout;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.trex.racetracker.DatabaseHelper;
import com.trex.racetracker.EntryObj;
import com.trex.racetracker.LogoutWorker;
import com.trex.racetracker.MainActivity;
import com.trex.racetracker.R;
import com.trex.racetracker.StaticMethods;

import java.util.Date;

import static com.trex.racetracker.StaticMethods.PopulateInputEntriesListView;
import static com.trex.racetracker.StaticMethods.*;

/**
 * Created by Igor on 02.12.2016.
 */

public class EditEntryDialog extends DialogFragment {

    Context context;
    private String BIB = null;
    private String hours = null;
    private String minutes = null;
    private String seconds = null;
    private EntryObj entryObj;
    private ListView lvInputEntries;


    public EditEntryDialog() {
        super();
    }

    public EditEntryDialog(Context ctx, String bib, String hr, String min, String sec, EntryObj entryObj, ListView lvInputEntries) {
   // public EditEntryDialog(Handler handler, Context ctx) {
      //  this.handler = handler;
        this.context = ctx;
        this.BIB = bib;
        this.hours = hr;
        this.minutes = min;
        this.seconds = sec;
        this.entryObj = entryObj;
        this.lvInputEntries = lvInputEntries;


    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //  handler.sendEmptyMessage(0);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertDialog = inflater.inflate(R.layout.alert_edit_entry,null);

     //   final View fragmentLogin = inflater.inflate(R.layout.fragment_login,null);
     //   final View fragmentRacers = inflater.inflate(R.layout.fragment_racers,null);

        builder.setView(alertDialog);
        final Button btnEdit = (Button) alertDialog.findViewById(R.id.btnEdit);
        final Button btnCancelEdit = (Button) alertDialog.findViewById(R.id.btnCancelLogout);
        final EditText etBIBAlert = (EditText) alertDialog.findViewById(R.id.etBIBAlert);
        final EditText etHours = (EditText) alertDialog.findViewById(R.id.etHours);
        final EditText etMinutes = (EditText) alertDialog.findViewById(R.id.etMinutes);
        final EditText etSeconds = (EditText) alertDialog.findViewById(R.id.etSeconds);

        final SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
        final int inputDigitsNo = globals.getInt("inputdigitsno",3);

                //initialize dialog
        etBIBAlert.setText(BIB);
        etHours.setText(hours);
        etMinutes.setText(minutes);
        etSeconds.setText(seconds);


        etBIBAlert.requestFocus();
        etBIBAlert.setSelection(inputDigitsNo);
        //ToggleKeyboard(Boolean.TRUE,context);

        btnEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO
                DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
                String oldDate = entryObj.getTime();
                String newBIB = etBIBAlert.getText().toString();
                int newHours = Integer.parseInt(etHours.getText().toString());
                int newMins = Integer.parseInt(etMinutes.getText().toString());
                int newSecs = Integer.parseInt(etSeconds.getText().toString());

                String newDate = StaticMethods.formatEditedDate(oldDate,  newHours, newMins, newSecs);
                String whereClause = "myEntry=1 AND TimeStamp='" + entryObj.getTimeStamp() + "'";
               int rowsUpdated = dbHelper.updateEntry(newBIB,newDate,whereClause);
                Toast.makeText(context, rowsUpdated + " entries updated.", Toast.LENGTH_SHORT).show();
        //        handler.sendEmptyMessage(0);
                getDialog().dismiss();
               // ToggleKeyboard(Boolean.FALSE,context);
                PopulateInputEntriesListView(context,lvInputEntries, getActivity());

            }
        });

        btnCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                //ToggleKeyboard(Boolean.FALSE,context);

            }
        });

        etBIBAlert.addTextChangedListener(new TextWatcher() {
            private String beforeText;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // etBIBAlert.setSelection(etBIBAlert.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {

                ValidatePopup(etBIBAlert,etHours,etMinutes,etSeconds, btnEdit,inputDigitsNo);
                if (etBIBAlert.getText().toString().length() > inputDigitsNo) {
                    etBIBAlert.setText(beforeText);
                    etBIBAlert.setSelection(etBIBAlert.getText().length());
                }

            }
        });

        etBIBAlert.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ValidatePopup(etBIBAlert,etHours,etMinutes,etSeconds, btnEdit,inputDigitsNo);
            }

        });

        etHours.addTextChangedListener(new TextWatcher() {
            private String beforeText;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // etBIBAlert.setSelection(etBIBAlert.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etHours.getText().toString().equals("")) {
                    try {
                        Integer.parseInt(etHours.getText().toString());
                    } catch(Exception e) {
                        etHours.setText(beforeText);
                    }
                }

                ValidatePopup(etBIBAlert,etHours,etMinutes,etSeconds, btnEdit,inputDigitsNo);
                if (etHours.getText().toString().length() > 2) {
                    etHours.setText(beforeText);
                    etHours.setSelection(etHours.getText().length());
                }


            }
        });

        etHours.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ValidatePopup(etBIBAlert,etHours,etMinutes,etSeconds, btnEdit,inputDigitsNo);
            }
        });


        etMinutes.addTextChangedListener(new TextWatcher() {

            private String beforeText;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // etBIBAlert.setSelection(etBIBAlert.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etMinutes.getText().toString().equals("")) {
                    try {
                        Integer.parseInt(etMinutes.getText().toString());
                    } catch(Exception e) {
                        etMinutes.setText(beforeText);
                    }
                }
                ValidatePopup(etBIBAlert,etHours,etMinutes,etSeconds, btnEdit,inputDigitsNo);
                if (etMinutes.getText().toString().length() > 2) {
                    etMinutes.setText(beforeText);
                    etMinutes.setSelection(etMinutes.getText().length());
                }
            }
        });

        etMinutes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ValidatePopup(etBIBAlert,etHours,etMinutes,etSeconds, btnEdit,inputDigitsNo);
            }
        });


        etSeconds.addTextChangedListener(new TextWatcher() {
            private String beforeText;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // etBIBAlert.setSelection(etBIBAlert.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etSeconds.getText().toString().equals("")) {
                    try {
                        Integer.parseInt(etSeconds.getText().toString());
                    } catch(Exception e) {
                        etSeconds.setText(beforeText);
                    }
                }

                ValidatePopup(etBIBAlert,etHours,etMinutes,etSeconds, btnEdit,inputDigitsNo);
                if (etSeconds.getText().toString().length() > 2) {
                    etSeconds.setText(beforeText);
                    etSeconds.setSelection(etSeconds.getText().length());
                }

            }
        });

        etSeconds.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ValidatePopup(etBIBAlert,etHours,etMinutes,etSeconds, btnEdit,inputDigitsNo);
            }
        });



        return builder.create();
    }

    public void ValidatePopup(EditText etBIBAlert, EditText etHours, EditText etMinutes, EditText etSeconds, Button btnEdit, int inputDigitsNo) {
        boolean valid = true;
        if (etBIBAlert.getText().toString().length() != inputDigitsNo) {
            valid = false;
            etBIBAlert.setBackgroundColor(Color.parseColor("#FFFF0004")); //red
        } else {
            if (etBIBAlert.hasFocus()) {
                etBIBAlert.setBackgroundColor(Color.parseColor("#FFF5F5F5")); //ligthest gray
            } else {
                etBIBAlert.setBackgroundColor(Color.parseColor("#FFFFFFFF")); //white
            }
        }

        if (etHours.getText().toString().length() != 2 || Integer.parseInt(etHours.getText().toString()) > 23) {
            valid = false;
            etHours.setBackgroundColor(Color.parseColor("#FFFF0004")); //red
        } else {
            if (etHours.hasFocus()) {
                etHours.setBackgroundColor(Color.parseColor("#FFF5F5F5")); //ligthest gray
            } else {
                etHours.setBackgroundColor(Color.parseColor("#FFFFFFFF")); //white
            }
        }

        if (etMinutes.getText().toString().length() != 2 || Integer.parseInt(etMinutes.getText().toString()) > 59) {
            valid = false;
            etMinutes.setBackgroundColor(Color.parseColor("#FFFF0004")); //red
        } else {
            if (etMinutes.hasFocus()) {
                etMinutes.setBackgroundColor(Color.parseColor("#FFF5F5F5")); //ligthest gray
            } else {
                etMinutes.setBackgroundColor(Color.parseColor("#FFFFFFFF")); //white
            }
        }

        if (etSeconds.getText().toString().length() != 2 || Integer.parseInt(etSeconds.getText().toString()) > 59) {
            valid = false;
            etSeconds.setBackgroundColor(Color.parseColor("#FFFF0004")); //red
        } else {
            if (etSeconds.hasFocus()) {
                etSeconds.setBackgroundColor(Color.parseColor("#FFF5F5F5")); //ligthest gray
            } else {
                etSeconds.setBackgroundColor(Color.parseColor("#FFFFFFFF")); //white
            }
        }

        if (valid) {
            btnEdit.setEnabled(true);
        } else {
            btnEdit.setEnabled(false);
        }

    }


}
