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
import android.content.res.Configuration;
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
    private SharedPreferences.Editor editor;
    private int sBar_value = 0;

    private ToggleButton tBut;
    private ProgressBar pBar;
    private SeekBar sBar;

    private boolean serviceOn = false;
    private long serviceDate = 0;
    private boolean unset = true;

    private CountDownTimer cdt;

    /**
     * Static factory method that takes an int parameter,
     * initializes the fragment's arguments, and returns the
     * new fragment to the client.
     */
    public static RandomFragment newInstance() {
        Log.w("TAG", "newInstance()");
        RandomFragment fragment = new RandomFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.w("TAG", "onCreate()");
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(false);
        context = getSupportActivity().getApplicationContext();
        prefs = getDefaultSharedPreferences();
        editor = prefs.edit();

    }

    public void updateUI(Intent intent) {
        Log.w("TAG", "updateUI()");
        if(unset)  {
            unset= false;
            boolean serviceRunning = intent.getBooleanExtra("isRunning", false);
            serviceDate = intent.getLongExtra("date", 0);

            Log.w("TAG", "Received date: " + serviceDate + " and status: " + serviceRunning);

            Calendar c = Calendar.getInstance();
            final long timeDistance = c.getTimeInMillis() - serviceDate;
            int max = (int) timeDistance;
            if(max<0) {
                max = max * (-1);
            }

            if(serviceRunning) {
                //tBut.setChecked(true);
                serviceOn = true;
            }
            else {
                //tBut.setChecked(false);
               // pBar.setProgress(pBar.getMax());

                serviceOn = false;
            }

            startCountDown(serviceDate, max);
        }
    }

    private void startCountDown(long count, int max) {

        Log.w("TAG", "startCountDown()");
        if(count!=0) {
            final int maxFinal = max;

            pBar.setMax(max/1000);
            //editor.putLong("randomfragment_cdt_start", serviceDate);
            editor.putInt("randomfragment_pbar_max", pBar.getMax());
            editor.commit();

            Log.w("TAG", "Setting cdt. pBar is " + maxFinal);
            if(cdt!=null) {
                cdt.cancel();
            }
            cdt = new CountDownTimer(maxFinal, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int step = (int) (millisUntilFinished/1000);
                    Log.w("TAG", "step " + step + " from " + maxFinal/1000);
                    pBar.setProgress(step);
                }

                @Override
                public void onFinish() {
                    //editor.putLong("randomfragment_cdt_start", 0);
                    unset = true;
                    //pBar.setMax(pBar.getMax());
                }
            }.start();
            //cdt.start();
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.w("TAG", "onCreateView()");
        setRetainInstance(true);
        rootView = inflater.inflate(R.layout.fragment_section_random, container, false);

        pBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        sBar = (SeekBar) rootView.findViewById(R.id.seekBar);

        tBut = (ToggleButton) rootView.findViewById(R.id.toggleButton);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.w("TAG", "onViewCreated()");
        super.onViewCreated(view, savedInstanceState);

        //pBar.setMax(prefs.getInt("randomfragment_pbar_max", 100));
        pBar.setProgress(pBar.getMax());

        serviceOn = prefs.getBoolean("randomfragment_service_status", false);
        Log.w("TAG", "server is " + serviceOn);

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

        tBut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !serviceOn) {
                    // Start Random service
                    Intent intent = new Intent("cat.wuyingren.whatsannoy.services.RandomNotificationService");
                    Bundle b = new Bundle();
                    b.putInt(RandomNotificationService.ARG_FREQUENCY, sBar_value);
                    intent.putExtras(b);
                    context.startService(intent);
                    serviceOn = true;
                } else {
                    if (!isChecked && serviceOn) {
                        // Stop Random service
                        Intent intent = new Intent("cat.wuyingren.whatsannoy.services.RandomNotificationService");
                        context.stopService(intent);
                        if (cdt != null) {
                            Log.w("TAG", "Canceling cdt");
                            cdt.cancel();
                        }
                        serviceOn = false;
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        Log.w("TAG", "onDestroyView()");
        unset = true;
        if(cdt!=null) cdt.cancel();
        //editor.putInt("randomfragment_pbar_max", pBar.getMax());
        editor.putBoolean("randomfragment_service_status", serviceOn);
        editor.commit();
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        if(cdt!=null) cdt.cancel();
        super.onPause();
    }
}
