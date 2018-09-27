package com.veeville.farm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.veeville.farm.R;
import com.veeville.farm.helper.DashBoardDataClasses;

import java.util.List;

/**
 * Created by Prashant C on 04/07/18.
 */

public class TemperatureActivityAdapter extends RecyclerView.Adapter<TemperatureActivityAdapter.TemperatureCardHolder> {

    private Context context;
    private List<DashBoardDataClasses.TemperatureData> temperatureDataList;

    public TemperatureActivityAdapter(Context context, List<DashBoardDataClasses.TemperatureData> temperatureDataList) {
        this.context = context;
        this.temperatureDataList = temperatureDataList;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return 0;
        } else
            return 1;
    }

    @NonNull
    @Override
    public TemperatureCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.temperature_activity_first_card, parent, false);
            return new TemperatureCardHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.temperature_activity_remaining_cards, parent, false);
            return new TemperatureCardHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull TemperatureCardHolder holder, int position) {

        holder.date.setText(temperatureDataList.get(position).month);
        holder.place.setText(temperatureDataList.get(position).place);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        ValuesAdapter adapter = new ValuesAdapter(temperatureDataList.get(position).soilMoistureValues);
        holder.valuesRecyclerview.setLayoutManager(manager);
        holder.valuesRecyclerview.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return temperatureDataList.size();
    }

    class TemperatureCardHolder extends RecyclerView.ViewHolder {
        RecyclerView valuesRecyclerview;
        TextView date, place;

        TemperatureCardHolder(View view) {
            super(view);
            valuesRecyclerview = view.findViewById(R.id.recyclerview_values);
            date = view.findViewById(R.id.date);
            place = view.findViewById(R.id.place);
        }
    }


    class ValuesAdapter extends RecyclerView.Adapter<ValuesAdapter.SingleValueHolder> {
        List<DashBoardDataClasses.TemperatureData.TempValue> tempValues;

        ValuesAdapter(List<DashBoardDataClasses.TemperatureData.TempValue> list) {
            this.tempValues = list;
        }

        @NonNull
        @Override
        public SingleValueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_moisture_time_value, parent, false);
            return new SingleValueHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SingleValueHolder holder, int position) {

            holder.value.setText(tempValues.get(position).value1);
            holder.time.setText(tempValues.get(position).date);
        }

        @Override
        public int getItemCount() {
            return tempValues.size();
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
}