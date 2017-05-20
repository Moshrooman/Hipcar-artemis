package com.example.justinkwik.hipcar.Splash;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.justinkwik.hipcar.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView hipCarLogoImageView;
    private LottieAnimationView testLottie; //TODO: Delete and make a hipcar animation, one going off the other, make color of letter
    //TODO: RED
    private AlphaAnimation fadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        hipCarLogoImageView = (ImageView) findViewById(R.id.hipCarLogoImageView); //TODO: delete
        testLottie = (LottieAnimationView) findViewById(R.id.testLottie);
        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(2000);

        hipCarLogoImageView.startAnimation(fadeIn); //TODO: Delete
        testLottie.playAnimation();


    }

}
