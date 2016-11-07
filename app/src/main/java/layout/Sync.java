package layout;

/**
 * Created by Igor on 22.10.2016.
 */

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.trex.racetracker.LoginWorker;
import com.trex.racetracker.LogoutWorker;
import com.trex.racetracker.MainActivity;
import com.trex.racetracker.Methods;
import com.trex.racetracker.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class Sync extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TYPE_LOGIN = "login";
    private static final String TYPE_LOGOUT= "logout";
    private static final String URL_LOGIN = "http://app.trex.mk/login.php";
    private static final String URL_LOGOUT = "http://app.trex.mk/logout.php";
    private static final String COMMENT_LOGIN = "";
    private static final String COMMENT_LOGOUT = "";

    public Sync() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Sync newInstance(int sectionNumber) {
        Sync fragment = new Sync();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_sync, container, false);

        final FrameLayout fragmentSync = (FrameLayout) rootView.findViewById(R.id.fragmentSync);
        final TextView tvStatusTop = (TextView) rootView.findViewById(R.id.tvStatusTop);
        final EditText etUsername = (EditText) rootView.findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) rootView.findViewById(R.id.etPassword);
        final EditText etOperator = (EditText) rootView.findViewById(R.id.etOperator);
        final Button btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        final Button btnLogout = (Button) rootView.findViewById(R.id.btnLogout);
        final TextView tvStatusBottom = (TextView) rootView.findViewById(R.id.tvStatusBottom);


        //initialize views appearence
        final SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);
        final String DeviceID = globals.getString("deviceid","");
        Methods methods = new Methods();
        methods.InitializeSyncFragment(getContext(),rootView, globals);

        btnLogin.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //Do stuff here:
                //1. set status variable to Logging...
                //2. Call a function from LoginWorker to login
                //3. contact mysql db, return result from webservice
                //4. if false print error msg
                //5. if true: set global variables
                //6. call oncreate again and modify it to show different things depending on the global variables

                LoginWorker loginWorker = new LoginWorker(getContext(),fragmentSync);
                loginWorker.execute(TYPE_LOGIN,URL_LOGIN,etUsername.getText().toString(),etPassword.getText().toString(),etOperator.getText().toString(),DeviceID,COMMENT_LOGIN);

                //turn off keyboard:
                InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow((null == getActivity().getCurrentFocus()) ? null : getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            }
        });

        btnLogout.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                LogoutWorker logoutWorker = new LogoutWorker(getContext(),fragmentSync);
                logoutWorker.execute(TYPE_LOGOUT,URL_LOGOUT,globals.getString("username",""),globals.getString("operator",""),DeviceID,COMMENT_LOGOUT);

                //turn off keyboard:
                InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow((null == getActivity().getCurrentFocus()) ? null : getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        return rootView;

    }




/*
    public void OnLogin (View view) {
        String Username =
                String Query = QueryET.getText().toString();
        String Type = "query";
        LoginWorker backgroundWorker = new LoginWorker(this);
        backgroundWorker.execute(Type, Query);

    }
    */

}
