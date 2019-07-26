package com.example.ioanoanea15.creative.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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
import com.example.ioanoanea15.creative.pakages.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends Fragment {
    private CircleImageView profil_img;
    private EditText name, last_name, email;
    private Button save;
    private Bitmap bitmap;
    private static String URL_UPDATE;
    SessionManager sessionManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.name);
        last_name = view.findViewById(R.id.last_name);
        email = view.findViewById(R.id.email);
        profil_img = view.findViewById(R.id.profil_img);
        save = view.findViewById(R.id.save);

        URL_UPDATE = getString(R.string.server) + "/update_profil.php";

         sessionManager = new SessionManager(getContext());
        HashMap<String, String> user = sessionManager.getUserDetail();

        name.setText(user.get(sessionManager.NAME));
        last_name.setText(user.get(sessionManager.LAST_NAME));
        email.setText(user.get(sessionManager.EMAIL));
        Picasso.get().load(user.get(sessionManager.PHOTO)).into(profil_img);

        profil_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
                else ((HomeActivity)getActivity()).chooseFile(1);
            }
        });



        final String _id = user.get(sessionManager.ID);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _name = name.getText().toString();
                String l_name = last_name.getText().toString();
                String em = email.getText().toString();

                updateProfil(_name,l_name,_id,em);


            }
        });

    }

    private void  updateProfil(final  String name, final String last_name, final String id, final String email){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("1")){
                                HashMap<String, String> user = sessionManager.getUserDetail();
                                String username = user.get(sessionManager.USERNAME);
                                String photo = user.get(sessionManager.PHOTO);
                                sessionManager.createSession(id,name,last_name,username,email,photo);

                                ((HomeActivity)getActivity()).displayError("Done");
                            }
                            else if(success.equals("2"))
                                ((HomeActivity)getActivity()).displayError("Email already exists");

                        }catch (JSONException e){
                            e.printStackTrace();
                            ((HomeActivity)getActivity()).displayError("Something went wrong");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((HomeActivity)getActivity()).displayError("Something went wrong");

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("last_name", last_name);
                params.put("id",id);
                params.put("email", email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    public void setProfilImg(String img){
        Picasso.get().load(img).into(profil_img);
    }

}
