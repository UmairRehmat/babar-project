package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FoodDetailsAdapter
        extends RecyclerView.Adapter<FoodDetailsAdapter.foodDetailsViewHolder>
{
    private ArrayList<FoodDetails> mFoodDetailsList;
    private Context mContext;
    private OnOrderClickListener listener;

    public FoodDetailsAdapter(ArrayList<FoodDetails> mFoodDetailsList, Context mContext, OnOrderClickListener listener)
    {
        this.mFoodDetailsList = mFoodDetailsList;
        this.mContext = mContext;
        this.listener = listener;
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
        FoodDetails foodDetails = mFoodDetailsList.get(position);
        holder.foodName.setText(foodDetails.getFoodName());
        holder.foodPrice.setText(foodDetails.getPrice());
        holder.contact.setText(foodDetails.getNumber());
        Glide.with(mContext)
             .load(foodDetails.getImageUrl())
             .placeholder(R.drawable.ic_menu_gallery)
             .into(holder.foodImage);
        holder.button.setOnClickListener(v -> {
            if (listener != null)
                listener.onOrderButtonClick(position);
        });
    }

    @Override
    public int getItemCount()
    {
        return mFoodDetailsList.size();
    }

    public class foodDetailsViewHolder
            extends RecyclerView.ViewHolder
    {
        private ImageView foodImage;
        private TextView foodName;
        private TextView foodPrice;
        private TextView contact;
        private Button button;

        public foodDetailsViewHolder(@NonNull View itemView)
        {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            contact = itemView.findViewById(R.id.phone_number);
            button = itemView.findViewById(R.id.order);
        }
    }


    public interface OnOrderClickListener
    {
        void onOrderButtonClick(int position);
    }
}
