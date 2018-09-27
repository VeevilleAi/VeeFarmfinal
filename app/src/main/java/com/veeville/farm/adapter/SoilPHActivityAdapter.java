package com.veeville.farm.adapter;

import android.support.annotation.NonNull;
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
public class SoilPHActivityAdapter extends RecyclerView.Adapter<SoilPHActivityAdapter.SoilpHCardHolder> {

    private List<DashBoardDataClasses.SoilPH> soilPHList;

    public SoilPHActivityAdapter(List<DashBoardDataClasses.SoilPH> soilPHList) {
        this.soilPHList = soilPHList;
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
    public SoilpHCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_ph_first_card, parent, false);
            return new SoilpHCardHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_ph_remaining_cards, parent, false);
            return new SoilpHCardHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SoilpHCardHolder holder, int position) {

        holder.date.setText(soilPHList.get(position).month);
        holder.place.setText(soilPHList.get(position).place);
        holder.value.setText(soilPHList.get(position).value);

    }

    @Override
    public int getItemCount() {
        return soilPHList.size();
    }


    class SoilpHCardHolder extends RecyclerView.ViewHolder {
        TextView date, place, value;

        SoilpHCardHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            place = view.findViewById(R.id.place);
            value = view.findViewById(R.id.value);
        }
    }
}
