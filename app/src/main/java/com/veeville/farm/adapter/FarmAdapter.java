package com.veeville.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.veeville.farm.R;
import com.veeville.farm.farmer.FarmDescriptionActivity;
import com.veeville.farm.farmer.FarmerHelperClasses.Farm;
import com.veeville.farm.helper.AppSingletonClass;

import java.util.List;

/**
 * Created by Prashant C on 11/10/18.
 * adapter for farms
 *
 */
public class FarmAdapter extends RecyclerView.Adapter<FarmAdapter.SingleFarmHolder> {

    private List<Farm> farms;
    private Context context;
    private final String TAG = FarmAdapter.class.getSimpleName();

    public FarmAdapter(List<Farm> farms, Context context) {
        this.farms = farms;
        this.context = context;
    }

    @NonNull
    @Override
    public SingleFarmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.farmers_farm_dashboard_card, parent, false);
        return new SingleFarmHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleFarmHolder holder, int position) {
        final Farm farm = farms.get(position);
        holder.cropName.setText(farm.cropName);
        holder.farmName.setText(farm.farmName);
        holder.farmStatus.setText(farm.farmStatus);
        Glide.with(context).load(farm.cropImageLink).into(holder.farmImage);
        holder.farmCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FarmDescriptionActivity.class);
                intent.putExtra("farmName", farm.farmName);
                intent.putExtra("cropName", farm.cropName);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        logMessage("size;" + farms.size());
        return farms.size();
    }

    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }

    class SingleFarmHolder extends RecyclerView.ViewHolder {
        CardView farmCard;
        ImageView farmImage;
        TextView farmName, farmStatus, cropName;

        SingleFarmHolder(View view) {
            super(view);
            farmCard = view.findViewById(R.id.farm_id);
            farmImage = view.findViewById(R.id.farm_image);
            farmName = view.findViewById(R.id.farm_name);
            farmStatus = view.findViewById(R.id.farm_status);
            cropName = view.findViewById(R.id.crop_name);
        }
    }

}
