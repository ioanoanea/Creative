package com.example.ioanoanea15.creative.home;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
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
import com.example.ioanoanea15.creative.pakages.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopFragment extends Fragment {
    private RecyclerView listview;
    private TextView nothing;
    private String id, userId, profileName, profileLastName, profileUsername, profile_image, Image, userLike,gft;
    private int  Likes;
    private SwipeRefreshLayout refreshLayout;
    private static String URL_DISPLAY;
    private static String URL_LIKES;
    private static String URL_DELETE;
    Boolean isLike = false;


    ArrayList<String> ID = new ArrayList<>();
    ArrayList<String> Images = new ArrayList<>();
    ArrayList<String> UserId = new ArrayList<>();
    ArrayList<String> Names = new ArrayList<>();
    ArrayList<String> Usernames = new ArrayList<>();
    ArrayList<String> profile_images = new ArrayList<>();
    ArrayList<Integer> nr_likes = new ArrayList<>();
    ArrayList<String> user_like = new ArrayList<>();
    ArrayList<String> Gifts = new ArrayList<>();

    CustomAdapter customAdapter = new CustomAdapter();
    String check;

    // ProfileManager profileManager = new ProfileManager(getContext());


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listview = view.findViewById(R.id.nv_list);
        refreshLayout = view.findViewById(R.id.refresh);
        nothing = view.findViewById(R.id.nothing);

        URL_DISPLAY = getString(R.string.server) + "/display_top.php";
        URL_LIKES = getString(R.string.server) + "/like.php";
        URL_DELETE = getString(R.string.server) + "/delete_post.php";

        //Toast.makeText(getContext(),,Toast.LENGTH_SHORT).show();



        getPictures(getUserData("id"), URL_DISPLAY);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((HomeActivity)getActivity()).displayError("INVISIBLE");
                getFragmentManager().beginTransaction().replace(R.id.home_container, new TopFragment()).addToBackStack(null).commit();
                refreshLayout.setRefreshing(false);
            }
        });


    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        SessionManager sessionManager = new SessionManager(getContext());

        private ImageView image, gift;
        private CircleImageView profileImage;
        private TextView name, username, likes;
        private Button btn_like, btn_dislike, btn_delete, btn_send;
        private Bundle bundle = getArguments();
        private String Gift;

        public ListViewHolder(@NonNull View convertView) {
            super(convertView);

            image = convertView.findViewById(R.id.display_image);
            profileImage = convertView.findViewById(R.id.post_profile_image);
            name = convertView.findViewById(R.id.post_profile_name);
            // username = convertView.findViewById(R.id.post_profile_username);
            likes = convertView.findViewById(R.id.likes);
            btn_like = convertView.findViewById(R.id.btn_likes);
            btn_dislike = convertView.findViewById(R.id.btn_liked);
            btn_delete = convertView.findViewById(R.id.delete);
            btn_send = convertView.findViewById(R.id.send);
            gift = convertView.findViewById(R.id.gift);
        }
    }

    public class CustomAdapter extends RecyclerView.Adapter<ListViewHolder>{


        private Context context;

        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);

            return new ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ListViewHolder listViewHolder, final int position) {

            Picasso.get().load(Images.get(position)).resize(getWidth(), 0).into(listViewHolder.image);
            Picasso.get().load(profile_images.get(position)).into(listViewHolder.profileImage);
            listViewHolder.name.setText(Names.get(position));
            listViewHolder.likes.setText(nr_likes.get(position).toString());

            listViewHolder.btn_delete.setVisibility(View.INVISIBLE);
            listViewHolder.btn_dislike.setVisibility(View.INVISIBLE);
            listViewHolder.btn_like.setVisibility(View.VISIBLE);

            listViewHolder.btn_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity)getActivity()).goToMessenger(UserId.get(position),Names.get(position),profile_images.get(position));
                }
            });

            if(position == 0){
                listViewHolder.gift.setVisibility(View.VISIBLE);
                listViewHolder.gift.setImageResource(R.drawable.gold_cup);
            } else if(position == 1) {
                listViewHolder.gift.setVisibility(View.VISIBLE);
                listViewHolder.gift.setImageResource(R.drawable.silver_cup);
            } else if(position == 2) {
                listViewHolder.gift.setVisibility(View.VISIBLE);
                listViewHolder.gift.setImageResource(R.drawable.bronze_cup);
            } else if(position<50) {
                listViewHolder.gift.setVisibility(View.VISIBLE);
                listViewHolder.gift.setImageResource(R.drawable.insigne);
            }

            if (user_like.get(position).equals("1")) {
                listViewHolder.btn_dislike.setVisibility(View.VISIBLE);
                listViewHolder.btn_like.setVisibility(View.INVISIBLE);
            }

            listViewHolder.btn_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity)getActivity()).changeVisibility(listViewHolder.btn_like, View.INVISIBLE);
                    ((HomeActivity)getActivity()).changeVisibility(listViewHolder.btn_dislike, View.VISIBLE);
                    nr_likes.set(position,nr_likes.get(position)+1);
                    user_like.set(position,"1");
                    listViewHolder.likes.setText(nr_likes.get(position).toString());
                    newLike(ID.get(position), position, getUserData("id"),UserId.get(position),"like", URL_LIKES);
                }
            });

            listViewHolder.btn_dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity)getActivity()).changeVisibility(listViewHolder.btn_dislike, View.INVISIBLE);
                    ((HomeActivity)getActivity()).changeVisibility(listViewHolder.btn_like, View.VISIBLE);
                    nr_likes.set(position,nr_likes.get(position)-1);
                    user_like.set(position,"0");
                    listViewHolder.likes.setText(nr_likes.get(position).toString());
                    newLike(ID.get(position), position, getUserData("id"),UserId.get(position),"dislike", URL_LIKES);
                }
            });

            listViewHolder.profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  profileManager.createData(Names.get(position), Usernames.get(position), profile_images.get(position));
                    ((HomeActivity)getActivity()).goToProfil(Usernames.get(position));

                }
            });

            listViewHolder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity)getActivity()).goToProfil(Usernames.get(position));
                }
            });

            HashMap<String, String> user = listViewHolder.sessionManager.getUserDetail();

            if(Usernames.get(position).equals(user.get(listViewHolder.sessionManager.USERNAME))){
                listViewHolder.btn_delete.setVisibility(View.VISIBLE);
            }
            listViewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(getContext(),"it works!", Toast.LENGTH_SHORT).show();
                    deletePost(ID.get(position).toString(),URL_DELETE);
                }
            });

        }

        @Override
        public int getItemCount() {
            return ID.size();
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
                                    id = object.getString("id").trim();
                                    userId = object.getString("user_id").trim();
                                    profileName = object.getString("name").trim();
                                    profileLastName = object.getString("last_name").trim();
                                    profileUsername = object.getString("username").trim();
                                    profile_image = object.getString("profile_picture").trim();
                                    Image = object.getString("pic_name").trim();
                                    Likes = object.getInt("pic_likes");
                                    userLike = object.getString("user_like").trim();
                                    gft = object.getString("gift").trim();

                                    ID.add(id);
                                    UserId.add(userId);
                                    Images.add(Image);
                                    Names.add(profileName + " " + profileLastName);
                                    Usernames.add(profileUsername);
                                    profile_images.add(profile_image);
                                    nr_likes.add(Likes);
                                    user_like.add(userLike);
                                    Gifts.add(gft);

                                    //Toast.makeText(getContext(), user_like.get(i), Toast.LENGTH_SHORT).show();
                                }
                                if(Images.size() == 0)
                                    nothing.setVisibility(View.VISIBLE);
                                else {
                                    listview.setLayoutManager(new LinearLayoutManager(getContext()));
                                    listview.setAdapter(customAdapter);
                                }
                            } else
                                ((HomeActivity)getActivity()).displayError("something went wrong");

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

    private void newLike(final String img_id, final int position, final String user_id, final String user, final String action, final String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            //JSONArray jsonArray = jsonObject.getJSONArray("display");

                            if (!success.equals("1"))
                                ((HomeActivity)getActivity()).displayError("Something went wrong");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            ((HomeActivity)getActivity()).displayError("error" + e.toString());
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
                params.put("img_id", img_id);
                params.put("user_id", user_id);
                params.put("user", user);
                params.put("action", action);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private String getUserData(String data) {
        final SessionManager sessionManager = new SessionManager(getContext());
        HashMap<String, String> user = sessionManager.getUserDetail();

        if (data.equals("id")) return user.get(sessionManager.ID);
        else if (data.equals("name")) return user.get(sessionManager.NAME);
        else if (data.equals("last_name")) return user.get(sessionManager.LAST_NAME);
        else if (data.equals("username")) return user.get(sessionManager.USERNAME);
        else return null;
    }





    public int getWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        return displayMetrics.widthPixels;
    }


    private void deletePost(final String img_id, final String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            //JSONArray jsonArray = jsonObject.getJSONArray("display");

                            if (success.equals("1")) {
                                //Toast.makeText(getContext(), "Successful deleted!", Toast.LENGTH_SHORT).show();
                                ((HomeActivity)getActivity()).displayError("Done");
                            }
                            else ((HomeActivity)getActivity()).displayError("Something went wrong");

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
                params.put("img_id", img_id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}

