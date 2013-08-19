package cat.wuyingren.whatsannoy.fragments;

import android.os.Bundle;

import org.holoeverywhere.preference.PreferenceFragment;
import org.holoeverywhere.preference.RingtonePreference;
import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.preference.SharedPreferences.OnSharedPreferenceChangeListener;

import cat.wuyingren.whatsannoy.R;


public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {


    public SettingsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        RingtonePreference rtone = (RingtonePreference)findPreference(key);
        if(key.equals(rtone.getKey())) {
            //Toast.makeText(getActivity(), "Value: " + preferences.getString(key, ""), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
}
