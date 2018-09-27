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

public class LightActivityAdapter extends RecyclerView.Adapter<LightActivityAdapter.LightCardHolder> {

    private Context context;
    private List<DashBoardDataClasses.LightData> lightDataList;

    public LightActivityAdapter(Context context, List<DashBoardDataClasses.LightData> lightDataList) {
        this.context = context;
        this.lightDataList = lightDataList;
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
    public LightCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.light_activity_first_card, parent, false);
            return new LightCardHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.light_activity_remaining_cards, parent, false);
            return new LightCardHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull LightCardHolder holder, int position) {

        holder.place.setText(lightDataList.get(position).place);
        holder.date.setText(lightDataList.get(position).month);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        ValuesAdapter adapter = new ValuesAdapter(lightDataList.get(position).soilMoistureValues);
        holder.valuesRecyclerview.setLayoutManager(manager);
        holder.valuesRecyclerview.setAdapter(adapter);
        holder.valuesRecyclerview.scrollToPosition(5);
    }

    @Override
    public int getItemCount() {
        return lightDataList.size();
    }

    class LightCardHolder extends RecyclerView.ViewHolder {
        RecyclerView valuesRecyclerview;
        TextView date, place;

        LightCardHolder(View view) {
            super(view);
            valuesRecyclerview = view.findViewById(R.id.recyclerview_values);
            date = view.findViewById(R.id.date);
            place = view.findViewById(R.id.place);
        }
    }


    class ValuesAdapter extends RecyclerView.Adapter<ValuesAdapter.SingleValueHolder> {
        List<DashBoardDataClasses.LightData.LightValues> lightValues;

        ValuesAdapter(List<DashBoardDataClasses.LightData.LightValues> lightValues) {
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
}
