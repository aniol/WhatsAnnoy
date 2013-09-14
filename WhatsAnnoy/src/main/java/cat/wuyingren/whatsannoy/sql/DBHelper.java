package cat.wuyingren.whatsannoy.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "schedules.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + Constants.TABLE_SCHEDULE + "(" + Constants._ID
            + " integer primary key autoincrement, " + Constants.SCHEDULE_DATE
            + " bigint not null, " + Constants.SCHEDULE_RINGTONE + " integer, "
            + Constants.SCHEDULE_ENABLED + " integer not null);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_SCHEDULE);
        onCreate(db);
    }
}
