package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
                            if(firebaseAuth.getCurrentUser().isEmailVerified()) {

                                Toast.makeText(Login.this, "Successfully SignIn", Toast.LENGTH_LONG)
                                        .show();
                                startActivity(
                                        new Intent(getApplicationContext(), HomeActivity.class));
                            return;
                            }
                            new AlertDialog.Builder(Login.this)
                                    .setTitle("Verification Failed")
                                    .setMessage("Please Check your email to verify. or get new verification email but clicking below")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("Resend verification email", (dialog, which) -> {
                                        if (which==-1)
                                        {
                                            progressDialog.show();
                                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressDialog.dismiss();
                                                    firebaseAuth.signOut();
                                                }
                                            });

                                        }
                                        // Continue with delete operation
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton("Dismiss", (dialogInterface, i) -> {
                                        firebaseAuth.signOut();
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

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
            finish();
            startActivity(new Intent(this, ForgortPasswordActivity.class));
        }
        if (v==buttonSignUp)
        {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

    }
}
