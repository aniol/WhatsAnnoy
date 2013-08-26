package cat.wuyingren.whatsannoy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import org.holoeverywhere.app.Activity;

import cat.wuyingren.whatsannoy.R;
import cat.wuyingren.whatsannoy.fragments.SettingsFragment;

public class SettingsActivity extends Activity {

    private ActionBar actBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        actBar = getSupportActionBar();

        actBar.setHomeButtonEnabled(true);
        actBar.setDisplayHomeAsUpEnabled(true);

        // Display the fragment as the main content.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings_content, new SettingsFragment())
                .commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
