package com.veeville.farm.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.veeville.farm.R;
import com.veeville.farm.helper.ChatmessageDataClasses;

import java.util.List;

/**
 * Created by Prashant C on 21/11/18.
 */

class LightTimeWithValuesAdapter extends RecyclerView.Adapter<LightTimeWithValuesAdapter.SingleValueHolder> {
    private List<ChatmessageDataClasses.Light.LightValues> lightValues;

    LightTimeWithValuesAdapter(List<ChatmessageDataClasses.Light.LightValues> lightValues) {
        this.lightValues = lightValues;
    }

    @NonNull
    @Override
    public SingleValueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_moisture_time_value, parent, false);
        return new SingleValueHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleValueHolder holder, int position) {
        holder.value.setText(lightValues.get(position).value1);
        holder.time.setText(lightValues.get(position).date);

    }

    @Override
    public int getItemCount() {
        return lightValues.size();
    }

    class SingleValueHolder extends RecyclerView.ViewHolder {
        TextView time, value;

        SingleValueHolder(View view) {
            super(view);
            time = view.findViewById(R.id.time);
            value = view.findViewById(R.id.value);
        }
    }
}