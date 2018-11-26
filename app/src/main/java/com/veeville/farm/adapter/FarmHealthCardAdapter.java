package com.veeville.farm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.veeville.farm.R;
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.ChatmessageDataClasses;

import java.util.List;

/**
 * Created by Prashant C on 20/11/18.
 */
public class FarmHealthCardAdapter extends RecyclerView.Adapter<FarmHealthCardAdapter.SingleElementHealthCardHolder> {

    private final String TAG = FarmHealthCardAdapter.class.getSimpleName();
    private Context context;
    private List<ChatmessageDataClasses.HealthCard.SingleElementHealth> healthCards;

    FarmHealthCardAdapter(Context context, List<ChatmessageDataClasses.HealthCard.SingleElementHealth> healthCards) {
        this.context = context;
        this.healthCards = healthCards;
    }

    @NonNull
    @Override
    public SingleElementHealthCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.health_card_single_element, parent, false);
        return new SingleElementHealthCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleElementHealthCardHolder holder, int position) {
        ChatmessageDataClasses.HealthCard.SingleElementHealth health = healthCards.get(position);
        holder.title.setText(health.title);
        String average = health.average + "";
        holder.average.setText(average);
        String max = health.max + "";
        holder.max.setText(max);
        String min = health.min + "";
        holder.min.setText(min);

    }

    @Override
    public int getItemCount() {
        logMessage("size:" + healthCards.size());
        return healthCards.size();
    }

    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }

    class SingleElementHealthCardHolder extends RecyclerView.ViewHolder {
        TextView title, average, max, min;

        SingleElementHealthCardHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            average = view.findViewById(R.id.average);
            min = view.findViewById(R.id.minimum);
            max = view.findViewById(R.id.maximum);
        }
    }
}
