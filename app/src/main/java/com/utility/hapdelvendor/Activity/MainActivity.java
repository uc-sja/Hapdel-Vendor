package com.utility.hapdelvendor.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.Common;
import com.utility.hapdelvendor.Utils.LocalStorage;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryGreen));
            Common.setStatusColor(MainActivity.this, R.color.colorPrimaryGreen);
        }
        //for testing
//        LocalStorage.setUser(null);
        if (LocalStorage.getUser() != null) {

            Log.d(TAG, "onCreate: "+getIntent().getStringExtra("isOrder"));
            Intent intent = null;
            if  (getIntent()!= null && getIntent().getStringExtra("isOrder") != null ){
                if(getIntent().getStringExtra("isOrder").equalsIgnoreCase("y")){
                    intent = new Intent(MainActivity.this, OrderActivity.class);
                } else {
                    intent = new Intent(MainActivity.this, NotificationActivity.class);
                }
             } else {
                intent = new Intent(MainActivity.this, HomeActivity.class);
            }
            startActivity(intent);
            finish();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

}

