package com.example.justinkwik.hipcar.Welcome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.justinkwik.hipcar.R;
import com.example.justinkwik.hipcar.Splash.SplashActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private SharedPreferences sharedPreferences;
    private TextView skipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        skipButton = (TextView) findViewById(R.id.skipButton);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        //TODO: uncomment this
//        if(sharedPreferences.contains("firsttime")) {
//
//            Intent switchToSplashActivity = new Intent(WelcomeActivity.this, SplashActivity.class);
//            startActivity(switchToSplashActivity);
//
//        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {

                    case 0:
                        radioGroup.check(R.id.radioButton1);
                        break;
                    case 1:
                        radioGroup.check(R.id.radioButton2);
                        break;
                    case 2:
                        radioGroup.check(R.id.radioButton3);
                        break;
                    case 3:
                        radioGroup.check(R.id.radioButton4);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!sharedPreferences.contains("firsttime")) {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("firsttime", true);
                    editor.commit();

                    Intent switchToSplashActivity = new Intent(WelcomeActivity.this, SplashActivity.class);
                    startActivity(switchToSplashActivity);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();

                }

            }
        });

        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));

    }

    public class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {super(fm);}

        @Override
        public Fragment getItem(int position) {

            switch(position) {

                //TODO: Make the different fragments for the different pages.
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;

            }

        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
