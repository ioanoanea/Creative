package com.example.ioanoanea15.creative.home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class notificationsFragment extends Fragment {

    private ListView listView;
    private TextView txt;
    private ArrayList<String> Id = new ArrayList<>();
    private ArrayList<String> Type = new ArrayList<>();
    private ArrayList<String> Text = new ArrayList<>();
    private CustomAdapter customAdapter = new CustomAdapter();
    private SessionManager sessionManager;
    private String URL_NOTIFICATIONS,URL_DELETE;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.list_notifications);
        txt = view.findViewById(R.id.text);

        URL_NOTIFICATIONS = getResources().getString(R.string.server)+"/display_notifications.php";
        URL_DELETE = getResources().getString(R.string.server) + "/delete_notification.php";

        sessionManager = new SessionManager(getContext());
        HashMap<String, String> user = sessionManager.getUserDetail();
        final String mId = user.get(sessionManager.ID);

        getNotifications(mId,URL_NOTIFICATIONS);

    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Id.size();
        }

        @Override
        public Object getItem(int position) {
            return Id.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.card_notifications,null);

            ImageView img;
            TextView text;

            sessionManager = new SessionManager(getContext());
            final HashMap<String, String> user = sessionManager.getUserDetail();
            final String mName = user.get(sessionManager.NAME);
            final String mUsername = user.get(sessionManager.USERNAME);
            final String mPhoto = user.get(sessionManager.PHOTO);

            img = convertView.findViewById(R.id.img);
            text = convertView.findViewById(R.id.text);


            if(Type.get(position).equals("1"))
                img.setImageResource(R.drawable.gold_cup);
            else if(Type.get(position).equals("2"))
                img.setImageResource(R.drawable.silver_cup);
            else if(Type.get(position).equals("3"))
                img.setImageResource(R.drawable.bronze_cup);
            else if(Type.get(position).equals("theme"))
                img.setImageResource(R.drawable.pictures);
            else img.setImageResource(R.drawable.insigne);

            text.setText(Text.get(position));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!Type.get(position).equals("theme"))
                        ((HomeActivity)getActivity()).goToProfil(mUsername);
                    else getFragmentManager().beginTransaction().replace(R.id.home_container, new PublicFragment()).addToBackStack(null).commit();

                    deleteNotification(Id.get(position),URL_DELETE);
                }
            });

            return convertView;
        }
    }

    private void goToProfil(String name, String username, String photo) {

        ProfilFragment profilFragment = new ProfilFragment();

        Bundle bundle = new Bundle();

        bundle.putString("name", name);
        bundle.putString("username", username);
        bundle.putString("photo", photo);

        profilFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.home_container, profilFragment).addToBackStack(null).commit();

    }


    private void getNotifications(final String id, final String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("display");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id");
                                    String text = object.getString("text");
                                    String type = object.getString("type");

                                    Id.add(id);
                                    Text.add(text);
                                    Type.add(type);

                                    //Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
                                }
                                if(Id.size() == 0)
                                    txt.setVisibility(View.VISIBLE);
                                else listView.setAdapter(customAdapter);
                            } else
                                ((HomeActivity)getActivity()).displayError("Something went wrong");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            ((HomeActivity)getActivity()).displayError("Something went wrong");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        ((HomeActivity)getActivity()).displayError("Something went wrong");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void deleteNotification(final String id, final String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            ((HomeActivity)getActivity()).displayError("Something went wrong");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        ((HomeActivity)getActivity()).displayError(err.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}
