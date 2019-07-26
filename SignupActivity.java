package com.example.ioanoanea15.creative.login;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText name;
    private EditText last_name;
    private EditText username;
    private EditText email;
    private EditText password1;
    private EditText password2;
    private Button signup;
    private TextView error;
    public Context context;
    SessionManager sessionManager;


    private static String URL_REGISTER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        URL_REGISTER = getString(R.string.server) + "/signup.php";

        setViews();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               final String _name = name.getText().toString();
               final String _last_name = last_name.getText().toString();
               final String user = username.getText().toString();
               final String _email = email.getText().toString();
               final String pass1 = password1.getText().toString();
               final String pass2 = password2.getText().toString();

                int check;
                check = Validate(_name,_last_name,user,_email,pass1,pass2);
                if(check == 0)
                    displayError("Complete all boxes");
                else if(check == -1)
                    displayError("To short password");
                else if(check == -2)
                    displayError("Passwords don't match");
                else {
                    final String password = pass1;
                    Signup(_name,_last_name,user, _email, password);
                    //goToRegistered();
                }

            }
        });
    }


    //atribuiri
    private void setViews(){
        name = (EditText) findViewById(R.id.name);
        last_name = (EditText) findViewById(R.id.last_name);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password1 = (EditText) findViewById(R.id.password1);
        password2 = (EditText) findViewById(R.id.password2);
        signup = (Button) findViewById(R.id.btn_signup);
        error = findViewById(R.id.error);
    }


    //validare date
    private int Validate(String name, String last_name, String user, String email, String pass1, String pass2){
        if(name.isEmpty() || last_name.isEmpty() || user.isEmpty() || email.isEmpty() || pass1.isEmpty() || pass2.isEmpty())
            return 0;
        if(pass1.length() < 8)
            return -1;
        if(!pass1.equals(pass2))
            return -2;
        return 1;
    }


    //inserare date in baza de date
    private void  Signup(final  String name, final String last_name, final String username, final String email, final String password){

      StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
              new Response.Listener<String>() {
                  @Override
                  public void onResponse(String response) {
                    try{
                       JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");

                        if(success.equals("1")){
                            //goToRegistered();
                            //((SignupActivity) context).finish();
                            displayError("Done");
                            error.setBackgroundColor(getResources().getColor(R.color.err_color3));
                        }
                            //Toast.makeText(SignupActivity.this, "Signup Succes!", Toast.LENGTH_SHORT).show();
                        else if(success.equals("2"))
                            displayError("Username or email already exists");
                    }catch (JSONException e){
                        e.printStackTrace();
                        displayError("Something went wrong");
                    }
                  }
              },
              new Response.ErrorListener() {
                  @Override
                  public void onErrorResponse(VolleyError error) {
                      displayError("Something went wrong");

                  }
              }){
          @Override
          protected Map<String, String> getParams() throws AuthFailureError {
              Map<String, String> params = new HashMap<>();
              params.put("name", name);
              params.put("last_name", last_name);
              params.put("username", username);
              params.put("email", email);
              params.put("password", password);
              return params;
          }
      };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


     //arata eroare
    public void displayError(String err) {
        error.setVisibility(View.VISIBLE);
        error.setBackgroundColor(getResources().getColor(R.color.err_color));
        if(err.equals("INVISIBLE"))
            error.setVisibility(View.INVISIBLE);
        else error.setText(err);

        error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error.setVisibility(View.INVISIBLE);
            }
        });
    }

}



