package com.example.ioanoanea15.creative.login;

import android.app.ProgressDialog;
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

public class sendFragment extends Fragment {

    private EditText username;
    private Button next;
    private static String URL_RESET;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_send,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username = view.findViewById(R.id.username);
        next = view.findViewById(R.id.btn_next);

        URL_RESET = getString(R.string.server) + "/reset.php";

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user = username.getText().toString();
                Send(user);
            }
        });
    }

    private void Send(final String username){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Check email...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RESET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("1")){
                                Next();
                            }
                            else if(success.equals("-1"))
                                //Toast.makeText(getContext(), "success ", Toast.LENGTH_SHORT).show();
                            ((ResetActivity)getActivity()).displayError("Invalid email");
                        }catch (JSONException e){
                            progressDialog.dismiss();
                            e.printStackTrace();
                            ((ResetActivity)getActivity()).displayError("Something went wrong");
                            //Toast.makeText(getContext(), "Error #1 ! " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        ((ResetActivity)getActivity()).displayError("Something went wrong");

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void Next(){
        CodeVerificationFragment codeVerificationFragment = new CodeVerificationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("email", username.getText().toString());

        codeVerificationFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.reset, codeVerificationFragment).addToBackStack(null).commit();
    }



}
