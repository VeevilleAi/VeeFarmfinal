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

import com.bumptech.glide.Glide;
import com.veeville.farm.R;
import com.veeville.farm.farmer.FarmDescriptionActivity;
import com.veeville.farm.helper.Farm;

import java.util.List;

/**
 * Created by Prashant C on 11/10/18.
 */
public class FarmAdapter extends RecyclerView.Adapter<FarmAdapter.SingleFarmHolder> {

    private List<List<Farm>> names;
    private Context context;

    public FarmAdapter(List<List<Farm>> names, Context context) {
        this.names = names;
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

        holder.farmCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FarmDescriptionActivity.class);
                context.startActivity(intent);
            }
        });
        holder.farmCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FarmDescriptionActivity.class);
                context.startActivity(intent);
            }
        });
        Glide.with(context).load("http://www.smarknews.it/press/wp-content/uploads/2014/05/Banana-1-.jpg").into(holder.farmImage);
        Glide.with(context).load("https://images.pexels.com/photos/102104/pexels-photo-102104.jpeg?auto=compress&cs=tinysrgb&h=350").into(holder.farmImage2);

    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    class SingleFarmHolder extends RecyclerView.ViewHolder {
        CardView farmCard1, farmCard2;
        ImageView farmImage, farmImage2;

        SingleFarmHolder(View view) {
            super(view);
            farmCard1 = view.findViewById(R.id.farm_id1);
            farmCard2 = view.findViewById(R.id.farm_id2);
            farmImage = view.findViewById(R.id.farm_image1);
            farmImage2 = view.findViewById(R.id.farm_image2);
        }
    }
}
