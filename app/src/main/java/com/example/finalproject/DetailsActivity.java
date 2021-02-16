package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.finalproject.databinding.ActivityDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailsActivity extends AppCompatActivity {
    public static final String MODEL_DETAILS = "model_details";
    private static final int REQUEST_PHONE_CALL = 113;
    ActivityDetailsBinding mBinding;
    PropertyDetails mPropertyDetails;
    private FirebaseFirestore fireStore;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        getSupportActionBar().setTitle("Property Details");
        progressDialog = new ProgressDialog(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPropertyDetails = (PropertyDetails) getIntent().getParcelableExtra(MODEL_DETAILS);
        if (FirebaseAuth.getInstance()
                .getCurrentUser() != null && FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(mPropertyDetails.getOwnerEmail()))
            mBinding.removeCon.setVisibility(View.VISIBLE);
        setData();
    }

    private void setData() {
        SliderAdapter adapter = new SliderAdapter(DetailsActivity.this,mPropertyDetails.getImageUrl());
        mBinding.image.setAdapter(adapter);
        mBinding.indicators.setNumberOfItems(mPropertyDetails.getImageUrl().size());
        mBinding.indicators.setSelectedItem(0,true);
        mBinding.image.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mBinding.indicators.setSelectedItem(position,true);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBinding.foodName.setText(mPropertyDetails.getPropertyName());
        mBinding.foodPrice.setText(mPropertyDetails.getPrice() + " PKR");
        mBinding.description.setText(mPropertyDetails.getDescription());
        mBinding.whatsapp.setOnClickListener(view -> whatsAppAction(mPropertyDetails.getPhoneNumber()));
        mBinding.phone.setOnClickListener(view -> {
            makePhoneCall(mPropertyDetails.getPhoneNumber());
        });
        mBinding.mapDirection.setOnClickListener(view -> {
            Uri gmmIntentUri = Uri.parse("google.navigation:q="+mPropertyDetails.getGeoPoint().getLatitude()+","+mPropertyDetails.getGeoPoint().getLongitude());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });
        mBinding.showOnMap.setOnClickListener(view -> {

            String uri = "https://www.google.com/maps/search/?api=1&query="+mPropertyDetails.getGeoPoint().getLatitude()+","+mPropertyDetails.getGeoPoint().getLongitude();
            Log.d("locc",uri);
            Uri gmmIntentUri = Uri.parse("geo:"+mPropertyDetails.getGeoPoint().getLatitude()+","+mPropertyDetails.getGeoPoint().getLongitude());

            Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            startActivity(intent);
        });
        mBinding.textmessage.setOnClickListener(view -> {
            Uri smsUri=  Uri.parse("smsto:" + mPropertyDetails.getPhoneNumber());
            Intent smsIntent = new Intent(Intent.ACTION_VIEW, smsUri);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("sms_body","i am interested in a property you posted in real estate app");
            startActivity(smsIntent);
        });
        mBinding.removeButton.setOnClickListener(view -> {
            progressDialog.setMessage("Deleting...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            fireStore = FirebaseFirestore.getInstance();
            fireStore.collection("property_details")
                    .document(mPropertyDetails
                            .getPropertyId())
                    .delete()
                    .addOnCompleteListener(task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(DetailsActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish();

                            startActivity(intent);

                        }
                    });
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void makePhoneCall(String phoneNumber) {
        Intent intent = new Intent(
                Intent.ACTION_CALL);
        intent.setData(Uri.parse(
                "tel:" +
                        phoneNumber));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this
                    .checkSelfPermission(
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                if (intent.resolveActivity(
                        this
                                .getPackageManager()) != null) {
                    startActivity(
                            intent);
                } else {
                    Toast.makeText(this, "Facility not available", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Permission Not Given", Toast.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        REQUEST_PHONE_CALL);

            }
        }
    }

    private void whatsAppAction(String phoneNumber) {
        if (phoneNumber.startsWith("0"))
            phoneNumber.substring(1);
        boolean installed = appInstalledOrNot("com.whatsapp");
        if (installed) {
            Uri uri = Uri.parse(
                    "https://wa.me/92" + (phoneNumber.startsWith("0") ? phoneNumber.substring(1) : phoneNumber) + "?text=Hello!"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Whatsapp is not installed", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = this.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

}