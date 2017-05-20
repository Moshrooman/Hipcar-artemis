package com.example.justinkwik.hipcar.Splash;

import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.justinkwik.hipcar.R;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private ImageView hipCarLogoImageView;
    private LottieAnimationView aLotte;
    private LottieAnimationView rLotte;
    private LottieAnimationView tLotte;
    private LottieAnimationView eLotte;
    private LottieAnimationView mLotte;
    private LottieAnimationView iLotte;
    private LottieAnimationView sLotte;
    private AlphaAnimation fadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        hipCarLogoImageView = (ImageView) findViewById(R.id.hipCarLogoImageView);
        aLotte = (LottieAnimationView) findViewById(R.id.aLotte);
        rLotte = (LottieAnimationView) findViewById(R.id.rLotte);
        tLotte = (LottieAnimationView) findViewById(R.id.tLotte);
        eLotte = (LottieAnimationView) findViewById(R.id.eLotte);
        mLotte = (LottieAnimationView) findViewById(R.id.mLotte);
        iLotte = (LottieAnimationView) findViewById(R.id.iLotte);
        sLotte = (LottieAnimationView) findViewById(R.id.sLotte);

        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(2000);

        hipCarLogoImageView.startAnimation(fadeIn);

    }

}
