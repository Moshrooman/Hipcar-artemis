package com.example.justinkwik.hipcar.Login;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.justinkwik.hipcar.ConnectionManager;
import com.example.justinkwik.hipcar.R;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperToast;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private final String loginUrl = "https://artemis-api.hipcar.com/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (EditText) findViewById(R.id.userNameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        loginButton = (Button) findViewById(R.id.loginButton);

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

                        Log.e("Response: ", response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
