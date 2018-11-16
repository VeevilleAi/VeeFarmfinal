package com.veeville.farm.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.veeville.farm.R;
import com.veeville.farm.farmer.FarmerHelperClasses.Yield;

import java.util.List;

/**
 * Created by Prashant C on 18/10/18.
 */
public class AddFarmYieldAdapter extends RecyclerView.Adapter<AddFarmYieldAdapter.AddSingleYearYieldHolder> {


    private List<Yield> yields;

    public AddFarmYieldAdapter(List<Yield> yields) {
        this.yields = yields;
    }

    @NonNull
    @Override
    public AddSingleYearYieldHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_previous_year_yield_card, parent, false);
        return new AddSingleYearYieldHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddSingleYearYieldHolder holder, int position) {
        Yield yield = yields.get(position);
        holder.year.setText(yield.year);
        holder.quantity.setText(yield.quantity);
        holder.name.setText(yield.name);
    }

    @Override
    public int getItemCount() {
        return yields.size();
    }

    class AddSingleYearYieldHolder extends RecyclerView.ViewHolder {
        TextView year, name, quantity;

        AddSingleYearYieldHolder(View view) {
            super(view);
            year = view.findViewById(R.id.year);
            name = view.findViewById(R.id.name);
            quantity = view.findViewById(R.id.quantity);
        }
    }
}
