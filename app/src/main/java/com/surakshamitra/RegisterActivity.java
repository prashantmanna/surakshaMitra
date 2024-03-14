package com.surakshamitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.surakshamitra.databinding.ActivityRegisterBinding;

import java.util.jar.Attributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    ActivityRegisterBinding activityRegisterBinding;
    TextInputEditText Name,Email,Password,MobileNo,ConfirmPassword;
    Button Register;
    ProgressBar progressBar;

    FirebaseAuth auth;
    FirebaseUser user;


    private static final String TAG="RegisterActivity";
    TextView Login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toast.makeText(RegisterActivity.this, "You Can register Now", Toast.LENGTH_SHORT).show();

        Name = findViewById(R.id.edtName);
        Email = findViewById(R.id.edtEmail);
        Password = findViewById(R.id.edtPassword);
        MobileNo = findViewById(R.id.edtMobile);
        ConfirmPassword = findViewById(R.id.edtConfirmPassword);
        Login = findViewById(R.id.loginIcon);
        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        Register = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progress);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActivity.this, "You can Login Now", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = String.valueOf(Name.getText());
                String userEmail = String.valueOf(Email.getText());
                String userPassword = String.valueOf(Password.getText());
                String Mobile = String.valueOf(MobileNo.getText());
                String PasswordConfirm = String.valueOf(ConfirmPassword.getText());





                if(TextUtils.isEmpty(username)){
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
                    Name.setError("Full Name Is Required");
                    Name.requestFocus();
                } else if (TextUtils.isEmpty(userEmail)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                    Email.setError("Email is required");
                    Email.requestFocus();
                    
                } else if (TextUtils.isEmpty(userEmail)) {
                    Email.setError("Email Field cannot be empty");
                    Email.requestFocus();

                } else if (TextUtils.isEmpty(Mobile)) {

                    MobileNo.setError("Mobile No. is required");
                    MobileNo.requestFocus();

                }

                 else if (userPassword.isEmpty()) {

                    Password.setError("Enter Password");
                    Password.requestFocus();
                    
                }
                else if (userPassword.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Please Should Be 6 digits", Toast.LENGTH_SHORT).show();
                    Password.setError("Password is Weak");
                    Password.requestFocus();
                    
                } else if (TextUtils.isEmpty(PasswordConfirm) || !PasswordConfirm.equals(userPassword)) {

                    ConfirmPassword.setError("Password Should be Valid");
                    ConfirmPassword.requestFocus();


                }
                else {
                    progressDialog.setMessage("Please Wait While Registration......");
                    progressDialog.setTitle("Registration");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    registerUser(username,userEmail,userPassword,Mobile,PasswordConfirm);
                }
            }
        });



    }

    private void registerUser(String username, String userEmail, String userPassword, String mobile, String passwordConfirm) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //create new user
        auth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "user registered successfully", Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    if(firebaseUser != null) {
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                        firebaseUser.updateProfile(profileChangeRequest);
                        //create class
                        userDetails user = new userDetails(userEmail, mobile,userPassword, passwordConfirm);

                        //extracting user details or reference of users
                        DatabaseReference userDetails = FirebaseDatabase.getInstance().getReference("Registered users");
                        userDetails.child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    sendToNextActivity();
                                    Toast.makeText(RegisterActivity.this, "Registered Successfully,Please Verify Your Email", Toast.LENGTH_SHORT).show();


                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();

                                }
                                progressBar.setVisibility(View.GONE);

                            }
                        });
                    }


                }else {
                    try {
                        throw task.getException();

                    }
                    catch (Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    private void sendToNextActivity() {
        Intent inext = new Intent(RegisterActivity.this,MainActivity.class);
        inext.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(inext);
    }
}