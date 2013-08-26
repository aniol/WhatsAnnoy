package cat.wuyingren.whatsannoy.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import cat.wuyingren.whatsannoy.R;

public class LicenseActivity extends Activity {

    private ActionBar actBar;
    private Resources res;
    private TextView tView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        res=getResources();
        actBar = getSupportActionBar();

        actBar.setHomeButtonEnabled(true);
        actBar.setDisplayHomeAsUpEnabled(true);

        findViews();
        readFile();
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

    private void findViews() {
        tView = (TextView) findViewById(R.id.textView);
    }

    private void readFile() {
        try {
            InputStream in = res.openRawResource(R.raw.licenses);
            byte[] b = new byte[in.available()];
            in.read(b);
            tView.setText(new String(b));
            tView.setMovementMethod(new ScrollingMovementMethod());
        } catch (IOException e) {
            //e.printStackTrace();
            tView.setText(res.getString(R.string.error));
        }
    }

}
