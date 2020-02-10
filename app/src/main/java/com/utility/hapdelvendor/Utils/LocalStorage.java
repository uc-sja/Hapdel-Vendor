package com.utility.hapdelvendor.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.utility.hapdelvendor.Activity.BaseApp;
import com.utility.hapdelvendor.Model.LoginModel.UserModel;


public class LocalStorage {
    private static final String TAG = "LocalStorage";
    public static final String MyPreferences = "mypreferences";

    //helper method to getItemType object of sharedpreferences
    public static SharedPreferences getSharedPreferences(){
        SharedPreferences sharedPreferences = BaseApp.AppContext.getSharedPreferences(MyPreferences, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static String storeString(String key, String toJson){
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString(key, toJson);
        editor.apply();
        return toJson;
    }

    public static String getString(String key){
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString(key, null);
    }
    //helper method to store string to sharedpreferences
    private static void storeBoolean(String key, Boolean b) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, b);
        editor.apply();
    }

    //helper method to fetch string from sharedpreferences
    private static boolean getBoolean(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getBoolean(key, false);
    }

    public static void setUser(UserModel userModel){
        storeString("user", new Gson().toJson(userModel));
    }

    public static UserModel getUser(){
        UserModel userModel = new Gson().fromJson(getString("user"), UserModel.class);
        return userModel;
    }


    public static void setIsNotificationRereshed(boolean b) {
        storeBoolean("isRefreshed", b);
    }

    public static Boolean isNotificationRereshed() {
        return getBoolean("isRefreshed");
    }



    public static String getNotificationToken() {
        return getString("notificationToken");
    }

    public static void setNotificationToken(String s) {
        storeString("notificationToken", s);
    }

    public static void setNotificationCount(int i) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("notificationCount", i);
        editor.apply();
    }

    public static int getNotificationCount() {
        SharedPreferences sharedPreferences = null;
        try {
            sharedPreferences = getSharedPreferences();
        } catch (Exception e) {
            Log.d(TAG, "getUser: " + e.toString());
        }
        int notificationCount = sharedPreferences.getInt("notificationCount", 0);
        return notificationCount;
    }

    public static void setRideNotificationCount(int i) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("rideNotificationCount", i);
        editor.apply();
    }

    public static int getRideNotificationCount() {
        SharedPreferences sharedPreferences = null;
        try {
            sharedPreferences = getSharedPreferences();
        } catch (Exception e) {
            Log.d(TAG, "getUser: " + e.toString());
        }
        int rideNotificationCount = sharedPreferences.getInt("rideNotificationCount", 0);
        return rideNotificationCount;
    }

}
