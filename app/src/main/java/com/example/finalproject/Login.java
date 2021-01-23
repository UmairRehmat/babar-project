package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login
        extends AppCompatActivity
        implements View.OnClickListener
{

    private Button buttonsignIn;
    private Button buttonSignUp;
    private EditText editTextmail;
    private EditText editTextPass;
    private TextView textView;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("Login");

        //  if (firebaseAuth.getCurrentUser() != null)
        //  {
        //      finish();
        //      Toast.makeText(Login.this ,"Successfully SignIn",Toast.LENGTH_LONG).show();
        //      startActivity(new Intent(getApplicationContext() , ProfileActivity.class));
        //  }


        buttonsignIn = findViewById(R.id.button);
        buttonSignUp = findViewById(R.id.signupButton);
        editTextmail = findViewById(R.id.email);
        editTextPass = findViewById(R.id.pass);
        textView = findViewById(R.id.textv);

        buttonsignIn.setOnClickListener(this);
        textView.setOnClickListener(this);
        buttonSignUp.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);

    }

    private void userLogin()
    {

        String email = editTextmail.getText()
                                   .toString()
                                   .trim();
        String password = editTextPass.getText()
                                      .toString()
                                      .trim();

        if (TextUtils.isEmpty(email))
        {

            Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_SHORT)
                 .show();
            return;
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT)
                 .show();
            return;
        }


        progressDialog.setMessage("Login Progress.....");
        progressDialog.show();


        firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful())
                        {
                            //Profile Activity will be here
                            Toast.makeText(Login.this, "Successfully SignIn", Toast.LENGTH_LONG)
                                 .show();
                            startActivity(
                                    new Intent(getApplicationContext(), HomeActivity.class));

                        }
                        else
                        {
                            Toast.makeText(Login.this, "Couldn't SignIn"+task.getException().getLocalizedMessage(), Toast.LENGTH_LONG)
                                 .show();
                        }

                    });

    }

    @Override
    public void onClick(View v)
    {

        if (v == buttonsignIn)
        {

            userLogin();
        }
        if (v == textView)
        {


        }
        if (v==buttonSignUp)
        {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

    }
}
