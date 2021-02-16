package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class HomeActivity
        extends AppCompatActivity
{
    private static final int REQUEST_PHONE_CALL = 113;
    private static final String FOR_MY = "for_my";
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private ArrayList<PropertyDetails> mPropertyDetailsList;
    private FirebaseFirestore fireStore;
    private FirebaseAuth firebaseAuth;
    private MenuItem myAds;
    private MenuItem homeMenu;
   static private boolean myProfile = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.food_recycler_view);
        progressDialog = new ProgressDialog(this);
        if (myProfile)
            getSupportActionBar().setTitle("My Ads");
else
        getSupportActionBar().setTitle("Home");
        loadFoodData();
    }

    private void loadFoodData()
    {
        progressDialog.setMessage("DATA LOADING !!!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        fireStore = FirebaseFirestore.getInstance();
        Query query = myProfile?fireStore.collection("property_details").whereEqualTo("ownerEmail",firebaseAuth.getCurrentUser().getEmail()):fireStore.collection("property_details");
               query  .get()
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
         if (myProfile)
          menu.findItem(R.id.my_ads).setVisible(false);
         else
            menu.findItem(R.id.home_menu_top).setVisible(false);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.add_profile) {
            finish();
            Intent intent =new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.my_ads) {
//            myAds.setVisible(false);
//            homeMenu.setVisible(true);
            myProfile = true;
//            invalidateOptionsMenu();
            finish();
            Intent intent =new Intent(HomeActivity.this, HomeActivity.class);
            intent.putExtra(FOR_MY,true);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.home_menu_top) {
//            myAds.setVisible(true);
//            homeMenu.setVisible(false);
//            invalidateOptionsMenu();
            myProfile = false;
            finish();
            Intent intent =new Intent(HomeActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        else  if (item.getItemId() == R.id.logout)
        {
            logoutUser();
        }
        return super.onOptionsItemSelected(item);
    }


    private void initializeRecyclerView()
    {
        PropertyDetailsAdapter adapter = new PropertyDetailsAdapter(mPropertyDetailsList, HomeActivity.this,
                                                            position ->makePhoneCall(mPropertyDetailsList.get(position).getPhoneNumber()), position -> {
            progressDialog.setMessage("Deleting...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            fireStore.collection("property_details")
                     .document(mPropertyDetailsList.get(position)
                                               .getPropertyId())
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
        },position -> {
whatsAppAction(mPropertyDetailsList.get(position).getPhoneNumber());
        }, position -> {
            Intent intent = new Intent(HomeActivity.this,DetailsActivity.class);
            intent.putExtra(DetailsActivity.MODEL_DETAILS,mPropertyDetailsList.get(position));
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void whatsAppAction(String phoneNumber) {
        if (phoneNumber.startsWith("0"))
            phoneNumber.substring(1);
        boolean installed = appInstalledOrNot("com.whatsapp");
        if (installed)
        {
            Uri uri = Uri.parse(
                    "https://wa.me/92"+(phoneNumber.startsWith("0")?phoneNumber.substring(1):phoneNumber)+"?text=Hello!"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Whatsapp is not installed", Toast.LENGTH_LONG)
                    .show();
        }
    }
    private boolean appInstalledOrNot(String uri)
    {
        PackageManager pm = this.getPackageManager();
        boolean app_installed;
        try
        {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            app_installed = false;
        }
        return app_installed;
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
