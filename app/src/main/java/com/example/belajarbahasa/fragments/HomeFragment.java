package com.example.belajarbahasa.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.belajarbahasa.R;
import com.example.belajarbahasa.activities.QuizActivity;

public class HomeFragment extends Fragment {

    private Button btnBeginner, btnIntermediate, btnAdvanced;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btnBeginner = view.findViewById(R.id.btnBeginner);
        btnIntermediate = view.findViewById(R.id.btnIntermediate);
        btnAdvanced = view.findViewById(R.id.btnAdvanced);

        // Button untuk level Beginner
        btnBeginner.setOnClickListener(v -> startQuizActivity("beginner"));

        // Button untuk level Intermediate
        btnIntermediate.setOnClickListener(v -> startQuizActivity("intermediate"));

        // Button untuk level Advanced
        btnAdvanced.setOnClickListener(v -> startQuizActivity("advanced"));

        return view;
    }

    private void startQuizActivity(String level) {
        Intent intent = new Intent(getActivity(), QuizActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
    }
}
