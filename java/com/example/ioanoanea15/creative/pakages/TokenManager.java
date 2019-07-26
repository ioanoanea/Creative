package com.example.ioanoanea15.creative.pakages;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class TokenManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;

    public TokenManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("TOKEN",0);
        editor = sharedPreferences.edit();
    }

    public void addToken(String token){
        editor.putString("token",token);
        editor.apply();
    }

    public String getToken(){
        String token = sharedPreferences.getString("token",null);

        return token;
    }
}
