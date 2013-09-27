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

package cat.wuyingren.whatsannoy.activities;

import android.content.Context;
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

/**
 * @author Jordi López <wuyingren>
 */
public class LicenseActivity extends Activity {

    private ActionBar actBar;
    private Resources res;
    private TextView tView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        context = getApplicationContext();
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

                Intent homeIntent = new Intent(context, MainActivity.class);
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
