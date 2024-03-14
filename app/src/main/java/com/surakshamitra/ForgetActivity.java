package com.surakshamitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity {

    MaterialButton reset, back;
    TextInputEditText edtResetEmail;
    FirebaseAuth auth;

    ProgressBar progress;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        reset = findViewById(R.id.resetBtn);
        edtResetEmail = findViewById(R.id.resetEmail);
        back = findViewById(R.id.backBtn);
        progress = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = String.valueOf(edtResetEmail.getText());
                if (email.isEmpty()) {
                    edtResetEmail.setError("Email ID is required");
                } else {
                    forgotPass();
                }

            }

        });


    }

    private void forgotPass() {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgetActivity.this, "Verify Your Email", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgetActivity.this, LoginActivity.class));
                    finish();
                    progress.setVisibility(View.VISIBLE);
                    reset.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(ForgetActivity.this, "Error ", Toast.LENGTH_SHORT).show();
                    reset.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}


