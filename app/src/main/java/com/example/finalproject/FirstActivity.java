package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class FirstActivity
        extends AppCompatActivity
{

    private Button homeButton;
    private Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        homeButton = findViewById(R.id.home_btn);
        loginButton = findViewById(R.id.login);
        homeButton.setOnClickListener(v -> openRecylerView());
        loginButton.setOnClickListener(v -> gotoLoginActivity());


    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loginButton.setVisibility(FirebaseAuth.getInstance()
                                              .getCurrentUser() != null ? View.GONE : View.VISIBLE);

    }

    private void gotoLoginActivity()
    {
        startActivity(new Intent(FirstActivity.this, Login.class));
    }

    public void openRecylerView()
    {

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

}
