package cat.wuyingren.whatsannoy.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;

import org.holoeverywhere.preference.CheckBoxPreference;
import org.holoeverywhere.preference.Preference;
import org.holoeverywhere.preference.PreferenceFragment;
import org.holoeverywhere.preference.RingtonePreference;

import cat.wuyingren.whatsannoy.R;
import cat.wuyingren.whatsannoy.activities.SettingsScheduleActivity;
import cat.wuyingren.whatsannoy.profiles.Schedule;
import cat.wuyingren.whatsannoy.sql.ScheduleDataSource;


public class SettingsScheduleFragment extends PreferenceFragment{

    private ActionBar actBar;
    private Bundle args;
    private Schedule s;
    private ScheduleDataSource dataSource;
    private Context context;

    public SettingsScheduleFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_schedule);

        context = getSupportActivity();

        actBar = getSupportActionBar();
        actBar.setHomeButtonEnabled(true);
        actBar.setDisplayHomeAsUpEnabled(true);

        args = getArguments();
        s = args.getParcelable(SettingsScheduleActivity.ARG_SCHEDULE);

        dataSource = new ScheduleDataSource(context);
        dataSource.open();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final CheckBoxPreference cbox = (CheckBoxPreference) findPreference(R.id.alarm_enabled);
        cbox.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                s.setIsEnabled(cbox.isChecked());
                dataSource.updateSchedule(s);
                return true;
            }
        });

        final RingtonePreference rng = (RingtonePreference) findPreference(R.id.rtone_pref);
        rng.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                Log.w("TAG", o.toString());
                s.setRingtone(o.toString());
                dataSource.updateSchedule(s);
                Log.w("TAG", s.getRingtone());
                return false;
            }
        });

        if(s!=null) {
            cbox.setChecked(s.isEnabled());

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        dataSource.open();
    }

    @Override
    public void onPause() {
        dataSource.close();
        super.onPause();
    }
}
