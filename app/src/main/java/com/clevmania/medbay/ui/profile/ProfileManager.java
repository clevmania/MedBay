package com.clevmania.medbay.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by grandilo-lawrence on 4/17/18.
 */

public class ProfileManager {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    public ProfileManager(Context context){
        this.context = context;
        preferences = context.getSharedPreferences("UserProfileDetails",Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setUserDetails(String name, String mail,String phone, String img){
        editor.putString("userName", name);
        editor.putString("userMail",mail);
        editor.putString("userMobile", phone);
        editor.putString("userImg",img);
        editor.commit();
    }

    public String getUserName(){
        return preferences.getString("userName",null);
    }

    public String getUserMail(){
        return preferences.getString("userMail",null);
    }

    public String getUserMobile(){
        return preferences.getString("userMobile",null);
    }

    public String getUserImg(){
        return preferences.getString("userImg",null);
    }
}
