package cat.wuyingren.whatsannoy.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import cat.wuyingren.whatsannoy.profiles.Schedule;
import cat.wuyingren.whatsannoy.sql.ScheduleDataSource;

public class Alarm extends BroadcastReceiver {

    private Schedule s;

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        SystemUtils.createScheduleNotification(context, s);
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
        this.s = s;
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.set(AlarmManager.RTC_WAKEUP, s.getDate(),pi);
        //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 10, pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
