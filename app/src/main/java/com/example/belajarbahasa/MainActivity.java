package com.example.belajarbahasa;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.belajarbahasa.activities.DashboardActivity;
import com.example.belajarbahasa.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()) {
            // User sudah login dan email sudah diverifikasi
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
        } else {
            // Belum login atau belum verifikasi
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }


        finish(); // Biar MainActivity nggak bisa di-back
    }
}
