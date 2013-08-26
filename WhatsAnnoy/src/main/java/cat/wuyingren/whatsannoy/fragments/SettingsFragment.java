package cat.wuyingren.whatsannoy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import org.holoeverywhere.preference.PreferenceFragment;
import org.holoeverywhere.preference.RingtonePreference;
import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.preference.SharedPreferences.OnSharedPreferenceChangeListener;

import cat.wuyingren.whatsannoy.R;
import cat.wuyingren.whatsannoy.activities.MainActivity;


public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

    private ActionBar actBar;

    public SettingsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        actBar = getSupportActionBar();
        actBar.setHomeButtonEnabled(true);
        actBar.setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(getActivity(), MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
