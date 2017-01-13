package zt.sakoonkinamaz;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.List;

public class Adapter extends ArrayAdapter<Bean> {

    private Context context;
    private List<Bean> bean;

    public Adapter(Context c, List<Bean> b) {
        super(c, R.layout.list_item, b);
        this.context = c;
        this.bean = b;
    }

    private class Holder {
        private TextView name;
        private TextView startTime;
        private TextView endTime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Holder holder = new Holder();
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
            holder.name = (TextView) v.findViewById(R.id.namaz);
            holder.startTime = (TextView) v.findViewById(R.id.start_time);
            holder.endTime = (TextView) v.findViewById(R.id.end_time);

            v.setTag(holder);
        } else {
            holder = (Holder) v.getTag();
        }

        final Bean item = getItem(position);

        if (item != null) {
            holder.name.setText(item.getName());
            holder.startTime.setText(PublicClass.LongToString(item.getStartTime()));
            holder.endTime.setText(PublicClass.LongToString(item.getEndTime()));
        }
        holder.startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        long l = PublicClass.IntToLong(selectedHour, selectedMinute);
                        if(item != null) item.setStartTime(l);
                        ((TextView) v).setText(PublicClass.LongToString(l));
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        holder.endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        long l = PublicClass.IntToLong(selectedHour, selectedMinute);
                        if(item != null) item.setEndTime(l);
                        ((TextView) v).setText(PublicClass.LongToString(l));
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        return v;
    }

    @Override
    public int getCount() {
        return bean.size();
    }

    @Override
    public Bean getItem(int position) {
        return bean.get(position);
    }

}
