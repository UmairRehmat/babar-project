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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgortPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Button button;
    private EditText editTextmail;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgort_password);

        firebaseAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("Reset Password");
        button = (Button)findViewById(R.id.button);
        editTextmail = (EditText)findViewById(R.id.email);
        textView = (TextView)findViewById(R.id.textv);
        button.setOnClickListener(this);
        textView.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        if (v == button)
        {
            sendResetLink();
        }
        if (v == textView)
        {
            gotoLogin();
        }
    }

    private void gotoLogin() {
        finish();
        startActivity(new Intent(this, Login.class));
    }

    private void sendResetLink() {
        String email = editTextmail.getText()
                .toString()
                .trim();
        if (TextUtils.isEmpty(email))
        {

            Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        progressDialog.setMessage("User Registering....");
        progressDialog.show();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                  new  AlertDialog.Builder(ForgortPasswordActivity.this)
                            .setMessage("Reset password email sent to your email")
                            .setTitle("Success")

                            .setCancelable(false)
                            .setPositiveButton(
                                    "OK", (dialogInterface, i) -> gotoLogin())
                            .create()
                            .show();
                    return;
                }
                Toast.makeText(ForgortPasswordActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ForgortPasswordActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}