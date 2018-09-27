package com.veeville.farm.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.veeville.farm.R;
import com.veeville.farm.helper.ChatmessageDataClasses;

import java.util.List;

/**
 * Created by user on 12-07-2017.
 */

public class QuickReplyAdapter extends RecyclerView.Adapter<QuickReplyAdapter.SingleQuickreplyholder> {

    private List<String> quickreplystrings;
    private QuickReplyOption option;

    QuickReplyAdapter(ChatmessageDataClasses.OptionMenu quickreplylist, QuickReplyOption option) {
        this.quickreplystrings = quickreplylist.menuItems;
        this.option = option;
    }

    @NonNull
    @Override
    public SingleQuickreplyholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quickreplycard, parent, false);
        return new SingleQuickreplyholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleQuickreplyholder holder, int position_temp) {

        final int position = holder.getAdapterPosition();
        holder.quickreplyname.setText(quickreplystrings.get(position));
        holder.quickreplyname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option.selectedMessage(quickreplystrings.get(position));
            }
        });

    }


    @Override
    public int getItemCount() {
        return quickreplystrings.size();
    }

    class SingleQuickreplyholder extends RecyclerView.ViewHolder {
        TextView quickreplyname;

        SingleQuickreplyholder(View view) {
            super(view);
            quickreplyname = view.findViewById(R.id.quickreplytext);
        }
    }

    public interface QuickReplyOption {
        void selectedMessage(String message);

        void insertDiseaseNames(String diseaseName);

        void insertImage(String qNaQuesry, String imageLink);
    }
}
