package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.URI;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private Button buttonlogout;
    private FirebaseUser FirebaseUser;

    private static final int pick_image_request= 1;
    private Button chose_button;
    private Button upload_button;
    private TextView textView;
    private EditText edit_file_name;
    private ImageView imageView;
    private ProgressBar  progressBar;

    private URI image_uri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        chose_button = (Button) findViewById(R.id.chose_file);
        upload_button = (Button) findViewById(R.id.upload);
        textView = (TextView) findViewById(R.id.view_upload);
        edit_file_name = (EditText) findViewById(R.id.text_name);
        imageView = (ImageView) findViewById(R.id.image_View);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        chose_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){

            finish();
            startActivity(new Intent(this , Login.class));
        }
        FirebaseUser = firebaseAuth.getCurrentUser();

        buttonlogout = (Button) findViewById(R.id.logout);
        buttonlogout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v == buttonlogout)

        {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this , Login.class));
        }
    }
}
