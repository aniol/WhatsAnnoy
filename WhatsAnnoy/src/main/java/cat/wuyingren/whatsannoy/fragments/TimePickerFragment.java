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
package cat.wuyingren.whatsannoy.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.text.format.DateFormat;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.Dialog;
import org.holoeverywhere.app.DialogFragment;
import org.holoeverywhere.app.TimePickerDialog;
import org.holoeverywhere.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import cat.wuyingren.whatsannoy.profiles.Schedule;
import cat.wuyingren.whatsannoy.sql.ScheduleDataSource;

/**
 * @author Jordi López (wuyingren)
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener, DialogInterface.OnDismissListener{


    public static final String ARG_SCHEDULE = "schedule";
    public static final String ARG_UPDATE = "update";
    private ScheduleDataSource dataSource;
    private OnDBChangedListener mCallback;
    private Context context;

    private Schedule s;
    private Bundle args;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();

        context = getActivity();

        if(args!=null) {
            s = args.getParcelable(ARG_SCHEDULE);
        }

        if(s!=null) {
            c.setTimeInMillis(s.getDate());
        }
        else {
            c.add(Calendar.MINUTE, 5);  // add 5 minutes
        }
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        dataSource = new ScheduleDataSource(getActivity());
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        if(c.getTime().before(now)) { // If time selected is before now, set it to tomorrow
            c.add(Calendar.DAY_OF_YEAR,1);
        }

//        if(!args.getBoolean(ARG_UPDATE)) { // if creating new schedule
            //c.add(Calendar.MINUTE, 5); // add 5 minutes
       // }

        long date = c.getTimeInMillis();
        //Schedule schedule = new Schedule();
        //schedule = dataSource.createSchedule(date);
        dataSource.open();
        if(s!=null) {
            s.setDate(date);
            dataSource.updateSchedule(context, s);
        }
        else {
            dataSource.createSchedule(date, RingtoneManager.getActualDefaultRingtoneUri(getSupportActivity(), RingtoneManager.TYPE_NOTIFICATION).toString(),
                    1, getSupportActivity());
        }
        dataSource.close();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mCallback.onDBChanged();
        super.onDismiss(dialog);
    }


    // Container Activity must implement this interface
    public interface OnDBChangedListener {
        public void onDBChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnDBChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDBChangedListener");
        }
    }
}
