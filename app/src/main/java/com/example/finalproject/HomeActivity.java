package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeActivity
        extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private ArrayList<FoodDetails> mFoodDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.food_recycler_view);
        progressDialog = new ProgressDialog(this);
        loadFoodData();
    }

    private void loadFoodData()
    {
        progressDialog.setMessage("data loading.....");
        progressDialog.show();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("food")
                 .get()
                 .addOnSuccessListener(queryDocumentSnapshots -> {
                     mFoodDetailsList = new ArrayList<>();
                     for (DocumentSnapshot childData : queryDocumentSnapshots)
                     {
                         FoodDetails foodDetails = childData.toObject(FoodDetails.class);
                         mFoodDetailsList.add(foodDetails);
                     }
                     progressDialog.dismiss();
                     initializeRecyclerView();
                 });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.goto_profile, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.add_profile)
            finish();
        startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        return super.onOptionsItemSelected(item);
    }


    private void initializeRecyclerView()
    {
        FoodDetailsAdapter adapter = new FoodDetailsAdapter(mFoodDetailsList, HomeActivity.this,
                                                            position -> {
                                                                new AlertDialog.Builder(this)
                                                                        .setMessage(
                                                                                "order send for: " + mFoodDetailsList.get(
                                                                                        position)
                                                                                                                     .getFoodName())
                                                                        .setTitle("order sent")
                                                                        .setCancelable(false)
                                                                        .setPositiveButton(
                                                                                "ok", null)
                                                                        .create()
                                                                        .show();
                                                            });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

}
