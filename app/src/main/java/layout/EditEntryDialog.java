package layout;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.trex.racetracker.LogoutWorker;
import com.trex.racetracker.MainActivity;
import com.trex.racetracker.R;

/**
 * Created by Igor on 02.12.2016.
 */

public class EditEntryDialog extends DialogFragment {
    Handler handler;
    Context context;
    private String BIB = null;
    private String hours = null;
    private String minutes = null;
    private String seconds = null;

    public EditEntryDialog() {
        super();
    }

    public EditEntryDialog(Context ctx, String bib, String hr, String min, String sec) {
   // public EditEntryDialog(Handler handler, Context ctx) {
      //  this.handler = handler;
        this.context = ctx;
        this.BIB = bib;
        this.hours = hr;
        this.minutes = min;
        this.seconds = sec;
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

        //initialize dialog
        etBIBAlert.setText(BIB);
        etHours.setText(hours);
        etMinutes.setText(minutes);
        etSeconds.setText(seconds);
        etBIBAlert.setFocusable(true);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        btnCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
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
                if (etBIBAlert.getText().toString().length() != 3) {
                    etBIBAlert.setBackgroundColor(Color.parseColor("#FFFF0004")); //red
                    btnEdit.setEnabled(false);

                } else {
                    etBIBAlert.setBackgroundColor(Color.parseColor("#FFF5F5F5")); //lightest gray
                    btnEdit.setEnabled(true);
                }

                if (etBIBAlert.getText().toString().length() > 3) {
                    etBIBAlert.setText(beforeText);
                    etBIBAlert.setSelection(etBIBAlert.getText().length());
                }

            }
        });

        etBIBAlert.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etBIBAlert.setSelection(etBIBAlert.getText().length());
                    etBIBAlert.setBackgroundColor(Color.parseColor("#FFF5F5F5")); //lightest gray
                } else {
                    etBIBAlert.setBackgroundColor(Color.parseColor("#FFFFFFFF")); //white
                    ValidateInputs();
                }
            }

            private void ValidateInputs() {
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
                boolean valid;
                String value = etHours.getText().toString();
                if (value.length() != 2 || Integer.parseInt(value) > 23 || Integer.parseInt(value) < 0) {
                    valid = false;
                } else {
                    valid = true;
                }

                if (valid) {
                    etHours.setBackgroundColor(Color.parseColor("#FFF5F5F5")); //lightest gray
                    btnEdit.setEnabled(true);
                } else {
                    etHours.setBackgroundColor(Color.parseColor("#FFFF0004")); //red
                    btnEdit.setEnabled(false);
                }

                if (etHours.getText().toString().length() > 2) {
                    etHours.setText(beforeText);
                    etHours.setSelection(etHours.getText().length());
                }



            }
        });

        etHours.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etHours.setSelection(etHours.getText().length());
                    etHours.setBackgroundColor(Color.parseColor("#FFF5F5F5")); //lightest gray
                } else {
                    etHours.setBackgroundColor(Color.parseColor("#FFFFFFFF")); //white
                }
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
                boolean valid;
                String value = etMinutes.getText().toString();
                if (value.length() != 2 || Integer.parseInt(value) > 59 || Integer.parseInt(value) < 0) {
                    valid = false;
                } else {
                    valid = true;
                }

                if (valid) {
                    etMinutes.setBackgroundColor(Color.parseColor("#FFF5F5F5")); //lightest gray
                    btnEdit.setEnabled(true);
                } else {
                    etMinutes.setBackgroundColor(Color.parseColor("#FFFF0004")); //red
                    btnEdit.setEnabled(false);

                }

                if (etMinutes.getText().toString().length() > 2) {
                    etMinutes.setText(beforeText);
                    etMinutes.setSelection(etMinutes.getText().length());
                }



            }
        });

        etMinutes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etMinutes.setSelection(etMinutes.getText().length());
                    etMinutes.setBackgroundColor(Color.parseColor("#FFF5F5F5")); //lightest gray
                } else {
                    etMinutes.setBackgroundColor(Color.parseColor("#FFFFFFFF")); //white
                }
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
                boolean valid;
                String value = etSeconds.getText().toString();
                if (value.length() != 2 || Integer.parseInt(value) > 59 || Integer.parseInt(value) < 0) {
                    valid = false;
                } else {
                    valid = true;
                }

                if (valid) {
                    etSeconds.setBackgroundColor(Color.parseColor("#FFF5F5F5")); //lightest gray
                    btnEdit.setEnabled(true);
                } else {
                    etSeconds.setBackgroundColor(Color.parseColor("#FFFF0004")); //red
                    btnEdit.setEnabled(false);
                }

                if (etSeconds.getText().toString().length() > 2) {
                    etSeconds.setText(beforeText);
                    etSeconds.setSelection(etSeconds.getText().length());
                }

            }
        });

        etSeconds.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etSeconds.setSelection(etSeconds.getText().length());
                    etSeconds.setBackgroundColor(Color.parseColor("#FFF5F5F5")); //lightest gray
                } else {
                    etSeconds.setBackgroundColor(Color.parseColor("#FFFFFFFF")); //white
                }
            }
        });



        return builder.create();
    }


}
