package com.example.ioanoanea15.creative.pakages;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class ProfileManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MOD = 0;

    private static final String PREF_NAME = "PROFIL";
    public static final String NAME = "NAME";
    public static final String USERNAME = "USERNAME";
    public static final String PHOTO = "PHOTO";


    public ProfileManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MOD);
        editor = sharedPreferences.edit();
    }

    public void createData( String name, String username,  String photo) {
        editor.putString(NAME, name);
        editor.putString(USERNAME, username);
        editor.putString(PHOTO, photo);
        editor.apply();
    }


    public HashMap<String, String> getUserData(){
        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME,null));
        user.put(USERNAME, sharedPreferences.getString(USERNAME, null));
        user.put(PHOTO, sharedPreferences.getString(PHOTO, null));

        return user;
    }
}
