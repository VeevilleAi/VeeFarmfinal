package com.veeville.farm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.veeville.farm.R;
import com.veeville.farm.helper.AppSingletonClass;

import java.util.List;

/**
 * Created by Prashant C on 21/11/18.
 */
class WeatherTimeWithTempAdapter extends RecyclerView.Adapter<WeatherTimeWithTempAdapter.ThreeHourHolder> {
    private final String TAG = WeatherTimeWithTempAdapter.class.getSimpleName();
    private List<List<String>> lists;
    private Context context;

    WeatherTimeWithTempAdapter(List<List<String>> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public ThreeHourHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_time_value_card, parent, false);
        return new ThreeHourHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreeHourHolder holder, int position) {
        holder.time.setText(lists.get(position).get(0));
        holder.temp.setText(lists.get(position).get(1));
    }

    @Override
    public int getItemCount() {
        logMessage("Size:" + lists.size());
        return lists.size();
    }

    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }

    class ThreeHourHolder extends RecyclerView.ViewHolder {
        TextView time, temp;

        ThreeHourHolder(View view) {
            super(view);
            time = view.findViewById(R.id.time);
            temp = view.findViewById(R.id.temperature);
        }
    }
}