package com.utility.hapdelvendor.Activity;

import android.app.Dialog;
import android.content.Context;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.gson.Gson;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.utility.hapdelvendor.Adapter.OpenCatAdapter;
import com.utility.hapdelvendor.Interfaces.ResponseResult;
import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum;
import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.ParentCategoryModel;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.BottomNavigation;
import com.utility.hapdelvendor.Utils.CircularTextView;
import com.utility.hapdelvendor.Utils.Common;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.utility.hapdelvendor.Utils.Common.hideKeyboard;

public class OpenCategoryActivity extends AppCompatActivity {
    private static final int PICK_FROM_ADDRESS_REQUEST = 2133;
    Toolbar tl;
    TextView location_text;
    public RecyclerView open_category_view;
    private OpenCatAdapter open_cat_adapter;
    private LinearLayout location_layout;
    private TextView assist_text,  error_msg;
    private RelativeLayout open_category_layout;
    private RelativeLayout error_msg_layout;

    private RelativeLayout containerLayout, slider_layout;
    private ImageView slider_img;

    private Button slider_one_btn, slider_two_btn;

    private RecyclerView category_banner_view;
    private SlidingUpPanelLayout sliding_layout;

    private NestedScrollView nested_scroll_view;

    private int i = 1;
    private int scrolledOutItems,currentItems,totalItems;
    private LinearLayoutManager layoutManager;
    private boolean isScrolling;
    private boolean firstLoad;
    private static final String TAG = "OpenCategoryActivity";
    private CircularTextView cart_badge;
    private ShimmerRecyclerView shimmerRecycler;
    private Dialog dialog;
    private ImageView cart_icon;
    private TextView slider_msg;
    private Datum parentCategoryDatum;
    private AppCompatAutoCompleteTextView search_bar;
    private int AUTOCOMPLETE_REQUEST_CODE = 2515;
    private AHBottomNavigation bottomNavigation;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Common.setAnimation(this);
        setContentView(R.layout.activity_open_category);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            Common.setStatusColor(OpenCategoryActivity.this, R.color.colorPrimary);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if(!Places.isInitialized()){
            Places.initialize(OpenCategoryActivity.this, getString(R.string.place_api_key));
        }


        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        BottomNavigation.initializeBottomNavigation(bottomNavigation, OpenCategoryActivity.this);
        bottomNavigation.setCurrentItem(-1, false);
        parentCategoryDatum = new Gson().fromJson(getIntent().getStringExtra("category"), Datum.class);

        tl = findViewById(R.id.toolbar);
        tl.setTitle("");

        setSupportActionBar(tl);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //seeting up content layout and error layout
        open_category_layout = findViewById(R.id.open_category_layout);
        error_msg_layout = findViewById(R.id.error_layout);
        error_msg = findViewById(R.id.error_msg);

        nested_scroll_view = findViewById(R.id.nested_scroll_view);



        slider_layout = findViewById(R.id.slider_layout);
        slider_img = findViewById(R.id.slider_img);
        slider_msg = findViewById(R.id.slider_msg);

        sliding_layout = findViewById(R.id.sliding_layout);

        slider_one_btn = findViewById(R.id.slider_btn_one);
        slider_two_btn = findViewById(R.id.slider_btn_two);



        shimmerRecycler = (ShimmerRecyclerView) findViewById(R.id.shimmer_recycler_view);

        open_category_view = findViewById(R.id.open_category_view);
        open_cat_adapter = new OpenCatAdapter(this, new ArrayList<Datum>(), false, true);
        open_category_view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        open_category_view.setAdapter(open_cat_adapter);
//
//        location_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
//                bottomSheetFragment.show((getSupportFragmentManager()), bottomSheetFragment.getTag());
//            }
//        });

        if(parentCategoryDatum != null){
            fetchSubCategories(parentCategoryDatum);

            search_bar = findViewById(R.id.search_bar);
            search_bar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(OpenCategoryActivity.this, SearchActivity.class);
                        intent.putExtra("categoryId", parentCategoryDatum.getId());
                        startActivity(intent);
                }
            });
        }




        //        fetchBannerImages();

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public static int getDrawable(Context context, String name) {
        if (context==null) throw new AssertionError("Object cannot be null");
        if (name==null) throw new AssertionError("Object cannot be null");

        return context.getResources().getIdentifier(name,
                "drawable", context.getPackageName());
    }

    private void fetchSubCategories(Datum datum) {
        Call<ParentCategoryModel> categoryModel = Common.getApiInstance().fetchSubCategory("1", "",  datum.getId());
        shimmerRecycler.showShimmerAdapter();

        categoryModel.enqueue(new Callback<ParentCategoryModel>() {
            @Override
            public void onResponse(Call<ParentCategoryModel> call, Response<ParentCategoryModel> response) {
                shimmerRecycler.hideShimmerAdapter();
                if(!response.isSuccessful()){
                    Toast.makeText(OpenCategoryActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: "+response.code()+"  mes "+response.message());
                    return;
                }

                if(response.body() != null){
                    ParentCategoryModel parentCategoryModel = null;
                    try {
                        parentCategoryModel = response.body();
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: "+e.toString());
                        Toast.makeText(OpenCategoryActivity.this, "Error parsing response.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String content = "";
                    if(parentCategoryModel.getResult().equals("success")){
                        Log.d(TAG, "onResponse: parentcategory fetch success");
                        if(parentCategoryModel.getData()!=null && parentCategoryModel.getData().size()>0){
                            Log.d(TAG, "onResponse: parentcategory size "+ parentCategoryModel.getData().size());

                            open_cat_adapter.updateItems(parentCategoryModel.getData());
                            hideErrorMessage();
//                            Drawable horizontalDivider = ContextCompat.getDrawable(OpenCategoryActivity.this, R.drawable.line_divider_orange);
//                            Drawable verticalDivider = ContextCompat.getDrawable(OpenCategoryActivity.this, R.drawable.line_divider_orange);
//                            try {
//                                open_category_view.addItemDecoration(new GridDividerItemDecoration(horizontalDivider, verticalDivider, 4));
//                            } catch (Exception e) {
//                                Log.d(TAG, "onResponse: item decorator "+e.toString());
//                            }
                        } else {
                            showErrorMessage("There are no further categories");
                        }

                    } else {
                        showErrorMessage(parentCategoryModel.getMsg());
                        Log.d(TAG, "onResponse: "+parentCategoryModel.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<ParentCategoryModel> call, Throwable t) {
                shimmerRecycler.hideShimmerAdapter();
                showErrorMessage(t.getLocalizedMessage());
                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
            }
        });

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
                    startActivity(new Intent(OpenCategoryActivity.this, SignUpActivity.class));
                }
            });

            slider_two_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(OpenCategoryActivity.this, SignInActivity.class));

                }
            });

        }
    }


    private void showErrorMessage(String s) {
        error_msg_layout.setVisibility(View.VISIBLE);
        error_msg.setText(s);
        open_category_layout.setVisibility(View.GONE);
//        sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
    }

    private void hideErrorMessage() {
        error_msg_layout.setVisibility(View.GONE);
        open_category_layout.setVisibility(View.VISIBLE);
//        sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
    }

}
