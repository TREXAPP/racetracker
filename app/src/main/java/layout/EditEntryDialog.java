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

    public EditEntryDialog() {
        super();
    }

    public EditEntryDialog(Context ctx) {
   // public EditEntryDialog(Handler handler, Context ctx) {
      //  this.handler = handler;
        this.context = ctx;
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

        return builder.create();
    }
}
