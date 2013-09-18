package cat.wuyingren.whatsannoy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import org.holoeverywhere.app.Activity;

import cat.wuyingren.whatsannoy.R;
import cat.wuyingren.whatsannoy.fragments.SettingsScheduleFragment;
import cat.wuyingren.whatsannoy.profiles.Schedule;

public class SettingsScheduleActivity extends Activity {

    private ActionBar actBar;
    private Bundle args;
    public static final String ARG_SCHEDULE = "schedule";
    public static final String ARG_POSITION = "position";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //addPreferencesFromResource(R.xml.settings);
        setContentView(R.layout.activity_settings);

        actBar = getSupportActionBar();
        actBar.setHomeButtonEnabled(true);
        actBar.setDisplayHomeAsUpEnabled(true);

        args = getIntent().getExtras();

        if(args != null){
            Schedule s = args.getParcelable(SettingsScheduleActivity.ARG_SCHEDULE);
            /*Log.w("TAG", "Act" + s.getDate());
            Log.w("TAG", "Act" + s.getEnabled());*/
        }
        SettingsScheduleFragment f = new SettingsScheduleFragment();
        f.setArguments(args);


        // Display the fragment as the main content.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings_content, f)
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
