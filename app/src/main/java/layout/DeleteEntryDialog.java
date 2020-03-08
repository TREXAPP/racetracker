package layout;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.trex.racetracker.Adapters.EntriesListAdapter;
import com.trex.racetracker.Models.EntryObj;
import com.trex.racetracker.Activities.MainActivity;
import com.trex.racetracker.R;

import static com.trex.racetracker.Database.DbMethods.setEntryDeleted;
import static com.trex.racetracker.StaticMethods.InitializeEntriesListView;
import static com.trex.racetracker.StaticMethods.PopulateInputEntriesListView;

/**
 * Created by Igor on 02.10.2017.
 */

public class DeleteEntryDialog extends DialogFragment {

    public static final int INPUT = 11;
    public static final int ENTRIES = 12;

    Context context;
    private EntryObj entryObj;
    private ListView listView;
    private int fragment;
    private Activity activity;
    private int deleteBtnMode;

    public DeleteEntryDialog() {
        super();
    }

    public DeleteEntryDialog(Context ctx, Activity act, EntryObj entryObj, ListView listView, int fragment, int mode) {
        this.context = ctx;
        this.entryObj = entryObj;
        this.listView = listView;
        this.fragment = fragment;
        this.activity = act;
        this.deleteBtnMode = mode;
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
        View alertDialog = inflater.inflate(R.layout.alert_delete,null);

        //   final View fragmentLogin = inflater.inflate(R.layout.fragment_login,null);
        //   final View fragmentRacers = inflater.inflate(R.layout.fragment_racers,null);

        builder.setView(alertDialog);
        final Button btnDelete = (Button) alertDialog.findViewById(R.id.btnDeleteFinal);
        final Button btnCancelDelete = (Button) alertDialog.findViewById(R.id.btnCancelDelete);
        final TextView tvAlertDeleteTitle = (TextView) alertDialog.findViewById(R.id.tvAlertDeleteTitle);
        final TextView tvErrorDelete = (TextView) alertDialog.findViewById(R.id.tvErrorDelete);

        final SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);

        if (deleteBtnMode == EntriesListAdapter.DELETE) {
            tvAlertDeleteTitle.setText("Confirm Delete");
            tvErrorDelete.setText("Are you sure you wish to DELETE this entry?");
            btnDelete.setText("Delete");
        }
        if (deleteBtnMode == EntriesListAdapter.RESTORE) {
            tvAlertDeleteTitle.setText("Confirm Restore");
            tvErrorDelete.setText("Are you sure you wish to RESTORE this entry?");
            btnDelete.setText("Restore");
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setEntryDeleted(context,"TimeStamp='" + entryObj.getTimeStamp() + "'", true, deleteBtnMode);
                if (fragment == INPUT) {
                    PopulateInputEntriesListView(context, listView, activity);
                    Toast.makeText(context, "Entry deleted!", Toast.LENGTH_SHORT).show();
                }
                if (fragment == ENTRIES) {

                    if (deleteBtnMode == EntriesListAdapter.DELETE) {
                        Toast.makeText(context, "Entry deleted!", Toast.LENGTH_SHORT).show();
                    }
                    if (deleteBtnMode == EntriesListAdapter.RESTORE) {
                        Toast.makeText(context, "Entry restored!", Toast.LENGTH_SHORT).show();
                    }

                    String selection = EntriesTab.getSelectionString(globals);
                    Boolean leftJoin = globals.getBoolean("EntriesSelectionLeftJoin",true);
                    InitializeEntriesListView(context,listView, selection, leftJoin, activity);

                }

                getDialog().dismiss();
            }
        });

        btnCancelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                //ToggleKeyboard(Boolean.FALSE,context);

            }
        });

        return builder.create();
    }

}
