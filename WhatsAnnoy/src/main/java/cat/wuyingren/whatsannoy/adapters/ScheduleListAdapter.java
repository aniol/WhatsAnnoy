package cat.wuyingren.whatsannoy.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.ArrayAdapter;
import org.holoeverywhere.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cat.wuyingren.whatsannoy.R;
import cat.wuyingren.whatsannoy.profiles.Schedule;
import cat.wuyingren.whatsannoy.sql.ScheduleDataSource;


public class ScheduleListAdapter extends ArrayAdapter<Schedule> {
    private final Context context;
    private List<Schedule> values;
    private final ScheduleDataSource dataSource;

    public ScheduleListAdapter(Context context, List<Schedule> values, ScheduleDataSource dataSource) {
        //super(context, R.layout.schedulelist_row_layout, values);
        super(context, R.layout.simple_list_item_1, values);
        this.context = context;
        this.values = values;
        this.dataSource = dataSource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.schedulelist_row_layout, parent, false);
        TextView tView = (TextView) rowView.findViewById(R.id.textView);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");

        Date resultdate = new Date(values.get(position).getDate());
        tView.setText(sdf.format(resultdate));

        TextView tView2 = (TextView) rowView.findViewById(R.id.textView2);
        tView2.setText(sdf2.format(resultdate));
/*
        final Schedule s = values.get(position);
        ImageButton b2 = (ImageButton) rowView.findViewById(R.id.Button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.deleteSchedule(s);
            }
        });
*/
        return rowView;
    }

    public void updateSchedules(List<Schedule> schedules) {
        this.values.clear();
        this.values.addAll(schedules);
        notifyDataSetChanged();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }
}
