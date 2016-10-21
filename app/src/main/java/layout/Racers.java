package layout;

/**
 * Created by Igor on 22.10.2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trex.racetracker.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class Racers extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public Racers() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Racers newInstance(int sectionNumber) {
        Racers fragment = new Racers();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText("Vamu ke gi lista site momentalni trkaci");
      //  textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;

    }

}
