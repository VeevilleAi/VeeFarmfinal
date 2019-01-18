package com.veevillefarm.vfarm.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.activity.GroupChatActivity;
import com.veevillefarm.vfarm.helper.ChatGroup;
import com.veevillefarm.vfarm.helper.CircleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant C on 08/01/19.
 */
public class ChatGroupFragmentAdapter extends RecyclerView.Adapter<ChatGroupFragmentAdapter.SingleGroupHolder> {


    private List<ChatGroup> chatGroups;
    private Context context;
    public ChatGroupFragmentAdapter(Context context,List<ChatGroup> chatGroups){
        this.chatGroups = chatGroups;
        this.context = context;
    }
    @NonNull
    @Override
    public SingleGroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_card,parent,false);
        return new SingleGroupHolder(view);
    }
    public void updateNewGroups(List<ChatGroup> chatGroups){
        this.chatGroups = chatGroups;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull SingleGroupHolder holder, int position) {
        final ChatGroup group = chatGroups.get(position);
        holder.groupName.setText(group.groupName);
        holder.mostRecentMessage.setText(group.latestMessage);
        Picasso.with(context).load("https://static.wixstatic.com/media/200fe1_06ce139451b6433d95342587d4542c01.png").resize(500, 500).centerCrop().transform(new CircleTransform()).into(holder.groupIcon);

        holder.contact_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GroupChatActivity.class);
                intent.putExtra("name",group.groupName);
                intent.putParcelableArrayListExtra("contacts", (ArrayList<? extends Parcelable>) group.contacts);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatGroups.size();
    }

    class SingleGroupHolder extends RecyclerView.ViewHolder{
        TextView groupName,mostRecentMessage;
        CardView contact_card;
        ImageView groupIcon;
        SingleGroupHolder(View view){
            super(view);
            groupName = view.findViewById(R.id.name);
            mostRecentMessage = view.findViewById(R.id.most_recent_message);
            contact_card = view.findViewById(R.id.contact_card);
            groupIcon = view.findViewById(R.id.profile_picture);
        }
    }
}
