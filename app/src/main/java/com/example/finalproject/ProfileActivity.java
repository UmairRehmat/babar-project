package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.schibstedspain.leku.LekuPoi;
import com.schibstedspain.leku.LocationPickerActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import io.nlopez.smartlocation.SmartLocation;

import static android.provider.ContactsContract.CommonDataKinds.Email.ADDRESS;
import static android.provider.MediaStore.Video.VideoColumns.LATITUDE;
import static android.provider.MediaStore.Video.VideoColumns.LONGITUDE;

public class ProfileActivity
        extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 121;
    private static final int RESULT_CODE_LOCATION = 122;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser FirebaseUser;

    private static final int pick_image_request = 1;
    private ImageView chose_button;
    private Button upload_button;
    private TextView textView;
    private EditText edit_file_name;
    private ProgressBar progressBar;
    private String mImageUploadUrl;
    private FirebaseAuth mAuth;
    private EditText foodName;
    private EditText price;
    private EditText description;
    private TextView location;
    private EditText phoneNumber;
    private Button saveData;
    private ProgressDialog progressDialog;
    private List<String> imagesList=new ArrayList<>();
    private Uri image_uri;
    private GeoPoint mGeoPointLocation;
    private RecyclerView mPhotoRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        chose_button = findViewById(R.id.chose_file);
        upload_button = findViewById(R.id.upload_image);
        getSupportActionBar().setTitle("Add Menu");
        textView = findViewById(R.id.view_upoad);
        progressBar = findViewById(R.id.progress_bar);
        mAuth = FirebaseAuth.getInstance();
        foodName = findViewById(R.id.food_name);
        price = findViewById(R.id.food_price);
        location = findViewById(R.id.location);
        saveData = findViewById(R.id.save_data);
        description = findViewById(R.id.property_description);
        phoneNumber = findViewById(R.id.phone_number);
        mPhotoRecyclerView = findViewById(R.id.images_recycler_view);
        progressDialog = new ProgressDialog(this);
        saveData.setOnClickListener(v -> {
            if (mImageUploadUrl == null) {
                Toast.makeText(this, "upload image first", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            if (TextUtils.isEmpty(foodName.getText()
                    .toString())) {
                foodName.setError("required");
                foodName.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(price.getText()
                    .toString())) {
                price.setError("required");
                price.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(description.getText()
                    .toString())) {

                description.setError("required");
                description.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(location.getText()
                    .toString())|| location.getText().equals("Property Location")) {

                location.setError("required");
                location.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(phoneNumber.getText()
                    .toString())) {

                phoneNumber.setError("required");
                phoneNumber.requestFocus();
                return;
            }

            saveDataToFireBase();

        });
        location.setOnClickListener(view -> {
            takeGoogleMapPermission();
        });
        chose_button.setOnClickListener(v -> pickImage());

        upload_button.setOnClickListener(v -> {
            if (image_uri == null) {
                Toast.makeText(ProfileActivity.this, "select image first", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            progressDialog.setMessage("image uploading.......");
            progressDialog.setCancelable(false);
            progressDialog.show();
            GetImageUrl(image_uri, mAuth.getCurrentUser()
                    .getUid(), Url -> {
                progressDialog.dismiss();
                mImageUploadUrl = Url;
                imagesList.add(mImageUploadUrl);
                image_uri=null;
                chose_button.setImageDrawable(ProfileActivity.this.getDrawable(R.drawable.add_ic));
                setUpPhotosRecyclerView();
                Toast.makeText(ProfileActivity.this, "upload successfully", Toast.LENGTH_SHORT)
                        .show();
            });
        });

        textView.setOnClickListener(v -> {

        });


        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {

            finish();
            startActivity(new Intent(this, Login.class));
        }
        FirebaseUser = firebaseAuth.getCurrentUser();


    }

    private void setUpPhotosRecyclerView() {
        PhotosAdapter adapter = new PhotosAdapter(imagesList);
        mPhotoRecyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this,LinearLayoutManager.HORIZONTAL,false));
        mPhotoRecyclerView.setAdapter(adapter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        progressDialog.dismiss();
    }

    //************************************************************************************
    public  void takeGoogleMapPermission()
    //************************************************************************************
    {
        int MyVersion = Build.VERSION.SDK_INT;

        if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            ActivityCompat.requestPermissions(ProfileActivity.this,
                    new String[] { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, },
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        if (!getGPSStatus())
        {
            showGPSSnackBar(
                    getWindow()
                            .getDecorView()
                            .getRootView(),
                    ProfileActivity.this);
            return;
        }
        progressDialog.setMessage("Loading Map.....");
        progressDialog.show();
        openLocationPickerScreen();

    }


    //**************************************************************
    private void openLocationPickerScreen()
    //**************************************************************
    {
        SmartLocation.with(ProfileActivity.this)
                .location()
                .start(location -> {
                    Intent locationPickerIntent = new LocationPickerActivity.Builder()
                            .withLocation(location.getLatitude(),
                                    location.getLongitude())
                            .withGeolocApiKey(getString(R.string.google_maps_key))
                            .shouldReturnOkOnBackPressed()
                            .withSatelliteViewHidden()
                            .withGooglePlacesEnabled()
                            .withSearchZone("en_PK")
//                            .withGoogleTimeZoneEnabled()
                            .build(ProfileActivity.this);

                    progressDialog.dismiss();
                    startActivityForResult(locationPickerIntent, RESULT_CODE_LOCATION);
                });
    }
    //************************************************************************************
    public void showGPSSnackBar(View view, Activity activity)
    //************************************************************************************
    {
        Snackbar snackbar = Snackbar
                .make(view, getString(R.string.location_disable), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.action_settings),
                        new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                Intent intent1 = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                activity.startActivity(intent1);

                            }
                        });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = new TextView(activity);
        textView.setTextColor(Color.YELLOW);

        snackbar.show();

    }
    //******************************************************************
    public boolean getGPSStatus()
    //******************************************************************
    {
        LocationManager locationManager;
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressDialog = new ProgressDialog(this);
    }

    private void saveDataToFireBase() {
        progressDialog.setMessage("Uploading Data....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String foodId = UUID.randomUUID()
                .toString();
        PropertyDetails propertyDetails = new PropertyDetails(foodId, foodName.getText()
                .toString()
                .trim(), imagesList,
                firebaseAuth.getCurrentUser()
                        .getUid(), price.getText()
                .toString()
                .trim(),
                description.getText()
                        .toString(), location.getText().toString(), firebaseAuth.getCurrentUser().getEmail(), phoneNumber.getText().toString(),mGeoPointLocation);
        firestore.collection("property_details")
                .document(foodId)
                .set(propertyDetails)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(this, "uploaded successfully", Toast.LENGTH_SHORT)
                                .show();
                        finish();
                        startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.go_home, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home_menu)
            finish();
        startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            if (image != null) {
                image_uri = Uri.fromFile(new File(image.getPath()));
                Glide.with(this)
                        .load(image_uri.toString())
                        .into(chose_button);
            }
        }
        if (resultCode == Activity.RESULT_OK && data != null)
        {
            Log.d("RESULT****", "OK");
            if (requestCode == RESULT_CODE_LOCATION)
            {
                double latitude = data.getDoubleExtra(LATITUDE, 0.0);
                Log.d("LATITUDE****", "" + latitude);
                double longitude = data.getDoubleExtra(LONGITUDE, 0.0);
                Log.d("LONGITUDE****", "" + longitude);
                mGeoPointLocation = new GeoPoint(latitude, longitude);
                Geocoder geocoder;
                List<Address> addresses = new ArrayList<>();
                geocoder = new Geocoder(ProfileActivity.this, Locale.getDefault());

                try
                {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                String address = addresses.get(0)
                        .getAddressLine(0);
                location.setText(address);
                Log.d("ADDRESS****", address.toString());

                Parcelable[] fullAddress = data.getParcelableArrayExtra(ADDRESS);
                if (fullAddress != null)
                {
                    Log.d("FULL ADDRESS****", fullAddress.toString());
                }
            }
        }
    }

    private void pickImage() {
        ImagePicker.create(this)
                .single()
                .start();
    }

    //****************************************************************************************************
    private void GetImageUrl(@NonNull Uri imageURI, @NonNull String userId, ImageUploadedListener listener)
    //****************************************************************************************************
    {
        String imageId = UUID.randomUUID()
                .toString();
        StorageReference storageReference = FirebaseStorage.getInstance()
                .getReference(
                        "userPhotos/food/photos/" + userId + "/" + imageId);

        StorageReference filePath = storageReference.child(userId);
        Task<Uri> uriTask = filePath.putFile(imageURI)
                .continueWithTask(
                        task -> {
                            if (!task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this,
                                        task.getException()
                                                .getLocalizedMessage(),
                                        Toast.LENGTH_SHORT)
                                        .show();
                                throw task.getException();
                            }
                            return filePath.getDownloadUrl();
                        })
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri mImageIntentURI = task.getResult();
                        if (listener == null)
                            return;
                        listener.onImageUploaded(mImageIntentURI.toString());
                        //   Log.d(TAG, "onComplete: Url: " + downUri.toString());
                    }
                })
                .addOnFailureListener(
                        e -> Log.d("reeeerr", e.getLocalizedMessage()));

    }
}


//**********************************************
interface ImageUploadedListener
//**********************************************
{
    void onImageUploaded(String Url);
}

