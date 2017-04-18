package layout;

/**
 * Created by Igor on 22.10.2016.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.inputmethod.InputMethodManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.trex.racetracker.LoginWorker;
import com.trex.racetracker.LogoutWorker;
import com.trex.racetracker.MainActivity;
import com.trex.racetracker.R;

import static com.trex.racetracker.StaticMethods.*;

/**
 * A placeholder fragment containing a simple view.
 */
public class Login extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private boolean viewInflated;
    private static final String ARG_SECTION_NUMBER = "section_number";
    protected static final String TYPE_LOGIN = "login";
    protected static final String TYPE_LOGOUT= "logout";
    protected static final String URL_LOGIN = "http://app.trex.mk/login.php";
    protected static final String URL_LOGOUT = "http://app.trex.mk/logout.php";
    protected static final String COMMENT_LOGIN = "";
    protected static final String COMMENT_LOGOUT = "";

    public Login() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Login newInstance(int sectionNumber) {
        Login fragment = new Login();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View fragmentLogin = inflater.inflate(R.layout.fragment_login, container, false); //rootview == fragment_login
        final View fragmentRacers = inflater.inflate(R.layout.fragment_racers, container, false);

        final TextView tvStatusTop = (TextView) fragmentLogin.findViewById(R.id.tvStatusTop);
        final EditText etUsername = (EditText) fragmentLogin.findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) fragmentLogin.findViewById(R.id.etPassword);
        final EditText etOperator = (EditText) fragmentLogin.findViewById(R.id.etOperator);
        final Button btnLogin = (Button) fragmentLogin.findViewById(R.id.btnLogin);
        final Button btnLogout = (Button) fragmentLogin.findViewById(R.id.btnLogout);


        //initialize views appearence
        final SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);
        final String DeviceID = globals.getString("deviceid","");

        if (!viewInflated) {
            InitializeLoginFragment(getContext(),fragmentLogin, globals);
        }
        viewInflated = true;



        btnLogin.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                LoginWorker loginWorker = new LoginWorker(getContext(),fragmentLogin, fragmentRacers);
                loginWorker.execute(TYPE_LOGIN,URL_LOGIN,etUsername.getText().toString(),etPassword.getText().toString(),etOperator.getText().toString(),DeviceID,COMMENT_LOGIN);
                TurnOffKeyboard(getActivity(),getContext());
                //turn off keyboard:
               // InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                //inputManager.hideSoftInputFromWindow((null == getActivity().getCurrentFocus()) ? null : getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            }
        });

        btnLogout.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                LogoutDialogDismissHandler handler = new LogoutDialogDismissHandler();
                final SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);
                DialogFragment dialog = new ConfirmLogoutDialog(handler);
                dialog.show(getFragmentManager(),"logout");


                /*
                final EditText etPassword = new EditText(getContext());
                alert.setMessage("Enter the password for user " + globals.getString("username",""));
                alert.setTitle("Logout");
                alert.setView(etPassword);
                alert.
                alert.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String Password = etPassword.getText().toString();
                        LogoutWorker logoutWorker = new LogoutWorker(getContext(),fragmentLogin,fragmentRacers);
                        logoutWorker.execute(TYPE_LOGOUT,URL_LOGOUT,globals.getString("username",""),globals.getString("operator",""),DeviceID,COMMENT_LOGOUT);
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
                */


                /*
                LogoutWorker logoutWorker = new LogoutWorker(getContext(),fragmentLogin,fragmentRacers);
                logoutWorker.execute(TYPE_LOGOUT,URL_LOGOUT,globals.getString("username",""),globals.getString("operator",""),DeviceID,COMMENT_LOGOUT);

                //turn off keyboard:
                InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow((null == getActivity().getCurrentFocus()) ? null : getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
*/

            }
        });

        return fragmentLogin;

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() == null) viewInflated = false;
        if (isVisibleToUser && viewInflated) {
            final SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);
            InitializeLoginFragment(getContext(),getView(),globals);
        }
    }


    private class LogoutDialogDismissHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);
            InitializeLoginFragment(getContext(),getView(), globals);

        }
    }

}


