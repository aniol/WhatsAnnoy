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
package cat.wuyingren.whatsannoy.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import cat.wuyingren.whatsannoy.profiles.Schedule;
import cat.wuyingren.whatsannoy.sql.ScheduleDataSource;

/**
 * @author Jordi López <wuyingren>
 */
public class Alarm extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        Bundle b = intent.getExtras();
        long aID = b.getLong("alarmID");
        SharedPreferences preferences = context.getSharedPreferences("BRec_"+aID, Context.MODE_PRIVATE);
        long sID = preferences.getLong("pref_schedule_id", -1);
        if(sID!= -1) {
            ScheduleDataSource sDS = new ScheduleDataSource(context);
            sDS.open();
            Schedule s = sDS.getScheduleByID(sID);
            SystemUtils.createScheduleNotification(context, s);
            s.setIsEnabled(false);
            sDS.updateSchedule(context, s);
            sDS.close();
        }
        else {
            b.setClassLoader(Schedule.class.getClassLoader());
            Schedule s = (Schedule)b.getParcelable("schedule");
            SystemUtils.createScheduleNotification(context, s);
        }
        /*if(s!=null) {
            ScheduleDataSource sDS = new ScheduleDataSource(context);
            sDS.open();
            sDS.deleteSchedule(s);
            sDS.close();
        }*/

        wl.release();
    }


    public void setAlarm(Context context, Schedule s)
    {
        SharedPreferences preferences = context.getSharedPreferences("BRec_"+ s.getId(),Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putLong("pref_schedule_id", s.getId());
        edit.commit();

        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        Bundle b = new Bundle();
        b.putLong("alarmID", s.getId());
        b.putParcelable("schedule", s);
        i.putExtras(b);

        PendingIntent pi = PendingIntent.getBroadcast(context, SystemUtils.safeLongToInt(s.getId()) , i, PendingIntent.FLAG_CANCEL_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, s.getDate(),pi);
        //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 10, pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context, int sID)
    {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, sID, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
