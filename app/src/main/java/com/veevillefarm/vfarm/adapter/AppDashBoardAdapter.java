package com.veevillefarm.vfarm.adapter;

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

import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.activity.ChatSectorActivity;
import com.veevillefarm.vfarm.activity.CropWorkFlow;
import com.veevillefarm.vfarm.activity.GovernmentSchemesActivity;
import com.veevillefarm.vfarm.activity.HumidityActivity;
import com.veevillefarm.vfarm.activity.LightActivity;
import com.veevillefarm.vfarm.activity.SoilMoistureActivity;
import com.veevillefarm.vfarm.activity.SoilPhActivity;
import com.veevillefarm.vfarm.activity.TemperatureActivity;
import com.veevillefarm.vfarm.activity.WeatherActivity;
import com.veevillefarm.vfarm.farmer.FarmsActivity;
import com.veevillefarm.vfarm.farmer.MarketPlaceActivity;
import com.veevillefarm.vfarm.helper.DashBoardClass;

import java.util.List;

/**
 * Created by Prashant C on 25/10/18.
 * adapter for DashBoard cards
 */
public class AppDashBoardAdapter extends RecyclerView.Adapter<AppDashBoardAdapter.SingleCardHolder> {

    private final String TAG = AppDashBoardAdapter.class.getSimpleName();
    private List<DashBoardClass> dashBoardClasses;
    private Context context;

    //intilize items like dashboard items and context
    public AppDashBoardAdapter(Context context, List<DashBoardClass> boardClasses) {
        this.dashBoardClasses = boardClasses;
        this.context = context;
    }

    //creating items view and adding to adapter
    @NonNull
    @Override
    public SingleCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_dashboard_card, parent, false);
        return new SingleCardHolder(view);
    }

    //setting data to each view
    @Override
    public void onBindViewHolder(@NonNull SingleCardHolder holder, int position) {
        final DashBoardClass aClass = dashBoardClasses.get(position);
        holder.title.setText(aClass.title);
        holder.subTitle.setText(aClass.subTitle);
        holder.dashboardIcon.setImageResource(aClass.icon);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveOnCardClick(aClass.cardType);
            }
        });
    }

    //based item selected starting that activity
    private void moveOnCardClick(String type) {
        Intent intent = null;
        switch (type) {
            case "Chat":
                intent = new Intent(context, ChatSectorActivity.class);
                break;
            case "WorkFlow":
                intent = new Intent(context, CropWorkFlow.class);
                break;
            case "Weather":
                intent = new Intent(context, WeatherActivity.class);
                break;
            case "Temperature":
                intent = new Intent(context, TemperatureActivity.class);
                break;
            case "SoilMoisture":
                intent = new Intent(context, SoilMoistureActivity.class);
                break;
            case "Farm":
                intent = new Intent(context, FarmsActivity.class);
                break;
            case "Light":
                intent = new Intent(context, LightActivity.class);
                break;
            case "SoilpH":
                intent = new Intent(context, SoilPhActivity.class);
                break;
            case "Humidity":
                intent = new Intent(context, HumidityActivity.class);
                break;
            case "MarketPlace":
                intent = new Intent(context, MarketPlaceActivity.class);
                break;
            case "News":
                intent = new Intent(context, GovernmentSchemesActivity.class);
                break;

        }
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return dashBoardClasses.size();
    }


    //hoder for each dash board item
    class SingleCardHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title, subTitle;
        ImageView dashboardIcon;

        SingleCardHolder(View view) {
            super(view);
            dashboardIcon = view.findViewById(R.id.dashboard_icon);
            title = view.findViewById(R.id.title);
            subTitle = view.findViewById(R.id.sub_title);
            cardView = view.findViewById(R.id.cardview);

        }
    }
}
