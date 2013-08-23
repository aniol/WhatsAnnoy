package cat.wuyingren.whatsannoy.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.Dialog;
import org.holoeverywhere.app.DialogFragment;
import org.holoeverywhere.app.TimePickerDialog;
import org.holoeverywhere.widget.TimePicker;

import java.util.Calendar;

import cat.wuyingren.whatsannoy.sql.ScheduleDataSource;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener, DialogInterface.OnDismissListener{


    private ScheduleDataSource dataSource;
    private OnDBChangedListener mCallback;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
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
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);


        long date = c.getTimeInMillis();
        //Schedule schedule = new Schedule();
        //schedule = dataSource.createSchedule(date);
        dataSource.open();
        dataSource.createSchedule(date);
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
