package com.utility.hapdelvendor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.Common;
import com.utility.hapdelvendor.Utils.LocalStorage;

public class MainActivity extends AppCompatActivity {

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
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

}

