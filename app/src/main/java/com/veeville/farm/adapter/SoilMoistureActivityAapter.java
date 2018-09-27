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
public class SoilMoistureActivityAapter extends RecyclerView.Adapter<SoilMoistureActivityAapter.SoilMoistureCardHolder> {

    private Context context;
    private List<DashBoardDataClasses.SoilMoistureData> dataList;

    public SoilMoistureActivityAapter(Context context, List<DashBoardDataClasses.SoilMoistureData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        else
            return 1;
    }

    @NonNull
    @Override
    public SoilMoistureCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_moisture_activity_first_card, parent, false);
            return new SoilMoistureCardHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_moisture_activity_remaining_cards, parent, false);
            return new SoilMoistureCardHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SoilMoistureCardHolder holder, int position) {

        holder.date.setText(dataList.get(position).month);
        holder.place.setText(dataList.get(position).place);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        ValuesAdapter adapter = new ValuesAdapter(dataList.get(position).soilMoistureValues);
        holder.valuesRecyclerview.setLayoutManager(manager);
        holder.valuesRecyclerview.setAdapter(adapter);
        holder.valuesRecyclerview.scrollToPosition(5);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class SoilMoistureCardHolder extends RecyclerView.ViewHolder {
        RecyclerView valuesRecyclerview;
        TextView date, place;

        SoilMoistureCardHolder(View view) {
            super(view);
            valuesRecyclerview = view.findViewById(R.id.recyclerview_values);
            date = view.findViewById(R.id.date);
            place = view.findViewById(R.id.place);
        }
    }


    class ValuesAdapter extends RecyclerView.Adapter<ValuesAdapter.SingleValueHolder> {
        List<DashBoardDataClasses.SoilMoistureData.SoilMoistureValues> soilMoistureValues;

        ValuesAdapter(List<DashBoardDataClasses.SoilMoistureData.SoilMoistureValues> soilMoistureValues) {
            this.soilMoistureValues = soilMoistureValues;
        }

        @NonNull
        @Override
        public SingleValueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_moisture_values_with_time_card, parent, false);
            return new SingleValueHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SingleValueHolder holder, int position) {
            holder.time.setText(soilMoistureValues.get(position).date);
            holder.value1.setText(soilMoistureValues.get(position).value1);
            holder.value2.setText(soilMoistureValues.get(position).value2);
        }

        @Override
        public int getItemCount() {
            return soilMoistureValues.size();
        }

        class SingleValueHolder extends RecyclerView.ViewHolder {
            TextView time, value1, value2;

            SingleValueHolder(View view) {
                super(view);
                time = view.findViewById(R.id.time);
                value1 = view.findViewById(R.id.value1);
                value2 = view.findViewById(R.id.value2);
            }
        }
    }
}

