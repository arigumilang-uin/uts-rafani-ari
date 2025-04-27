package com.example.belajarbahasa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.belajarbahasa.MainActivity;
import com.example.belajarbahasa.R;

public class SplashActivity extends AppCompatActivity {

    ImageView logoImageView;
    TextView appNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Mendapatkan referensi ke elemen UI
        logoImageView = findViewById(R.id.logoImageView);
        appNameTextView = findViewById(R.id.appNameTextView);

        // Animasi logo (tampil)
        Animation logoAnimation = new AlphaAnimation(0f, 1f);
        logoAnimation.setDuration(2000); // Durasi 2 detik
        logoImageView.startAnimation(logoAnimation);

        // Animasi teks nama aplikasi (muncul setelah logo)
        logoAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Setelah logo selesai, munculkan nama aplikasi
                appNameTextView.setVisibility(TextView.VISIBLE);
                Animation textAnimation = new AlphaAnimation(0f, 1f);
                textAnimation.setDuration(1000); // Durasi 1 detik
                appNameTextView.startAnimation(textAnimation);

                // Setelah animasi selesai, pindah ke Activity berikutnya
                textAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // Pindah ke MainActivity setelah Splash selesai
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish(); // Menutup SplashActivity agar tidak kembali
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }
}
