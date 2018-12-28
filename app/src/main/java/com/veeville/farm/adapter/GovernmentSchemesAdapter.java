package com.veeville.farm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.veeville.farm.R;
import com.veeville.farm.helper.News;

import java.util.List;

/**
 * Created by Prashant C on 07/12/18.
 * this adapter handles Government schems and handling its explanation listeners
 */
public class GovernmentSchemesAdapter extends RecyclerView.Adapter<GovernmentSchemesAdapter.SingleNewsCardHolder> {


    private Context context;
    private List<News> newsList;

    public GovernmentSchemesAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public SingleNewsCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_card, parent, false);
        return new SingleNewsCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleNewsCardHolder holder, int position) {
        News news = newsList.get(position);
        holder.newsShortDesc.setText(news.shortDesc);
        Glide.with(context).load(news.imageUrl).into(holder.newsImage);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class SingleNewsCardHolder extends RecyclerView.ViewHolder {

        ImageView newsImage;
        TextView newsShortDesc;

        SingleNewsCardHolder(View view) {
            super(view);
            newsImage = view.findViewById(R.id.news_image);
            newsShortDesc = view.findViewById(R.id.news_short_desc);
        }
    }
}
