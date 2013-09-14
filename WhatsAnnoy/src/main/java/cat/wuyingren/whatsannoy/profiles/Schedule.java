package cat.wuyingren.whatsannoy.profiles;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Schedule implements Parcelable {

    private long id;
    private long date;
    private int ringtone;
    private int enabled;

    public Schedule() {

    }

    public Schedule(Parcel in) {
        readFromParcel(in);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getRingtone() {
        return ringtone;
    }

    public void setRingtone(int ringtone) {
        this.ringtone = ringtone;
    }

    public boolean isEnabled() {
        if (enabled==0) {
            return false;
        }
        else {
            return true;
        }
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public void setIsEnabled(boolean enabled) {
        if(!enabled) {
            this.enabled = 0;
        }
        else {
            this.enabled = 1;
        }
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date resultdate = new Date(date);
        return sdf.format(resultdate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(this.enabled);
        dest.writeInt(this.ringtone);
        dest.writeLong(this.date);
        dest.writeLong(this.id);

    }

    private void readFromParcel(Parcel in) {

        this.enabled = in.readInt();
        this.ringtone = in.readInt();
        this.date = in.readLong();
        this.id = in.readLong();

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }

    };
}
