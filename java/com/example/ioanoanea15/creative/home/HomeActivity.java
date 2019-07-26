package com.example.ioanoanea15.creative.home;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ioanoanea15.creative.pakages.TokenManager;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.ioanoanea15.creative.messenger.MessengerActivity;
import com.example.ioanoanea15.creative.pakages.SessionManager;
import com.example.ioanoanea15.creative.pakages.DeviceLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Button menu, friends, search, add, home, notifications, top, chat, userFriends;
    private TextView name, username,theme;
    private TextView error;
    private ImageView new_notifications;
    private ImageView new_message;
    private ImageView profileImg;
    String getId, profile_pic_url, getName, getLast_name, getUsername,getEmail;
    private ConstraintLayout head;
    private int fragmentManager = 1;
    private LinearLayout background;
    private static final String TAG = HomeActivity.class.getSimpleName();
    private static String URL_UPLOAD_PROFILE_IMAGE;
    private static String URL_UPLOAD_PICTURE;
    private static String URL_GET_THEME;
    private static String URL_IS_NOTIFICATIONS;
    private static String URL_AD_LOCATION;
    private static String URL_UPDATE_TOKEN;
    private Bitmap bitmap;
    public String updated_photo;
    private String isNotifications;
    String[] profileNames, profileUsernames, profileImages, Images, Likes;

    SessionManager sessionManager;
    final DeviceLocation deviceLocation = new DeviceLocation();



    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            displayError("You have a new message");
            new_message.setVisibility(View.VISIBLE);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headView = navigationView.getHeaderView(0);
        name = headView.findViewById(R.id.profile_name);
        username = headView.findViewById(R.id.profile_username);
        profileImg = headView.findViewById(R.id.profile_img);
        head = findViewById(R.id.head);


        URL_UPLOAD_PROFILE_IMAGE = getString(R.string.server) + "/upload_profile_image.php";
        URL_UPLOAD_PICTURE = getString(R.string.server) + "/upload.php";
        URL_GET_THEME = getResources().getString(R.string.server) + "/get_theme.php";
        URL_IS_NOTIFICATIONS = getResources().getString(R.string.server) + "/is_notifications.php";
        URL_AD_LOCATION = getResources().getString(R.string.server) + "/ad_location.php";
        URL_UPDATE_TOKEN = getResources().getString(R.string.server) + "/update_token.php";

        setViews();

        //get screen size
         DisplayMetrics displayMetrics = new DisplayMetrics();
         getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

         final  int screenWidth = displayMetrics.widthPixels;

        final PublicFragment publicFragment = new PublicFragment();
        final  FollowingFragment folowingFragment = new FollowingFragment();


        //getFragmentManager().beginTransaction().replace(R.id.home_container, profilFragment).addToBackStack(null).commit();


      //setare informatii user
      sessionManager = new SessionManager(this);

      HashMap<String, String> user = sessionManager.getUserDetail();
      getName = user.get(sessionManager.NAME);
      getLast_name = user.get(sessionManager.LAST_NAME);
      getUsername = user.get(sessionManager.USERNAME);
      getEmail = user.get(sessionManager.EMAIL);
      getId = user.get(sessionManager.ID);
      profile_pic_url = user.get(sessionManager.PHOTO);



        Picasso.get().load(profile_pic_url).into(profileImg);

      name.setText(getName + " " + getLast_name);
      username.setText(getUsername);

      getTheme(getId,URL_GET_THEME);
      getLocation();
      setToken(getId,URL_UPDATE_TOKEN);

      //Actiuni butoane
        menu.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              drawer.openDrawer(GravityCompat.START);
          }
      });

        home.setBackgroundResource(R.drawable.ic_home_red);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(HomeActivity.this, "This function will be available soon", Toast.LENGTH_SHORT).show();
                setNvbarcolor();
                search.setBackgroundResource(R.drawable.ic_search_red);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new SearchFragment()).addToBackStack(null).commit();
                displayError("INVISIBLE");
            }
        });
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 goToProfil(getUsername);
                 drawer.closeDrawer(GravityCompat.START);
                displayError("INVISIBLE");
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
                else chooseFile(2);
                displayError("INVISIBLE");
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNvbarcolor();
                home.setBackgroundResource(R.drawable.ic_home_red);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new PublicFragment()).addToBackStack(null).commit();
                displayError("INVISIBLE");
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNvbarcolor();
                new_notifications.setVisibility(View.INVISIBLE);
                notifications.setBackgroundResource(R.drawable.ic_notificatipns_red);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_container,new notificationsFragment()).addToBackStack(null).commit();
                displayError("INVISIBLE");
            }
        });
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNvbarcolor();
                top.setBackgroundResource(R.drawable.ic_top_red);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new TopFragment()).addToBackStack(null).commit();
                displayError("INVISIBLE");
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_message.setVisibility(View.INVISIBLE);
                setNvbarcolor();
                chat.setBackgroundResource(R.drawable.ic_send_red);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new ChatFragment()).addToBackStack(null).commit();
                displayError("INVISIBLE");
            }
        });



        //container manager
            getNotifications(getId,URL_IS_NOTIFICATIONS);
            getSupportFragmentManager().beginTransaction().replace(R.id.home_container, publicFragment).addToBackStack(null).commit();


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



    public void setViews(){
        drawer = findViewById(R.id.home_layout);
        menu = findViewById(R.id.menu_button);
        search = findViewById(R.id.btn_search);
        add = findViewById(R.id.btn_ad);
        home = findViewById(R.id.btn_home);
        notifications = findViewById(R.id.btn_notifications);
        top = findViewById(R.id.btn_top);
        chat = findViewById(R.id.btn_chat);
        error = findViewById(R.id.error);
        theme = findViewById(R.id.theme);
        new_notifications = findViewById(R.id.new_notifications);
        new_message = findViewById(R.id.new_message);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.nav_settings){
           getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new SettingsFragment()).addToBackStack(null).commit();
           setNvbarcolor();
        } else if(menuItem.getItemId() == R.id.nav_info){
            getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new InfoFragment()).addToBackStack(null).commit();
            setNvbarcolor();
        } else if(menuItem.getItemId() == R.id.nav_logout){
            sessionManager.LogOut();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    //Alegerea unei fotografii
    public void chooseFile(int code){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "Select picture"), code);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profileImg.setImageBitmap(bitmap);

            } catch (IOException e){
                e.printStackTrace();
            }
            UploadPicture(getId, getStringImage(bitmap),URL_UPLOAD_PROFILE_IMAGE,1);
        } else if(requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e){
                e.printStackTrace();
            }
            UploadPicture(getId, getStringImage(bitmap),URL_UPLOAD_PICTURE,2);
        } else displayError("Try again!");


    }





    //Incarcare fotografie pe server
    private void UploadPicture(final String id, final String photo, final String URL, final int action){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        //Log.i(TAG, response.toString());
                        try{
                          JSONObject jsonObject = new JSONObject(response);
                          String success = jsonObject.getString("success");
                          String message = jsonObject.getString("message");

                          if(success.equals("1")){
                             if(action == 1) {
                                 updated_photo = message;
                                 sessionManager.createSession(getId, getName, getLast_name, getUsername, getEmail, message);
                                 getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new SettingsFragment()).addToBackStack(null).commit();
                             }
                              displayError("Done");
                          }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            displayError("Try again!");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        displayError("Try again!");
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                params.put("photo", photo);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    //Convertire fotografie in format string
    public String getStringImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }

    public int getScreenWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.widthPixels;
    }

    public void displayError(String err) {
        error.setVisibility(View.VISIBLE);
        error.setBackgroundColor(getResources().getColor(R.color.err_color2));
        if(err.equals("Done"))
            error.setBackgroundColor(getResources().getColor(R.color.err_color3));
        else if(err.equals("You have a new message"))
            error.setBackgroundColor(getResources().getColor(R.color.err_color4));
        if(err.equals("INVISIBLE"))
            error.setVisibility(View.INVISIBLE);
        else error.setText(err);

        /*error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error.setVisibility(View.INVISIBLE);
            }
        });*/
    }

    public String getUserData(String data) {
        final SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();

        if (data.equals("id")) return user.get(sessionManager.ID);
        else if (data.equals("name")) return user.get(sessionManager.NAME);
        else if (data.equals("last_name")) return user.get(sessionManager.LAST_NAME);
        else if (data.equals("username")) return user.get(sessionManager.USERNAME);
        else return null;
    }

    public void goToProfil(String username) {

        ProfilFragment profilFragment = new ProfilFragment();

        Bundle bundle = new Bundle();
        bundle.putString("username", username);

        profilFragment.setArguments(bundle);

        setNvbarcolor();
        getSupportFragmentManager().beginTransaction().replace(R.id.home_container, profilFragment).addToBackStack(null).commit();
    }

    public void setNvbarcolor(){
        home.setBackgroundResource(R.drawable.ic_home);
        menu.setBackgroundResource(R.drawable.ic_menu);
        add.setBackgroundResource(R.drawable.ic_add);
        search.setBackgroundResource(R.drawable.ic_search);
        notifications.setBackgroundResource(R.drawable.ic_notifications);
        top.setBackgroundResource(R.drawable.ic_top);
        chat.setBackgroundResource(R.drawable.ic_send);
    }

    public void getTheme(final String id, final String URL){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.i(TAG, response.toString());
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            if(success.equals("1")){
                                theme.setText(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            displayError("Something went wrong");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        displayError("Something went wrong");
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void getNotifications(final String id, final String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")){
                               isNotifications = message;
                               if(!isNotifications.equals("0"))
                                new_notifications.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            displayError("Something went wrong");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        displayError("Something went wrong");
                       // Toast.makeText(HomeActivity.this,err.toString(),Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void sendLocation(final String id, final String lat, final String lon, final String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                               //do nothing
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            displayError("Something went wrong");
                            //Toast.makeText(HomeActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        displayError("Something went wrong");
                         //Toast.makeText(HomeActivity.this,err.toString(),Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("latitude", lat);
                params.put("longitude", lon);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void changeVisibility(Button button, int visibilty){
            button.setVisibility(visibilty);
    }


    public void getLocation(){
        FusedLocationProviderClient fusedLocationClient;
        sessionManager = new SessionManager(this);

        final HashMap<String, String> user = sessionManager.getUserDetail();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if( ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
        else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                deviceLocation.setLatitude(location.getLatitude());
                                deviceLocation.setLongitude(location.getLongitude());
                                //Toast.makeText(HomeActivity.this, "la:" + String.valueOf(deviceLocation.getLattitude()) + "\nlo:" + String.valueOf(deviceLocation.getLongitude()), Toast.LENGTH_SHORT).show();
                                sendLocation(user.get(sessionManager.ID), String.valueOf(deviceLocation.getLatitude()), String.valueOf(deviceLocation.getLongitude()),URL_AD_LOCATION);
                            }
                        }
                    });
        }
    }

    public double deviceLatitude(){
        return deviceLocation.getLatitude();
    }

    public double deviceLongitude(){
        return deviceLocation.getLongitude();
    }

    public void goToMessenger(String id, String name, String photo){
        Intent intent = new Intent(HomeActivity.this, MessengerActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("name",name);
        intent.putExtra("photo",photo);
        startActivity(intent);
    }



    public void setToken(final String id, final String URL){

        TokenManager tokenManager = new TokenManager(this);
        final String token = tokenManager.getToken();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.i(TAG, response.toString());
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")){
                                //Toast.makeText(HomeActivity.this,"success",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            displayError("Something went wrong");
                           // Toast.makeText(HomeActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        displayError("Something went wrong");
                        //Toast.makeText(HomeActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                params.put("token",token);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }




    public void sendMessage(final String id, final String to, final String name, final String message, final String isPhoto, final String URL){

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
                            Toast.makeText(HomeActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displayError("Something went wrong");
                        Toast.makeText(HomeActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
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

}
