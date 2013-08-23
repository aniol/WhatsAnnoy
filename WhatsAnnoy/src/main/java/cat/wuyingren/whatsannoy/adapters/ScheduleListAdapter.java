package cat.wuyingren.whatsannoy.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.ArrayAdapter;
import org.holoeverywhere.widget.TextView;

import java.util.List;

import cat.wuyingren.whatsannoy.R;
import cat.wuyingren.whatsannoy.profiles.Schedule;
import cat.wuyingren.whatsannoy.sql.ScheduleDataSource;


public class ScheduleListAdapter extends ArrayAdapter<Schedule> {
    private final Context context;
    private final List<Schedule> values;
    private final ScheduleDataSource dataSource;

    public ScheduleListAdapter(Context context, List<Schedule> values, ScheduleDataSource dataSource) {
        super(context, R.layout.schedulelist_row_layout, values);
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
        tView.setText(values.get(position).toString());

        final Schedule s = values.get(position);
        ImageButton b2 = (ImageButton) rowView.findViewById(R.id.Button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.open();
                dataSource.deleteSchedule(s);
                dataSource.close();
            }
        });

        return rowView;
    }
}
