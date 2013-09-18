package cat.wuyingren.whatsannoy.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.Random;

import cat.wuyingren.whatsannoy.R;
import cat.wuyingren.whatsannoy.activities.MainActivity;
import cat.wuyingren.whatsannoy.profiles.Schedule;
import cat.wuyingren.whatsannoy.sql.ScheduleDataSource;

public class SystemUtils {

    public static int getSdkInt() {
        return Build.VERSION.SDK_INT;
    }

    public static int createScheduleNotification(Context context, Schedule s) {

        ScheduleDataSource dataSource;

        dataSource = new ScheduleDataSource(context);
        dataSource.open();
        Random r = new Random();
        int mId = 0;// r.nextInt();
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Log.w("TAG", "ring - " + s.getRingtone());
        String uri = s.getRingtone(); // prefs.getString("pref_general_sound_key", "");
        Uri ringtone = Uri.parse(uri);
        if(uri.isEmpty()) {
            ringtone = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION);
        }
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setContentText(context.getResources().getString(R.string.notification));
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mBuilder.setSound(ringtone);
        //mBuilder.setWhen(s.getDate());
        mNotificationManager.notify(mId, mBuilder.build());
        s.setIsEnabled(false);
        dataSource.updateSchedule(context, s);
        dataSource.close();
        return mId;
    }

    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
}
