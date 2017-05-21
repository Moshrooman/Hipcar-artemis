package com.example.justinkwik.hipcar.Login;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.justinkwik.hipcar.ConnectionManager;
import com.example.justinkwik.hipcar.HipCarApplication;
import com.example.justinkwik.hipcar.Main.MainActivity;
import com.example.justinkwik.hipcar.R;
import com.example.justinkwik.hipcar.Splash.SplashActivity;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private final String loginUrl = "https://artemis-api-dev.hipcar.com/login";
    private static UserCredentials userCredentials;
    private LottieAnimationView loadingAnimationView;
    private LottieAnimationView checkMarkAnimationView;
    private final SharedPreferences sharedPreferences = HipCarApplication.getSharedPreferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (EditText) findViewById(R.id.userNameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        loginButton = (Button) findViewById(R.id.loginButton);
        loadingAnimationView = (LottieAnimationView) findViewById(R.id.loadingAnimationView);
        checkMarkAnimationView = (LottieAnimationView) findViewById(R.id.checkMarkAnimationView);

        checkMarkAnimationView.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                if(animation.getCurrentPlayTime() >= animation.getDuration()) {

                    Runnable mainActivityRunnable = new Runnable() {
                        @Override
                        public void run() {

                            Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainActivityIntent);
                            finish();

                        }
                    };

                    Handler mainActivityHandler = new Handler();
                    mainActivityHandler.postDelayed(mainActivityRunnable, 1000);

                }

            }
        });

        loginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN) {

                    loginButton.setBackgroundResource(R.drawable.loginbuttonpressed);

                } else {

                    loginButton.setBackgroundResource(R.drawable.loginbutton);

                }

                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingAnimationView.setVisibility(View.VISIBLE);
                loadingAnimationView.playAnimation();

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(username.equals("") || password.equals("")) {

                    SuperToast superToast = SuperToast.create(LoginActivity.this, "Please Fill In All Fields!", Style.DURATION_SHORT,
                            Style.red()).setAnimations(Style.ANIMATIONS_POP);
                    superToast.show();

                    return;

                }

                final JSONObject loginJSON = new JSONObject();

                try {

                    loginJSON.put("email", usernameEditText.getText().toString());
                    loginJSON.put("password", passwordEditText.getText().toString());

                } catch (JSONException e) {

                    e.printStackTrace();

                }

                StringRequest loginRequest = new StringRequest(Request.Method.POST, loginUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loadingAnimationView.pauseAnimation();
                        loadingAnimationView.setVisibility(View.INVISIBLE);

                        checkMarkAnimationView.setVisibility(View.VISIBLE);

                        checkMarkAnimationView.playAnimation();

                        userCredentials = new Gson().fromJson(response, UserCredentials.class);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("loggedin", true);
                        editor.putString("credentials", new Gson().toJson(userCredentials));
                        editor.apply();

                        //TODO: add a border around the login window

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        loadingAnimationView.pauseAnimation();
                        loadingAnimationView.setVisibility(View.INVISIBLE);

                        SuperToast superToast = SuperToast.create(LoginActivity.this, "Invalid Username/Password", Style.DURATION_SHORT,
                                Style.red()).setAnimations(Style.ANIMATIONS_POP);
                        superToast.show();

                    }
                }) {

                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return loginJSON.toString().getBytes();
                    }
                };

                ConnectionManager.getInstance(LoginActivity.this).add(loginRequest);

            }
        });

    }

    public static UserCredentials getUserCredentials() {

        return userCredentials;

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
