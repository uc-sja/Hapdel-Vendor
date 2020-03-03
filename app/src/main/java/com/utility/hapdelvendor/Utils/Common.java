package com.utility.hapdelvendor.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.utility.hapdelvendor.Model.LoginModel.Datum;
import com.utility.hapdelvendor.Model.LoginModel.UserModel;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.RetrofitClient.HapdelApi;
import com.utility.hapdelvendor.RetrofitClient.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.text.TextUtils.isEmpty;

public class Common {

//        public static final String baseUrl = "http://stage.hapdel.in/api/v1/vendors/";

    public static final String baseUrl = "https://test.hapdel.in/api/v1/vendors/";

//    public static final String baseUrl = "https://live.hapdel.in/api/v1/vendors/";

//    public static final String baseUrl = "http://192.168.0.143/hapdel/api/v1/vendors/";

//    public static final String baseUrl = "http://192.168.43.59:8080/hapdel/api/v1/vendors/";

    private static final String TAG = "Common";
    public static boolean service_available = false;
    public static String currentCity = "";

    public static HapdelApi getApiInstance(){
        HapdelApi hapdelApi = RetrofitClient.getRetrofit().create(HapdelApi.class);
        return hapdelApi;
    }

    public static Datum getCurrentUser(){
        if(LocalStorage.getUser()!=null) {
            return LocalStorage.getUser().getData().get(0);
        } else{
            return null;
        }
    }

    public static void selectTextView(Context context, LinearLayout nick_name_option_layout, TextView selected_text_view, int selected_color, int unselect_color) {
        ArrayList<TextView> textViews = findTextViews(nick_name_option_layout, null);
        for(TextView textView: textViews){
            if(textView.getId()==selected_text_view.getId()){
                textView.setBackgroundColor(context.getResources().getColor(selected_color));
                textView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            } else {
                textView.setBackgroundColor(context.getResources().getColor(unselect_color));
                textView.setTextColor(context.getResources().getColor(R.color.medium_gray));
            }
        }
    }

    public static ArrayList<TextView> findTextViews(ViewGroup viewGroup, ArrayList<TextView> textViews){
        if(textViews == null){
            textViews = new ArrayList<>();
        }

        Log.d(TAG, "findEditableMarks: "+viewGroup.getChildCount());
        for(int i = 0; i < viewGroup.getChildCount(); i++){
            View view = viewGroup.getChildAt(i);
            if(view instanceof  ViewGroup){
                Log.d(TAG, "findTextViews: viewgroup hai"+view);
                ViewGroup viewGroup1 = (ViewGroup)view;
                findTextViews(viewGroup1, textViews);
            } else if(view instanceof TextView){
                Log.d(TAG, "findTextViews: textview hai");
                TextView textView = (TextView) view;
                Log.d(TAG, "findTextViews: "+textView.getText().toString());
                textViews.add(textView);

            }
        }

        Log.d(TAG, "findTextViews: "+textViews.size());
        return textViews;
    }

    public static ArrayList<TextInputEditText> findTextInputEditTexts(ViewGroup viewGroup, ArrayList<TextInputEditText> textInputEditTexts){
        if(textInputEditTexts == null){
            textInputEditTexts = new ArrayList<>();
        }

        Log.d(TAG, "findEditableMarks: "+viewGroup.getChildCount());
        for(int i = 0; i < viewGroup.getChildCount(); i++){
            View view = viewGroup.getChildAt(i);
            if(view instanceof  ViewGroup){
                Log.d(TAG, "findTextViews: viewgroup hai"+view);
                ViewGroup viewGroup1 = (ViewGroup)view;
                findTextInputEditTexts(viewGroup1, textInputEditTexts);
            } else if(view instanceof TextInputEditText){
                Log.d(TAG, "findTextViews: textview hai");
                TextInputEditText textInputEditText = (TextInputEditText) view;
                Log.d(TAG, "findTextViews: "+textInputEditText.getText().toString());
                textInputEditTexts.add(textInputEditText);

            }
        }
        Log.d(TAG, "findTextViews: "+textInputEditTexts.size());
        return textInputEditTexts;
    }


    public boolean setCurrentUser(UserModel userModel){
        LocalStorage.setUser(userModel);
        return true;
    }

    public static void setAnimation(Context context){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH){
            Slide slide = new Slide();
            slide.setSlideEdge(Gravity.LEFT);
            slide.setDuration(400);
            slide.setInterpolator(new DecelerateInterpolator());
            ((Activity)context).getWindow().setExitTransition(slide);
            ((Activity)context).getWindow().setEnterTransition(slide);
        }
    }



    public static void showEmptyDialog(Context context, String dialog_subtitle, String dialog_desc) {
        final Dialog dialog = new Dialog(new ContextThemeWrapper(context, R.style.DialogSlideAnim));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setTitle("Hapdel");
        //before inflating the custom alert dialog layout, we will getItemType the current activity viewgroup

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(context).inflate(R.layout.my_dialog, null, false);
        TextView dialog_subt = dialogView.findViewById(R.id.dialog_subtitle);
        TextView dialog_details = dialogView.findViewById(R.id.dialog_details);
        dialog_details.setMovementMethod(new ScrollingMovementMethod());
        Button buttonOk = dialogView.findViewById(R.id.buttonOk);
        dialog_subt.setText(dialog_subtitle);
        dialog_details.setText(isEmpty(dialog_desc)?"No Descripition Availble":dialog_desc);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(dialogView);
        dialog.show();

    }


    public static void hideKeyboardFrom(Context context) {
        View view = ((Activity)context).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        }
    }



    public static void setStatusColor(Context context, @Nullable int color){
        Window window = ((Activity)context).getWindow();
    // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

    // finally change the color
        if(color == 0){
            window.setStatusBarColor(ContextCompat.getColor(((Activity)context), R.color.colorPrimaryGreen));
        } else {
            //hard coded for now
            window.setStatusBarColor(ContextCompat.getColor(((Activity)context), R.color.colordarkbackground));
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
    }


    public static String fetchCityFromLatLng(Context context, LatLng latLng){
        String city = "";
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        Log.d(TAG, "fetchCityFromLatLng: "+latLng.latitude+"   "+latLng.longitude);
        try {
            addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                city = addresses.get(0).getLocality();

                if(city == null){
                    city = addresses.get(0).getAdminArea();
                }
                Log.d(TAG, "fetchCityFromLatLng: "+city+addresses.get(0).getAdminArea()+addresses.get(0).getFeatureName()+ new Gson().toJson(addresses.get(0)));
            }
            else {
                Log.d(TAG, "else : ");
            }
        } catch (IOException e) {
            Log.d(TAG, "onSuccess: catch"+e.toString());
            fetchCityFromLatLng(context, latLng);
        }
        return city;
    }

    public static Address fetchAddressFromLatLng(Context context, LatLng latLng){
        Address address = null;
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        Log.d(TAG, "fechAdd: "+latLng.latitude+"   "+latLng.longitude);
        try {
            addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                address = addresses.get(0);
            }
            else {
                Log.d(TAG, "else : ");
            }

            Log.d(TAG, "fetchCityFromLatLng: "+addresses.get(0).getAdminArea()+addresses.get(0).getFeatureName()+ new Gson().toJson(addresses.get(0)));

        } catch (IOException e) {
            Log.d(TAG, "onSuccess: catch"+e.toString());
            fetchAddressFromLatLng(context, latLng);
        }
        return address;
    }
}
