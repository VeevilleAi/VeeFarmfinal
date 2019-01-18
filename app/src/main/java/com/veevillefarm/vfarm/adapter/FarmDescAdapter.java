package com.veevillefarm.vfarm.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.activity.LightActivity;
import com.veevillefarm.vfarm.activity.SoilPhActivity;
import com.veevillefarm.vfarm.activity.TemperatureActivity;
import com.veevillefarm.vfarm.farmer.FarmerHelperClasses.FarmDesc;
import com.veevillefarm.vfarm.farmer.FarmerHelperClasses.FarmDescMap;
import com.veevillefarm.vfarm.farmer.FarmerHelperClasses.SoilTestResult;
import com.veevillefarm.vfarm.farmer.ShowFarmInMapActivity;
import com.veevillefarm.vfarm.helper.AppSingletonClass;
import com.veevillefarm.vfarm.helper.ChatmessageDataClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant C on 22/10/18.
 * adapter for FarmDescriptions like farm image ,crop short desc,soil test results....
 */
public class FarmDescAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> objects;
    private final String TAG = FarmDescAdapter.class.getSimpleName();
    private Context context;
    private String farmname;

    public FarmDescAdapter(Context context, List<Object> objects, String farmname) {
        this.objects = objects;
        this.context = context;
        this.farmname = farmname;
    }

    @Override
    public int getItemViewType(int position) {

        if (objects.get(position) instanceof FarmDesc) {
            return 0;
        } else if (objects.get(position) instanceof FarmDescMap) {
            return 1;
        } else if (objects.get(position) instanceof ChatmessageDataClasses.SoilPH) {
            return 2;
        } else if (objects.get(position) instanceof ChatmessageDataClasses.SoilTemperature) {
            return 3;
        } else if (objects.get(position) instanceof ChatmessageDataClasses.Light) {
            return 4;
        } else if (objects.get(position) instanceof SoilTestResult) {
            return 5;
        } else if (objects.get(position) instanceof SoilTestResult.SoilTestGraph) {
            return 6;
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm_description_card, parent, false);
                holder = new FarmDescCardHolder(view);
                break;
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm_description_static_map_card, parent, false);
                holder = new FarmDescStaticMapHolder(view);
                break;
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm_description_soil_ph_card, parent, false);
                holder = new SoilPhHolder(view);
                break;
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm_description_soil_temperature_card, parent, false);
                holder = new SoilTemperatureHolder(view);
                break;
            case 4:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm_description_light_on_soil_card, parent, false);
                holder = new LightHolder(view);
                break;
            case 5:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_test_result_card, parent, false);
                holder = new SoilTestResultHolder(view);
                break;

            case 6:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_test_result_graph_card, parent, false);
                holder = new SoilTestGraphHolder(view);
                break;

            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_test_result_graph_card, parent, false);
                holder = new SoilTestGraphHolder(view);
                break;
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case 0:
                handleFarmDesc((FarmDescCardHolder) holder, position);
                break;
            case 1:
                handleFarmDescMap((FarmDescStaticMapHolder) holder, position);
                break;
            case 2:
                handleSoilPh((SoilPhHolder) holder, position);
                break;
            case 3:
                handleSoilTmeprature((SoilTemperatureHolder) holder, position);
                break;
            case 4:
                handleLightCard((LightHolder) holder, position);
                break;
            case 5:
                handleSoilTestResults((SoilTestResultHolder) holder, position);
                break;
            case 6:
                handleSoilTestResultsGraph((SoilTestGraphHolder) holder, position);
                break;
        }
    }

    private void handleSoilTestResultsGraph(final SoilTestGraphHolder holder, int position) {

        logMessage("" + position);
        ArrayList<Entry> yvalues = new ArrayList<>();
        yvalues.add(new Entry(20f, 0));
        yvalues.add(new Entry(20f, 1));
        yvalues.add(new Entry(20f, 2));
        yvalues.add(new Entry(20f, 3));

        PieDataSet dataSet = new PieDataSet(yvalues, "Election Results");
        ArrayList<String> xVals = new ArrayList<>();

        xVals.add("P - 34");
        xVals.add("S - 34");
        xVals.add("N - 25");
        xVals.add("C - 45");

        PieData data = new PieData(xVals, dataSet);
        data.setDrawValues(false);
        // In percentage Term
        data.setValueFormatter(new PercentFormatter());
        holder.pieChart.setData(data);
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        dataSet.setColors(colors);
        holder.pieChart.setDrawHoleEnabled(true);
        holder.pieChart.setTransparentCircleRadius(50f);
        holder.pieChart.setHoleRadius(50f);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);
        holder.pieChart.animateXY(1400, 1400);
        holder.pieChart.setCenterText("Click on one to get more info");
        holder.pieChart.setUsePercentValues(false);
        holder.pieChart.setDescription("");    // Hide the description
        holder.pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                String value = "Nitrogen\t25 ppm\t" + "Range 65-80 ppm\n" + "Thinks to take care";
                holder.description.setText(value);
            }

            @Override
            public void onNothingSelected() {
                holder.description.setText("");
            }
        });
        holder.pieChart.getLegend().setEnabled(false);

    }

    private void handleSoilTestResults(SoilTestResultHolder holder, int position) {
        SoilTestResult result = (SoilTestResult) objects.get(position);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(manager);
        SingleSoilTestResultAdapter adapter = new SingleSoilTestResultAdapter(result.soilTests);
        holder.recyclerView.setAdapter(adapter);
    }

    private void handleLightCard(LightHolder holder, int position) {
        logMessage("" + position);
        holder.light_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LightActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void handleFarmDesc(FarmDescCardHolder holder, int position) {
        logMessage("" + holder + position);
    }

    private void handleFarmDescMap(final FarmDescStaticMapHolder holder, int position) {
        logMessage("" + position);
        String url = "https://maps.googleapis.com/maps/api/staticmap?maptype=hybrid&size=600x300&zoom=15&key=AIzaSyBeilqJcTJPyZ--59DXSsK1mWrWL3guh8k&markers=13.053990,77.572937";
        Glide.with(context).load(url).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowFarmInMapActivity.class);
                intent.putExtra("FarmName", farmname);
                context.startActivity(intent);
            }
        });

    }

    private void handleSoilPh(SoilPhHolder holder, int position) {
        logMessage("" + position);
        holder.soilPhCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SoilPhActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void handleSoilTmeprature(SoilTemperatureHolder holder, int position) {
        logMessage("" + position);
        holder.soilTempCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TemperatureActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    private class FarmDescCardHolder extends RecyclerView.ViewHolder {
        FarmDescCardHolder(View view) {
            super(view);
        }
    }

    private class SoilTestGraphHolder extends RecyclerView.ViewHolder {
        PieChart pieChart;
        TextView description;

        SoilTestGraphHolder(View view) {
            super(view);
            pieChart = view.findViewById(R.id.piechart);
            description = view.findViewById(R.id.description);
        }
    }

    private class FarmDescStaticMapHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        FarmDescStaticMapHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image);
        }
    }

    private class SoilTestResultHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        SoilTestResultHolder(View view) {
            super(view);
            recyclerView = view.findViewById(R.id.recyclerview);

        }
    }

    class LightHolder extends RecyclerView.ViewHolder {
        CardView light_card;

        LightHolder(View view) {
            super(view);
            light_card = view.findViewById(R.id.light_card);

        }
    }

    class SoilPhHolder extends RecyclerView.ViewHolder {
        CardView soilPhCard;

        SoilPhHolder(View view) {
            super(view);
            soilPhCard = view.findViewById(R.id.soilph_card);
        }
    }

    class SoilTemperatureHolder extends RecyclerView.ViewHolder {
        CardView soilTempCard;

        SoilTemperatureHolder(View view) {
            super(view);
            soilTempCard = view.findViewById(R.id.soil_temperature_card);
        }
    }


    private class SoilTestResultsAdapter extends RecyclerView.Adapter<SoilTestResultsAdapter.SingleSoilTestSingleELementResultHolder> {

        List<SoilTestResult.SingleSoilTestResult.SingleSoilTestResultSingleElement> list;

        SoilTestResultsAdapter(List<SoilTestResult.SingleSoilTestResult.SingleSoilTestResultSingleElement> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public SingleSoilTestSingleELementResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.soil_test_result_single_element_card, parent, false);
            return new SingleSoilTestSingleELementResultHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final SingleSoilTestSingleELementResultHolder holder, int position) {
            SoilTestResult.SingleSoilTestResult.SingleSoilTestResultSingleElement element = list.get(position);
            holder.element.setText(element.element);
            holder.explanation.setText(element.description);
            String result = element.result + "";
            holder.quantity.setText(result);
            if (element.result > element.rangeMin && element.result < element.rangeMax) {
                holder.indicator.setImageResource(R.drawable.ic_arrow_normal_green);
            } else if (element.result < element.rangeMin) {
                holder.indicator.setImageResource(R.drawable.ic_arrow_upword_gray);
            } else {
                holder.indicator.setImageResource(R.drawable.ic_arrow_normal_green);
            }
            holder.moreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.moreInfo.getText().toString().equals("MoreInfo -")) {
                        holder.explanation.setVisibility(View.GONE);
                        String value = "MoreInfo +";
                        holder.moreInfo.setText(value);
                    } else {
                        String value = "MoreInfo -";
                        holder.moreInfo.setText(value);
                        holder.explanation.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class SingleSoilTestSingleELementResultHolder extends RecyclerView.ViewHolder {
            TextView element, quantity, moreInfo, explanation;
            ImageView indicator;

            SingleSoilTestSingleELementResultHolder(View view) {
                super(view);
                element = view.findViewById(R.id.element);
                quantity = view.findViewById(R.id.quantity);
                moreInfo = view.findViewById(R.id.more_info);
                explanation = view.findViewById(R.id.explanation);
                indicator = view.findViewById(R.id.indicator);
            }
        }

    }

    class SingleSoilTestResultAdapter extends RecyclerView.Adapter<SingleSoilTestResultAdapter.SingleSoilTestHolder> {

        List<SoilTestResult.SingleSoilTestResult> soilTestResults;

        SingleSoilTestResultAdapter(List<SoilTestResult.SingleSoilTestResult> soilTestResults) {
            this.soilTestResults = soilTestResults;
        }

        @NonNull
        @Override
        public SingleSoilTestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlesoil_test_result_card, parent, false);
            return new SingleSoilTestHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SingleSoilTestHolder holder, int position) {
            LinearLayoutManager manager = new LinearLayoutManager(context);
            SoilTestResult.SingleSoilTestResult result = soilTestResults.get(position);
            holder.recyclerView.setLayoutManager(manager);
            SoilTestResultsAdapter adapter = new SoilTestResultsAdapter(result.singleSoilTestResultSingleElements);
            holder.recyclerView.setAdapter(adapter);
        }

        @Override
        public int getItemCount() {
            return soilTestResults.size();
        }

        class SingleSoilTestHolder extends RecyclerView.ViewHolder {
            RecyclerView recyclerView;

            SingleSoilTestHolder(View view) {
                super(view);
                recyclerView = view.findViewById(R.id.recyclerview);
            }
        }

    }

    private void logMessage(String logMessage) {
        AppSingletonClass.logMessage(TAG, logMessage);
    }

}
