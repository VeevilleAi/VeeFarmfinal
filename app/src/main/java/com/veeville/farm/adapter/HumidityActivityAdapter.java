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
 * Created by Prashant C on 29/06/18.
 */
public class HumidityActivityAdapter extends RecyclerView.Adapter<HumidityActivityAdapter.HumidityCardHolder> {

    private Context context;
    private List<DashBoardDataClasses.HumidityData> humidityDataList;

    public HumidityActivityAdapter(Context context, List<DashBoardDataClasses.HumidityData> humidityDataList) {
        this.context = context;
        this.humidityDataList = humidityDataList;
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
    public HumidityCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.humidity_activity_first_card, parent, false);
            return new HumidityCardHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.humidity_activity_card, parent, false);
            return new HumidityCardHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull HumidityCardHolder holder, int position) {

        holder.place.setText(humidityDataList.get(position).place);
        holder.date.setText(humidityDataList.get(position).date);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        HumidityValuesAdapter adapter = new HumidityValuesAdapter(humidityDataList.get(position).humidityDataValues);
        holder.valuesRecyclerview.setLayoutManager(manager);
        holder.valuesRecyclerview.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return humidityDataList.size();
    }

    class HumidityCardHolder extends RecyclerView.ViewHolder {
        RecyclerView valuesRecyclerview;
        TextView date, place;

        HumidityCardHolder(View view) {
            super(view);
            valuesRecyclerview = view.findViewById(R.id.recyclerview_values);
            date = view.findViewById(R.id.date);
            place = view.findViewById(R.id.place);
        }
    }


    class HumidityValuesAdapter extends RecyclerView.Adapter<HumidityValuesAdapter.SingleValueHolder> {
        List<DashBoardDataClasses.HumidityData.HumidityDataValues> humidityDataValues;

        HumidityValuesAdapter(List<DashBoardDataClasses.HumidityData.HumidityDataValues> humidityDataValues) {
            this.humidityDataValues = humidityDataValues;
        }

        @NonNull
        @Override
        public SingleValueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.humidity_value_card_with_time, parent, false);
            return new SingleValueHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SingleValueHolder holder, int position) {
            DashBoardDataClasses.HumidityData.HumidityDataValues values = humidityDataValues.get(position);
            holder.time.setText(values.hour);
            holder.hAbsulute.setText(values.hAbsulute);
            holder.hRelative.setText(values.hRelative);
        }

        @Override
        public int getItemCount() {
            return humidityDataValues.size();
        }

        class SingleValueHolder extends RecyclerView.ViewHolder {
            TextView time, hRelative, hAbsulute;

            SingleValueHolder(View view) {
                super(view);
                time = view.findViewById(R.id.time_humidity);
                hRelative = view.findViewById(R.id.h_relative);
                hAbsulute = view.findViewById(R.id.h_absolute);
            }
        }
    }
}
