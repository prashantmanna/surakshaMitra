package com.surakshamitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.surakshamitra.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActivityMainBinding mainBinding;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    ImageView camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        mainBinding.navBar.setBackground(null);


        bottomNavigationView = findViewById(R.id.navBar);
        frameLayout = findViewById(R.id.container);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.homepage) {
                    replaceFragment(new homefragment(), false);
                } else if (id == R.id.alert) {
                    replaceFragment(new Alert(), false);
                } else if (id == R.id.chats) {
                    replaceFragment(new chatfragment(), false);
                } else if (id == R.id.tips) {
                    replaceFragment(new safetyfragment(), false);
                } else if (id == R.id.Contacts) {
                    replaceFragment(new contactFragment(), false);
                }
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.homepage);
    }



    protected void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (addToBackStack) {
            fragmentTransaction.add(R.id.container, fragment);
            fragmentTransaction.addToBackStack(null);
        } else {
            fragmentTransaction.replace(R.id.container, fragment);
        }

        fragmentTransaction.commit();
    }
}
