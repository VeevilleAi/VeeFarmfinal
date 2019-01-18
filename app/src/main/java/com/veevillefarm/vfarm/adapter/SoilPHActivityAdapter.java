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
import android.widget.LinearLayout;
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
 * Created by Prashant C on 04/07/18.
 *
 * this adapter is used to handle Soil ph Activity with numbers and graphs
 * which will explain the variation of data weekly monthly quartly
 */
public class SoilPHActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = SoilPHActivityAdapter.class.getSimpleName();
    private List<Object> soilPHList;
    private Context context;

    public SoilPHActivityAdapter(List<Object> soilPHList, Context context) {
        this.soilPHList = soilPHList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (soilPHList.get(position) instanceof DashBoardDataClasses.SoilPH) {
            return 0;
        } else if (soilPHList.get(position) instanceof DashBoardDataClasses.SensorGraph) {
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_ph_remaining_cards, parent, false);
                holder = new SoilPhFirstCardHolder(view);
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
                handleFirstPhCard((SoilPhFirstCardHolder) holder, position);
                break;
            case 1:
                setUpgraphData((SoilPhGraphHolder) holder, position);
                break;
        }

    }

    private void handleFirstPhCard(SoilPhFirstCardHolder holder, int position) {
        logMessage("" + position);
        LinearLayoutManager manager = new GridLayoutManager(context, 15);
        holder.recyclerView.setLayoutManager(manager);
        SoilPhValuesAdapter adapter = new SoilPhValuesAdapter(getSoilPhValues());
        holder.recyclerView.setAdapter(adapter);

    }



    private List<String> getSoilPhValues() {
        List<String> soilPhValues = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            if (i < 10)
                soilPhValues.add("0" + i);
            else
                soilPhValues.add("" + i);
        }
        return soilPhValues;
    }

    private void logMessage(String logMessage) {
        AppSingletonClass.logMessage(TAG, logMessage);
    }

    @Override
    public int getItemCount() {
        return soilPHList.size();
    }

    private void setUpgraphData(final SoilPhGraphHolder holder, int position) {
        DashBoardDataClasses.SensorGraph data = (DashBoardDataClasses.SensorGraph) soilPHList.get(position);
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
        soilPHList.set(1, soilPhGraphData);
        SoilPHActivityAdapter.this.notifyItemChanged(1);

    }

    private ArrayList<Entry> setYAxisValues(int count) {
        ArrayList<Entry> yVals = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Random random = new Random();
            float randomValue = random.nextFloat() % 14.0f;
            yVals.add(new Entry(randomValue, i));
        }
        return yVals;
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
        set1.setCircleRadius(1f);
        set1.setDrawCircles(true);
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
        chart.setDescription("");
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
                legendText = "X - time in hours , Y - pH Values";
                break;
            case "1W":
                legendText = "X - time in days , Y - pH Values";
                break;
            case "1M":
                legendText = "X - time in days , Y - pH Values";
                break;
            case "3M":
                legendText = "X - time in week , Y - pH Values";
                break;
            case "6M":
                legendText = "X - time in week , Y - pH Values";
                break;
            case "1Y":
                legendText = "X - time in months , Y - pH Values";
                break;
            case "2Y":
                legendText = "X - time in months , Y - pH Values";
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
                holder.titleCard.setCardBackgroundColor(Color.parseColor("#DFBA74"));
            } else {
                holder.titleCard.setCardBackgroundColor(Color.parseColor("#FFC20E"));
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

    class SoilPhValuesAdapter extends RecyclerView.Adapter<SoilPhValuesAdapter.SingleSoilPhHolder> {

        private List<String> phValues;
        private int phValue = 6;
        SoilPhValuesAdapter(List<String> phValues) {
            this.phValues = phValues;
        }

        @NonNull
        @Override
        public SingleSoilPhHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_ph_single_value, parent, false);
            return new SingleSoilPhHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SingleSoilPhHolder holder, int positionTemp) {
            int position = holder.getAdapterPosition();
            holder.soilPhValue.setText(phValues.get(position));


            if (phValue - 3 >= position || phValue + 3 <= position) {

                holder.soilphColor.setBackgroundColor(Color.GRAY);
            } else {
                if (position < 4 || position > 10) {
                    holder.soilphColor.setBackgroundColor(Color.parseColor("#de1f26"));
                }
                if ((position > 3 && position < 6) || (position > 8 && position < 11)) {
                    holder.soilphColor.setBackgroundColor(Color.parseColor("#fee024"));
                } else {
                    holder.soilphColor.setBackgroundColor(Color.parseColor("#1f8d44"));
                }
            }

        }

        @Override
        public int getItemCount() {
            return phValues.size();
        }

        class SingleSoilPhHolder extends RecyclerView.ViewHolder {
            TextView soilPhValue;
            LinearLayout soilphColor;

            SingleSoilPhHolder(View view) {
                super(view);
                soilPhValue = view.findViewById(R.id.soil_ph_value);
                soilphColor = view.findViewById(R.id.soil_ph_color);
            }
        }

    }

    class SoilPhFirstCardHolder extends RecyclerView.ViewHolder {
        TextView showPhScale;
        RecyclerView recyclerView;
        SoilPhFirstCardHolder(View view) {
            super(view);
            showPhScale = view.findViewById(R.id.show_phscale);
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

}