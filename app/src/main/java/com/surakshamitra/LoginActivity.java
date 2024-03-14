package com.surakshamitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class LoginActivity extends AppCompatActivity {

    public  static String LoginID = "MyProfile";
    Dialog dialog;
    TextInputEditText Email,Password;

    Button loginButton;

    ProgressBar progressBar;

    FirebaseAuth authUser;

    TextView register,forgotPassword;
    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        Email = findViewById(R.id.editTextEmail);
        Password = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.btnLogin);
        register = findViewById(R.id.registerIcon);
        progressBar = findViewById(R.id.progressBar1);
        authUser = FirebaseAuth.getInstance();
        forgotPassword = findViewById(R.id.forgot);





        loginButton.setOnClickListener(v -> {
            String email = String.valueOf(Email.getText());
            String password = String.valueOf(Password.getText());





            if (TextUtils.isEmpty(email)) {

                    Email.setError("Please Enter Your Email");
                    Email.requestFocus();
            }
            else if (TextUtils.isEmpty(email)||!email.contains("@")) {
                Email.setError("Email is not Valid");
                Email.requestFocus();

            } else if (TextUtils.isEmpty(password) || password.length()<6) {

                    Password.setError("Password should be valid");
                    Password.requestFocus();
                }
            else {
                progressBar.setVisibility(View.VISIBLE);
                loginUser(email,password);
            }

        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iRegister = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(iRegister);
                finish();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "You can reset your password now!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,ForgetActivity.class));

            }
        });




    }

    private void loginUser(String email, String password) {
        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            if(!password.isEmpty()) {
                authUser.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                                SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.LoginID, 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("hasLoggedIn", true);
                                editor.apply();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
            else
            {
                Password.setError("Password Is Empty");
            }

        } else if (email.isEmpty()) {
            Email.setError("Email is Empty");
            
        }
        else {
            Email.setError("Please Enter email");
        }


    }


    public void checkUser()
    {
        String username = Email.getText().toString();
        String password = Password.getText().toString();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered users");
        Query checkUser = databaseReference.orderByChild("username").equalTo("userEmail");

    }

    @Override
    protected void onDestroy() {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
    }

}