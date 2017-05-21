package com.example.justinkwik.hipcar;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Justin Kwik on 19/05/2017.
 */
public class HipCarApplication extends Application {

    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        }

    }

    public static SharedPreferences getSharedPreferences() {

        return sharedPreferences;

    }

}
