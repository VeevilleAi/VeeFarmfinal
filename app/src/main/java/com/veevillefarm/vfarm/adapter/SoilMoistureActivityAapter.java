package com.veevillefarm.vfarm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.helper.AppSingletonClass;
import com.veevillefarm.vfarm.helper.CustomMarkerView;
import com.veevillefarm.vfarm.helper.DashBoardDataClasses;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;


/**
 * Created by Prashant C on 29/06/18.
 */
public class SoilMoistureActivityAapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Object> dataList;
    private final String TAG = SoilMoistureActivityAapter.class.getSimpleName();

    public SoilMoistureActivityAapter(Context context, List<Object> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position) instanceof DashBoardDataClasses.SoilMoistureData) {
            return 0;
        } else if (dataList.get(position) instanceof DashBoardDataClasses.SensorGraph) {
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_moisture_activity_first_card, parent, false);
                holder = new SoilpHCardHolder(view);
                break;
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_ph_graph_data_card, parent, false);
                holder = new SoilPhGraphHolder(view);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_ph_graph_data_card, parent, false);
                holder = new SoilPhGraphHolder(view);
                break;

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case 0:
                handleTodaySoilMoistureCard((SoilpHCardHolder) holder, position);
                break;
            case 1:
                setUpgraphData((SoilPhGraphHolder) holder, position);
                break;
        }
    }

    private void handleTodaySoilMoistureCard(SoilpHCardHolder holder, int position) {

        DashBoardDataClasses.SoilMoistureData data = (DashBoardDataClasses.SoilMoistureData) dataList.get(position);
        List<DashBoardDataClasses.SoilMoistureData.SoilMoistureValues> soilMoistureValues = data.soilMoistureValues;
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView.setLayoutManager(manager);
        TodaySoilMoistureAdapter adapter = new TodaySoilMoistureAdapter(soilMoistureValues);
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.scrollToPosition(soilMoistureValues.size() - 1);
    }

    private void setData(final LineChart chart, String type) {

        ArrayList<String> xVals = setXAxisValues(type);
        ArrayList<Entry> yVals = setYAxisValues(xVals.size());
        LineDataSet set1;
        set1 = new LineDataSet(yVals, "Soil pH");
        set1.setFillAlpha(110);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setDrawCubic(true);
        set1.setDrawFilled(true);
        set1.setFillColor(Color.rgb(168, 168, 168));
        set1.setCircleRadius(3f);
        set1.setDrawCircles(false);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawValues(false);
        set1.setColor(Color.rgb(120, 120, 120));
        set1.setDrawHighlightIndicators(true);
        set1.setHighlightEnabled(true);
        set1.setHighLightColor(Color.RED);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(xVals, dataSets);
        chart.setDescription("");

        chart.setData(data);
        chart.animateX(2000);
        Legend l = chart.getLegend();
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setTextSize(12f);
        l.setTextColor(Color.BLACK);
        l.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
        l.setYEntrySpace(5f); // set the space between the legend entries on the y-axis


        String legendText = "";
        switch (type) {
            case "1D":
                legendText = "X - time in hours , Y - Soil Moisture Values";
                break;
            case "1W":
                legendText = "X - time in days , Y -  Soil Moisture Values";
                break;
            case "1M":
                legendText = "X - time in days , Y -  Soil Moisture Values";
                break;
            case "3M":
                legendText = "X - time in week , Y -  Soil Moisture Values";
                break;
            case "6M":
                legendText = "X - time in week , Y -  Soil Moisture Values";
                break;
            case "1Y":
                legendText = "X - time in months , Y -  Soil Moisture Values";
                break;
            case "2Y":
                legendText = "X - time in months , Y -  Soil Moisture Values";
                break;
        }
        l.setCustom(new int[]{Color.BLACK}, new String[]{legendText});

        chart.setDrawMarkerViews(false);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(false);
        chart.setDrawGridBackground(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void setUpgraphData(final SoilPhGraphHolder holder, int position) {
        DashBoardDataClasses.SensorGraph data = (DashBoardDataClasses.SensorGraph) dataList.get(position);
        setData(holder.mChart, data.selectedValue);
        holder.mChart.setTouchEnabled(true);
        holder.mChart.setDrawMarkerViews(true);
        CustomMarkerView customMarkerView = new CustomMarkerView(context, R.layout.custom_marker_view_layout);
        holder.mChart.setMarkerView(customMarkerView);
        setUpGraphTitleRecyclerview(holder, data.selectedPosition);

    }

    private void setUpGraphTitleRecyclerview(SoilPhGraphHolder holder, int selectedPosition) {
        LinearLayoutManager manager = new GridLayoutManager(context, 7);
        List<String> titles = getTitlesForGraph();
        holder.selectedPosition = selectedPosition;
        GraphTitleAdapter adapter = new GraphTitleAdapter(titles, selectedPosition);
        holder.recyclerView.setLayoutManager(manager);
        holder.recyclerView.setAdapter(adapter);

    }

    private List<String> getTitlesForGraph() {
        List<String> titles = new ArrayList<>();
        titles.add("1D");
        titles.add("1W");
        titles.add("1M");
        titles.add("3M");
        titles.add("6M");
        titles.add("1Y");
        titles.add("2Y");
        return titles;
    }


    private ArrayList<String> setXAxisValues(String type) {
        ArrayList<String> xVals = new ArrayList<>();

        switch (type) {
            case "1D":
                Calendar calendar = Calendar.getInstance();
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                for (int i = 0; i <= hours; i++) {
                    if (i < 10)
                        xVals.add("0" + i);
                    else
                        xVals.add("" + i);
                }
                break;

            case "1W":

                for (int i = 7; i > 0; i--) {
                    calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_MONTH, -i);
                    xVals.add(calendar.get(Calendar.DAY_OF_MONTH) + "");
                }
                break;

            case "1M":
                calendar = Calendar.getInstance();
                int days = calendar.get(Calendar.DAY_OF_MONTH);
                for (int i = 1; i <= days; i++) {
                    if (i < 10) {
                        xVals.add("0" + i);
                    } else {
                        xVals.add(i + "");
                    }
                }
                break;

            case "3M":
                break;

            case "6M":
                break;

            case "1Y":
                calendar = Calendar.getInstance();
                int month = calendar.get(Calendar.MONTH);
                for (int i = month; i >= 0; i--) {
                    calendar = Calendar.getInstance();
                    SimpleDateFormat month_date = new SimpleDateFormat("MMMM", Locale.ENGLISH);
                    calendar.add(Calendar.MONTH, -i);
                    String temp = month_date.format(calendar.getTime());
                    xVals.add(temp);
                }
                break;

            case "2Y":
                calendar = Calendar.getInstance();
                month = calendar.get(Calendar.MONTH);
                for (int i = month; i >= 0; i--) {
                    calendar = Calendar.getInstance();
                    SimpleDateFormat month_date = new SimpleDateFormat("MMMM", Locale.ENGLISH);
                    calendar.add(Calendar.MONTH, -i);
                    String temp = month_date.format(calendar.getTime());
                    xVals.add(temp);
                }

                break;


        }


        return xVals;
    }

    private void updateGraphData(String title, int updatePosition) {

        DashBoardDataClasses.SensorGraph soilPhGraphData = new DashBoardDataClasses.SensorGraph(title, updatePosition);
        dataList.set(1, soilPhGraphData);
        SoilMoistureActivityAapter.this.notifyItemChanged(1);

    }

    private ArrayList<Entry> setYAxisValues(int count) {
        ArrayList<Entry> yVals = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Random random = new Random();
            float randomValue = random.nextInt(100 - 80);
            yVals.add(new Entry(randomValue, i));
        }
        return yVals;
    }

    class TodaySoilMoistureAdapter extends RecyclerView.Adapter<TodaySoilMoistureAdapter.SingleValueHolder> {
        List<DashBoardDataClasses.SoilMoistureData.SoilMoistureValues> soilMoistureValues;

        TodaySoilMoistureAdapter(List<DashBoardDataClasses.SoilMoistureData.SoilMoistureValues> tempValues) {
            this.soilMoistureValues = tempValues;
        }

        @NonNull
        @Override
        public SingleValueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.humidity_value_card_with_time, parent, false);
            return new SingleValueHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SingleValueHolder holder, int position) {
            DashBoardDataClasses.SoilMoistureData.SoilMoistureValues value = soilMoistureValues.get(position);
            holder.time.setText(value.date);
            holder.value.setText(value.value1);
            holder.vale2.setText(value.value2);
        }

        @Override
        public int getItemCount() {
            return soilMoistureValues.size();
        }

        class SingleValueHolder extends RecyclerView.ViewHolder {
            TextView time, value, vale2;

            SingleValueHolder(View view) {
                super(view);
                time = view.findViewById(R.id.time_humidity);
                value = view.findViewById(R.id.h_relative);
                vale2 = view.findViewById(R.id.h_absolute);
            }
        }

    }

    class GraphTitleAdapter extends RecyclerView.Adapter<GraphTitleAdapter.GraphSingleTitleHolder> {

        int selectedPosition;
        List<String> titles;

        GraphTitleAdapter(List<String> titles, int selectedPosition) {
            this.titles = titles;
            this.selectedPosition = selectedPosition;
        }

        @NonNull
        @Override
        public GraphSingleTitleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.graph_title_item_card, parent, false);
            return new GraphSingleTitleHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final GraphSingleTitleHolder holder, int positionTemp) {
            final int position = holder.getAdapterPosition();
            if (selectedPosition == position) {
                holder.titleCard.setCardBackgroundColor(Color.parseColor("#FFC20E"));
            } else {
                holder.titleCard.setCardBackgroundColor(Color.parseColor("#DFBA74"));
            }

            holder.title.setText(titles.get(position));
            holder.titleCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyDataSetChanged();
                    String title = holder.title.getText().toString();
                    updateGraphData(title, position);
                }
            });


        }

        @Override
        public int getItemCount() {
            return titles.size();
        }

        class GraphSingleTitleHolder extends RecyclerView.ViewHolder {
            TextView title;
            CardView titleCard;

            GraphSingleTitleHolder(View view) {
                super(view);
                title = view.findViewById(R.id.title);
                titleCard = view.findViewById(R.id.cardview);
            }
        }

    }

    class SoilpHCardHolder extends RecyclerView.ViewHolder {
        TextView date, place, value;
        RecyclerView recyclerView;
        SoilpHCardHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            place = view.findViewById(R.id.place);
            value = view.findViewById(R.id.value);
            recyclerView = view.findViewById(R.id.recyclerview);
        }
    }

    class SoilPhGraphHolder extends RecyclerView.ViewHolder {
        int selectedPosition = -1;
        LineChart mChart;
        RecyclerView recyclerView;

        SoilPhGraphHolder(View view) {
            super(view);
            mChart = view.findViewById(R.id.linechart);
            recyclerView = view.findViewById(R.id.recyclerview);
        }
    }

    private void logMessage(String logMessage) {
        AppSingletonClass.logMessage(TAG, logMessage);
    }
}

