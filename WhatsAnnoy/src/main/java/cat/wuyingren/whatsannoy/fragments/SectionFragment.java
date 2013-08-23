package cat.wuyingren.whatsannoy.fragments;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.DialogFragment;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.widget.ArrayAdapter;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.ProgressBar;

import java.io.IOException;
import java.util.List;

import cat.wuyingren.whatsannoy.R;
import cat.wuyingren.whatsannoy.profiles.Schedule;
import cat.wuyingren.whatsannoy.sql.ScheduleDataSource;
import cat.wuyingren.whatsannoy.adapters.ScheduleListAdapter;

public class SectionFragment extends Fragment {

    public static final String ARG_SECTION_NUMBER = "section_number";
    private Context context;
    private ScheduleDataSource dataSource;
    private ArrayAdapter<Schedule> adapter;
    private View rootView;
    private SharedPreferences prefs;


    public SectionFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        dataSource = new ScheduleDataSource(context);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int id = getArguments().getInt(ARG_SECTION_NUMBER);
        switch(id) {
            case 1:     // Screen "Now"
                setHasOptionsMenu(false);
                rootView = inflater.inflate(R.layout.fragment_section_now, container, false);
                Button b = (Button) rootView.findViewById(R.id.button);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            String uri = prefs.getString("pref_general_sound_key", "");
                            Uri ringtone = Uri.parse(uri);
                            if(uri.isEmpty()) {
                                ringtone = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION);
                            }
                            MediaPlayer mPlayer = new MediaPlayer();
                            mPlayer.setDataSource(context, ringtone);
                            AudioManager aManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                            if(aManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) !=0 ) {
                                mPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                                mPlayer.setLooping(false);
                                mPlayer.prepare();
                                mPlayer.start();
                            }
                        }
                        catch(IOException e) {

                        }
                    }
                });
                break;
            case 2:     // Screen "Schedule"
                setHasOptionsMenu(true);
                rootView = inflater.inflate(R.layout.fragment_section_add_schedule, container, false);
                updateDB();
                /*ListView lView = (ListView)rootView.findViewById(R.id.listView);
                dataSource.open();

                List<Schedule> schedules = dataSource.getAllSchedules();
                /*adapter = new ArrayAdapter<Schedule>(context, android.R.layout.simple_list_item_1,
                        schedules);*/
               /* adapter = new ScheduleListAdapter(context, schedules);
                lView.setAdapter(adapter);
                dataSource.close();*/
                break;
            case 3:     // Screen "Random"
                setHasOptionsMenu(false);
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

    @Override
    public void onResume() {
        /*if(dataSource!=null) {
            dataSource.open();
            List<Schedule> schedules = dataSource.getAllSchedules();
            adapter = new ArrayAdapter<Schedule>(context, android.R.layout.simple_list_item_1,
                    schedules);
            adapter.notifyDataSetChanged();
        }*/
        super.onResume();
    }

    @Override
    public void onPause() {
        /*if(dataSource!=null) {
            dataSource.close();
        }*/
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getMenuInflater().inflate(R.menu.fragment_schedule_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_new:
                scheduleNew();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void scheduleNew() {
        DialogFragment df = new TimePickerFragment();
        df.show(getActivity().getSupportFragmentManager(), "timePicker");

    }
    public void updateDB() {
        if(dataSource!=null) {
            ListView lView = (ListView)rootView.findViewById(R.id.listView);
            dataSource.open();

            List<Schedule> schedules = dataSource.getAllSchedules();
            /*adapter = new ArrayAdapter<Schedule>(context, android.R.layout.simple_list_item_1,
                    schedules);*/

            adapter = new ScheduleListAdapter(context, schedules, dataSource);
            lView.setAdapter(adapter);
            dataSource.close();
        }

    }


}
