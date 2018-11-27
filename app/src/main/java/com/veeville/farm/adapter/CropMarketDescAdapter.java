package com.veeville.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.veeville.farm.R;
import com.veeville.farm.activity.PriceActivity;
import com.veeville.farm.farmer.FarmerHelperClasses.CropMarketplace;
import com.veeville.farm.helper.AppSingletonClass;

import java.util.List;

/**
 * Created by Prashant C on 25/10/18.
 */
public class CropMarketDescAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> objects;
    private Context context;
    private final String TAG = CropMarketDescAdapter.class.getSimpleName();

    public CropMarketDescAdapter(Context context, List<Object> objects) {
        this.objects = objects;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (objects.get(position) instanceof CropMarketplace.CropMarketPlaceDescImage) {
            return 0;
        } else if (objects.get(position) instanceof CropMarketplace.CropMarketPlaceCropDesc) {
            return 1;
        } else if (objects.get(position) instanceof CropMarketplace.CropMarketPlacePrice) {
            return 2;
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
                View view = LayoutInflater.from(context).inflate(R.layout.crop_market_desc_first_card, parent, false);
                holder = new CropImageHolder(view);
                break;
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.crop_market_desc_crop_desc_card, parent, false);
                holder = new CropDescHolder(view);
                break;
            case 2:
                view = LayoutInflater.from(context).inflate(R.layout.crop_market_market_price_card, parent, false);
                holder = new CropMarketPriceHolder(view);
                break;

            default:
                view = LayoutInflater.from(context).inflate(R.layout.crop_market_market_price_card, parent, false);
                holder = new CropMarketPriceHolder(view);
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
                break;
            case 2:
                handlePriceList((CropMarketPriceHolder) holder, position);
                break;
        }
    }


    private void handlePriceList(CropMarketPriceHolder holder, int position) {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(manager);
        CropMarketplace.CropMarketPlacePrice cropMarketPlacePrice = (CropMarketplace.CropMarketPlacePrice) objects.get(position);
        PriceListAdapter adapter = new PriceListAdapter(cropMarketPlacePrice);
        holder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        logMessage("size:" + objects.size());
        return objects.size();
    }

    private class CropImageHolder extends RecyclerView.ViewHolder {
        CropImageHolder(View view) {
            super(view);
        }
    }

    private class CropDescHolder extends RecyclerView.ViewHolder {
        CropDescHolder(View view) {
            super(view);
        }
    }

    private class CropMarketPriceHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        CropMarketPriceHolder(View view) {
            super(view);
            recyclerView = view.findViewById(R.id.recyclerview);
        }
    }

    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }

    private class PriceListAdapter extends RecyclerView.Adapter<PriceListAdapter.SinglePriceHolder> {
        CropMarketplace.CropMarketPlacePrice singleMarketPlacePrices;

        PriceListAdapter(CropMarketplace.CropMarketPlacePrice singleMarketPlacePrice) {
            this.singleMarketPlacePrices = singleMarketPlacePrice;
        }

        @NonNull
        @Override
        public SinglePriceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_place_price_list_single_element_card, parent, false);
            return new SinglePriceHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SinglePriceHolder holder, int position) {
            final CropMarketplace.CropMarketPlacePrice.SingleMarketPlacePrices prices = singleMarketPlacePrices.list.get(position);
            holder.price.setText(prices.price);
            holder.distance.setText(prices.distance);
            holder.place.setText(prices.place);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PriceActivity.class);
                    intent.putExtra("city", prices.place);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return singleMarketPlacePrices.list.size();
        }

        class SinglePriceHolder extends RecyclerView.ViewHolder {

            TextView place, distance, price;
            CardView cardView;

            SinglePriceHolder(View view) {
                super(view);
                cardView = view.findViewById(R.id.cardview);
                place = view.findViewById(R.id.place);
                distance = view.findViewById(R.id.distance);
                price = view.findViewById(R.id.price);

            }
        }

    }
}
