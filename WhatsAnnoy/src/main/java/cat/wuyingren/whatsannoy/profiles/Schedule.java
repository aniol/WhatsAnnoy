package cat.wuyingren.whatsannoy.profiles;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Schedule {

    private long id;
    private long date;

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
}
