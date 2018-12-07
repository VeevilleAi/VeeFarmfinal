package com.veeville.farm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.veeville.farm.R;
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.ChatmessageDataClasses;

import java.util.List;

/**
 * Created by Prashant C on 29/06/18.
 */
public class WeatherActivityAdapter extends RecyclerView.Adapter<WeatherActivityAdapter.WeatherCardHolder> {

    private List<ChatmessageDataClasses.WeatherData> weatherDataList;
    private Context context;
    private final String TAG = WeatherActivityAdapter.class.getSimpleName();

    public WeatherActivityAdapter(List<ChatmessageDataClasses.WeatherData> weatherData, Context context) {
        this.context = context;
        this.weatherDataList = weatherData;
    }

    @NonNull
    @Override
    public WeatherCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_activity_card, parent, false);
        return new WeatherCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherCardHolder holder, int position) {
        ChatmessageDataClasses.WeatherData weatherData = weatherDataList.get(position);
        String temp = weatherData.temp + "\u00b0";
        holder.temp.setText(temp);
        holder.date.setText(weatherData.date);
        Glide.with(context).load(weatherData.imgLink).into(holder.weather_icon);
        holder.place.setText(weatherData.place);
        String humidity = ":" + weatherData.humidity;
        String windSpeed = ":" + weatherData.wind;
        holder.humidity.setText(humidity);
        holder.windSpeed.setText(windSpeed);
        String precipitation = ":" + weatherData.precipitation;
        holder.prec.setText(precipitation);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView.setLayoutManager(manager);
        WeatherBottomAdapter adapter = new WeatherBottomAdapter(weatherData.tempHourly);
        holder.recyclerView.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        logMessage("size:" + weatherDataList.size());
        return weatherDataList.size();
    }

    class WeatherCardHolder extends RecyclerView.ViewHolder {
        TextView date, place, temp, prec, humidity, windSpeed;
        CardView weahter_card;
        ImageView weather_icon;
        RecyclerView recyclerView;

        WeatherCardHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            weather_icon = view.findViewById(R.id.weather_icon);
            place = view.findViewById(R.id.place);
            weahter_card = view.findViewById(R.id.weather_card);
            recyclerView = view.findViewById(R.id.recyclerview);
            prec = view.findViewById(R.id.precipitation);
            windSpeed = view.findViewById(R.id.wind_speed);
            humidity = view.findViewById(R.id.humidity);
            temp = view.findViewById(R.id.temperature);
        }
    }

    class WeatherBottomAdapter extends RecyclerView.Adapter<WeatherBottomAdapter.ThreeHourHolder> {
        List<List<String>> lists;

        WeatherBottomAdapter(List<List<String>> lists) {
            this.lists = lists;
        }

        @NonNull
        @Override
        public ThreeHourHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.weather_time_value_card, parent, false);
            return new ThreeHourHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ThreeHourHolder holder, int position) {
            holder.time.setText(lists.get(position).get(0));
            holder.temp.setText(lists.get(position).get(1));
        }

        @Override
        public int getItemCount() {
            return lists.size();
        }

        class ThreeHourHolder extends RecyclerView.ViewHolder {
            TextView time, temp;

            ThreeHourHolder(View view) {
                super(view);
                time = view.findViewById(R.id.time);
                temp = view.findViewById(R.id.temperature);
            }
        }
    }

    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }
}
