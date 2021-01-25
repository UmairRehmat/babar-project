package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.UUID;

public class ProfileActivity
        extends AppCompatActivity {

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
    private EditText location;
    private EditText phoneNumber;
    private Button saveData;
    private ProgressDialog progressDialog;


    private Uri image_uri;


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
                    .toString())) {

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

    @Override
    protected void onPause() {
        super.onPause();
        progressDialog.dismiss();
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
                .trim(), mImageUploadUrl,
                firebaseAuth.getCurrentUser()
                        .getUid(), price.getText()
                .toString()
                .trim(),
                description.getText()
                        .toString(), location.getText().toString(), firebaseAuth.getCurrentUser().getEmail(), phoneNumber.getText().toString());
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

