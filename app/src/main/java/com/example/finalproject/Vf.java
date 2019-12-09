package com.example.finalproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Vf
        extends AppCompatActivity
{

    private static int splash_time = 2000;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vf);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Vf.this, FirstActivity.class);
            startActivity(intent);
            finish();
        }, splash_time);
    }

}
