package com.example.ioanoanea15.creative.login;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ioanoanea15.creative.R;
import com.example.ioanoanea15.creative.pakages.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login;
    private TextView reset;
    private TextView signup;
    private TextView error;
    private ImageView passView;
    private static  String URL_LOGIN;
    SessionManager sessionManager;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        final  int screenWidth = displayMetrics.widthPixels;


        username = (EditText) findViewById(R.id.textUsername);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.btn_login);
        reset = (TextView) findViewById(R.id.btn_gotoreset);
        signup = (TextView) findViewById(R.id.btn_gotosignup);
        passView = (ImageView) findViewById(R.id.view);
        error = (TextView) findViewById(R.id.error);

        URL_LOGIN = getString(R.string.server) + "/login.php";

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               final String user = username.getText().toString().trim();
               final String pass = password.getText().toString().trim();
               error.setVisibility(View.INVISIBLE);
                if(!user.isEmpty() && !pass.isEmpty())
                    Login(user,pass);
                else
                    displayError("Invalid data entered");
            }
        });
        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                goTosignup();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToreset();
            }
        });

       final int type = password.getInputType();

        passView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getInputType()!=1) {
                    password.setInputType(1);
                    passView.setImageResource(R.drawable.ic_lock_open);
                } else {
                    password.setInputType(type);
                    passView.setImageResource(R.drawable.ic_lock);
                }
            }
        });

    }


    //login
    private void Login (final String username, final String password){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if(success.equals("1")){
                                for(int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id").trim();
                                    String name = object.getString("name").trim();
                                    String last_name = object.getString("last_name").trim();
                                    String email = object.getString("email").trim();
                                    String Username = object.getString("username").trim();
                                    String photo = object.getString("photo").trim();
                                    sessionManager.createSession(id,name,last_name,Username,email,photo);
                                    sessionManager.checkLogin();

                                   // Intent intent = new Intent(MainActivity.this, HomeActivity.class);

                                    /*intent.putExtra("name",name);
                                    intent.putExtra("last_name",last_name);
                                    intent.putExtra("username",Username);
                                    intent.putExtra("email", email);
                                    Toast.makeText(MainActivity.this, "success login. \nuser: "+Username, Toast.LENGTH_SHORT).show();

                                    startActivity(intent);*/

                                }
                                //goTohome();
                            }

                            else
                               displayError("Invalid data entered");

                        } catch (JSONException e) {
                            e.printStackTrace();
                              displayError("Something went wrong");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        displayError("Error "+ err.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    //schimba activitatea cu activitatea de signup
    private void goTosignup(){
        Intent intent = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(intent);
    }


    //schimba activitatea cu activitatea cuactivitatea home
    private void goToreset (){
        Intent intent = new Intent(MainActivity.this, ResetActivity.class);
        startActivity(intent);
    }

    private void displayError(String err){
        error.setVisibility(View.VISIBLE);
        error.setText(err);

        //error.setVisibility(View.GONE);
    }
}
