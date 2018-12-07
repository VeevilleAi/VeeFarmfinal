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

import com.veeville.farm.R;
import com.veeville.farm.activity.ChatSectorActivity;
import com.veeville.farm.activity.CropWorkFlow;
import com.veeville.farm.activity.GovernmentSchemesActivity;
import com.veeville.farm.activity.HumidityActivity;
import com.veeville.farm.activity.LightActivity;
import com.veeville.farm.activity.SoilMoistureActivity;
import com.veeville.farm.activity.SoilPhActivity;
import com.veeville.farm.activity.TemperatureActivity;
import com.veeville.farm.activity.WeatherActivity;
import com.veeville.farm.farmer.FarmsActivity;
import com.veeville.farm.farmer.MarketPlaceActivity;
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.DashBoardClass;

import java.util.List;

/**
 * Created by Prashant C on 25/10/18.
 */
public class AppDashBoardAdapter extends RecyclerView.Adapter<AppDashBoardAdapter.SingleCardHolder> {

    private final String TAG = AppDashBoardAdapter.class.getSimpleName();
    private List<DashBoardClass> dashBoardClasses;
    private Context context;

    public AppDashBoardAdapter(Context context, List<DashBoardClass> boardClasses) {
        this.dashBoardClasses = boardClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public SingleCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_dashboard_card, parent, false);
        return new SingleCardHolder(view);
    }

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

    private void moveOnCardClick(String type) {
        Intent intent = null;
        switch (type) {
            case "Chat":
//                intent = new Intent(context, ChatActivity.class);
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
        logMessage("size:" + dashBoardClasses.size());
        return dashBoardClasses.size();
    }

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

    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }

}
