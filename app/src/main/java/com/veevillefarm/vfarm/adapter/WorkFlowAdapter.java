package com.veevillefarm.vfarm.adapter;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.helper.AlaramReceiver;
import com.veevillefarm.vfarm.helper.AppSingletonClass;
import com.veevillefarm.vfarm.helper.DashBoardDataClasses;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Prashant C on 01/08/18.
 * adapter for workflow
 * here he can also set timer to alarm
 */
public class WorkFlowAdapter extends RecyclerView.Adapter<WorkFlowAdapter.SingleCardWorkHolder> {
    private Context context;
    private boolean wantTRepeat = false;
    private final String TAG = WorkFlowAdapter.class.getSimpleName();
    private List<DashBoardDataClasses.WorkFlowData> workFlowDataList;

    public WorkFlowAdapter(List<DashBoardDataClasses.WorkFlowData> workFlowDataList, Context context) {
        this.workFlowDataList = workFlowDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public SingleCardWorkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workflow_activity_outer_cards, parent, false);
        return new SingleCardWorkHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleCardWorkHolder holder, int position) {

        holder.title.setText(workFlowDataList.get(position).title);
        holder.date.setText(workFlowDataList.get(position).date);
        holder.add_alaram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlaram();
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(manager);
        SubAdapter adapter = new SubAdapter(workFlowDataList.get(position).subTitle, workFlowDataList.get(position).description);
        holder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return workFlowDataList.size();
    }

    class SingleCardWorkHolder extends RecyclerView.ViewHolder {
        TextView title, date;
        RecyclerView recyclerView;
        ImageView add_alaram;

        SingleCardWorkHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            date = view.findViewById(R.id.date);
            recyclerView = view.findViewById(R.id.recyclerview_to_do_things);
            add_alaram = view.findViewById(R.id.add_alaram);
        }
    }

    class SubAdapter extends RecyclerView.Adapter<SubAdapter.TitleNqHolder> {

        List<String> subTitles, descriptions;

        SubAdapter(List<String> subTitles, List<String> descriptions) {
            this.subTitles = subTitles;
            this.descriptions = descriptions;
        }

        @NonNull
        @Override
        public TitleNqHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workflow_activity_card, parent, false);
            return new TitleNqHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final TitleNqHolder holder, int position) {
            holder.checkBox.setText(subTitles.get(position));
            holder.more_info_text.setText(descriptions.get(position));
            holder.more_info_about_schedule_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.more_info_layout_hide.getVisibility() == View.VISIBLE) {
                        holder.more_info_layout_hide.setVisibility(View.GONE);
                    } else {
                        holder.more_info_layout_hide.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return subTitles.size();
        }

        class TitleNqHolder extends RecyclerView.ViewHolder {
            CheckBox checkBox;
            RelativeLayout more_info_about_schedule_task;
            TextView more_info_text;
            LinearLayout more_info_layout_hide;

            TitleNqHolder(View view) {
                super(view);
                checkBox = view.findViewById(R.id.checkbox_text_id);
                more_info_about_schedule_task = view.findViewById(R.id.more_info_about_schedule_task);
                more_info_text = view.findViewById(R.id.more_info_text);
                more_info_layout_hide = view.findViewById(R.id.more_info_layout_hide);
            }
        }
    }

    //set reminder for particular work of a farmer
    private void setAlaram() {

        wantTRepeat = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.alertdialog_for_setting_alarm, null);
        EditText datePicker = view.findViewById(R.id.date_picker);
        final EditText timePicker = view.findViewById(R.id.time_picker);
        Button cancel = view.findViewById(R.id.cancel_alarm);
        Button setAlarm = view.findViewById(R.id.set_alarm);
        CheckBox checkBoxRepeatDialy = view.findViewById(R.id.repeat_daily);
        checkBoxRepeatDialy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                wantTRepeat = b;

            }
        });
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final Calendar calendar = Calendar.getInstance();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

            }
        });
        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                AlarmManager alarmMgr;
                PendingIntent alarmIntent;
                calendar.set(Calendar.SECOND, 0);
                alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(context, AlaramReceiver.class);
                intent.putExtra("title", "Remainder");
                intent.putExtra("body", "spray water reminder");
                alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
                assert alarmMgr != null;
                logMessage(calendar.getTimeInMillis() + " alaram time ");
                alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
                Toast.makeText(context, "Reminder set ", Toast.LENGTH_SHORT).show();
                if (wantTRepeat) {
                    alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, alarmIntent);
                    Toast.makeText(context, "Reminder repeats daily", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final Calendar date = Calendar.getInstance();
        final int mYear = date.get(Calendar.YEAR);
        final int mMonth = date.get(Calendar.MONTH);
        final int mDay = date.get(Calendar.DAY_OF_MONTH);
        String dateString = mDay + "/" + mMonth + "/" + mYear;
        datePicker.setText(dateString);

        final Calendar time = Calendar.getInstance();
        final int mHour = time.get(Calendar.HOUR_OF_DAY);
        final int mMinute = time.get(Calendar.MINUTE);
        String timeString = mHour + ":" + mMinute;
        timePicker.setText(timeString);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.SECOND, 0);
                        logMessage("onDateSet: thank you");
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String time = hourOfDay + ":" + minute;
                                timePicker.setText(time);
                                calendar.set(Calendar.MINUTE, minute);
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

    }

    //should use this method to ebugg messages
    private void logMessage(String logMessage) {
        AppSingletonClass.logMessage(TAG, logMessage);
    }
}
