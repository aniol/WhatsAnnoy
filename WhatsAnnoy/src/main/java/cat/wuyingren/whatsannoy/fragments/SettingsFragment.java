package cat.wuyingren.whatsannoy.fragments;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.preference.CheckBoxPreference;
import org.holoeverywhere.preference.PreferenceFragment;
import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.preference.SharedPreferences.OnSharedPreferenceChangeListener;
import org.holoeverywhere.widget.Toast;

import cat.wuyingren.whatsannoy.R;


public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

    public SettingsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        CheckBoxPreference cbox = (CheckBoxPreference)findPreference(R.id.my_checkbox);
        //Toast.makeText(this, "Value: " + preferences.getBoolean(R.id.my_checkbox, false), Toast.LENGTH_SHORT).show();
    }
}
