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
import com.veeville.farm.helper.ChatContact;
import com.veeville.farm.helper.CircleTransform;

import java.util.List;

/**
 * Created by Prashant C on 07/12/18.
 * adpater for Contact list in FriendsFragment
 * on click on each contact will move into one to one caht with text and image
 */
public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.SingleContactHolder> {

    private List<ChatContact> farmerContacts;
    private Context context;
    private String fromAddress;

    public FriendsListAdapter(List<ChatContact> farmerContacts, Context context, String fromAddress) {
        this.farmerContacts = farmerContacts;
        this.context = context;
        this.fromAddress = fromAddress;
    }

    @NonNull
    @Override
    public SingleContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_card, parent, false);
        return new SingleContactHolder(view);
    }

    public void updateContacts(List<ChatContact> farmerContacts) {
        this.farmerContacts = farmerContacts;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull SingleContactHolder holder, int position) {

        final ChatContact contact = farmerContacts.get(position);
        holder.name.setText(contact.name);
        Picasso.with(context).load(contact.picUrl).resize(500, 500).centerCrop().transform(new CircleTransform()).into(holder.profilePic);
        holder.mostRecentMessage.setText("");
        holder.contactCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OneToOneChatActivity.class);
                intent.putExtra("name", contact.name);
                intent.putExtra("to", contact.email);
                intent.putExtra("from", fromAddress);
                context.startActivity(intent);
            }
        });
        holder.mostRecentMessage.setText(contact.recentMessage);

    }

    @Override
    public int getItemCount() {
        return farmerContacts.size();
    }

    class SingleContactHolder extends RecyclerView.ViewHolder {
        TextView name, mostRecentMessage;
        CardView contactCard;
        ImageView profilePic;

        SingleContactHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            profilePic = view.findViewById(R.id.profile_picture);
            contactCard = view.findViewById(R.id.contact_card);
            mostRecentMessage = view.findViewById(R.id.most_recent_message);
        }
    }
}
