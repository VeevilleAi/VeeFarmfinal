package com.veevillefarm.vfarm.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.farmer.CropMarketDescActivity;
import com.veevillefarm.vfarm.farmer.FarmerHelperClasses.MarketPlace;
import com.veevillefarm.vfarm.helper.AppSingletonClass;

import java.util.List;

/**
 * Created by Prashant C on 23/10/18.
 *
 * market adaptyer for market activity which card in DashBoardActivity
 * shows all crops grown by farmer
 */
public class MarketPlaceAdapter extends RecyclerView.Adapter<MarketPlaceAdapter.MarketPlaceHolder> {

    private List<MarketPlace> marketPlaces;
    private Context context;
    private final String TAG = MarketPlaceAdapter.class.getSimpleName();

    public MarketPlaceAdapter(Context context, List<MarketPlace> marketPlaces) {
        this.marketPlaces = marketPlaces;
        this.context = context;
    }

    @NonNull
    @Override
    public MarketPlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_place_card, parent, false);
        return new MarketPlaceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketPlaceHolder holder, final int temp_position) {
        final int position = holder.getAdapterPosition();
        final MarketPlace marketPlace = marketPlaces.get(position);
        Picasso.with(context).load(marketPlace.imgicon).resize(500, 500).into(holder.cropIcon);
        holder.cropName.setText(marketPlace.cropName);
        String totalMoney = marketPlace.totalMoney;
        holder.amount.setText(totalMoney);
        holder.yield.setText(marketPlace.cropYield);
        String currentPrice = marketPlace.currentPrice + "/kg";
        holder.price.setText(currentPrice);
        String farmerPrice = marketPlace.farmerPrice + "/kg";
        holder.farmerPrice.setText(farmerPrice);
        holder.description.setText(marketPlace.description);
        String place = "(" + marketPlace.city + ")";
        holder.cityName.setText(place);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogToEditPrice(position, marketPlace.currentPrice);
            }
        });
        holder.cropCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CropMarketDescActivity.class);
                intent.putExtra("cropName", marketPlace.cropName);
                intent.putExtra("cropImageLink", marketPlace.imgicon);
                context.startActivity(intent);
            }
        });
    }


    private void showDialogToEditPrice(final int position, String priceTemp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_to_edit_price_card, null);
        builder.setView(view);
        final AlertDialog dialog= builder.create();
        dialog.show();
        final EditText price = view.findViewById(R.id.edit_price);
        price.setText(priceTemp);
        Button cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button update = view.findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String farmerPrice = price.getText().toString();
                if (!farmerPrice.equals("")) {
                    MarketPlace place = marketPlaces.get(position);
                    MarketPlace marketPlace = new MarketPlace(place.imgicon, place.cropName, place.currentPrice, farmerPrice, place.city, place.cropYield, place.totalMoney, place.description);
                    marketPlaces.set(position, marketPlace);
                    notifyItemChanged(position);
                }
                dialog.cancel();

            }
        });
    }

    @Override
    public int getItemCount() {
        return marketPlaces.size();
    }

    class MarketPlaceHolder extends RecyclerView.ViewHolder {
        ImageView cropIcon, edit;
        CardView cropCardView;
        TextView cropName, price, yield, amount, description, cityName, farmerPrice;

        MarketPlaceHolder(View view) {
            super(view);
            cropIcon = view.findViewById(R.id.crop_icon);
            cropName = view.findViewById(R.id.crop_name);
            price = view.findViewById(R.id.crop_price);
            yield = view.findViewById(R.id.crop_yield);
            amount = view.findViewById(R.id.total_amount);
            description = view.findViewById(R.id.crop_description);
            cityName = view.findViewById(R.id.city);
            cropCardView = view.findViewById(R.id.cardview);
            edit = view.findViewById(R.id.edit);
            farmerPrice = view.findViewById(R.id.farmer_price);

        }
    }

    private void logMessage(String logMessage) {
        AppSingletonClass.logMessage(TAG, logMessage);
    }

    private void logErrorMessage(String logErrorMessage) {
        AppSingletonClass.logErrorMessage(TAG, logErrorMessage);
    }
}
