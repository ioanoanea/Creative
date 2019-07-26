package com.example.ioanoanea15.creative.pakages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.ioanoanea15.creative.home.HomeActivity;
import com.example.ioanoanea15.creative.login.MainActivity;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MOD = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String LAST_NAME = "LAST_NAME";
    public static final String USERNAME = "USERNAME";
    public static final String EMAIL = "EMAIL";
    public static final String PHOTO = "PHOTO";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MOD);
        editor = sharedPreferences.edit();
    }

    public void createSession(String id, String name, String last_name, String username, String email, String photo) {
        editor.putBoolean(LOGIN, true);
        editor.putString(ID, id);
        editor.putString(NAME, name);
        editor.putString(LAST_NAME, last_name);
        editor.putString(USERNAME, username);
        editor.putString(EMAIL, email);
        editor.putString(PHOTO, photo);
        editor.apply();
    }

    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN,false);
    }

    public void checkLogin(){
        if (this.isLoggin()) {
            Intent intent = new Intent(context, HomeActivity.class);
            context.startActivity(intent);
            ((MainActivity) context).finish();
            //((RegisteredActivity) context).finish();
        }

    }

    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(ID, sharedPreferences.getString(ID,null));
        user.put(NAME, sharedPreferences.getString(NAME,null));
        user.put(LAST_NAME, sharedPreferences.getString(LAST_NAME, null));
        user.put(USERNAME, sharedPreferences.getString(USERNAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(PHOTO, sharedPreferences.getString(PHOTO, null));

        return user;
    }

    public void LogOut(){
        editor.clear();
        editor.commit();

        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ((HomeActivity) context).finish();

    }

}
