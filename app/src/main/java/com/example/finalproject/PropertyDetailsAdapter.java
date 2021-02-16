package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PropertyDetailsAdapter
        extends RecyclerView.Adapter<PropertyDetailsAdapter.foodDetailsViewHolder>
{
    private ArrayList<PropertyDetails> mPropertyDetailsList;
    private Context mContext;
    private OnOrderClickListener listener;
    private OnOrderClickListener dellListener;
    private OnOrderClickListener whatsappListener;
    private OnOrderClickListener cardListener;

    public PropertyDetailsAdapter(ArrayList<PropertyDetails> mPropertyDetailsList, Context mContext, OnOrderClickListener listener, OnOrderClickListener dellListener, OnOrderClickListener whatsappListener, OnOrderClickListener cardListener)
    {
        this.mPropertyDetailsList = mPropertyDetailsList;
        this.mContext = mContext;
        this.listener = listener;
        this.dellListener = dellListener;
        this.whatsappListener = whatsappListener;
        this.cardListener = cardListener;

    }


    @NonNull
    @Override
    public foodDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.recyclerview_list_layout, parent, false);

        return new foodDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull foodDetailsViewHolder holder, int position)
    {
        PropertyDetails propertyDetails = mPropertyDetailsList.get(position);

        holder.foodName.setText(  propertyDetails.getPropertyName());
        holder.foodPrice.setText( propertyDetails.getPrice());
        holder.contact.setText( propertyDetails.getLocation());
        Glide.with(mContext)
             .load(propertyDetails.getImageUrl().get(0))
             .placeholder(R.drawable.imager_loader)
             .into(holder.foodImage);
        holder.button.setOnClickListener(v -> {
            if (listener != null)
                listener.onOrderButtonClick(position);
        });
        holder.delete.setOnClickListener(v -> {
            if (dellListener != null)
                dellListener.onOrderButtonClick(position);
        });
        holder.whatsapp.setOnClickListener(v -> {
            if (whatsappListener != null)
                whatsappListener.onOrderButtonClick(position);
        });
        holder.cardView.setOnClickListener(v -> {
            if (cardListener != null)
                cardListener.onOrderButtonClick(position);
        });
    }

    @Override
    public int getItemCount()
    {
        return mPropertyDetailsList.size();
    }

    public class foodDetailsViewHolder
            extends RecyclerView.ViewHolder
    {
        private ImageView foodImage;
        private TextView foodName;
        private TextView foodPrice;
        private TextView contact;
        private ImageView button;
        private ImageView delete;
        private ImageView whatsapp;
        private FrameLayout deleteCon;
        private CardView cardView;

        public foodDetailsViewHolder(@NonNull View itemView)
        {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            contact = itemView.findViewById(R.id.property_description);
            button = itemView.findViewById(R.id.order);
            delete = itemView.findViewById(R.id.delete);
            whatsapp = itemView.findViewById(R.id.whatsapp);
            deleteCon = itemView.findViewById(R.id.delete_con);
            cardView = itemView.findViewById(R.id.main_card);
        }
    }


    public interface OnOrderClickListener
    {
        void onOrderButtonClick(int position);
    }
}
