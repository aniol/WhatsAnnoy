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
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.widget.ProgressBar;
import org.holoeverywhere.widget.SeekBar;
import org.holoeverywhere.widget.ToggleButton;

import java.util.Calendar;

import cat.wuyingren.whatsannoy.R;
import cat.wuyingren.whatsannoy.services.RandomNotificationService;

/**
 * @author Jordi López (wuyingren)
 */
public class RandomFragment extends Fragment {

   // public static final String ARG_SECTION_NUMBER = "section_number";
    private Context context;
    private View rootView;
    private SharedPreferences prefs;
    private int sBar_value = 0;

    private ToggleButton tBut;
    private ProgressBar pBar;
    private SeekBar sBar;

    private CountDownTimer cdt;

    /**
     * Static factory method that takes an int parameter,
     * initializes the fragment's arguments, and returns the
     * new fragment to the client.
     */
    public static RandomFragment newInstance() {
        RandomFragment fragment = new RandomFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(false);
        context = getActivity();
        prefs = getDefaultSharedPreferences();
    }

    public void updateUI(Intent intent) {
        boolean serviceRunning = intent.getBooleanExtra("isRunning", false);
        long serviceDate = intent.getLongExtra("date", 0);

        Log.w("TAG", "Received date: " + serviceDate + " and status: " + serviceRunning);
        if(serviceRunning) {
            tBut.setChecked(true);
        }
        else {
            tBut.setChecked(false);
            pBar.setProgress(pBar.getMax());
        }

        if(serviceDate!=0) {
            Calendar c = Calendar.getInstance();
            final long timeDistance = c.getTimeInMillis() - serviceDate;
            int max = (int) timeDistance;
            if(max<0) {
                max = max * (-1);
            }
            final int maxFinal = max;
            pBar.setMax(max/1000);
            Log.w("TAG", "Setting cdt. pBar is " + maxFinal);
            cdt = new CountDownTimer(maxFinal, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int step = (int) (millisUntilFinished/1000);
                    Log.w("TAG", "step " + step + " from " + maxFinal/1000);
                    pBar.setProgress(step);
                }

                @Override
                public void onFinish() {
                    //pBar.setMax(pBar.getMax());
                }
            }.start();
            //cdt.start();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_section_random, container, false);

        pBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        pBar.setProgress(pBar.getMax());

        sBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                sBar_value = i;
                Log.w("TAG", "sBar = " + sBar_value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        tBut = (ToggleButton) rootView.findViewById(R.id.toggleButton);
        tBut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    // Start Random service
                    Intent intent = new Intent("cat.wuyingren.whatsannoy.services.RandomNotificationService");
                    Bundle b = new Bundle();
                    b.putInt(RandomNotificationService.ARG_FREQUENCY, sBar_value);
                    intent.putExtras(b);
                    context.startService(intent);
                }
                else {
                    // Stop Random service
                    Intent intent = new Intent("cat.wuyingren.whatsannoy.services.RandomNotificationService");
                    context.stopService(intent);
                    if(cdt!=null) {
                        Log.w("TAG", "Canceling cdt");
                        cdt.cancel();
                    }
                }
            }
        });
        return rootView;
    }
}
