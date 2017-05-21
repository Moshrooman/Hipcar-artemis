package com.example.justinkwik.hipcar.Splash;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.example.justinkwik.hipcar.Login.LoginActivity;
import com.example.justinkwik.hipcar.R;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private ImageView hipCarLogoImageView;
    private AlphaAnimation fadeIn;
    private LinearLayout lettersLinearLayout;
    private String[] letterArray;
    private List<LottieAnimationView> viewList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        hipCarLogoImageView = (ImageView) findViewById(R.id.hipCarLogoImageView);
        lettersLinearLayout = (LinearLayout) findViewById(R.id.lettersLinearLayout);
        letterArray = new String[]{"A", "R", "T", "E", "M", "I", "S"};

        for (int i = 0; i < letterArray.length; i++) {

            final int finalI = i;

            LottieComposition.Factory.fromAssetFileName(getApplicationContext(), "Mobilo/" + letterArray[i] + ".json",
                    new OnCompositionLoadedListener() {
                        @Override
                        public void onCompositionLoaded(LottieComposition composition) {

                            LottieAnimationView lottieAnimationView = new LottieAnimationView(getApplicationContext());
                            lottieAnimationView.setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));

                            lottieAnimationView.setComposition(composition);
                            lottieAnimationView.setColorFilter(getResources().getColor(R.color.red)); //TODO need to set color

                            if(finalI == 6) {

                                viewList.add(lottieAnimationView);
                                viewList.get(viewList.size() - 1).addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {

                                        if(animation.getCurrentPlayTime() >= animation.getDuration()) {

                                            Runnable moveToLoginScreen = new Runnable() {
                                                @Override
                                                public void run() {

                                                    Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                                                    startActivity(loginIntent);
                                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                    finish();

                                                }
                                            };

                                            Handler loginScreenHandler = new Handler();
                                            loginScreenHandler.postDelayed(moveToLoginScreen, 1500);

                                        }

                                    }
                                });

                            } else {

                                viewList.add(lottieAnimationView);

                            }

                            if (viewList.size() == 7) {

                                for (int i = 0; i < viewList.size(); i++) {

                                    lettersLinearLayout.addView(viewList.get(i));

                                    viewList.get(i).playAnimation();

                                }

                            }

                        }
                    });

        }

        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(3000);

        hipCarLogoImageView.startAnimation(fadeIn);

    }

    //TODO: Make sure to recycle all views
    //TODO: finish if clicking back
    //TODO: fix the padding for each letter (make closer together using after effects)

}
