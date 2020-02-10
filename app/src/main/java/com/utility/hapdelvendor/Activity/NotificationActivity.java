package com.utility.hapdelvendor.Activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.utility.hapdelvendor.Adapter.NotificationAdapter;
import com.utility.hapdelvendor.Model.NotificationModel.Datum;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Service.ParentNotificationService;
import com.utility.hapdelvendor.Utils.BottomNavigation;
import com.utility.hapdelvendor.Utils.Common;
import com.utility.hapdelvendor.Utils.LocalStorage;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.utility.hapdelvendor.Utils.Common.getApiInstance;
import static com.utility.hapdelvendor.Utils.Common.getCurrentUser;


public class NotificationActivity extends AppCompatActivity {

    private Toolbar tl;

    private TextView error_msg;
    private RelativeLayout profile_layout;
    private RelativeLayout error_msg_layout;
    private LottieAnimationView error_image;
    private Button slider_one_btn,slider_two_btn;
    private RelativeLayout notify_container_layout;
    private RelativeLayout log_btn_layout;
    private static final String TAG = "NotificationActivity";
    private NotificationAdapter notificationAdapter;

    private RecyclerView user_notification_view;
    private LinearLayoutManager layoutManager;

    private int scrolledOutItems, currentItems, totalItems;

    private ArrayList<com.utility.hapdelvendor.Model.NotificationModel.Datum> total_notification_list;
    private boolean firstLoad = false;
    private boolean isScrolling;
    private AHBottomNavigation bottomNavigation;
    private int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        tl = findViewById(R.id.toolbar);

        setSupportActionBar(tl);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            Common.setStatusColor(NotificationActivity.this, R.color.colorPrimaryDark);
        }



        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        BottomNavigation.initializeBottomNavigation(bottomNavigation, NotificationActivity.this);
        bottomNavigation.setCurrentItem(3, false);

        setInitialNotification(bottomNavigation);

        //Error Msg Initialization
        error_msg_layout = findViewById(R.id.error_layout);
        error_msg = findViewById(R.id.error_msg);
        error_image = findViewById(R.id.error_image);
        error_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_image.playAnimation();
            }
        });
        log_btn_layout = findViewById(R.id.log_btn_layout);

        notify_container_layout = findViewById(R.id.notify_container_layout);

        slider_one_btn = findViewById(R.id.slider_btn_one);
        slider_two_btn = findViewById(R.id.slider_btn_two);

        if(getCurrentUser()==null || getCurrentUser().getId()==null){
            showErrorMessage("Please Login to see your profile", "login");
            return;
        } else {
            hideErrorMessage();
            notify_container_layout.setVisibility(View.VISIBLE);
        }



        user_notification_view = findViewById(R.id.user_notification_view);
        layoutManager = new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false);
        user_notification_view.setLayoutManager(layoutManager);
        notificationAdapter = new NotificationAdapter(NotificationActivity.this, new ArrayList<Datum>());
        user_notification_view.setAdapter(notificationAdapter);


        user_notification_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "onScrollStateChanged: "+newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled: "+dy);
                currentItems=layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrolledOutItems = layoutManager.findFirstVisibleItemPosition();
                Log.d(TAG, "onScrolled: "+isScrolling+" "+totalItems+" "+scrolledOutItems+" "+currentItems);
                if(!isScrolling && totalItems == scrolledOutItems+currentItems){
                    //Since we cannot assign a variable to final int i
                    Log.d(TAG, "onScrolled: if "+isScrolling+" "+totalItems+" "+scrolledOutItems+" "+currentItems);

                    fetchNotifications(++i);
                }else {
                    Log.d(TAG, "onScrolled: else "+isScrolling+" "+totalItems+" "+scrolledOutItems+" "+currentItems);
                }

            }
        });


        user_notification_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "onScrollStateChanged: "+newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled: "+dy);
                currentItems=layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrolledOutItems = layoutManager.findFirstVisibleItemPosition();
                Log.d(TAG, "onScrolled: "+isScrolling+" "+totalItems+" "+scrolledOutItems+" "+currentItems);
                if(!isScrolling && totalItems == scrolledOutItems+currentItems){
                    //Since we cannot assign a variable to final int i
                    Log.d(TAG, "onScrolled: if "+isScrolling+" "+totalItems+" "+scrolledOutItems+" "+currentItems);

                    fetchNotifications(++i);
                }else {
                    Log.d(TAG, "onScrolled: else "+isScrolling+" "+totalItems+" "+scrolledOutItems+" "+currentItems);
                }

            }
        });
    }


    public static void setInitialNotification(AHBottomNavigation bottomNavigation) {
        Log.d(TAG, "setInitialNotification: "+LocalStorage.getRideNotificationCount()+ ", "+LocalStorage.getNotificationCount());
        bottomNavigation.setNotification(LocalStorage.getNotificationCount(), 3);
        bottomNavigation.setNotification(LocalStorage.getRideNotificationCount(), 1);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getCurrentUser()==null){
            showErrorMessage("Kindly login to view your orders", "login");
            return;
        }

        i = 1;
        total_notification_list = new ArrayList<>();

        fetchNotifications(i);
    }

    private void fetchNotifications(int page) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        final ProgressDialog progressDialog = new ProgressDialog(NotificationActivity.this, R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading notification...");
        progressDialog.setCancelable(false);
        if(!((Activity)NotificationActivity.this).isFinishing()){
            progressDialog.show();
        }
        firstLoad = i == 1 ? true:false;

        Call<com.utility.hapdelvendor.Model.NotificationModel.NotificationModel> notificationModelCall = getApiInstance().getNotifications(getCurrentUser().getId(), getCurrentUser().getAccessToken(), page+"");
        notificationModelCall.enqueue(new Callback<com.utility.hapdelvendor.Model.NotificationModel.NotificationModel>() {
            @Override
            public void onResponse(Call<com.utility.hapdelvendor.Model.NotificationModel.NotificationModel> call, Response<com.utility.hapdelvendor.Model.NotificationModel.NotificationModel> response) {
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    Toast.makeText(NotificationActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                com.utility.hapdelvendor.Model.NotificationModel.NotificationModel notificationModel = null;
                try {
                    notificationModel = response.body();
                } catch (Exception e) {
                    Toast.makeText(NotificationActivity.this, "Error in response", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d(TAG, "onResponse: "+notificationModel.getData().size());

                if(notificationModel!=null){
                    if(notificationModel.getData()!= null && notificationModel.getData().size()>0){

                        //Clearing all notification once we fetch them
                        bottomNavigation.setNotification("", 3);
                        ParentNotificationService.general_notification_count = 0;
                        LocalStorage.setNotificationCount(0);

                        notificationManager.cancelAll();

                        hideErrorMessage();
                        firstLoad = false;
                        isScrolling = false;
                        total_notification_list.addAll(notificationModel.getData());
                        notificationAdapter.updateItems(total_notification_list);
                    } else {
                        isScrolling = true;
                        if(firstLoad){
                            showErrorMessage("Caught Up!...No new notifictions", "");
                        }
                    }

                } else {
                    Toast.makeText(NotificationActivity.this, "Invalid Response from Server", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<com.utility.hapdelvendor.Model.NotificationModel.NotificationModel> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
                progressDialog.dismiss();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(NotificationActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    private void showErrorMessage(String message, String warning_type) {
        error_msg_layout.setVisibility(View.VISIBLE);
        error_msg.setText(message);
        notify_container_layout.setVisibility(View.GONE);

        if(warning_type.equalsIgnoreCase("login")){
            log_btn_layout.setVisibility(View.VISIBLE);
            slider_two_btn.setText("Login Now");

            slider_two_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NotificationActivity.this, SignInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });

        } else {
            log_btn_layout.setVisibility(View.GONE);
        }

    }

    private void hideErrorMessage() {
        error_msg_layout.setVisibility(View.GONE);
        notify_container_layout.setVisibility(View.VISIBLE);
    }

}

