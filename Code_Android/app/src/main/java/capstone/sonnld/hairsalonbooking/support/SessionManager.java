package capstone.sonnld.hairsalonbooking.support;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import capstone.sonnld.hairsalonbooking.LoginActivity;
import capstone.sonnld.hairsalonbooking.MainActivity;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private int PRIVATE_MODE = 0;

    private final String PREF_NAME = "LOGIN";
    private final String LOGIN = "IS_LOGIN";
    private final String USERNAME = "USERNAME";

    private int userID;


    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();

    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void setEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getPRIVATE_MODE() {
        return PRIVATE_MODE;
    }

    public  String getPrefName() {
        return PREF_NAME;
    }

    public  String getLOGIN() {
        return LOGIN;
    }

    public  String getUSERNAME() {
        return USERNAME;
    }

    public void createSession(String username, int userID) {
        editor.putBoolean(LOGIN, true);
        editor.putString(USERNAME, username);
        editor.putInt("userId", userID);
        editor.apply();


    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }



    public HashMap<String, String> getUserDetail() {

        HashMap<String, String> user = new HashMap<>();
        user.put(USERNAME, sharedPreferences.getString(USERNAME, null));
        user.put("userId", String.valueOf(sharedPreferences.getInt("userId", 0)));
        return user;


    }


    public void logout() {
        editor.clear();
        editor.commit();


    }
}
