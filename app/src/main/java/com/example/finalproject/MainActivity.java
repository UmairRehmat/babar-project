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

public class MainActivity
        extends AppCompatActivity
        implements View.OnClickListener
{

    private FirebaseAuth firebaseAuth;
    private Button button;
    private EditText editTextmail;
    private EditText editTextPass;
    private TextView textView;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("Sign Up");

//        if (firebaseAuth.getCurrentUser() != null)
//        {
//
//            finish();
//            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//        }


        button = (Button)findViewById(R.id.button);
        editTextmail = (EditText)findViewById(R.id.email);
        editTextPass = (EditText)findViewById(R.id.pass);
        textView = (TextView)findViewById(R.id.textv);

        button.setOnClickListener(this);
        textView.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);

    }

    private void RegisterUsr()
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

        progressDialog.setMessage("User Registering....");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                sendVerificationEmail();
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "Could Not Register",
                                               Toast.LENGTH_LONG)
                                     .show();
                                progressDialog.dismiss();
                            }

                        }
                    });
    }

    private void sendVerificationEmail() {
        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {

                Toast.makeText(MainActivity.this, "Register Successfully verify your email address",
                        Toast.LENGTH_LONG)
                        .show();
                finish();
                startActivity(new Intent(MainActivity.this, Login.class));
                progressDialog.dismiss();
return;
            }
            progressDialog.dismiss();
        });
    }

    @Override
    public void onClick(View v)
    {

        if (v == button)
        {
            RegisterUsr();
        }
        if (v == textView)
        {
            finish();
            startActivity(new Intent(this, Login.class));
        }
    }
}
