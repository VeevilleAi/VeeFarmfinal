package com.veevillefarm.vfarm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.helper.ChatContact;
import com.veevillefarm.vfarm.helper.CircleTransform;

import java.util.List;

/**
 * Created by Prashant C on 07/01/19.
 */
public class SelectedContactsAdapter extends RecyclerView.Adapter<SelectedContactsAdapter.ContactHolder> {

    private List<ChatContact> contacts;
    private Context context;
    public SelectedContactsAdapter(Context context, List<ChatContact> contacts){
        this.contacts =contacts;
        this.context = context;
    }
    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.selected_contact_card,parent,false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        ChatContact contact = contacts.get(position);
        holder.name.setText(contact.name);
        Picasso.with(context).load(contact.picUrl).resize(500, 500).centerCrop().transform(new CircleTransform()).into(holder.pic);

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ContactHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView pic;
        ContactHolder(View view){
            super(view);
            name = view.findViewById(R.id.name);
            pic = view.findViewById(R.id.profile_image);
        }
    }
}
