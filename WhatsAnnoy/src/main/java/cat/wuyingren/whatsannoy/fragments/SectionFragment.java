package cat.wuyingren.whatsannoy.fragments;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import org.holoeverywhere.LayoutInflater;
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
                rootView = inflater.inflate(R.layout.fragment_section_add_schedule, container, false);
                ListView lView = (ListView)rootView.findViewById(R.id.listView);
                dataSource.open();

                List<Schedule> schedules = dataSource.getAllSchedules();
                adapter = new ArrayAdapter<Schedule>(context, android.R.layout.simple_list_item_1,
                        schedules);
                lView.setAdapter(adapter);
               /* TimePicker tp = (TimePicker) rootView.findViewById(R.id.timePicker);
                tp.setIs24HourView(true);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                TextView tv = (TextView) rootView.findViewById(R.id.textView);
                tv.setText(sdf.format(new Date()));*/
                break;
            case 3:     // Screen "Random"
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
        int id = getArguments().getInt(ARG_SECTION_NUMBER);
        switch(id) {
            case 2:     //Screen "Schedule"
                dataSource.open();
                adapter.notifyDataSetChanged();
                break;
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        int id = getArguments().getInt(ARG_SECTION_NUMBER);
        switch(id) {
            case 2:     // Screen "Schedule"
                dataSource.close();
                break;
        }
        super.onPause();
    }

    public void updateDB() {
        int id = getArguments().getInt(ARG_SECTION_NUMBER);
        switch(id) {
            case 2:     // Screen "Schedule"
                ListView lView = (ListView)rootView.findViewById(R.id.listView);
                dataSource.open();

                List<Schedule> schedules = dataSource.getAllSchedules();
                adapter = new ArrayAdapter<Schedule>(context, android.R.layout.simple_list_item_1,
                        schedules);
                lView.setAdapter(adapter);
                break;
        }
    }

}
