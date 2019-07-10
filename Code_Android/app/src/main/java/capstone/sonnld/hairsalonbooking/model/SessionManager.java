package capstone.sonnld.hairsalonbooking.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import capstone.sonnld.hairsalonbooking.LoginActivity;
import capstone.sonnld.hairsalonbooking.MainActivity;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    public static final String PREF_NAME = "LOGIN";
    public static final String LOGIN = "IS_LOGIN";
    public static final String USERNAME = "USERNAME";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=sharedPreferences.edit();

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void createSession(String username){
        editor.putBoolean(LOGIN,true);
        editor.putString(USERNAME,username);
        editor.apply();


    }

    public boolean isLogin(){
        return sharedPreferences.getBoolean(LOGIN,false);
    }

    public void checkLogin(){
        if (!isLogin()){
            Intent i = new Intent(getContext(),LoginActivity.class);
            getContext().startActivity(i);

        }
    }

    public HashMap<String,String> getUserDetail(){

        HashMap<String,String> user = new HashMap<>();
        user.put(USERNAME,sharedPreferences.getString(USERNAME,null));
        return user;


    }


    public void logout(){
        editor.clear();
        editor.commit();






    }
}
