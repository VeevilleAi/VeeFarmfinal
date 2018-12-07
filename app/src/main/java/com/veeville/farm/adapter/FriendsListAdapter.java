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

import com.squareup.picasso.Picasso;
import com.veeville.farm.R;
import com.veeville.farm.activity.OneToOneChatActivity;
import com.veeville.farm.helper.CircleTransform;
import com.veeville.farm.helper.FarmerContact;

import java.util.List;

/**
 * Created by Prashant C on 07/12/18.
 */
public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.SingleContactHolder> {

    private List<FarmerContact> farmerContacts;
    private Context context;

    public FriendsListAdapter(List<FarmerContact> farmerContacts, Context context) {
        this.farmerContacts = farmerContacts;
        this.context = context;
    }

    @NonNull
    @Override
    public SingleContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_card, parent, false);
        return new SingleContactHolder(view);
    }

    public void updateContacts(List<FarmerContact> farmerContacts) {
        this.farmerContacts = farmerContacts;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull SingleContactHolder holder, int position) {

        final FarmerContact contact = farmerContacts.get(position);
        holder.name.setText(contact.name);
        Picasso.with(context).load(contact.profilePic).resize(500, 500).centerCrop().transform(new CircleTransform()).into(holder.profilePic);

        holder.contactCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OneToOneChatActivity.class);
                intent.putExtra("name", contact.name);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return farmerContacts.size();
    }

    class SingleContactHolder extends RecyclerView.ViewHolder {
        TextView name;
        CardView contactCard;
        ImageView profilePic;

        SingleContactHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            profilePic = view.findViewById(R.id.profile_picture);
            contactCard = view.findViewById(R.id.contact_card);
        }
    }
}
