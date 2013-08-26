package cat.wuyingren.whatsannoy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.ProgressBar;

import cat.wuyingren.whatsannoy.R;

public class RandomFragment extends Fragment {

   // public static final String ARG_SECTION_NUMBER = "section_number";
    private Context context;
    private View rootView;

    /**
     * Static factory method that takes an int parameter,
     * initializes the fragment's arguments, and returns the
     * new fragment to the client.
     */
    public static RandomFragment newInstance() {
        RandomFragment fragment = new RandomFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(false);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_section_random, container, false);

        ProgressBar pbar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        pbar.setProgress(45);
        return rootView;
    }

}
