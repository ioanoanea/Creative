package com.example.ioanoanea15.creative.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessengerActivity extends AppCompatActivity {


    private TextView name;
    private Button send;
    private EditText text;
    private CircleImageView img;
    private Button back;
    private RecyclerView listView;
    private static String URL_GET_MESSAGES;
    private static String URL_SEND_MESSAGE;
    private static String URL_GET_USER_DATA;

    private ArrayList<String> ID = new ArrayList<>();
    private ArrayList<String> Message = new ArrayList<>();
    private ArrayList<String> Type = new ArrayList<>();
    private ArrayList<Boolean> IsPhoto= new ArrayList<>();

    SessionManager sessionManager;
    CustomAdapter customAdapter = new CustomAdapter();




    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                Intent getUser = getIntent();
                String userid = getUser.getStringExtra("id");
                //Toast.makeText(context,intent.getStringExtra("from"),Toast.LENGTH_SHORT).show();

                if(userid.equals(intent.getStringExtra("from"))) {

                    String message = intent.getStringExtra("message");
                    String isPhoto = intent.getStringExtra("isphoto");
                    ID.add("0");
                    Message.add(message);
                    Type.add("received");
                    if(isPhoto.equals("1"))
                        IsPhoto.add(true);
                    else IsPhoto.add(false);

                    customAdapter.notifyItemInserted(ID.size() - 1);
                    listView.scrollToPosition(ID.size() - 1);

                } else Toast.makeText(context,"You have a new message",Toast.LENGTH_SHORT).show();

        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        name = findViewById(R.id.name);
        send = findViewById(R.id.send);
        text = findViewById(R.id.text);
        img = findViewById(R.id.img);
        back = findViewById(R.id.back);
        listView = findViewById(R.id.list_messages);

        URL_GET_MESSAGES = getResources().getString(R.string.server) + "/get_messages.php";
        URL_SEND_MESSAGE = getResources().getString(R.string.server) + "/send_message.php";
        URL_GET_USER_DATA = getResources().getString(R.string.server) + "/get_user_data.php";

        text.setSelected(false);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessengerActivity.this);
        listView.setLayoutManager(linearLayoutManager);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessengerActivity.super.onBackPressed();
            }
        });

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        final String myId = user.get(sessionManager.ID);
        final String myName = user.get(sessionManager.NAME) + " " + user.get(sessionManager.LAST_NAME);


        Intent intent = getIntent();
        final String userId = intent.getStringExtra("id");
        final String Name = intent.getStringExtra("name");
        final String Photo = intent.getStringExtra("photo");

        name.setText(Name);
        Picasso.get().load(Photo).into(img);

        getMessages(myId,userId,URL_GET_MESSAGES);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = text.getText().toString();
                if(!message.isEmpty()){
                    text.setText("");
                    addSentMessage(message);
                    sendMessage(myId,userId,myName,message,"0",URL_SEND_MESSAGE);
                }
            }
        });

    }





    @Override
    protected void onResume(){
        super.onResume();
        registerReceiver(broadcastReceiver,new IntentFilter("UpdateMessages"));
    }


    @Override
    protected void onStop(){
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }





    public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public class ListViewHolder extends RecyclerView.ViewHolder {

            private TextView receivedMessage, sentMessage;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                receivedMessage = itemView.findViewById(R.id.received_message);
                sentMessage = itemView.findViewById(R.id.sended_message);

            }
        }

        public class PictureListViewHolder extends RecyclerView.ViewHolder {

            private ImageView receivedImg, sentImg;

            public PictureListViewHolder(@NonNull View itemView) {
                super(itemView);
                receivedImg = itemView.findViewById(R.id.received_img);
                sentImg = itemView.findViewById(R.id.sended_img);
            }
        }


        @Override
        public int getItemViewType(int position){
            if(IsPhoto.get(position) == true)
                return 1;
            else return 2;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            if(viewType == 1){
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.picture_item_message, viewGroup, false);
                return new PictureListViewHolder(view);
            } else {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item, viewGroup, false);
                return new ListViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

            if(viewHolder.getItemViewType() == 1){
                final PictureListViewHolder pictureListViewHolder = (PictureListViewHolder)viewHolder;
                if(Type.get(position).equals("received")){
                    pictureListViewHolder.sentImg.setVisibility(View.INVISIBLE);
                    pictureListViewHolder.receivedImg.setVisibility(View.VISIBLE);
                    Picasso.get().load(Message.get(position)).into(pictureListViewHolder.receivedImg);
                } else {
                    pictureListViewHolder.receivedImg.setVisibility(View.INVISIBLE);
                    pictureListViewHolder.sentImg.setVisibility(View.VISIBLE);
                    Picasso.get().load(Message.get(position)).into(pictureListViewHolder.sentImg);
                }

            } else {
                final ListViewHolder listViewHolder = (ListViewHolder)viewHolder;

                int width = getScreenWidth();

                if(width > 1400)
                    width -= 800;
                else if(width < 800)
                    width -= 250;
                else width -= 500;

                listViewHolder.receivedMessage.setMaxWidth(width);
                listViewHolder.sentMessage.setMaxWidth(width);

                if(Type.get(position).equals("received")){
                    listViewHolder.receivedMessage.setVisibility(View.VISIBLE);
                    listViewHolder.receivedMessage.setText(Message.get(position));
                    listViewHolder.sentMessage.setText("");
                    listViewHolder.sentMessage.setVisibility(View.INVISIBLE);
                } else {
                    listViewHolder.sentMessage.setVisibility(View.VISIBLE);
                    listViewHolder.sentMessage.setText(Message.get(position));
                    listViewHolder.receivedMessage.setText("");
                    listViewHolder.receivedMessage.setVisibility(View.INVISIBLE);
                }

            }
        }

        @Override
        public int getItemCount() {
            return ID.size();
        }
    }





    public void getMessages(final String id, final String from, final String URL) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.i(TAG, response.toString());
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("display");
                            if(success.equals("1")){
                               for(int i = 0; i < jsonArray.length(); i++){
                                   JSONObject object = jsonArray.getJSONObject(i);

                                   ID.add(object.getString("id").trim());
                                   Message.add(object.getString("message").trim());
                                   Type.add(object.getString("type").trim());

                                   if(object.getString("is_photo").equals("1"))
                                       IsPhoto.add(true);
                                   else IsPhoto.add(false);
                                   //Toast.makeText(MessengerActivity.this, object.getString("type").trim(),Toast.LENGTH_SHORT).show();
                               }
                                listView.setAdapter(customAdapter);
                                listView.scrollToPosition(ID.size()-1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                                   // displayError("Something went wrong");
                             Toast.makeText(MessengerActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displayError("Something went wrong");
                        Toast.makeText(MessengerActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                params.put("from",from);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }




    private void sendMessage(final String id, final String to, final String name, final String message, final String isPhoto, final String URL){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.i(TAG, response.toString());
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")){
                                   //
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // displayError("Something went wrong");
                            Toast.makeText(MessengerActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displayError("Something went wrong");
                        Toast.makeText(MessengerActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("from",id);
                params.put("to",to);
                params.put("name",name);
                params.put("message",message);
                params.put("is_photo",isPhoto);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }




    private void addSentMessage(String message){

        ID.add("0");
        Type.add("sent");
        IsPhoto.add(false);
        Message.add(message);

        customAdapter.notifyItemInserted(ID.size() - 1);
        listView.scrollToPosition(ID.size() - 1);

    }

    public int getScreenWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.widthPixels;
    }

    private String getUserId(){
        Intent intent = getIntent();
        return intent.getStringExtra("id");
    }


}
