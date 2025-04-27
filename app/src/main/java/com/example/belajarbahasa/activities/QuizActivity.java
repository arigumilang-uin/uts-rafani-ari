package com.example.belajarbahasa.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.belajarbahasa.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class QuizActivity extends AppCompatActivity {

    private TextView questionTextView;
    private RadioGroup rgOptions;
    private Button submitButton;
    private ProgressBar progressBar;  // Deklarasi ProgressBar

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    private String levelToPlay;
    private String userLevel;

    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;

    private String[] questions;
    private String[][] options;
    private String[] correctAnswersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Inisialisasi Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();

        // Inisialisasi tampilan
        questionTextView = findViewById(R.id.tvQuestion);
        rgOptions = findViewById(R.id.rgOptions);
        submitButton = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);  // Inisialisasi ProgressBar

        // Ambil level dari intent (level kuis yang mau dikerjakan)
        levelToPlay = getIntent().getStringExtra("level");

        // Set listener tombol submit
        submitButton.setOnClickListener(view -> {
            int selectedId = rgOptions.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedButton = findViewById(selectedId);
                checkAnswer(selectedButton.getText().toString());
            } else {
                Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
            }
        });

        // Ambil level user dari Firebase
        String userId = auth.getCurrentUser().getUid();
        databaseReference.child(userId).child("level").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userLevel = task.getResult().getValue(String.class);

                if (isLevelAccessible(userLevel, levelToPlay)) {
                    setupQuestionsByLevel(levelToPlay);
                    loadQuestion();  // Pastikan ini dipanggil setelah setup
                    progressBar.setVisibility(View.VISIBLE);  // Menampilkan progress bar
                    progressBar.setMax(questions.length);  // Set max progress sesuai jumlah soal
                } else {
                    Toast.makeText(this, "Selesaikan pelajaran sebelumnya dulu!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(this, "Failed to retrieve user level", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isLevelAccessible(String userLevel, String levelToPlay) {
        if (levelToPlay.equals("beginner")) {
            return userLevel.equals("newbie") || userLevel.equals("beginner") || userLevel.equals("intermediate") || userLevel.equals("advanced");
        } else if (levelToPlay.equals("intermediate")) {
            return userLevel.equals("beginner") || userLevel.equals("intermediate") || userLevel.equals("advanced");
        } else if (levelToPlay.equals("advanced")) {
            return userLevel.equals("intermediate") || userLevel.equals("advanced");
        }
        return false;
    }

    private void setupQuestionsByLevel(String level) {
        if (level.equals("beginner")) {
            questions = new String[]{
                    "What is the English translation of 'selamat pagi'?",
                    "Which word is the opposite of 'happy'?"
            };
            options = new String[][]{
                    {"Good evening", "Good morning", "Good night", "Hello"},
                    {"Sad", "Excited", "Angry", "Tired"}
            };
            correctAnswersList = new String[]{
                    "Good morning",
                    "Sad"
            };
        } else if (level.equals("intermediate")) {
            questions = new String[]{
                    "Which of the following is a synonym of 'fast'?",
                    "Which sentence is correct?"
            };
            options = new String[][]{
                    {"Slow", "Quick", "Lazy", "Heavy"},
                    {"I has a car", "I have a car", "I car have", "I haves a car"}
            };
            correctAnswersList = new String[]{
                    "Quick",
                    "I have a car"
            };
        } else if (level.equals("advanced")) {
            questions = new String[]{
                    "Which word means 'extremely sad'?",
                    "What is the past tense of 'go'?"
            };
            options = new String[][]{
                    {"Joyful", "Ecstatic", "Depressed", "Content"},
                    {"Gone", "Went", "Goes", "Going"}
            };
            correctAnswersList = new String[]{
                    "Depressed",
                    "Went"
            };
        }
    }

    private void loadQuestion() {
        if (currentQuestionIndex < questions.length) {
            questionTextView.setText(questions[currentQuestionIndex]);
            rgOptions.removeAllViews();

            for (String option : options[currentQuestionIndex]) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(option);
                rgOptions.addView(radioButton);
            }
        } else {
            finishQuiz();
        }
    }

    private void checkAnswer(String selectedAnswer) {
        if (selectedAnswer.equals(correctAnswersList[currentQuestionIndex])) {
            correctAnswers++;
        }

        currentQuestionIndex++;
        progressBar.setProgress(currentQuestionIndex); // Update progress bar berdasarkan soal yang sudah dijawab
        loadQuestion();
    }

    private void finishQuiz() {
        Toast.makeText(this, "Quiz selesai! Benar: " + correctAnswers + " dari " + questions.length, Toast.LENGTH_LONG).show();

        // Minimal harus semua benar untuk naik level (boleh kamu ubah kriteria)
        if (correctAnswers == questions.length) {
            upgradeLevelIfNeeded(levelToPlay);
        }

        progressBar.setVisibility(View.GONE); // Menyembunyikan progress bar setelah selesai
        finish(); // Balik setelah selesai
    }

    private void upgradeLevelIfNeeded(String currentQuizLevel) {
        String userId = auth.getCurrentUser().getUid();

        final String newLevel;
        if (currentQuizLevel.equals("beginner")) {
            newLevel = "beginner";
        } else if (currentQuizLevel.equals("intermediate")) {
            newLevel = "intermediate";
        } else if (currentQuizLevel.equals("advanced")) {
            newLevel = "advanced";
        } else {
            newLevel = null;
        }

        if (newLevel != null) {
            databaseReference.child(userId).child("level").setValue(newLevel)
                    .addOnSuccessListener(unused -> Toast.makeText(this, "Selamat! Level naik ke " + newLevel, Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Gagal update level", Toast.LENGTH_SHORT).show());
        }
    }
}
