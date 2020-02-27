package com.utility.hapdelvendor.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.kingfisher.easyviewindicator.AnyViewIndicator;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.utility.hapdelvendor.Adapter.MainCatAdapter;
import com.utility.hapdelvendor.Adapter.RecentOrderAdapter;
import com.utility.hapdelvendor.BottomSheetFragment;
import com.utility.hapdelvendor.Interfaces.ResponseResult;
import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.ParentCategoryModel;
import com.utility.hapdelvendor.Model.RecentOrderModel.Datum;
import com.utility.hapdelvendor.Model.RecentOrderModel.RecentOrderModel;
import com.utility.hapdelvendor.Model.ResponseModel.ResponseModel;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Service.ParentNotificationService;
import com.utility.hapdelvendor.Utils.BottomNavigation;
import com.utility.hapdelvendor.Utils.CircularTextView;
import com.utility.hapdelvendor.Utils.Common;
import com.utility.hapdelvendor.Utils.LocalStorage;

import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import me.relex.circleindicator.CircleIndicator2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.utility.hapdelvendor.Utils.Common.getApiInstance;
import static com.utility.hapdelvendor.Utils.Common.getCurrentUser;
import static com.utility.hapdelvendor.Utils.Common.hideKeyboard;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private Toolbar tl;

    RecyclerView main_category_view;
    private MainCatAdapter main_cat_adapter;
    private RecyclerView banner_recycler, banner_recycler2;

    private CircleIndicator2 indicator1, indicator2;
    private RecyclerView deal_recycler;
    private RecyclerView recentOrderView;
    private ShimmerRecyclerView shimmerRecycler;
    private RelativeLayout error_msg_layout;
    private TextView error_msg;
    private RelativeLayout main_cat_layout;
    private RelativeLayout containerLayout, slider_layout;
    private ImageView slider_img;
    private Button slider_one_btn, slider_two_btn;
    TextView slider_msg;

    ImageView cart_icon;
    private CircularTextView cart_badge;

    private RecyclerView category_banner_view;

    private LinearLayout slider_banner_layout;
    private LinearLayout slider_banner_layout2;
    private SlidingUpPanelLayout sliding_layout;
    private Dialog dialog;

    private int backPressCount = 0;
    AppCompatAutoCompleteTextView search_bar;
    private Handler handler;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private com.utility.hapdelvendor.Utils.AutoSuggestAdapter autoSuggestAdapter;
    private NestedScrollView nested_scroll_view;
    private int i = 1;
    private int scrolledOutItems, currentItems, totalItems;
    private LinearLayoutManager layoutManager;
    private boolean isScrolling;
    private boolean firstLoad;
    private FusedLocationProviderClient fusedLocationClient;
    private AHBottomNavigation bottomNavigation;
    private boolean permissionDenied = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    public BottomSheetFragment bottomSheetFragment;
    private ArrayList<Datum> total_banner_items;
    private RecentOrderAdapter recentOrderAdapter;
    private LinearLayoutManager linearLayoutManager;
    private AnyViewIndicator circleIndicator;


    private boolean myReceiverIsRegistered = false;
    private static BroadcastReceiver mMessageReceiver = null;
    private static boolean firstConnect = true;
    private static boolean dialogActive;
    private boolean isWindowActive;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            Common.setStatusColor(HomeActivity.this, R.color.colorPrimaryGreen);
        }

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        BottomNavigation.initializeBottomNavigation(bottomNavigation, HomeActivity.this);
        bottomNavigation.setCurrentItem(0, false);
        tl = findViewById(R.id.toolbar);
        tl.setTitle("");
        setSupportActionBar(tl);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (!Places.isInitialized()) {
            Places.initialize(HomeActivity.this, getString(R.string.place_api_key));
        }
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(HomeActivity.this);

//        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        main_category_view = findViewById(R.id.main_category_view);
        main_cat_adapter = new MainCatAdapter(HomeActivity.this, new ArrayList<com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum>(), true, true);
        main_category_view.setLayoutManager(new GridLayoutManager(HomeActivity.this, 3));
        main_category_view.setAdapter(main_cat_adapter);

        slider_layout = findViewById(R.id.slider_layout);
        slider_img = findViewById(R.id.slider_img);
        slider_msg = findViewById(R.id.slider_msg);
        sliding_layout = findViewById(R.id.sliding_layout);

        slider_one_btn = findViewById(R.id.slider_btn_one);
        slider_two_btn = findViewById(R.id.slider_btn_two);


//        //making recyclerview sticky
//        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
//        pagerSnapHelper.attachToRecyclerView(banner_recycler);
//        indicator1 = findViewById(R.id.banner_indicator1);
//        indicator1.attachToRecyclerView(banner_recycler, pagerSnapHelper);
//        bannerAdapter.registerAdapterDataObserver(indicator1.getAdapterDataObserver());
//
//        //making recyclerview2 sticky
//        PagerSnapHelper pagerSnapHelper2 = new PagerSnapHelper();
//        pagerSnapHelper2.attachToRecyclerView(banner_recycler2);
//        indicator2 = findViewById(R.id.banner_indicator2);
//        indicator2.attachToRecyclerView(banner_recycler2, pagerSnapHelper);
//        bannerAdapter2.registerAdapterDataObserver(indicator2.getAdapterDataObserver());


        recentOrderView = findViewById(R.id.recent_order_recycler);

//        circleIndicator = findViewById(R.id.recent_order_indicator);
        linearLayoutManager = new  LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false);
        recentOrderView.setLayoutManager(linearLayoutManager);


        recentOrderAdapter = new RecentOrderAdapter(HomeActivity.this, new ArrayList<Datum>());
        recentOrderView.setAdapter(recentOrderAdapter);
        shimmerRecycler = (ShimmerRecyclerView) findViewById(R.id.shimmer_recycler_view);

//        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
//        recentOrderView.setOnFlingListener(null);
//        pagerSnapHelper.attachToRecyclerView(recentOrderView);

        //        fetchMainCategories();


        fetchRecentOrder();

        error_msg_layout = findViewById(R.id.error_layout);
        error_msg = findViewById(R.id.error_msg);
        main_cat_layout = findViewById(R.id.main_cat_layout);

        nested_scroll_view = findViewById(R.id.nested_scroll_view);
        nested_scroll_view.setNestedScrollingEnabled(false);


//        Log.d(TAG, "onCreate: "+nested_scroll_view.getChildAt(nested_scroll_view.getChildCount()-1));
//        Log.d(TAG, "onScrollChange:  e"+error_msg_layout.getId()+"  "+main_cat_layout.getId());

        search_bar = findViewById(R.id.search_bar);
        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AllProducts.class);
                intent.putExtra("category", "");
                startActivity(intent);
            }
        });

        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "aed broadcast ");
                final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetInfo != null) {

                    Log.d(TAG, "onReceive: active info");


                    //using first connect flag because we want to receive the broadcast only once
                    //and in previous versions of android onreceive is called multiple number of times simultaneously
                    //but we need to reset firstConnect to true after a while also so that we receive broadcast updtes in future

                    if(firstConnect) {
                        firstConnect = false;

//                        final Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                // Do something after 5s = 5000ms
//                                firstConnect = true;
//                            }
//                        }, 1500);

                        String isOrder = intent.getExtras().getString("isOrder");
                        //                current_job_id = job_id;
                        //                LocalStorage.setCurrentJobId(getCurrentUser().getId(), job_id);

                        String body = intent.getExtras().getString("body");
                        String title = intent.getExtras().getString("title");

//                        Log.d(TAG, "onReceive jobid " + job_id +  "price"+ price +" distance" +distance+"  "+isOnRide+"  "+dialogActive);

                        //we are null checking price and distance too below because this is how we diferenciate the notification of type general
                        //or notification type ride
                        if (isOrder != null && isOrder.equalsIgnoreCase("y") && body!=null && title!=null  && !dialogActive) {
                            // Customize notification (title, background, typeface)
                            bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));

                            // Add or remove notification for each item
                            bottomNavigation.setNotification(ParentNotificationService.order_notification_count, 1);

                            showRideDialog(HomeActivity.this, title, body   );
                        } else if (isOrder != null && isOrder.equalsIgnoreCase("y") && body!=null && title!=null) {
                            Log.d(TAG, "onReceive: on ride notification ");
                            bottomNavigation.setNotification(ParentNotificationService.order_notification_count, 1);
                        } else {
                            Log.d(TAG, "onReceive: general notification ");
                            //GENERAL NOTIFICATION
                            bottomNavigation.setNotification(ParentNotificationService.general_notification_count, 3);
                        }

                    } else {
                        firstConnect = true;
                    }
                }

            }

            ;
        };



        if (LocalStorage.isNotificationRereshed()) {
            Log.d(TAG, "onCreate: isNotificationRereshed ");
            sendRegistrationToServer(LocalStorage.getNotificationToken());
        } else {
            Log.d(TAG, "onCreate: isNotificationRereshed " + LocalStorage.isNotificationRereshed());
        }


    }


    private void
    sendRegistrationToServer(String s) {
        Log.d(TAG, "login: " + s);
        // TODO: Implement your own authentication logic here.

        Call<ResponseModel> loginResponseCall = getApiInstance().sendNotificationToken(getCurrentUser().getId(), getCurrentUser().getAccessToken(), s);
        loginResponseCall.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: fail " + response.code());
                    return;
                }
                Log.d(TAG, "onResponse: success" + response.code() + response.body());
                if (response.body() != null) {
                    ResponseModel responseModel = null;
                    try {
                        responseModel = response.body();
                    } catch (Exception e) {
                        Toast.makeText(HomeActivity.this, "Error in response", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String content = "";
                    Log.d(TAG, "onResponse: response msg" + response.body().getResult() + "  msg  ");
                    if (responseModel.getResult().equals("success")) { //very important conditon
                        Log.d(TAG, "onResponse: success send registration token");
                        Toast.makeText(HomeActivity.this, "Successfully created access token", Toast.LENGTH_SHORT).show();
                        LocalStorage.setIsNotificationRereshed(false);
                    } else {
                        content += responseModel.getMsg();
                        Log.d(TAG, "onResponse: invalid response" + content);
                    }
                    Log.d(TAG, "onResponse: login res" + content);
                } else {
                    Log.d(TAG, "onResponse: Invalid response from server");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());

            }
        });
    }



    private void showRideDialog(HomeActivity homeActivity, String body, String title) {
        Log.d(TAG, "showRideDialog: is called");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Dialog dialog = new Dialog(new ContextThemeWrapper(HomeActivity.this, R.style.DialogSlideAnim));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setTitle("Hapdel");
        //before inflating the custom alert dialog layout, we will getItemType the current activity viewgroup

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.my_ride_dialog, null, false);
        TextView dialog_subt = dialogView.findViewById(R.id.dialog_subtitle);
        TextView dialog_details = dialogView.findViewById(R.id.dialog_details);

        TextView distance_text = dialogView.findViewById(R.id.distance);
        TextView price_text = dialogView.findViewById(R.id.price);

        dialog_details.setMovementMethod(new ScrollingMovementMethod());
        Button buttonOk = dialogView.findViewById(R.id.buttonOk);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
        dialog_subt.setText(title);
        dialog_details.setText(body);

//
//        price_text.setText(getString(R.string.rupee_icon) + price);
//        distance_text.setText(distance);
        dialog.setContentView(dialogView);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogActive = false;
                notificationManager.cancelAll();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                notificationManager.cancelAll();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialogActive = false;
            }
        });

        if (isWindowActive) {
            dialog.show();
            dialogActive = true;
        } else {
            Log.d(TAG, "showRideDialog: activity is finishing");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
    }



    @Override
    protected void onPause() {
        super.onPause();
        isWindowActive = false;
        Log.d(TAG, "state onPause: ");
        if (myReceiverIsRegistered) {
            unregisterReceiver(mMessageReceiver);
            myReceiverIsRegistered = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        backPressCount = 0;
        fetchHomePage();

        isWindowActive = true;
        Log.d(TAG, "state onResume: ");
        if (!myReceiverIsRegistered) {
            dialogActive = false;
            LocalBroadcastManager.getInstance(HomeActivity.this).registerReceiver(mMessageReceiver, new IntentFilter("MyData"));
        }
    }

    private void fetchHomePage() {
        Log.d(TAG, "fetchHomePage: ");
        fetchMainCategories();
//        fetchRecentOrder();
        i = 1;
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (++backPressCount <= 1) {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            return;
        } else {
            backPressCount = 0;
            super.onBackPressed();
        }
    }

    private void fetchRecentOrder() {
        final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this, R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching Offers...");
        progressDialog.setCancelable(false);
        if (!((Activity) HomeActivity.this).isFinishing()) {
            progressDialog.show();
        }

        //initially
        Call<RecentOrderModel> loginResponseCall = getApiInstance().fetchRecentOrders(getCurrentUser().getId(), getCurrentUser().getAccessToken(), null);
        loginResponseCall.enqueue(new Callback<RecentOrderModel>() {
            @Override
            public void onResponse(Call<RecentOrderModel> call, Response<RecentOrderModel> response) {
                progressDialog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(HomeActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: fail " + response.code());
                    return;
                }

                Log.d(TAG, "onResponse: success" + response.code() + response.body());
                if (response.body() != null) {
                    RecentOrderModel recentOrderModel = null;
                    try {
                        recentOrderModel = response.body();
                    } catch (Exception e) {
                        Toast.makeText(HomeActivity.this, "Error in response", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String content = "";
                    content += recentOrderModel.getMsg();

                    Log.d(TAG, "onResponse: response msg" + response.body().getResult() + "  msg  ");
                    if (recentOrderModel.getResult().equalsIgnoreCase("success")) { //very important conditon
                        final List<Datum> bannerList = new ArrayList<>();

                        if (recentOrderModel.getData() != null && recentOrderModel.getData().size() > 0) {

                            Log.d(TAG, "onResponse: banner size " + recentOrderModel.getData().size());

                            recentOrderAdapter.updateItems(recentOrderModel.getData());
                        }

                    } else {
                        Toast.makeText(HomeActivity.this, content, Toast.LENGTH_SHORT).show();
                    }

                    Log.d(TAG, "onResponse: fetch banner images res " + content);
                } else {
                    Toast.makeText(HomeActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecentOrderModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(HomeActivity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showEmptyDialog() {
        dialog = new Dialog(new ContextThemeWrapper(this, R.style.DialogSlideAnim));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setTitle("Hapdel");
        //before inflating the custom alert dialog layout, we will getItemType the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.my_dialog, viewGroup, false);

        dialog.setContentView(dialogView);
        dialog.show();

    }


    private void fetchMainCategories() {
        Log.d(TAG, "fetchMainCategories: ");
        Call<ParentCategoryModel> categoryModel = getApiInstance().fetchParentCategory(getCurrentUser().getId(), getCurrentUser().getAccessToken());
        shimmerRecycler.showShimmerAdapter();
        categoryModel.enqueue(new Callback<ParentCategoryModel>() {
            @Override
            public void onResponse(Call<ParentCategoryModel> call, Response<ParentCategoryModel> response) {
                shimmerRecycler.hideShimmerAdapter();
                if (!response.isSuccessful()) {
                    Toast.makeText(HomeActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: " + response.code() + "  mes " + response.message());
                    return;
                }
                if (response.body() != null) {
                    ParentCategoryModel parentCategoryModel = null;


                    try {
                        parentCategoryModel = response.body();
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: " + e.toString());
                        Toast.makeText(HomeActivity.this, "Error parsing response.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String content = "";
                    if (parentCategoryModel.getResult().equals("success")) {
                        Log.d(TAG, "onResponse: parentcategory fetch success");
                        if (parentCategoryModel.getData() != null && parentCategoryModel.getData().size() > 0) {
                            hideErrorMessage();
                            Log.d(TAG, "onResponse: parentcategory size " + parentCategoryModel.getData().size());
                            main_cat_adapter.updateItems(parentCategoryModel.getData());

//                            Drawable divider = ContextCompat.getDrawable(HomeActivity.this, R.drawable.line_divider_orange);
//                            Drawable hidden_divider = ContextCompat.getDrawable(HomeActivity.this, R.drawable.hidden_divider);
//                            main_category_view.addItemDecoration(new GridDividerItemDecoration(hidden_divider, divider, 1));
//                            main_category_view.addItemDecoration(new GridDividerItemDecoration(divider, hidden_divider, 2));
                        } else {
                            showErrorMessage("There are not categories yet");
                            Log.d(TAG, "onResponse: empty result ");
                        }

                    } else {
                        showErrorMessage(parentCategoryModel.getMsg());
                        Log.d(TAG, "onResponse: " + parentCategoryModel.getMsg());
                        Toast.makeText(HomeActivity.this, "" + parentCategoryModel.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ParentCategoryModel> call, Throwable t) {
                shimmerRecycler.hideShimmerAdapter();
                showErrorMessage(t.getLocalizedMessage());
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });

    }



    private void showErrorMessage(String s) {
        main_cat_layout.setVisibility(View.GONE);
        error_msg_layout.setVisibility(View.VISIBLE);
        error_msg.setText(s);
//        sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
    }

    private void hideErrorMessage() {
        error_msg_layout.setVisibility(View.GONE);
        main_cat_layout.setVisibility(View.VISIBLE);
//        sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.d(TAG, "onSaveInstanceState: ");
    }

    public void showSliderLayout(String msg, boolean showBtns, String btn_function, final ResponseResult cleartCartResponseResult) {
        hideKeyboard(this);
        slider_msg.setText(msg);
        sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        if (!showBtns) {
            slider_one_btn.setVisibility(View.INVISIBLE);
            slider_two_btn.setVisibility(View.INVISIBLE);
        }

        if (btn_function.trim().equalsIgnoreCase("login")) {
            slider_one_btn.setText("Register");
            slider_two_btn.setText("Login Now");
            slider_one_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivity.this, SignUpActivity.class));
                }
            });

            slider_two_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivity.this, SignInActivity.class));
                }
            });

        } else if (btn_function.trim().equalsIgnoreCase("clear")) {
            slider_one_btn.setText("Cancel");
            slider_two_btn.setText("Clear");
            slider_one_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            });

            slider_two_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            });
        }
    }
}
