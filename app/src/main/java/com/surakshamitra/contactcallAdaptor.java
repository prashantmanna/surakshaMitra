package com.surakshamitra;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class contactcallAdaptor extends RecyclerView.Adapter<contactcallAdaptor.ViewHolder> {

    Context context;
    ArrayList<model> arrContact;

    private static int lastposition = -1;

    contactcallAdaptor(Context context, ArrayList<model> arrContact) {
        this.context = context;
        this.arrContact = arrContact;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_list, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        model contact = arrContact.get(position);

        // Bind the contact data to the views in the ViewHolder
        holder.txtContact.setText(contact.getContact());
        holder.txtNumber.setText(contact.getNumber());

        // Set image resource using resource identifier
        int imageResource = contact.getImg(); // Assuming getImageResource() returns the resource identifier
        holder.img.setImageResource(imageResource);

        // Apply animation
        holder.setAnimation(holder.itemView, position);

        // Set click listener for the layout containing the contact information
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ACTION_CALL intent when the layout is clicked
                String phoneNumber = contact.getNumber();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                context.startActivity(callIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrContact.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtContact, txtNumber;
        LinearLayout layout;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            txtContact = itemView.findViewById(R.id.contact);
            txtNumber = itemView.findViewById(R.id.number);
            img = itemView.findViewById(R.id.user);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Get the clicked contact
                        model clickedContact = arrContact.get(position);

                    }
                }
            });
        }

        private void setAnimation(View view, int position) {
            if (position > lastposition) {
                Animation slide = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
                view.startAnimation(slide);
                lastposition = position;
            }
        }
    }
}
