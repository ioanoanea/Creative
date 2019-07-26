package com.example.ioanoanea15.creative.home;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchFragment extends Fragment {

    //liste persoane
    ArrayList<String> ID = new ArrayList<>();
    ArrayList<String> UserImages = new ArrayList<>();
    ArrayList<String> Names = new ArrayList<>();
    ArrayList<String> Usernames = new ArrayList<>();
    ArrayList<Double> Latitude = new ArrayList<>();
    ArrayList<Double> Longitude = new ArrayList<>();

    //liste fotografii
    ArrayList<String> PicId = new ArrayList<>();
    ArrayList<String> Images = new ArrayList<>();
    ArrayList<String> UserId = new ArrayList<>();
    ArrayList<String> PicNames = new ArrayList<>();
    ArrayList<String> PicUsernames = new ArrayList<>();
    ArrayList<String> profile_images = new ArrayList<>();
    ArrayList<Integer> nr_likes = new ArrayList<>();
    ArrayList<String> user_like = new ArrayList<>();
    ArrayList<String> Gifts = new ArrayList<>();
    ArrayList<String> Themes  = new ArrayList<>();

    //liste rezultate
    ArrayList<String> FoundId = new ArrayList<>();
    ArrayList<String> FoundNames = new ArrayList<>();
    ArrayList<String> FoundUsernames = new ArrayList<>();
    ArrayList<String> FoundImages = new ArrayList<>();
    ArrayList<String> FoundUserId = new ArrayList<>();
    ArrayList<String> FoundProfilPhoto = new ArrayList<>();
    ArrayList<Integer> FoundNrLIkes = new ArrayList<>();
    ArrayList<String> FoundUserLike = new ArrayList<>();
    ArrayList<String> FoundGifts = new ArrayList<>();

    //liste locatie
    ArrayList<String> LocId = new ArrayList<>();
    ArrayList<String> LocPhoto = new ArrayList<>();
    ArrayList<String> LocUsername = new ArrayList<>();
    ArrayList<Double> Distance = new ArrayList<>();


    private static  String URL_SEARCH, URL_SEARCH_PICTURES, URL_LIKES;
    private TextView error;
    private String id, name, last_name, username, photo;
    private RecyclerView listView;
    private androidx.appcompat.widget.SearchView searchView;
    private int contor = -1;
    private Button people, tag;
    private int searchMethod = 1;
    CustomAdapter customAdapter = new CustomAdapter();
    SessionManager sessionManager;
    ArrayList<String> test = new ArrayList();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // search = view.findViewById(R.id.search_box);
        error = view.findViewById(R.id.error);
        listView = view.findViewById(R.id.nv_list);
        searchView = view.findViewById(R.id.search_box);
        people = view.findViewById(R.id.people);
        tag = view.findViewById(R.id.tag);

        URL_SEARCH = getString(R.string.server) + "/search.php";
        URL_SEARCH_PICTURES = getString(R.string.server) + "/search_pictures.php";
        URL_LIKES = getResources().getString(R.string.server) + "/like.php";

        sessionManager = new SessionManager(getContext());
        HashMap<String, String> user = sessionManager.getUserDetail();
        final String mId = user.get(sessionManager.ID);



        test.add("http://192.168.1.7/profile_image/1.1.jpeg");
        test.add("http://192.168.1.7/profile_image/2.1.jpeg");
        test.add("http://192.168.1.7/profile_image/1.1.jpeg");
        test.add("http://192.168.1.7/profile_image/2.1.jpeg");
        test.add("http://192.168.1.7/profile_image/3.1.jpeg");
        test.add("http://192.168.1.7/profile_image/4.1.jpeg");
        test.add("http://192.168.1.7/profile_image/1.1.jpeg");


        getPeople("search",URL_SEARCH);
        getPictures(mId, "search", URL_SEARCH_PICTURES);

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(searchMethod == 1) {
                    if (s!=null && !s.isEmpty()){
                        emptyLists();
                        for(int i = 0; i < ID.size(); i++)
                            if(Names.get(i).toLowerCase().contains(s.toLowerCase()) || Usernames.get(i).toLowerCase().contains(s.toLowerCase())){
                                FoundNames.add(Names.get(i));
                                FoundUsernames.add(Usernames.get(i));
                                FoundId.add(ID.get(i));
                                FoundImages.add(UserImages.get(i));
                            }
                    } else emptyLists();
                    listView.setAdapter(customAdapter);
                } else {
                    if(s!=null && !s.isEmpty()){
                        emptyLists();
                        for(int i = 0; i < PicId.size(); i++)
                            if(Themes.get(i).toLowerCase().contains(s.toLowerCase())){
                                FoundId.add(PicId.get(i));
                                FoundNames.add(PicNames.get(i));
                                FoundUsernames.add(PicUsernames.get(i));
                                FoundImages.add(Images.get(i));
                                FoundProfilPhoto.add(profile_images.get(i));
                                FoundUserLike.add(user_like.get(i));
                                FoundNrLIkes.add(nr_likes.get(i));
                                FoundGifts.add(Gifts.get(i));
                            }
                    } else emptyLists();
                    listView.setAdapter(customAdapter);
                }
                return false;
            }
        });

        people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMethod = 1;
                people.setVisibility(View.INVISIBLE);
                tag.setVisibility(View.VISIBLE);
            }
        });

        tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMethod = 2;
                tag.setVisibility(View.INVISIBLE);
                people.setVisibility(View.VISIBLE);
            }
        });

    }



    //ViewHolders
    public class ListViewHolder1 extends RecyclerView.ViewHolder {

        CircleImageView profil_img;
        TextView name, username;

        public ListViewHolder1(@NonNull View itemView) {
            super(itemView);

            profil_img = itemView.findViewById(R.id.img_profil);
            name = itemView.findViewById(R.id.text_name);
            username = itemView.findViewById(R.id.username);
        }
    }

    public class ListViewHolder2 extends RecyclerView.ViewHolder {

        SessionManager sessionManager = new SessionManager(getContext());

        ImageView image, gift;
        CircleImageView profileImage;
        TextView name, username, likes;
        Button btn_like, btn_dislike, btn_delete;
        Bundle bundle = getArguments();
        String Gift;

        public ListViewHolder2(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.display_image);
            profileImage = itemView.findViewById(R.id.post_profile_image);
            name = itemView.findViewById(R.id.post_profile_name);
            // username = itemView.findViewById(R.id.post_profile_username);
            likes = itemView.findViewById(R.id.likes);
            btn_like = itemView.findViewById(R.id.btn_likes);
            btn_dislike = itemView.findViewById(R.id.btn_liked);
            gift = itemView.findViewById(R.id.gift);
        }
    }



    public class ListViewHolder3 extends RecyclerView.ViewHolder {
        RecyclerView list;
        public ListViewHolder3(@NonNull View itemView) {
            super(itemView);
            list = itemView.findViewById(R.id.list_nearby_people);
        }
    }


    //Adapter
    public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context context;

        @Override
        public int getItemViewType(int position) {
            if(FoundId.size() == 0)
                return 3;
            else if(searchMethod == 1)
                return 1;
            else return 2;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            if(viewType == 3) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nearby_people, viewGroup, false);
                return new ListViewHolder3(view);
            } else if(viewType == 1) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_profil, viewGroup, false);
                return  new ListViewHolder1(view);
            } else {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);
                return new ListViewHolder2(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            if(viewHolder.getItemViewType() == 3){
                final ListViewHolder3 listViewHolder3 = (ListViewHolder3)viewHolder;
                NearbyPeopleListAdapter adapter = new NearbyPeopleListAdapter();

                listViewHolder3.list.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                listViewHolder3.list.setAdapter(adapter);

            } else if(viewHolder.getItemViewType() == 1){

                final ListViewHolder1 listViewHolder1 = (ListViewHolder1)viewHolder;
                final int position = i;

                Picasso.get().load(FoundImages.get(position)).into(listViewHolder1.profil_img);
                listViewHolder1.name.setText(FoundNames.get(position));
                listViewHolder1.username.setText(FoundUsernames.get(position));

                listViewHolder1.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((HomeActivity)getActivity()).goToProfil(FoundUsernames.get(position));
                    }
                });
            } else {

                final ListViewHolder2 listViewHolder2 = (ListViewHolder2)viewHolder;
                final int position = i;

                Picasso.get().load(FoundImages.get(position)).resize(getWidth(), 0).into(listViewHolder2.image);
                Picasso.get().load(FoundProfilPhoto.get(position)).into(listViewHolder2.profileImage);
                listViewHolder2.name.setText(FoundNames.get(position));
                listViewHolder2.likes.setText(FoundNrLIkes.get(position).toString());



                listViewHolder2.Gift = FoundGifts.get(position);

                if(listViewHolder2.Gift.equals("1")){
                    listViewHolder2.gift.setVisibility(View.VISIBLE);
                    listViewHolder2.gift.setImageResource(R.drawable.gold_cup);
                } else if(listViewHolder2.Gift.equals("2")){
                    listViewHolder2.gift.setVisibility(View.VISIBLE);
                    listViewHolder2.gift.setImageResource(R.drawable.silver_cup);
                } else if(listViewHolder2.Gift.equals("3")){
                    listViewHolder2.gift.setVisibility(View.VISIBLE);
                    listViewHolder2.gift.setImageResource(R.drawable.bronze_cup);
                } else if(listViewHolder2.Gift.equals("4")) {
                    listViewHolder2.gift.setVisibility(View.VISIBLE);
                    listViewHolder2.gift.setImageResource(R.drawable.insigne);
                }




                if (FoundUserLike.get(position).equals("1")) {
                    listViewHolder2.btn_dislike.setVisibility(View.VISIBLE);
                    listViewHolder2.btn_like.setVisibility(View.INVISIBLE);
                }

                listViewHolder2.btn_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listViewHolder2.btn_like.setVisibility(View.INVISIBLE);
                        listViewHolder2.btn_dislike.setVisibility(View.VISIBLE);
                        FoundNrLIkes.set(position,nr_likes.get(position)+1);
                        FoundUserLike.set(position,"1");
                        listViewHolder2.likes.setText(nr_likes.get(position).toString());
                        newLike(FoundId.get(position), position, getUserData("id"),FoundUserId.get(position),"like", URL_LIKES);
                    }
                });

                listViewHolder2.btn_dislike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listViewHolder2.btn_dislike.setVisibility(View.INVISIBLE);
                        listViewHolder2.btn_like.setVisibility(View.VISIBLE);
                        FoundNrLIkes.set(position,nr_likes.get(position)-1);
                        FoundUserLike.set(position,"0");
                        listViewHolder2.likes.setText(FoundNrLIkes.get(position).toString());
                        newLike(FoundId.get(position), position, getUserData("id"),FoundUserId.get(position),"dislike", URL_LIKES);
                    }
                });

                listViewHolder2.profileImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  profileManager.createData(Names.get(position), Usernames.get(position), profile_images.get(position));
                        ((HomeActivity)getActivity()).goToProfil(FoundUsernames.get(position));

                    }
                });

                listViewHolder2.name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((HomeActivity)getActivity()).goToProfil(FoundUsernames.get(position));
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if(FoundId.size() == 0)
                return 1;
            else return FoundId.size();
        }
    }


    public int getWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        return displayMetrics.widthPixels;
    }

    private void getPeople(final String search, final String URL) {
         emptyLists();
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
                                    name = object.getString("name").trim();
                                    last_name = object.getString("last_name").trim();
                                    username = object.getString("username").trim();
                                    photo = object.getString("photo").trim();
                                    double latitude = object.getDouble("latitude");
                                    double longitude = object.getDouble("longitude");

                                    ID.add(id);
                                    Names.add(name+" "+last_name);
                                    Usernames.add(username);
                                    UserImages.add(photo);
                                    Latitude.add(latitude);
                                    Longitude.add(longitude);

                                    //Toast.makeText(getContext(), name+" "+last_name+" "+photo, Toast.LENGTH_SHORT).show();
                                }
                                setNearbyPeople();
                                listView.setLayoutManager(new LinearLayoutManager(getContext()));
                                listView.setAdapter(customAdapter);
                                //contor = ID.size()-1;
                            } else
                                ((HomeActivity)getActivity()).displayError("Something went wrong");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                            ((HomeActivity)getActivity()).displayError(" Something went wrong");
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
                params.put("search", search);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void getPictures(final String username, final String search, final String URL) {
        emptyLists();
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
                                    String userId = object.getString("user_id").trim();
                                    String profileName = object.getString("name").trim();
                                    String profileLastName = object.getString("last_name").trim();
                                    String profileUsername = object.getString("username").trim();
                                    String profile_image = object.getString("profile_picture").trim();
                                    String Image = object.getString("pic_name").trim();
                                    Integer Likes = object.getInt("pic_likes");
                                    String userLike = object.getString("user_like").trim();
                                    String gft = object.getString("gift").trim();
                                    String theme = object.getString("theme").trim();

                                    PicId.add(id);
                                    UserId.add(userId);
                                    Images.add(Image);
                                    PicNames.add(profileName + " " + profileLastName);
                                    PicUsernames.add(profileUsername);
                                    profile_images.add(profile_image);
                                    nr_likes.add(Likes);
                                    user_like.add(userLike);
                                    Gifts.add(gft);
                                    Themes.add(theme);

                                    //Toast.makeText(getContext(), user_like.get(i), Toast.LENGTH_SHORT).show();
                                }
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
                params.put("search", search);
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

                            if (success.equals("1")) {
                                user_like.set(position,"1");
                            } else if(success.equals("-1")){
                                user_like.set(position,"0");
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
                        ((HomeActivity)getActivity()).displayError("Something went wrong");
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

    private void emptyLists(){

            FoundId.clear();
            FoundNames.clear();
            FoundUsernames.clear();
            FoundImages.clear();
            FoundUserId.clear();
            FoundProfilPhoto.clear();
            FoundNrLIkes.clear();
            FoundUserLike.clear();
            FoundGifts.clear();
    }


     public class NearbyPeopleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

         @NonNull
         @Override
         public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
             View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_nearby_people, viewGroup, false);
             return new ListViewHolder(view);
         }

         @Override
         public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
              final ListViewHolder listViewHolder = (ListViewHolder)viewHolder;

              Picasso.get().load(LocPhoto.get(i)).into(listViewHolder.circleImageView);
             DecimalFormat dec = new DecimalFormat("#0");
              listViewHolder.km.setText(String.valueOf(dec.format(Distance.get(i)/1000)) + "Km");
              listViewHolder.circleImageView.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      ((HomeActivity)getActivity()).goToProfil(LocUsername.get(i));
                  }
              });
         }

         @Override
         public int getItemCount() {
             return LocId.size();
         }

         private class ListViewHolder extends RecyclerView.ViewHolder{

             CircleImageView circleImageView;
             TextView km;

             public ListViewHolder(@NonNull View itemView) {
                 super(itemView);
                 circleImageView = itemView.findViewById(R.id.nearby_people_img);
                 km = itemView.findViewById(R.id.km);
             }
         }

     }



     private void setNearbyPeople(){

         double latitude = ((HomeActivity)getActivity()).deviceLatitude();
         double longitude = ((HomeActivity)getActivity()).deviceLongitude();

         Location locationA = new Location("Point A");
         locationA.setLatitude(latitude);
         locationA.setLongitude(longitude);

         for(int i = 0; i < ID.size(); i++){
             Location locationB = new Location("Point B");
             locationB.setLatitude(Latitude.get(i));
             locationB.setLongitude(Longitude.get(i));

             double distance = locationA.distanceTo(locationB);
             //Toast.makeText(getContext(),String.valueOf(distance),Toast.LENGTH_LONG).show();
             if(distance < 50000){
                 LocId.add(ID.get(i));
                 LocPhoto.add(UserImages.get(i));
                 LocUsername.add(Usernames.get(i));
                 Distance.add(distance);
             }
         }

     }




}
