package com.example.ioanoanea15.creative.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class ChatFragment extends Fragment {

    private ArrayList<String> Id = new ArrayList<>();
    private ArrayList<String> UserId = new ArrayList<>();
    private ArrayList<String> Name = new ArrayList<>();
    private ArrayList<String> Photo = new ArrayList<>();
    private ArrayList<String> LastMessage = new ArrayList<>();
    private ArrayList<Boolean> Seen = new ArrayList<>();

    private static String URL_GET_CHAT;
    private static String URL_SET_SEEN;
    private RecyclerView listView;
    private SwipeRefreshLayout refresh;

    CustomAdapter customAdapter = new CustomAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        URL_GET_CHAT = getResources().getString(R.string.server) + "/get_chat.php";
        URL_SET_SEEN = getResources().getString(R.string.server) + "/set_seen_message.php";
        listView = view.findViewById(R.id.chat_list);
        refresh = view.findViewById(R.id.refresh);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));


        SessionManager sessionManager = new SessionManager(getContext());

        HashMap<String, String> user = sessionManager.getUserDetail();
        final String id = user.get(sessionManager.ID);

        getCaht(id);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Clear();
                getFragmentManager().beginTransaction().replace(R.id.home_container,new ChatFragment()).addToBackStack(null).commit();
                refresh.setRefreshing(false);
                ((HomeActivity)getActivity()).displayError("INVISIBLE");
            }
        });
    }



    class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        class ListViewHolder extends RecyclerView.ViewHolder {

            CircleImageView img;
            TextView name, lastMessage;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);

                img = itemView.findViewById(R.id.img_profil);
                name = itemView.findViewById(R.id.text_name);
                lastMessage = itemView.findViewById(R.id.username);
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_profil, viewGroup, false);
            return new ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
            final ListViewHolder listViewHolder = (ListViewHolder)viewHolder;

            Picasso.get().load(Photo.get(position)).into(listViewHolder.img);
            listViewHolder.name.setText(Name.get(position));
            listViewHolder.lastMessage.setText(LastMessage.get(position));
            if(!Seen.get(position)) {
                listViewHolder.lastMessage.setTextColor(getResources().getColor(R.color.color4));
                listViewHolder.lastMessage.setTypeface(listViewHolder.lastMessage.getTypeface(), Typeface.BOLD);
            }

            listViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listViewHolder.lastMessage.setTypeface(listViewHolder.lastMessage.getTypeface(),Typeface.ITALIC);
                    listViewHolder.lastMessage.setTextColor(getResources().getColor(R.color.color9));
                    setMessageSeen(Id.get(position),URL_SET_SEEN);
                    ((HomeActivity)getActivity()).goToMessenger(UserId.get(position),Name.get(position),Photo.get(position));
                }
            });

        }

        @Override
        public int getItemCount() {
            return Id.size();
        }
    }

    public void getCaht(final String id){

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_CHAT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.i(TAG, response.toString());
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                JSONArray jsonArray = jsonObject.getJSONArray("display_chat");
                                if(success.equals("1")){
                                    for(int i = 0; i < jsonArray.length(); i++){
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        Id.add(object.getString("id").trim());
                                        Name.add(object.getString("name") + " " + object.getString("last_name").trim());
                                        Photo.add(object.getString("photo").trim());
                                        LastMessage.add(object.getString("last_message").trim());
                                        if(object.getString("seen").trim().equals("1"))
                                            Seen.add(true);
                                        else Seen.add(false);
                                        if(object.getString("user1").trim().equals(id))
                                            UserId.add(object.getString("user2").trim());
                                        else UserId.add(object.getString("user1").trim());

                                        //Toast.makeText(getContext(), Name.get(i) + " " + UserId.get(i),Toast.LENGTH_SHORT).show();
                                    }
                                    listView.setAdapter(customAdapter);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                // displayError("Something went wrong");
                                Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //displayError("Something went wrong");
                            Toast.makeText(getContext(),error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id",id);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
    }






    private void setMessageSeen(final String id, final String URL){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.i(TAG, response.toString());
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")){

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // displayError("Something went wrong");
                            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displayError("Something went wrong");
                        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }




    private void Clear(){
        Id.clear();
        LastMessage.clear();
        Name.clear();
        Seen.clear();
        UserId.clear();
        Photo.clear();
    }

}
