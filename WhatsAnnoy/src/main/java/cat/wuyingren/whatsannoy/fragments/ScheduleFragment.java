package cat.wuyingren.whatsannoy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import org.holoeverywhere.app.DialogFragment;
import org.holoeverywhere.app.ListFragment;
import org.holoeverywhere.widget.ListView;

import cat.wuyingren.whatsannoy.R;
import cat.wuyingren.whatsannoy.activities.SettingsScheduleActivity;
import cat.wuyingren.whatsannoy.adapters.ScheduleListAdapter;
import cat.wuyingren.whatsannoy.profiles.Schedule;
import cat.wuyingren.whatsannoy.sql.ScheduleDataSource;

public class ScheduleFragment extends ListFragment {

    //public static final String ARG_SECTION_NUMBER = "section_number";
    public static final String ARG_SDKVERSION_NUMBER = "sdkversion_number";
    private Context context;
    private ScheduleDataSource dataSource;
    private ScheduleListAdapter adapter;
    private int sdkVer;
    private ListView lView;

    private ActionMode mActionMode;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.cab_list, menu);
           // actionMode.setTitle("1 selected");
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_edit:
                    editItem(lView.getCheckedItemPosition());
                    actionMode.finish(); // Action picked, so close the CAB
                    return true;
                case R.id.action_delete:
                    deleteItem(lView.getCheckedItemPosition());
                    actionMode.finish();
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mActionMode = null;
        }
    };

    private void editItem(int id) {
        Schedule s = adapter.getItem(id);
        updateSchedule(s);
    }

    private void deleteItem(int id) {
        Schedule s = adapter.getItem(id);
        dataSource.deleteSchedule(s);
        updateDB();
    }

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lView = getListView();
        lView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        lView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                lView.setItemChecked(position, true);
                if (mActionMode != null) {
                    return false;
                }
                // Start the CAB using the ActionMode.Callback defined above
                mActionMode = getSupportActivity().startSupportActionMode(mActionModeCallback);
                view.setSelected(true);
                return true;
            }
        });

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Display the fragment as the main content.
                /*getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.pager, new SettingsScheduleActivity())
                        .commit();*/
                Intent i = new Intent(context, SettingsScheduleActivity.class);
                Bundle b = new Bundle();
                Schedule s = adapter.getItem(position);
                /*Log.w("TAG", "FRG " + s.getDate());
                Log.w("TAG", "FRG " + s.getEnabled());*/
                b.putParcelable(SettingsScheduleActivity.ARG_SCHEDULE, s);
                i.putExtras(b);
                startActivity(i);
            }
        });
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
        Bundle b = new Bundle();
        b.putBoolean(TimePickerFragment.ARG_UPDATE, false);
        df.setArguments(b);
        df.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    private void updateSchedule(Schedule s) {
        DialogFragment df = new TimePickerFragment();
        Bundle b = new Bundle();
        b.putParcelable(TimePickerFragment.ARG_SCHEDULE, s);
        b.putBoolean(TimePickerFragment.ARG_UPDATE, true);
        Log.w("TAG", String.valueOf(s.getDate()));
        df.setArguments(b);
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
        dataSource.open();
        if(dataSource == null) {
            initDB();
        }
        else {
            updateDB();
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