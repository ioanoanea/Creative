package com.example.ioanoanea15.creative.home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ioanoanea15.creative.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserPictures extends Fragment {

    private GridView gridView;
    private String URL_GET_PICTURES;

    GridAdapter gridAdapter = new GridAdapter();

    ArrayList<String> Images = new ArrayList<>();
    ArrayList<String> Likes = new ArrayList<>();
    ArrayList<String> Gifts = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_pictures,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        URL_GET_PICTURES = getResources().getString(R.string.server)+"/display_public_pictures.php";
        gridView = view.findViewById(R.id.pictures);

       /* String _username;
        Bundle bundle = getArguments();



        _username = bundle.getString("username");*/

        getPictures("ioanoanea",URL_GET_PICTURES);
    }


    private class GridAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return Images.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.card_pic,null);

            ImageView img;

            img = convertView.findViewById(R.id.image);

            Picasso.get().load(Images.get(position)).into(img);

            return convertView;
        }
    }


    private void getPictures(final String username, final String URL) {
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
                                    String id = object.getString("id").trim();
                                    String Image = object.getString("pic_name").trim();
                                    String likes = object.getString("pic_likes").trim();
                                    String gft = object.getString("gift").trim();


                                    Images.add(Image);
                                    Gifts.add(gft);
                                    Likes.add(likes);

                                   // Toast.makeText(getContext(), Images.get(i), Toast.LENGTH_SHORT).show();
                                }
                            gridView.setAdapter(gridAdapter);
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
                params.put("username", username);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}
