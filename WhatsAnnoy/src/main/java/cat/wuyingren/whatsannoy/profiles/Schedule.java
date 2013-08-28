package cat.wuyingren.whatsannoy.profiles;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Schedule implements Parcelable {

    private long id;
    private long date;

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

        dest.writeLong(this.date);
        dest.writeLong(this.id);

    }

    private void readFromParcel(Parcel in) {

        id = in.readLong();
        date = in.readLong();

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
