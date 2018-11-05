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
public class HumidityActivityAdapterTemp extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Object> humidityDataList;


    public HumidityActivityAdapterTemp(Context context, List<Object> humidityDataList) {
        this.context = context;
        this.humidityDataList = humidityDataList;
    }

    @Override
    public int getItemViewType(int position) {
        if (humidityDataList.get(position) instanceof DashBoardDataClasses.HumidityData.HumidityDataGraphCardMonth) {
            return 0;
        } else if (humidityDataList.get(position) instanceof DashBoardDataClasses.HumidityData.HumidityDataGraphCardYear) {
            return 1;
        } else if (humidityDataList.get(position) instanceof DashBoardDataClasses.HumidityData.HumidityToday) {
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.humidity_graph_one_month_card, parent, false);
                holder = new HumidityCardHolder(view);
                break;
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.humidity_graph_one_month_card, parent, false);
                holder = new HumidityCardHolder(view);
                break;
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.humidity_activity_first_card, parent, false);
                holder = new HumidityCardTodayHolder(view);
                break;

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                updateMonthData((HumidityCardHolder) holder, "Month");
                break;
            case 1:
                updateMonthData((HumidityCardHolder) holder, "Year");
                break;
        }
    }


    private void updateMonthData(HumidityCardHolder holder, String isMonth) {
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
            int randomValue = random.nextInt(80 - 0);
            yVals.add(new Entry(randomValue, i));
        }
        return yVals;
    }

    private ArrayList<Entry> setYAxisValuesYear() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < 12; i++) {
            Random random = new Random();
            int randomValue = random.nextInt(80 - 0);
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
        LineDataSet set1, set2;
        set1 = new LineDataSet(yVals, "Absolete");
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
        set2 = new LineDataSet(yVals, "Relative");
        set2.setFillAlpha(110);
        set2.setColor(Color.BLACK);
        set2.setCircleColor(Color.BLACK);
        set2.setLineWidth(1f);
        set2.setColor(Color.GREEN);
        set2.setCircleRadius(3f);
        set2.setDrawCircles(false);
        set2.setDrawCircleHole(false);
        set2.setValueTextSize(9f);
        set1.setDrawHighlightIndicators(true);
        set2.setDrawHighlightIndicators(true);
        set1.setHighlightEnabled(true);
        set2.setHighlightEnabled(true);
        set1.setHighLightColor(Color.RED);
        set2.setHighLightColor(Color.RED);
        set2.setDrawValues(false);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);// add the datasets
        LineData data = new LineData(xVals, dataSets);

        chart.setData(data);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(false);
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

    }

    @Override
    public int getItemCount() {
        return humidityDataList.size();
    }

    class HumidityCardHolder extends RecyclerView.ViewHolder {
        LineChart mChart;
        TextView title;

        HumidityCardHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            mChart = view.findViewById(R.id.linechart);
        }
    }

    class HumidityCardTodayHolder extends RecyclerView.ViewHolder {
        HumidityCardTodayHolder(View view) {
            super(view);
        }
    }
}
