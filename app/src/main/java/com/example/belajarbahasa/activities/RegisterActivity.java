package com.example.belajarbahasa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.belajarbahasa.R;
import com.example.belajarbahasa.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword;
    private Button btnRegister;
    private TextView tvLoginLink;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisialisasi EditText
        etUsername = findViewById(R.id.etUsername);  // Tambahkan EditText untuk username
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLoginLink = findViewById(R.id.tvLoginLink);
        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(v -> registerUser());
        tvLoginLink.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();  // Ambil username
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Isi username, email, dan password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Setelah berhasil registrasi, ambil data pengguna
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Simpan data pengguna ke Realtime Database
                        if (user != null) {
                            saveUserDataToDatabase(user, username);
                        }

                        Toast.makeText(RegisterActivity.this, "Registrasi berhasil, silakan login", Toast.LENGTH_SHORT).show();
                        // Setelah berhasil daftar, kembali ke LoginActivity
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registrasi gagal: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method untuk menyimpan data pengguna ke Firebase Realtime Database
    private void saveUserDataToDatabase(FirebaseUser firebaseUser, String username) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Buat Map untuk menyimpan data dengan urutan yang benar
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", username);  // Username pertama
        userData.put("email", firebaseUser.getEmail());  // Email kedua
        userData.put("level", "newbie");  // Level ketiga

        // Menyimpan data dengan UID pengguna sebagai kunci utama
        databaseReference.child(firebaseUser.getUid()).updateChildren(userData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Data pengguna berhasil disimpan", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Gagal menyimpan data pengguna", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
