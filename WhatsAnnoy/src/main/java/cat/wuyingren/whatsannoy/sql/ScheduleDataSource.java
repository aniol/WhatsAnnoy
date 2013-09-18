package cat.wuyingren.whatsannoy.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cat.wuyingren.whatsannoy.profiles.Schedule;

public class ScheduleDataSource {

    // Database fields
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] allColumns = { Constants._ID, Constants.SCHEDULE_DATE, Constants.SCHEDULE_RINGTONE,
    Constants.SCHEDULE_ENABLED };

    public ScheduleDataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Schedule createSchedule(long date, String ringtone, int enabled, Context context) {
        ContentValues values = new ContentValues();
        values.put(Constants.SCHEDULE_DATE, date);
        values.put(Constants.SCHEDULE_RINGTONE, ringtone);
        values.put(Constants.SCHEDULE_ENABLED, enabled);
        long insertId = database.insert(Constants.TABLE_SCHEDULE, null,
                values);
        Cursor cursor = database.query(Constants.TABLE_SCHEDULE,
                allColumns, Constants._ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        Schedule schedule = cursorToSchedule(cursor);
        cursor.close();
        //SystemUtils.createScheduleNotification(context, schedule);
        /*Alarm alarm = new Alarm();
        alarm.setAlarm(context, schedule);*/
        return schedule;
    }

    public void updateSchedule(Schedule s) {
        long id = s.getId();
        long date = s.getDate();
        String ringtone = s.getRingtone();
        int enabled = s.getEnabled();
        ContentValues values = new ContentValues();
        values.put(Constants.SCHEDULE_DATE, date);
        values.put(Constants.SCHEDULE_RINGTONE, ringtone);
        values.put(Constants.SCHEDULE_ENABLED, enabled);
        database.update(Constants.TABLE_SCHEDULE, values, Constants._ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteSchedule(Schedule schedule) {
        long id = schedule.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(Constants.TABLE_SCHEDULE, Constants._ID
                + " = " + id, null);
    }

    public Schedule getScheduleByID(long id) {
        Schedule s=new Schedule();
        Cursor cursor = database.query(Constants.TABLE_SCHEDULE, allColumns, Constants._ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        //while(!cursor.isAfterLast()) {
            s = cursorToSchedule(cursor);
          //  cursor.moveToNext();
        //}
        return s;
    }

    public List<Schedule> getAllSchedules() {
        List<Schedule> schedules = new ArrayList<Schedule>();

        Cursor cursor = database.query(Constants.TABLE_SCHEDULE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Schedule schedule = cursorToSchedule(cursor);
            schedules.add(schedule);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return schedules;
    }

    private Schedule cursorToSchedule(Cursor cursor) {
        Schedule schedule = new Schedule();
        schedule.setId(cursor.getLong(0));
        schedule.setDate(cursor.getLong(1));
        schedule.setRingtone(cursor.getString(2));
        schedule.setEnabled(cursor.getInt(3));

        return schedule;
    }
}
