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

public class CodeVerificationFragment extends Fragment {
    private EditText code;
    private Button btn_next;
    private static String URL_VERIFY;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_code_verification,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        code = view.findViewById(R.id.code);
        btn_next = view.findViewById(R.id.next);

        URL_VERIFY = getResources().getString(R.string.server) + "/code_verify.php";

        final String email;
        Bundle bundle = getArguments();
        email = bundle.getString("email");

        ((ResetActivity)getActivity()).displayError("INVISIBLE");

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Verify(email,code.getText().toString(),URL_VERIFY);
            }
        });
    }

    private void Verify(final String email, final String code, final String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1"))
                                //((ResetActivity)getActivity()).displayError("success");
                                Next(email);
                            else ((ResetActivity)getActivity()).displayError("invalid code");
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
                params.put("email",email);
                params.put("code",code);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void Next(String email){
        ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
        Bundle bundle = new Bundle();
        bundle.putString("email", email);

        changePasswordFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.reset, changePasswordFragment).addToBackStack(null).commit();
    }

}
