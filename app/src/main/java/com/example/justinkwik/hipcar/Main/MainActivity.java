package com.example.justinkwik.hipcar.Main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.justinkwik.hipcar.HipCarApplication;
import com.example.justinkwik.hipcar.Login.LoginActivity;
import com.example.justinkwik.hipcar.Login.UserCredentials;
import com.example.justinkwik.hipcar.R;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private UserCredentials userCredentials;
    private final SharedPreferences sharedPreferences = HipCarApplication.getSharedPreferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userCredentials = new Gson().fromJson(sharedPreferences.getString("credentials", ""), UserCredentials.class);
        Log.e("Name: ", userCredentials.getName());



        //TODO: for logging out delete the credentials and set loggedin boolean to false (both in sharedPreference)
        //TODO: give an option to skip splash screen in beginning
        //TODO: work on reservation.

    }

}
