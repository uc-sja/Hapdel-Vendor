package com.utility.hapdelvendor.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.utility.hapdelvendor.Adapter.VendorOrderAdapter;
import com.utility.hapdelvendor.Interfaces.ResponseResult;
import com.utility.hapdelvendor.Model.UserOrderModel.OrderModel.Datum;
import com.utility.hapdelvendor.Model.UserOrderModel.OrderModel.OrderModel;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.BottomNavigation;
import com.utility.hapdelvendor.Utils.CircularTextView;
import com.utility.hapdelvendor.Utils.Common;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.utility.hapdelvendor.Utils.Common.getApiInstance;

public class OrderActivity extends AppCompatActivity {
    private static final int AUTOCOMPLETE_REQUEST_CODE = 4144;
    ImageView cart_icon;
    private CircularTextView cart_badge;
    private Toolbar tl;
    private AppCompatAutoCompleteTextView search_bar;
    private static final String TAG = "OrderActivity";
    private RecyclerView user_order_view;
    private VendorOrderAdapter vendorOrderAdapter;
    private LinearLayoutManager layoutManager;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean firstLoad = true;
    private List<Datum> total_order_list;

    //Error Msg Layout
    private LottieAnimationView error_image;
    private RelativeLayout error_msg_layout;
    private TextView error_msg;
    private RelativeLayout open_product_layout;

    //Sliding PanelLayout
    private SlidingUpPanelLayout sliding_layout;
    private ImageView slider_img;
    private Button slider_one_btn, slider_two_btn;
    private TextView slider_msg;
    private boolean isScrolling;
    private int scrolledOutItems,currentItems,totalItems;

    //location
    TextView location_text;
    private LinearLayout location_layout;


    private int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        tl = findViewById(R.id.toolbar);

        setSupportActionBar(tl);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            Common.setStatusColor(OrderActivity.this, R.color.colorPrimaryDark);
        }


        location_text = findViewById(R.id.location_text);
        location_layout = findViewById(R.id.location_layout);


        sliding_layout = findViewById(R.id.sliding_layout);
        slider_img = findViewById(R.id.slider_img);
        slider_msg = findViewById(R.id.slider_msg);

        slider_one_btn = findViewById(R.id.slider_btn_one);
        slider_two_btn = findViewById(R.id.slider_btn_two);

        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        BottomNavigation.initializeBottomNavigation(bottomNavigation, OrderActivity.this);
        bottomNavigation.setCurrentItem(1, false);

//
//        if(getCurrentUser()==null || getCurrentUser().getId()==null){
//            showSliderLayout("Please login to see your orders", true, "login", null);
//            return;
//        }

        search_bar = findViewById(R.id.search_bar);
        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, SearchActivity.class);
                intent.putExtra("categoryId", "");
                startActivity(intent);
            }
        });

        user_order_view = findViewById(R.id.user_order_view);
        layoutManager = new LinearLayoutManager(OrderActivity.this, LinearLayoutManager.VERTICAL, false);
        user_order_view.setLayoutManager(layoutManager);
        vendorOrderAdapter = new VendorOrderAdapter(OrderActivity.this, new ArrayList<Datum>());
        user_order_view.setAdapter(vendorOrderAdapter);

        user_order_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                    fetchVendorOrders(++i);
                }else {
                    Log.d(TAG, "onScrolled: else "+isScrolling+" "+totalItems+" "+scrolledOutItems+" "+currentItems);
                }

            }
        });

        //Error Msg Initializtion
        open_product_layout = findViewById(R.id.view_order_layout);
        error_msg_layout = findViewById(R.id.error_layout);
        error_msg = findViewById(R.id.error_msg);
        error_image = findViewById(R.id.error_image);
        error_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_image.playAnimation();
            }
        });
    }

    private void fetchVendorOrders(final int i) {
        Log.d(TAG, "fetchVendorOrderDetails: "+i);
        firstLoad = i == 1 ? true:false;
        final ProgressDialog progressDialog = new ProgressDialog(OrderActivity.this, R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching orders...  ");
        progressDialog.setCancelable(false);
        if(!((Activity)this).isFinishing()){
            progressDialog.show();
        }

        Call<OrderModel> orderModelCall = getApiInstance().fetchVendorOrder("1", "", i+"");
        orderModelCall.enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    Toast.makeText(OrderActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: fail "+response.code());
                    return;
                }

                Log.d(TAG, "onResponse: success"+response.code()+response.body());
                if(response.body()!=null ){
                    OrderModel orderModel = null;
                    try {
                        orderModel = response.body();
                    } catch (Exception e) {
                        Toast.makeText(OrderActivity.this, "Error in response", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String content="";
                    Log.d(TAG, "onResponse: response msg"+response.body().getResult()+"  msg  ");
                    if (orderModel.getResult().equals("success")){ //very important conditon
                        if(orderModel.getData()!=null && orderModel.getData().size()>0){
                            firstLoad = false;
                            isScrolling = false;
                            total_order_list.addAll(orderModel.getData());
                            vendorOrderAdapter.updateItems(total_order_list);
                        } else {
                            isScrolling = true;
                            if(firstLoad){
                                showErrorMessage("Currently you have no orders");
                            }
                        }
                    }else{
                        content+= orderModel.getMsg();
                        Toast.makeText(OrderActivity.this, content, Toast.LENGTH_SHORT).show();
                    }

                    Log.d(TAG, "onResponse: fetch orders res "+content);
                } else {
                    Toast.makeText(OrderActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(OrderActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: OrderActivity ");
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();

//            if(getCurrentUser()==null){
//                showSliderLayout("Kindly login to view your orders", true, "login", null);
//                return;
//            }

            i = 1;
            total_order_list = new ArrayList<>();
            fetchVendorOrders(i);
    }


    private void showErrorMessage(String s) {
        error_msg_layout.setVisibility(View.VISIBLE);
        error_msg.setText(s);
        open_product_layout.setVisibility(View.GONE);
    }

    private void hideErrorMessage() {
        error_msg_layout.setVisibility(View.GONE);
        open_product_layout.setVisibility(View.VISIBLE);
    }

    public void showSliderLayout(String msg, boolean showBtns, String btn_function, final ResponseResult cleartCartResponseResult){
        Common.hideKeyboard(this);
        slider_msg.setText(msg);
        sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        if(!showBtns){
            slider_one_btn.setVisibility(View.INVISIBLE);
            slider_two_btn.setVisibility(View.INVISIBLE);
        }

        if(btn_function.trim().equalsIgnoreCase("login")){
            slider_one_btn.setText("Register");
            slider_two_btn.setText("Login Now");
            slider_one_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(OrderActivity.this, SignUpActivity.class));
                }
            });

            slider_two_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(OrderActivity.this, SignInActivity.class));

                }
            });

        }

    }
}
