package cat.wuyingren.whatsannoy;

import android.os.Bundle;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.Fragment;

import cat.wuyingren.whatsannoy.fragments.SettingsFragment;

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Display the fragment as the main content.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings_content, new SettingsFragment())
                .commit();
    }
}
