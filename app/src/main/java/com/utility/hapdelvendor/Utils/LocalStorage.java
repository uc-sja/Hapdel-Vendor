package com.utility.hapdelvendor.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.utility.hapdelvendor.Activity.BaseApp;
import com.utility.hapdelvendor.Model.LoginModel.UserModel;
import com.utility.hapdelvendor.Model.ProducModel.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.utility.hapdelvendor.Utils.Common.getCurrentUser;


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


    public static void addToCart(Product product) {
        ArrayList<Product> cartList = getCart();
        cartList.add(product);
        Log.d(TAG, "addToCart: "+cartList.size());
        storeString("cartItems"+getCurrentUser().getId(), new Gson().toJson(cartList));
    }

    public static void removeFromCart(Product product, boolean isAll){
        ArrayList<Product> cartList = getCart();
        for(Product product1: cartList){
            Log.d(TAG, "removeFromCart: product loop "+ product.getProductName());
        }
        Log.d(TAG, "removeFromCart: "+Boolean.valueOf(product==null)+"  "+product.getProductName());
        Log.d(TAG, "removeFromCart: "+cartList.size());
        if(isAll){
            Log.d(TAG, "removeFromCart: all products");
            while (cartList.contains(product)){
                cartList.remove(product);
            }
        } else {
            Iterator<Product> it = cartList.iterator();
            while (it.hasNext()) {
                Product p = it.next();
                if (p.getPid().equals(product.getPid())) {
                    it.remove();
                    break;
                }
            }
        }
        storeString("cartItems"+ getCurrentUser().getId(), new Gson().toJson(cartList));
    }

    public static int getCartCount(Product p){
        ArrayList<Product> cartList = getCart();
        Log.d(TAG, "getCartCount: "+cartList.size());
        int count = 0;
        Iterator<Product> productIterator = cartList.iterator();
        while (productIterator.hasNext()){
            Product product = productIterator.next();
            if(p.getPid().equals(product.getPid())){
                count++;
            }
        }
        return count;
    }

    public static ArrayList<Product> getCart() {
        if(getCurrentUser()==null || getString("cartItems"+ getCurrentUser().getId()) == null ){
            return new ArrayList<Product>();
        }
        Type type = new TypeToken<ArrayList<Product>>(){}.getType();
        return new Gson().fromJson(getString("cartItems"+ getCurrentUser().getId()), type);
    }

    public static void setCart(List<Product> productArrayList) {
        if(productArrayList == null){
            storeString("cartItems"+ getCurrentUser().getId(), new Gson().toJson(new ArrayList<Product>()));
        }
        storeString("cartItems"+ getCurrentUser().getId(), new Gson().toJson(productArrayList));
    }

    public static void clearCart() {
        storeString("cartItems"+ getCurrentUser().getId(), new Gson().toJson(new ArrayList<Product>()));
    }
}
