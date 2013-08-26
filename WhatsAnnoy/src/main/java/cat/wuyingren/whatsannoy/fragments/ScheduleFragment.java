package cat.wuyingren.whatsannoy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.holoeverywhere.app.DialogFragment;
import org.holoeverywhere.app.ListFragment;

import cat.wuyingren.whatsannoy.R;
import cat.wuyingren.whatsannoy.adapters.ScheduleListAdapter;
import cat.wuyingren.whatsannoy.sql.ScheduleDataSource;

public class ScheduleFragment extends ListFragment {

    //public static final String ARG_SECTION_NUMBER = "section_number";
    public static final String ARG_SDKVERSION_NUMBER = "sdkversion_number";
    private Context context;
    private ScheduleDataSource dataSource;
    private ScheduleListAdapter adapter;
    private int sdkVer;


    /**
     * Static factory method that takes an int parameter,
     * initializes the fragment's arguments, and returns the
     * new fragment to the client.
     */
    public static ScheduleFragment newInstance(int sdkVer) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_SDKVERSION_NUMBER, sdkVer);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        sdkVer = getArguments().getInt(ARG_SDKVERSION_NUMBER);
        if (sdkVer>=11) {
            setHasOptionsMenu(true);
        }
        initDB();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(sdkVer>=11) {
            inflater.inflate(R.menu.fragment_schedule_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_new:
                if(sdkVer>=11) {
                    scheduleNew();
                    return true;
                }
                else {
                    return false;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void scheduleNew() {
        DialogFragment df = new TimePickerFragment();
        df.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onPause() {
        dataSource.close();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(dataSource == null) {
            initDB();
        }
    }

    private void initDB() {
        dataSource = new ScheduleDataSource(context);
        dataSource.open();

        adapter = new ScheduleListAdapter(context, dataSource.getAllSchedules(), dataSource);

        setListAdapter(adapter);
    }

    public void updateDB() {
        adapter.updateSchedules(dataSource.getAllSchedules());
    }
}