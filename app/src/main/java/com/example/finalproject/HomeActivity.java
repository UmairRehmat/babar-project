package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeActivity
        extends AppCompatActivity
{
    private static final int REQUEST_PHONE_CALL = 113;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private ArrayList<PropertyDetails> mPropertyDetailsList;
    private FirebaseFirestore fireStore;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.food_recycler_view);
        progressDialog = new ProgressDialog(this);
        getSupportActionBar().setTitle("Home");
        loadFoodData();
    }

    private void loadFoodData()
    {
        progressDialog.setMessage("DATA LOADING !!!");
        progressDialog.show();
        fireStore = FirebaseFirestore.getInstance();
        fireStore.collection("property_details")
                 .get()
                 .addOnSuccessListener(queryDocumentSnapshots -> {
                     mPropertyDetailsList = new ArrayList<>();
                     for (DocumentSnapshot childData : queryDocumentSnapshots)
                     {
                         PropertyDetails propertyDetails = childData.toObject(PropertyDetails.class);
                         mPropertyDetailsList.add(propertyDetails);
                     }
                     progressDialog.dismiss();
                     initializeRecyclerView();
                 });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (FirebaseAuth.getInstance()
                        .getCurrentUser() == null)
            return false;
        getMenuInflater().inflate(R.menu.goto_profile, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.add_profile) {
            finish();
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        }
        else  if (item.getItemId() == R.id.logout)
        {
            logoutUser();
        }
        return super.onOptionsItemSelected(item);
    }


    private void initializeRecyclerView()
    {
        FoodDetailsAdapter adapter = new FoodDetailsAdapter(mPropertyDetailsList, HomeActivity.this,
                                                            position ->makePhoneCall(mPropertyDetailsList.get(position).getPhoneNumber()), position -> {
            progressDialog.setMessage("Deleting...");
            progressDialog.show();
            fireStore.collection("property_details")
                     .document(mPropertyDetailsList.get(position)
                                               .getFoodId())
                     .delete()
                     .addOnCompleteListener(task -> {
                         if (task.isSuccessful())
                         {
                             progressDialog.dismiss();
                             mPropertyDetailsList.remove(position);
                             initializeRecyclerView();
                             Toast.makeText(this, "delete successful", Toast.LENGTH_SHORT)
                                  .show();
                         }
                     });
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void logoutUser() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(this, Login.class));
    }

    private void makePhoneCall(String phoneNumber)
    {
        Intent intent = new Intent(
                Intent.ACTION_CALL);
        intent.setData(Uri.parse(
                "tel:" +
                        phoneNumber));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (this
                    .checkSelfPermission(
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
            {
                if (intent.resolveActivity(
                        this
                                .getPackageManager()) != null)
                {
                    startActivity(
                            intent);
                }
                else
                {
                    Toast.makeText(this, "Facility not available", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, "Permission Not Given", Toast.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(
                        this,
                        new String[] { Manifest.permission.CALL_PHONE },
                        REQUEST_PHONE_CALL);

            }
        }
    }
}
