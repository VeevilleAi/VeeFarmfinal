package com.veeville.farm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.veeville.farm.R;
import com.veeville.farm.helper.DashBoardDataClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by Prashant C on 29/06/18.
 */
public class SoilMoistureActivityAapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Object> dataList;

    public SoilMoistureActivityAapter(Context context, List<Object> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position) instanceof DashBoardDataClasses.SoilMoistureData) {
            return 0;
        } else if (dataList.get(position) instanceof DashBoardDataClasses.SoilMoistureData.SoilMoistureGraphMonthData) {
            return 1;
        } else if (dataList.get(position) instanceof DashBoardDataClasses.SoilMoistureData.SoilMoistureGraphyearData) {
            return 2;
        } else {
            return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case 0:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_moisture_activity_first_card, parent, false);
                holder = new SoilMoistureCardHolder(view);
                break;
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_moisture_graph_card, parent, false);
                holder = new SoilMoistMonthGraphHolder(view);
                break;
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_moisture_graph_card, parent, false);
                holder = new SoilMoistMonthGraphHolder(view);

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
                updateMonthData((SoilMoistMonthGraphHolder) holder, "Month");
                break;
            case 2:
                updateMonthData((SoilMoistMonthGraphHolder) holder, "Year");
                break;
        }
//
//        holder.date.setText(dataList.get(position).month);
//        holder.place.setText(dataList.get(position).place);
//        LinearLayoutManager manager = new LinearLayoutManager(context);
//        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        ValuesAdapter adapter = new ValuesAdapter(dataList.get(position).soilMoistureValues);
//        holder.valuesRecyclerview.setLayoutManager(manager);
//        holder.valuesRecyclerview.setAdapter(adapter);
//        holder.valuesRecyclerview.scrollToPosition(5);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class SoilMoistureCardHolder extends RecyclerView.ViewHolder {
        RecyclerView valuesRecyclerview;
        TextView date, place;

        SoilMoistureCardHolder(View view) {
            super(view);
            valuesRecyclerview = view.findViewById(R.id.recyclerview_values);
            date = view.findViewById(R.id.date);
            place = view.findViewById(R.id.place);
        }
    }

    private void updateMonthData(SoilMoistMonthGraphHolder holder, String isMonth) {
        setData(holder.mChart, isMonth);
        if (isMonth.equals("Month"))
            holder.title.setText("October");
        else {
            holder.title.setText("2018");
        }

    }

    private ArrayList<String> setXAxisValues() {
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 1; i < 31; i++) {
            if (i < 10) {
                xVals.add("0" + i);
            } else {
                xVals.add("" + i);
            }
        }


        return xVals;
    }

    private ArrayList<String> setXAxisValuesForyear() {
        ArrayList<String> xVals = new ArrayList<>();
        xVals.add("Jan");
        xVals.add("Feb");
        xVals.add("Mar");
        xVals.add("Apr");
        xVals.add("May");
        xVals.add("Jun");
        xVals.add("Jul");
        xVals.add("Aug");
        xVals.add("Sept");
        xVals.add("Oct");
        xVals.add("Nov");
        xVals.add("Dec");


        return xVals;
    }

    private ArrayList<Entry> setYAxisValues() {
        ArrayList<Entry> yVals = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Random random = new Random();
            int randomValue = random.nextInt(50 - 40);
            yVals.add(new Entry(randomValue, i));
        }
        return yVals;
    }

    private ArrayList<Entry> setYAxisValuesYear() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < 12; i++) {
            Random random = new Random();
            int randomValue = random.nextInt(50 - 40);
            yVals.add(new Entry(randomValue, i));
        }
        return yVals;
    }

    private void setData(LineChart chart, String isMonth) {
        ArrayList<String> xVals;
        ArrayList<Entry> yVals;
        if (isMonth.equals("Month")) {
            xVals = setXAxisValues();
            yVals = setYAxisValues();
        } else {
            xVals = setXAxisValuesForyear();
            yVals = setYAxisValuesYear();
        }
        LineDataSet set1;
        set1 = new LineDataSet(yVals, "Soil Moisture");
        set1.setFillAlpha(110);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);

        set1.setCircleRadius(3f);
        set1.setDrawCircles(false);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawValues(false);


        if (isMonth.equals("Month")) {
            yVals = setYAxisValues();
        } else {
            yVals = setYAxisValuesYear();
        }

        set1.setDrawHighlightIndicators(true);
        set1.setHighlightEnabled(true);
        set1.setHighLightColor(Color.RED);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(xVals, dataSets);

        chart.setData(data);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(false);
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

    }

    class ValuesAdapter extends RecyclerView.Adapter<ValuesAdapter.SingleValueHolder> {
        List<DashBoardDataClasses.SoilMoistureData.SoilMoistureValues> soilMoistureValues;

        ValuesAdapter(List<DashBoardDataClasses.SoilMoistureData.SoilMoistureValues> soilMoistureValues) {
            this.soilMoistureValues = soilMoistureValues;
        }

        @NonNull
        @Override
        public SingleValueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_moisture_values_with_time_card, parent, false);
            return new SingleValueHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SingleValueHolder holder, int position) {
            holder.time.setText(soilMoistureValues.get(position).date);
            holder.value1.setText(soilMoistureValues.get(position).value1);
            holder.value2.setText(soilMoistureValues.get(position).value2);
        }

        @Override
        public int getItemCount() {
            return soilMoistureValues.size();
        }

        class SingleValueHolder extends RecyclerView.ViewHolder {
            TextView time, value1, value2;

            SingleValueHolder(View view) {
                super(view);
                time = view.findViewById(R.id.time);
                value1 = view.findViewById(R.id.value1);
                value2 = view.findViewById(R.id.value2);
            }
        }
    }

    class SoilMoistMonthGraphHolder extends RecyclerView.ViewHolder {
        LineChart mChart;
        TextView title;

        SoilMoistMonthGraphHolder(View view) {
            super(view);
            mChart = view.findViewById(R.id.linechart);
            title = view.findViewById(R.id.title);
        }
    }
}

