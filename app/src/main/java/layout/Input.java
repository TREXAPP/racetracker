package layout;

/**
 * Created by Igor on 22.10.2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DigitalClock;
import android.widget.TextClock;
import android.widget.TextView;

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
    //TODO do stuff here
      //  TextClock clock = new TextClock(getContext());


        return rootView;

    }

}
