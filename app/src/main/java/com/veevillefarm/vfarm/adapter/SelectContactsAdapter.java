package com.veevillefarm.vfarm.adapter;

import android.content.Context;
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
import com.veevillefarm.vfarm.helper.ChatContact;
import com.veevillefarm.vfarm.helper.CircleTransform;

import java.util.List;

/**
 * Created by Prashant C on 07/01/19.
 */
public class SelectContactsAdapter extends RecyclerView.Adapter<SelectContactsAdapter.ContactHolder> {

    private Context context;
    private SelectedContactsInterface anInterface;
    private List<ChatContact> contacts;
    public SelectContactsAdapter(Context context, List<ChatContact> contacts,SelectedContactsInterface anInterface){
        this.context = context;
        this.contacts = contacts;
        this.anInterface = anInterface;
    }
    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_card,parent,false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactHolder holder, int position) {
        final ChatContact contact = contacts.get(position);
        holder.name.setText(contact.name);
        holder.email.setText(contact.email);
        Picasso.with(context).load(contact.picUrl).resize(500, 500).centerCrop().transform(new CircleTransform()).into(holder.selectedContact);
        holder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.contact.getTag().toString().equals("Selected")){
                    holder.contact.setTag("UnSelected");
                    Picasso.with(context).load(contact.picUrl).resize(500, 500).centerCrop().transform(new CircleTransform()).into(holder.selectedContact);
                    anInterface.removeContactFromSelectedList(contact);
                }else {
                    holder.contact.setTag("Selected");
                    holder.selectedContact.setImageResource(R.drawable.ic_select_green_icon);
                    anInterface.addCOntactToSelectedList(contact);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ContactHolder extends RecyclerView.ViewHolder{
        ImageView selectedContact;
        TextView name,email;
        CardView contact;
        ContactHolder(View view){
            super(view);
            selectedContact = view.findViewById(R.id.profile_picture);
            contact = view.findViewById(R.id.contact_card);
            name = view.findViewById(R.id.name);
            email = view.findViewById(R.id.most_recent_message);
        }
    }
    public interface SelectedContactsInterface{
        void addCOntactToSelectedList(ChatContact contact);
        void removeContactFromSelectedList(ChatContact contact);
    }
}
