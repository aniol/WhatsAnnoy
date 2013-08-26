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
import org.holoeverywhere.widget.Button;

import java.io.IOException;

import cat.wuyingren.whatsannoy.R;

public class NowFragment extends Fragment {

   // public static final String ARG_SECTION_NUMBER = "section_number";
    private Context context;
    private View rootView;
    private SharedPreferences prefs;

    /**
     * Static factory method that takes an int parameter,
     * initializes the fragment's arguments, and returns the
     * new fragment to the client.
     */
    public static NowFragment newInstance() {
        NowFragment fragment = new NowFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(false);
        context = getActivity();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        return rootView;
    }

}
