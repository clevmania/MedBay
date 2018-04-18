package com.clevmania.medbay.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

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

    public void setUserDetails(String name, String mail,String phone){
        editor.putString("userName", name);
        editor.putString("userMail",mail);
        editor.putString("userMobile", phone);
        editor.commit();
    }

    public void wipeUserDetails(){
        editor.putString("userName", null);
        editor.putString("userMail",null);
        editor.putString("userMobile", null);
        editor.putString("userImg",null);
        editor.commit();
    }

    public void setUid(String uid){
        editor.putString("uid",uid);
    }

    public String getUid(){
        return preferences.getString("uid",null);
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
