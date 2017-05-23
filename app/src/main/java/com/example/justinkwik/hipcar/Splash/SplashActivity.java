package com.example.justinkwik.hipcar.Splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.example.justinkwik.hipcar.HipCarApplication;
import com.example.justinkwik.hipcar.Login.LoginActivity;
import com.example.justinkwik.hipcar.Main.MainActivity;
import com.example.justinkwik.hipcar.R;
import com.google.gson.Gson;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity extends AppCompatActivity {

    private ImageView hipCarLogoImageView;
    private AlphaAnimation fadeInLogo;
    private final SharedPreferences sharedPreferences = HipCarApplication.getSharedPreferences();
    private TextView[] lettersTextViewArray;
    private LottieComposition hamburgerArrowComposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Pre-load the composition so no lag in setting the animation in Main Activity
        LottieComposition.Factory.fromAssetFileName(getApplicationContext(), "Lottie/hamburgerarrow.json",
                new OnCompositionLoadedListener() {
                    @Override
                    public void onCompositionLoaded(LottieComposition composition) {

                        hamburgerArrowComposition = composition;

                    }
                });

        hipCarLogoImageView = (ImageView) findViewById(R.id.hipCarLogoImageView);
        lettersTextViewArray = new TextView[]{(TextView) findViewById(R.id.A), (TextView) findViewById(R.id.R),
                (TextView) findViewById(R.id.T), (TextView) findViewById(R.id.E), (TextView) findViewById(R.id.M),
                (TextView) findViewById(R.id.I), (TextView) findViewById(R.id.S)};

        fadeInLogo = new AlphaAnimation(0, 1);
        fadeInLogo.setDuration(2000);

        fadeInLogo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                startLetterAnimations(lettersTextViewArray, 0);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        hipCarLogoImageView.startAnimation(fadeInLogo);

    }

    private void startLetterAnimations(final TextView[] textViewArray, final int position) {

        AlphaAnimation fadeInLetters = new AlphaAnimation(0, 1);
        fadeInLetters.setDuration(800);

        if(position != textViewArray.length) {

            textViewArray[position].setVisibility(View.VISIBLE);

            textViewArray[position].startAnimation(fadeInLetters);

            Runnable startLetterRunnable = new Runnable() {
                @Override
                public void run() {
                    startLetterAnimations(textViewArray, position + 1);
                }
            };

            Handler startLetterHandler = new Handler();
            startLetterHandler.postDelayed(startLetterRunnable, 90);

        } else {

            return;

        }

        if(position == textViewArray.length - 1) {

            fadeInLetters.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    Runnable mainActivityRunnable = new Runnable() {
                        @Override
                        public void run() {

                            if (sharedPreferences.getBoolean("loggedin", false)) {

                                Intent mainActivityIntent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(mainActivityIntent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();

                            } else {

                                Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(loginIntent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();

                            }

                        }
                    };

                    Handler mainActivityHandler = new Handler();
                    mainActivityHandler.postDelayed(mainActivityRunnable, 1000);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
