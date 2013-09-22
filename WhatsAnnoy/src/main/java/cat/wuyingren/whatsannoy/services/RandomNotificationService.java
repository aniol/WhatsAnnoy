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
package cat.wuyingren.whatsannoy.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.widget.Toast;

import java.util.Calendar;
import java.util.Random;

import cat.wuyingren.whatsannoy.profiles.Schedule;
import cat.wuyingren.whatsannoy.utils.Alarm;

/**
 * @author Jordi López <wuyingren>
 */
public class RandomNotificationService extends Service {

    public static final String BROADCAST_ACTION = "cat.wuyingren.broadcast.notificationcreated";

    private boolean isRunning = false;
    private int frequency = 0;
    private Alarm a = null;
    private Calendar c = null;
    private Schedule s = new Schedule();
    private SharedPreferences prefs;

    private boolean unset = true;
    private final Handler handler = new Handler();
    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            if(unset) {
                setUIInfo();
                //handler.postDelayed(this, 5000); // 5 seconds
                handler.postDelayed(this,2000);
                unset=false;
            }

        }
    };

    Intent intent;
    public static final String ARG_FREQUENCY = "frequency";

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        handler.removeCallbacks(sendUpdatesToUI);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Toast.makeText(this, "Start Service", Toast.LENGTH_SHORT).show();

        isRunning = true;
        Bundle args = intent.getExtras();
        frequency = args.getInt(ARG_FREQUENCY);
        if(frequency<5) {
            frequency = 5;
        }
        Log.w("TAG", "frequency set to " + frequency);

        Thread bkgThread = new Thread(new BackgroundThread());
        bkgThread.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        isRunning = false;
        if(a!=null) {
            a.cancelAlarm(getBaseContext(), -1);
        }
        Toast.makeText(this, "Stop Service", Toast.LENGTH_SHORT).show();
        //handler.postDelayed(sendUpdatesToUI, 1000);

    }

    private void setUIInfo() {
        Log.w("TAG", "Setting UI info");
        Bundle b = new Bundle();
        b.putLong("date", c.getTimeInMillis());
        b.putBoolean("isRunning", isRunning);
        intent.putExtras(b);
        sendBroadcast(intent);
    }

    private class BackgroundThread implements Runnable {

        @Override
        public void run() {

            try {
                while(isRunning) {
                    if(a==null) {

                        Random r = new Random();
                        c = Calendar.getInstance();
                        s.setIsEnabled(true);

                        a = new Alarm();
                        int addMin = r.nextInt(frequency);
                        Log.w("TAG", "adding " + addMin + " minutes");
                        c.add(Calendar.MINUTE, addMin);
                        c.set(Calendar.SECOND, 0);
                        s.setDate(c.getTimeInMillis());
                        Log.w("TAG", "date " + c.getTime());
                        s.setId(-1);
                        //s.setRingtone(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString());
                        String uri = prefs.getString("pref_general_sound_key", "");
                        s.setRingtone(uri);

                        a.setAlarm(getBaseContext(), s);
                        unset=true;
                        handler.postDelayed(sendUpdatesToUI,1000);
                    }
                    else {
                        if (Calendar.getInstance().getTimeInMillis() > c.getTimeInMillis()) {
                            a=null;
                        }
                    }
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}

