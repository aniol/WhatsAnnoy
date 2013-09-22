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
package cat.wuyingren.whatsannoy.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cat.wuyingren.whatsannoy.profiles.Schedule;
import cat.wuyingren.whatsannoy.utils.Alarm;
import cat.wuyingren.whatsannoy.utils.SystemUtils;

/**
 * @author Jordi López <wuyingren>
 */
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
        if(enabled==1) {
            Alarm alarm = new Alarm();
            alarm.setAlarm(context, schedule);
        }
        return schedule;
    }

    public void updateSchedule(Context context, Schedule s) {
        long id = s.getId();
        long date = s.getDate();
        String ringtone = s.getRingtone();
        int enabled = s.getEnabled();
        ContentValues values = new ContentValues();
        values.put(Constants.SCHEDULE_DATE, date);
        values.put(Constants.SCHEDULE_RINGTONE, ringtone);
        values.put(Constants.SCHEDULE_ENABLED, enabled);
        database.update(Constants.TABLE_SCHEDULE, values, Constants._ID + "=?", new String[]{String.valueOf(id)});

        if(s.isEnabled()) {
            //Reset alarm
            Alarm alarm = new Alarm();
            alarm.cancelAlarm(context, SystemUtils.safeLongToInt(s.getId()));
            alarm.setAlarm(context, s);
        }
    }

    public void deleteSchedule(Schedule schedule) {
        long id = schedule.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(Constants.TABLE_SCHEDULE, Constants._ID
                + " = " + id, null);
    }

    public Schedule getScheduleByID(long id) {
        Schedule s;
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
