package com.example.belajarbahasa.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.belajarbahasa.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LevelFragment extends Fragment {

    private TextView tvLevel;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Mengambil layout fragment_level.xml
        View view = inflater.inflate(R.layout.fragment_level, container, false);

        // Inisialisasi UI
        tvLevel = view.findViewById(R.id.tvLevel);

        // Inisialisasi Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Mendapatkan userId dari FirebaseAuth
        String userId = mAuth.getCurrentUser().getUid();

        // Mengambil data level dan nama pengguna dari Firebase
        getUserData(userId);

        return view;
    }

    // Method untuk memperbarui level dan nama pada UI fragment
    public void updateLevelUI(String username, String newLevel) {
        if (tvLevel != null) {
            tvLevel.setText("Halo " + username + ", level kamu saat ini adalah " + newLevel); // Menampilkan level dan nama
        }
    }

    // Method untuk memuat level dan nama dari Firebase
    public void getUserData(String userId) {
        mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Ambil data level dan username dari snapshot
                String level = dataSnapshot.child("level").getValue(String.class);
                String username = dataSnapshot.child("username").getValue(String.class);

                // Pastikan level dan username tidak null
                if (level != null && !level.isEmpty() && username != null && !username.isEmpty()) {
                    updateLevelUI(username, level); // Memperbarui UI dengan nama dan level yang diambil dari Firebase
                } else {
                    Toast.makeText(getContext(), "Data level atau username tidak ditemukan!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Menangani error jika pengambilan data gagal
                Toast.makeText(getContext(), "Gagal mengambil data level atau username", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
