package com.veeville.farm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.veeville.farm.R;
import com.veeville.farm.farmer.FarmerHelperClasses.FarmProfile;

import java.util.List;

/**
 * Created by Prashant C on 18/10/18.
 */
public class FarmProfilesAdapter extends RecyclerView.Adapter<FarmProfilesAdapter.SingleFarmProfileHolder> {


    Context context;
    private List<FarmProfile> objects;

    public FarmProfilesAdapter(Context context, List<FarmProfile> objects) {
        this.objects = objects;
        this.context = context;
    }


    @NonNull
    @Override
    public SingleFarmProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm_profile_card, parent, false);
        return new SingleFarmProfileHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleFarmProfileHolder holder, int position) {
        FarmProfile profile = objects.get(position);
        holder.farmName.setText(profile.farmName);
        holder.cropName.setText(profile.farmCrop);
        holder.yield.setText(profile.yield);
        holder.cropStatus.setText(profile.cropStatus);
        Glide.with(context).load(profile.farmPicture).into(holder.cropImage);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    class SingleFarmProfileHolder extends RecyclerView.ViewHolder {
        TextView farmName, cropName, cropStatus, yield;
        ImageView cropImage;
        SingleFarmProfileHolder(View view) {
            super(view);
            farmName = view.findViewById(R.id.farm_name);
            cropName = view.findViewById(R.id.crop_name);
            cropStatus = view.findViewById(R.id.crop_status);
            yield = view.findViewById(R.id.crop_yield);
            cropImage = view.findViewById(R.id.crop_icon);

        }
    }
}
