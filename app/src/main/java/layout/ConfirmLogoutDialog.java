package layout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.inputmethod.InputMethodManager;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.trex.racetracker.LogoutWorker;
import com.trex.racetracker.MainActivity;
import com.trex.racetracker.R;
import layout.Login;

import org.w3c.dom.Text;

/**
 * Created by Igor_2 on 13.11.2016.
 */

public class ConfirmLogoutDialog extends DialogFragment {

    Handler handler;

    public ConfirmLogoutDialog(Handler handler) {
        this.handler = handler;
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
        View alertDialog = inflater.inflate(R.layout.alert_logout,null);
        final View fragmentLogin = inflater.inflate(R.layout.fragment_login,null);
        final View fragmentRacers = inflater.inflate(R.layout.fragment_racers,null);
        builder.setView(alertDialog);
        final Button btnLogout = (Button) alertDialog.findViewById(R.id.btnLogoutFinal);
        final Button btnCancel = (Button) alertDialog.findViewById(R.id.btnCancelLogout);
        final EditText etPassword = (EditText) alertDialog.findViewById(R.id.etPasswordAlert);
        final TextView tvErrorLogout = (TextView) alertDialog.findViewById(R.id.tvErrorLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);
                if (etPassword.getText().toString().equals(globals.getString("password",""))) {
                    //LOGOUT success!
                    tvErrorLogout.setText("Enter your password");
                    tvErrorLogout.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDarkGray));
                    etPassword.setText("");

                    final String DeviceID = globals.getString("deviceid","");
                    LogoutWorker logoutWorker = new LogoutWorker(getContext(),fragmentLogin,fragmentRacers, handler);
                    logoutWorker.execute(Login.TYPE_LOGOUT,Login.URL_LOGOUT,globals.getString("username",""),globals.getString("operator",""),DeviceID,Login.COMMENT_LOGOUT);

                    fragmentLogin.postInvalidate();

                    getDialog().dismiss();
                    //turn off keyboard:
                    InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow((null == getActivity().getCurrentFocus()) ? null : getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                   // handler.sendEmptyMessage(0);
                } else {
                    //LOGOUT failed, wrong password
                    tvErrorLogout.setText("Wrong password!");
                    tvErrorLogout.setTextColor(Color.RED);
                    etPassword.setText("");

                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return builder.create();
    }
}
