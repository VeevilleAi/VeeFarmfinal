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
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.Fruit;

import java.util.List;

/**
 * Created by Prashant C on 04/09/18.
 */
public class VegPriceAdapter extends RecyclerView.Adapter<VegPriceAdapter.PriceHolder> {


    private List<Fruit> fruits;
    private Context context;
    private final String TAG = VegPriceAdapter.class.getSimpleName();

    public VegPriceAdapter(List<Fruit> fruits, Context context) {
        this.fruits = fruits;
        this.context = context;
    }

    @NonNull
    @Override
    public PriceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.price_singlecard_layout, parent, false);
        return new PriceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceHolder holder, int position) {

        Fruit fruit = fruits.get(position);
        String desc = fruit.name + fruit.pieceOrKg;
        holder.name.setText(desc);
        holder.price.setText(fruit.price);
        Glide.with(context).load(fruit.imageLink).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        logMessage("size:" + fruits.size());
        return fruits.size();
    }

    class PriceHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView imageView;

        PriceHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            price = view.findViewById(R.id.price);
            imageView = view.findViewById(R.id.imageview);
        }
    }

    public void changeDataset(List<Fruit> fruitList) {
        fruits = fruitList;
        notifyDataSetChanged();
    }

    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }
}
