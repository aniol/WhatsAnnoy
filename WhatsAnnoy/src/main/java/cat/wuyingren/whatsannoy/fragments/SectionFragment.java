package cat.wuyingren.whatsannoy.fragments;

import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
import org.holoeverywhere.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.TimePicker;

import org.holoeverywhere.widget.ProgressBar;
import org.holoeverywhere.widget.TextView;
import org.holoeverywhere.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.holoeverywhere.app.Fragment;

import cat.wuyingren.whatsannoy.R;

/**
 * Created by jordilb on 05/08/13.
 */
public class SectionFragment extends Fragment {

    public static final String ARG_SECTION_NUMBER = "section_number";

    public SectionFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        int id = getArguments().getInt(ARG_SECTION_NUMBER);
        View rootView;
        switch(id) {
            case 1:
                rootView = inflater.inflate(R.layout.fragment_section_now, container, false);
                break;
            case 2:
                rootView = inflater.inflate(R.layout.fragment_section_schedule, container, false);
                TimePicker tp = (TimePicker) rootView.findViewById(R.id.timePicker);
                tp.setIs24HourView(true);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                TextView tv = (TextView) rootView.findViewById(R.id.textView);
                tv.setText(sdf.format(new Date()));
                break;
            case 3:
                rootView = inflater.inflate(R.layout.fragment_section_random, container, false);

                ProgressBar pbar = (ProgressBar) rootView.findViewById(R.id.progressBar);
                pbar.setProgress(45);
                break;
            default:
                rootView = inflater.inflate(R.layout.fragment_section_now, container, false);
                break;
        }

        return rootView;
    }


}
