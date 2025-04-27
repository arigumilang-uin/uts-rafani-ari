package com.example.belajarbahasa.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.belajarbahasa.R;
import com.example.belajarbahasa.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private TextView tvUserName;
    private ImageView tvUserAvatar;
    private ImageButton btnChangeAvatar;
    private Button btnLogout;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inisialisasi komponen UI
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserAvatar = view.findViewById(R.id.tvUserAvatar);
        btnChangeAvatar = view.findViewById(R.id.btnChangeAvatar);
        btnLogout = view.findViewById(R.id.btnLogout); // Tombol logout

        // Inisialisasi Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Mendapatkan userId dari FirebaseAuth
        String userId = mAuth.getCurrentUser().getUid();

        // Ambil data pengguna dari Firebase Realtime Database
        mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Ambil data pengguna (username)
                String username = dataSnapshot.child("username").getValue(String.class);

                // Set data ke TextView
                tvUserName.setText(username);  // Menampilkan username
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Menangani jika ada error
                Toast.makeText(getActivity(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });

        // Tombol logout
        btnLogout.setOnClickListener(v -> {
            // Logout pengguna dari Firebase
            mAuth.signOut();
            // Arahkan kembali ke layar login
            Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
            // Bisa diarahkan ke halaman login menggunakan Intent
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();  // Menutup Activity Profile
        });

        return view;
    }
}
