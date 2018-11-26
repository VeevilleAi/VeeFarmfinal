package com.veeville.farm.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.veeville.farm.R;
import com.veeville.farm.farmer.FarmerHelperClasses.FarmProfile;
import com.veeville.farm.farmer.FarmerHelperClasses.FarmerProfile;
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.CircleTransform;

import java.util.List;

/**
 * Created by Prashant C on 18/10/18.
 */
public class FarmProfilesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Object> objects;
    private final String TAG = FarmProfilesAdapter.class.getSimpleName();

    public FarmProfilesAdapter(List<Object> objects) {
        this.objects = objects;
    }

    @Override
    public int getItemViewType(int position) {
        Object object = objects.get(position);
        if (object instanceof FarmerProfile) {
            return 0;
        } else if (object instanceof FarmProfile) {
            return 1;
        } else {
            return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case 0:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.famer_profile_card, parent, false);
                holder = new SingleFarmerProfileHolder(view);
                break;
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm_profile_card, parent, false);
                holder = new SingleFarmProfileHolder(view);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm_profile_card, parent, false);
                holder = new SingleFarmProfileHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                break;
            case 1:
                handleFarmProfile((SingleFarmProfileHolder) holder, position);
                break;
        }
    }


    private void handleFarmProfile(SingleFarmProfileHolder holder, int position) {
        logMessage("" + holder + position);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    private class SingleFarmerProfileHolder extends RecyclerView.ViewHolder {
        ImageView profilePic;
        String imgUrl = "https://wle.cgiar.org/sites/default/files/header%20images/Vietnamese%20farmer%20header%20format.jpg";

        SingleFarmerProfileHolder(View view) {
            super(view);
            profilePic = view.findViewById(R.id.profile_image);
            Picasso.with(view.getContext()).load(imgUrl).resize(500, 500).centerCrop().transform(new CircleTransform()).into(profilePic);
        }
    }

    private class SingleFarmProfileHolder extends RecyclerView.ViewHolder {
        SingleFarmProfileHolder(View view) {
            super(view);


        }
    }

    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }
}
