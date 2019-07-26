package com.example.ioanoanea15.creative.home;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilFragment extends Fragment {

    private static String URL_PROFIL,URL_DISPLAY, URL_FOLLOW;
    private String Id, Name, Last_name, Username, Email, Photo, Gold, Silver, Bronze, Medal, posts, likes, followers, is_following;
    private RecyclerView listView;
    private ImageView img_display, black;
    private Button like, close;



   // ProfileManager profileManager = new ProfileManager(getContext());

    ArrayList<String> Images = new ArrayList<>();
    ArrayList<String> Likes = new ArrayList<>();
    ArrayList<String> Gifts = new ArrayList<>();


    CustomAdapter customAdapter = new CustomAdapter();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profil,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        URL_PROFIL = getResources().getString(R.string.server)+"/profil.php";
        URL_DISPLAY = getResources().getString(R.string.server)+"/display_user_pictures.php";
        URL_FOLLOW = getResources().getString(R.string.server)+"/follow.php";


        listView = view.findViewById(R.id.list_profil);
        img_display = view.findViewById(R.id.pic);
       // close = view.findViewById(R.id.close);
       // black = view.findViewById(R.id.black);


        final String _username;
        Bundle bundle = getArguments();



        _username = bundle.getString("username");
        getProfilData(_username,((HomeActivity)getActivity()).getUserData("id"),URL_PROFIL);
       /// getPictures(_username,URL_DISPLAY);


        img_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_display.setVisibility(View.INVISIBLE);
            }
        });

    }



    //ViewHolders
     public class ListViewHolder1 extends RecyclerView.ViewHolder {
        CircleImageView profil_img;
        Button follow,unfollow,edit;
        TextView name, username;
        TextView gold, silver, bronze, medal;
        TextView text1, text2, text3;

        public ListViewHolder1(@NonNull View itemView) {
            super(itemView);

            profil_img = itemView.findViewById(R.id.prof_img);
            follow = itemView.findViewById(R.id.follow);
            unfollow = itemView.findViewById(R.id.unfollow);
            edit = itemView.findViewById(R.id.edit);
            name = itemView.findViewById(R.id.prof_name);
            username = itemView.findViewById(R.id.prof_username);
            gold = itemView.findViewById(R.id.text_gold);
            silver = itemView.findViewById(R.id.text_silver);
            bronze = itemView.findViewById(R.id.text_bronze);
            medal = itemView.findViewById(R.id.text_medal);
            text1 = itemView.findViewById(R.id.text1);
            text2 = itemView.findViewById(R.id.text2);
            text3 = itemView.findViewById(R.id.text3);
        }
    }

    public class ListViewHolder2 extends RecyclerView.ViewHolder {

        ImageView img1, img2, img3;

        public ListViewHolder2(@NonNull View itemView) {
            super(itemView);

            img1 = itemView.findViewById(R.id.image1);
            img2 = itemView.findViewById(R.id.image2);
            img3 = itemView.findViewById(R.id.image3);
        }
    }


    //Adapter pentru lista
    class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context context;
        int x = 0;
        @Override
        public int getItemViewType(int position) {
            if(position == 0)
                return 0;
            else return 1;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

            if(viewType == 0) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profil_header, viewGroup, false);
                return new ListViewHolder1(view);
            } else {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_pic, viewGroup, false);
                return new ListViewHolder2(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            if(viewHolder.getItemViewType() == 0){
                final ListViewHolder1 listViewHolder1 = (ListViewHolder1)viewHolder;

                Picasso.get().load(Photo).into(listViewHolder1.profil_img);
                listViewHolder1.name.setText(Name+" "+Last_name);
                listViewHolder1.username.setText(Username);

                listViewHolder1.gold.setText(Gold);
                listViewHolder1.silver.setText(Silver);
                listViewHolder1.bronze.setText(Bronze);
                listViewHolder1.medal.setText(Medal);

                if(is_following.equals("1")){
                    listViewHolder1.follow.setVisibility(View.INVISIBLE);
                    listViewHolder1.unfollow.setVisibility(View.VISIBLE);
                }

                if(Id.equals(((HomeActivity)getActivity()).getUserData("id"))){
                    listViewHolder1.follow.setVisibility(View.INVISIBLE);
                    listViewHolder1.unfollow.setVisibility(View.INVISIBLE);
                    listViewHolder1.edit.setVisibility(View.VISIBLE);
                }

                listViewHolder1.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFragmentManager().beginTransaction().replace(R.id.home_container, new SettingsFragment()).addToBackStack(null).commit();
                    }
                });

                listViewHolder1.follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((HomeActivity)getActivity()).changeVisibility(listViewHolder1.follow,View.INVISIBLE);
                        ((HomeActivity)getActivity()).changeVisibility(listViewHolder1.unfollow,View.VISIBLE);
                        is_following = "0";
                        Follow(((HomeActivity)getActivity()).getUserData("id"),Id,"follow",URL_FOLLOW);
                    }
                });

                listViewHolder1.unfollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((HomeActivity)getActivity()).changeVisibility(listViewHolder1.unfollow,View.INVISIBLE);
                        ((HomeActivity)getActivity()).changeVisibility(listViewHolder1.follow,View.VISIBLE);
                        Follow(((HomeActivity)getActivity()).getUserData("id"),Id,"unfollow",URL_FOLLOW);
                    }
                });

                listViewHolder1.text1.setText(posts + " posts");
                listViewHolder1.text2.setText(likes + " likes");
                listViewHolder1.text3.setText(followers + " followers");
            } else {
                final ListViewHolder2 listViewHolder2 = (ListViewHolder2)viewHolder;


                final int k1 = x;
                Picasso.get().load(Images.get(x)).into(listViewHolder2.img1);
                x++; final int k2 = x;
                Picasso.get().load(Images.get(x)).into(listViewHolder2.img2);
                x++; final int k3 = x;
                Picasso.get().load(Images.get(x)).into(listViewHolder2.img3);
                x++;

                listViewHolder2.img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        img_display.setVisibility(View.VISIBLE);
                        Picasso.get().load(Images.get(k1)).into(img_display);
                    }
                });

                listViewHolder2.img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        img_display.setVisibility(View.VISIBLE);
                        Picasso.get().load(Images.get(k2)).into(img_display);
                    }
                });

                listViewHolder2.img3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        img_display.setVisibility(View.VISIBLE);
                        Picasso.get().load(Images.get(k3)).into(img_display);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {

            int sz;
            sz = Images.size() - 3;
            if(sz%3!=0) sz = sz/3+1;
            else sz = sz/3;
            return sz+1;
        }
    }

    private void getProfilData (final String username, final String user, final String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("profil");

                            if(success.equals("1")){
                                for(int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    Id = object.getString("id").trim();
                                    Name = object.getString("name").trim();
                                    Last_name = object.getString("last_name").trim();
                                    Email = object.getString("email").trim();
                                    Username = object.getString("username").trim();
                                    Photo = object.getString("photo").trim();
                                    Gold = object.getString("gold").trim();
                                    Silver = object.getString("silver").trim();
                                    Bronze = object.getString("bronze").trim();
                                    Medal = object.getString("medal").trim();
                                    posts = object.getString("posts");
                                    likes =  object.getString("likes");
                                    followers = object.getString("followers");
                                    is_following = object.getString("is_following").trim();

                                    // Toast.makeText(getContext(),Name+" "+Last_name+" "+Username+" "+Photo,Toast.LENGTH_SHORT).show();
                                }
                                getPictures(Id,URL_DISPLAY);
                            }

                            else
                                ((HomeActivity)getActivity()).displayError("Something went wrong");

                        } catch (JSONException e) {
                            e.printStackTrace();
                           //Toast.makeText(getContext(),"error: " + e.toString(),Toast.LENGTH_SHORT).show();
                            ((HomeActivity)getActivity()).displayError("Something went wrong");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        //Toast.makeText(getContext(),"Error: " + err.toString(),Toast.LENGTH_SHORT).show();
                        ((HomeActivity)getActivity()).displayError("Something went wrong");
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("profil", username);
                params.put("user",user);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
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

                                    //Toast.makeText(getContext(), user_like.get(i), Toast.LENGTH_SHORT).show();
                                }
                                Images.add("0");
                                Images.add("0");
                                Images.add("0");
                                listView.setLayoutManager(new LinearLayoutManager(getContext()));
                             listView.setAdapter(customAdapter);
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
                params.put("id", username);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    private void Follow(final String user1, final String user2, final String action, final String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            //JSONArray jsonArray = jsonObject.getJSONArray("display");

                            if (success.equals("1")) {
                                is_following = "1";
                            }  else ((HomeActivity)getActivity()).displayError("Something went wrong");

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
                params.put("user1", user1);
                params.put("user2", user2);
                params.put("action", action);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }



    private void goToPictures(String username) {

        UserPictures userPictures = new UserPictures();

        Bundle bundle = new Bundle();


        bundle.putString("username", username);

        userPictures.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.home_container, userPictures).addToBackStack(null).commit();

    }

}
