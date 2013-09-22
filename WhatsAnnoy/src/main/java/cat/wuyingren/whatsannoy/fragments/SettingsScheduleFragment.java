/** Copyright (c) 2013 Jordi López <@wuyingren> & <@aniol>
 *
 * MIT License:
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package cat.wuyingren.whatsannoy.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;

import org.holoeverywhere.preference.CheckBoxPreference;
import org.holoeverywhere.preference.Preference;
import org.holoeverywhere.preference.PreferenceFragment;
import org.holoeverywhere.preference.RingtonePreference;

import cat.wuyingren.whatsannoy.R;
import cat.wuyingren.whatsannoy.activities.SettingsScheduleActivity;
import cat.wuyingren.whatsannoy.profiles.Schedule;
import cat.wuyingren.whatsannoy.sql.ScheduleDataSource;

/**
 * @author Jordi López (wuyingren)
 */
public class SettingsScheduleFragment extends PreferenceFragment{

    private ActionBar actBar;
    private Bundle args;
    private Schedule s;
    private int position;
    private ScheduleDataSource dataSource;
    private Context context;

    public SettingsScheduleFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_schedule);

        context = getActivity();

        actBar = getSupportActionBar();
        actBar.setHomeButtonEnabled(true);
        actBar.setDisplayHomeAsUpEnabled(true);

        args = getArguments();
        s = args.getParcelable(SettingsScheduleActivity.ARG_SCHEDULE);
        position = args.getInt(SettingsScheduleActivity.ARG_POSITION);

        dataSource = new ScheduleDataSource(context);
        dataSource.open();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final CheckBoxPreference cbox = (CheckBoxPreference) findPreference(R.id.alarm_enabled);
        cbox.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                s.setIsEnabled((Boolean) o);
                dataSource.updateSchedule(context, s);
                return true;
            }
        });

        final RingtonePreference rng = (RingtonePreference) findPreference(R.id.rtone_pref);
        rng.setKey(rng.getKey()+position);
        rng.setDefaultValue(Uri.parse(s.getRingtone()));
        rng.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                //Log.w("TAG", o.toString());
                s.setRingtone(o.toString());
                dataSource.updateSchedule(context, s);
                //Log.w("TAG", s.getRingtone());
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
