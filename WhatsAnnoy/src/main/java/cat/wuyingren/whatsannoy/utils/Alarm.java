package cat.wuyingren.whatsannoy.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import cat.wuyingren.whatsannoy.profiles.Schedule;
import cat.wuyingren.whatsannoy.sql.ScheduleDataSource;

public class Alarm extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        Log.w("TAG", "Context "+ context.toString());
        Bundle b = intent.getExtras();
        long aID = b.getLong("alarmID");
        Log.w("TAG", "w " + aID);
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

        Log.w("TAG", "Context "+ context.toString());
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        Bundle b = new Bundle();
        Log.w("TAG", "r " + s.getId());
        b.putLong("alarmID", s.getId());
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
