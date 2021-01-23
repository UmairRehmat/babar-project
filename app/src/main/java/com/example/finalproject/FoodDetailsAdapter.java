package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FoodDetailsAdapter
        extends RecyclerView.Adapter<FoodDetailsAdapter.foodDetailsViewHolder>
{
    private ArrayList<PropertyDetails> mPropertyDetailsList;
    private Context mContext;
    private OnOrderClickListener listener;
    private OnOrderClickListener dellListener;

    public FoodDetailsAdapter(ArrayList<PropertyDetails> mPropertyDetailsList, Context mContext, OnOrderClickListener listener, OnOrderClickListener dellListener)
    {
        this.mPropertyDetailsList = mPropertyDetailsList;
        this.mContext = mContext;
        this.listener = listener;
        this.dellListener = dellListener;
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
        if (FirebaseAuth.getInstance()
                        .getCurrentUser() == null || !FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(propertyDetails.getOwnerEmail()))
            holder.delete.setVisibility(View.GONE);

        holder.foodName.setText(  propertyDetails.getFoodName());
        holder.foodPrice.setText( propertyDetails.getPrice());
        holder.contact.setText( propertyDetails.getLocation());
        Glide.with(mContext)
             .load(propertyDetails.getImageUrl())
             .placeholder(R.drawable.ic_menu_gallery)
             .into(holder.foodImage);
        holder.button.setOnClickListener(v -> {
            if (listener != null)
                listener.onOrderButtonClick(position);
        });
        holder.delete.setOnClickListener(v -> {
            if (dellListener != null)
                dellListener.onOrderButtonClick(position);
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
        private Button button;
        private Button delete;

        public foodDetailsViewHolder(@NonNull View itemView)
        {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            contact = itemView.findViewById(R.id.property_description);
            button = itemView.findViewById(R.id.order);
            delete = itemView.findViewById(R.id.delete);
        }
    }


    public interface OnOrderClickListener
    {
        void onOrderButtonClick(int position);
    }
}
