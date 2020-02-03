package com.utility.hapdelvendor.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.utility.hapdelvendor.Activity.HomeActivity;
import com.utility.hapdelvendor.Activity.NotificationActivity;
import com.utility.hapdelvendor.Activity.OrderActivity;
import com.utility.hapdelvendor.Activity.ProfileActivity;
import com.utility.hapdelvendor.R;

public class BottomNavigation extends AppCompatActivity {
    private static final String TAG = "BottomNavigation";

    public static void initializeBottomNavigation(AHBottomNavigation bottomNavigation, final Context context){
        // Create items
        AHBottomNavigationItem home_item = new AHBottomNavigationItem(R.string.tab_1, R.drawable.ic_dashboard_home, R.color.lighterGray);
        AHBottomNavigationItem order_item = new AHBottomNavigationItem(R.string.tab_2, R.drawable.ic_shopping_bag, R.color.lighterGray);
        AHBottomNavigationItem profile_item = new AHBottomNavigationItem(R.string.tab_3, R.drawable.ic_profile, R.color.lighterGray);
        AHBottomNavigationItem task_item = new AHBottomNavigationItem(R.string.tab_4, R.drawable.ic_lace, R.color.lighterGray);

        bottomNavigation.addItem(home_item);
        bottomNavigation.addItem(order_item);
        bottomNavigation.addItem(profile_item);
        bottomNavigation.addItem(task_item);

        // Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));
        // Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

        // Change colors
        bottomNavigation.setAccentColor((R.color.colorBlack));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setColoredModeColors(context.getResources().getColor(R.color.colorPrimary), context.getResources().getColor(R.color.colorPrimaryGreen));
        bottomNavigation.setColored(true);
        //bottomNavigation.setTranslucentNavigationEnabled(true);

        // Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));

        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            Fragment fragment = null;
            Intent intent ;
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                Log.d(TAG, "onTabSelected: " + position + " " + wasSelected+ context.getClass().getSimpleName());
                if(wasSelected){
                    return false;
                }
                switch (position){
                    case 0:
                        // TODO
                        intent = new Intent(context, HomeActivity.class);
//                        fragment = HomeFragment.newInstance("Home", "'Fragment");
                        break;
                    case 1:
                        // TODO
                        intent = new Intent(context, OrderActivity.class);
//                        fragment = OrdersFragment.newInstance("Orders", "'Fragment");
                        break;
                    case 2:
                        // TODO
                        intent = new Intent(context, ProfileActivity.class);
//                        fragment = ProfileFragment.newInstance("User", "'Fragment");
                        break;
                    case 3:
                        intent = new Intent(context, NotificationActivity.class);
//                        fragment = TasksFragment.newInstance("Tasks", "Fragment");
                        // popupMenuExample(bottomNavigation.getViewAtPosition(3));
                        break;

                }

                if(intent != null){
                    if (context.getClass().getSimpleName().trim().equalsIgnoreCase("HomeActivity")) {
                        ((Activity) context).startActivity(intent);
                    } else {
                        Log.d(TAG, "onTabSelected:  context is not home activity");
                        ((Activity) context).startActivity(intent);
                        ((Activity) context).finish();
                    }

                } else {
                    Log.d(TAG, "onTabSelected:  is");
                    ((Activity)context).startActivity(intent);
                    ((Activity)context).finish();
                }
                return false;
                // Do something cool  ...
            }
        });

//        bottomNavigation.setCurrentItem(AHBottomNavigation.CURRENT_ITEM_NONE);


    }
}
