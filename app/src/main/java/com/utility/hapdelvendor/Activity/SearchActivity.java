package com.utility.hapdelvendor.Activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.utility.hapdelvendor.Adapter.ProductsAdapter;
import com.utility.hapdelvendor.Interfaces.ResponseResult;
import com.utility.hapdelvendor.Model.ProducModel.Product;
import com.utility.hapdelvendor.Model.ResponseModel.ResponseModel;
import com.utility.hapdelvendor.Model.SearchModel.SearchResultModel;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.AutoSuggestAdapter;
import com.utility.hapdelvendor.Utils.BottomNavigation;
import com.utility.hapdelvendor.Utils.CircularTextView;
import com.utility.hapdelvendor.Utils.Common;
import com.utility.hapdelvendor.Utils.LocalStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.utility.hapdelvendor.Utils.Common.hideKeyboard;

public class SearchActivity extends AppCompatActivity {
    private static final int AUTOCOMPLETE_REQUEST_CODE = 4454;
    EditText search_bar;
    private Handler handler;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private AutoSuggestAdapter autoSuggestAdapter;
    private static final String TAG = "SearchActivity";
    private ProductsAdapter productAdapter;


    private ShimmerRecyclerView shimmerRecycler;
    private RecyclerView products_search_view;

    //Slidingpanel
    private SlidingUpPanelLayout sliding_layout;
    private ImageView slider_img;
    private Button slider_one_btn, slider_two_btn;
    private TextView slider_msg;


    //SnackBar
    private Snackbar snackbar;
    private Button snack_btn;
    private TextView textViewTop;
    private RelativeLayout containerLayout;

    //Cart
    private ImageView cart_icon;
    private CircularTextView cart_badge;



    //Error Msg Layout
    private LottieAnimationView error_image1;
    private ImageView error_image2;

    private RelativeLayout error_msg_layout;
    private TextView error_msg;
    private RelativeLayout search_layout;

    private String categoryId;
    private Toolbar tl;

    //location
    private TextView location_text;
    private LinearLayout location_layout;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryGreen));
            Common.setStatusColor(SearchActivity.this, R.color.colorPrimaryGreen);
        }

        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        BottomNavigation.initializeBottomNavigation(bottomNavigation, SearchActivity.this);
        bottomNavigation.setCurrentItem(-1, false);


        tl = findViewById(R.id.toolbar);

        tl.setTitle("");
        setSupportActionBar(tl);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        search_bar = findViewById(R.id.search_bar);

        containerLayout = findViewById(R.id.container_layout);

//        shimmerRecycler = (ShimmerRecyclerView) findViewById(R.id.shimmer_recycler_view);

        //Sliding Panel Intitialization
        sliding_layout = findViewById(R.id.sliding_layout);
        slider_img = findViewById(R.id.slider_img);
        slider_msg = findViewById(R.id.slider_msg);
        slider_one_btn = findViewById(R.id.slider_btn_one);
        slider_two_btn = findViewById(R.id.slider_btn_two);

        //Cart Initialization
        cart_badge = findViewById(R.id.cart_badge);
        cart_icon = findViewById(R.id.cart_icon);
        cart_badge.setSolidColor("#FF6347");
        cart_badge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, CartActivity.class);
                startActivity(intent);

            }
        });
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });


        //Error Msg Initializtion
        search_layout = findViewById(R.id.main_cat_layout);
        error_msg_layout = findViewById(R.id.error_layout);
        error_msg = findViewById(R.id.error_msg);
        error_image1 = findViewById(R.id.error_image1);
        error_image2 = findViewById(R.id.error_image2);


        products_search_view = findViewById(R.id.product_search_view);
        productAdapter = new ProductsAdapter(this, new ArrayList<Product>());
        products_search_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        products_search_view.setAdapter(productAdapter);
        products_search_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                hideKeyboard(SearchActivity.this);
            }
        });

        //initializing on start of activity
        error_image1.setVisibility(View.GONE);
        error_image2.setVisibility(View.VISIBLE);
        error_msg_layout.setVisibility(View.VISIBLE);
        error_msg.setText("Enter your search");
        search_layout.setVisibility(View.GONE);

        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("categoryId");

            search_bar.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    searchItem(search_bar.getText().toString(), categoryId);

                }
            });

        }


    }

    private void searchItem(final String keyword, String categoryId) {
        Call<SearchResultModel> searchResultModelCall = Common.getApiInstance().searchItem(keyword, Common.currentLat, Common.currentLong, categoryId);
        hideErrorMessage();
//        shimmerRecycler.showShimmerAdapter();
        searchResultModelCall.enqueue(new Callback<SearchResultModel>() {
            @Override
            public void onResponse(Call<SearchResultModel> call, Response<SearchResultModel> response) {
//                shimmerRecycler.hideShimmerAdapter();
                if(!response.isSuccessful()){
                    Toast.makeText(SearchActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: fail "+response.code());
                    return;
                }

                Log.d(TAG, "onResponse: success"+response.code()+response.body());
                if(response.body()!=null ){
                    SearchResultModel searchResultModel = null;
                    try {
                        searchResultModel = response.body();
                    } catch (Exception e) {
                        Toast.makeText(SearchActivity.this, "Error in response", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String content="";
                    Log.d(TAG, "onResponse: response msg"+response.body().getResult()+"  msg  ");
                    if (searchResultModel.getResult().equals("success")){ //very important conditon
                        Log.d(TAG, "onResponse: success");
                        if(searchResultModel.getData()!=null && searchResultModel.getData().size()>0){
                            hideErrorMessage();
                            productAdapter.updateItems(searchResultModel.getData());
                        } else {
                            showErrorMessage("No Search Results found for keyword "+"\""+keyword+"\"");
//                            Toast.makeText(SearchActivity.this, "No Search Results", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        content+= searchResultModel.getMsg();
                        Toast.makeText(SearchActivity.this, content, Toast.LENGTH_SHORT).show();
                    }

                    Log.d(TAG, "onResponse: search item res"+content);
                } else {
                    Toast.makeText(SearchActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchResultModel> call, Throwable t) {
//                shimmerRecycler.hideShimmerAdapter();
                Toast.makeText(SearchActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void showErrorMessage(String s) {
        error_image1.setVisibility(View.VISIBLE);
        error_image2.setVisibility(View.GONE);
        error_msg_layout.setVisibility(View.VISIBLE);
        error_msg.setText(s);
        search_layout.setVisibility(View.GONE);
    }

    private void hideErrorMessage() {
        error_msg_layout.setVisibility(View.GONE);
        search_layout.setVisibility(View.VISIBLE);
    }

    public void showSliderLayout(String msg, boolean showBtns, String btn_function, final ResponseResult cleartCartResponseResult){
        hideKeyboard(this);
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
                    startActivity(new Intent(SearchActivity.this, SignUpActivity.class));
                }
            });

            slider_two_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SearchActivity.this, SignInActivity.class));

                }
            });

        } else if(btn_function.trim().equalsIgnoreCase("clear")){
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


    @Override
    public void onBackPressed() {
        if(sliding_layout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)){
            sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return;
        }
        super.onBackPressed();
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

}

