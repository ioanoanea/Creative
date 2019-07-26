package com.example.ioanoanea15.creative.login;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ioanoanea15.creative.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordFragment extends Fragment {

    private EditText password1, password2;
    private Button change;
    private static String URL_CHANGE_PASSWORD;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        password1 = view.findViewById(R.id.password1);
        password2 = view.findViewById(R.id.password2);
        change = view.findViewById(R.id.change);

        URL_CHANGE_PASSWORD = getResources().getString(R.string.server) + "/change_password.php";

        ((ResetActivity)getActivity()).displayError("INVISIBLE");

        final String email;
        Bundle bundle = getArguments();
        email = bundle.getString("email");

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password1.getText().toString().equals(password2.getText().toString())){
                    String password = password2.getText().toString();
                     if(password.length()>=8){
                         changePassword(email,password,URL_CHANGE_PASSWORD);
                     } else ((ResetActivity)getActivity()).displayError("To short password!");
                } else ((ResetActivity)getActivity()).displayError("Passwords don't match!");
            }
        });

    }

    private void changePassword(final String email, final String password, final String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1"))
                                ((ResetActivity)getActivity()).displayError("Done");
                        }catch (JSONException e){
                            e.printStackTrace();
                            ((ResetActivity)getActivity()).displayError("Something went wrong");
                            //Toast.makeText(getContext(), "Error #1 ! "+ e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((ResetActivity)getActivity()).displayError("Something went wrong");

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
